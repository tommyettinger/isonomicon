package isonomicon.b;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.TimeUtils;
import isonomicon.visual.ShaderUtils;

public class SpecialViewer extends ApplicationAdapter {
    public ShaderProgram indexShader;

    public Texture palettes;
    public Texture[] images;

    public SpriteBatch batch;

    private long startTime;

    @Override
    public void create() {
//        palettes = new Texture("palettes/palettes-b.png");
//        palettes = new Texture("palettes/b/TanClothDarkSkin.png");
//        palettes = new Texture("palettes/b/CherrySkinDarkCloth.png");
        palettes = new Texture("palettes/b/BlueFurCyanCrystal.png");
//        palettes = new Texture("palettes/edited/NaturalWoodAndLeaves.png");
//        String name = "Eye_Tyrant";
        String name = "Lomuk";
//        String name = "Damned";
//        String name = "Tree";
//        String name = "Phantom_Wand";
//        String name = "Figure";

        images = new Texture[32];
        for (int a = 0, i = 0; a < 8; a++) {
            for (int f = 0; f < 4; f++) {
                images[i++] = new Texture(Gdx.files.local("out/b/special_lab/"+name+"/"+name+"_angle"+a+"_"+f+".png"));
            }
        }
        indexShader = new ShaderProgram(ShaderUtils.stuffSelectVertex, ShaderUtils.stuffSelectFragment);
        if (!indexShader.isCompiled()) throw new GdxRuntimeException("Error compiling shader: " + indexShader.getLog());
        batch = new SpriteBatch(256, indexShader);

        startTime = TimeUtils.millis();
    }

    @Override
    public void render() {
        ScreenUtils.clear(0.5f, 0.5f, 0.5f, 1f);
        palettes.bind(1);
        batch.begin();

        indexShader.setUniformi("u_texPalette", 1);
        Gdx.gl.glActiveTexture(GL20.GL_TEXTURE0);
        batch.setColor(0f, 0.5f, 0.5f, 1f);
        final int time = (int) (TimeUtils.timeSinceMillis(startTime) >>> 7);
        batch.draw(images[(time & 3) | (time >>> 1 & 28)], 0, 0);
        batch.end();
    }


    public static void main(String[] arg) {
        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
        config.setTitle("Isonomicon Test: Special Viewer");
        config.setWindowedMode(256, 256);
        config.setIdleFPS(10);
        config.useVsync(true);
        config.setResizable(false);
        config.disableAudio(true);
//        new Specialist(null);
        final SpecialViewer app = new SpecialViewer();
        new Lwjgl3Application(app, config);
    }

}
