package objects;

//import java.util.Random;

import pt.iscte.poo.game.Room;
import pt.iscte.poo.utils.Point2D;
import pt.iscte.poo.utils.Vector2D;

public abstract class GameCharacter extends GameObject {

	protected boolean hasWon = false;
	protected int numMoves;
	protected int totalMoves;
	protected boolean isAlive;
	protected int deaths = 0;
	protected int totalDeaths = 0;

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
		totalMoves++;
		setPosition(destination);
		return destination;
	}

	public void kill(){
		isAlive = false;
		deaths++;
	}

	public boolean getAliveStatus(){
		return isAlive;
	}

	public int getDeaths(){
		return deaths;
	}

	public int getTotalDeaths(){
		return totalDeaths;
	}

	public int getNumMoves(){
		return numMoves;
	}

	public int getTotalMoves(){
		return totalMoves;
	}

	public boolean getHasWon(){
		return hasWon;
	}

	public void resetWin() {
		hasWon = false;
	}

  public void resetMoves() {
		totalMoves += numMoves;
    numMoves = 0;
  }

	public void resetAliveStatus() {
    isAlive = true;
  }

	public void resetDeaths(){
		totalDeaths += deaths;
		deaths = 0;
	}

	public abstract void changeStatus(int x);
}