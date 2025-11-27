package objects;

//import java.util.Random;

import pt.iscte.poo.game.Room;
import pt.iscte.poo.utils.Point2D;
import pt.iscte.poo.utils.Vector2D;

public abstract class GameCharacter extends GameObject {

	protected boolean hasWon = false;
	protected int numMoves;
	protected boolean isAlive;

	public GameCharacter(Room room) {
		super(room);
	}
	
	public Point2D move(Vector2D direction) {
		int destx = getPosition().getX() + direction.getX();
		int desty = getPosition().getY() + direction.getY();
		Point2D destination = new Point2D(destx, desty);	
		return destination;
	}

	@Override
	public int getLayer() {
		return 2;
	}

	public void changeHasWon(){
		hasWon = true;
		setPosition(100,100);
	}

	public Point2D sendToDestination(Point2D destination){
		numMoves++;
		setPosition(destination);
		return destination;
	}

	public void kill(){
		isAlive = false;
	}

	public boolean getAliveStatus(){
		return isAlive;
	}

	public int getNumMoves(){
		return numMoves;
	}

	public boolean getHasWon(){
		return hasWon;
	}

	public void resetWin() {
		hasWon = false;
	}

  public void resetMoves() {
    numMoves = 0;
  }

	public void resetAliveStatus() {
    isAlive = true;
  }

	public abstract void changeStatus(int x);
}