package core;

import edu.princeton.cs.algs4.In;
import tileengine.Tileset;

import static utils.FileUtils.fileExists;
import static utils.FileUtils.writeFile;

public class SaveLoadGame {





    public static void saveGame(World world) {
        writeSaveFile(world);
        System.exit(0);
    }

    static void writeSaveFile(World world) {
        StringBuilder returnContent = new StringBuilder();
        returnContent.append(world.seed).append("\n");
        returnContent.append(World.avatarX).append(" ").append(World.avatarY);
        for (Enemy enemy: world.enemies) {
            returnContent.append("\n").append(enemy.currX).append(" ").append(enemy.currY);
        }
        writeFile("savedGamed.txt", returnContent.toString());
    }

    public static World loadGame() {
        if (!fileExists("savedGamed.txt")) {
            return null;
        }
        In file = new In("savedGamed.txt");
        if (file.isEmpty()) {
            return null;
        }
        long seed = Long.parseLong(file.readLine());
        String[] avatarCoords = file.readLine().split(" ");
        World world = new World(seed);
        world.seed = seed;
        world.gameState[World.avatarX][World.avatarY] = World.floor;
        World.avatarX = Integer.parseInt(avatarCoords[0]);
        World.avatarY = Integer.parseInt(avatarCoords[1]);
        world.gameState[World.avatarX][World.avatarY] = Tileset.AVATAR;
        for (Enemy enemy: world.enemies) {
            String[] enemyCoords = file.readLine().split(" ");
            world.gameState[enemy.currX][enemy.currY] = World.floor;
            enemy.currX = Integer.parseInt(enemyCoords[0]);
            enemy.currY = Integer.parseInt(enemyCoords[1]);
            world.gameState[enemy.currX][enemy.currY] = Tileset.ENEMY;
        }
        return world;
    }

}
