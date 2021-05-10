package isonomicon;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.scenes.scene2d.utils.UIUtils;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.TimeUtils;
import com.github.tommyettinger.colorful.FloatColors;
import com.github.tommyettinger.colorful.oklab.ColorTools;
import com.github.tommyettinger.colorful.oklab.Palette;
import isonomicon.physical.Stuff;

/*
           Pixmap pix = new Pixmap(256, 1, Pixmap.Format.RGBA8888);
           for (int i = 1; i < PALETTE.length; i++) {
               pix.drawPixel(i - 1, 0, PALETTE[i]);
           }
           pix.drawPixel(255, 0, 0);
           try {
               png.write(Gdx.files.local("temp.png"), pix, false);
           } catch (IOException e) {
               e.printStackTrace();
           }

    */
public class PaletteDrafter extends ApplicationAdapter {
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
                    "const vec3 forward = vec3(1.0 / 3.0);\n" +
                    "void main()\n" +
                    "{\n" +
                    "  vec4 color = texture2D(u_texture, v_texCoords);\n" +
                    "  vec3 index = vec3(color.rg * (254.0 / 255.0), v_color.r);\n" +
                    "  vec3 tgt = texture2D(u_texPalette, index.xz).rgb;\n" +
                    "  vec3 lab = mat3(+0.2104542553, +1.9779984951, +0.0259040371, +0.7936177850, -2.4285922050, +0.7827717662, -0.0040720468, +0.4505937099, -0.8086757660) *" +
                    "             pow(mat3(0.4121656120, 0.2118591070, 0.0883097947, 0.5362752080, 0.6807189584, 0.2818474174, 0.0514575653, 0.1074065790, 0.6302613616) \n" +
                    "             * (tgt.rgb * tgt.rgb), forward);\n" +
                    "  lab.x = clamp(lab.x + index.y + v_color.g - 0.875, 0.0, 1.0);\n" +
                    "  lab.yz = clamp(lab.yz * (v_color.b + 0.5), -1.0, 1.0);\n" +
                    "  lab = mat3(1.0, 1.0, 1.0, +0.3963377774, -0.1055613458, -0.0894841775, +0.2158037573, -0.0638541728, -1.2914855480) * lab;\n" +
                    "  gl_FragColor = vec4(sqrt(clamp(" +
                    "                 mat3(+4.0767245293, -1.2681437731, -0.0041119885, -3.3072168827, +2.6093323231, -0.7034763098, +0.2307590544, -0.3411344290, +1.7068625689) *\n" +
                    "                 (lab * lab * lab)," +
                    "                 0.0, 1.0)), v_color.a * color.a);\n" +
                    "}\n";

    public ShaderProgram indexShader;
    public ShaderProgram regularShader;

    public Texture palettes;
    public Texture previewTexture;
    public Texture[] images;
    public Pixmap workingPalette;
    public Pixmap preview;

    public int stuffIndex = 1;

    public BitmapFont font;
    public SpriteBatch batch;

    private long startTime;
    private float L = 0.5f, A = 0.5f, B = 0.5f;

    @Override
    public void create() {
        font = new BitmapFont(Gdx.files.internal("font.fnt"));
        workingPalette = new Pixmap(Gdx.files.internal("palettes/repeated-blocks.png"));
        palettes = new Texture(workingPalette);
        preview = new Pixmap(16, 16, Pixmap.Format.RGBA8888);
        preview.setColor(workingPalette.getPixel(stuffIndex & 127, 0));
        preview.fill();
        previewTexture = new Texture(preview);
        String name = "Eye_Tyrant";
//        String name = "Lomuk";

        images = new Texture[32];
        for (int a = 0, i = 0; a < 8; a++) {
            for (int f = 0; f < 4; f++) {
                images[i++] = new Texture(Gdx.files.local("out/special_lab/"+name+"/"+name+"_angle"+a+"_"+f+".png"));
            }
        }
        batch = new SpriteBatch();
        indexShader = new ShaderProgram(vertex, fragment);
        if (!indexShader.isCompiled()) throw new GdxRuntimeException("Error compiling shader: " + indexShader.getLog());
        regularShader = SpriteBatch.createDefaultShader();
        startTime = TimeUtils.millis();
        Gdx.input.setInputProcessor(new InputAdapter(){
            @Override
            public boolean keyDown(int keycode) {
                int old = stuffIndex;
                switch (keycode){
                    case Input.Keys.LEFT:
                        stuffIndex -= 2;
                    case Input.Keys.RIGHT:
                        stuffIndex++;
                        workingPalette.drawPixel(old & 127, 0, ColorTools.toRGBA8888(ColorTools.limitToGamut(L, A, B)));
                        final float oklab = ColorTools.fromRGBA8888(workingPalette.getPixel(stuffIndex & 127, 0));
                        L = ColorTools.channelL(oklab);
                        A = ColorTools.channelA(oklab);
                        B = ColorTools.channelB(oklab);
                        palettes.draw(workingPalette, 0, 0);
                        return true;
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
        boolean changed = false;
        if(Gdx.input.isKeyJustPressed(Input.Keys.SLASH)){
            System.out.printf("limited=%08X L=%1.4f A=%1.4f B=%1.4f\n",
                    Float.floatToRawIntBits(ColorTools.limitToGamut(L, A, B)),
                    L,
                    A,
                    B);
        }
        float step = Gdx.graphics.getDeltaTime() * 0.25f;
        if(Gdx.input.isKeyPressed(Input.Keys.L)){
//            oklab = UIUtils.shift()
//                    ? ColorTools.darken(oklab, step)
//                    : ColorTools.lighten(oklab, step);
//            oklab = FloatColors.lerpFloatColors(oklab, UIUtils.shift() ? Palette.BLACK : Palette.WHITE, step);
            if(UIUtils.shift()) L -= step;
            else L += step;
            changed = true;
        }
        if(Gdx.input.isKeyPressed(Input.Keys.A)){
//            oklab = UIUtils.shift()
//                    ? ColorTools.lowerA(oklab, step)
//                    : ColorTools.raiseA(oklab, step);
            if(UIUtils.shift()) A -= step;
            else A += step;
            changed = true;
        }
        if(Gdx.input.isKeyPressed(Input.Keys.B)){
//            oklab = UIUtils.shift()
//                    ? ColorTools.lowerB(oklab, step)
//                    : ColorTools.raiseB(oklab, step);
            if(UIUtils.shift()) B -= step;
            else B += step;
            changed = true;
        }
        int currentPreview = ColorTools.toRGBA8888(ColorTools.limitToGamut(L, A, B));

        ScreenUtils.clear(0.5f, 0.5f, 0.5f, 1f);
        batch.setShader(indexShader);
        Gdx.gl.glActiveTexture(GL20.GL_TEXTURE1);
        palettes.bind();
        batch.begin();

        indexShader.setUniformi("u_texPalette", 1);
        Gdx.gl.glActiveTexture(GL20.GL_TEXTURE0);
        batch.setColor(0f, 0.5f, 0.5f, 1f);
        batch.draw(images[(int) (TimeUtils.timeSinceMillis(startTime) >>> 8) & 31], 0, 0);
        batch.end();

        batch.setShader(regularShader);

        preview.setColor(currentPreview);
        preview.fill();
        previewTexture.draw(preview, 0, 0);

        batch.begin();
        batch.setPackedColor(Color.WHITE_FLOAT_BITS);
        font.draw(batch, Stuff.STUFFS[stuffIndex + 1 & 127].name, 0, 210, 256, Align.center, false);
        batch.draw(previewTexture, 112, 150);
        batch.end();
    }


    public static void main(String[] arg) {
        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
        config.setTitle("Isonomicon Test: Special Viewer");
        config.setWindowedMode(256, 256);
        config.setIdleFPS(10);
        config.setForegroundFPS(30);
        config.useVsync(true);
        config.setResizable(false);
        final PaletteDrafter app = new PaletteDrafter();
        new Lwjgl3Application(app, config);
    }

}

