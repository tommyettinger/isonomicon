package isonomicon.b;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.PixmapIO;
import isonomicon.physical.Stuff;

import java.io.IOException;

public class PaletteFusion extends ApplicationAdapter {
    public Pixmap[] palettes;
    public Pixmap workingPalette;
    public Pixmap oldPalette;

    private PixmapIO.PNG png;

    @Override
    public void create() {
        oldPalette = new Pixmap(Gdx.files.internal("palettes/b/ColorGuardMasterPalette.png"));
        palettes = new Pixmap[]{
                new Pixmap(Gdx.files.internal("palettes/b/ColorGuardTerrainCoast.png")),
                new Pixmap(Gdx.files.internal("palettes/b/ColorGuardTerrainDesert.png")),
                new Pixmap(Gdx.files.internal("palettes/b/ColorGuardTerrainForest.png")),
                new Pixmap(Gdx.files.internal("palettes/b/ColorGuardTerrainIce.png")),
                new Pixmap(Gdx.files.internal("palettes/b/ColorGuardTerrainJungle.png")),
                new Pixmap(Gdx.files.internal("palettes/b/ColorGuardTerrainMountains.png")),
                new Pixmap(Gdx.files.internal("palettes/b/ColorGuardTerrainOcean.png")),
                new Pixmap(Gdx.files.internal("palettes/b/ColorGuardTerrainPlains.png")),
                new Pixmap(Gdx.files.internal("palettes/b/ColorGuardTerrainRiver.png")),
                new Pixmap(Gdx.files.internal("palettes/b/ColorGuardTerrainRocky.png")),
                new Pixmap(Gdx.files.internal("palettes/b/ColorGuardTerrainRuins.png")),
                new Pixmap(Gdx.files.internal("palettes/b/ColorGuardTerrainVolcano.png")),
        };
        workingPalette = new Pixmap(256, 256, Pixmap.Format.RGBA8888);
        workingPalette.drawPixmap(oldPalette, 0, 0);
        for (int i = 0; i < palettes.length; i++) {
            Pixmap pal = palettes[i];
            workingPalette.drawPixmap(pal, 0, 208 + i);
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
        config.setTitle("Isonomicon Palette Fusion");
        config.setWindowedMode(400, 400);
        config.setIdleFPS(10);
        config.setForegroundFPS(10);
        config.useVsync(true);
        config.setResizable(false);
        config.disableAudio(true);
        final PaletteFusion app = new PaletteFusion();
        new Lwjgl3Application(app, config);
    }

}

