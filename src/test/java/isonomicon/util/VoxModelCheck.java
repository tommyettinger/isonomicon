package isonomicon.util;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.badlogic.gdx.utils.TimeUtils;
import com.github.tommyettinger.anim8.PaletteReducer;
import isonomicon.io.LittleEndianDataInputStream;
import isonomicon.io.extended.*;
import isonomicon.physical.Tools3D;
import isonomicon.visual.Coloring;
import squidpony.StringKit;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class VoxModelCheck extends ApplicationAdapter {

    public static void main(String[] arg) {
        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
        config.setTitle("VoxModel Check");
        config.setWindowedMode(320, 320);
        config.setIdleFPS(1);
        config.setResizable(false);
        new Lwjgl3Application(new VoxModelCheck(), config);
    }

    public void create() {
        String name = "specialized/vox/FigureSplit.vox";
        try {
            VoxModel model = VoxIOExtended.readVox(new LittleEndianDataInputStream(new FileInputStream(name)));
            System.out.println("TransformChunks");
            for(TransformChunk tc : model.transformChunks.values())
                System.out.println(tc.id + " with child " + tc.childId);
            System.out.println("GroupChunks");
            for(GroupChunk gc : model.groupChunks.values())
                System.out.println(gc.id + " with children " + StringKit.join(",", gc.childIds));
            System.out.println("ShapeChunks");
            for(ShapeChunk sc : model.shapeChunks.values()) {
                System.out.println(sc.id + " with children");
                for(ShapeModel sm : sc.models)
                    System.out.println(sm.id + " hash: " + Tools3D.hash64(model.grids.get(sm.id)));
            }
            System.out.println();
            for(int ch : model.groupChunks.values().iterator().next().childIds) {
                for (ShapeModel sm : model.shapeChunks.get(model.transformChunks.get(ch).childId).models) {
                    byte[][][] g = model.grids.get(sm.id);
                    System.out.println(sm.id + ": " + Tools3D.countNot(g, 0));
                    System.out.println(sm.id + " hash: " + Tools3D.hash64(g));
                }
            }
            System.out.println();
            for (int i = 0; i < model.grids.size(); i++) {
                byte[][][] grid = model.grids.get(i);
                System.out.println(i + " hash: " + Tools3D.hash64(grid));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Gdx.app.exit();
    }
}
