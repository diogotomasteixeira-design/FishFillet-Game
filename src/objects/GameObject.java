package objects;

import java.util.List;
import pt.iscte.poo.game.Room;
import pt.iscte.poo.gui.ImageTile;
import pt.iscte.poo.utils.Point2D;
import pt.iscte.poo.utils.Vector2D;

public abstract class GameObject implements ImageTile{
	
	protected Point2D position;
	protected Room room;
	protected Weight weight = Weight.IMMOVABLE;
	protected boolean movesRandomly = false;
	protected boolean justSpawned = false;
	
	// Inicializa o objeto na sala especificada.
	public GameObject(Room room) {
		this.room = room;
	}
	
	// Inicializa o objeto na sala e posição especificadas.
	public GameObject(Point2D position, Room room) {
		this.position = position;
		this.room = room;
	}

	// Define a posição do objeto usando coordenadas x e y.
	public void setPosition(int i, int j) {
		position = new Point2D(i, j);
	}
	
	// Define a posição do objeto usando um Point2D.
	public void setPosition(Point2D position) {
		this.position = position;
	}

	// Retorna a posição atual do objeto.
	@Override
	public Point2D getPosition() {
		return position;
	}
	
	// Retorna a sala à qual o objeto pertence.
	public Room getRoom() {
		return room;
	}
	
	// Define a sala do objeto.
	public void setRoom(Room room) {
		this.room = room;
	}

	// Retorna o peso do objeto.
	public Weight getWeight(){
		return weight;
	}

	// Retorna se o objeto acabou de aparecer.
	public boolean getJustSpawned(){
		return justSpawned;
	}

	// Retorna se o objeto se move aleatoriamente.
	public boolean getMovesRandomly(){
		return movesRandomly;
	}

	// Retorna todos os objetos na posição especificada.
	public List<GameObject> getObjectsAt(Point2D pos) {
    return room.getObjectsAtPosition(pos);
  }

	// Retorna o primeiro objeto visível na posição especificada.
	public GameObject getTopObj(Point2D position){
		GameObject object = null;
		for (GameObject obj : getObjectsAt(position)) {
			if (obj.getWeight() != Weight.WATER) {
        object = obj;
				break;
			}
		}
		return object;
	}

	// Remove o objeto da sala.
	protected void removeFromRoom() {
    if (room != null) room.removeObject(this);
  }

	// Adiciona o objeto à sala.
	protected void addToRoom() {
    if (room != null) room.addObject(this);
  }

	// Verifica se a posição é uma saída da sala.
	public boolean isExit(Point2D destination){
		return destination.getX() == -1 || destination.getX() == 10 || destination.getY() == -1 || destination.getY() == 10;
	}

	// Tenta empurrar uma cadeia de objetos (padrão: não faz nada).
	public boolean tryPushChain(Point2D objPosition, Vector2D direction) {
    return false;
  }

	// Move o objeto para a posição de destino.
	public boolean push(Point2D destination, Vector2D direction){
    setPosition(destination);
		return true;
  }

	// Verifica se o objeto pode se mover para a posição especificada.
	public boolean canMove(Point2D position){
		List<GameObject> atPosition = getObjectsAt(position);
		for (GameObject object : atPosition) {
      if (object.getWeight() == Weight.IMMOVABLE){
        return false;
			}
    }
		return true;
	}

	//Subtrai duas posições.
	public Vector2D sub(Point2D pos1, Point2D pos2){
		Vector2D destination = new Vector2D(pos1.getX()-pos2.getX(),pos1.getY()-pos2.getY());
		return destination;
	}

	//Soma duas posições.
	public Point2D add(Point2D pos1, Point2D pos2){
		Point2D destination = new Point2D(pos1.getX()+pos2.getX(),pos1.getY()+pos2.getY());
		return destination;
	}

	//Soma uma posição com uma direção
	public Point2D add(Point2D pos1, Vector2D dir){
		Point2D destination = new Point2D(pos1.getX()+dir.getX(),pos1.getY()+dir.getY());
		return destination;
	}

	// Retorna se o objeto flutua para cima (padrão: false).
	public boolean floatsUp() {
    return false;
  }

	// Retorna se o objeto se move aleatoriamente (padrão: false).
	public boolean movesRandomly(){
    return false;
  }

	// Método para mover o objeto (padrão vazio).
	public void move(){}
	
	@Override
	public String toString(){
		return getName();
	}
}
