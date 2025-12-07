package objects;

import pt.iscte.poo.game.Room;
import pt.iscte.poo.utils.Point2D;

public class Bomb extends MovableObjects {

	public Bomb(Room room) {
		super(room);
		weight = Weight.LIGHT;
	}

	@Override
	public String getName() {
		return "bomb";
	}	

	@Override
	public int getLayer() {
		return 1;
	}

	// Remove objetos adjacentes e mata peixes se forem atingidos
	public void explode() {

    int[][] offsets = {{0, -1},{0,  1},{-1, 0},{1,  0}};

    for (int[] off : offsets) {
        Point2D adjObject = new Point2D(position.getX() + off[0], position.getY() + off[1]);
        GameObject object = room.getTopObj(adjObject);

        if (object != null) {
          room.removeObject(object);
					if( object instanceof BigFish){
						BigFish.getInstance().kill();
					}
					if( object instanceof SmallFish){
						SmallFish.getInstance().kill();
					}
        }
    }

    room.removeObject(this);
}

}
