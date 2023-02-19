package isonomicon.testing;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.Gdx2DPixmap;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.github.tommyettinger.anim8.*;
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
import java.nio.ByteBuffer;
import java.util.ArrayList;

public class ShaderTest extends ApplicationAdapter {
    public static final int SCREEN_WIDTH = 1024;//640;
    public static final int SCREEN_HEIGHT = 1024;//720;
    private FastPNG png;
//    private PixmapIO.PNG png;
    private SpriteBatch batch;
    public ShaderTest(String[] args){
    }

    public static Pixmap createFromFrameBuffer(int x, int y, int w, int h) {
        Gdx.gl.glPixelStorei(GL20.GL_PACK_ALIGNMENT, 1);
        Pixmap pixmap = new Pixmap(new Gdx2DPixmap(w, h, Gdx2DPixmap.GDX2D_FORMAT_RGBA8888));
        ByteBuffer pixels = pixmap.getPixels();
        Gdx.gl.glReadPixels(x, y, w, h, GL20.GL_RGBA, GL20.GL_UNSIGNED_BYTE, pixels);
        return pixmap;
    }

    @Override
    public void create() {
        ShaderProgram indexShader = new ShaderProgram(ShaderUtils.vertexShader, """
                #ifdef GL_ES
                precision lowp float;
                #endif

                varying vec4 v_color;
                varying vec2 v_texCoords;
                uniform sampler2D u_texture;

                const vec3 GRASS_COLOR = vec3(0, 1, 0);
                const vec2 C1 = vec2(12.9898,78.233);
                const float C2 = 43758.5453;
                float rand(vec2 co){
                    return fract(sin(dot(co, C1)) * C2);
                    //    return sin(dot(co, C1)) * C2;
                }
                                
                float hash12(vec2 p)
                {
                	vec3 p3  = fract(vec3(p.xyx) * .1031);
                    p3 += dot(p3, p3.yzx + 33.33);
                    return fract((p3.x + p3.y) * p3.z);
                }
                                
                void main() {
                    vec4 color = texture2D(u_texture, v_texCoords);

                    if (color.g - (color.r + color.b) * 0.5 > 0.3) {
                        color.rgb = vec3(
                            0.196 + rand(v_texCoords.xy) * 0.094,
                            0.509 + rand(v_texCoords.yx) * 0.118,
                            0);
                    }

                    gl_FragColor = color;
                }""");
//        Viewport viewport = new StretchViewport(SCREEN_WIDTH, SCREEN_HEIGHT);
//        viewport.update(SCREEN_WIDTH, SCREEN_HEIGHT);
//        viewport.apply();
        if (!indexShader.isCompiled()) throw new GdxRuntimeException("Error compiling shader: " + indexShader.getLog());
        batch = new SpriteBatch(256, indexShader);

        long startTime = TimeUtils.millis();
//        Gdx.files.local("out/vox/").mkdirs();
        png = new FastPNG(1 << 10);
//        png = new PixmapIO.PNG();
        png.setCompression(2); // we are likely to compress these with something better, like oxipng.
        png.setFlipY(false);

        Pixmap pixmap;
        Texture t = new Texture(Gdx.files.local("src/test/resources/Color_Guard.png"));
        FrameBuffer fb = new FrameBuffer(Pixmap.Format.RGBA8888, SCREEN_WIDTH, SCREEN_HEIGHT, false);
        fb.begin();
        ScreenUtils.clear(Color.CLEAR);
        batch.begin();

        batch.draw(t, 0, t.getHeight(), t.getWidth(), -t.getHeight());
        batch.end();

//                    pixmap = Pixmap.createFromFrameBuffer(0, 0, t.getWidth(), t.getHeight());
        //// The above is equivalent to the following, but the above also fills the pixmap.
        pixmap = createFromFrameBuffer(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);

        fb.end();
//        try {
            png.write(Gdx.files.local("out/testing/greenTest.png"), pixmap);
//        } catch (IOException e) {
//            e.printStackTrace();
//            throw new RuntimeException(e);
//        }
        fb.dispose();
        t.dispose();
        pixmap.dispose();

        System.out.println("Finished in " + TimeUtils.timeSinceMillis(startTime) * 0.001 + " seconds.");
        Gdx.app.exit();
    }

    @Override
    public void render() {
    }


    public static void main(String[] arg) {
        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
        config.setTitle("Shader Test");
        config.setWindowedMode(1024, 1024);
        config.setIdleFPS(10);
        config.useVsync(true);
        config.setResizable(false);
        config.disableAudio(true);
        final ShaderTest app = new ShaderTest(arg);
        new Lwjgl3Application(app, config);
    }
}
