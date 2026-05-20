package core;

import tileengine.TETile;
import tileengine.Tileset;

import java.util.*;

public class World {
    public static TETile wall = Tileset.WALL;
    public static TETile floor = Tileset.FLOOR;
    public static TETile nothing = Tileset.NOTHING;
    public static final Deque<TETile[]> themes = new ArrayDeque<TETile[]>();
    public final int WIDTH = 90;
    public final int HEIGHT = 50;
    TETile[][] gameState;
    Random generator;
    long seed;
    public static int avatarX;
    public static int avatarY;
    private final List<int[]> roomCenters = new ArrayList<>();
    private final Map<Integer, List<Integer>> floors = new HashMap<>();
    public HashSet<Enemy> enemies = new HashSet<>();
    static {
        themes.addFirst(new TETile[]{Tileset.NOTHING, Tileset.FLOOR, Tileset.WALL});
        themes.addFirst(new TETile[]{Tileset.NOTHING, Tileset.SAND, Tileset.TREE});
        themes.addFirst(new TETile[]{Tileset.NOTHING, Tileset.GRASS, Tileset.MOUNTAIN});
    }
    public World() {
        gameState = new TETile[WIDTH][HEIGHT];
        for (int x = 0; x < WIDTH; x++) {
            for (int y = 0; y < HEIGHT; y++) {
                gameState[x][y] = World.nothing;
            }
        }
    }
    public World(long seed) {
        //creates new random number generator
        generator = new Random(seed);
        this.seed = seed;
        //creates new board grid
        gameState = new TETile[WIDTH][HEIGHT];
        // Fill grid with NOTHING tiles.
        for (int x = 0; x < WIDTH; x++) {
            for (int y = 0; y < HEIGHT; y++) {
                gameState[x][y] = World.nothing;
            }
        }
        createWorld();
        addEnemies();

    }

    private void addEnemies() {
        for (int i = 0; i < 5; i++) {
            enemies.add(new Enemy(this));
        }
    }

    private void createWorld() {
        //creates rooms + hallways
        createRooms();
        //puts hashtags in for walls
        addWalls();
        checkEdges();
        addAvatar();
    }

    private void checkEdges() {
        for (int x : new int[]{0, WIDTH -1}) {
            for (int y = 0; y < HEIGHT - 1; y++) {
                if (gameState[x][y] == World.floor) {
                    gameState[x][y] = World.wall;
                }
            }
        }

        for (int y : new int[]{0, HEIGHT - 1}) {
            for (int x = 0; x < WIDTH - 1; x++) {
                if (gameState[x][y] == World.floor) {
                    gameState[x][y] = World.wall;
                }
            }
        }
    }

    private void placeRoom(int x, int y, int width, int height) {
        for (int w = 0; w < width; w++) {
            for (int h = 0; h < height; h++) {
                gameState[x + w][y + h] = World.floor;
                addToFloors(x + w, y + h);
            }
        }
        int centerX = x + width / 2;
        int centerY = y + height / 2;
        roomCenters.add(new int[]{centerX, centerY});
    }

    private boolean canPlaceRoom(int x, int y, int width, int height) {
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                if (x + i >= WIDTH - 1 || y + j >= HEIGHT - 1 || x + i <= 0 || y + j <= 0 ||
                        gameState[x + i][y + j] != World.nothing) {
                    return false;
                }
            }
        }
        return true;
    }

    private void connectRooms(int[] center1, int[] center2) {
        int x1 = center1[0];
        int y1 = center1[1];
        int x2 = center2[0];
        int y2 = center2[1];

        for (int x = Math.min(x1, x2); x <= Math.max(x1, x2); x++) {
            gameState[x][y1] = World.floor;
            addToFloors(x , y1);
        }

        for (int y = Math.min(y1, y2); y <= Math.max(y1, y2); y++) {
            gameState[x2][y] = World.floor;
            addToFloors(x2 , y);
        }
    }

    private void createRooms() {
        int numRooms = generateNumber(15, 20);
        for (int i = 0; i < numRooms; i++) {
            int xPosition = generateNumber(1, WIDTH - 5);
            int yPosition = generateNumber(1, HEIGHT - 5);
            int roomWidth = generateNumber(5, 15);
            int roomHeight = generateNumber(5, 15);
            if (canPlaceRoom(xPosition, yPosition, roomWidth, roomHeight)) {
                placeRoom(xPosition, yPosition, roomWidth, roomHeight);
                //int[] result = buildHallWay(xPosition, yPosition, roomWidth, roomHeight);
                //xPosition = result[0];
                //yPosition = result[1];
                if (roomCenters.size() > 1) {
                    int[] newRoomCenter = roomCenters.get(roomCenters.size() - 1);
                    int[] prevRoomCenter = roomCenters.get(roomCenters.size() - 2);
                    connectRooms(prevRoomCenter, newRoomCenter);
                }
            } else {
                i--;
            }
        }
    }


    public int generateNumber(int min, int max) {
        return generator.nextInt((max - min) + 1) + min;
    }

    private void addWalls() {
        for (int x : floors.keySet()) {
            for (int y : floors.get(x)) {
                if (x - 1 >= 0 && gameState[x - 1][y] == World.nothing) {
                    gameState[x - 1][y] = World.wall;
                }
                if (x + 1 <= WIDTH - 1 && gameState[x + 1][y] == World.nothing) {
                    gameState[x + 1][y] = World.wall;
                }
                if (y - 1 >= 0 && gameState[x][y - 1] == World.nothing) {
                    gameState[x][y - 1] = World.wall;
                }
                if (y + 1 <= HEIGHT - 1 && gameState[x][y + 1] == World.nothing) {
                    gameState[x][y + 1] = World.wall;
                }
            }
        }
    }

    private void addToFloors(int x, int y) {
        if (floors.containsKey(x)) {
            floors.get(x).add(y);
        } else {
            List<Integer> yFloors = new ArrayList<>();
            yFloors.add(y);
            floors.put(x, yFloors);
        }
    }
    private void addAvatar() {

        boolean foundStart = false;
        while (!foundStart) {
            int testX = generateNumber(1, WIDTH-1);
            int testY = generateNumber(1, HEIGHT-1);
            if (gameState[testX][testY].equals(World.floor)) {
                avatarX = testX;
                avatarY = testY;
                gameState[avatarX][avatarY] = Tileset.AVATAR;
                foundStart = true;
            }
        }

    }
}
