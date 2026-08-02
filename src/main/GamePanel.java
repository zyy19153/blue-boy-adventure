package main;

import java.awt.Dimension;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.swing.JPanel;

import entity.Entity;
import entity.Player;
import tile.TileManager;
import object.SuperObject;
import object.OBJ_Key;

public class GamePanel extends JPanel implements Runnable {

    final int originlTileSize = 16;
    final int scale = 3;

    public final int tileSize = originlTileSize * scale;
    public final int maxScreenCol = 16;
    public final int maxScreenRow = 12;
    public final int screenWidth = tileSize * maxScreenCol;
    public final int screenHeight = tileSize * maxScreenRow;

    // world settings
    public final int maxWorldCol = 50;
    public final int maxWorldRow = 50;
    public final int worldWidth = tileSize * maxWorldCol;
    public final int worldHeight = tileSize * maxWorldRow;

    // FPS
    final int FPS = 60;

    // System
    public TileManager tileM = new TileManager(this);
    KeyHandler keyH = new KeyHandler(this);
    Sound music = new Sound();
    Sound se/*sound effect*/ = new Sound();
    public CollisionChecker cChecker = new CollisionChecker(this);
    public AssetSetter aSetter = new AssetSetter(this);
    public UI ui = new UI(this);
    Thread gameThread;

    // entity and object
    public Player player = new Player(this, keyH);
    public SuperObject[] obj = new SuperObject[10]; // 10 slots: means we can have up to10 objects at the same time; but the categorys of objects are not only 10;
    public Entity npc[] = new Entity[10];

    // game state
    public int gameState;
    public final int playState = 1;
    public final int pauseState = 2;

    public GamePanel () {
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.black);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyH);
        this.setFocusable(true);
    }

    public void setupGame() {
        aSetter.setObject();
        aSetter.setNpc();

        playMusic(0);
        //stopMusic();

        gameState = playState;
    }

    public void startGameThread() {
        gameThread = new Thread(this);
        gameThread.start();
    }

    public void anotherRun() {

        double drawInterval = 1000000000/FPS; // 0.01666 seconds
        double nextDrawTime = System.nanoTime() + drawInterval;

        while(gameThread != null) {
            // System.out.printf("keyH.upPressed= %s\n", keyH.upPressed);

            // long currrentTime = System.nanoTime(); // 1s = 1e9 nanosecond

            // 1. UPDATE: update information in such as character positions
            update();

            // 2. DRAW: draw the screen with the updated information
            repaint();

            try {
                double remainingTime = nextDrawTime - System.nanoTime(); // how much time remaining
                remainingTime = remainingTime / 1000000;

                if (remainingTime < 0) remainingTime = 0;

                Thread.sleep((long) remainingTime);

                nextDrawTime += drawInterval;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void run() {

        double drawInterval = 1000000000/FPS; // 0.01666 seconds
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;
        long timer = 0;
        int drawCount = 0;

        while(gameThread != null) {

            currentTime = System.nanoTime(); // 1s = 1e9 nanosecond

            delta += (currentTime - lastTime) / drawInterval;
            timer += currentTime - lastTime;

            lastTime = currentTime;

            if (delta >= 1) {
                // 1. UPDATE: update information in such as character positions
                update();
                // 2. DRAW: draw the screen with the updated information
                repaint();
                delta--;
                drawCount++;
            }

            // how many frames we draw within one second
            if (timer >= 1000000000) {
                // System.out.printf("FPS: %s\n", drawCount);
                drawCount = 0;
                timer = 0;
            }

        }
    }

    public void update() {
        if (gameState == playState) {
            // PLAYER
            player.update();
            // NPC
            for (int i = 0; i < npc.length; i++) {
                if (npc[i] != null) {
                    npc[i].update();
                }
            }
        }
        if (gameState == pauseState) {
            // nothing
        }
    }

    public void paintComponent(Graphics/*pencil or brushstroke*/ g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;

        // tile
        tileM.draw(g2);

        // Object
        for (int i = 0; i < obj.length; i++) {
            if (obj[i] != null) {
                obj[i].draw(g2, this);
            }
        }

        // NPC
        for (int i = 0; i < npc.length; i++) {
            if (npc[i] != null) {
                npc[i].draw(g2);
            }
        }

        // player
        player.draw(g2);

        // ui
        ui.draw(g2);

        if (keyH.debugOn == true) {
            g2.setColor(Color.white);
            g2.setFont(g2.getFont().deriveFont(30f));
            int debugInfoIndexX = 10;
            AtomicInteger debugInfoIndexY = new AtomicInteger(100);
            int yDelta = g2.getFont().getSize();

            // g2.drawString("Draw Time: " + passed, 10, 400);
            int tileNumX = player.worldX/tileSize;
            int tileNumY = player.worldY/tileSize;
            g2.drawString(String.format("x: %s, y: %s", tileNumX, tileNumY), debugInfoIndexX, debugInfoIndexY.getAndAdd(yDelta));
            g2.drawString(String.format("curTileName: %s", tileM.tile[tileM.mapTileNum[tileNumX][tileNumY]].name), debugInfoIndexX, debugInfoIndexY.getAndAdd(yDelta));
            g2.drawString(String.format("P: l: %s, r %s, u: %s, b: %s",
                                        (player.worldX+player.solidArea.x),
                                        (player.worldX+player.solidArea.x+player.solidArea.width),
                                        (player.worldY+player.solidArea.y),
                                        (player.worldY+player.solidArea.y+player.solidArea.height)
                         ), debugInfoIndexX, debugInfoIndexY.getAndAdd(yDelta));
            g2.drawString(String.format("P: lc: %s, rc %s, ur: %s, br: %s",
                                        (player.worldX+player.solidArea.x)/tileSize,
                                        (player.worldX+player.solidArea.x+player.solidArea.width)/tileSize,
                                        (player.worldY+player.solidArea.y)/tileSize,
                                        (player.worldY+player.solidArea.y+player.solidArea.height)/tileSize
                         ), debugInfoIndexX, debugInfoIndexY.getAndAdd(yDelta));

            Player entity = player;
            int entityLeftWorldX = entity.worldX + entity.solidArea.x;
            int entityRightWorldX = entity.worldX + entity.solidArea.x + entity.solidArea.width;
            int entityTopWorldY = entity.worldY + entity.solidArea.y;
            int entityBottomWorldY = entity.worldY + entity.solidArea.y + entity.solidArea.height;

            int entityLeftCol = entityLeftWorldX / tileSize;
            int entityRightCol = entityRightWorldX / tileSize;
            int entityTopRow = entityTopWorldY / tileSize;
            int entityBottomRow = entityBottomWorldY / tileSize;

            int tileNum1 = 0, tileNum2 = 0;

            switch (entity.direction) {
            case "up":
                entityTopRow = (entityTopWorldY - entity.speed) / tileSize;
                tileNum1 = tileM.mapTileNum[entityLeftCol][entityTopRow];
                tileNum2 = tileM.mapTileNum[entityRightCol][entityTopRow];
                if (tileM.tile[tileNum1].collision || tileM.tile[tileNum2].collision)
                    entity.collisionOn = true;
                break;
            case "down":
                entityBottomRow = (entityBottomWorldY + entity.speed) / tileSize;
                tileNum1 = tileM.mapTileNum[entityLeftCol][entityBottomRow];
                tileNum2 = tileM.mapTileNum[entityRightCol][entityBottomRow];
                if (tileM.tile[tileNum1].collision || tileM.tile[tileNum2].collision)
                    entity.collisionOn = true;
                break;
            case "left":
                entityLeftCol = (entityLeftWorldX - entity.speed) / tileSize;
                tileNum1 = tileM.mapTileNum[entityLeftCol][entityTopRow];
                tileNum2 = tileM.mapTileNum[entityLeftCol][entityBottomRow];
                if (tileM.tile[tileNum1].collision || tileM.tile[tileNum2].collision)
                    entity.collisionOn = true;
                break;
            case "right":
                entityRightCol = (entityRightWorldX + entity.speed) / tileSize;
                tileNum1 = tileM.mapTileNum[entityRightCol][entityTopRow];
                tileNum2 = tileM.mapTileNum[entityRightCol][entityBottomRow];
                if (tileM.tile[tileNum1].collision || tileM.tile[tileNum2].collision)
                    entity.collisionOn = true;
                break;
            }
            g2.drawString(String.format("P: dir: %s, tile[1]: %s, tile[2]: %s",
                                        entity.direction,
                                        tileM.tile[tileNum1].name,
                                        tileM.tile[tileNum2].name
                         ), debugInfoIndexX, debugInfoIndexY.getAndAdd(yDelta));
            g2.drawString(String.format("P: c: %s, tile[1].c: %s, tile[2].c: %s",
                                        entity.collisionOn,
                                        tileM.tile[tileNum1].collision,
                                        tileM.tile[tileNum2].collision
                         ), debugInfoIndexX, debugInfoIndexY.getAndAdd(yDelta));
            Map<String/*objName*/, String/*indexInfo*/> objInfo = Stream.of(obj)
                    .filter(Objects::nonNull)
                    .collect(Collectors.groupingBy(
                            o -> o.name,
                            Collectors.mapping(
                                    o -> String.format("%s-%s;", o.worldX, o.worldY),
                                    Collectors.joining(" "))));

            objInfo.forEach((k, v) -> g2.drawString(String.format("%s: %s", k, v), debugInfoIndexX, debugInfoIndexY.getAndAdd(yDelta)));
        }

        g2.dispose();
    }

    public void playMusic(int i) {
        music.setFile(i);
        music.play();
        music.loop();
    }

    public void stopMusic() {
        music.stop();
    }

    public void playSE/*sound effects*/(int i) {
        se.setFile(i);
        se.play();
    }

}
