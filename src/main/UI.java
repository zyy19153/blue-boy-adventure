package main;

import java.awt.Graphics2D;
import java.awt.Font;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.text.DecimalFormat;

import object.OBJ_Key;

public class UI {
    GamePanel gp;
    Font arial_40, arial_80_bold;
    BufferedImage keyImage;
    public boolean messageOn = false;
    public String message = "";
    int messageCounter = 0;
    public boolean gameFinished = false;

    double playTime;
    DecimalFormat dFormat = new DecimalFormat("#0.00");

    public UI(GamePanel gp) {
        this.gp = gp;
        arial_40 = new Font("Arial", Font.PLAIN, 40);
        arial_80_bold = new Font("Arial", Font.BOLD, 80);
        OBJ_Key key = new OBJ_Key();
        keyImage = key.image;
    }

    public void showMessage(String text) {
        message = text;
        messageOn = true;
    }

    public void draw(Graphics2D g2) {

        if (gameFinished) {

            g2.setFont(arial_40);
            g2.setColor(Color.white);

            String text;
            int textLength;
            int x, y;

            text = "You found the treasure!";
            textLength = (int) g2.getFontMetrics().getStringBounds(text, g2).getWidth();
            x = gp.screenWidth/2 - textLength/2;
            y = gp.screenHeight/2 - gp.tileSize*3;
            g2.drawString(text, x, y);

            text = "You Time is :" + dFormat.format(playTime) + "!";
            textLength = (int) g2.getFontMetrics().getStringBounds(text, g2).getWidth();
            x = gp.screenWidth/2 - textLength/2;
            y = gp.screenHeight/2 + gp.tileSize*4;
            g2.drawString(text, x, y);

            g2.setFont(arial_80_bold);
            g2.setColor(Color.yellow);
            text = "Congratulations!";
            textLength = (int) g2.getFontMetrics().getStringBounds(text, g2).getWidth();
            x = gp.screenWidth/2 - textLength/2;
            y = gp.screenHeight/2 + gp.tileSize*2;
            g2.drawString(text, x, y);

            gp.gameThread = null;

            return;
        }

        //
        // NOTE: don't create new instance inside a game loop!
        //
        // g2.setFont(new Font("Arial", Font.PLAIN, 40));
        g2.setFont(arial_40);
        g2.setColor(Color.white);

        //
        // g2's drawImage's y doesn't have the same meaning as the y from drawString
        // g2's drawString's y: the String's baseline from the y_0
        //
        // param: font name, font style, font size
        // g2.drawString("Key = " + gp.player.hasKey, 25/*x pixel*/, 50/*y pixel*/);

        g2.drawImage(keyImage, gp.tileSize / 2, gp.tileSize / 2, gp.tileSize, gp.tileSize, null);
        g2.drawString("x " + gp.player.hasKey, 74, 65);

        // time
        playTime += (double) 1/60;
        g2.drawString("Time: " + dFormat.format(playTime), gp.tileSize*11, 65);

        // message
        if (messageOn) {
            g2.setFont(g2.getFont().deriveFont(30f));
            g2.drawString(message, gp.tileSize/2, gp.tileSize*5);
            messageCounter++;

            // we have 60 fps, so 120 count means 2 seconds
            if (messageCounter > 120) {
                messageCounter = 0;
                messageOn = false;
            }
        }
    }
}
