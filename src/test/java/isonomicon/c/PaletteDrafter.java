package isonomicon.c;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.scenes.scene2d.utils.UIUtils;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.TimeUtils;
import com.github.tommyettinger.colorful.oklab.ColorTools;
import com.github.tommyettinger.digital.MathTools;
import com.github.tommyettinger.ds.ObjectObjectOrderedMap;
import isonomicon.physical.Stuff;

import java.io.IOException;

public class PaletteDrafter extends ApplicationAdapter {
    /**
     * Change this if ColorGuardAssets was just run, to match the current date.
     */
    public static final String outDir = "out/c/special_lab/";

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
                    "  lab.x = clamp(lab.x + index.y + v_color.g - 0.75, 0.0, 1.0);\n" +
                    "  lab.yz = clamp(lab.yz * (v_color.b + 0.5), -1.0, 1.0);\n" +
                    "  lab = mat3(1.0, 1.0, 1.0, +0.3963377774, -0.1055613458, -0.0894841775, +0.2158037573, -0.0638541728, -1.2914855480) * lab;\n" +
                    "  gl_FragColor = vec4(sqrt(clamp(" +
                    "                 mat3(+4.0767245293, -1.2681437731, -0.0041119885, -3.3072168827, +2.6093323231, -0.7034763098, +0.2307590544, -0.3411344290, +1.7068625689) *\n" +
                    "                 (lab * lab * lab)," +
                    "                 0.0, 1.0)), v_color.a * color.a);\n" +
                    "}\n";
    public static final String fragment2 =
            "#ifdef GL_ES\n" +
                    "#define LOWP lowp\n" +
                    "precision highp float;\n" +
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
                    "  vec4 index = vec4(color.rgb, v_color.r);\n" +
                    "  index.rgb *= (254.0 / 255.5);\n" +
                    "  vec3 tgt = texture2D(u_texPalette, index.xw).rgb;\n" +
                    "  vec3 lab = mat3(+0.2104542553, +1.9779984951, +0.0259040371, +0.7936177850, -2.4285922050, +0.7827717662, -0.0040720468, +0.4505937099, -0.8086757660) *" +
                    "             pow(mat3(0.4121656120, 0.2118591070, 0.0883097947, 0.5362752080, 0.6807189584, 0.2818474174, 0.0514575653, 0.1074065790, 0.6302613616) \n" +
                    "             * (tgt.rgb * tgt.rgb), forward);\n" +

                    "  lab.x = smoothstep(0.0, 1.0, (lab.x + index.y + v_color.g - 1.25) * 0.8 + 0.5);\n" +
                    "  lab.y = clamp(lab.y * (3.8 * color.b) * (v_color.b), -1.0, 1.0);\n" +
                    "  lab.z = clamp(lab.z * (3.8 * color.b) * (v_color.b) + (sqrt(lab.x) - 0.8) * 0.25, -1.0, 1.0);\n" +
                    "  lab = mat3(1.0, 1.0, 1.0, +0.3963377774, -0.1055613458, -0.0894841775, +0.2158037573, -0.0638541728, -1.2914855480) * lab;\n" +
                    "  gl_FragColor = vec4(sqrt(clamp(" +
                    "                 mat3(+4.0767245293, -1.2681437731, -0.0041119885, -3.3072168827, +2.6093323231, -0.7034763098, +0.2307590544, -0.3411344290, +1.7068625689) *\n" +
                    "                 (lab * lab * lab)," +
                    "                 0.0, 1.0)), v_color.a * color.a);\n" +
                    "}\n";

    public ShaderProgram indexShader;
    public ShaderProgram indexShader2;
    public ShaderProgram regularShader;

    public Texture palettes;
    public Texture previewTexture;
    public Texture[] images;
    public Pixmap workingPalette;
    public Pixmap preview;
    public float[] workingOklab;

    public int groupIndex = 0;
    public int stuffIndex = 0;

    public BitmapFont font;
    public SpriteBatch batch;

    private long startTime, scrollTime;
    private float[] H = new float[256];
    private float[] S = new float[256];
    private float[] L = new float[256];
    private float allL = 0f;
    private float allH = 0f;
    private float allS = 0f;

    private PixmapIO.PNG png;

    private static final Stuff[] STUFFS = Stuff.STUFFS_C;

    private final ObjectObjectOrderedMap<String, int[]> groups = new ObjectObjectOrderedMap<>(256);
    {
        int[] all = new int[215];

        for (int i = 3, idx = 0; i < 220; i++) {
            // we're removing "constant color" materials here.
            if(i == 9 || i == 15) continue;
            all[idx++] = i;
        }

        groups.put("All", all);
        groups.put("Skin", new int[]{123, 121, 122, 124, 125, 126, 114});
        groups.put("Fluff", new int[]{83, 82, 84, 77, 76, 78});
        groups.put("Eye", new int[]{4, 25, 11, 113});
        groups.put("Body", new int[]{90, 112, 89});
        groups.put("Scales", new int[]{164, 163, 165, 179, 178, 180});
        groups.put("Carapace", new int[]{146, 145, 147, 176, 175, 177});
        groups.put("Mollusk", new int[]{104, 103, 105, 86, 85, 87});
        groups.put("Wood", new int[]{21, 22, 23, 24});
        groups.put("Leaves", new int[]{96, 95, 94});
        groups.put("Fruit", new int[]{158, 157, 159});
        groups.put("Flower", new int[]{209, 208, 210});
        groups.put("Food", new int[]{118, 119});
        groups.put("Drink", new int[]{139, 140, 217});
        groups.put("Water", new int[]{51, 52, 53, 56, 57, 58, 59});
        groups.put("Cold", new int[]{60, 55, 174});
        groups.put("Earth", new int[]{18, 17, 20, 88});
        groups.put("City", new int[]{65, 63, 64, 19, 3, 5, 61, 30});
        groups.put("Decay", new int[]{149, 148, 150, 152, 151, 106, 110, 109, 111});
        groups.put("Cardboard", new int[]{129, 127, 128});
        groups.put("Fabric", new int[]{134, 133, 135, 28, 26, 29, 120, 16, 182, 181, 183, 188, 187, 189});
        groups.put("Camouflage", new int[]{92, 91, 93});
        groups.put("Marks", new int[]{80, 79, 81, 191, 190, 192});
        groups.put("Metal", new int[]{7, 10, 13, 14, 27, 70, 142, 143, 170, 194, 193, 195, 98, 97, 99, 101, 100, 102});
        groups.put("Stone", new int[]{32, 33, 34, 35, 62, 73, });
        groups.put("Plastic", new int[]{185, 184, 186, 137, 136, 138, 201});
        groups.put("Glass", new int[]{171, 169, 54, 6, 12});
        groups.put("Crystal", new int[]{172, 74, 75});
        groups.put("Eerie", new int[]{8, 72, 71, 199, 215, 214, 216});
        groups.put("Fire", new int[]{132, 141, 131, 144, 31});
        groups.put("Magic", new int[]{211, 212, 213, 219, 167, 166, 168});
        groups.put("Science", new int[]{108, 202, 203, 204, });
        groups.put("Light", new int[]{153, 162, 160, 161, 206, 205, 207});
    }
    @Override
    public void create() {
        font = new BitmapFont(Gdx.files.internal("font.fnt"));
        workingPalette = new Pixmap(Gdx.files.internal("palettes/c/yam4.png"));
        workingOklab = new float[256];
        palettes = new Texture(workingPalette);
        preview = new Pixmap(16, 16, Pixmap.Format.RGBA8888);
        preview.setColor(workingPalette.getPixel(stuffIndex & 255, 0));
        preview.fill();
        previewTexture = new Texture(preview);
        png = new PixmapIO.PNG(1024);
        String name = "Eye_Tyrant";
//        String name = "Lomuk";
//        String name = "Damned";
//        String name = "Figure";

        images = new Texture[32];
        for (int a = 0, i = 0; a < 8; a++) {
            for (int f = 0; f < 4; f++) {
                images[i++] = new Texture(Gdx.files.local(outDir+name+"/"+name+"_angle"+a+"_"+f+".png"));
            }
        }
        for (int i = 1; i < 256; i++) {
            float oklab = ColorTools.fromRGBA8888(workingPalette.getPixel(i - 1 & 255, 0));
            workingOklab[i] = oklab;
            L[i] = ColorTools.channelL(oklab);
            H[i] = ColorTools.oklabHue(oklab);
            S[i] = ColorTools.oklabSaturation(oklab);
        }

        batch = new SpriteBatch();
        indexShader = new ShaderProgram(vertex, fragment);
        if (!indexShader.isCompiled()) throw new GdxRuntimeException("Error compiling shader: " + indexShader.getLog());
        indexShader2 = new ShaderProgram(vertex, fragment2);
        if (!indexShader2.isCompiled()) throw new GdxRuntimeException("Error compiling shader: " + indexShader2.getLog());
        regularShader = SpriteBatch.createDefaultShader();
        startTime = TimeUtils.millis();
        scrollTime = Long.MAX_VALUE >>> 4;
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
        boolean changed = false, regroup = false, switched = false;
        int currentPreview;
        if(Gdx.input.isKeyJustPressed(Input.Keys.SLASH)){
            System.out.printf("rgba=%08X H=%1.4f S=%1.4f L=%1.4f\n",
                    ColorTools.toRGBA8888(ColorTools.oklabByHSL(H[stuffIndex], S[stuffIndex], L[stuffIndex], 1f)),
                    H[stuffIndex], S[stuffIndex], L[stuffIndex]);
        }
        if(Gdx.input.isKeyJustPressed(Input.Keys.ENTER)){
            try {
                png.write(Gdx.files.local("tempPalette.png"), workingPalette);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if(Gdx.input.isKeyJustPressed(Input.Keys.DOWN)) {
            groupIndex--;
            scrollTime = TimeUtils.millis();
            regroup = true;
        }
        else if(Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
            groupIndex++;
            scrollTime = TimeUtils.millis();
            regroup = true;
        }
        else if(Gdx.input.isKeyJustPressed(Input.Keys.LEFT)) {
            stuffIndex--;
            scrollTime = TimeUtils.millis();
            switched = true;
        }
        else if(Gdx.input.isKeyJustPressed(Input.Keys.RIGHT)) {
            stuffIndex++;
            scrollTime = TimeUtils.millis();
            switched = true;
        }
        else if(Gdx.input.isKeyPressed(Input.Keys.DOWN)){
            if(TimeUtils.timeSinceMillis(scrollTime) >= 500){
                groupIndex--;
                scrollTime += 500L;
                regroup = true;
            }
        }
        else if(Gdx.input.isKeyPressed(Input.Keys.UP)){
            if(TimeUtils.timeSinceMillis(scrollTime) >= 500){
                groupIndex++;
                scrollTime += 500L;
                regroup = true;
            }
        }
        else if(Gdx.input.isKeyPressed(Input.Keys.LEFT)){
            if(TimeUtils.timeSinceMillis(scrollTime) >= 250){
                stuffIndex--;
                scrollTime += 250L;
                switched = true;
            }
        }
        else if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)){
            if(TimeUtils.timeSinceMillis(scrollTime) >= 250){
                stuffIndex++;
                scrollTime += 250L;
                switched = true;
            }
        }
        if(regroup){
            stuffIndex = 0;
            groupIndex = (groupIndex + groups.size()) % groups.size();
        }
        int[] group = groups.getAt(groupIndex);
        if(regroup || switched){
            stuffIndex = (stuffIndex + group.length) % group.length;
        }
        float step = Math.min(Gdx.graphics.getDeltaTime(), (1f/15f)) * (UIUtils.shift() ? 0.3f : -0.3f);;
//        float step = Math.min(Gdx.graphics.getDeltaTime(), 0.3f) * (UIUtils.shift() ? 0.0625f : -0.0625f);
        if(UIUtils.ctrl()) {
            // dark to light
            if (Gdx.input.isKeyPressed(Input.Keys.L)) {
                allL = step;
                changed = true;
            }
            //hue
            else if (Gdx.input.isKeyPressed(Input.Keys.H)) {
                allH = step;
                changed = true;
            }
            //saturation
            else if (Gdx.input.isKeyPressed(Input.Keys.S)) {
                allS = step;
                changed = true;
            }
            if (changed) {
                for (int i = 0; i < group.length; i++) {
                    final int idx = group[i];
                    L[idx] = Math.min(Math.max(L[idx] + allL,  0f),  1f);
                    H[idx] = MathTools.fract(H[idx] + allH);
                    S[idx] = Math.min(Math.max(S[idx] + allS,  0f),  1f);
                    float edited = ColorTools.oklabByHSL(H[idx], S[idx], L[idx], 1f);
                    workingOklab[idx] = edited;

                    int pre = ColorTools.toRGBA8888(edited);
                    workingPalette.drawPixel(idx - 1 & 255, 0, pre);
                }
                allL = allH = allS = 0f;
                palettes.draw(workingPalette, 0, 0);
            }
            int gsi = group[stuffIndex];
            currentPreview = ColorTools.toRGBA8888(ColorTools.oklabByHSL(H[gsi], S[gsi], L[gsi], 1f));
        }
        else {
            int gsi = group[stuffIndex];
            // dark to light
            if (Gdx.input.isKeyPressed(Input.Keys.L)) {
                L[gsi] = Math.min(Math.max(L[gsi] + step, 0f), 1f);
                changed = true;
            }
            //hue
            else if (Gdx.input.isKeyPressed(Input.Keys.H)) {
                H[gsi] = MathTools.fract(H[gsi] + step);
                changed = true;
            }
            //saturation
            else if (Gdx.input.isKeyPressed(Input.Keys.S)) {
                S[gsi] = Math.min(Math.max(S[gsi] + step, 0f), 1f);
                changed = true;
            }
            float edited = ColorTools.oklabByHSL(H[gsi], S[gsi], L[gsi], 1f);
            currentPreview = ColorTools.toRGBA8888(workingOklab[gsi] = edited);
            if (changed) {
                workingPalette.drawPixel(gsi - 1 & 255, 0, currentPreview);
                palettes.draw(workingPalette, 0, 0);
            }
        }
        ScreenUtils.clear(0.5f, 0.5f, 0.5f, 1f);
        batch.setShader(Gdx.input.isKeyPressed(Input.Keys.SPACE) ? indexShader2 : indexShader);
        Gdx.gl.glActiveTexture(GL20.GL_TEXTURE1);
        palettes.bind();
        batch.begin();

        batch.getShader().setUniformi("u_texPalette", 1);
        Gdx.gl.glActiveTexture(GL20.GL_TEXTURE0);
        batch.setColor(0f, 0.5f, 0.5f, 1f);
        Texture img = images[(int) (TimeUtils.timeSinceMillis(startTime) >>> 8) & 31];
        batch.draw(img, 0, img.getHeight(), img.getWidth(), -img.getHeight());
        batch.end();

        batch.setShader(regularShader);

        preview.setColor(currentPreview);
        preview.fill();
        previewTexture.draw(preview, 0, 0);

        batch.begin();
        batch.setPackedColor(Color.WHITE_FLOAT_BITS);
        font.draw(batch, groups.keyAt(groupIndex), Gdx.graphics.getWidth() * 3 / 8, Gdx.graphics.getHeight() * 15 / 16, Gdx.graphics.getWidth() / 2, Align.left, false);
        font.draw(batch, STUFFS[group[stuffIndex]].name, 0, Gdx.graphics.getHeight() * 14 / 16, Gdx.graphics.getWidth(), Align.center, false);
        batch.draw(previewTexture, Gdx.graphics.getWidth() / 2 - 8, Gdx.graphics.getHeight() / 2 + 16);
        batch.end();
    }


    public static void main(String[] arg) {
        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
        config.setTitle("Isonomicon C: Special Viewer");
        config.setWindowedMode(400, 400);
        config.setIdleFPS(10);
        config.setForegroundFPS(15);
        config.useVsync(true);
        config.setResizable(false);
        config.disableAudio(true);
        final PaletteDrafter app = new PaletteDrafter();
        new Lwjgl3Application(app, config);
    }

}

