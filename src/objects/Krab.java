package objects;

import pt.iscte.poo.game.Room;

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
		return 1;
	}
}
