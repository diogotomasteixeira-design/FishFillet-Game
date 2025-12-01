package pt.iscte.poo.game;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import objects.SmallFish;
import objects.Weight;
import objects.BigFish;
import objects.Bomb;
import objects.GameCharacter;
import objects.GameObject;
import objects.MovableObjects;
import pt.iscte.poo.gui.ImageGUI;
import pt.iscte.poo.observer.Observed;
import pt.iscte.poo.observer.Observer;
import pt.iscte.poo.utils.Direction;
import pt.iscte.poo.utils.Point2D;
import pt.iscte.poo.utils.Vector2D;

public class GameEngine implements Observer {
	
	private Map<String,Room> rooms;
	private Room currentRoom;
	private int lastTickProcessed = 0;
	private GameCharacter fishTurn;
	private Direction pendingMovement = null;
	private int currentLevel = 0;
		
	public GameEngine() {
		rooms = new HashMap<String,Room>();
		loadGame();
		currentRoom = rooms.get("room" +currentLevel+ ".txt");
		updateGUI();		
		SmallFish.getInstance().setRoom(currentRoom);
		BigFish.getInstance().setRoom(currentRoom);
		fishTurn = SmallFish.getInstance();
	}

	private void loadGame() {
		File[] files = new File("./rooms").listFiles();
		for(File f : files) {
			rooms.put(f.getName(),Room.readRoom(f,this));
		}
	}

	@Override
	public void update(Observed source) {

		if (ImageGUI.getInstance().wasKeyPressed()) {
			int k = ImageGUI.getInstance().keyPressed();

			if (Direction.isRestart(k)){
				ImageGUI.getInstance().showMessage("Restart","Game Restarted");
				loadLevel(currentLevel);
				return;
			}

			if (Direction.isSwitch(k)) {
				if (fishTurn == SmallFish.getInstance()){
					fishTurn = BigFish.getInstance();
				}
				else { fishTurn = SmallFish.getInstance();}
				pendingMovement = null;
			}

			try {
				pendingMovement = Direction.directionFor(k);
			} catch (IllegalArgumentException e) {
				pendingMovement = null;
			}
		}

		int t = ImageGUI.getInstance().getTicks();
		while (lastTickProcessed < t) {
			processTick();
		}

		ImageGUI.getInstance().setStatusMessage("Good luck!              Time:" + getTime() + "                         SmallFishMoves: " + SmallFish.getInstance().getNumMoves() + " || BigFishMoves: " + BigFish.getInstance().getNumMoves());
		ImageGUI.getInstance().update();
	}

	private void processTick() {		 
		lastTickProcessed++;

		if(!BigFish.getInstance().getAliveStatus() || !SmallFish.getInstance().getAliveStatus()){
			ImageGUI.getInstance().showMessage("Dead", "You died");
			loadLevel(currentLevel);
		}

		if (bothFishHaveWon()) {
			ImageGUI.getInstance().showMessage("Victory!", "Level complete!");
			currentLevel++;
			loadLevel(currentLevel);
			return;
		}
		
		if (lastTickProcessed % 10 == 0){gravity();}


		if (pendingMovement == null){return;}

		int status = pendingMovement.asVector().getX();

		fishTurn.changeStatus(status);
		fishTurn.move(pendingMovement.asVector());
		
		pendingMovement = null;
	}

	public void updateGUI() {
		if(currentRoom!=null) {
			ImageGUI.getInstance().clearImages();
			ImageGUI.getInstance().addImages(currentRoom.getObjects());
		}
	}

	public boolean bothFishHaveWon(){
		return SmallFish.getInstance().getHasWon() && BigFish.getInstance().getHasWon();
	}
	
	private void loadLevel(int level) {
		
		String resetRoom = "room" + level + ".txt";
		rooms.put(currentRoom.getName(), Room.readRoom(new File("./rooms/" + resetRoom), this));

    Room next = rooms.get(resetRoom);

    if (next == null) {
        ImageGUI.getInstance().showMessage("Fim de Jogo", "Acabaste todos os níveis!");
        ImageGUI.getInstance().dispose();
				System.exit(0);
    }
    currentRoom = next;

    pendingMovement = null;
    fishTurn = SmallFish.getInstance(); 
    resetFishPositions();

    updateGUI();
		String title = "Level " + currentLevel;
    ImageGUI.getInstance().showMessage( title,"Good luck! º-º");
	}

	private void resetFishPositions() {

    Point2D sfPos = currentRoom.getSmallFishStartingPosition();
    Point2D bfPos = currentRoom.getBigFishStartingPosition();

    SmallFish sf = SmallFish.getInstance();
    BigFish bf = BigFish.getInstance();

    
    currentRoom.removeObject(sf);
    currentRoom.removeObject(bf);


    sf.setRoom(currentRoom);
    bf.setRoom(currentRoom);


    if (sfPos != null) sf.setPosition(sfPos);
    if (bfPos != null) bf.setPosition(bfPos);

  
    currentRoom.addObject(sf);
    currentRoom.addObject(bf);


    sf.resetMoves();  
    bf.resetMoves();

    sf.resetWin();    
    bf.resetWin();

		sf.resetAliveStatus();    
    bf.resetAliveStatus();

	}

	public void gravity() {

    int fallingLightOnSmall = 0;
    int fallingHeavyOnSmall = 0;
    int fallingHeavyOnBig = 0;

    for (int line = 9; line >= 0; line--) {
        for (int column = 0; column <= 9; column++) {

          Point2D position = new Point2D(column, line);
          List<GameObject> obj = currentRoom.getObjectsAtPosition(position);

          for (GameObject object : obj) {

            if (!(object instanceof MovableObjects mo)){continue;}

              int lightCount = 0;
              int heavyCount = 0;

              Point2D scan = position;

              while (true) {

                GameObject top = currentRoom.getTopObj(scan);
                if (top == null || !(top instanceof MovableObjects moStack)){break;}

                if (moStack.getWeight() == Weight.LIGHT){lightCount++;}
                if (moStack.getWeight() == Weight.HEAVY){heavyCount++;}
                  
								scan = new Point2D(scan.getX(), scan.getY() - 1);

                if (scan.getY() < 0){break;}
              }

              Point2D destination = new Point2D(column, line + 1);
              List<GameObject> belowObj = currentRoom.getObjectsAtPosition(destination);

              for (GameObject below : belowObj) {

                boolean protectedHoleOrTrap = false;

                if (below instanceof SmallFish) {
                  GameObject topBelow = currentRoom.getTopObj(destination);
                  if (topBelow != null && (topBelow.getWeight() == Weight.HOLE || topBelow.getWeight() == Weight.TRAP)) {
                    protectedHoleOrTrap = true;
                  }
                }

                if ( below instanceof SmallFish && !protectedHoleOrTrap) {
                  fallingLightOnSmall += lightCount;
                  fallingHeavyOnSmall += heavyCount;
                }

                if (below instanceof BigFish) {fallingHeavyOnBig += heavyCount;}
              }

							boolean pushed = mo.push(position, new Point2D(0, 1));

							if (pushed && mo instanceof Bomb bo) {

								GameObject landedOn = currentRoom.getTopObj(destination.plus(new Vector2D(0,1)));


								if (landedOn != null && !(landedOn instanceof SmallFish) && !(landedOn instanceof BigFish) && landedOn.getWeight() != Weight.WATER) {
											bo.explode();
											System.out.println("boom!");
									}
							}
            }
        }
    }

    if (fallingLightOnSmall >= 2 || fallingHeavyOnSmall >= 1) {
        SmallFish.getInstance().kill();
    }

    if (fallingHeavyOnBig > 1) {
        BigFish.getInstance().kill();
    }
	}

	public String getTime(){
		int totalSec = lastTickProcessed/10;
		int minutes = totalSec/60;
		int seconds = totalSec%60;
		return String.format("%02d:%02d", minutes, seconds);
	}
}

