package core;

import tileengine.TERenderer;
import tileengine.TETile;

public class LineOfSight {
    TERenderer ter;
    World world;
    boolean isLineOfSightEnabled;
    public LineOfSight(TERenderer ter, World world) {
        this.ter = ter;
        this.world = world;
        this.isLineOfSightEnabled = false;
    }
    public void toggleLineOfSight() {
        isLineOfSightEnabled = !isLineOfSightEnabled;
    }
    public void renderWorld() {
        if (!isLineOfSightEnabled) {
            ter.renderFrame(world.gameState);
            return;
        }
        World currentView = new World();
        for (int i = Math.max(0, World.avatarX - 4); i < 90 && i <= World.avatarX + 4; i++) {
            for (int j = Math.max(0, World.avatarY - 4); j < 50 && j <= World.avatarY + 4; j++) {
                if (distanceCheck(i, j)) {
                    currentView.gameState[i][j] = world.gameState[i][j];
                }
            }
        }
        ter.renderFrame(currentView.gameState);
    }
    private boolean distanceCheck(int i, int j) {
        int dx = World.avatarX - i;
        int dy = World.avatarY - j;
        double distance = Math.sqrt(dx*dx + dy*dy);
        return distance <= 4.0;
    }
}
