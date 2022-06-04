package isonomicon.b;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.PixmapIO;
import isonomicon.physical.Stuff;
import isonomicon.visual.Coloring;

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
        int hairA, hairB, scar, skin, nose, freckle, ears;
        for (int appearance = 0; appearance < 201; appearance+=8) {
            if(appearance == 24 || appearance == 64 || appearance == 88 || appearance == 144 || appearance == 168 || appearance == 200){
                hairB = hairA = oldPalette.getPixel(216, appearance);
            }
            else {
                hairA = oldPalette.getPixel(225, appearance); // 49
                hairB = oldPalette.getPixel(223, appearance); // 50
            }
            scar = oldPalette.getPixel(217, appearance); // 51
            skin = oldPalette.getPixel(216, appearance); // 52
            nose = Coloring.lerp(oldPalette.getPixel(221, appearance), skin, 0.6f); // 53
            freckle = oldPalette.getPixel(218, appearance); // 54
            ears = oldPalette.getPixel(220, appearance); // 55
            for (int faction = 0; faction < 8; faction++) {
                workingPalette.drawPixmap(palettes[faction], 0, appearance + faction);
                workingPalette.drawPixel(49-1, appearance + faction, hairA);
                workingPalette.drawPixel(50-1, appearance + faction, hairB);
                workingPalette.drawPixel(51-1, appearance + faction, scar);
                workingPalette.drawPixel(52-1, appearance + faction, skin);
                workingPalette.drawPixel(53-1, appearance + faction, nose);
                workingPalette.drawPixel(54-1, appearance + faction, freckle);
                workingPalette.drawPixel(55-1, appearance + faction, ears);
            }
        }
        png = new PixmapIO.PNG(1024);
        png.setFlipY(false);
        try {
            png.write(Gdx.files.local("ColorGuardMasterPalette.png"), workingPalette);
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
        config.disableAudio(true);
        final PaletteScavenger app = new PaletteScavenger();
        new Lwjgl3Application(app, config);
    }

}

