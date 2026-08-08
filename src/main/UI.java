package main;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;

import object.OBJ_Key;
import object.OBJ_Heart;
import entity.Entity;

public class UI {
    GamePanel gp;
    Graphics2D g2;
    Font purisaB;
    BufferedImage heart_full, heart_half, heart_blank;
    public boolean messageOn = false;
    public String message = "";
    int messageCounter = 0;
    public boolean gameFinished = false;
    public String currentDialogue = "";
    public int commandNum = 0;
    public int titleScreenState = 0; // 0: the first screen; 1: the second screen;

    public UI(GamePanel gp) {
        this.gp = gp;
        InputStream is = getClass().getResourceAsStream("/font/Purisa Bold.ttf");
        try {
            purisaB = Font.createFont(Font.TRUETYPE_FONT, is);
        } catch (FontFormatException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // create heart object
        Entity heart = new OBJ_Heart(gp);
        heart_full = heart.image;
        heart_half = heart.image1;
        heart_blank = heart.image2;
    }

    public void showMessage(String text) {
        message = text;
        messageOn = true;
    }

    public void draw(Graphics2D g2) {
        this.g2 = g2;

        g2.setFont(purisaB);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2.setColor(Color.white);

        // TITLE STATE
        if (gp.gameState == gp.titleState) {
            drawTitleScreen();
        }

        // PLAY STATE
        if (gp.gameState == gp.playState) {
            // Do playState stuff
            drawPlayerLife();
        }
        // PAUSE STATE
        if (gp.gameState == gp.pauseState) {
            // Do pauseState stuff
            drawPlayerLife();
            drawPauseScreen();
        }
        // dialogue state
        if (gp.gameState == gp.dialogueState) {
            drawPlayerLife();
            drawDialogueScreen();
        }
    }

    public void drawPlayerLife() {

        int x = gp.tileSize/2;
        int y = gp.tileSize/2;
        int i = 0;

        // draw max life
        while (i < gp.player.maxLife/2) {
            g2.drawImage(heart_blank, x, y, null);
            i++;
            x += gp.tileSize;
        }

        x = gp.tileSize/2;
        y = gp.tileSize/2;
        i = 0;

        // gp.player.life = 5; // only for test

        // draw current life
        while (i < gp.player.life) {
            g2.drawImage(heart_half, x, y, null);
            i++;
            if (i < gp.player.life) {
                g2.drawImage(heart_full, x, y, null);
            }
            i++;
            x += gp.tileSize;
        }

    }

    public void drawTitleScreen() {

        if (titleScreenState == 0) {

            g2.setColor(new Color(70, 120, 80));
            g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);

            // title name
            g2.setFont(g2.getFont().deriveFont(Font.BOLD, 50));
            String text = "Blue Boy Adventure";
            int x = getXForCenterText(text);
            int y = gp.tileSize*3;

            // shadow
            g2.setColor(Color.black);
            g2.drawString(text, x+5, y+5);

            // main color
            g2.setColor(Color.white);
            g2.drawString(text, x, y);

            // blue boy image
            x = gp.screenWidth/2 - gp.tileSize;
            y += gp.tileSize*2;
            g2.drawImage(gp.player.down1, x, y, gp.tileSize*2, gp.tileSize*2, null);

            // menu
            g2.setFont(g2.getFont().deriveFont(Font.BOLD, 24F));

            text = "New Game";
            x = getXForCenterText(text);
            y += gp.tileSize*4;
            g2.drawString(text, x, y);
            if (commandNum == 0) {
                g2.drawString(">", x-gp.tileSize, y);
            }

            text = "Load Game";
            x = getXForCenterText(text);
            y += gp.tileSize;
            g2.drawString(text, x, y);
            if (commandNum == 1) {
                g2.drawString(">", x-gp.tileSize, y);
            }

            text = "Quit";
            x = getXForCenterText(text);
            y += gp.tileSize;
            g2.drawString(text, x, y);
            if (commandNum == 2) {
                g2.drawString(">", x-gp.tileSize, y);
            }
        }

        else if (titleScreenState == 1) {

            g2.setColor(new Color(70, 120, 80));
            g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);

            // class selection screen
            g2.setColor(Color.white);
            g2.setFont(g2.getFont().deriveFont(21F));

            String text = "Select You Class!";
            int x = getXForCenterText(text);
            int y = gp.tileSize*3;
            g2.drawString(text, x, y);

            text = "Fighter";
            x = getXForCenterText(text);
            y += gp.tileSize*3;
            g2.drawString(text, x, y);
            if (commandNum == 0) {
                g2.drawString(">", x-gp.tileSize, y);
            }

            text = "Thief";
            x = getXForCenterText(text);
            y += gp.tileSize;
            g2.drawString(text, x, y);
            if (commandNum == 1) {
                g2.drawString(">", x-gp.tileSize, y);
            }

            text = "Sorcerer";
            x = getXForCenterText(text);
            y += gp.tileSize;
            g2.drawString(text, x, y);
            if (commandNum == 2) {
                g2.drawString(">", x-gp.tileSize, y);
            }

            text = "Back";
            x = getXForCenterText(text);
            y += gp.tileSize*2;
            g2.drawString(text, x, y);
            if (commandNum == 3) {
                g2.drawString(">", x-gp.tileSize, y);
            }

        }

    }


    public void drawDialogueScreen() {
        // win
        int x = gp.tileSize * 2;
        int y = gp.tileSize / 2;
        int width = gp.screenWidth - (gp.tileSize*4);
        int height = gp.tileSize*4;

        drawSubWindow(x, y, width, height);

        g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 20F));
        x += gp.tileSize;
        y += gp.tileSize;

        for (String line : currentDialogue.split("\n")) {
            g2.drawString(line, x, y);
            y+=40;
        }
    }

    public void drawSubWindow(int x, int y, int width, int height) {
        // Color c = new Color(0, 0, 0); // black
        Color c = new Color(0, 0, 0, /*transparencyValue:0-255*/210);
        g2.setColor(c);
        g2.fillRoundRect(x, y, width, height, /*arcWidth*/35, /*arcHeight*/35);
        c = new Color(255, 255, 255); // white
        g2.setColor(c);
        g2.setStroke(new BasicStroke(/*width*/5));
        g2.drawRoundRect(x+5, y+5, width-10, height-10, 25, 25);
    }

    public void drawPauseScreen() {
        g2.setFont(g2.getFont().deriveFont(80f));
        String text = "PAUSED";

        int x = getXForCenterText(text), y = gp.screenHeight/2;

        g2.drawString(text, x, y);
    }

    public int getXForCenterText(String text) {
        int length = (int) g2.getFontMetrics().getStringBounds(text, g2).getWidth();
        return gp.screenWidth / 2 - length / 2;
    }

}
