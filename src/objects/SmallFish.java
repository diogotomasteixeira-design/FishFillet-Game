package objects;

import pt.iscte.poo.game.Room;
import pt.iscte.poo.utils.Point2D;
import pt.iscte.poo.utils.Vector2D;

public class SmallFish extends GameCharacter {

	private static SmallFish sf = new SmallFish(null);
	private static int currentStatus;
	
	private SmallFish(Room room) {
		super(room);
		currentStatus = -1;
		numMoves = 0;
		totalMoves = 0;
		isAlive = true;
	}

	public static SmallFish getInstance() {
		return sf;
	}

	@Override
	public void changeStatus(int x){
		if (x == 0){return;}
		currentStatus = x;
	}
	
	@Override
	public String getName() {
		if (currentStatus == 1){ return "smallFishRight"; }		
		return "smallFishLeft";
	}

	@Override
	public int getLayer() {
		return 2;
	}

  @Override
  public Point2D move(Vector2D direction) {
    Point2D destination = super.move(direction);
		
		if(hasWon){return null;}

		if (isExit(destination)){ 
			changeHasWon();
			return sendToDestination(destination);
		}

		GameObject object = getTopObj(destination);

		if(object instanceof Krab){kill();}

		if (object == null){return sendToDestination(destination);}

		if (object.getWeight() == Weight.HOLE || object.getWeight() == Weight.TRAP){return sendToDestination(destination);}

		if (object.getWeight() != Weight.LIGHT){return getPosition();}

		if ("buoy".equals(object.getName()) && direction.getY() != 0){return getPosition();}

		Vector2D dirObj = sub(destination, getPosition());
    Point2D nextPos = object.getPosition().plus(dirObj);
		boolean nextIsBlocking = false;
    
    for (GameObject obj : room.getObjectsAtPosition(nextPos)) {

			if (object instanceof Cup && obj.getWeight() == Weight.HOLE){break;}

      if (obj.getWeight() != Weight.WATER) {
				if (obj.getWeight() != Weight.IMMOVABLE && direction.equals(new Vector2D(0,-1))){
					kill();
				}
        nextIsBlocking = true;
        break;
      }
    }
    
    if (nextIsBlocking) {
			return getPosition();
		}

		if (object.push(object.getPosition(), dirObj)){return sendToDestination(destination);}

    return getPosition();
	}
}
