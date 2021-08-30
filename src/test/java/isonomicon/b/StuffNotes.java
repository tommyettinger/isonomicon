package isonomicon.b;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.utils.UIUtils;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.TimeUtils;
import com.github.tommyettinger.colorful.oklab.ColorTools;
import isonomicon.physical.Stuff;
import isonomicon.physical.VoxMaterial;
import isonomicon.visual.Coloring;
import squidpony.ArrayTools;
import squidpony.squidmath.OrderedMap;

import java.io.IOException;

public class StuffNotes extends ApplicationAdapter {
    public BitmapFont font;
    public SpriteBatch batch;
    public Texture pixel;

    private PixmapIO.PNG png;

    private static final Stuff[] STUFFS = Stuff.STUFFS_B;

    @Override
    public void create() {
//        Texture fontTex = new Texture(Gdx.files.internal("canada1500.png"), true);
//        fontTex.setFilter(Texture.TextureFilter.MipMapLinearNearest, Texture.TextureFilter.MipMapLinearNearest);
//        font = new BitmapFont(Gdx.files.internal("canada1500.fnt"), new TextureRegion(fontTex));
//        font.getData().setScale(0.2f);
        font = new BitmapFont(Gdx.files.internal("Iosevka_Outlined.fnt"));
        font.setUseIntegerPositions(false);
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
                font.setColor(Color.WHITE);
//                font.setColor((batch.getColor().r + 0.4f) % 1f, (batch.getColor().g + 0.4f) % 1f, (batch.getColor().b + 0.4f) % 1f, 1f);
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
        final StuffNotes app = new StuffNotes();
        new Lwjgl3Application(app, config);
    }

}

