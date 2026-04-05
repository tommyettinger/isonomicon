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
import com.github.tommyettinger.digital.Hasher;
import com.github.yellowstonegames.text.Language;
import isonomicon.io.LittleEndianDataInputStream;
import isonomicon.io.VoxIO;
import isonomicon.io.extended.VoxIOExtended;
import isonomicon.io.extended.VoxModel;
import isonomicon.physical.ModelMaker;
import isonomicon.physical.Stuff;
import isonomicon.physical.Tools3D;
import isonomicon.visual.Coloring;
import isonomicon.visual.ShaderUtils;
import isonomicon.visual.SmudgeRenderer;
import isonomicon.visual.SpecialRenderer;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.nio.ByteBuffer;
import java.util.ArrayList;

public class ShipSmudgeGenerator extends ApplicationAdapter {
    public static final int SCREEN_WIDTH = 512;//640;
    public static final int SCREEN_HEIGHT = 512;//720;
    public static final boolean TURNTABLE = true;
    private SmudgeRenderer renderer;
    private VoxModel voxels;
    private String name;
    private FastPNG png;
    private AnimatedGif gif;
    private AnimatedPNG apng;
    private QualityPalette analyzed;
    private SpriteBatch batch;
    private Texture palette;
    public ShipSmudgeGenerator(String[] args){
        VoxIOExtended.GENERAL = true;
        VoxIOExtended.USE_MATERIALS = false;
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

        ShaderProgram indexShader = new ShaderProgram(ShaderUtils.stuffSelectVertex, ShaderUtils.stuffSelectFragment);
        if (!indexShader.isCompiled()) throw new GdxRuntimeException("Error compiling shader: " + indexShader.getLog());
        batch = new SpriteBatch(256, indexShader);

        png = new FastPNG();
        png.setCompression(2); // we are likely to compress these with something better, like oxipng.
        png.setFlipY(true);
//        png8 = new PNG8();
        gif = new AnimatedGif();
        gif.setDitherAlgorithm(AppConfig.DITHER);
        gif.setFlipY(true);
        apng = new AnimatedPNG();
        apng.setCompression(2);
        apng.setFlipY(true);
        gif.palette = analyzed = new QualityPalette();
        gif.setDitherStrength(AppConfig.STRENGTH);
        long startTime = TimeUtils.millis();
        long seed = Hasher.randomize3(startTime);
        ModelMaker mm = new ModelMaker(seed, new PaletteReducer());

        for (int n = 0; n < 16; n++) {
            palette = new Texture(Gdx.files.local("assets/" + pals[n & 7]));
            String output = this.name = lang.word(seed = Hasher.randomize3(seed), true);
            while (Gdx.files.local("out/b/shipSmudge/" + output).exists()){
                output = this.name = lang.word(++seed, true);
            }
            System.out.println("Rendering " + output);
            mm.rng.setSeed(Hasher.botis.hashBulk64(output));
            byte[][][] voxelData = mm.shipLargeSmoothColorized();
            voxels = new VoxModel(voxelData, Coloring.BETTS64, Stuff.MATERIALS_B);
            renderer = new SmudgeRenderer(voxels.grids.get(0).length);
            renderer.palette(Coloring.BETTS64);
            renderer.saturation(0f);

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
                    pixmap = renderer.drawSplats(voxels.grids.get(0), i * 0.125f, f, Stuff.MATERIALS_B);
                    Pixmap p = new Pixmap(pixmap.getWidth(), pixmap.getHeight(), pixmap.getFormat());
                    p.drawPixmap(pixmap, 0, 0);
                    pm.add(p);

                    png.write(Gdx.files.local("out/b/shipSmudge/" + output + '/' + output + "_angle" + i + "_" + f + ".png"), pixmap);
                }
                pm.insertRange(pm.size - 4, 4);
            }
            apng.write(Gdx.files.local("out/b/shipSmudge/" + output + '/' + output + ".png"), pm, 8);
            if(gif != null){
                SpecialRenderer.monoAlpha(pm);
                analyzed.analyze(pm, 75.0, 256);
                gif.palette = analyzed;
                gif.write(Gdx.files.local("out/b/shipSmudge/" + output + '/' + output + ".gif"), pm, 8);
            }
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
                    if((i & 7) == 7) {
                        for (int j = 0; j < voxels.grids.size(); j++) {
                            Stuff.evolve(Stuff.STUFFS_B, voxels.grids.get(j), i);
                        }
                    }
                    pixmap = renderer.drawSplats(voxels.grids.get(0), i * 0x1p-7f + 0.125f, i >>> 3, Stuff.MATERIALS_B);
                    Pixmap p = new Pixmap(pixmap.getWidth(), pixmap.getHeight(), pixmap.getFormat());
                    p.drawPixmap(pixmap, 0, 0);
                    pm.add(p);
                }
                apng.write(Gdx.files.local("out/b/shipSmudge/" + output + '/' + output + "_Turntable.png"), pm, 24);
                if(gif != null) {
                    SpecialRenderer.monoAlpha(pm);
                    analyzed.analyze(pm, 75.0, 256);
                    gif.palette = analyzed;
                    gif.write(Gdx.files.local("out/b/shipSmudge/" + output + '/' + output + "_Turntable.gif"), pm, 24);
                }
                for (Pixmap pix : pm) {
                    if (!pix.isDisposed())
                        pix.dispose();
                }
            }
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
        final ShipSmudgeGenerator app = new ShipSmudgeGenerator(arg);
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
            renderer = new SmudgeRenderer(voxels.grids.get(0).length);
            renderer.palette(Coloring.BETTS64);
            renderer.saturation(0f);
        } catch (FileNotFoundException e) {
            voxels = new VoxModel();
        }
    }
}
