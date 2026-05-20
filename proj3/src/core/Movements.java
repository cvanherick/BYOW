package core;

import tileengine.Tileset;

import java.util.List;

public class Movements {
    World world;
    private static boolean enemyMove;
    public Movements(World world) {
        this.world = world;
    }

    private boolean canBeMoved(int x , int y) {
        return this.world.gameState[x][y] == World.floor || this.world.gameState[x][y] == World.nothing;
    }

    public void move(int prevX, int prevY, int x, int y) {
        if (canBeMoved(x, y)) {
            this.world.gameState[x][y] = Tileset.AVATAR;
            if (Menu.pacMan) {
                this.world.gameState[prevX][prevY] = World.nothing;
            } else {
                this.world.gameState[prevX][prevY] = World.floor;
            }
            World.avatarX = x;
            World.avatarY = y;
            if(enemyMove) {
                enemyMove = false;
                for (Enemy enemy: world.enemies) {
                    List<Integer> nextMove = enemy.nextMove();
                    if (nextMove != null) {
                        world.gameState[enemy.currX][enemy.currY] = enemy.prevTile;
                        enemy.prevTile = world.gameState[nextMove.get(0)][nextMove.get(1)];
                        world.gameState[nextMove.get(0)][nextMove.get(1)] = Tileset.ENEMY;
                        enemy.currX = nextMove.get(0);
                        enemy.currY = nextMove.get(1);
                        if (enemy.currX == World.avatarX && enemy.currY == World.avatarY) {
                            Menu.newGame = true;
                        }
                    }
                }
            } else {
                enemyMove = true;
            }
        }
    }
}
