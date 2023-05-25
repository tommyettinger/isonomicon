package isonomicon.app;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.TimeUtils;
import com.github.tommyettinger.anim8.*;
import com.github.tommyettinger.ds.IntObjectMap;
import com.github.tommyettinger.ds.ObjectIntMap;
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

public class ColorGuardAssets extends ApplicationAdapter {
    public static boolean DIVERSE = true;
    public static boolean ATTACKS = true;
    public static boolean DEATHS = false;
    public static boolean EXPLOSION = true;
    public static boolean TERRAIN = true;
    public static boolean SHADOWS = true;
    
    public static final String outDir = "out/cg_shallow";

    public static final int SCREEN_WIDTH = 512;//640;
    public static final int SCREEN_HEIGHT = 512;//720;
    private SpecialRenderer renderer;
    private VoxModel voxels, head;
    private VoxModel[] frames = new VoxModel[8];
    private String name;
    private String[] armies;
    private FastPNG png;
    private FastGif gif;
    private FastAPNG apng;
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
//        ColorGuardData.units = ColorGuardData.units.stream().filter(u -> u.primaryStrength > 0).toList();
//        ColorGuardData.units = ColorGuardData.units.stream().filter(u -> u.hasWeapon("Debug")).toList();
//        ColorGuardData.units = ColorGuardData.units.stream().filter(u -> u.hasWeapon("Forward_Missile")).toList();
//        ColorGuardData.units = ColorGuardData.units.stream().filter(u -> u.hasWeapon("Torpedo")).toList();
//        ColorGuardData.units = ColorGuardData.units.stream().filter(u -> u.hasWeapon("Flame_Wave")).toList();
//        ColorGuardData.units = ColorGuardData.units.stream().filter(u -> u.hasWeapon("Bomb_Drop")).toList();
//        ColorGuardData.units = ColorGuardData.units.stream().filter(u -> u.hasWeapon("Hack")).toList();
//        ColorGuardData.units = ColorGuardData.units.stream().filter(u -> u.hasWeapon("Arc_Missile")).toList();
//        ColorGuardData.units = ColorGuardData.units.stream().filter(u -> u.hasWeapon("Arc_Cannon")).toList();
//        ColorGuardData.units = ColorGuardData.units.stream().filter(u -> u.hasWeapon("Forward_Cannon")).toList();
//        ColorGuardData.units = ColorGuardData.units.stream().filter(u -> u.hasWeapon("Machine_Gun")).toList();
//        ColorGuardData.units = ColorGuardData.units.stream().filter(u -> u.hasWeapon("Forward_Missile") || u.hasWeapon("Handgun")).toList();
//        ColorGuardData.units = ColorGuardData.units.stream().filter(u -> u.name.equals("Heavy_Cannon")).toList();
//        ColorGuardData.units = ColorGuardData.units.stream().filter(u -> u.name.startsWith("Terrain")).toList();
//        ColorGuardData.units = ColorGuardData.units.subList(52, ColorGuardData.units.size());
//        ColorGuardData.units = ColorGuardData.units.subList(2, 3);
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
        batch = new SpriteBatch(16, indexShader);

        long startTime = TimeUtils.millis();
        png = new FastPNG();
        png.setFlipY(false);
        png.setCompression(2); // we are likely to compress these with something better, like oxipng.
        gif = new FastGif();
        gif.setFlipY(false);
        apng = new FastAPNG();
        apng.setFlipY(false);
        apng.setCompression(2);
        //// Using Neue on a null palette takes 146.797 seconds with just the five units with an arc missile.
        //// (with fastAnalysis=false.)
        //// Now with fastAnalysis=true, 34.407 seconds.
//        gif.setDitherAlgorithm(Dithered.DitherAlgorithm.NEUE);
        //// Using Scatter with the below YAM3 preloaded palette, dither strength 0.75f, takes 25.596 seconds.
//        gif.setDitherAlgorithm(Dithered.DitherAlgorithm.SCATTER);
        //// Neue with the preloaded YAM3 palette, dither strength 0.5f, looks a little better, and takes similar time.

        //// The latest BLUE_NOISE dither is a huge improvement, and looks much better in animations.
//        gif.setDitherAlgorithm(Dithered.DitherAlgorithm.BLUE_NOISE);
        //// The recent changes to GRADIENT_NOISE dither finally make it usable, and it's an ordered dither, too
//        gif.setDitherAlgorithm(Dithered.DitherAlgorithm.GRADIENT_NOISE);
        // can be pretty good, but this might be too strong by default. Ordered dither, again.
//        gif.setDitherAlgorithm(Dithered.DitherAlgorithm.ROBERTS);
        // Not an ordered dither; let's see how this goes.
        gif.setDitherAlgorithm(Dithered.DitherAlgorithm.DODGY);
//        gif.palette = new com.github.tommyettinger.anim8.FastPalette(Coloring.AURORA);
        gif.palette = new com.github.tommyettinger.anim8.QualityPalette(); // uses AURORA, OklabCareful metric
//        gif.palette = new com.github.tommyettinger.anim8.FastPalette(PaletteReducer.YAMPED); // uses YAMPED, simplest RGB metric
//        gif.palette = new com.github.tommyettinger.anim8.FastPalette(PaletteReducer.YAMPED, Gdx.files.local("assets/YampedOklabPreload.dat").readBytes()); // uses YAMPED, OklabCareful metric
//        gif.palette = new com.github.tommyettinger.anim8.FastPalette(Coloring.MUNSELLISH255, Gdx.files.local("assets/MunsellishRGBPreload.dat").readBytes());
//        gif.palette = new com.github.tommyettinger.anim8.FastPalette(Coloring.MUNSELLISH255, Gdx.files.local("assets/MunsellishOklabPreload.dat").readBytes());
//        gif.palette = new com.github.tommyettinger.anim8.FastPalette(Coloring.AURORA, Gdx.files.local("assets/AuroraOklabPreload.dat").readBytes());
//        gif.palette = new com.github.tommyettinger.anim8.FastPalette(Coloring.CORN4, Gdx.files.local("assets/Corn4RGBPreload.dat").readBytes());
//        gif.palette = new com.github.tommyettinger.anim8.FastPalette(Coloring.CORN4, Gdx.files.local("assets/Corn4OklabPreload.dat").readBytes());
//        gif.palette = new com.github.tommyettinger.anim8.FastPalette(Coloring.YAM3, Gdx.files.local("assets/Yam3Preload.dat").readBytes());
//        gif.palette = new com.github.tommyettinger.anim8.FastPalette(Coloring.TATER255, Gdx.files.local("assets/TaterPreload.dat").readBytes());
//        gif.palette = new com.github.tommyettinger.anim8.FastPalette(Coloring.TETRA256, Gdx.files.local("assets/TetraPreload.dat").readBytes());
//        gif.palette = new com.github.tommyettinger.anim8.FastPalette(Coloring.BETSY256, Gdx.files.local("assets/BetsyPreload.dat").readBytes());
        //// BLUE_NOISE doesn't need this, but NEUE, GRADIENT_NOISE, and ROBERTS do.
        gif.setDitherStrength(0.5f);
        FrameBuffer fb = new FrameBuffer(Pixmap.Format.RGBA8888, Gdx.graphics.getBackBufferWidth(), Gdx.graphics.getBackBufferHeight(), false);
        ObjectIntMap<String> doneReceive = new ObjectIntMap<>(16);
        doneReceive.setDefaultValue(-1);
        for(String s : EffectGenerator.KNOWN_RECEIVE_EFFECTS.keySet())
            doneReceive.put(s, 0);
        // many skin and hair colors
        if(DIVERSE)
        {
            int[] canonicalLooks ={0, 7, 15, 3, 18, 17, 1, 12};
            EACH_INPUT:
            for (int n = 0; n < ColorGuardData.units.size(); n++) {
                ColorGuardData.Unit unit = ColorGuardData.units.get(n);
                name = unit.name;
                System.out.println("Rendering " + name);
                Gdx.files.local(outDir + "/animated_diverse/" + name + '/').mkdirs();
                load("specialized/b/vox/color_guard/" + name + ".vox");
                if(name.startsWith("Terrain")){
                    renderer.outline = 2;
                } else {
                    renderer.outline = 4;
                }
                Texture t = new Texture(renderer.palettePixmap.getWidth(), renderer.palettePixmap.getHeight(), Pixmap.Format.RGBA8888);
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
                        renderer.drawModelSimple(voxels, i * 0.25f, 0f, 0f, f, 0.00f, 0.00f, 0.00f);
                        t.draw(renderer.palettePixmap, 0, 0);
                        for (int look = 0, lk = 0; look < 153; look+=8, lk++) {
//                            if(lk == 3 || lk == 8 || lk == 11 || lk == 18 || lk == 21 || lk == 25)
//                                continue;
                            for (int j = 0; j < armies.length; j++) {
                                fb.begin();
                                palette.bind(1);
                                ScreenUtils.clear(1f, 1f, 1f, 0f);
                                batch.begin();

                                indexShader.setUniformi("u_texPalette", 1);
                                Gdx.gl.glActiveTexture(GL20.GL_TEXTURE0);
                                batch.setColor((look + j) / 255f, 0.5f, 0.5f, 1f);

                                batch.draw(t, 0, 0, t.getWidth(), t.getHeight());
                                batch.end();
                                pixmap = Pixmap.createFromFrameBuffer(0, 0, t.getWidth(), t.getHeight());
                                fb.end();
                                png.write(Gdx.files.local(outDir + "/" + armies[j] + "/" + name + '/' + armies[j] + "_look" + lk + '_' + name + "_angle" + i + "_" + f + ".png"), pixmap);
                                if(look + j == 0)
                                    png.write(Gdx.files.local(outDir + "/lab/" + name + '/' + name + "_angle" + i + "_" + f + ".png"), renderer.palettePixmap);
                                if(lk == canonicalLooks[j]) {
                                    pm.set(j * 32 + i * 8 + f, pixmap);
                                    pm.set(j * 32 + i * 8 + f + 4, pixmap);
                                } else {
                                    pixmap.dispose();
                                }
                            }
                        }
//                png8.write(Gdx.files.local("out/" + name + '/' + name + "_angle" + i + ".png"), p, false, true);
                    }
                }
//                gif.palette.analyze(pm);
                apng.write(Gdx.files.local(outDir + "/animated_diverse/" + name + '/' + name + ".png"), pm, 8);
                SpecialRenderer.monoAlpha(pm);
                gif.write(Gdx.files.local(outDir + "/animated_diverse/" + name + '/' + name + ".gif"), pm, 8);
//                apng.write(Gdx.files.local(outDir + "/animated_diverse_flat/" + name + ".png"), pm, 8);
                for (Pixmap pix : pm) {
                    if (!pix.isDisposed())
                        pix.dispose();
                }
                if (ATTACKS) {
                    pm.clear();
                    pm.setSize(32 * armies.length);
                    String attack = unit.primary, ps = "_Primary";
                    int strength = unit.primaryStrength;
                    boolean pose = unit.primaryPose;
                    for (int which = 0; which < 2; which++) {
                        if (attack == null) continue EACH_INPUT;
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
                        EffectGenerator.r.setSeed(unit.name.hashCode() ^ which);
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
                                renderer.drawModelSimple(frames[f], i * 0.25f, 0f, 0f, f, 0.00f, 0.00f, 0.00f);
                                t.draw(renderer.palettePixmap, 0, 0);
                                for (int look = 0, lk = 0; look < 153; look+=8, lk++) {
                                    for (int j = 0; j < armies.length; j++) {
                                        fb.begin();
                                        palette.bind(1);
                                        ScreenUtils.clear(1f, 1f, 1f, 0f);
                                        batch.begin();

                                        indexShader.setUniformi("u_texPalette", 1);
                                        Gdx.gl.glActiveTexture(GL20.GL_TEXTURE0);
                                        batch.setColor((look + j) / 255f, 0.5f, 0.5f, 1f);

                                        batch.draw(t, 0, 0, t.getWidth(), t.getHeight());
                                        batch.end();
                                        pixmap = Pixmap.createFromFrameBuffer(0, 0, t.getWidth(), t.getHeight());
                                        fb.end();
                                        png.write(Gdx.files.local(outDir + "/" + armies[j] + "/" + name + '/' + armies[j] + "_look" + lk + "_" + name + ps + "_angle" + i + "_" + f + ".png"), pixmap);
                                        if (look + j == 0)
                                            png.write(Gdx.files.local(outDir + "/lab/" + name + '/' + name + ps + "_angle" + i + "_" + f + ".png"), renderer.palettePixmap);
                                        if(lk == canonicalLooks[j]) {
                                            pm.set(j * 32 + i * 8 + f, pixmap);
                                        } else {
                                            pixmap.dispose();
                                        }
                                    }
                                }
                                byte[][][] g = frames[f].grids.get(0);
                                for (int j = 1; j < frames[f].grids.size(); j++) {
                                    Tools3D.deepCopyInto(g, frames[f].grids.get(j));
                                }

//                png8.write(Gdx.files.local("out/" + name + '/' + name + "_angle" + i + ".png"), p, false, true);
                            }
                        }
//                gif.palette.analyze(pm);
                        apng.write(Gdx.files.local(outDir + "/animated_diverse/" + name + '/' + name + ps + ".png"), pm, 8);
                        SpecialRenderer.monoAlpha(pm);
                        gif.write(Gdx.files.local(outDir + "/animated_diverse/" + name + '/' + name + ps + ".gif"), pm, 8);
//                        apng.write(Gdx.files.local(outDir + "/animated_diverse_flat/" + name + ps + ".png"), pm, 8);
                        for (Pixmap pix : pm) {
                            if (!pix.isDisposed())
                                pix.dispose();
                        }
                        EffectGenerator.r.setSeed(unit.name.hashCode() ^ which);
                        if(strength > 0) {
                            int rec = doneReceive.get(attack);
                            if(rec >= 0 && (rec & 1 << strength) == 0) {
                                doneReceive.put(attack, rec | 1 << strength);
                                EffectGenerator.ReceiveEffect recEff = EffectGenerator.KNOWN_RECEIVE_EFFECTS.get(attack);
                                if (recEff != null) {
                                    for (int i = 0; i < 4; i++) {
                                        frames = recEff.runEffect(60 << 1, 8, strength);
                                        for (int f = 0; f < frames.length; f++) {
                                            renderer.drawModelSimple(frames[f], i * 0.25f, 0f, 0f, f, 0.00f, 0.00f, 0.00f);
                                            t.draw(renderer.palettePixmap, 0, 0);
                                            int look = 0;
                                            for (int j = 0; j < armies.length; j++) {
                                                fb.begin();
                                                palette.bind(1);
                                                ScreenUtils.clear(1f, 1f, 1f, 0f);
                                                batch.begin();

                                                indexShader.setUniformi("u_texPalette", 1);
                                                Gdx.gl.glActiveTexture(GL20.GL_TEXTURE0);
                                                batch.setColor((look + j) / 255f, 0.5f, 0.5f, 1f);

                                                batch.draw(t, 0, 0, t.getWidth(), t.getHeight());
                                                batch.end();
                                                pixmap = Pixmap.createFromFrameBuffer(0, 0, t.getWidth(), t.getHeight());
                                                fb.end();
                                                pm.set(j * 32 + i * 8 + f, pixmap);
                                                png.write(Gdx.files.local(outDir + "/" + armies[j] + "/" + attack + "_Receive/" + armies[j] + "_look" + look + "_" + attack + "_Receive_" + strength + "_angle" + i + "_" + f + ".png"), pixmap);
                                                if (look + j == 0)
                                                    png.write(Gdx.files.local(outDir + "/lab/" + attack + "_Receive/" + attack + "_Receive_" + strength + "_angle" + i + "_" + f + ".png"), renderer.palettePixmap);
                                            }
                                        }
                                    }
                                    apng.write(Gdx.files.local(outDir + "/animated_diverse/" + attack + "_Receive/" + attack + "_Receive_" + strength + ".png"), pm, 8);
                                    SpecialRenderer.monoAlpha(pm);
                                    gif.write(Gdx.files.local(outDir + "/animated_diverse/" + attack + "_Receive/" + attack + "_Receive_" + strength + ".gif"), pm, 8);
                                    for (Pixmap pix : pm) {
                                        if (!pix.isDisposed())
                                            pix.dispose();
                                    }

                                }
                            }
                        }
                        attack = unit.secondary;
                        strength = unit.secondaryStrength;
                        pose = unit.secondaryPose;
                        ps = "_Secondary";
                    }
                }
                t.dispose();
            }
        }
        // just a single skin/hair combination
        else {
            EACH_INPUT:
            for (int n = 0; n < ColorGuardData.units.size(); n++) {
                ColorGuardData.Unit unit = ColorGuardData.units.get(n);
                String s = unit.name;
                System.out.println("Rendering " + s);
                load("specialized/b/vox/color_guard/" + s + ".vox");
                if(name.startsWith("Terrain")){
                    renderer.outline = 2;
                } else {
                    renderer.outline = 4;
                }
                Texture t = new Texture(renderer.palettePixmap.getWidth(), renderer.palettePixmap.getHeight(), Pixmap.Format.RGBA8888);
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
                        renderer.drawModelSimple(voxels, i * 0.25f, 0f, 0f, f, 0.00f, 0.00f, 0.00f);
                        t.draw(renderer.palettePixmap, 0, 0);
                        int look = 0;
                        for (int j = 0; j < armies.length; j++) {
                            fb.begin();
                            palette.bind(1);
                            ScreenUtils.clear(1f, 1f, 1f, 0f);
                            batch.begin();

                            indexShader.setUniformi("u_texPalette", 1);
                            Gdx.gl.glActiveTexture(GL20.GL_TEXTURE0);
                            batch.setColor((look + j) / 255f, 0.5f, 0.5f, 1f);

                            batch.draw(t, 0, 0, t.getWidth(), t.getHeight());
                            batch.end();
                            pixmap = Pixmap.createFromFrameBuffer(0, 0, t.getWidth(), t.getHeight());
                            fb.end();
                            pm.set(j * 32 + i * 8 + f, pixmap);
                            pm.set(j * 32 + i * 8 + f + 4, pixmap);
                            png.write(Gdx.files.local(outDir + "/" + armies[j] + "/" + name + '/' + armies[j] + "_look0_" + name + "_angle" + i + "_" + f + ".png"), pixmap);
                            if (look + j == 0)
                                png.write(Gdx.files.local(outDir + "/lab/" + name + '/' + name + "_angle" + i + "_" + f + ".png"), renderer.palettePixmap);
                        }
//                png8.write(Gdx.files.local("out/" + name + '/' + name + "_angle" + i + ".png"), p, false, true);
                    }
                }
                apng.write(Gdx.files.local(outDir + "/animated/" + name + '/' + name + ".png"), pm, 8);
                SpecialRenderer.monoAlpha(pm);
                gif.write(Gdx.files.local(outDir + "/animated/" + name + '/' + name + ".gif"), pm, 8);
                for (Pixmap pix : pm) {
                    if (!pix.isDisposed())
                        pix.dispose();
                }
                if (ATTACKS) {
                    pm.clear();
                    pm.setSize(32 * armies.length);
                    String attack = unit.primary, ps = "_Primary";
                    int strength = unit.primaryStrength;
                    boolean pose = unit.primaryPose;
                    for (int which = 0; which < 2; which++) {
                        if(attack == null) continue EACH_INPUT;
                        if (!EffectGenerator.KNOWN_EFFECTS.containsKey(attack)) {
                            continue;
                        }
                        if (pose) {
                            load("specialized/b/vox/color_guard/" + unit.name + "_Firing.vox");
                        } else {
                            load("specialized/b/vox/color_guard/" + unit.name + ".vox");
                        }
                        name = unit.name;
                        original = voxels.copy();
                        EffectGenerator.r.setSeed(unit.name.hashCode() ^ which);
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
                                pixmap = renderer.drawModelSimple(frames[f], i * 0.25f, 0f, 0f, f, 0.00f, 0.00f, 0.00f);
                                t.draw(renderer.palettePixmap, 0, 0);
                                int look = 0;
                                for (int j = 0; j < armies.length; j++) {
                                    fb.begin();
                                    palette.bind(1);
                                    ScreenUtils.clear(1f, 1f, 1f, 0f);
                                    batch.begin();

                                    indexShader.setUniformi("u_texPalette", 1);
                                    Gdx.gl.glActiveTexture(GL20.GL_TEXTURE0);
                                    batch.setColor((look + j) / 255f, 0.5f, 0.5f, 1f);

                                    batch.draw(t, 0, 0, t.getWidth(), t.getHeight());
                                    batch.end();
                                    pixmap = Pixmap.createFromFrameBuffer(0, 0, t.getWidth(), t.getHeight());
                                    fb.end();
                                    pm.set(j * 32 + i * 8 + f, pixmap);
                                    png.write(Gdx.files.local(outDir + "/" + armies[j] + "/" + name + '/' + armies[j] + "_look" + look + "_" + name + ps + "_angle" + i + "_" + f + ".png"), pixmap);
                                    if (look + j == 0) {
                                        png.write(Gdx.files.local(outDir + "/lab/" + name + '/' + name + ps + "_angle" + i + "_" + f + ".png"), renderer.palettePixmap);
//                                            VoxIOExtended.writeVOX(outDir + "/vox/" + name + "/" + ps + "_angle" + i + "_" + f + ".vox", frames[f].grids.get(0), VoxIO.lastPalette, VoxIO.lastMaterials);
                                    }
                                }
                                byte[][][] g = frames[f].grids.get(0);
                                for (int j = 1; j < frames[f].grids.size(); j++) {
                                    Tools3D.deepCopyInto(g, frames[f].grids.get(j));
                                }
                            }
                        }
                        apng.write(Gdx.files.local(outDir + "/animated/" + name + '/' + name + ps + ".png"), pm, 8);
                        SpecialRenderer.monoAlpha(pm);
                        gif.write(Gdx.files.local(outDir + "/animated/" + name + '/' + name + ps + ".gif"), pm, 8);
                        for (Pixmap pix : pm) {
                            if (!pix.isDisposed())
                                pix.dispose();
                        }
                        EffectGenerator.r.setSeed(unit.name.hashCode() ^ which);
                        if(strength > 0){
                            int rec = doneReceive.get(attack);
                            if(rec >= 0 && (rec & 1 << strength) == 0) {
                                doneReceive.put(attack, rec | 1 << strength);
                                EffectGenerator.ReceiveEffect recEff = EffectGenerator.KNOWN_RECEIVE_EFFECTS.get(attack);
                                if (recEff != null) {
                                    for (int i = 0; i < 4; i++) {
                                        frames = recEff.runEffect(60 << 1, 8, strength);
                                        for (int f = 0; f < frames.length; f++) {
                                            renderer.drawModelSimple(frames[f], i * 0.25f, 0f, 0f, f, 0.00f, 0.00f, 0.00f);
                                            t.draw(renderer.palettePixmap, 0, 0);
                                            int look = 0;
                                            for (int j = 0; j < armies.length; j++) {
                                                fb.begin();
                                                palette.bind(1);
                                                ScreenUtils.clear(1f, 1f, 1f, 0f);
                                                batch.begin();

                                                indexShader.setUniformi("u_texPalette", 1);
                                                Gdx.gl.glActiveTexture(GL20.GL_TEXTURE0);
                                                batch.setColor((look + j) / 255f, 0.5f, 0.5f, 1f);

                                                batch.draw(t, 0, 0, t.getWidth(), t.getHeight());
                                                batch.end();
                                                pixmap = Pixmap.createFromFrameBuffer(0, 0, t.getWidth(), t.getHeight());
                                                fb.end();
                                                pm.set(j * 32 + i * 8 + f, pixmap);
                                                png.write(Gdx.files.local(outDir + "/" + armies[j] + "/" + attack + "_Receive/" + armies[j] + "_look" + look + "_" + attack + "_Receive_" + strength + "_angle" + i + "_" + f + ".png"), pixmap);
                                                if (look + j == 0)
                                                    png.write(Gdx.files.local(outDir + "/lab/" + attack + "_Receive/" + attack + "_Receive_" + strength + "_angle" + i + "_" + f + ".png"), renderer.palettePixmap);
                                            }
                                        }
                                    }
                                    apng.write(Gdx.files.local(outDir + "/animated/" + attack + "_Receive/" + attack + "_Receive_" + strength + ".png"), pm, 8);
                                    SpecialRenderer.monoAlpha(pm);
                                    gif.write(Gdx.files.local(outDir + "/animated/" + attack + "_Receive/" + attack + "_Receive_" + strength + ".gif"), pm, 8);
                                    for (Pixmap pix : pm) {
                                        if (!pix.isDisposed())
                                            pix.dispose();
                                    }

                                }
                            }
                        }
                        attack = unit.secondary;
                        strength = unit.secondaryStrength;
                        pose = unit.secondaryPose;
                        ps = "_Secondary";

                    }
                }
                t.dispose();
            }
        }
        if(EXPLOSION)
        {
            voxels = new VoxModel();
//            SpecialRenderer.shrink = 1;
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
            EffectGenerator.r.setSeed(1);
            byte[][][][] explosion = EffectGenerator.fireballAnimation(fireSeed, 12, 3, 0);
            Pixmap pixmap;
            Array<Pixmap> pm = new Array<>(4 * explosion.length);
            Texture t = new Texture(renderer.palettePixmap.getWidth(), renderer.palettePixmap.getHeight(), Pixmap.Format.RGBA8888);

            for (int i = 0; i < 4; i++) {
                for (int f = 0; f < explosion.length; f++) {
                    voxels.grids.clear();
                    voxels.grids.add(explosion[f]);
//                    Stuff.evolve(Stuff.STUFFS_B, fireSeed, f);
//                    voxels.grids.add(fireSeed);
                    renderer.drawModelSimple(voxels, i * 0.25f, 0f, 0f, f, 0.00f, 0.00f, 0.00f);
                    t.draw(renderer.palettePixmap, 0, 0);
                    fb.begin();
                    palette.bind(1);
                    ScreenUtils.clear(1f, 1f, 1f, 0f);
                    batch.begin();

                    indexShader.setUniformi("u_texPalette", 1);
                    Gdx.gl.glActiveTexture(GL20.GL_TEXTURE0);
                    batch.setColor(0f, 0.5f, 0.5f, 1f);

                    batch.draw(t, 0, 0, t.getWidth(), t.getHeight());
                    batch.end();
                    pixmap = Pixmap.createFromFrameBuffer(0, 0, t.getWidth(), t.getHeight());
                    fb.end();
                    pm.add(pixmap);
                    png.write(Gdx.files.local(outDir + "/effects/" + name + '/' + name + "_angle" + i + "_" + f + ".png"), pixmap);
                    png.write(Gdx.files.local(outDir + "/lab/" + name + '/' + name + "_angle" + i + "_" + f + ".png"), renderer.palettePixmap);
                }
            }
            apng.write(Gdx.files.local(outDir + "/animated/" + name + '/' + name + ".png"), pm, 8);
            SpecialRenderer.monoAlpha(pm);
            gif.write(Gdx.files.local(outDir + "/animated/" + name + '/' + name + ".gif"), pm, 8);
            for (Pixmap pix : pm) {
                if(!pix.isDisposed())
                    pix.dispose();
            }
            t.dispose();
        }
        if(TERRAIN)
        {
            load("specialized/b/vox/color_guard/Terrain_Small.vox");
            renderer.outline = 2;
            Texture t = new Texture(renderer.palettePixmap.getWidth(), renderer.palettePixmap.getHeight(), Pixmap.Format.RGBA8888);
            for (int n = 0; n < ColorGuardData.terrains.size(); n++) {
                name = ColorGuardData.terrains.get(n);
                System.out.println("Rendering " + name);
                Gdx.files.local(outDir + "/Landscape/" + name + '/').mkdirs();
                Pixmap pixmap;
                VoxModel original = voxels.copy();
                for (int i = 0; i < 4; i++) {
                    voxels = original.copy();
                    for (int f = 0; f < 1; f++) {
                        pixmap = renderer.drawModelSimple(voxels, i * 0.25f, 0f, 0f, f, 0.00f, 0.00f, 0.00f);
                        t.draw(renderer.palettePixmap, 0, 0);
                        fb.begin();
                        palette.bind(1);
                        ScreenUtils.clear(1f, 1f, 1f, 0f);
                        batch.begin();

                        indexShader.setUniformi("u_texPalette", 1);
                        Gdx.gl.glActiveTexture(GL20.GL_TEXTURE0);
                        batch.setColor((160 + n) / 255f, 0.625f, 0.5f, 1f);

                        batch.draw(t, 0, 0, t.getWidth(), t.getHeight());
                        batch.end();
                        pixmap = Pixmap.createFromFrameBuffer(0, 0, t.getWidth(), t.getHeight());
                        fb.end();
                        try {
                            png.write(Gdx.files.local(outDir + "/Landscape/" + name + '/' + name + "_angle" + i + "_" + f + ".png"), pixmap);
                            if (n == 0)
                                png.write(Gdx.files.local(outDir + "/lab/Landscape/" + name + "_angle" + i + "_" + f + ".png"), renderer.palettePixmap);
                        } finally {
                            pixmap.dispose();
                        }
                    }
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
//            SpecialRenderer.shrink = 1;
            renderer = new SpecialRenderer(voxels.grids.get(0).length, Stuff.STUFFS_B);
            renderer.palette(Coloring.BETTS64);
            renderer.saturation(0f);
            
        } catch (FileNotFoundException e) {
            voxels = new VoxModel();
        }
    }
}
