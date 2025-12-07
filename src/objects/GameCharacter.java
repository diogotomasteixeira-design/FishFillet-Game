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

	// Inicializa o personagem na sala.
	public GameCharacter(Room room) {
		super(room);
	}

	// Calcula a nova posição com base na direção e retorna.
	public Point2D move(Vector2D direction) {
		int destx = getPosition().getX() + direction.getX();
		int desty = getPosition().getY() + direction.getY();
		Point2D destination = new Point2D(destx, desty);	
		return destination;
	}

	// Retorna a camada do personagem para renderização.
	@Override
	public int getLayer() {
		return 2;
	}

	// Marca o personagem como vencedor e remove da sala.
	public void changeHasWon(){
		hasWon = true;
		setPosition(100,100);
	}

	// Move o personagem para o destino e incrementa os movimentos.
	public Point2D sendToDestination(Point2D destination){
		numMoves++;
		totalMoves++;
		setPosition(destination);
		return destination;
	}

	// Mata o personagem e incrementa mortes.
	public void kill(){
		isAlive = false;
		deaths++;
	}

	// Retorna se o personagem está vivo.
	public boolean getAliveStatus(){
		return isAlive;
	}

	// Retorna o número de mortes na sessão atual.
	public int getDeaths(){
		return deaths;
	}

	// Retorna o total de mortes acumuladas.
	public int getTotalDeaths(){
		return totalDeaths;
	}

	// Retorna o número de movimentos na sessão atual.
	public int getNumMoves(){
		return numMoves;
	}

	// Retorna o total de movimentos acumulados.
	public int getTotalMoves(){
		return totalMoves;
	}

	// Retorna se o personagem venceu.
	public boolean getHasWon(){
		return hasWon;
	}

	// Reseta o estado de vitória.
	public void resetWin() {
		hasWon = false;
	}

	// Acumula movimentos atuais no total e reseta os da sessão.
  public void resetMoves() {
		totalMoves += numMoves;
    numMoves = 0;
  }

	// Reseta o status de vivo.
	public void resetAliveStatus() {
    isAlive = true;
  }

	// Acumula mortes atuais no total e reseta as da sessão.
	public void resetDeaths(){
		totalDeaths += deaths;
		deaths = 0;
	}

	 // Método abstrato para atualizar o estado do personagem.
	public abstract void changeStatus(int x);
}