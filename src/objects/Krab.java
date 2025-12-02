package objects;

import java.util.Random;
import pt.iscte.poo.game.Room;
import pt.iscte.poo.utils.Point2D;
import pt.iscte.poo.utils.Vector2D;

public class Krab extends MovableObjects {

	private int moves = 0;

	public Krab(Room room) {
		super(room);
		weight = Weight.HEAVY;
	}

	@Override
	public String getName() {
		return "krab";
	}	

	@Override
	public int getLayer() {
		return 10;
	}

	@Override
	public boolean movesRandomly(){
    return true;
  }

	@Override
	public void move(){
		Random rand = new Random();

		Point2D posBelow = add(getPosition(), new Vector2D(0,1));
		if(room.getTopObj(posBelow) == null){return;}

		int x = rand.nextBoolean() ? 1 : -1;
		
    Vector2D direction = new Vector2D(x,0);
		Point2D destination = add(getPosition(),direction);

		GameObject destObj = getTopObj(destination);

		if(destObj == null){
			setPosition(destination);
      return;
		}
			
		if (destObj.getWeight() == Weight.TRAP) {
      room.removeObject(this);
      return;
    }
		if (destObj instanceof SmallFish sf) {
      sf.kill();
    }
		if (destObj instanceof BigFish) {
      room.removeObject(this);
      return;
    }

		if (destObj.getWeight() != Weight.HOLE) {
      return;
		}
		setPosition(destination);
	}
  

	public void crabFall() {

    Point2D down = getPosition().plus(new Vector2D(0, 1));

    for (GameObject obj : getRoom().getObjectsAtPosition(down)) {

        if (obj.getWeight() == Weight.WATER || obj.getWeight() == Weight.HOLE) continue;

        if (obj instanceof SmallFish sf) {
          sf.kill();
          return;
        }

        if (obj instanceof BigFish) {
          room.removeObject(this);
          return;
        }

      return;
    }

    setPosition(down);
	}
}
