package core;

import tileengine.TETile;
import tileengine.Tileset;

public class AutograderBuddy {

    /**
     * Simulates a game, but doesn't render anything or call any StdDraw
     * methods. Instead, returns the world that would result if the input string
     * had been typed on the keyboard.
     *
     * Recall that strings ending in ":q" should cause the game to quit and
     * save. To "quit" in this method, save the game to a file, then just return
     * the TETile[][]. Do not call System.exit(0) in this method.
     *
     * @param input the input string to feed to your program
     * @return the 2D TETile[][] representing the state of the world
     */
    public static TETile[][] getWorldFromInput(String input) {
        if (input == null || input.isEmpty()) {
            return new World().gameState;
        }

        String normalized = input.toLowerCase();
        World world;
        int index;

        if (normalized.charAt(0) == 'n') {
            int seedEnd = normalized.indexOf('s', 1);
            if (seedEnd < 0) {
                throw new IllegalArgumentException("New game input must include a seed followed by S.");
            }
            long seed = Long.parseLong(normalized.substring(1, seedEnd));
            world = new World(seed);
            index = seedEnd + 1;
        } else if (normalized.charAt(0) == 'l') {
            world = SaveLoadGame.loadGame();
            if (world == null) {
                return new World().gameState;
            }
            index = 1;
        } else {
            throw new IllegalArgumentException("Input must start with N for new game or L for load.");
        }

        Movements movements = new Movements(world);
        boolean colonTyped = false;
        while (index < normalized.length()) {
            char command = normalized.charAt(index);
            if (colonTyped && command == 'q') {
                SaveLoadGame.writeSaveFile(world);
                return world.gameState;
            }

            if (command == ':') {
                colonTyped = true;
            } else {
                colonTyped = false;
                applyMovement(world, movements, command);
            }
            index += 1;
        }

        return world.gameState;
    }

    private static void applyMovement(World world, Movements movements, char command) {
        if (command == 'w') {
            movements.move(World.avatarX, World.avatarY, World.avatarX, World.avatarY + 1);
        } else if (command == 's') {
            movements.move(World.avatarX, World.avatarY, World.avatarX, World.avatarY - 1);
        } else if (command == 'a') {
            movements.move(World.avatarX, World.avatarY, World.avatarX - 1, World.avatarY);
        } else if (command == 'd') {
            movements.move(World.avatarX, World.avatarY, World.avatarX + 1, World.avatarY);
        }
    }


    /**
     * Used to tell the autograder which tiles are the floor/ground (including
     * any lights/items resting on the ground). Change this
     * method if you add additional tiles.
     */
    public static boolean isGroundTile(TETile t) {
        return t.character() == Tileset.FLOOR.character()
                || t.character() == Tileset.AVATAR.character()
                || t.character() == Tileset.FLOWER.character();
    }

    /**
     * Used to tell the autograder while tiles are the walls/boundaries. Change
     * this method if you add additional tiles.
     */
    public static boolean isBoundaryTile(TETile t) {
        return t.character() == Tileset.WALL.character()
                || t.character() == Tileset.LOCKED_DOOR.character()
                || t.character() == Tileset.UNLOCKED_DOOR.character();
    }
}
