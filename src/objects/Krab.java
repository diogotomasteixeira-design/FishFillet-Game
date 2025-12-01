package objects;

import java.util.List;
import java.util.Random;
import pt.iscte.poo.game.Room;
import pt.iscte.poo.utils.Point2D;
import pt.iscte.poo.utils.Vector2D;

public class Krab extends MovableObjects {

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

		int x = rand.nextInt(3)-1;
		System.out.println(x);

		if (x == 0){return;}

    Vector2D direction = new Vector2D(x,0);
		Point2D destination = add(getPosition(),direction);

		List<GameObject> destObj = getObjectsAt(destination);
		for (GameObject obj : destObj){

			if (obj.getWeight() == Weight.IMMOVABLE){move();}
			
			if (obj.getWeight() == Weight.TRAP) {
        room.removeObject(this);
        return;
      }
			if (obj instanceof SmallFish sf) {
        sf.kill();
        return;
      }
			if (obj instanceof BigFish) {
        room.removeObject(this);
        return;
      }
			if (obj.getWeight() != Weight.WATER && obj.getWeight() != Weight.HOLE) {
        return;
			}
			setPosition(destination);
		}
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
