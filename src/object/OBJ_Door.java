package object;

import main.GamePanel;
import entity.Entity;

public class OBJ_Door extends Entity {

    public OBJ_Door(GamePanel gp) {
        super(gp);
        name = "Door";
        down1 = setup("/objects/door");
        collision = true;
    }
}
