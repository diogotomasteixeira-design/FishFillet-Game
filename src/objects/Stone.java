package objects;

import pt.iscte.poo.game.Room;

public class Stone extends MovableObjects {

	public Stone(Room room) {
		super(room);
		weight = Weight.HEAVY;
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
