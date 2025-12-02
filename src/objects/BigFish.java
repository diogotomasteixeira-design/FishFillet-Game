package objects;

import java.util.List;
import pt.iscte.poo.game.Room;
import pt.iscte.poo.utils.Point2D;
import pt.iscte.poo.utils.Vector2D;

public class BigFish extends GameCharacter {
	
	private static BigFish bf = new BigFish(null);
	private static int currentStatus;
	
	private BigFish(Room room) {
		super(room);
		numMoves = 0;
		totalMoves = 0;
		currentStatus = -1;
		isAlive = true;
	}

	public static BigFish getInstance() {
		return bf;
	}

	@Override
	public void changeStatus(int x){
		if (x == 0){return;}
		currentStatus = x;
	}
	
	@Override
	public String getName() {
		if (currentStatus == 1){ return "bigFishRight"; }		
		return "bigFishLeft";
	}

	@Override
	public int getLayer() {
		return 1;
	}

  @Override
  public Point2D move(Vector2D direction) {
    Point2D destination = super.move(direction);

		if(hasWon){return null;}

		if (isExit(destination)){ 
			changeHasWon();
			return sendToDestination(destination);
		}

		List<GameObject> checkForHole = getObjectsAt(destination);
		for (GameObject object : checkForHole){
			if (object.getWeight() == Weight.HOLE){return getPosition();}
		}

		GameObject firstObj = getTopObj(destination);

		if (firstObj == null){return sendToDestination(destination);}

		if (firstObj.getWeight() == Weight.HOLE){return getPosition();}

		if (firstObj.getWeight() == Weight.TRAP){
				kill();
				return destination;
			}
		
		if (firstObj.getWeight() != Weight.LIGHT && firstObj.getWeight() != Weight.HEAVY){return getPosition();}

		int numHeavyPushed = 0;
		Vector2D dirObj = sub(destination, getPosition());
		Point2D checkPos = destination;
		int objectsMoved = 0;
    
    while (true) {

      GameObject currentObj = getTopObj(checkPos);

			if (dirObj.getY() == -1 && currentObj instanceof Anchor){return getPosition();}

      if (currentObj == null){break;}

      Weight w = currentObj.getWeight();

      if (w == Weight.IMMOVABLE){return getPosition();}

      if (currentObj.getWeight() == Weight.HEAVY) {
        numHeavyPushed++;
        if (numHeavyPushed > 1) {
					kill();
          return getPosition(); 
        }
      }

			if (direction.getY() == -1){if (objectsMoved >1){return getPosition();}}

      checkPos = add(checkPos, dirObj);
    }

    if (firstObj instanceof MovableObjects mo) {
      boolean pushed = mo.push(firstObj.getPosition(), dirObj);
      if (!pushed) {
        return getPosition();
      }
    }
    return sendToDestination(destination);
		
	}
}
