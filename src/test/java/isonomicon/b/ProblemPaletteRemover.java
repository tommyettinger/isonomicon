package isonomicon.b;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.PixmapIO;

import java.io.IOException;

public class ProblemPaletteRemover extends ApplicationAdapter {
    public Pixmap[] palettes;
    public Pixmap workingPalette;
    public Pixmap oldPalette;

    private PixmapIO.PNG png;

    @Override
    public void create() {
        oldPalette = new Pixmap(Gdx.files.internal("palettes/b/ColorGuardMasterPalette.png"));
        workingPalette = new Pixmap(256, 256, Pixmap.Format.RGBA8888);
        for (int i = 0, y = 0; i < 220; i++) {
            int lk = i >> 3;
            if(lk == 3 || lk == 8 || lk == 11 || lk == 18 || lk == 21 || lk == 25)
                continue;
            workingPalette.drawPixmap(oldPalette, 0, y++, 0, i, 256, 1);
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
        final ProblemPaletteRemover app = new ProblemPaletteRemover();
        new Lwjgl3Application(app, config);
    }

}

