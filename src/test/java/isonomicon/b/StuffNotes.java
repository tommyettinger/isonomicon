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
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;
import isonomicon.physical.Stuff;
import isonomicon.visual.Coloring;

public class StuffNotes extends ApplicationAdapter {
    public BitmapFont font;
    public SpriteBatch batch;
    public Texture pixel;

    private PixmapIO.PNG png;

    private static final Stuff[] STUFFS = Stuff.STUFFS_B;

    @Override
    public void create() {
        Texture fontTex = new Texture(Gdx.files.internal("canada1500.png"), true);
        fontTex.setFilter(Texture.TextureFilter.MipMapLinearNearest, Texture.TextureFilter.MipMapLinearNearest);
        font = new BitmapFont(Gdx.files.internal("canada1500.fnt"), new TextureRegion(fontTex));
        font.setUseIntegerPositions(false);
        font.getData().setScale(0.2f);
        Pixmap px = new Pixmap(3, 3, Pixmap.Format.RGBA8888);
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
        ScreenUtils.clear(0f, 0f, 0f, 1f);

        batch.begin();
        for (int i = 1, r = 0; i < 128;) {
            for (int n = 0; n < 8 && i < 128; n++, i++) {
                batch.setColor(batch.getColor().set(Coloring.BETTS64[i]));
                batch.draw(pixel, Gdx.graphics.getWidth() * n / 8f, Gdx.graphics.getHeight() * r / 16f, Gdx.graphics.getWidth() / 8f, Gdx.graphics.getHeight() / 16f);
//                font.setColor(batch.getColor().set(Color.rgba8888(batch.getColor()) ^ 0xffffff00));
                font.setColor((batch.getColor().r + 0.4f) % 1f, (batch.getColor().g + 0.4f) % 1f, (batch.getColor().b + 0.4f) % 1f, 1f);
                font.draw(batch, STUFFS[i].name, Gdx.graphics.getWidth() * n / 8f, Gdx.graphics.getHeight() * (r + 0.92f) / 16f, Gdx.graphics.getWidth() / 8f, Align.center, true);
            }
            r++;
        }
        batch.end();
    }


    public static void main(String[] arg) {
        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
        config.setTitle("Isonomicon Test: Special Viewer");
        config.setWindowedMode(600, 900);
        config.setIdleFPS(10);
        config.setForegroundFPS(60);
        config.useVsync(true);
        config.setResizable(false);
        config.disableAudio(true);
        final StuffNotes app = new StuffNotes();
        new Lwjgl3Application(app, config);
    }

}

