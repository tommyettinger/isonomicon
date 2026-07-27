package isonomicon.app;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Gdx2DPixmap;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.TimeUtils;
import com.github.tommyettinger.anim8.AnimatedGif;
import com.github.tommyettinger.anim8.FastPNG;
import com.github.tommyettinger.anim8.QualityPalette;
import com.github.tommyettinger.ds.ObjectObjectOrderedMap;
import isonomicon.io.LittleEndianDataInputStream;
import isonomicon.io.extended.VoxIOExtended;
import isonomicon.io.extended.VoxModel;
import isonomicon.physical.Stuff;
import isonomicon.physical.Tools3D;
import isonomicon.visual.Coloring;
import isonomicon.visual.ShaderUtils;
import isonomicon.visual.SpecialRenderer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.nio.ByteBuffer;
import java.util.ArrayList;

public class AttitudeAssets extends ApplicationAdapter {
    public static final boolean TURNTABLE = true;

    public static final int SCREEN_WIDTH = 512;//640;
    public static final int SCREEN_HEIGHT = 512;//720;
    public static final String ANIMATION_PATH = "out/attitude/animated/";
    public static final String ANIMATION_PATH_PALETTE = "out/attitude/animated_snuggly/";
    private SpecialRenderer renderer;
    private VoxModel voxels;
    private String name;
    private ObjectObjectOrderedMap<String, String[]> inputs;
    private FastPNG png;
    private AnimatedGif gif;
//    private AnimatedPNG apng;
    private QualityPalette analyzed, fixed;
    private SpriteBatch batch;
    private Texture palette;
    public AttitudeAssets() {
        VoxIOExtended.GENERAL = true;
        SpecialRenderer.SHADOW_INDEX = (byte) 2;
        SpecialRenderer.shrink = 2;
        SpecialRenderer.distortHX = 2;
        SpecialRenderer.distortHY = 1;
        SpecialRenderer.distortVX = 1;
        SpecialRenderer.distortVY = 1;
        SpecialRenderer.distortVZ = 2;

        System.out.println("INVALID ARGUMENTS. Please supply space-separated absolute paths to .vox models, or use the .bat file.");
        inputs = ObjectObjectOrderedMap.with(
//                "c/attitude/Priest.vox", new String[]{
//                                "palettes/c/yam4mod.png", "Humble_Priest",
//                                "palettes/b/ColorGuardBaseWhite.png", "Light_Priest",
//                                "palettes/b/ColorGuardBaseRed.png", "War_Priest",
//                        },
//                "c/attitude/Man.vox", new String[]{
//                        "palettes/c/yam4mod.png", "Man_In_Brown",
//                        "palettes/b/ColorGuardBaseWhite.png", "Man_In_White",
//                        "palettes/b/ColorGuardBaseRed.png", "Man_In_Red",
//                        "palettes/b/ColorGuardBaseGreen.png", "Man_In_Green",
//                        "palettes/b/ColorGuardBaseBlue.png", "Man_In_Blue",
//                },
                "c/attitude/Earth_Voxel.vox", new String[]{
                        "palettes/c/yam4mod.png", "Earth_Voxel",
//                "c/attitude/Hunter.vox", new String[]{
//                        "palettes/c/yam4mod.png", "Hunter",
//                        "palettes/b/ColorGuardBaseBlue.png", "Sea_Hunter",
//                },
//                "c/attitude/A27.vox", new String[]{
//                        "palettes/b/ColorGuardBaseDark.png", "Night_Warrior",
//                        "palettes/b/ColorGuardBaseYellow.png", "Mercenary_Warrior",
//                        "palettes/b/ColorGuardBaseRed.png", "Crimson_Warrior",
//                },
//                "c/attitude/A28.vox", new String[]{
//                        "palettes/b/ColorGuardBaseDark.png", "Death_Knight",
//                        "palettes/b/ColorGuardBaseYellow.png", "Mercenary_Knight",
//                        "palettes/b/ColorGuardBaseGreen.png", "Verdant_Knight",
//                        "palettes/b/ColorGuardBaseWhite.png", "Paladin",
//                },
//                "c/attitude/A29.vox", new String[]{
//                        "palettes/b/ColorGuardBaseDark.png", "Blackguard",
//                        "palettes/b/ColorGuardBaseRed.png", "Furious_Hoplite",
//                        "palettes/b/ColorGuardBasePurple.png", "Mystery_Spearman",
//                        "palettes/b/ColorGuardBaseWhite.png", "Holy_Lancer",
//                },
//                "c/attitude/A30.vox", new String[]{
//                        "palettes/b/ColorGuardBaseDark.png", "Dreadnought",
//                        "palettes/b/ColorGuardBaseBlue.png", "Defensive_Lineman",
//                        "palettes/b/ColorGuardBaseGreen.png", "Warden",
//                        "palettes/b/ColorGuardBaseWhite.png", "Heavy_Bouncer",
//                },
//                "c/attitude/A31.vox", new String[]{
//                        "palettes/b/ColorGuardBaseDark.png", "Terror_Knight",
//                        "palettes/b/ColorGuardBasePurple.png", "Eldritch_Knight",
//                        "palettes/b/ColorGuardBaseGreen.png", "Horned_Guardian",
//                        "palettes/b/ColorGuardBaseRed.png", "Ruined_Destroyer",
                }
                );
//            inputs = new String[]{
//                    "b/vox/odyssey/Assassin_Dagger.vox", "palettes/b/TanClothDarkSkin.png",
//                    "b/vox/odyssey/Noble_Knife.vox", "palettes/b/TanClothDarkSkin.png",
//            };
//            inputs = new String[]{"b/vox/Figure.vox", "b/vox/Tree.vox"};
//            inputs = new String[]{"b/vox/Figure_Split.vox", "palettes/b/TanClothDarkSkin.png"};
//            inputs = new String[]{"b/vox/Damned.vox", "palettes/b/CherrySkinDarkCloth.png"};
//            inputs = new String[]{"b/vox/Direction_Cube.vox", "palettes/b/TanClothDarkSkin.png"};
//            inputs = new String[]{"b/vox/Lomuk.vox", "palettes/b/BlueFurCyanCrystal.png"};
//            inputs = new String[]{"b/vox/Lomuk.vox", "palettes/b/BlueFurCyanCrystal.png",
//                    "b/vox/Damned.vox", "palettes/b/CherrySkinDarkCloth.png",
//                    "b/vox/Figure.vox", "palettes/b/TanClothDarkSkin.png",
//                    "b/vox/Figure_Split.vox", "palettes/b/TanClothDarkSkin.png"};
        if (!new File("specialized/" + inputs.keyAt(0)).exists()) {
            System.out.println("File not found: specialized/" + inputs.keyAt(0));
            System.exit(0);
        }
    }
    public static Pixmap createFromFrameBuffer(int x, int y, int w, int h) {
        Gdx.gl.glPixelStorei(GL20.GL_PACK_ALIGNMENT, 1);
        Pixmap pixmap = new Pixmap(new Gdx2DPixmap(w, h, Gdx2DPixmap.GDX2D_FORMAT_RGBA8888));
        ByteBuffer pixels = pixmap.getPixels();
        Gdx.gl.glReadPixels(x, y, w, h, GL20.GL_RGBA, GL20.GL_UNSIGNED_BYTE, pixels);
        return pixmap;
    }

    @Override
    public void create() {
        if (inputs == null) Gdx.app.exit();

        ShaderProgram indexShader = new ShaderProgram(ShaderUtils.stuffSelectVertex, ShaderUtils.stuffSelectFragment);
        if (!indexShader.isCompiled()) throw new GdxRuntimeException("Error compiling shader: " + indexShader.getLog());
        batch = new SpriteBatch(256, indexShader);

        long startTime = TimeUtils.millis();
//        Gdx.files.local("out/vox/").mkdirs();
        png = new FastPNG();
        png.setCompression(2); // we are likely to compress these with something better, like oxipng.
        png.setFlipY(false);
//        png8 = new PNG8();
        gif = new AnimatedGif();
        gif.setDitherAlgorithm(AppConfig.DITHER);
        gif.setFlipY(false);
//        apng = new AnimatedPNG();
//        apng.setCompression(2);
//        apng.setFlipY(false);
        gif.setDitherStrength(AppConfig.STRENGTH);
        fixed =
                // Snuggly, unless analyze() is called
                new QualityPalette();
        gif.palette = analyzed = new QualityPalette();

        FrameBuffer fb = new FrameBuffer(Pixmap.Format.RGBA8888, Gdx.graphics.getBackBufferWidth(), Gdx.graphics.getBackBufferHeight(), false);
        for (int n = 0; n < inputs.size(); n++) {
            String s = inputs.keyAt(n);
            String[] outputs = inputs.getAt(n);
            System.out.println("Rendering " + s);
            load("specialized/" + s);
            Texture t = new Texture(renderer.palettePixmap.getWidth(), renderer.palettePixmap.getHeight(), Pixmap.Format.RGBA8888);
            Pixmap pixmap;
            ObjectObjectOrderedMap<String, Array<Pixmap>> pmm = new ObjectObjectOrderedMap<>(outputs.length>>1);
            ArrayList<byte[][][]> original = new ArrayList<>(voxels.grids.size());
            for (int i = 0; i < voxels.grids.size(); i++) {
                original.add(Tools3D.deepCopy(voxels.grids.get(i)));
            }
            for (int i = 0; i < 4; i++) {
                voxels.grids.clear();
                for (int j = 0; j < original.size(); j++) {
                    voxels.grids.add(Tools3D.deepCopy(original.get(j)));
                }
                for (int f = 0; f < 4; f++) {
                    for (int j = 0; j < voxels.grids.size(); j++) {
                        Stuff.evolve(Stuff.STUFFS_C, voxels.grids.get(j), f);
                    }
                    renderer.drawModelSimple(voxels, i * 0.25f + 0.125f, 0f, 0f, f, 0, 0, 0);
//                    renderer.drawModelSimple(voxels, i * 0.25f + Tools3D.wiggle(f) * 0.0125f, 0f, 0f, f, 0, 0, 0);
                    t.draw(renderer.palettePixmap, 0, 0);
                    for (int p = 0; p < outputs.length-1; p+=2) {
                        String paletteName = outputs[p], output = outputs[p+1];
                        Array<Pixmap> pm;
                        if(!pmm.containsKey(output))
                            pmm.put(output, pm = new Array<>(128));
                        else
                            pm = pmm.get(output);
                        palette = new Texture(Gdx.files.local("assets/" + paletteName));
                        fb.begin();
                        palette.bind(1);
                        ScreenUtils.clear(1f, 1f, 1f, 0f);
                        batch.begin();

                        indexShader.setUniformi("u_texPalette", 1);
                        Gdx.gl.glActiveTexture(GL20.GL_TEXTURE0);
                        batch.setColor(0f, 0.5f, 0.5f, 1f);

                        batch.draw(t, 0, 0, t.getWidth(), t.getHeight());
                        batch.end();

//                    pixmap = Pixmap.createFromFrameBuffer(0, 0, t.getWidth(), t.getHeight());
                        //// The above is equivalent to the following, but the above also fills the pixmap.
                        pixmap = createFromFrameBuffer(0, 0, t.getWidth(), t.getHeight());

                        fb.end();
                        pm.add(pixmap);
                        png.write(Gdx.files.local("out/attitude/png/" + output + '/' + output + "_angle" + i + "_" + f + ".png"), pixmap);
                        if (p == 0) {
                            png.write(Gdx.files.local("out/attitude/lab/" + name + '/' + name + "_angle" + i + "_" + f + ".png"), renderer.palettePixmap);
                        }
                        palette.dispose();
                    }
                }
                for(Array<Pixmap> p : pmm.values())
                    p.insertRange(p.size - 4, 4);
            }
            for (int p = 1; p < outputs.length; p+=2) {
                String output = outputs[p];
                Array<Pixmap> pm = pmm.get(output);
//                apng.write(Gdx.files.local(ANIMATION_PATH + output + '/' + output + ".png"), pm, 8);
                SpecialRenderer.monoAlpha(pm);
                analyzed.analyze(pm, 75.0, 256);
                gif.palette = analyzed;
                gif.write(Gdx.files.local(ANIMATION_PATH + output + '/' + output + ".gif"), pm, 8);
                gif.palette = fixed;
                gif.write(Gdx.files.local(ANIMATION_PATH_PALETTE + output + '/' + output + ".gif"), pm, 8);
                for (Pixmap pix : pm) {
                    if (!pix.isDisposed())
                        pix.dispose();
                }
                pm.clear();
            }
            if(TURNTABLE) {
                voxels.grids.clear();
                for (int j = 0; j < original.size(); j++) {
                    voxels.grids.add(Tools3D.deepCopy(original.get(j)));
                }
                for (int i = 0; i < 128; i++) {
                    for (int j = 0; j < voxels.grids.size(); j++) {
                        Stuff.evolve(Stuff.STUFFS_C, voxels.grids.get(j), i);
                    }
                    renderer.drawModelSimple(voxels, i * 0x1p-7f + 0.125f, 0f, 0f, i, 0, 0, 0);
                    t.draw(renderer.palettePixmap, 0, 0);
                    for (int p = 0; p < outputs.length - 1; p += 2) {
                        String paletteName = outputs[p], output = outputs[p + 1];
                        Array<Pixmap> pm;
                        if (!pmm.containsKey(output))
                            pmm.put(output, pm = new Array<>(128));
                        else
                            pm = pmm.get(output);
                        palette = new Texture(Gdx.files.local("assets/" + paletteName));
                        fb.begin();
                        palette.bind(1);
                        ScreenUtils.clear(1f, 1f, 1f, 0f);
                        batch.begin();

                        indexShader.setUniformi("u_texPalette", 1);
                        Gdx.gl.glActiveTexture(GL20.GL_TEXTURE0);
                        batch.setColor(0f, 0.5f, 0.5f, 1f);

                        batch.draw(t, 0, 0, t.getWidth(), t.getHeight());
                        batch.end();
                        pixmap = createFromFrameBuffer(0, 0, t.getWidth(), t.getHeight());
                        fb.end();
                        pm.add(pixmap);
                        palette.dispose();
                    }
                }
                for (int p = 1; p < outputs.length; p+=2) {
                    String output = outputs[p];
                    Array<Pixmap> pm = pmm.get(output);
//                    apng.write(Gdx.files.local(ANIMATION_PATH + output + '/' + output + "_Turntable.png"), pm, 24);
                    SpecialRenderer.monoAlpha(pm);
                    analyzed.analyze(pm, 75.0, 256);
                    gif.palette = analyzed;
                    gif.write(Gdx.files.local(ANIMATION_PATH + output + '/' + output + "_Turntable.gif"), pm, 24);
                    gif.palette = fixed;
                    gif.write(Gdx.files.local(ANIMATION_PATH_PALETTE + output + '/' + output + "_Turntable.gif"), pm, 24);
                    for (Pixmap pix : pm) {
                        if (!pix.isDisposed())
                            pix.dispose();
                    }
                    pm.clear();
                }
            }
            t.dispose();
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
        config.setTitle("Writing Test");
        config.setWindowedMode(SCREEN_WIDTH, SCREEN_HEIGHT);
        config.setIdleFPS(10);
        config.useVsync(true);
        config.setResizable(false);
        config.disableAudio(true);
        final AttitudeAssets app = new AttitudeAssets();
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
            int nameStart = Math.max(name.lastIndexOf('/'), name.lastIndexOf('\\')) + 1;
            this.name = name.substring(nameStart, name.indexOf('.', nameStart));
            renderer = new SpecialRenderer(voxels.grids.get(0).length, Stuff.STUFFS_C);
            renderer.palette(Coloring.YAM4);
            renderer.saturation(0f);
        } catch (FileNotFoundException e) {
            voxels = new VoxModel();
        }
    }
}
