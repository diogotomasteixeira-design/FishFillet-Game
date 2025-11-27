package pt.iscte.poo.game;

import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import objects.Anchor;
import objects.Water;
import objects.Weight;
import objects.Wall;
import objects.BigFish;
import objects.Cup;
import objects.SteelHorizontal;
import objects.SteelVertical;
import objects.GameObject;
import objects.HoledWall;
import objects.SmallFish;
import objects.Trunk;
import objects.Trap;
import objects.Stone;
import objects.Bomb;
import pt.iscte.poo.utils.Point2D;
import objects.MovableObjects;

public class Room {
	
	private List<GameObject> objects;
	private String roomName;
	private GameEngine engine;
	private Point2D smallFishStartingPosition;
	private Point2D bigFishStartingPosition;
	private List<MovableObjects> MObjects = new ArrayList<>();
	
	public Room() {
		objects = new ArrayList<GameObject>();
	}

	private void setName(String name) {
		roomName = name;
	}
	
	public String getName() {
		return roomName;
	}
	
	private void setEngine(GameEngine engine) {
		this.engine = engine;
	}

	public void addObject(GameObject obj) {
		objects.add(obj);
		engine.updateGUI();
	}
	
	public void removeObject(GameObject obj) {
		objects.remove(obj);
		engine.updateGUI();
	}
	
	public List<GameObject> getObjects() {
		return objects;
	}

	public void setSmallFishStartingPosition(Point2D heroStartingPosition) {
		this.smallFishStartingPosition = heroStartingPosition;
	}
	
	public Point2D getSmallFishStartingPosition() {
		return smallFishStartingPosition;
	}
	
	public void setBigFishStartingPosition(Point2D heroStartingPosition) {
		this.bigFishStartingPosition = heroStartingPosition;
	}
	
	public Point2D getBigFishStartingPosition() {
		return bigFishStartingPosition;
	}

	public void setMObjects(MovableObjects obj){
		MObjects.add(obj);
	}
	
	public static Room readRoom(File f, GameEngine engine) {
		Room r = new Room();
		r.setEngine(engine);
		r.setName(f.getName());

		try (Scanner sc = new Scanner(f)) {

			int y = 0;
			while (sc.hasNextLine()) {

				String line = sc.nextLine();
				
				for (int x = 0; x < 10 ; x++) {

					GameObject water = new Water(r);
					water.setPosition(x, y);
					r.addObject(water);
					char c = line.charAt(x);

					switch (c) {

						case 'W':
							GameObject wall = new Wall(r);
							wall.setPosition(x, y);
							r.addObject(wall);
							break;

						case 'B':
							GameObject bf = BigFish.getInstance();
							bf.setRoom(r);
							bf.setPosition(x, y);
							r.setBigFishStartingPosition(new Point2D(x,y));
							r.addObject(bf);
							break;

						case 'H':
							GameObject horiz = new SteelHorizontal(r);
							horiz.setPosition(x, y);
							r.addObject(horiz);
							break;

						case 'S':
							GameObject sf = SmallFish.getInstance();
							sf.setRoom(r);
							sf.setPosition(x, y);
							r.setSmallFishStartingPosition(new Point2D(x,y));
							r.addObject(sf);
							break;

						case 'V':
							GameObject vert = new SteelVertical(r);
							vert.setPosition(x, y);
							r.addObject(vert);
							break;	

						case 'X':
							GameObject hole = new HoledWall(r);
							hole.setPosition(x, y);
							r.addObject(hole);
							break;

						case 'T':
							GameObject trap = new Trap(r);
							trap.setPosition(x, y);
							r.addObject(trap);
							break;

						case 'Y':
							GameObject trunk = new Trunk(r);
							trunk.setPosition(x, y);
							r.addObject(trunk);
							break;

						case 'b':
							MovableObjects bomb = new Bomb(r);
							bomb.setPosition(x, y);
							r.addObject(bomb);
							r.setMObjects(bomb);
							break;

						case 'A':
							MovableObjects anchor = new Anchor(r);
							anchor.setPosition(x, y);
							r.addObject(anchor);
							r.setMObjects(anchor);
							break;
						
						case 'R':
							MovableObjects stone = new Stone(r);
							stone.setPosition(x, y);
							r.addObject(stone);
							r.setMObjects(stone);
							break;

						case 'C':
							MovableObjects cup = new Cup(r);
							cup.setPosition(x, y);
							r.addObject(cup);
							r.setMObjects(cup);
							break;
					}
				}
				y++; 
			}
		} 
		catch (FileNotFoundException e) {
			System.err.println("Não foi encontrado o ficheiro: " + f.getName());
		}

		return r;
	}

	public List<GameObject> getObjectsAtPosition(Point2D position){
		
		List<GameObject> list = new ArrayList<>();
		for (GameObject object : objects){
			if(object.getPosition().equals(position)){
				list.add(object);
			}
		}
		return list;
	}

	public GameObject getTopObj(Point2D position){
		GameObject object = null;
		for (GameObject obj : getObjectsAtPosition(position)) {
			if (obj.getWeight() != Weight.WATER) {
        object = obj;
        break;
			}
		}
		return object;
	}

}