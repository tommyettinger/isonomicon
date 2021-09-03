package isonomicon.app;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.TimeUtils;
import com.github.tommyettinger.anim8.AnimatedGif;
import com.github.tommyettinger.anim8.AnimatedPNG;
import com.github.tommyettinger.anim8.Dithered;
import com.github.tommyettinger.anim8.PaletteReducer;
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
import java.io.IOException;
import java.util.ArrayList;

public class ColorGuardAssets extends ApplicationAdapter {
    public static final int SCREEN_WIDTH = 512;//640;
    public static final int SCREEN_HEIGHT = 512;//720;
    private SpecialRenderer renderer;
    private VoxModel voxels, head;
    private String name;
    private String[] inputs;
    private PixmapIO.PNG png;
    private AnimatedGif gif;
    private AnimatedPNG apng;
    private SpriteBatch batch;
    private Texture palette;
    public ColorGuardAssets() {
//            inputs = new String[]{"vox/Box.vox", "vox/Damned.vox", "vox/Lomuk.vox", "vox/Eye_Tyrant.vox", "vox/Direction_Cube.vox", "vox/Infantry.vox", "vox/Infantry_Firing.vox", "vox/Tree.vox", "vox/teapot.vox"};
//            inputs = new String[]{"vox/Eye_Tyrant_Floor.vox", "vox/Eye_Tyrant.vox", "vox/Bear.vox", "vox/Infantry_Firing.vox", "vox/Lomuk.vox", "vox/Tree.vox"};
//            inputs = new String[]{"vox/Eye_Tyrant.vox", "vox/Bear.vox", "vox/Infantry_Firing.vox", "vox/Tree.vox"};
//            inputs = new String[]{"vox/Eye_Tyrant.vox", "vox/Infantry_Firing.vox", "vox/Lomuk.vox", "vox/Tree.vox", "vox/LAB.vox"};
//            inputs = new String[]{"vox/Lomuk.vox", "vox/Tree.vox", "vox/Eye_Tyrant.vox", "vox/IPT.vox", "vox/LAB.vox"};
//            inputs = new String[]{"vox/Infantry_Firing.vox"};
//            inputs = new String[]{"vox/Infantry.vox"};
//            inputs = new String[]{"vox/Materials.vox"};
//            inputs = new String[]{"vox/IPT_No_Pow.vox"};
//            inputs = new String[]{"vox/Box.vox", "vox/Direction_Cube.vox"};
//            inputs = new String[]{"vox/IPT_Original.vox"};
//            inputs = new String[]{"vox/IPT.vox"};
//            inputs = new String[]{"vox/LAB.vox"};
//            inputs = new String[]{"vox/Oklab.vox"};
//            inputs = new String[]{"vox/Oklab.vox", "vox/LAB.vox", "vox/IPT.vox"};
//            inputs = new String[]{"vox/Eye_Tyrant.vox"};
//            inputs = new String[]{"vox/Floor.vox"};
//            inputs = new String[]{"vox/Bear.vox"};
//            inputs = new String[]{"vox/Lomuk.vox"};
//            inputs = new String[]{"vox/Lomuk.vox", "vox/Damned.vox"};
//            inputs = new String[]{"vox/Damned.vox"};
//            inputs = new String[]{"vox/Phantom_Wand.vox"};
//            inputs = new String[]{"vox/Tree.vox"};
//            inputs = new String[]{"vox/teapot.vox"};
//            inputs = new String[]{"vox/Figure.vox"};
//            inputs = new String[]{"b/vox/Figure.vox", "b/vox/Tree.vox"};
//            inputs = new String[]{"b/vox/Figure_Split.vox", "palettes/b/TanClothDarkSkin.png"};
//            inputs = new String[]{"b/vox/Damned.vox", "palettes/b/CherrySkinDarkCloth.png"};
//            inputs = new String[]{"b/vox/Direction_Cube.vox", "palettes/b/TanClothDarkSkin.png"};
//            inputs = new String[]{"b/vox/Lomuk.vox", "palettes/b/BlueFurCyanCrystal.png"};
        inputs = new String[]{
                "Tank.vox", "palettes/b/ColorGuardBaseDark.png"};
        if (!new File("specialized/b/vox/color_guard/" + inputs[0]).exists()) {
            System.out.println("File not found: specialized/b/vox/color_guard/" + inputs[0]);
            System.exit(0);
        }
        try {
            head = VoxIOExtended.readVox(new LittleEndianDataInputStream(new FileInputStream("specialized/b/vox/color_guard/human/Head.vox")));
        }
        catch (FileNotFoundException ignored){
            System.exit(0);
        }
    }
    @Override
    public void create() {
        if (inputs == null) Gdx.app.exit();

        ShaderProgram indexShader = new ShaderProgram(ShaderUtils.stuffSelectVertex, ShaderUtils.stuffSelectFragment);
        if (!indexShader.isCompiled()) throw new GdxRuntimeException("Error compiling shader: " + indexShader.getLog());
        batch = new SpriteBatch(256, indexShader);

        long startTime = TimeUtils.millis();
        png = new PixmapIO.PNG();
        png.setCompression(2); // we are likely to compress these with something better, like oxipng.
        gif = new AnimatedGif();
        apng = new AnimatedPNG();
        gif.setDitherAlgorithm(Dithered.DitherAlgorithm.SCATTER);
        gif.palette = new PaletteReducer(Coloring.BETSY256, Gdx.files.local("assets/BetsyPreload.dat").readBytes());
        gif.palette.setDitherStrength(1f);
        for (int n = 0; n < inputs.length; n++) {
            String s = inputs[n++];
            palette = new Texture(Gdx.files.local("assets/"+inputs[n]));
            System.out.println("Rendering " + s);
            load("specialized/b/vox/color_guard/"+s);
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
                    pixmap = renderer.drawModel2(voxels, i * 0.25f, 0f, 0f, f, 0, 0, 0);
                    for (int j = 0; j < voxels.grids.size(); j++) {
                        Stuff.evolve(Stuff.STUFFS_B, voxels.grids.get(j), f);
                    }
                    Texture t = new Texture(pixmap.getWidth(), pixmap.getHeight(), Pixmap.Format.RGBA8888);
                    t.draw(renderer.palettePixmap, 0, 0);
                    FrameBuffer fb = new FrameBuffer(Pixmap.Format.RGBA8888, Gdx.graphics.getBackBufferWidth(), Gdx.graphics.getBackBufferHeight(), false);
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
                        png.write(Gdx.files.local("out/color_guard/render/" + name + '/' + name + "_angle" + i + "_" + f + ".png"), pixmap);
                        png.write(Gdx.files.local("out/color_guard/lab/" + name + '/' + name + "_angle" + i + "_" + f + ".png"), renderer.palettePixmap);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    fb.dispose();
                    t.dispose();
//                png8.write(Gdx.files.local("out/" + name + '/' + name + "_angle" + i + ".png"), p, false, true);
                }
                pm.insertRange(pm.size - 4, 4);
            }
//                gif.palette.analyze(pm);
            gif.write(Gdx.files.local("out/color_guard/render/" + name + '/' + name + ".gif"), pm, 8);
//                gif.palette.exact(Coloring.HALTONITE240, PRELOAD);
//                gif.write(Gdx.files.local("out/" + name + '/' + name + "-256-color.gif"), pm, 1);
            apng.write(Gdx.files.local("out/color_guard/render/" + name + '/' + name + ".png"), pm, 8);
            for (Pixmap pix : pm) {
                if(!pix.isDisposed())
                    pix.dispose();
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
            SpecialRenderer.shrink = 1;
            renderer = new SpecialRenderer(voxels.grids.get(0).length, Stuff.STUFFS_B);
            renderer.palette(Coloring.BETTS64);
            renderer.saturation(0f);
            
        } catch (FileNotFoundException e) {
            voxels = new VoxModel();
        }
    }
}
