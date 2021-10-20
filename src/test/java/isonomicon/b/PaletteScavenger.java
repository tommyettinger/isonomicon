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
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.utils.UIUtils;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.TimeUtils;
import com.github.tommyettinger.colorful.oklab.ColorTools;
import isonomicon.physical.Stuff;
import squidpony.ArrayTools;
import squidpony.squidmath.OrderedMap;

import java.io.IOException;

public class PaletteScavenger extends ApplicationAdapter {
    public Pixmap[] palettes;
    public Pixmap workingPalette;
    public Pixmap oldPalette;

    private PixmapIO.PNG png;

    private static final Stuff[] STUFFS = Stuff.STUFFS_B;

    @Override
    public void create() {
        oldPalette = new Pixmap(Gdx.files.internal("OldColorGuardPalettes.png"));
        palettes = new Pixmap[]{
                new Pixmap(Gdx.files.internal("palettes/b/ColorGuardBaseDark.png")),
                new Pixmap(Gdx.files.internal("palettes/b/ColorGuardBaseWhite.png")),
                new Pixmap(Gdx.files.internal("palettes/b/ColorGuardBaseRed.png")),
                new Pixmap(Gdx.files.internal("palettes/b/ColorGuardBaseOrange.png")),
                new Pixmap(Gdx.files.internal("palettes/b/ColorGuardBaseYellow.png")),
                new Pixmap(Gdx.files.internal("palettes/b/ColorGuardBaseGreen.png")),
                new Pixmap(Gdx.files.internal("palettes/b/ColorGuardBaseBlue.png")),
                new Pixmap(Gdx.files.internal("palettes/b/ColorGuardBasePurple.png")),
        };
        workingPalette = new Pixmap(256, 256, Pixmap.Format.RGBA8888);
        for (int appearance = 0; appearance < 201; appearance+=8) {
            int hairA = oldPalette.getPixel(223, appearance); // 49
            int hairB = oldPalette.getPixel(228, appearance); // 50
            int scar = oldPalette.getPixel(217, appearance); // 51
            int skin = oldPalette.getPixel(216, appearance); // 52
            int nose = oldPalette.getPixel(221, appearance); // 53
            int freckle = oldPalette.getPixel(218, appearance); // 54
            int ears = oldPalette.getPixel(220, appearance); // 55
            for (int faction = 0; faction < 8; faction++) {
                workingPalette.drawPixmap(palettes[faction], 0, appearance + faction);
                workingPalette.drawPixel(49, appearance + faction, hairA);
                workingPalette.drawPixel(50, appearance + faction, hairB);
                workingPalette.drawPixel(51, appearance + faction, scar);
                workingPalette.drawPixel(52, appearance + faction, skin);
                workingPalette.drawPixel(53, appearance + faction, nose);
                workingPalette.drawPixel(54, appearance + faction, freckle);
                workingPalette.drawPixel(55, appearance + faction, ears);
            }
        }
        png = new PixmapIO.PNG(1024);
        try {
            png.write(Gdx.files.local("bigPalette.png"), workingPalette);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Gdx.app.exit();
    }

    @Override
    public void render() {
    }


    public static void main(String[] arg) {
        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
        config.setTitle("Isonomicon Scavenger");
        config.setWindowedMode(400, 400);
        config.setIdleFPS(10);
        config.setForegroundFPS(10);
        config.useVsync(true);
        config.setResizable(false);
        final PaletteScavenger app = new PaletteScavenger();
        new Lwjgl3Application(app, config);
    }

}

