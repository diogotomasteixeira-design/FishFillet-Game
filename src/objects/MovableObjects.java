package objects;

import java.util.List;
import pt.iscte.poo.game.Room;
import pt.iscte.poo.utils.Point2D;

public abstract class MovableObjects extends GameObject {

  public MovableObjects (Room room) {
		super(room);
	}

  @Override
	public boolean push(Point2D position, Point2D direction){
		if (tryPushChain(position, direction)){
			setPosition(add(position, direction));
			return true;
		}
		return false;
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

