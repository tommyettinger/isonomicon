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

public class SpecialistC extends ApplicationAdapter {
    public static final int SCREEN_WIDTH = 512;//640;
    public static final int SCREEN_HEIGHT = 512;//720;
    public static final boolean TURNTABLE = true;
    public static final boolean PNG = true;
    public static final boolean GIF = true;
    private SpecialRenderer renderer;
    private VoxModel voxels;
    private String name;
    private String[] inputs;
    private FastPNG png;
    private AnimatedGif gif;
    private QualityPalette analyzed;
    private SpriteBatch batch;
    private Texture palette;
    public SpecialistC(String[] args){
        VoxIOExtended.GENERAL = true;
        SpecialRenderer.SHADOW_INDEX = (byte) 2;
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
//            inputs = new String[]{
//                    "b/vox/gratitude/A24.vox", "palettes/b/ColorGuardBaseDark.png", "Dark_Priest",
//                    "b/vox/gratitude/A24.vox", "palettes/b/ColorGuardBaseWhite.png", "Light_Priest",
//                    "b/vox/gratitude/A24.vox", "palettes/b/ColorGuardBaseRed.png", "War_Priest",
//                    "b/vox/gratitude/A25.vox", "palettes/b/ColorGuardBaseDark.png", "Man_In_Black",
//                    "b/vox/gratitude/A25.vox", "palettes/b/ColorGuardBaseWhite.png", "Man_In_White",
//                    "b/vox/gratitude/A25.vox", "palettes/b/ColorGuardBaseRed.png", "Man_In_Red",
//                    "b/vox/gratitude/A25.vox", "palettes/b/ColorGuardBaseGreen.png", "Man_In_Green",
//                    "b/vox/gratitude/A25.vox", "palettes/b/ColorGuardBaseBlue.png", "Man_In_Blue",
//            };
//            inputs = new String[]{
//                    "b/vox/odyssey/Assassin_Dagger.vox", "palettes/b/TanClothDarkSkin.png",
//                    "b/vox/odyssey/Noble_Knife.vox", "palettes/b/TanClothDarkSkin.png",
//            };
//            inputs = new String[]{"b/vox/Figure.vox", "b/vox/Tree.vox"};
//            inputs = new String[]{"b/vox/Figure_Split.vox", "palettes/b/TanClothDarkSkin.png"};
//            inputs = new String[]{"b/vox/Damned.vox", "palettes/b/CherrySkinDarkCloth.png"};
            inputs = new String[]{"c/Lomuk.vox", "palettes/c/BlueFurCyanCrystal.png", "Lomuk"};
//            inputs = new String[]{"c/Eye_Tyrant.vox", "palettes/c/yam4.png", "Eye Tyrant"};
//            inputs = new String[]{"c/Direction_Cube.vox", "palettes/c/yam4.png", "Direction Cube"};
//            inputs = new String[]{"b/vox/Lomuk.vox", "palettes/b/BlueFurCyanCrystal.png", "Blue Lomuk"};
//            inputs = new String[]{"b/vox/Lomuk.vox", "palettes/b/BlueFurCyanCrystal.png",
//                    "b/vox/Damned.vox", "palettes/b/CherrySkinDarkCloth.png",
//                    "b/vox/Figure.vox", "palettes/b/TanClothDarkSkin.png",
//                    "b/vox/Figure_Split.vox", "palettes/b/TanClothDarkSkin.png"};
            if(!new File("specialized/" + inputs[0]).exists()) {
                System.out.println("File not found: specialized/" + inputs[0]);
                System.exit(0);
            }
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
        if(PNG) {
            png = new FastPNG();
            png.setCompression(2); // we are likely to compress these with something better, like oxipng.
            png.setFlipY(false);
        }
//        png8 = new PNG8();
        if(GIF) {
            gif = new AnimatedGif();
            gif.setDitherAlgorithm(AppConfig.DITHER);
            gif.setFlipY(false);
            gif.palette = analyzed = new QualityPalette();
            gif.setDitherStrength(AppConfig.STRENGTH);
        }
        Gdx.files.local("out/vox").mkdirs();
        for (int n = 0; n < inputs.length - 2;) {
            String s = inputs[n++];
            palette = new Texture(Gdx.files.local("assets/" + inputs[n++]));
            String output = inputs[n++];
            System.out.println("Rendering " + s + " to " + output);
            load("specialized/" + s);
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
                        Stuff.evolve(Stuff.STUFFS_C, voxels.grids.get(j), f);
                    }
                    renderer.drawModelSimple(voxels, i * 0.125f, 0f, 0f, f, 0, 0, 0);
                    t.draw(renderer.palettePixmap, 0, 0);
                    FrameBuffer fb = new FrameBuffer(Pixmap.Format.RGBA8888, Gdx.graphics.getBackBufferWidth(), Gdx.graphics.getBackBufferHeight(), false);
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
                    if (PNG) {
                        png.write(Gdx.files.local("out/c/specialized/" + output + '/' + output + "_angle" + i + "_" + f + ".png"), pixmap);
                        png.write(Gdx.files.local("out/c/special_lab/" + name + '/' + name + "_angle" + i + "_" + f + ".png"), renderer.palettePixmap);
                    }
                    fb.dispose();
                }
                pm.insertRange(pm.size - 4, 4);
            }
            if(GIF) {
                SpecialRenderer.monoAlpha(pm);
                analyzed.analyze(pm, 75.0, 256);
                gif.palette = analyzed;
                gif.setDitherStrength(AppConfig.STRENGTH);
                gif.write(Gdx.files.local("out/c/specialized/" + output + '/' + output + ".gif"), pm, 8);
            }
            for (Pixmap pix : pm) {
                if (!pix.isDisposed())
                    pix.dispose();
            }
            pm.clear();
            if(TURNTABLE) {
                renderer.clear();
                voxels.grids.clear();
                for (int j = 0; j < original.size(); j++) {
                    voxels.grids.add(Tools3D.deepCopy(original.get(j)));
                }
                for (int i = 0; i < 128; i++) {
                    if((i & 3) == 0) {
                        for (int j = 0; j < voxels.grids.size(); j++) {
                            Stuff.evolve(Stuff.STUFFS_C, voxels.grids.get(j), i >>> 2 & 3);
                        }
                    }
                    renderer.drawModelSimple(voxels, i * 0x1p-7f + 0.125f, 0f, 0f, i, 0, 0, 0);
                    t.draw(renderer.palettePixmap, 0, 0);
                    FrameBuffer fb = new FrameBuffer(Pixmap.Format.RGBA8888, Gdx.graphics.getBackBufferWidth(), Gdx.graphics.getBackBufferHeight(), false);
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
                    fb.dispose();
                }
                if(GIF) {
                    SpecialRenderer.monoAlpha(pm);
                    analyzed.analyze(pm, 75.0, 256);
                    gif.palette = analyzed;
                    gif.write(Gdx.files.local("out/c/specialized/" + output + '/' + output + "_Turntable.gif"), pm, 24);
                }
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
        final SpecialistC app = new SpecialistC(arg);
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
