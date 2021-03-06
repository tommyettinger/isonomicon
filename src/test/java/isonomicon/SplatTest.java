package isonomicon;

import com.badlogic.gdx.*;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3WindowAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.scenes.scene2d.utils.UIUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import isonomicon.io.LittleEndianDataInputStream;
import isonomicon.io.VoxIO;
import isonomicon.physical.ModelMaker;
import isonomicon.physical.Tools3D;
import isonomicon.visual.Colorizer;
import isonomicon.visual.SplatRenderer;
import squidpony.FakeLanguageGen;
import squidpony.squidmath.CrossHash;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class SplatTest extends ApplicationAdapter {
    public static final int SCREEN_WIDTH = 512;//640;
    public static final int SCREEN_HEIGHT = 512;//720;
    public static final int VIRTUAL_WIDTH = SCREEN_WIDTH;
    public static final int VIRTUAL_HEIGHT = SCREEN_HEIGHT;
    protected SpriteBatch batch;
    protected Viewport worldView;
    protected Viewport screenView;
    protected BitmapFont font;
    protected FrameBuffer buffer;
    protected Texture screenTexture, pmTexture;
    protected ModelMaker maker;
    private SplatRenderer renderer;
    private byte[][][] voxels;
    private Colorizer colorizer;
    
    @Override
    public void create() {
        font = new BitmapFont(Gdx.files.internal("font.fnt"));
        batch = new SpriteBatch();
        worldView = new FitViewport(VIRTUAL_WIDTH, VIRTUAL_HEIGHT);
        screenView = new FitViewport(VIRTUAL_WIDTH, VIRTUAL_HEIGHT);
        buffer = new FrameBuffer(Pixmap.Format.RGBA8888, VIRTUAL_WIDTH, VIRTUAL_HEIGHT, false, false);
        screenView.getCamera().position.set(VIRTUAL_WIDTH / 2, VIRTUAL_HEIGHT / 2, 0);
        screenView.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        batch.enableBlending();
//        colorizer = Colorizer.arbitraryColorizer(Coloring.AURORA);
//        colorizer = Colorizer.arbitraryColorizer(Coloring.DB16);
//        colorizer = Colorizer.arbitraryColorizer(Coloring.GB_GREEN);
//        colorizer = Colorizer.arbitraryColorizer(Coloring.DB32);
//        colorizer = Colorizer.arbitraryColorizer(Coloring.BLK36);
//        colorizer = Colorizer.arbitraryBonusColorizer(Coloring.UNSEVEN);
//        colorizer = Colorizer.arbitraryBonusColorizer(Coloring.CW_PALETTE);
//        colorizer = Colorizer.arbitraryBonusColorizer(Coloring.JAPANESE_WOODBLOCK);
//        colorizer = Colorizer.arbitraryBonusColorizer(Coloring.FLESURRECT);
//        colorizer = Colorizer.arbitraryBonusColorizer(Coloring.GB_GREEN);
//        colorizer = Colorizer.FlesurrectColorizer;
//        colorizer = Colorizer.AzurestarColorizer;
//        colorizer = Colorizer.SplayColorizer;
        
        colorizer = Colorizer.ManosColorizer;
//        colorizer = Colorizer.ManossusColorizer;
        renderer = new SplatRenderer(80).colorizer(colorizer);
//        renderer.alternate = Colorizer.ManossusColorizer;
        pmTexture = new Texture(VIRTUAL_WIDTH, VIRTUAL_HEIGHT, Pixmap.Format.RGBA8888);
        maker = new ModelMaker(-1L, colorizer);
        load("assets/vox/Materials.vox");
//        voxels = maker.shipSmoothColorized();
        Gdx.input.setInputProcessor(inputProcessor());
    }

    @Override
    public void render() {
//        model.setFrame((int)(TimeUtils.millis() >>> 7) & 15);
//        boom.setFrame((int)(TimeUtils.millis() >>> 7) & 15);
        buffer.begin();
        
        Gdx.gl.glClearColor(0.4f, 0.75f, 0.3f, 1f);
        // for GB_GREEN palette
//        Gdx.gl.glClearColor(0xE0 / 255f, 0xF8 / 255f, 0xD0 / 255f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        
        worldView.apply();
        worldView.getCamera().position.set(VIRTUAL_WIDTH / 2, VIRTUAL_HEIGHT / 2, 0);
        worldView.update(VIRTUAL_WIDTH, VIRTUAL_HEIGHT);
        batch.setProjectionMatrix(screenView.getCamera().combined);
        batch.begin();
        pmTexture.draw(renderer.drawSplats(voxels), 0, 0);
        batch.draw(pmTexture,
                0,
                0);
        //batch.setColor(-0x1.fffffep126f); // white as a packed float, resets any color changes that the renderer made
        batch.end();
        buffer.end();
        Gdx.gl.glClearColor(0, 0, 0, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        screenView.apply();
        batch.setProjectionMatrix(screenView.getCamera().combined);
        batch.begin();
        screenTexture = buffer.getColorBufferTexture();
        screenTexture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
        batch.draw(screenTexture, 0, 0);
//        font.setColor(1f, 1f, 1f, 1f);
//        font.draw(batch, Gdx.graphics.getFramesPerSecond() + " FPS", 0, 20);
        batch.end();
    }

    @Override
    public void resize(int width, int height) {
        screenView.update(width, height);
    }

    public static void main(String[] arg) {
        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
        config.setTitle("Isonomicon Test: SlopeBox");
        config.setWindowedMode(SCREEN_WIDTH, SCREEN_HEIGHT);
        config.setIdleFPS(10);
        config.useVsync(true);
        config.setResizable(true);
        final SplatTest app = new SplatTest();
        config.setWindowListener(new Lwjgl3WindowAdapter() {
            @Override
            public void filesDropped(String[] files) {
                if (files != null && files.length > 0) {
                    if (files[0].endsWith(".vox"))
                        app.load(files[0]);
//                    else if (files[0].endsWith(".hex"))
//                        app.loadPalette(files[0]);
                    app.maker.rng.setState(CrossHash.hash64(files[0]));
                }
            }
        });
        new Lwjgl3Application(app, config);
    }

    public InputProcessor inputProcessor() {
        return new InputAdapter() {
            @Override
            public boolean keyDown(int keycode) {
                switch (keycode) {
                    case Input.Keys.P:
                        Tools3D.fill(voxels, 0);
                        Tools3D.translateCopyInto(maker.shipSmoothColorized(), voxels,
                                voxels.length - 12 >> 1, voxels[0].length - 12 >> 1, voxels[0][0].length - 8 >> 1);
                        break;
                    case Input.Keys.D: // dither
                        renderer.dither = !renderer.dither;
                        break;
                    case Input.Keys.U: // useAlternate
                        renderer.useAlternate = !renderer.useAlternate;
                        break;
                    case Input.Keys.F: // fringe, affects outline/edge
                        renderer.outline = !renderer.outline;
                        break;
                    case Input.Keys.R: // rotate
//                        System.out.println("(0x7F) before: " + Tools3D.count(seq.data[1], 0x7F));
                        Tools3D.clockwiseInPlace(voxels);
//                        System.out.println("(0xBF) after : " + Tools3D.count(seq.data[1], 0xBF));
                        break;
                    case Input.Keys.A: //  a-z, aurora and ziggurat colorizers
                        if (UIUtils.shift())
                        {
                            renderer.colorizer(Colorizer.ZigguratColorizer);
                            maker.setColorizer(Colorizer.ZigguratColorizer);
                        }
                        else
                        {
                            renderer.colorizer(Colorizer.AuroraColorizer);
                            maker.setColorizer(Colorizer.AuroraColorizer);
                        }
                        break;
                    case Input.Keys.S: // smaller palette, 64 colors
                        if (UIUtils.shift())
                        {
                            renderer.colorizer(Colorizer.AzurestarColorizer);
                            maker.setColorizer(Colorizer.AzurestarColorizer);
                        }
                        else 
                        {
                            renderer.colorizer(Colorizer.ManosColorizer);
                            maker.setColorizer(Colorizer.ManosColorizer);
                        }
                        break;
                    case Input.Keys.W: // write
                        VoxIO.writeVOX(FakeLanguageGen.MALAY.word(Tools3D.hash64(voxels), true) + ".vox", voxels, maker.getColorizer().getReducer().paletteArray);
                        break;
                    case Input.Keys.ESCAPE:
                        Gdx.app.exit();
                        break;
                }
                return true;
            }
        };
    }
    public void load(String name) {
        try {
            //// loads a file by its full path, which we get via drag+drop
            voxels = VoxIO.readVox(new LittleEndianDataInputStream(new FileInputStream(name)));
            renderer.colorizer(Colorizer.arbitraryColorizer(VoxIO.lastPalette));
        } catch (FileNotFoundException e) {
            voxels = maker.shipSmoothColorized();
            renderer.colorizer(colorizer);
        }
    }
}
