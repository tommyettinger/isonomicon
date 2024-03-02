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
import isonomicon.io.LittleEndianDataInputStream;
import isonomicon.io.extended.VoxIOExtended;
import isonomicon.io.extended.VoxModel;
import isonomicon.physical.Stuff;
import isonomicon.physical.Tools3D;
import isonomicon.visual.BoxyRenderer;
import isonomicon.visual.Coloring;
import isonomicon.visual.ShaderUtils;
import isonomicon.visual.SpecialRenderer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.nio.ByteBuffer;
import java.util.ArrayList;

public class Isologist extends ApplicationAdapter {
    public static final int SCREEN_WIDTH = 512;//640;
    public static final int SCREEN_HEIGHT = 512;//720;
    private BoxyRenderer renderer;
    private VoxModel voxels;
    private String name;
    private String[] inputs;
    private FastPNG png;
    private AnimatedGif gif;
//    private AnimatedPNG apng;
    private QualityPalette analyzed, aurora, azu;
    private SpriteBatch batch;
    private Texture palette;
    public Isologist(String[] args){
        VoxIOExtended.GENERAL = false;
        VoxIOExtended.SCALE = false;
        VoxIOExtended.SOAK = false;
        if(args != null && args.length > 0)
            inputs = args;
        else 
        {
            System.out.println("INVALID ARGUMENTS. Please supply space-separated absolute paths to .vox models, or use the .bat file.");
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
//            inputs = new String[]{"b/vox/Direction_Cube.vox", "palettes/b/TanClothDarkSkin.png"};
//            inputs = new String[]{"b/vox/Lomuk.vox", "palettes/b/BlueFurCyanCrystal.png"};
            inputs = new String[]{
                    "b/vox/Box.vox", "palettes/palettes-b.png", "Box",
                    "b/vox/Direction_Cube.vox", "palettes/b/TanClothDarkSkin.png", "Direction_Cube",
                    "b/vox/Lomuk.vox", "palettes/b/BlueFurCyanCrystal.png", "Lomuk",
                    "b/vox/Damned.vox", "palettes/b/CherrySkinDarkCloth.png", "Damned",
                    "b/vox/Figure.vox", "palettes/b/TanClothDarkSkin.png", "Figure"};
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
        png = new FastPNG();
        png.setCompression(2); // we are likely to compress these with something better, like oxipng.
        png.setFlipY(false);
//        png8 = new PNG8();
        gif = new AnimatedGif();
        gif.setFlipY(false);
//        apng = new AnimatedPNG();
//        apng.setCompression(2);
//        apng.setFlipY(false);
        gif.setDitherAlgorithm(Dithered.DitherAlgorithm.WREN);
        gif.palette = analyzed = new QualityPalette();
//        gif.palette = new com.github.tommyettinger.anim8.FastPalette(Coloring.YAM2, Gdx.files.local("assets/Yam2Preload.dat").readBytes());
        gif.setDitherStrength(0.375f);
        aurora = new QualityPalette();
        azu =
//                new QualityPalette(new int[]{
//                        // Prospecal-8
//                        0x00000000,
//                        0x6DB5BAFF, 0x26544CFF, 0x76AA3AFF, 0xFBFDBEFF, 0xD23C4FFF, 0x2B1328FF, 0x753D38FF, 0xEFAD5FFF
//                });
                new QualityPalette(new int[]{
                        // azurestar-32
                        0x00000000, 0x372B26FF, 0xC37C6BFF, 0xDD997EFF, 0x6E6550FF, 0x9A765EFF, 0xE1AD56FF, 0xC6B5A5FF,
                        0xE9B58CFF, 0xEFCBB3FF, 0xF7DFAAFF, 0xFFEDD4FF, 0xBBD18AFF, 0x355525FF, 0x557A41FF, 0x112D19FF,
                        0x45644FFF, 0x62966AFF, 0x86BB9AFF, 0x15452DFF, 0x396A76FF, 0x86A2B7FF, 0x92B3DBFF, 0x3D4186FF,
                        0x6672BFFF, 0x15111BFF, 0x9A76BFFF, 0x925EA2FF, 0xC7A2CFFF, 0x553549FF, 0xA24D72FF, 0xC38E92FF,
                        0xE3A6BBFF,
                });
//                new QualityPalette(new int[]{
//                        // Nice-31
//                        0x00000000, 0x636663FF, 0x87857CFF, 0xBCAD9FFF, 0xF2B888FF, 0xEB9661FF, 0xB55945FF, 0x734C44FF,
//                        0x3D3333FF, 0x593E47FF, 0x7A5859FF, 0xA57855FF, 0xDE9F47FF, 0xFDD179FF, 0xFEE1B8FF, 0xD4C692FF,
//                        0xA6B04FFF, 0x819447FF, 0x44702DFF, 0x2F4D2FFF, 0x546756FF, 0x89A477FF, 0xA4C5AFFF, 0xCAE6D9FF,
//                        0xF1F6F0FF, 0xD5D6DBFF, 0xBBC3D0FF, 0x96A9C1FF, 0x6C81A1FF, 0x405273FF, 0x303843FF, 0x14233AFF,
//                });
//                new QualityPalette(new int[]{
//                // Equpix15, by Night
//                0x00000000,0x523c4eff,0x2a2a3aff,0x3e5442ff,0x84545cff,0x38607cff,0x5c7a56ff,0x101024ff,
//                0xb27e56ff,0xd44e52ff,0x55a894ff,0x80ac40ff,0xec8a4bff,0x8bd0baff,0xffcc68ff,0xfff8c0ff,
//        });
//        Gdx.files.local("out/vox").mkdirs();
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
            Array<Pixmap> pm = new Array<>(32);
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
                        Stuff.evolve(Stuff.STUFFS_B, voxels.grids.get(j), f);
                    }
                    renderer.drawModelSimple(voxels, i * 0.25f, 0f, 0f, f, 0, 0, 0);
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
                    png.write(Gdx.files.local("out/boxy/specialized/" + output + '/' + output + "_angle" + i + "_" + f + ".png"), pixmap);
                    png.setFlipY(true);
                    png.write(Gdx.files.local("out/boxy/special_lab/" + name + '/' + name + "_angle" + i + "_" + f + ".png"), renderer.palettePixmap);
                    png.setFlipY(false);
                    fb.dispose();
                }
                pm.insertRange(pm.size - 4, 4);
            }
            analyzed.analyze(pm, 75.0, 256);
            gif.palette = analyzed;
            gif.write(Gdx.files.local("out/boxy/specialized/" + output + '/' + output + ".gif"), pm, 8);
//            apng.write(Gdx.files.local("out/b/specialized/" + output + '/' + output + ".png"), pm, 8);
            gif.palette = aurora;
            gif.setDitherStrength(0.375f);
            gif.write(Gdx.files.local("out/boxy/specializedAurora/" + output + '/' + output + ".gif"), pm, 8);
            gif.palette = azu;
            gif.setDitherStrength(0.625f);
            gif.write(Gdx.files.local("out/boxy/specializedAzu/" + output + '/' + output + ".gif"), pm, 8);
            for (Pixmap pix : pm) {
                if (!pix.isDisposed())
                    pix.dispose();
            }
            pm.clear();
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
        final Isologist app = new Isologist(arg);
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
            renderer = new BoxyRenderer(voxels.grids.get(0).length, Stuff.STUFFS_B);
            renderer.palette(Coloring.BETTS64);
            renderer.saturation(0f);
        } catch (FileNotFoundException e) {
            voxels = new VoxModel();
        }
    }
}
