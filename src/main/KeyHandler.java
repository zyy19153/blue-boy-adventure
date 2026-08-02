package main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyHandler implements KeyListener {

    public boolean upPressed, downPressed, leftPressed, rightPressed;

    // DEBUG
    public boolean debugOn = false;

    public void keyTyped(KeyEvent e) {}

    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();
        int modifiers = e.getModifiersEx();

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

        // DEBUG
        if (code == KeyEvent.VK_D && (modifiers & KeyEvent.CTRL_DOWN_MASK) != 0) {
            if (debugOn == false) {
                debugOn = true;
            } else if(debugOn == true) {
                debugOn = false;
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
