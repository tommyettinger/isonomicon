package isonomicon;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.scenes.scene2d.utils.UIUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import isonomicon.model.SlopeBox;
import isonomicon.model.color.Colorizer;
import isonomicon.view.render.VoxelPixmapRenderer;

public class SlopeBoxTest extends ApplicationAdapter {
    public static final int SCREEN_WIDTH = 320;//640;
    public static final int SCREEN_HEIGHT = 360;//720;
    public static final int VIRTUAL_WIDTH = 320;
    public static final int VIRTUAL_HEIGHT = 360;
    protected SpriteBatch batch;
    protected Viewport worldView;
    protected Viewport screenView;
    protected BitmapFont font;
    protected FrameBuffer buffer;
    protected Texture screenTexture, pmTexture;
    protected TextureRegion screenRegion;
    protected ModelMaker maker;
    private VoxelPixmapRenderer pixmapRenderer;
    private byte[][][] voxels;
    private SlopeBox seq;
    private Colorizer colorizer;
    
    @Override
    public void create() {
        font = new BitmapFont(Gdx.files.internal("font.fnt"));
        batch = new SpriteBatch();
        worldView = new FitViewport(VIRTUAL_WIDTH, VIRTUAL_HEIGHT);
        screenView = new FitViewport(VIRTUAL_WIDTH, VIRTUAL_HEIGHT);
        buffer = new FrameBuffer(Pixmap.Format.RGBA8888, VIRTUAL_WIDTH, VIRTUAL_HEIGHT, false, false);
        screenRegion = new TextureRegion();
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
//        colorizer = Colorizer.arbitraryBonusColorizer(Coloring.VGA256);
//        colorizer = Colorizer.arbitraryBonusColorizer(Coloring.FLESURRECT);
//        colorizer = Colorizer.arbitraryBonusColorizer(Coloring.GB_GREEN);
//        colorizer = Colorizer.FlesurrectColorizer;
//        colorizer = Colorizer.AzurestarColorizer;
//        colorizer = Colorizer.SplayColorizer;
        colorizer = Colorizer.ZigguratColorizer;
        pixmapRenderer = new VoxelPixmapRenderer().pixmap(new Pixmap(512, 512, Pixmap.Format.RGBA8888)).colorizer(colorizer);
        pixmapRenderer.easing = false;
        pmTexture = new Texture(512, 512, Pixmap.Format.RGBA8888);
        maker = new ModelMaker(-123456789, 1234567890, colorizer);
//        try {
//            box = VoxIO.readVox(new LittleEndianDataInputStream(new FileInputStream("Aurora/dumbcube.vox")));
//        } catch (Exception e) {
//            e.printStackTrace();
//            box = maker.shipNoiseColorized();
//        }
//        makeBoom(maker.fireRange());
//        try {
//            voxels = VoxIO.readVox(new LittleEndianDataInputStream(new FileInputStream("FlesurrectBonus/Damned.vox")));
//        } catch (FileNotFoundException e) {
//            voxels = maker.shipLargeNoiseColorized();
//        }
//        voxels = maker.blobLargeRandom();
        voxels = maker.shipLargeSmoothColorized();
        seq = new SlopeBox(voxels);
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
        pmTexture.draw(SlopeBox.drawIso(seq, pixmapRenderer), 0, 0);
        batch.draw(pmTexture, 64, 64);
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
        screenRegion.setRegion(screenTexture);
        screenRegion.flip(false, true);
        batch.draw(screenRegion, 0, 0);
        //// for GB_GREEN
        //font.setColor(0x34 / 255f, 0x68 / 255f, 0x56 / 255f, 1f);
        font.setColor(1f, 1f, 1f, 1f);
        //font.draw(batch, model.voxels.length + ", " + model.voxels[0].length + ", " + model.voxels[0][0].length + ", " + " (original)", 0, 80);
//        font.draw(batch, model.sizeX() + ", " + model.sizeY() + ", " + model.sizeZ() + " (sizes)", 0, 60);
//        font.draw(batch, StringKit.join(", ", model.rotation().rotation()) + " (rotation)", 0, 40);
        font.draw(batch, Gdx.graphics.getFramesPerSecond() + " FPS", 0, 20);
        batch.end();
    }

    @Override
    public void resize(int width, int height) {
        screenView.update(width, height);
    }

    public static void main(String[] arg) {
        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
        config.setTitle("Warp Tester");
        config.setWindowedMode(SCREEN_WIDTH, SCREEN_HEIGHT);
        config.setIdleFPS(10);
        config.useVsync(false);
        config.setResizable(false);
        final SlopeBoxTest app = new SlopeBoxTest();
        new Lwjgl3Application(app, config);
    }

    public InputProcessor inputProcessor() {
        return new InputAdapter() {
            @Override
            public boolean keyDown(int keycode) {
                switch (keycode) {
                    case Input.Keys.P:
//                        Tools3D.deepCopyInto(maker.blobLargeRandom(), voxels);
                        Tools3D.deepCopyInto(maker.shipLargeSmoothColorized(), voxels);
                        Tools3D.deepCopyInto(voxels, seq.data[0]);
                        Tools3D.fill(seq.data[1], -1);
                        seq.putSlopes();
                        break;
                    case Input.Keys.E: // easing
                        pixmapRenderer.easing = !pixmapRenderer.easing;
                        break;
                    case Input.Keys.F: // fringe, affects outline/edge
                        pixmapRenderer.outline = !pixmapRenderer.outline;
                        break;
                    case Input.Keys.T: // try again

                        break;
                    case Input.Keys.A:
                        if (UIUtils.shift())
                        {
                            pixmapRenderer.colorizer(Colorizer.AuroraBonusColorizer);
                            maker.setColorizer(Colorizer.AuroraBonusColorizer);
                        }
                        else
                        {
                            pixmapRenderer.colorizer(Colorizer.AuroraColorizer);
                            maker.setColorizer(Colorizer.AuroraColorizer);
                        }
                        break;
                    case Input.Keys.S: // smaller palette, 64 colors
                        if (UIUtils.shift())
                        {
                            pixmapRenderer.colorizer(Colorizer.AzurestarColorizer);
                            maker.setColorizer(Colorizer.AzurestarColorizer);
                        }
                        else 
                        {
                            pixmapRenderer.colorizer(Colorizer.SplayColorizer);
                            maker.setColorizer(Colorizer.SplayColorizer);
                        }
                        break;
//                    case Input.Keys.W: // write
//                        VoxIO.writeVOX(FakeLanguageGen.SIMPLISH.word(Tools3D.hash64(voxels), true) + ".vox", voxels, maker.getColorizer().getReducer().paletteArray);
//                        break;
                    case Input.Keys.ESCAPE:
                        Gdx.app.exit();
                        break;
                }
                return true;
            }
        };
    }

}
