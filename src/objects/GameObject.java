package objects;

import java.util.List;
import pt.iscte.poo.game.Room;
import pt.iscte.poo.gui.ImageTile;
import pt.iscte.poo.utils.Point2D;
import pt.iscte.poo.utils.Vector2D;

public abstract class GameObject implements ImageTile{
	
	protected Point2D position;
	protected Room room;
	protected Weight weight = Weight.IMMOVABLE;
	
	public GameObject(Room room) {
		this.room = room;
	}
	
	public GameObject(Point2D position, Room room) {
		this.position = position;
		this.room = room;
	}

	public void setPosition(int i, int j) {
		position = new Point2D(i, j);
	}
	
	public void setPosition(Point2D position) {
		this.position = position;
	}

	@Override
	public Point2D getPosition() {
		return position;
	}
	
	public Room getRoom() {
		return room;
	}
	
	public void setRoom(Room room) {
		this.room = room;
	}

	public Weight getWeight(){
		return weight;
	}

	public List<GameObject> getObjectsAt(Point2D pos) {
    return room.getObjectsAtPosition(pos);
  }

	public GameObject getTopObj(Point2D position){
		GameObject object = null;
		for (GameObject obj : getObjectsAt(position)) {
			if (obj.getWeight() != Weight.WATER) {
        object = obj;
				break;
			}
		}
		return object;
	}

	protected void removeFromRoom() {
    if (room != null) room.removeObject(this);
  }

	protected void addToRoom() {
    if (room != null) room.addObject(this);
  }

	public boolean isExit(Point2D destination){
		return destination.getX() == -1 || destination.getX() == 10 || destination.getY() == -1 || destination.getY() == 10;
	}

	public boolean tryPushChain(Point2D objPosition, Vector2D direction) {
    return false;
  }

	public boolean push(Point2D destination, Vector2D direction){
    setPosition(destination);
		return true;
  }

	public boolean canMove(Point2D position){
		List<GameObject> atPosition = getObjectsAt(position);
		for (GameObject object : atPosition) {
      if (object.getWeight() == Weight.IMMOVABLE){
        return false;
			}
    }
		return true;
	}

	public Vector2D sub(Point2D obj, Point2D fish){
		Vector2D destination = new Vector2D(obj.getX()-fish.getX(),obj.getY()-fish.getY());
		return destination;
	}

	public Point2D add(Point2D direction, Point2D obj){
		Point2D destination = new Point2D(direction.getX()+obj.getX(),direction.getY()+obj.getY());
		return destination;
	}

	public Point2D add(Point2D direction, Vector2D obj){
		Point2D destination = new Point2D(direction.getX()+obj.getX(),direction.getY()+obj.getY());
		return destination;
	}

	public boolean floatsUp() {
    return false;
  }

	public boolean movesRandomly(){
    return false;
  }

	@Override
	public String toString(){
		return getName();
	}
}
