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
    public static final String ANIMATION_PATH = "out/gratitude_animated/";
    public static final String ANIMATION_PATH_PALETTE = "out/gratitude_animated_ridgeback/";
    private SpecialRenderer renderer;
    private VoxModel voxels;
    private String name;
    private ObjectObjectOrderedMap<String, String[]> inputs;
    private FastPNG png;
    private AnimatedGif gif;
    private AnimatedPNG apng;
    private QualityPalette analyzed, fixed;
    private SpriteBatch batch;
    private Texture palette;
    public GratitudeAssets() {
        VoxIOExtended.GENERAL = true;
        System.out.println("INVALID ARGUMENTS. Please supply space-separated absolute paths to .vox models, or use the .bat file.");
        inputs = ObjectObjectOrderedMap.with(
                "b/vox/gratitude/A24.vox", new String[]{
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
                },
                "b/vox/gratitude/A28.vox", new String[]{
                        "palettes/b/ColorGuardBaseDark.png", "Death_Knight",
                        "palettes/b/ColorGuardBaseYellow.png", "Mercenary_Knight",
                        "palettes/b/ColorGuardBaseGreen.png", "Verdant_Knight",
                        "palettes/b/ColorGuardBaseWhite.png", "Paladin",
                },
                "b/vox/gratitude/A29.vox", new String[]{
                        "palettes/b/ColorGuardBaseDark.png", "Blackguard",
                        "palettes/b/ColorGuardBaseRed.png", "Furious_Hoplite",
                        "palettes/b/ColorGuardBasePurple.png", "Mystery_Spearman",
                        "palettes/b/ColorGuardBaseWhite.png", "Holy_Lancer",
                },
                "b/vox/gratitude/A30.vox", new String[]{
                        "palettes/b/ColorGuardBaseDark.png", "Dreadnought",
                        "palettes/b/ColorGuardBaseBlue.png", "Defensive_Lineman",
                        "palettes/b/ColorGuardBaseGreen.png", "Warden",
                        "palettes/b/ColorGuardBaseWhite.png", "Heavy_Bouncer",
                },
                "b/vox/gratitude/A31.vox", new String[]{
                        "palettes/b/ColorGuardBaseDark.png", "Terror_Knight",
                        "palettes/b/ColorGuardBasePurple.png", "Eldritch_Knight",
                        "palettes/b/ColorGuardBaseGreen.png", "Horned_Guardian",
                        "palettes/b/ColorGuardBaseRed.png", "Ruined_Destroyer",
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
        gif = new AnimatedGif();
        gif.setDitherAlgorithm(AppConfig.DITHER);
        gif.setFlipY(false);
        apng = new AnimatedPNG();
        apng.setCompression(2);
        apng.setFlipY(false);
        gif.setDitherStrength(AppConfig.STRENGTH);
        fixed =
                // Aurora, unless analyze() is called
//                new QualityPalette();
                // Aurora (of course)
//                new QualityPalette(PaletteReducer.AURORA)
                // Ridgeback2
                new QualityPalette(
                        new int[]{
                        0x00000000, 0x000000FF, 0x111111FF, 0x1F1F1FFF, 0x2C2C2CFF, 0x393939FF, 0x474747FF, 0x545454FF,
                        0x616161FF, 0x6E6E6EFF, 0x7C7C7CFF, 0x8B8B8BFF, 0x999999FF, 0xA7A7A7FF, 0xB6B6B6FF, 0xC6C6C6FF,
                        0xD6D6D6FF, 0xE5E5E5FF, 0xF5F5F5FF, 0xFFFFFFFF, 0x0E0034FF, 0x0D1A49FF, 0x1F1444FF, 0x2A0F3DFF,
                        0x330B30FF, 0x3B0020FF, 0x3E0010FF, 0x351E52FF, 0x431843FF, 0x4C1431FF, 0x501323FF, 0x51150CFF,
                        0x00370FFF, 0x11295BFF, 0x24245AFF, 0x23430DFF, 0x004725FF, 0x22356CFF, 0x382F67FF, 0x4B285AFF,
                        0x57234AFF, 0x5E213BFF, 0x612227FF, 0x5F260AFF, 0x713038FF, 0x713325FF, 0x6E3700FF, 0x3C4F13FF,
                        0x1D5528FF, 0x29457DFF, 0x423F78FF, 0x52396FFF, 0x623262FF, 0x6E2E4DFF, 0x823A4DFF, 0x823E36FF,
                        0x7F421FFF, 0x754A0BFF, 0x665200FF, 0x4F5B18FF, 0x34612EFF, 0x136443FF, 0x005C81FF, 0x295689FF,
                        0x444E8DFF, 0x5B4783FF, 0x6D4175FF, 0x783D63FF, 0x465F9DFF, 0x5F5798FF, 0x72518DFF, 0x814C7BFF,
                        0x8F4763FF, 0x93494CFF, 0x924C37FF, 0x8A5324FF, 0x7B5C11FF, 0x66651DFF, 0x506C2DFF, 0x337145FF,
                        0x00745EFF, 0x006E88FF, 0x286795FF, 0x31815CFF, 0x008276FF, 0x008089FF, 0x1D7A9CFF, 0x4272A9FF,
                        0x5D69ADFF, 0x7162A7FF, 0x855C98FF, 0x97577FFF, 0xA25467FF, 0xA45553FF, 0xA05B3DFF, 0x936429FF,
                        0x826D21FF, 0x6F742AFF, 0x557C40FF, 0x2D89ABFF, 0x4781B9FF, 0x6478BDFF, 0x7E70B7FF, 0x9369A7FF,
                        0xA56393FF, 0xB06179FF, 0xB4625FFF, 0xB06849FF, 0xA66F36FF, 0x95792FFF, 0x7D8237FF, 0x628A4CFF,
                        0x428F63FF, 0x23907EFF, 0x138E98FF, 0x9D79BDFF, 0xB074A8FF, 0xBE6F93FF, 0xC37178FF, 0xC3745CFF,
                        0xB97C4AFF, 0xAD843CFF, 0x958E44FF, 0x789753FF, 0x5B9D6CFF, 0x3DA082FF, 0x339E9DFF, 0x359AB7FF,
                        0x5093C6FF, 0x668BCFFF, 0x8382C9FF, 0x629FDBFF, 0x7F96DFFF, 0x968FD9FF, 0xB285C7FF, 0xC57FB1FF,
                        0xD27D96FF, 0xD57E80FF, 0xD28463FF, 0xC78C50FF, 0xB39649FF, 0x9F9E51FF, 0x7DA866FF, 0x5EAE7FFF,
                        0x44AF9BFF, 0x3EADB1FF, 0x4AA7CCFF, 0xE68E7CFF, 0xE0946AFF, 0xCE9F5DFF, 0xB9A957FF, 0x9BB366FF,
                        0x82B97AFF, 0x65BD98FF, 0x4ABEB5FF, 0x4CBAD1FF, 0x5EB4E1FF, 0x7DAAEBFF, 0x98A0EFFF, 0xB597E3FF,
                        0xC891D2FF, 0xD98DB5FF, 0xE68A99FF, 0x95B5FFFF, 0xB3ABFAFF, 0xCBA4EDFF, 0xE09DD6FF, 0xF199B9FF,
                        0xF69A9CFF, 0xF59E84FF, 0xE9A771FF, 0xD7B163FF, 0xBCBC6BFF, 0xA1C47AFF, 0x83CA94FF, 0x64CEB3FF,
                        0x5BCCD0FF, 0x60C7E6FF, 0x78BFF6FF, 0xFBB77FFF, 0xE8C172FF, 0xCCCC7AFF, 0xB1D589FF, 0x92DBA3FF,
                        0x73DFC3FF, 0x6BDDE1FF, 0x70D8F8FF, 0xDBB3FFFF, 0xF1ADE7FF, 0xFFA8C9FF, 0xCEE18FFF, 0xAEE9A5FF,
                        0x94EDBFFF, 0x7BEFDFFF, 0x7CEBFDFF, 0xEED580FF, 0xF9E78FFF, 0xD8F39EFF, 0xB3FCBAFF, 0x9FFED5FF,
                        0x86FFF5FF, 0xF6FEA5FF, 0x0A1189FF, 0x33179BFF, 0x4F0088FF, 0x3A2CB0FF, 0x5B1E9CFF, 0x740B81FF,
                        0x3641C3FF, 0x5D32B7FF, 0x7C259BFF, 0x931677FF, 0xA4004BFF, 0xAA001DFF, 0x3B51D5FF, 0x6A40C9FF,
                        0x8B32B0FF, 0xA42786FF, 0xB51B5DFF, 0xBC1B29FF, 0xD02B2EFF, 0x4B5EE7FF, 0x7C4CDBFF, 0x9F3EBCFF,
                        0xB93290FF, 0xC82C65FF, 0xB948BDFF, 0xD23C90FF, 0xE0385DFF, 0xE14022FF, 0x009A29FF, 0x2876FBFF,
                        0x6C65F8FF, 0x9755E2FF, 0xF4511CFF, 0xB060EFFF, 0xD154C4FF, 0xE84B95FF, 0xF34A60FF, 0x00BC4DFF,
                        0xBB71FFFF, 0xE062D9FF, 0xFA59A4FF, 0xF370E4FF, 0x85BE00FF, 0x16CC61FF, 0x6BD646FF, 0xABDD25FF,
                        0x5BEB6EFF, 0xC9E829FF, 0x7CFA6CFF, 0xF4F013FF, 0x3718FBFF, 0x980EFFFF, 0xEA1DE5FF, 0xFF2FECFF,
                })
                {
                    @Override
                    public double difference(int r1, int g1, int b1, int r2, int g2, int b2) {
                        float r = (r1 - r2) * 0.00392156862745098f; r *= r;
                        float g = (g1 - g2) * 0.00392156862745098f; g *= g;
                        float b = (b1 - b2) * 0.00392156862745098f; b *= b;

                        float l = OtherMath.cbrt(0.4121656120f * r + 0.5362752080f * g + 0.0514575653f * b);
                        float m = OtherMath.cbrt(0.2118591070f * r + 0.6807189584f * g + 0.1074065790f * b);
                        float s = OtherMath.cbrt(0.0883097947f * r + 0.2818474174f * g + 0.6302613616f * b);

                        float L = forwardLight(0.2104542553f * l + 0.7936177850f * m - 0.0040720468f * s) * 0.5f;
                        float A = 1.9779984951f * l - 2.4285922050f * m + 0.4505937099f * s;
                        float B = 0.0259040371f * l + 0.7827717662f * m - 0.8086757660f * s;

                        return (L * L + A * A + B * B) * 0x1p17;
                    }
                };
        gif.palette = analyzed = new QualityPalette() {
            @Override
            public double difference(int r1, int g1, int b1, int r2, int g2, int b2) {
                float r = (r1 - r2) * 0.00392156862745098f; r *= r;
                float g = (g1 - g2) * 0.00392156862745098f; g *= g;
                float b = (b1 - b2) * 0.00392156862745098f; b *= b;

                float l = OtherMath.cbrt(0.4121656120f * r + 0.5362752080f * g + 0.0514575653f * b);
                float m = OtherMath.cbrt(0.2118591070f * r + 0.6807189584f * g + 0.1074065790f * b);
                float s = OtherMath.cbrt(0.0883097947f * r + 0.2818474174f * g + 0.6302613616f * b);

                float L = forwardLight(0.2104542553f * l + 0.7936177850f * m - 0.0040720468f * s) * 0.5f;
                float A = 1.9779984951f * l - 2.4285922050f * m + 0.4505937099f * s;
                float B = 0.0259040371f * l + 0.7827717662f * m - 0.8086757660f * s;

                return (L * L + A * A + B * B) * 0x1p17;
            }
        };

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
                apng.write(Gdx.files.local(ANIMATION_PATH + output + '/' + output + ".png"), pm, 8);
                SpecialRenderer.monoAlpha(pm);
                analyzed.analyze(pm, 75.0, 256);
                gif.palette = analyzed;
                gif.write(Gdx.files.local(ANIMATION_PATH + output + '/' + output + ".gif"), pm, 8);
                gif.palette = fixed;
                gif.write(Gdx.files.local(ANIMATION_PATH_PALETTE + name + '/' + name + ".gif"), pm, 8);
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
                    apng.write(Gdx.files.local(ANIMATION_PATH + output + '/' + output + "_Turntable.png"), pm, 24);
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
