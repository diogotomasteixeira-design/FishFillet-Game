package objects;

import pt.iscte.poo.game.Room;

public class Buoy extends MovableObjects {

	public Buoy(Room room) {
		super(room);
		weight = Weight.LIGHT;
	}

	@Override
	public String getName() {
		return "buoy";
	}	

	@Override
	public int getLayer() {
		return 1;
	}
}