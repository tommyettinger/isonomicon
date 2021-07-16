package isonomicon;

import com.badlogic.gdx.*;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3WindowAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import isonomicon.io.LittleEndianDataInputStream;
import isonomicon.io.VoxIO;
import isonomicon.physical.Tools3D;
import isonomicon.visual.SpecialRenderer;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class RotationVisualizer extends ApplicationAdapter {
    public static final int SCREEN_WIDTH = 800;//640;
    public static final int SCREEN_HEIGHT = 800;//720;
    public static final int VIRTUAL_WIDTH = SCREEN_WIDTH;
    public static final int VIRTUAL_HEIGHT = SCREEN_HEIGHT;
    protected SpriteBatch batch;
    protected Viewport worldView;
    protected Viewport screenView;
    protected FrameBuffer buffer;
    protected Texture screenTexture, pmTexture;
    private SpecialRenderer renderer;
    private byte[][][] voxels;
    private float saturation;
    public float yaw, pitch, roll;
    
    @Override
    public void create() {
        batch = new SpriteBatch();
        saturation = 0f;
        worldView = new FitViewport(VIRTUAL_WIDTH, VIRTUAL_HEIGHT);
        screenView = new FitViewport(VIRTUAL_WIDTH, VIRTUAL_HEIGHT);
        buffer = new FrameBuffer(Pixmap.Format.RGBA8888, VIRTUAL_WIDTH, VIRTUAL_HEIGHT, false, false);
        screenView.getCamera().position.set(VIRTUAL_WIDTH / 2, VIRTUAL_HEIGHT / 2, 0);
        screenView.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        batch.enableBlending();
//        load("vox/libGDX_BadLogic_Logo.vox");
//        load("vox/Infantry_Firing.vox");
//        load("vox/CrazyBox.vox");
//        load("vox/Lomuk.vox");
//        load("vox/Tree.vox");
        load("specialized/vox/Eye_Tyrant.vox");
//        renderer.dither = true;
        Gdx.input.setInputProcessor(inputProcessor());
    }

    @Override
    public void render() {
        if(Gdx.input.isKeyPressed(Input.Keys.U)) 
            yaw += 0.25f * Gdx.graphics.getDeltaTime();
        else if(Gdx.input.isKeyPressed(Input.Keys.J)) 
            yaw -= 0.25f * Gdx.graphics.getDeltaTime();
        else if(Gdx.input.isKeyPressed(Input.Keys.I)) 
            pitch += 0.25f * Gdx.graphics.getDeltaTime();
        else if(Gdx.input.isKeyPressed(Input.Keys.K)) 
            pitch -= 0.25f * Gdx.graphics.getDeltaTime();
        else if(Gdx.input.isKeyPressed(Input.Keys.O)) 
            roll += 0.25f * Gdx.graphics.getDeltaTime();
        else if(Gdx.input.isKeyPressed(Input.Keys.L)) 
            roll -= 0.25f * Gdx.graphics.getDeltaTime();
        else if(Gdx.input.isKeyPressed(Input.Keys.R))
        {
            yaw = pitch = roll = 0f;
        }
//        model.setFrame((int)(TimeUtils.millis() >>> 7) & 15);
//        boom.setFrame((int)(TimeUtils.millis() >>> 7) & 15);
        buffer.begin();
        
        Gdx.gl.glClearColor(0.4f, 0.75f, 0.3f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        
        worldView.apply();
        worldView.getCamera().position.set(VIRTUAL_WIDTH / 2, VIRTUAL_HEIGHT / 2, 0);
        worldView.update(VIRTUAL_WIDTH, VIRTUAL_HEIGHT);
        batch.setProjectionMatrix(worldView.getCamera().combined);
        batch.begin();
//        pmTexture.draw(renderer.drawSplatsHalf(voxels, 0f, (TimeUtils.millis() & 2047) * 0x1p-11f, 0f), 0, 0);
        renderer.drawSplats(voxels, yaw, pitch, roll, (int) (TimeUtils.millis() >>> 8 & 3), 0, 0, 0);
        pmTexture.draw(renderer.pixmap, 0, 0);
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
        config.setTitle("Splat Drawing Test");
        config.setWindowedMode(SCREEN_WIDTH, SCREEN_HEIGHT);
        config.setIdleFPS(10);
        config.useVsync(false);
        config.setResizable(true);
        config.disableAudio(true);
        final RotationVisualizer app = new RotationVisualizer();
        config.setWindowListener(new Lwjgl3WindowAdapter() {
            @Override
            public void filesDropped(String[] files) {
                if (files != null && files.length > 0) {
                    if (files[0].endsWith(".vox"))
                        app.load(files[0]);
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
                    case Input.Keys.F: // fringe, affects outline/edge
                        renderer.outline = !renderer.outline;
                        break;
                    case Input.Keys.UP:
                        renderer.saturation(saturation = Math.min(1f, saturation + 0.01f));
                        System.out.println(saturation);
                        break;
                    case Input.Keys.DOWN:
                        renderer.saturation(saturation = Math.max(-1f, saturation - 0.01f));
                        System.out.println(saturation);
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
        if(pmTexture != null) pmTexture.dispose();
        pmTexture = new Texture(VIRTUAL_WIDTH, VIRTUAL_HEIGHT, Pixmap.Format.RGBA8888);
        try {
            //// loads a file by its full path, which we get via drag+drop
            byte[][][] v = VoxIO.readVox(new LittleEndianDataInputStream(new FileInputStream(name)));
            if(v == null) {
                voxels = new byte[][][]{{{1}}};
                return;
            }
            v = Tools3D.soak(v);
//            voxels = new byte[v.length * 3 >> 1][v.length * 3 >> 1][v.length * 3 >> 1];
//            Tools3D.translateCopyInto(v, voxels, v.length >> 2, v.length >> 2, v.length >> 2);
            voxels = v;
            renderer = new SpecialRenderer(voxels.length);
            renderer.palette(VoxIO.lastPalette);
        } catch (FileNotFoundException e) {
            voxels = new byte[][][]{{{1}}};
        }
    }
}
