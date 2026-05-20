package core;

import tileengine.TETile;
import tileengine.Tileset;

import java.util.*;

public class Enemy {
    public int currX;
    public int currY;
    private final World world;
    TETile prevTile;
    private static final int[][] DIRECTIONS = {{0, 1}, {0, -1}, {-1, 0}, {1, 0}};
    public Enemy(World world) {
        this.world = world;
        this.addEnemy();
        this.world.enemies.add(this);
        this.prevTile = World.floor;
    }

    private void addEnemy() {
        boolean foundStart = false;
        while (!foundStart) {
            int testX = world.generateNumber(1, world.WIDTH - 1);
            int testY = world.generateNumber(1, world.HEIGHT - 1);
            if (world.gameState[testX][testY] == (World.floor)) {
                this.currX = testX;
                this.currY = testY;
                world.gameState[this.currX][this.currY] = Tileset.ENEMY;
                foundStart = true;
            }
        }
    }

    public List<Integer> nextMove() {
        PriorityQueue<Node> queue = new PriorityQueue<>();
        boolean[][] visited = new boolean[world.WIDTH][world.HEIGHT];
        queue.add(new Node(this.currX, this.currY, null));
        visited[this.currX][this.currY] = true;
        while (!queue.isEmpty()) {
            Node currNode = queue.poll();
            if (world.gameState[currNode.x][currNode.y] == Tileset.AVATAR) {
                while (currNode.prev.prev != null) {
                    currNode = currNode.prev;
                }
                List<Integer> returnList = new ArrayList<>();
                returnList.add(currNode.x);
                returnList.add(currNode.y);
                return returnList;
            }
            for (int[] move: DIRECTIONS) {
                int newX = currNode.x + move[0];
                int newY = currNode.y + move[1];
                if (inBounds(newX, newY) && !visited[newX][newY] && world.gameState[newX][newY] != World.wall
                        && world.gameState[newX][newY] != Tileset.ENEMY) {
                    visited[newX][newY] = true;
                    queue.add(new Node(newX, newY, currNode));
                }
            }
        }
        return null;
    }

    private boolean inBounds(int x, int y) {
        return x >= 0 && x < world.WIDTH && y >= 0 && y < world.HEIGHT;
    }
}
