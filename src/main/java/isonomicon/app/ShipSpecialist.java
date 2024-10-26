package isonomicon.app;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.badlogic.gdx.files.FileHandle;
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
import com.github.tommyettinger.random.DistinctRandom;
import com.github.yellowstonegames.text.Language;
import isonomicon.io.LittleEndianDataInputStream;
import isonomicon.io.extended.VoxIOExtended;
import isonomicon.io.extended.VoxModel;
import isonomicon.physical.Stuff;
import isonomicon.physical.Tools3D;
import isonomicon.visual.Coloring;
import isonomicon.visual.ShaderUtils;
import isonomicon.visual.SpecialRenderer;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.nio.ByteBuffer;
import java.util.ArrayList;

public class ShipSpecialist extends ApplicationAdapter {
    public static final int SCREEN_WIDTH = 512;//640;
    public static final int SCREEN_HEIGHT = 512;//720;
    public static final boolean TURNTABLE = true;
    private SpecialRenderer renderer;
    private VoxModel voxels;
    private String name;
    private String[] inputs;
    private FastPNG png;
    private AnimatedGif gif;
    private AnimatedPNG apng;
    private QualityPalette analyzed;
    private SpriteBatch batch;
    private Texture palette;
    public ShipSpecialist(String[] args){
        VoxIOExtended.GENERAL = true;
        VoxIOExtended.USE_MATERIALS = false;
        Tools3D.STUFFS = Stuff.STUFFS_B;
        if(args != null && args.length > 0)
            inputs = args;
        else 
        {
            System.out.println("INVALID ARGUMENTS. Please supply space-separated absolute paths to .vox models, or use the .bat file.");
//            inputs = new String[]{
//                    "b/vox/color_guard/Coast.vox", "palettes/b/ColorGuardBaseDark.png", "Coast",
//                    "b/vox/color_guard/Desert.vox", "palettes/b/ColorGuardBaseDark.png", "Desert",
//                    "b/vox/color_guard/Forest.vox", "palettes/b/ColorGuardBaseDark.png", "Forest",
//                    "b/vox/color_guard/Ice.vox", "palettes/b/ColorGuardBaseDark.png", "Ice",
//                    "b/vox/color_guard/Jungle.vox", "palettes/b/ColorGuardBaseDark.png", "Jungle",
//                    "b/vox/color_guard/Mountains.vox", "palettes/b/ColorGuardBaseDark.png", "Mountains",
//                    "b/vox/color_guard/Ocean.vox", "palettes/b/ColorGuardBaseDark.png", "Ocean",
//                    "b/vox/color_guard/Plains.vox", "palettes/b/ColorGuardBaseDark.png", "Plains",
//                    "b/vox/color_guard/River.vox", "palettes/b/ColorGuardBaseDark.png", "River",
//                    "b/vox/color_guard/Rocky.vox", "palettes/b/ColorGuardBaseDark.png", "Rocky",
//                    "b/vox/color_guard/Ruins.vox", "palettes/b/ColorGuardBaseDark.png", "Ruins",
//                    "b/vox/color_guard/Volcano.vox", "palettes/b/ColorGuardBaseDark.png", "Volcano",
//            };
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

        String[] pals = {
                "palettes/b/ColorGuardBaseBlue.png",
                "palettes/b/ColorGuardBaseDark.png",
                "palettes/b/ColorGuardBaseGreen.png",
                "palettes/b/ColorGuardBaseOrange.png",
                "palettes/b/ColorGuardBasePurple.png",
                "palettes/b/ColorGuardBaseRed.png",
                "palettes/b/ColorGuardBaseWhite.png",
                "palettes/b/ColorGuardBaseYellow.png",
        };


        FileHandle[] all = Gdx.files.local("out/vox").list(".vox");

        inputs = new String[3 * all.length];

        DistinctRandom gen = new DistinctRandom(123L);
//        NameGenerator ng = new NameGenerator(NameGenerator.STAR_WARS_STYLE_NAMES, 3, gen);
        Language lang = Language.randomLanguage(1234567890L).mix(Language.MALAY, 0.6f).removeAccents();
        int id = 0;
        for (int i = 0; i < all.length; i++) {
//            for (int c = 0; c < 8; c++) {
                inputs[id++] = all[i].path();
                inputs[id++] = pals[i & 7];
                inputs[id++] = lang.word(all[i].name().hashCode(), true);
//            }
        }

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
        gif.setDitherAlgorithm(Dithered.DitherAlgorithm.LOAF);
        gif.setFlipY(false);
        apng = new AnimatedPNG();
        apng.setCompression(2);
        apng.setFlipY(false);
        gif.palette = analyzed = new QualityPalette();
//        gif.palette = new com.github.tommyettinger.anim8.FastPalette(Coloring.YAM2, Gdx.files.local("assets/Yam2Preload.dat").readBytes());
        gif.setDitherStrength(0.5f);
        Gdx.files.local("out/vox").mkdirs();
        for (int n = 0; n < inputs.length - 2;) {
            String s = inputs[n++];
            palette = new Texture(Gdx.files.local("assets/" + inputs[n++]));
            String output = inputs[n++];
            System.out.println("Rendering " + s + " to " + output);
            load(s);
//            VoxIO.writeVOX("out/" + s, voxels, renderer.palette, VoxIO.lastMaterials);
//            load("out/"+s);
            Texture t = new Texture(renderer.palettePixmap.getWidth(), renderer.palettePixmap.getHeight(), Pixmap.Format.RGBA8888);
            Pixmap pixmap;
            Array<Pixmap> pm = new Array<>(128);
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
                    FrameBuffer fb = new FrameBuffer(Pixmap.Format.RGBA8888, Gdx.graphics.getBackBufferWidth(), Gdx.graphics.getBackBufferHeight(), false);
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
                    png.write(Gdx.files.local("out/b/shipSpecialized/" + output + '/' + output + "_angle" + i + "_" + f + ".png"), pixmap);
                    png.write(Gdx.files.local("out/b/shipSpecial_lab/" + name + '/' + name + "_angle" + i + "_" + f + ".png"), renderer.palettePixmap);
                    fb.dispose();
                }
                pm.insertRange(pm.size - 4, 4);
            }
            analyzed.analyze(pm, 75.0, 256);
            gif.palette = analyzed;
            gif.write(Gdx.files.local("out/b/shipSpecialized/" + output + '/' + output + ".gif"), pm, 8);
            apng.write(Gdx.files.local("out/b/shipSpecialized/" + output + '/' + output + ".png"), pm, 8);
            for (Pixmap pix : pm) {
                if (!pix.isDisposed())
                    pix.dispose();
            }
            pm.clear();
            if(TURNTABLE) {
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
                    FrameBuffer fb = new FrameBuffer(Pixmap.Format.RGBA8888, Gdx.graphics.getBackBufferWidth(), Gdx.graphics.getBackBufferHeight(), false);
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
                    fb.dispose();
                }
                analyzed.analyze(pm, 75.0, 256);
                gif.palette = analyzed;
                gif.write(Gdx.files.local("out/b/shipSpecialized/" + output + '/' + output + "_Turntable.gif"), pm, 24);
                apng.write(Gdx.files.local("out/b/shipSpecialized/" + output + '/' + output + "_Turntable.png"), pm, 24);
//                gif.palette = aurora;
//                gif.setDitherStrength(0.5_0f);
//                gif.write(Gdx.files.local("out/b/specializedAurora/" + name + '/' + name + "_Turntable.gif"), pm, 24);
//                gif.palette = low;
//                gif.setDitherStrength(0.375f);
//                gif.write(Gdx.files.local("out/b/specializedLow/" + name + '/' + name + "_Turntable.gif"), pm, 24);
                for (Pixmap pix : pm) {
                    if (!pix.isDisposed())
                        pix.dispose();
                }
            }
            t.dispose();
        }
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
        final ShipSpecialist app = new ShipSpecialist(arg);
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
