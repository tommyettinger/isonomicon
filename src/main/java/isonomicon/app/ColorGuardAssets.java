package isonomicon.app;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.utils.*;
import com.github.tommyettinger.anim8.AnimatedGif;
import com.github.tommyettinger.anim8.AnimatedPNG;
import com.github.tommyettinger.anim8.Dithered;
import com.github.tommyettinger.anim8.PaletteReducer;
import com.github.tommyettinger.ds.IntObjectMap;
import isonomicon.io.LittleEndianDataInputStream;
import isonomicon.io.extended.VoxIOExtended;
import isonomicon.io.extended.VoxModel;
import isonomicon.physical.EffectGenerator;
import isonomicon.physical.Stuff;
import isonomicon.physical.Tools3D;
import isonomicon.visual.Coloring;
import isonomicon.visual.ShaderUtils;
import isonomicon.visual.SpecialRenderer;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class ColorGuardAssets extends ApplicationAdapter {
    public static boolean DIVERSE = false;
    public static boolean ATTACKS = true;
    public static boolean EXPLOSION = false;

    public static final int SCREEN_WIDTH = 512;//640;
    public static final int SCREEN_HEIGHT = 512;//720;
    private SpecialRenderer renderer;
    private VoxModel voxels, head;
    private VoxModel[] frames = new VoxModel[8];
    private String name;
    private String[] armies;
    private PixmapIO.PNG png;
    private AnimatedGif gif;
    private AnimatedPNG apng;
    private SpriteBatch batch;
    private Texture palette;
    public ColorGuardAssets() {
        armies = new String[]{
                "Dark",
                "White",
                "Red",
                "Orange",
                "Yellow",
                "Green",
                "Blue",
                "Purple",
        };
//        if (!new File("specialized/b/vox/color_guard/" + inputs[0]).exists()) {
//            System.out.println("File not found: specialized/b/vox/color_guard/" + inputs[0]);
//            System.exit(0);
//        }
        try {
            head = VoxIOExtended.readVox(new LittleEndianDataInputStream(new FileInputStream("specialized/b/vox/color_guard/human/Head.vox")));
        }
        catch (FileNotFoundException ignored){
            System.out.println("Head model not found; this was run from the wrong path. Exiting.");
            System.exit(0);
        }
    }
    @Override
    public void create() {
//        if (inputs == null) Gdx.app.exit();
        palette = new Texture(Gdx.files.local("assets/palettes/b/ColorGuardMasterPalette.png"));
//        palettes = new Texture[]{
//                new Texture(Gdx.files.local("assets/palettes/b/ColorGuardBaseDark.png")),
//                new Texture(Gdx.files.local("assets/palettes/b/ColorGuardBaseWhite.png")),
//                new Texture(Gdx.files.local("assets/palettes/b/ColorGuardBaseRed.png")),
//                new Texture(Gdx.files.local("assets/palettes/b/ColorGuardBaseOrange.png")),
//                new Texture(Gdx.files.local("assets/palettes/b/ColorGuardBaseYellow.png")),
//                new Texture(Gdx.files.local("assets/palettes/b/ColorGuardBaseGreen.png")),
//                new Texture(Gdx.files.local("assets/palettes/b/ColorGuardBaseBlue.png")),
//                new Texture(Gdx.files.local("assets/palettes/b/ColorGuardBasePurple.png")),
//        };

        ShaderProgram indexShader = new ShaderProgram(ShaderUtils.stuffSelectVertex, ShaderUtils.stuffSelectFragment);
        if (!indexShader.isCompiled()) throw new GdxRuntimeException("Error compiling shader: " + indexShader.getLog());
        batch = new SpriteBatch(256, indexShader);

        long startTime = TimeUtils.millis();
        png = new PixmapIO.PNG();
        png.setCompression(2); // we are likely to compress these with something better, like oxipng.
        gif = new AnimatedGif();
        apng = new AnimatedPNG();
        apng.setCompression(2);
        gif.setDitherAlgorithm(Dithered.DitherAlgorithm.NEUE);
        gif.palette = new PaletteReducer(Coloring.YAM3, Gdx.files.local("assets/Yam3Preload.dat").readBytes());
//        gif.palette = new PaletteReducer(Coloring.TATER255, Gdx.files.local("assets/TaterPreload.dat").readBytes());
//        gif.palette = new PaletteReducer(Coloring.TETRA256, Gdx.files.local("assets/TetraPreload.dat").readBytes());
//        gif.palette = new PaletteReducer(Coloring.BETSY256, Gdx.files.local("assets/BetsyPreload.dat").readBytes());
        gif.palette.setDitherStrength(0.625f);
        FrameBuffer fb = new FrameBuffer(Pixmap.Format.RGBA8888, Gdx.graphics.getBackBufferWidth(), Gdx.graphics.getBackBufferHeight(), false);
        // many skin and hair colors
        if(DIVERSE)
        {
            Gdx.files.local("out/color_guard/animated_diverse/" + name + '/').mkdirs();
            for (int n = 0; n < ColorGuardData.units.length; n++) {
                ColorGuardData.Unit unit = ColorGuardData.units[n];
                String s = unit.name;
                System.out.println("Rendering " + s);
                load("specialized/b/vox/color_guard/" + s);
                Pixmap pixmap;
                Array<Pixmap> pm = new Array<>(32 * armies.length);
                pm.setSize(32 * armies.length);
                VoxModel original = voxels.copy();
                for (int i = 0; i < 4; i++) {
                    voxels = original.copy();
                    for (int f = 0; f < 4; f++) {
                        for (int j = 0; j < voxels.grids.size(); j++) {
                            Stuff.evolve(Stuff.STUFFS_B, voxels.grids.get(j), f);
                        }
                        pixmap = renderer.drawModelSimple(voxels, i * 0.25f, 0f, 0f, f, 0, 0, 0);
                        Texture t = new Texture(pixmap.getWidth(), pixmap.getHeight(), Pixmap.Format.RGBA8888);
                        t.draw(renderer.palettePixmap, 0, 0);
                        for (int look = 0, lk = 0; look < 201; look+=8, lk++) {
                            for (int j = 0; j < armies.length; j++) {
                                fb.begin();
                                palette.bind(1);
                                ScreenUtils.clear(Color.CLEAR);
                                batch.begin();

                                indexShader.setUniformi("u_texPalette", 1);
                                Gdx.gl.glActiveTexture(GL20.GL_TEXTURE0);
                                batch.setColor((look + j) / 255f, 0.5f, 0.5f, 1f);

                                batch.draw(t, 0, t.getHeight(), t.getWidth(), -t.getHeight());
                                batch.end();
                                pixmap = Pixmap.createFromFrameBuffer(0, 0, t.getWidth(), t.getHeight());
                                fb.end();
                                if(lk == j * 3) {
                                    pm.set(j * 32 + i * 8 + f, pixmap);
                                    pm.set(j * 32 + i * 8 + f + 4, pixmap);
                                }
                                try {
                                    png.write(Gdx.files.local("out/color_guard/" + armies[j] + "/" + name + '/' + armies[j] + "_look" + lk + '_' + name + "_angle" + i + "_" + f + ".png"), pixmap);
                                    if(look + j == 0)
                                        png.write(Gdx.files.local("out/color_guard/lab/" + name + '/' + name + "_angle" + i + "_" + f + ".png"), renderer.palettePixmap);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                        t.dispose();
//                png8.write(Gdx.files.local("out/" + name + '/' + name + "_angle" + i + ".png"), p, false, true);
                    }
                }
//                gif.palette.analyze(pm);
                gif.write(Gdx.files.local("out/color_guard/animated_diverse/" + name + '/' + name + ".gif"), pm, 8);
                apng.write(Gdx.files.local("out/color_guard/animated_diverse/" + name + '/' + name + ".png"), pm, 8);
                for (Pixmap pix : pm) {
                    if (!pix.isDisposed())
                        pix.dispose();
                }
            }
        }
        // just a single skin/hair combination
        else {
            EACH_INPUT:
            for (int n = 0; n < ColorGuardData.units.length; n++) {
                ColorGuardData.Unit unit = ColorGuardData.units[n];
                String s = unit.name;
                System.out.println("Rendering " + s);
                load("specialized/b/vox/color_guard/" + s + ".vox");
                Pixmap pixmap;
                Array<Pixmap> pm = new Array<>(32 * armies.length);
                pm.setSize(32 * armies.length);
                VoxModel original = voxels.copy();
                for (int i = 0; i < 4; i++) {
                    voxels = original.copy();
                    for (int f = 0; f < 4; f++) {
                        for (int j = 0; j < voxels.grids.size(); j++) {
                            Stuff.evolve(Stuff.STUFFS_B, voxels.grids.get(j), f);
                        }
                        pixmap = renderer.drawModelSimple(voxels, i * 0.25f, 0f, 0f, f, 0, 0, 0);
                        Texture t = new Texture(pixmap.getWidth(), pixmap.getHeight(), Pixmap.Format.RGBA8888);
                        t.draw(renderer.palettePixmap, 0, 0);
                        int look = 0;
                        for (int j = 0; j < armies.length; j++) {
                            fb.begin();
                            palette.bind(1);
                            ScreenUtils.clear(Color.CLEAR);
                            batch.begin();

                            indexShader.setUniformi("u_texPalette", 1);
                            Gdx.gl.glActiveTexture(GL20.GL_TEXTURE0);
                            batch.setColor((look + j) / 255f, 0.5f, 0.5f, 1f);

                            batch.draw(t, 0, t.getHeight(), t.getWidth(), -t.getHeight());
                            batch.end();
                            pixmap = Pixmap.createFromFrameBuffer(0, 0, t.getWidth(), t.getHeight());
                            fb.end();
                            pm.set(j * 32 + i * 8 + f, pixmap);
                            pm.set(j * 32 + i * 8 + f + 4, pixmap);
                            try {
                                png.write(Gdx.files.local("out/color_guard/" + armies[j] + "/" + name + '/' + armies[j] + "_look0_" + name + "_angle" + i + "_" + f + ".png"), pixmap);
                                if (look + j == 0)
                                    png.write(Gdx.files.local("out/color_guard/lab/" + name + '/' + name + "_angle" + i + "_" + f + ".png"), renderer.palettePixmap);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        t.dispose();
//                png8.write(Gdx.files.local("out/" + name + '/' + name + "_angle" + i + ".png"), p, false, true);
                    }
                }
//                gif.palette.analyze(pm);
                gif.write(Gdx.files.local("out/color_guard/animated/" + name + '/' + name + "_Primary" + ".gif"), pm, 8);
                apng.write(Gdx.files.local("out/color_guard/animated/" + name + '/' + name + "_Primary" + ".png"), pm, 8);
                for (Pixmap pix : pm) {
                    if (!pix.isDisposed())
                        pix.dispose();
                }
                if (ATTACKS) {
                    pm.clear();
                    pm.setSize(32 * armies.length);
                    String attack = unit.primary, ps = "_Primary";
                    boolean pose = unit.primaryPose;
                    for (int which = 0; which < 2; which++) {
                        if(attack == null) continue EACH_INPUT;
                        if (!EffectGenerator.KNOWN_EFFECTS.containsKey(attack)) {
                            continue;
                        }
                        if (pose) {
                            load("specialized/b/vox/color_guard/" + unit.name + "_Firing.vox");
                            name = unit.name;
                            original = voxels.copy();
                        } else {
                            load("specialized/b/vox/color_guard/" + unit.name + ".vox");
                            name = unit.name;
                            original = voxels.copy();
                        }
                        for (int i = 0; i < 4; i++) {
                            frames[0] = original.copy();
                            for (int f = 0; f < frames.length; f++) {
                                if (f > 0) frames[f] = frames[f - 1].copy();
                                for (int j = 0; j < frames[f].grids.size(); j++) {
                                    Stuff.evolve(Stuff.STUFFS_B, frames[f].grids.get(j), f);
                                }
                            }
                            EffectGenerator.Effect effect = EffectGenerator.KNOWN_EFFECTS.get(attack);
                            if (effect == null) continue EACH_INPUT;
                            VoxModel[] anim = effect.runEffect(frames, which);
                            if (anim == null) continue EACH_INPUT;
                            else frames = anim;

                            for (int f = 0; f < frames.length; f++) {
                                pixmap = renderer.drawModelSimple(frames[f], i * 0.25f, 0f, 0f, f, 0, 0, 0);
                                Texture t = new Texture(pixmap.getWidth(), pixmap.getHeight(), Pixmap.Format.RGBA8888);
                                t.draw(renderer.palettePixmap, 0, 0);
                                int look = 0;
                                for (int j = 0; j < armies.length; j++) {
                                    fb.begin();
                                    palette.bind(1);
                                    ScreenUtils.clear(Color.CLEAR);
                                    batch.begin();

                                    indexShader.setUniformi("u_texPalette", 1);
                                    Gdx.gl.glActiveTexture(GL20.GL_TEXTURE0);
                                    batch.setColor((look + j) / 255f, 0.5f, 0.5f, 1f);

                                    batch.draw(t, 0, t.getHeight(), t.getWidth(), -t.getHeight());
                                    batch.end();
                                    pixmap = Pixmap.createFromFrameBuffer(0, 0, t.getWidth(), t.getHeight());
                                    fb.end();
                                    pm.set(j * 32 + i * 8 + f, pixmap);
                                    try {
                                        png.write(Gdx.files.local("out/color_guard/" + armies[j] + "/" + name + '/' + armies[j] + "_look0_" + name + "_" + "Primary" + "_angle" + i + "_" + f + ".png"), pixmap);
                                        if (look + j == 0)
                                            png.write(Gdx.files.local("out/color_guard/lab/" + name + '/' + name + "_" + "Primary" + "_angle" + i + "_" + f + ".png"), renderer.palettePixmap);
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                                t.dispose();
                                byte[][][] g = frames[f].grids.get(0);
                                for (int j = 1; j < frames[f].grids.size(); j++) {
                                    Tools3D.deepCopyInto(g, frames[f].grids.get(j));
                                }
                                Gdx.files.local("out/temp/" + name + '/').mkdirs();

//                            VoxIOExtended.writeVOX("out/temp/" + name + '/' + name + "_Machine_Gun_angle" + i + "_" + f + ".vox", g, Coloring.BETTS64, Stuff.MATERIALS_B);

//                png8.write(Gdx.files.local("out/" + name + '/' + name + "_angle" + i + ".png"), p, false, true);
                            }
                        }
//                gif.palette.analyze(pm);
                        gif.write(Gdx.files.local("out/color_guard/animated/" + name + '/' + name + ps + ".gif"), pm, 8);
                        apng.write(Gdx.files.local("out/color_guard/animated/" + name + '/' + name + ps + ".png"), pm, 8);
                        for (Pixmap pix : pm) {
                            if (!pix.isDisposed())
                                pix.dispose();
                        }
                        attack = unit.secondary;
                        pose = unit.secondaryPose;
                        ps = "_Secondary";

                    }
                }
            }
        }
        if(EXPLOSION)
        {
            voxels = new VoxModel();
            SpecialRenderer.shrink = 2;
            renderer = new SpecialRenderer(120, Stuff.STUFFS_B);
            renderer.palette(Coloring.BETTS64);
            renderer.saturation(0f);
            voxels.links.clear();
            IntObjectMap<float[]> links = new IntObjectMap<>(1);
            links.put(-1, new float[]{0, 0, 0});
            voxels.links.add(links);
            name = "Explosion";
            byte[][][] fireSeed = new byte[120][120][120];
            float xx = -9.5f;
            for (int x = 50; x < 69; x++, xx++) {
                float yy = -9.5f;
                for (int y = 50; y < 69; y++, yy++) {
                    float zz = 0f;
                    for (int z = 0; z < 9; z++, zz++) {
                        float dist = xx * xx + yy * yy + zz * zz;
                        if (dist <= 24f)
                            fireSeed[x][y][z] = 115;
                        else if (dist < 64f)
                            fireSeed[x][y][z] = 114;
                    }
                }
            }
//            byte[][][] fireSeed = new byte[60][60][60];
//            float xx = -4.5f;
//            for (int x = 25; x < 34; x++, xx++) {
//                float yy = -4.5f;
//                for (int y = 25; y < 34; y++, yy++) {
//                    float zz = 0f;
//                    for (int z = 0; z < 5; z++, zz++) {
//                        float dist = xx * xx + yy * yy + zz * zz;
//                        if (dist <= 6f)
//                            fireSeed[x][y][z] = 115;
//                        else if (dist < 16f)
//                            fireSeed[x][y][z] = 114;
//                    }
//                }
//            }
            Stuff.evolve(Stuff.STUFFS_B, fireSeed, -1);
            byte[][][][] explosion = EffectGenerator.fireballAnimation(fireSeed, 12, 3, 0);
            Pixmap pixmap;
            Array<Pixmap> pm = new Array<>(4 * explosion.length);

            for (int i = 0; i < 4; i++) {
                for (int f = 0; f < explosion.length; f++) {
                    voxels.grids.clear();
                    voxels.grids.add(explosion[f]);
//                    Stuff.evolve(Stuff.STUFFS_B, fireSeed, f);
//                    voxels.grids.add(fireSeed);
                    pixmap = renderer.drawModelSimple(voxels, i * 0.25f, 0f, 0f, f, 0, 0, 0);
                    Texture t = new Texture(pixmap.getWidth(), pixmap.getHeight(), Pixmap.Format.RGBA8888);
                    t.draw(renderer.palettePixmap, 0, 0);
                    fb.begin();
                    palette.bind(1);
                    ScreenUtils.clear(Color.CLEAR);
                    batch.begin();

                    indexShader.setUniformi("u_texPalette", 1);
                    Gdx.gl.glActiveTexture(GL20.GL_TEXTURE0);
                    batch.setColor(0f, 0.5f, 0.5f, 1f);

                    batch.draw(t, 0, t.getHeight(), t.getWidth(), -t.getHeight());
                    batch.end();
                    pixmap = Pixmap.createFromFrameBuffer(0, 0, t.getWidth(), t.getHeight());
                    fb.end();
                    pm.add(pixmap);
                    try {
                        png.write(Gdx.files.local("out/color_guard/effects/" + name + '/' + name + "_angle" + i + "_" + f + ".png"), pixmap);
                        png.write(Gdx.files.local("out/color_guard/lab/" + name + '/' + name + "_angle" + i + "_" + f + ".png"), renderer.palettePixmap);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    t.dispose();
                }
            }
            gif.write(Gdx.files.local("out/color_guard/animated/" + name + '/' + name + ".gif"), pm, 8);
            apng.write(Gdx.files.local("out/color_guard/animated/" + name + '/' + name + ".png"), pm, 8);
            for (Pixmap pix : pm) {
                if(!pix.isDisposed())
                    pix.dispose();
            }
        }
        fb.dispose();
        System.out.println("Finished in " + TimeUtils.timeSinceMillis(startTime) * 0.001 + " seconds.");
        Gdx.app.exit();
    }

    @Override
    public void render() {
    }


    public static void main(String[] arg) {
        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
        config.setTitle("Writing Color Guard assets");
        config.setWindowedMode(SCREEN_WIDTH, SCREEN_HEIGHT);
        config.setIdleFPS(10);
        config.useVsync(true);
        config.setResizable(false);
        config.disableAudio(true);
        final ColorGuardAssets app = new ColorGuardAssets();
        new Lwjgl3Application(app, config);
    }

    public void load(String name) {
        try {
            //// loads a file by its full path, which we get via a command-line arg
            voxels = VoxIOExtended.readVox(new LittleEndianDataInputStream(new FileInputStream(name)));
            if(voxels == null) {
                voxels = new VoxModel();
                return;
            }
//            voxels = Tools3D.scaleAndSoak(voxels);
//            voxels = Tools3D.soak(voxels);
            voxels.mergeWith(head);
            int nameStart = Math.max(name.lastIndexOf('/'), name.lastIndexOf('\\')) + 1;
            this.name = name.substring(nameStart, name.indexOf('.', nameStart));
//            renderer = new NextRenderer(voxels.length, QUALITY);
//            renderer = new AngledRenderer(voxels.length);
            SpecialRenderer.shrink = 2;
            renderer = new SpecialRenderer(voxels.grids.get(0).length, Stuff.STUFFS_B);
            renderer.palette(Coloring.BETTS64);
            renderer.saturation(0f);
            
        } catch (FileNotFoundException e) {
            voxels = new VoxModel();
        }
    }
}
