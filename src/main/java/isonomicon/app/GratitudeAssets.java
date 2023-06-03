package isonomicon.app;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.badlogic.gdx.graphics.Color;
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
import com.github.tommyettinger.anim8.*;
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

public class GratitudeAssets extends ApplicationAdapter {
    public static final int SCREEN_WIDTH = 512;//640;
    public static final int SCREEN_HEIGHT = 512;//720;
    private SpecialRenderer renderer;
    private VoxModel voxels;
    private String name;
    private ObjectObjectOrderedMap<String, String[]> inputs;
    private FastPNG png;
    private FastGif gif;
    private FastAPNG apng;
    private QualityPalette analyzed;
    private SpriteBatch batch;
    private Texture palette;
    public GratitudeAssets() {
        VoxIOExtended.GENERAL = true;
        System.out.println("INVALID ARGUMENTS. Please supply space-separated absolute paths to .vox models, or use the .bat file.");
        inputs = ObjectObjectOrderedMap.with("b/vox/gratitude/A24.vox", new String[]{
                                "palettes/b/ColorGuardBaseDark.png", "Dark_Priest",
                                "palettes/b/ColorGuardBaseWhite.png", "Light_Priest",
                                "palettes/b/ColorGuardBaseRed.png", "War_Priest",
                        },
                "b/vox/gratitude/A25.vox", new String[]{
                        "palettes/b/ColorGuardBaseDark.png", "Man_In_Black",
                        "palettes/b/ColorGuardBaseWhite.png", "Man_In_White",
                        "palettes/b/ColorGuardBaseRed.png", "Man_In_Red",
                        "palettes/b/ColorGuardBaseGreen.png", "Man_In_Green",
                        "palettes/b/ColorGuardBaseBlue.png", "Man_In_Blue",
                },
                "b/vox/gratitude/A26.vox", new String[]{
                        "palettes/b/ColorGuardBaseDark.png", "Dark_Hunter",
                        "palettes/b/ColorGuardBaseBlue.png", "Sea_Hunter",
                },
                "b/vox/gratitude/A27.vox", new String[]{
                        "palettes/b/ColorGuardBaseDark.png", "Night_Warrior",
                        "palettes/b/ColorGuardBaseYellow.png", "Mercenary_Warrior",
                        "palettes/b/ColorGuardBaseRed.png", "Crimson_Warrior",
                });
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
        gif = new FastGif();
        gif.setFlipY(false);
        apng = new FastAPNG();
        apng.setCompression(2);
        apng.setFlipY(false);
        gif.setDitherStrength(0.75f);
        gif.palette = analyzed = new QualityPalette();
        gif.setDitherStrength(0.5f);
//        aurora = new QualityPalette();
//        low = new QualityPalette(new int[]{
//                // Equpix15, by Night
//                0x00000000,0x523c4eff,0x2a2a3aff,0x3e5442ff,0x84545cff,0x38607cff,0x5c7a56ff,0x101024ff,
//                0xb27e56ff,0xd44e52ff,0x55a894ff,0x80ac40ff,0xec8a4bff,0x8bd0baff,0xffcc68ff,0xfff8c0ff,
//        });
        FrameBuffer fb = new FrameBuffer(Pixmap.Format.RGBA8888, Gdx.graphics.getBackBufferWidth(), Gdx.graphics.getBackBufferHeight(), false);
        for (int n = 0; n < inputs.size(); n++) {
            String s = inputs.keyAt(n);
            String[] outputs = inputs.getAt(n);
            System.out.println("Rendering " + s);
            load("specialized/" + s);
//            VoxIO.writeVOX("out/" + s, voxels, renderer.palette, VoxIO.lastMaterials);
//            load("out/"+s);
            Texture t = new Texture(renderer.palettePixmap.getWidth(), renderer.palettePixmap.getHeight(), Pixmap.Format.RGBA8888);
            Pixmap pixmap;
            ObjectObjectOrderedMap<String, Array<Pixmap>> pmm = new ObjectObjectOrderedMap<>(outputs.length>>1);
            ArrayList<byte[][][]> original = new ArrayList<>(voxels.grids.size());
            for (int i = 0; i < voxels.grids.size(); i++) {
                original.add(Tools3D.deepCopy(voxels.grids.get(i)));
            }
            for (int i = 0; i < 8; i++) {
                voxels.grids.clear();
                for (int j = 0; j < original.size(); j++) {
                    voxels.grids.add(Tools3D.deepCopy(original.get(j)));
                }
                for (int f = 0; f < 4; f++) {
                    for (int j = 0; j < voxels.grids.size(); j++) {
                        Stuff.evolve(Stuff.STUFFS_B, voxels.grids.get(j), f);
                    }
                    renderer.drawModelSimple(voxels, i * 0.125f, 0f, 0f, f, 0, 0, 0);
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
                        ScreenUtils.clear(Color.CLEAR);
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
                        png.write(Gdx.files.local("out/gratitude/" + output + '/' + output + "_angle" + i + "_" + f + ".png"), pixmap);
                        if (p == 0) {
                            png.write(Gdx.files.local("out/gratitude_lab/" + name + '/' + name + "_angle" + i + "_" + f + ".png"), renderer.palettePixmap);
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
                apng.write(Gdx.files.local("out/gratitude_animated/" + output + '/' + output + ".png"), pm, 8);
                SpecialRenderer.monoAlpha(pm);
                analyzed.analyze(pm, 75.0, 256);
                gif.palette = analyzed;
                gif.setDitherAlgorithm(Dithered.DitherAlgorithm.DODGY);
                gif.write(Gdx.files.local("out/gratitude_animated/" + output + '/' + output + ".gif"), pm, 8);
//            gif.palette = aurora;
//            gif.setDitherStrength(0.5f);
//            gif.write(Gdx.files.local("out/b/specializedAurora/" + name + '/' + name + ".gif"), pm, 8);
//            gif.palette = low;
//            gif.setDitherStrength(0.375f);
//            gif.write(Gdx.files.local("out/b/specializedLow/" + name + '/' + name + ".gif"), pm, 8);
                for (Pixmap pix : pm) {
                    if (!pix.isDisposed())
                        pix.dispose();
                }
                pm.clear();
            }
            if(true) {
                voxels.grids.clear();
                for (int j = 0; j < original.size(); j++) {
                    voxels.grids.add(Tools3D.deepCopy(original.get(j)));
                }
                for (int i = 0; i < 128; i++) {
                    for (int j = 0; j < voxels.grids.size(); j++) {
                        Stuff.evolve(Stuff.STUFFS_B, voxels.grids.get(j), i);
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
                        ScreenUtils.clear(Color.CLEAR);
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
                    apng.write(Gdx.files.local("out/gratitude_animated/" + output + '/' + output + "_Turntable.png"), pm, 24);
                    SpecialRenderer.monoAlpha(pm);
                    analyzed.analyze(pm, 75.0, 256);
                    gif.palette = analyzed;
                    gif.setDitherAlgorithm(Dithered.DitherAlgorithm.DODGY);
                    gif.write(Gdx.files.local("out/gratitude_animated/" + output + '/' + output + "_Turntable.gif"), pm, 24);
//            gif.palette = aurora;
//            gif.setDitherStrength(0.5f);
//            gif.write(Gdx.files.local("out/b/specializedAurora/" + name + '/' + name + ".gif"), pm, 8);
//            gif.palette = low;
//            gif.setDitherStrength(0.375f);
//            gif.write(Gdx.files.local("out/b/specializedLow/" + name + '/' + name + ".gif"), pm, 8);
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
        final GratitudeAssets app = new GratitudeAssets();
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
            renderer = new SpecialRenderer(voxels.grids.get(0).length, Stuff.STUFFS_B);
            renderer.palette(Coloring.BETTS64);
            renderer.saturation(0f);
        } catch (FileNotFoundException e) {
            voxels = new VoxModel();
        }
    }
}
