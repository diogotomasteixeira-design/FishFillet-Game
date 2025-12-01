package objects;

import java.util.List;
import pt.iscte.poo.game.Room;
import pt.iscte.poo.utils.Point2D;
import pt.iscte.poo.utils.Vector2D;

public class Cup extends MovableObjects {

	public Cup(Room room) {
		super(room);
		weight = Weight.LIGHT;
	}

	@Override
	public String getName() {
		return "cup";
	}	

	@Override
	public int getLayer() {
		return 1;
	}

	@Override
  public boolean tryPushChain(Point2D objPosition, Vector2D direction) {
    Point2D destination = objPosition.plus(direction);
    List<GameObject> objectsAtDest = getObjectsAt(destination);

    for (GameObject object : objectsAtDest) {
      if (object.getWeight() ==  Weight.IMMOVABLE || object.getWeight() == Weight.TRAP) {
        return false;
      }
    }

    for (GameObject object : objectsAtDest) {
      if (object instanceof MovableObjects mo) {
        if (!mo.tryPushChain(object.getPosition(), direction)) {
          return false;
        }
      }
    }
    return true;
  }
}

