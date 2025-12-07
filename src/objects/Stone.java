package objects;

import pt.iscte.poo.game.Room;
import pt.iscte.poo.utils.Point2D;

public class Stone extends MovableObjects {

	private boolean canSpawnKrab = true;

	public Stone(Room room) {
		super(room);
		weight = Weight.HEAVY;
	}

	// Gera um Krab acima da pedra se possível
	public void spawnKrab(){
		if(!canSpawnKrab){return;}

		Point2D posAbove = new Point2D(getPosition().getX(), getPosition().getY() - 1);


		if (getTopObj(posAbove) != null){return;}
		
		GameObject crab = new Krab(room);
		crab.setPosition(posAbove);
		room.addObject(crab);
		canSpawnKrab = false;
	}

	@Override
	public String getName() {
		return "stone";
	}	

	@Override
	public int getLayer() {
		return 1;
	}
}
