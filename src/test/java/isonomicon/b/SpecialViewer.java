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
import isonomicon.app.ColorGuardData;
import isonomicon.visual.ShaderUtils;

public class SpecialViewer extends ApplicationAdapter {
    public ShaderProgram indexShader;

    public Texture palettes;
    public Texture[] images, targetImages, receiveImages;

    public SpriteBatch batch;

    private long startTime;

    @Override
    public void create() {
//        palettes = new Texture("palettes/palettes-b.png");
//        palettes = new Texture("palettes/b/TanClothDarkSkin.png");
//        palettes = new Texture("palettes/b/CherrySkinDarkCloth.png");
//        palettes = new Texture("palettes/b/BlueFurCyanCrystal.png");

        palettes = new Texture("palettes/b/ColorGuardMasterPalette.png");

//        palettes = new Texture("palettes/edited/NaturalWoodAndLeaves.png");
//        String name = "Eye_Tyrant";
//        String name = "Lomuk";
//        String name = "Damned";
//        String name = "Tree";
//        String name = "Phantom_Wand";
//        String name = "Figure";

        String name = "Rifle_Sniper";
        ColorGuardData.Unit unit = ColorGuardData.byName.get(name);
        String target = "Infantry";

        images = new Texture[64];
        receiveImages = new Texture[64];
        for (int a = 0, i = 0; a < 4; a++) {
            for (int f = 0; f < 8; f++) {
                images[i] = new Texture(Gdx.files.local("out/color_guard/lab/"+name+"/"+name+"_Primary_angle"+a+"_"+f+".png"));
                receiveImages[i++] = new Texture(Gdx.files.local("out/color_guard/lab/"+unit.primary+"_Receive"+"/"+unit.primary+"_Receive_"+unit.primaryStrength+"_angle"+a+"_"+f+".png"));
            }
        }
        if(unit.secondaryStrength == 0){
            for (int a = 0, i = 32; a < 4; a++) {
                for (int f = 0; f < 8; f++) {
                    images[i] = new Texture(Gdx.files.local("out/color_guard/lab/"+name+"/"+name+"_Primary_angle"+a+"_"+f+".png"));
                    receiveImages[i++] = new Texture(Gdx.files.local("out/color_guard/lab/"+unit.primary+"_Receive"+"/"+unit.primary+"_Receive_"+unit.primaryStrength+"_angle"+a+"_"+f+".png"));
                }
            }
        }
        else {
            for (int a = 0, i = 32; a < 4; a++) {
                for (int f = 0; f < 8; f++) {
                    images[i] = new Texture(Gdx.files.local("out/color_guard/lab/"+name+"/"+name+"_Secondary_angle"+a+"_"+f+".png"));
                    receiveImages[i++] = new Texture(Gdx.files.local("out/color_guard/lab/"+unit.secondary+"_Receive"+"/"+unit.secondary+"_Receive_"+unit.secondaryStrength+"_angle"+a+"_"+f+".png"));
                }
            }
        }
        targetImages = new Texture[16];
        for (int a = 0, i = 0; a < 4; a++) {
            for (int f = 0; f < 4; f++) {
                targetImages[i++] = new Texture(Gdx.files.local("out/color_guard/lab/"+target+"/"+target+"_angle"+a+"_"+f+".png"));
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
        int time = (int) (TimeUtils.timeSinceMillis(startTime) >>> 7);
        batch.draw(images[(time & 7) | (time & 56)], 64.0f, 64.0f);
        time ^= 16;
        batch.setColor(1f/256f, 0.5f, 0.5f, 1f);
        batch.draw(targetImages[(time & 3) | (time >>> 1 & 12)], 60 + 64.0f, 30 + 64.0f);
        batch.setColor(2f/256f, 0.5f, 0.5f, 1f);
        batch.draw(targetImages[(time & 3) | (time >>> 1 & 12)], -60 + 64.0f, 30 + 64.0f);
        batch.setColor(3f/256f, 0.5f, 0.5f, 1f);
        batch.draw(targetImages[(time & 3) | (time >>> 1 & 12)], -60 + 64.0f, -30 + 64.0f);
        batch.setColor(4f/256f, 0.5f, 0.5f, 1f);
        batch.draw(targetImages[(time & 3) | (time >>> 1 & 12)], 60 + 64.0f, -30 + 64.0f);

        int angle = time >>> 3 & 3;
        batch.setColor(0f, 0.5f, 0.5f, 1f);
        batch.draw(receiveImages[(time & 7) | (time & 56)], 60 * (1|-((angle ^ angle >>> 1) & 1)) + 64.0f, 30 * (1 - (angle & 2)) + 64.0f);

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
