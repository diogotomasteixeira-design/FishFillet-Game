package objects;

import java.util.ArrayList;
import java.util.List;
import pt.iscte.poo.game.Room;
import pt.iscte.poo.utils.Point2D;
import pt.iscte.poo.utils.Vector2D;

public abstract class MovableObjects extends GameObject {

  public MovableObjects (Room room) {
		super(room);
	}

  @Override
	public boolean push(Point2D position, Vector2D direction){
		if (tryPushChain(position, direction)){
			setPosition(position.plus(direction));
			return true;
		}
		return false;
	}

	@Override
  public boolean tryPushChain(Point2D objPosition, Vector2D direction) {
    Point2D destination = objPosition.plus(direction);
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

  public static void gravity(Room currentRoom) {

    List<MovableObjects> falling = new ArrayList<>();
    List<MovableObjects> rising  = new ArrayList<>();

    for (int y = 9; y >= 0; y--) {
      for (int x = 0; x < 10; x++) {
        for (GameObject obj : currentRoom.getObjectsAtPosition(new Point2D(x, y))) {
          if (obj instanceof MovableObjects mo) {
            if (mo.floatsUp()) {
              rising.add(mo); 
            } else {
              falling.add(mo);
            }
          }
        }
      }
    }

    rising.sort((a, b) -> Integer.compare(a.getPosition().getY(), b.getPosition().getY()));

    for (MovableObjects mo : rising) {

      Vector2D movingDirection = new Vector2D(0, -1);
      Point2D position = mo.getPosition();

      Point2D up = position.plus(movingDirection);

      if (up.getY() < 0) continue;

      List<GameObject> above = currentRoom.getObjectsAtPosition(up);

      boolean blocked = false;
      for (GameObject obj : above) {
        
        if (obj.getWeight() != Weight.WATER) {
          if (obj instanceof MovableObjects && falling.contains(obj)) {
            Point2D down = position.plus(new Vector2D(0, 1));
            if (down.getY() <= 9) {
              mo.push(position, new Vector2D(0, 1));
            }
          blocked = true;
          break;
          }
        }
      }

      if (!blocked) {
        mo.push(position, movingDirection);
      }
    }


    for (MovableObjects mo : falling) {

      Point2D position = mo.getPosition();
      Point2D down = position.plus(new Vector2D(0, 1));

      if(mo instanceof Krab krab){
        krab.crabFall();
      }

      List<GameObject> below = currentRoom.getObjectsAtPosition(down);

      boolean blocked = false;
      for (GameObject obj : below) {
        if ((obj.getWeight() != Weight.WATER && obj.getWeight() != Weight.HOLE) ) {
          blocked = true;
          break;
        }
      }

      if (!blocked) {
        mo.push(position, new Vector2D(0, 1));
      }
    }
  }

  public static void randomMoves(Room currentRoom){
    for (int y = 9; y >= 0; y--) {
      for (int x = 0; x < 10; x++) {
        for (GameObject obj : currentRoom.getObjectsAtPosition(new Point2D(x, y))) {
          if (obj.movesRandomly())
            if(obj instanceof MovableObjects mo){
            mo.move();
          }
        }
      }
    }
  }

  public void move(){}

}

