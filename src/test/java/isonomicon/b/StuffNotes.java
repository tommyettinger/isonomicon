package isonomicon.b;

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
    public static final int WIDTH = 880;
    public static final int HEIGHT = 900;
    public BitmapFont font;
    public SpriteBatch batch;
    public Texture pixel;

    private PixmapIO.PNG png;

    private static final Stuff[] STUFFS = Stuff.STUFFS_B;

    @Override
    public void create() {
//        Texture fontTex = new Texture(Gdx.files.internal("canada1500.png"), true);
        Texture fontTex = new Texture(Gdx.files.internal("GentiumUnItalic-standard.png"), true);
        fontTex.setFilter(Texture.TextureFilter.MipMapLinearNearest, Texture.TextureFilter.MipMapLinearNearest);
//        fontTex.setFilter(Texture.TextureFilter.MipMapLinearLinear, Texture.TextureFilter.MipMapLinearLinear);
//        font = new BitmapFont(Gdx.files.internal("canada1500.fnt"), new TextureRegion(fontTex), false);
        font = new BitmapFont(Gdx.files.internal("GentiumUnItalic-standard.fnt"), new TextureRegion(fontTex), false);
        font.setUseIntegerPositions(false);
        font.getData().setScale(0.75f);
//        font.getData().setScale(0.3f);
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
        for (int i = 1, r = 0; i < 128;) {
            for (int n = 0; n < 8 && i < 128; n++, i++) {
                batch.setColor(batch.getColor().set(Coloring.BETTS64[i]));
                batch.draw(pixel, Gdx.graphics.getWidth() * n / 8f, Gdx.graphics.getHeight() * r / 16f, Gdx.graphics.getWidth() / 8f, Gdx.graphics.getHeight() / 16f);
//                font.setColor(batch.getColor().set(Color.rgba8888(batch.getColor()) ^ 0xffffff00));
//                font.setColor((batch.getColor().r + 0.4f) % 1f, (batch.getColor().g + 0.4f) % 1f, (batch.getColor().b + 0.4f) % 1f, 1f);
                font.setColor(batch.getColor().r + batch.getColor().g + batch.getColor().b >= 1f ? Color.BLACK : Color.WHITE);
                font.draw(batch, STUFFS[i].name, Gdx.graphics.getWidth() * n / 8f, Gdx.graphics.getHeight() * (r + 0.92f) / 16f, Gdx.graphics.getWidth() / 8f, Align.center, true);
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
                png.write(Gdx.files.local("Notes_B_Palette.png"), pixmap);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
//        PixmapIO.writePNG(Gdx.files.local("Notes_B_Palette.png"), Pixmap.createFromFrameBuffer(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight()), 6, false);
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
        config.setIdleFPS(10);
        config.setForegroundFPS(60);
        config.useVsync(true);
        config.setResizable(false);
        config.disableAudio(true);
        final StuffNotes app = new StuffNotes();
        new Lwjgl3Application(app, config);
    }

}

