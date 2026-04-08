package isonomicon.c;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.PixmapIO;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;
import isonomicon.app.Specialist;
import isonomicon.physical.Stuff;
import isonomicon.visual.Coloring;

import java.io.IOException;

public class StuffNotes extends ApplicationAdapter {
    public static final boolean WRITING = true;
    public static final int WIDTH = 8 * 100;
    public static final int HEIGHT = 30 * 32;
    public BitmapFont font;
    public SpriteBatch batch;
    public Texture pixel;

    private PixmapIO.PNG png;

    private static final Stuff[] STUFFS = Stuff.STUFFS_C;

    @Override
    public void create() {
        Texture fontTex = new Texture(Gdx.files.internal("CozetteOutlined-standard.png"), true);
//        fontTex.setFilter(Texture.TextureFilter.MipMapLinearNearest, Texture.TextureFilter.MipMapLinearNearest);
//        fontTex.setFilter(Texture.TextureFilter.MipMapLinearLinear, Texture.TextureFilter.MipMapLinearLinear);
        font = new BitmapFont(Gdx.files.internal("CozetteOutlined-standard.fnt"), new TextureRegion(fontTex), false);
        font.setUseIntegerPositions(true);
        Pixmap px = new Pixmap(3, 3, Pixmap.Format.RGB888);
        px.setColor(Color.WHITE);
        px.fill();
        pixel = new Texture(px);
        png = new PixmapIO.PNG(1024);
        batch = new SpriteBatch();
        Gdx.input.setInputProcessor(new InputAdapter(){
            @Override
            public boolean keyDown(int keycode) {
                switch (keycode){
                    case Input.Keys.ESCAPE:
                    case Input.Keys.Q:
                        Gdx.app.exit();
                        break;
                }
                return false;
            }
        });
    }

    @Override
    public void render() {
        ScreenUtils.clear(Color.BLACK);
        Texture t = new Texture(WIDTH, HEIGHT, Pixmap.Format.RGB888);
        FrameBuffer fb = new FrameBuffer(Pixmap.Format.RGB888, WIDTH, HEIGHT, false);
        fb.begin();
        ScreenUtils.clear(Color.BLACK);

        batch.begin();
        for (int i = 1, r = 0; i < 256;) {
            for (int n = 0; n < 8 && i < 256; n++, i++) {
                batch.setColor(batch.getColor().set(Coloring.YAM4[i]));
                batch.draw(pixel, Gdx.graphics.getWidth() * n / 8f, Gdx.graphics.getHeight() * r / 32f, Gdx.graphics.getWidth() / 8f, Gdx.graphics.getHeight() / 32f);
//                font.setColor(batch.getColor().r + batch.getColor().g + batch.getColor().b >= 1f ? Color.BLACK : Color.WHITE);
                font.draw(batch, STUFFS[i].name, Gdx.graphics.getWidth() * n / 8f, Gdx.graphics.getHeight() * (r + 0.875f) / 32f, Gdx.graphics.getWidth() / 8f, Align.center, true);
            }
            r++;
        }
        batch.end();
//        Pixmap pixmap = Pixmap.createFromFrameBuffer(0, 0, t.getWidth(), t.getHeight());
        Pixmap pixmap = Specialist.createFromFrameBuffer(0, 0, t.getWidth(), t.getHeight());
        t.draw(pixmap, 0, 0);
        fb.end();

        if(WRITING) {
            try {
                png.write(Gdx.files.local("Notes_C_Palette.png"), pixmap);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
//        PixmapIO.writePNG(Gdx.files.local("Notes_C_Palette.png"), Pixmap.createFromFrameBuffer(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight()), 6, false);
        ScreenUtils.clear(Color.BLACK);
        batch.begin();
        batch.setColor(Color.WHITE);
        batch.draw(t, 0, HEIGHT, WIDTH, -HEIGHT);
        batch.end();
        fb.dispose();
        pixmap.dispose();
    }


    public static void main(String[] arg) {
        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
        config.setTitle("Isonomicon Test: Stuff Notes");
        config.setWindowedMode(WIDTH, HEIGHT);
        config.setIdleFPS(1);
        config.setForegroundFPS(1);
        config.useVsync(true);
        config.setResizable(false);
        config.disableAudio(true);
        final StuffNotes app = new StuffNotes();
        new Lwjgl3Application(app, config);
    }

}

