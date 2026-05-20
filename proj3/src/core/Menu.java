package core;

import edu.princeton.cs.algs4.StdDraw;
import tileengine.TERenderer;
import tileengine.TETile;
import tileengine.Tileset;

import static java.awt.image.ImageObserver.HEIGHT;
import static java.awt.image.ImageObserver.WIDTH;
import static org.eclipse.jetty.webapp.MetaDataComplete.True;

public class Menu {
    private static final int WIDTH = 90;
    private static final int HEIGHT = 50;
    public static boolean newGame = false;
    public static boolean winGame = false;
    public static boolean pacMan = false;
    public static boolean french = false;
    public Menu () {

    }

    public void newMenu() {
        StdDraw.setXscale(0, 1);
        StdDraw.setYscale(0, 1);
        StdDraw.setCanvasSize(800, 400);
        if (!french) {
            displayMenu();
        } else {
            displayFrenchMenu();
        }
        while(true) {
            if (StdDraw.hasNextKeyTyped()) {
                char next = StdDraw.nextKeyTyped();
                if (Character.toLowerCase(next) == 'n') {
                    enterSeed();
                    break;
                } else if (Character.toLowerCase(next) == 'l') {
                    World world = SaveLoadGame.loadGame();
                    if (world != null) {
                        renderWorld(world);
                        break;
                    } else {
                        if (!french) {
                            StdDraw.text(0.5, 0.45, "CANNOT LOAD GAME. NO GAME FILE SAVED.");
                        } else {
                            StdDraw.text(0.5, 0.45, "IMPOSSIBLE DE CHARGER LA PARTIE. " +
                                    "AUCUN FICHIER DE JEU SAUVÉ");
                        }
                    }
                } else if (Character.toLowerCase(next) == 'q') {
                    System.exit(0);
                } else if (Character.toLowerCase(next) == 'p') {
                    pacMan = true;
                    enterSeed();
                    break;
                } else if (Character.toLowerCase(next) == 'f') {
                    displayFrenchMenu();
                    french = true;
                } else if (Character.toLowerCase(next) == 'e') {
                    displayMenu();
                    french = false;
                }
            }
        }
    }

    private void displayMenu() {
        StdDraw.clear();
        StdDraw.text(0.5, 0.9, "CS 61B: BYOW");
        if (newGame) {
            StdDraw.text(0.5, 0.7, "Game Over");
        } else if (winGame) {
            StdDraw.text(0.5, 0.7, "Yay! You won.");
        }
        StdDraw.text(0.5, 0.6, "(N) New Game");
        StdDraw.text(0.5, 0.5, "(L) Load Game");
        StdDraw.text(0.5, 0.4, "(Q) Quit");
        StdDraw.text(0.5, 0.3, "(P) PacMan");
        StdDraw.text(0.5, 0.2, "Controls: N turns light on off and T switches to new theme");
        StdDraw.text(0.5, 0.1, "Languages: E for English and F for French");
        StdDraw.show();
    }

    private void displayFrenchMenu() {
        StdDraw.clear();
        StdDraw.text(0.5, 0.9, "CS 61B: BYOW");
        if (newGame) {
            StdDraw.text(0.5, 0.7, "Fin de partie");
        } else if (winGame) {
            StdDraw.text(0.5, 0.7, "Youpi ! Tu as gagné.");
        }
        StdDraw.text(0.5, 0.6, "(N) Nouvelle Partie");
        StdDraw.text(0.5, 0.5, "(L) Charger une partie");
        StdDraw.text(0.5, 0.4, "(Q) Quitter");
        StdDraw.text(0.5, 0.3, "(P) PacMan");
        StdDraw.text(0.5, 0.2, "Commandes : N allume ou éteint la lumière et T change le thème");
        StdDraw.text(0.5, 0.1, "Langues : E pour Anglais et F pour Français");
        StdDraw.show();
    }

    private void enterSeed() {
        StdDraw.clear();
        StringBuilder seed = new StringBuilder();
        StdDraw.text(0.5, 0.9, "CS 61B: BYOW");
        if (!french) {
            StdDraw.text(0.5, 0.5, "Enter seed followed by S");
        } else {
            StdDraw.text(0.5, 0.5,"Entrez une graine suivie de S");
        }
        StdDraw.show();
        while (true) {
            if (StdDraw.hasNextKeyTyped()) {
                char next = StdDraw.nextKeyTyped();
                if (Character.isDigit(next)) {
                    seed.append(next);
                } else if (Character.toLowerCase(next) == 's') {
                    try {
                        renderWorld(new World(Long.parseLong(seed.toString())));
                        break;
                    }
                    catch (NumberFormatException e) {
                        enterSeed();
                        break;
                    }
                }
                StdDraw.clear();
                StdDraw.text(0.5, 0.4, seed.toString());
                StdDraw.text(0.5, 0.9, "CS 61B: BYOW");
                if (!french) {
                    StdDraw.text(0.5, 0.5, "Enter seed followed by S");
                } else {
                    StdDraw.text(0.5, 0.5,"Entrez une graine suivie de S");
                }
                StdDraw.show();
            }
        }
    }

    private void renderWorld(World world) {
        StdDraw.clear();
        newGame = false;
        winGame = false;
        TERenderer ter = new TERenderer();
        ter.initialize(WIDTH, HEIGHT);
        //long seed = 4906584014834285062L;
        //long seed = 8781147259157824675L;
        //long seed = 4561973943446226946L;
        //long seed = 868551914527569918L;
        //long seed = 191552634518619690L;


        ter.renderFrame(world.gameState);

        boolean colonTyped = false;
        Movements movements = new Movements(world);
        LineOfSight eyesight = new LineOfSight(ter, world);
        while (!newGame && !winGame) {
            if (StdDraw.hasNextKeyTyped()) {
                char next = StdDraw.nextKeyTyped();
                if (colonTyped && Character.toLowerCase(next) == 'q') {
                    SaveLoadGame.saveGame(world);
                    StdDraw.clear();
                    break;
                }
                if (next == ':') {
                    colonTyped = true;
                } else if (Character.toLowerCase(next) == 'w') {
                    movements.move(world.avatarX, world.avatarY, world.avatarX, world.avatarY + 1);
                } else if (Character.toLowerCase(next) == 's') {
                    movements.move(world.avatarX, world.avatarY, world.avatarX, world.avatarY - 1);
                } else if (Character.toLowerCase(next) == 'a') {
                    movements.move(World.avatarX, world.avatarY, world.avatarX - 1, world.avatarY);
                } else if (Character.toLowerCase(next) == 'd') {
                    movements.move(world.avatarX, world.avatarY, world.avatarX + 1, world.avatarY);
                } else if (Character.toLowerCase(next) == 'n') {
                    eyesight.toggleLineOfSight();
                } else if (Character.toLowerCase(next) == 't') {
                    TETile[] theme = World.themes.removeFirst();

                    for (int i = 0; i < WIDTH; i++) {
                        for (int j = 0; j < HEIGHT; j++) {
                            if(world.gameState[i][j] == World.nothing) {
                                world.gameState[i][j] = theme[0];
                            } else if (world.gameState[i][j] == World.floor) {
                                world.gameState[i][j] = theme[1];
                            } else if (world.gameState[i][j] == World.wall) {
                                world.gameState[i][j] = theme[2];
                            }
                        }
                    }
                    for (Enemy enemy: world.enemies) {
                        if(enemy.prevTile == World.nothing) {
                            enemy.prevTile = theme[0];
                        } else if (enemy.prevTile == World.floor) {
                            enemy.prevTile = theme[1];
                        }
                    }
                    World.nothing = theme[0];
                    World.floor = theme[1];
                    World.wall = theme[2];
                    World.themes.addLast(theme);
                }
            }
            eyesight.renderWorld();
            winGame = winChecker(world);
        }

        StdDraw.clear();
        pacMan = false;
        newMenu();
    }
    private boolean winChecker(World world) {
        for (int i = 0; i < WIDTH; i++) {
            for (int j = 0; j < HEIGHT; j++) {
                if (world.gameState[i][j] == World.floor) {
                    return false;
                }
            }
        }
        return true;
    }
}
