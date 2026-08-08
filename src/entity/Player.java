package entity;

import java.awt.Graphics2D;
import java.awt.Font;
import java.awt.Color;
import java.awt.Rectangle;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.AlphaComposite;

import main.KeyHandler;
import main.GamePanel;
import main.UtilityTool;

public class Player extends Entity {

    KeyHandler keyH;

    // worldX: 在 地图 中的坐标
    // screenX: 在 屏幕 中的坐标
    // 这里的坐标都是给 drawImage 函数用的，指定 image 左上角的点在哪里
    public final int screenX;
    public final int screenY;

    public Player(GamePanel gp, KeyHandler keyH) {
        super(gp);
        this.keyH = keyH;

        screenX = gp.screenWidth / 2 - (gp.tileSize / 2);
        screenY = gp.screenHeight / 2 - (gp.tileSize / 2);

        // solidArea = new Rectangle(8, 16, 32, 32);
        solidArea = new Rectangle();
        solidArea.x = 8;
        solidArea.y = 16;
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
        solidArea.width = 32;
        solidArea.height = 32;

        setDefaultValues();
        getPlayerImage();
    }

    public void setDefaultValues() {
        worldX = gp.tileSize * 23;
        worldY = gp.tileSize * 21;
        speed = 4;
        direction = "down";

        // player status
        maxLife = 6;
        life = maxLife;
    }

    public void getPlayerImage() {
        up1    = setup("/player/boy_up_1");
        up2    = setup("/player/boy_up_2");
        down1  = setup("/player/boy_down_1");
        down2  = setup("/player/boy_down_2");
        left1  = setup("/player/boy_left_1");
        left2  = setup("/player/boy_left_2");
        right1 = setup("/player/boy_right_1");
        right2 = setup("/player/boy_right_2");
    }

    public void update() {

        if(invincible == true) {
            invincibleCounter++;
            if(invincibleCounter > 60) {
                invincible = false;
                invincibleCounter = 0;
            }
        }

        if (keyH.upPressed == false
            && keyH.downPressed == false
            && keyH.leftPressed == false
            && keyH.rightPressed == false
            ) return;

        if (keyH.upPressed == true)         direction = "up";
        else if (keyH.downPressed == true)  direction = "down";
        else if (keyH.leftPressed == true)  direction = "left";
        else if (keyH.rightPressed == true) direction = "right";

        // check tile collision
        collisionOn = false;
        gp.cChecker.checkTile(this);

        // check object collision
        int objIndex = gp.cChecker.checkObject(this, true);
        pickUpObject(objIndex);

        // check npc collision
        int npcIndex = gp.cChecker.checkEntity(this, gp.npc);
        interactNPC(npcIndex);

        // check monster collision
        int monsterIndex = gp.cChecker.checkEntity(this, gp.monster);
        contactMonster(monsterIndex);

        // check event
        gp.eHandler.checkEvent();

        // if collisionOn is false, can move; otherwise can not move!
        if (collisionOn == false && gp.keyH.enterPressed == false) {
            switch (direction) {
                case "up"    : worldY -= speed; break;
                case "down"  : worldY += speed; break;
                case "left"  : worldX -= speed; break;
                case "right" : worldX += speed; break;
            }
        }

        gp.keyH.enterPressed = false;

        // every frame call this update one time;
        spriteCounter++;
        if (spriteCounter > 15) {
            if (spriteNum == 1) spriteNum = 2;
            else if (spriteNum == 2) spriteNum = 1;
            spriteCounter = 0;
        }

    }

    public void pickUpObject(int i) {
        if (i != 999) /* player has touched an object */ {
        }
    }

    public void interactNPC(int i) {
        if (i != 999) {
            if (gp.keyH.enterPressed) {
                gp.gameState = gp.dialogueState;
                gp.npc[i].speak();
            }
        }
    }

    public void contactMonster(int i) {
        if (i != 999) {
            if (invincible == false) {
                life -= 1;
                invincible = true;
            }
        }
    }

    public void draw(Graphics2D g2) {
        // g2.setColor(Color.white);
        // g2.fillRect(x, y, gp.tileSize, gp.tileSize);

        BufferedImage image = null;

        switch (direction) {
            case "up":
                if (spriteNum == 1) {
                    image = up1;
                }
                if (spriteNum == 2) {
                    image = up2;
                }
                break;
            case "down":
                if (spriteNum == 1) {
                    image = down1;
                }
                if (spriteNum == 2) {
                    image = down2;
                }
                break;
            case "left":
                if (spriteNum == 1) {
                    image = left1;
                }
                if (spriteNum == 2) {
                    image = left2;
                }
                break;
            case "right":
                if (spriteNum == 1) {
                    image = right1;
                }
                if (spriteNum == 2) {
                    image = right2;
                }
                break;
        }

        if(invincible) {
            // make player half transparent
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.3F));
        }

        g2.drawImage(image, screenX, screenY, null);

        // reset alpha
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1F));

        // debug
        // g2.setFont(new Font("Consolas", Font.PLAIN, 26));
        // g2.setColor(Color.white);
        // g2.drawString("Invincible:" + invincibleCounter, 10, 400);
    }
}
