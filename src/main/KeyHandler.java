package main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyHandler implements KeyListener {

    GamePanel gp;
    public boolean upPressed, downPressed, leftPressed, rightPressed, enterPressed;

    // DEBUG
    public boolean debugOn = false;

    public KeyHandler(GamePanel gp) {
        this.gp = gp;
    }

    public void keyTyped(KeyEvent e) {}

    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();
        int modifiers = e.getModifiersEx();


        if (code == KeyEvent.VK_C && (modifiers & KeyEvent.CTRL_DOWN_MASK) != 0) {
            System.exit(0);
        }

        // title state
        if (gp.gameState == gp.titleState) {

            if (gp.ui.titleScreenState == 0) {

                if (code == KeyEvent.VK_UP) {
                    gp.ui.commandNum--;
                    if (gp.ui.commandNum < 0) {
                        gp.ui.commandNum = 2;
                    }
                }

                if (code == KeyEvent.VK_DOWN) {
                    gp.ui.commandNum++;
                    if (gp.ui.commandNum > 2) {
                        gp.ui.commandNum = 0;
                    }
                }

                if (code == KeyEvent.VK_ENTER) {
                    if (gp.ui.commandNum == 0) {
                        // gp.gameState = gp.playState;
                        gp.ui.titleScreenState = 1;
                    }
                    else if (gp.ui.commandNum == 1) {
                        // todo
                    }
                    else if (gp.ui.commandNum == 2) {
                        System.exit(0);
                    }
                }

            }

            else if (gp.ui.titleScreenState == 1) {

                if (code == KeyEvent.VK_UP) {
                    gp.ui.commandNum--;
                    if (gp.ui.commandNum < 0) {
                        gp.ui.commandNum = 3;
                    }
                }

                if (code == KeyEvent.VK_DOWN) {
                    gp.ui.commandNum++;
                    if (gp.ui.commandNum > 3) {
                        gp.ui.commandNum = 0;
                    }
                }

                if (code == KeyEvent.VK_ENTER) {
                    if (gp.ui.commandNum == 0) {
                        // do some fighter specific stuff;
                        gp.gameState = gp.playState;
                        gp.playMusic(0);
                    }
                    else if (gp.ui.commandNum == 1) {
                        // do some thief specific stuff;
                        gp.gameState = gp.playState;
                        gp.playMusic(0);
                    }
                    else if (gp.ui.commandNum == 2) {
                        // do some sorcerer specific stuff;
                        gp.gameState = gp.playState;
                        gp.playMusic(0);
                    }
                    else if (gp.ui.commandNum == 3) {
                        gp.ui.titleScreenState = 0;
                        gp.ui.commandNum = 0;
                    }
                }

            }


        }

        // play state
        if (gp.gameState == gp.playState) {

            if (code == KeyEvent.VK_UP) {
                upPressed = true;
            }

            if (code == KeyEvent.VK_DOWN) {
                downPressed = true;
            }

            if (code == KeyEvent.VK_LEFT) {
                leftPressed = true;
            }

            if (code == KeyEvent.VK_RIGHT) {
                rightPressed = true;
            }

            if (code == KeyEvent.VK_P) {
                gp.gameState = gp.pauseState;
            }

            if (code == KeyEvent.VK_ENTER) {
                enterPressed = true;
            }

            // DEBUG
            if (code == KeyEvent.VK_D && (modifiers & KeyEvent.CTRL_DOWN_MASK) != 0) {
                if (debugOn == false) {
                    debugOn = true;
                } else if(debugOn == true) {
                    debugOn = false;
                }
            }
        }

        // pause state
        else if(gp.gameState == gp.pauseState) {
            if (code == KeyEvent.VK_P) {
                gp.gameState = gp.playState;
            }

        }

        // dialogue state
        else if(gp.gameState == gp.dialogueState) {
            if (code == KeyEvent.VK_ENTER) {
                gp.gameState = gp.playState;
            }
        }

    }

    public void keyReleased(KeyEvent e) {
        int code = e.getKeyCode();

        if (code == KeyEvent.VK_UP) {
            upPressed = false;
        }

        if (code == KeyEvent.VK_DOWN) {
            downPressed = false;
        }

        if (code == KeyEvent.VK_LEFT) {
            leftPressed = false;
        }

        if (code == KeyEvent.VK_RIGHT) {
            rightPressed = false;
        }

    }
}
