package isonomicon;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3WindowAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.ScreenUtils;
import squidpony.squidmath.CrossHash;

public class SpecialViewer extends ApplicationAdapter {
    public static final String vertex = "attribute vec4 a_position;\n" +
            "attribute vec4 a_color;\n" +
            "attribute vec2 a_texCoord0;\n" +
            "uniform mat4 u_projTrans;\n" +
            "varying vec4 v_color;\n" +
            "varying vec2 v_texCoords;\n" +
            "void main()\n" +
            "{\n" +
            "v_color = a_color;\n" +
            "v_color.a = v_color.a * (255.0/254.0);\n" +
            "v_texCoords = a_texCoord0;\n" +
            "gl_Position = u_projTrans * a_position;\n" +
            "}\n";
    public static final String fragment =
            "#ifdef GL_ES\n" +
                    "#define LOWP lowp\n" +
                    "precision mediump float;\n" +
                    "#else\n" +
                    "#define LOWP\n" +
                    "#endif\n" +
                    "varying LOWP vec4 v_color;\n" +
                    "varying vec2 v_texCoords;\n" +
                    "uniform sampler2D u_texture;\n" +
                    "uniform sampler2D u_texPalette;\n" +
                    "void main()\n" +
                    "{\n" +
                    "vec4 color = texture2D(u_texture, v_texCoords);\n" +
                    "vec2 index = vec2(color.r * (254.0 / 255.0), v_color.r);\n" +
                    "gl_FragColor = vec4(texture2D(u_texPalette, index).rgb, color.a);\n" +
                    "}\n";

    public ShaderProgram indexShader;

    public Texture palettes;
    public Texture image;

    public SpriteBatch batch;

    @Override
    public void create() {
        palettes = new Texture("palettes/palettes.png");
        image = new Texture(Gdx.files.local("out/special_lab/Eye_Tyrant/Eye_Tyrant_angle0_0.png"));
        batch = new SpriteBatch();
        indexShader = new ShaderProgram(vertex, fragment);
        if (!indexShader.isCompiled()) throw new GdxRuntimeException("Error compiling shader: " + indexShader.getLog());
    }

    @Override
    public void render() {
        ScreenUtils.clear(0.5f, 0.5f, 0.55f, 1f);
        batch.setShader(indexShader);
        Gdx.gl.glActiveTexture(GL20.GL_TEXTURE1);
        palettes.bind();
        batch.begin();

        indexShader.setUniformi("u_texPalette", 1);
        Gdx.gl.glActiveTexture(GL20.GL_TEXTURE0);
        batch.setColor(0f, 0.5f, 0.5f, 1f);
        batch.draw(image, 0, 0);
        batch.end();
    }


    public static void main(String[] arg) {
        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
        config.setTitle("Isonomicon Test: Special Viewer");
        config.setWindowedMode(512, 512);
        config.setIdleFPS(10);
        config.useVsync(true);
        config.setResizable(false);
        final SpecialViewer app = new SpecialViewer();
        new Lwjgl3Application(app, config);
    }

}
