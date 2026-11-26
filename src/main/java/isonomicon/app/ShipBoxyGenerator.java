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
import com.github.tommyettinger.anim8.AnimatedPNG;
import com.github.tommyettinger.anim8.FastPNG;
import com.github.tommyettinger.anim8.QualityPalette;
import com.github.tommyettinger.digital.Hasher;
import com.github.yellowstonegames.text.Language;
import isonomicon.io.extended.VoxIOExtended;
import isonomicon.io.extended.VoxModel;
import isonomicon.physical.ModelMaker;
import isonomicon.physical.Stuff;
import isonomicon.physical.Tools3D;
import isonomicon.visual.BoxyRenderer;
import isonomicon.visual.Coloring;
import isonomicon.visual.ShaderUtils;

import java.nio.ByteBuffer;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ShipBoxyGenerator extends ApplicationAdapter {
    public static final int SCREEN_WIDTH = 512;//640;
    public static final int SCREEN_HEIGHT = 512;//720;
    public static final boolean TURNTABLE = true;
    private BoxyRenderer renderer;
    private VoxModel voxels;
    private FastPNG png;
    private AnimatedGif gif;
    private AnimatedPNG apng;
    private SpriteBatch batch;
    private Texture palette;
    public ShipBoxyGenerator(String[] args){
        BoxyRenderer.shrink = 2;
        VoxIOExtended.GENERAL = true;
        VoxIOExtended.USE_MATERIALS = false;
        VoxIOExtended.SCALE = true;
        VoxIOExtended.SOAK = false;
        BoxyRenderer.outline = 4;
        BoxyRenderer.CORNER_OUTLINES = true;
        Tools3D.STUFFS = Stuff.STUFFS_B;
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

        Language lang = Language.randomLanguage(1234567890L).mix(Language.MALAY, 0.6f).removeAccents();

        ShaderProgram indexShader = new ShaderProgram(ShaderUtils.stuffSelectVertex, ShaderUtils.stuffSelectHueRotateFragment);
        if (!indexShader.isCompiled()) throw new GdxRuntimeException("Error compiling shader: " + indexShader.getLog());
        batch = new SpriteBatch(256, indexShader);

        png = new FastPNG();
        png.setCompression(2); // we are likely to compress these with something better, like oxipng.
//        png8 = new PNG8();
        gif = new AnimatedGif();
        gif.setDitherAlgorithm(AppConfig.DITHER);
        apng = new AnimatedPNG();
        apng.setCompression(2);
        gif.palette = new QualityPalette(
                Coloring.YAM4
//                new int[]{
//                0x00000000, 0x000000FF, 0x636363FF, 0x424242FF, 0xFFFF84FF, 0x737373FF, 0x7B7B7BFF, 0x212121FF,
//                0x9C9C9CFF, 0xFFB563FF, 0x313131FF, 0xFFA552FF, 0xB5B5B5FF, 0x212939FF, 0x181818FF, 0x007B00FF,
//                0xE7BD00FF, 0xD63900FF, 0xAD0042FF, 0x101010FF, 0x6B00EFFF, 0xB5CEDEFF, 0x21319CFF, 0x6B6B6BFF,
//                0x8C8C8CFF, 0x101829FF, 0x940031FF, 0xBD2100FF, 0xCEA500FF, 0x182184FF, 0x006300FF, 0x9CB5C6FF,
//                0x5A00CEFF, 0x73AD08FF, 0x5A5A63FF, 0x314AB5FF, 0x7B10FFFF, 0xCEE7F7FF, 0x109410FF, 0xFFD618FF,
//                0x006363FF, 0xE7FFFFFF, 0xC61052FF, 0xFF6329FF, 0x181008FF, 0x5A3929FF, 0x393939FF, 0xEF5210FF,
//                0x29314AFF, 0x292929FF, 0xFFEF39FF, 0xFFFFFFFF, 0x29A529FF, 0x39425AFF, 0x9431FFFF, 0x395ACEFF,
//                0xE7296BFF, 0x9C8C4AFF, 0x5A1829FF, 0x184A18FF, 0x39186BFF, 0x080810FF, 0x101842FF, 0x7B3921FF,
//                0xF7CE84FF, 0xFFFFDEFF, 0x4A5252FF, 0x63735AFF, 0x7B4A21FF, 0x080808FF, 0xD6CE73FF, 0x949494FF,
//                0x422918FF, 0x4A2100FF, 0x633908FF, 0x080000FF, 0x004A4AFF, 0x736352FF, 0x187373FF, 0x5A6352FF,
//                0xE7FF8CFF, 0x005A00FF, 0xE7E7E7FF, 0x635A42FF, 0xA59C39FF, 0x635242FF, 0x7B8484FF, 0x526B31FF,
//                0xADADB5FF, 0xA59C7BFF, 0x7B7363FF, 0xA59CB5FF, 0x007B73FF, 0x424A4AFF, 0x007300FF, 0xA50008FF,
//                0xB59C84FF, 0x6B6B8CFF, 0x424A39FF, 0x8C94ADFF, 0x8CA59CFF, 0xB58C39FF, 0xAD7BEFFF, 0xBDB594FF,
//                0x311810FF, 0x000821FF, 0x9CBDB5FF, 0x312110FF, 0xB5ADCEFF, 0x840000FF, 0x6B8C00FF, 0x9C7321FF,
//                0xDE4200FF, 0x183939FF, 0x946BD6FF, 0x5A5A73FF, 0xD6D6D6FF, 0x181018FF, 0xA57B00FF, 0xDEDEDEFF,
//                0xC6C6C6FF, 0xB54200FF, 0x10009CFF, 0x5A4A39FF, 0xCEAD9CFF, 0xC600CEFF, 0x420094FF, 0xCE5200FF,
//                0xBD394AFF, 0x947352FF, 0x7B94A5FF, 0x000010FF, 0x948C6BFF, 0xFFE7FFFF, 0x4A5242FF, 0x52314AFF,
//                0x100084FF, 0xFF9452FF, 0xC694FFFF, 0x8C6300FF, 0x004200FF, 0x6B7B8CFF, 0x29B5C6FF, 0xCEA54AFF,
//                0xDE9418FF, 0x846B94FF, 0x840042FF, 0x524208FF, 0xA5B5D6FF, 0x63CE29FF, 0x4A8494FF, 0x94BD10FF,
//                0x31637BFF, 0x9C2900FF, 0x31007BFF, 0x4A4A6BFF, 0x5AC618FF, 0xA5A5A5FF, 0x94A5BDFF, 0x5A2929FF,
//                0x108C8CFF, 0x6B0031FF, 0x73948CFF, 0x8C6B52FF, 0x1021B5FF, 0x391829FF, 0x21524AFF, 0x008400FF,
//                0x006B00FF, 0x295A7BFF, 0x9CADCEFF, 0xB5314AFF, 0x314A42FF, 0x8C7B9CFF, 0xEF9C29FF, 0xC67B00FF,
//                0x6B5A7BFF, 0xA58473FF, 0x081031FF, 0x5210ADFF, 0x0094A5FF, 0x423100FF, 0x9400A5FF, 0x18ADBDFF,
//                0x29A59CFF, 0xBD8C10FF, 0x182131FF, 0xCED6F7FF, 0x9C1831FF, 0x9C3900FF, 0x637B39FF, 0x521818FF,
//                0x7B6B4AFF, 0x102142FF, 0x422139FF, 0xDEB500FF, 0x4A3121FF, 0xE76B29FF, 0x9C1052FF, 0x211010FF,
//                0x100800FF, 0x848473FF, 0x6329BDFF, 0x1831CEFF, 0xB52963FF, 0xDEADFFFF, 0xB50029FF, 0xD62931FF,
//                0xE7BD63FF, 0xADD631FF, 0xC6BD94FF, 0xEFC600FF, 0xFFFF52FF, 0x5A2918FF, 0x5A00EFFF, 0x6300DEFF,
//                0x08398CFF, 0x8C7363FF, 0x7B7B94FF, 0x847B5AFF, 0x63847BFF, 0xAD6300FF, 0x636B7BFF, 0x846331FF,
//                0xBDD6E7FF, 0x31636BFF, 0x522129FF, 0x311018FF, 0x100008FF, 0xDEA500FF, 0xD6CEDEFF, 0x422931FF,
//                0x7B0021FF, 0xADC6D6FF, 0x524263FF, 0x007B84FF, 0x003900FF, 0x000031FF, 0x180031FF, 0x082908FF,
//                0x634A21FF, 0x210010FF, 0xA5BDCEFF, 0x639CB5FF, 0x005A5AFF, 0x844229FF, 0x948442FF, 0x213129FF,
//                0x425A52FF, 0x002173FF, 0x4A00CEFF, 0x2939A5FF, 0x522900FF, 0x184AA5FF, 0x8C4A31FF, 0xEFE784FF
//                }
        );
        gif.setDitherStrength(AppConfig.STRENGTH);
        long startTime = TimeUtils.millis();
        String date = DateFormat.getDateInstance().format(new Date());
        long seed = Hasher.randomize3(Hasher.shax.hashBulk64(date));
        ModelMaker mm = new ModelMaker(seed, new QualityPalette());

        for (int n = 0; n < 16; n++) {

            palette = new Texture(Gdx.files.local("assets/" + pals[n & 7]));
            String output = lang.word(seed = Hasher.randomize3(seed), true);
            while (Gdx.files.local("out/b/shipBoxy/" + date + "/" + output).exists()){
                output = lang.word(++seed, true);
            }
            Gdx.files.local("out/b/shipBoxy/" + date + "/" + output).mkdirs();
            if (!Gdx.files.local("out/b/shipBoxy/" + date + "/" + output).exists()){
                output = lang.word(++seed, true);
            }
            System.out.println("Rendering " + output);
            long hash = Hasher.botis.hashBulk64(output);
            float hue = Hasher.randomize3Float(hash);
            float light = (Hasher.randomizeHFloat(hash) + 1f) / 3f;
            hue *= (float)Math.sqrt(hue);
            mm.rng.setSeed(hash);
            byte[][][] voxelData = mm.shipLargeSmoothColorized();
            voxels = new VoxModel(voxelData, Coloring.BETTS64, Stuff.MATERIALS_B);
            renderer = new BoxyRenderer(voxels.grids.get(0).length, Stuff.STUFFS_B);
            renderer.palette(Coloring.BETTS64);
            renderer.saturation(0f);

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
                for (int f = 0; f < 4; f++) {
                    voxels.grids.clear();
                    for (int j = 0; j < original.size(); j++) {
                        voxels.grids.add(Tools3D.deepCopy(original.get(j)));
//                        voxels.grids.add(Tools3D.translateCopy(original.get(j), 0, 0, Tools3D.wiggle(f)));
                    }
                    for (int j = 0; j < voxels.grids.size(); j++) {
                        byte[][][] grid = voxels.grids.get(j);
                        for (int iter = 0; iter <= f; iter++) {
                            Stuff.evolve(Stuff.STUFFS_B, grid, f);
                        }
                    }
//                    renderer.drawModelSimple(voxels, i * 0.125f, 0f, 0f, f, 0, 0, 0);
                    // We need this to test why translateZ changes lightness badly.
                    renderer.drawModelSimple(voxels, i * 0.125f, 0f, 0f, f, 0, 0, Tools3D.wiggle(f));
//            VoxModel original = voxels.copy();
//            for (int i = 0; i < 8; i++) {
//                voxels = original.copy();
//                for (int f = 0; f < 4; f++) {
//                    for (int j = 0; j < voxels.grids.size(); j++) {
//                        Stuff.evolve(Stuff.STUFFS_B, voxels.grids.get(j), f);
//                    }
//                    renderer.drawModelSimple(voxels, i * 0.125f, 0f, 0f, f, 0.00f, 0.00f, Tools3D.wiggle(f));

                    png.write(Gdx.files.local("out/b/shipBoxy_lab/" + date + "/" + output + '/' + output + "_angle" + i + "_" + f + ".png"), renderer.palettePixmap);
                    png.write(Gdx.files.local("out/b/shipBoxy_lab/" + date + "/" + output + '/' + output + "_shadow_angle" + i + "_" + f + ".png"), renderer.shadowPixmap);

                    renderer.shadowPixmap.drawPixmap(renderer.palettePixmap, 0, 0);
                    t.draw(renderer.shadowPixmap, 0, 0);
                    FrameBuffer fb = new FrameBuffer(Pixmap.Format.RGBA8888, Gdx.graphics.getBackBufferWidth(), Gdx.graphics.getBackBufferHeight(), false);
                    fb.begin();
                    palette.bind(1);
                    ScreenUtils.clear(1f, 1f, 1f, 0f);
                    batch.begin();

                    indexShader.setUniformi("u_texPalette", 1);
                    Gdx.gl.glActiveTexture(GL20.GL_TEXTURE0);
                    batch.setColor(hue, 0.5f, light, 1f);

                    batch.draw(t, 0, t.getHeight(), t.getWidth(), -t.getHeight());
                    batch.end();

//                    pixmap = Pixmap.createFromFrameBuffer(0, 0, t.getWidth(), t.getHeight());
                    //// The above is equivalent to the following, but the above also fills the pixmap.
                    pixmap = createFromFrameBuffer(0, 0, t.getWidth(), t.getHeight());

                    fb.end();
                    pm.add(pixmap);
                    png.write(Gdx.files.local("out/b/shipBoxy/" + date + "/" + output + '/' + output + "_angle" + i + "_" + f + ".png"), pixmap);
                    fb.dispose();
                }
                pm.insertRange(pm.size - 4, 4);
            }
            apng.write(Gdx.files.local("out/b/shipBoxy/"  + date + "/"+ output + '/' + output + ".png"), pm, 8);
            if(gif != null){
                BoxyRenderer.monoAlpha(pm);
//                analyzed.analyze(pm, 75.0, 256);
//                gif.palette = analyzed;
                gif.write(Gdx.files.local("out/b/shipBoxy/"  + date + "/"+ output + '/' + output + ".gif"), pm, 8);
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
                            Stuff.evolve(Stuff.STUFFS_B, voxels.grids.get(j), i >>> 2 & 3);
                        }
                    }
                    renderer.drawModelSimple(voxels, i * 0x1p-7f + 0.125f, 0f, 0f, i >>> 2 & 3, 0, 0, 0);
                    // Use this to debug when rotation is a problem.
//                    renderer.drawModelSimple(voxels, 0.5f + 0.125f, 0f, 0f, i >>> 2 & 3, 0, 0, 0);
//                    t.draw(renderer.palettePixmap, 0, 0);
                    renderer.shadowPixmap.drawPixmap(renderer.palettePixmap, 0, 0);
                    t.draw(renderer.shadowPixmap, 0, 0);
                    FrameBuffer fb = new FrameBuffer(Pixmap.Format.RGBA8888, Gdx.graphics.getBackBufferWidth(), Gdx.graphics.getBackBufferHeight(), false);
                    fb.begin();
                    palette.bind(1);
                    ScreenUtils.clear(1f, 1f, 1f, 0f);
                    batch.begin();

                    indexShader.setUniformi("u_texPalette", 1);
                    Gdx.gl.glActiveTexture(GL20.GL_TEXTURE0);
                    batch.setColor(hue, 0.5f, light, 1f);

                    batch.draw(t, 0, t.getHeight(), t.getWidth(), -t.getHeight());
                    batch.end();
                    pixmap = createFromFrameBuffer(0, 0, t.getWidth(), t.getHeight());
                    fb.end();
                    pm.add(pixmap);
                    fb.dispose();
                }
                apng.write(Gdx.files.local("out/b/shipBoxy/" + date + "/" + output + '/' + output + "_Turntable.png"), pm, 24);
                if(gif != null) {
                    BoxyRenderer.monoAlpha(pm);
//                    analyzed.analyze(pm, 75.0, 256);
//                    gif.palette = analyzed;
                    gif.write(Gdx.files.local("out/b/shipBoxy/" + date + "/" + output + '/' + output + "_Turntable.gif"), pm, 24);
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
        final ShipBoxyGenerator app = new ShipBoxyGenerator(arg);
        new Lwjgl3Application(app, config);
    }
}
