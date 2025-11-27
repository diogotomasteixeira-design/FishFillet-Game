package objects;

import pt.iscte.poo.game.Room;

public class Water extends GameObject{

	public Water(Room room) {
		super(room);
		weight = Weight.WATER;
	}

	@Override
	public String getName() {
		return "water";
	}

	@Override
	public int getLayer() {
		return 0;
	}
}
