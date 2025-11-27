package objects;

import java.util.List;
import pt.iscte.poo.game.Room;
import pt.iscte.poo.utils.Point2D;

public class Anchor extends MovableObjects {

	public Anchor(Room room) {
		super(room);
		weight = Weight.HEAVY;
	}

	@Override
	public String getName() {
		return "anchor";
	}	

	@Override
	public int getLayer() {
		return 1;
	}

	@Override
  public boolean tryPushChain(Point2D objPosition, Point2D direction) {
    Point2D destination = add(objPosition, direction);
    List<GameObject> objectsAtDest = getObjectsAt(destination);

    for (GameObject object : objectsAtDest) {
      if (object.getWeight() ==  Weight.IMMOVABLE || object.getWeight() == Weight.HOLE || object.getWeight() == Weight.TRAP) {
        return false;
      }
    }

    for (GameObject object : objectsAtDest) {
      if (object instanceof MovableObjects mo) {
        if (!mo.push(object.getPosition(), direction)) {
          return false;
        }
      }
    }
    return true;
  }
}