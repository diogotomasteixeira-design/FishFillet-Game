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
import objects.Krab;
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
	private long levelStartTime;
	private long totalStartTime;
	private String playerName;
		
	public GameEngine() {
		boolean valid = false;

		while (!valid) {
			try {
				playerName = ImageGUI.getInstance().showInputDialog(
					"Nome do jogador",
					"Insere a tua Gamer Tag: "
				);

				if (playerName == null) {throw new NullPointerException("Cancel pressed");}

				if (playerName.trim().isEmpty()) {
					throw new IllegalArgumentException("Nome vazio");
				}

				valid = true;

			} catch (NullPointerException e) {
				ImageGUI.getInstance().dispose();
				System.exit(0);

			} catch (IllegalArgumentException e) {
				ImageGUI.getInstance().showMessage("Erro", "Tens de inserir um nome válido!");
			}
		}
		
		rooms = new HashMap<String,Room>();
		loadGame();
		currentRoom = rooms.get("room" +currentLevel+ ".txt");
		updateGUI();		
		SmallFish.getInstance().setRoom(currentRoom);
		BigFish.getInstance().setRoom(currentRoom);
		fishTurn = SmallFish.getInstance();

		totalStartTime = System.currentTimeMillis();
		levelStartTime = System.currentTimeMillis();
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
			if(!(SmallFish.getInstance().getHasWon() || BigFish.getInstance().getHasWon())){swapTurn();}
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

		ImageGUI.getInstance().setStatusMessage("Good luck! Room:"+currentLevel+"   Time:" + getTime() + "                SmallFishMoves: " + SmallFish.getInstance().getNumMoves() + " || BigFishMoves: " + BigFish.getInstance().getNumMoves());
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
			long levelTime = (System.currentTimeMillis() - levelStartTime) / 1000;

			ranking.score(
				playerName,
				currentLevel,
				SmallFish.getInstance().getNumMoves(),
				BigFish.getInstance().getNumMoves(),
				SmallFish.getInstance().getDeaths(),
				BigFish.getInstance().getDeaths(),
				levelTime
			);
			showRankingLevel(levelTime);
			levelStartTime = System.currentTimeMillis();
			SmallFish.getInstance().resetDeaths();
			BigFish.getInstance().resetDeaths();
			
			currentLevel++;
			loadLevel(currentLevel);
			return;
		}
		
		if (lastTickProcessed % 10 == 0){MovableObjects.gravity(currentRoom);}


		if (pendingMovement == null){return;}

		int status = pendingMovement.asVector().getX();
		Point2D currentPos = fishTurn.getPosition();
		
		fishTurn.changeStatus(status);
		fishTurn.move(pendingMovement.asVector());

		if(fishTurn.getHasWon()){swapTurn();}

		Point2D endPos = fishTurn.getPosition();

		if(!endPos.equals(currentPos)){MovableObjects.randomMoves(currentRoom);}
		
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
      long totalTime = (System.currentTimeMillis() - totalStartTime) / 1000;

			ranking.score(
				playerName,
				100, // <-- Usa 100 para o score total das salas
				SmallFish.getInstance().getTotalMoves(),
				BigFish.getInstance().getTotalMoves(),
				SmallFish.getInstance().getDeaths(),
				BigFish.getInstance().getDeaths(),
				totalTime
			);

			ImageGUI.getInstance().showMessage("Fim de Jogo", "Acabaste todos os níveis!");
			showRankingGame(totalTime);
			ImageGUI.getInstance().dispose();
			System.exit(0);
    }
    currentRoom = next;

    pendingMovement = null;
    fishTurn = SmallFish.getInstance(); 
    resetFishPositions();

    updateGUI();
		String title = "Level " + currentLevel;
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

	public String getTime(){
		int totalSec = lastTickProcessed/10;
		int minutes = totalSec/60;
		int seconds = totalSec%60;
		return String.format("%02d:%02d", minutes, seconds);
	}

	public void swapTurn(){
		if (fishTurn == SmallFish.getInstance()){
			fishTurn = BigFish.getInstance();
		}else { fishTurn = SmallFish.getInstance();}
	}

	private void showRankingLevel(long totalTime) {

    List<String> rankSala = ranking.getScoresForLevel(currentLevel);
    rankSala = ranking.top5(rankSala);

    String msg = "=== Ranking da Sala " + currentLevel + " ===\n\n";

    for (String s : rankSala) {
			String[] d = s.split(";");
			msg += "Player: " + d[0]
					+ " | SMov: " + d[2]
					+ " | BMov: " + d[3]
					+ " | SDeaths: " + d[4]
					+ " | BDeaths: " + d[5]
					+ " | Time: " + d[6] + "s\n";
    }

    msg += "\n--- O teu resultado ---\n";
    msg += "Player: " + playerName
        + " | SMov: " + SmallFish.getInstance().getNumMoves()
        + " | BMov: " + BigFish.getInstance().getNumMoves()
        + " | SDeaths: " + SmallFish.getInstance().getDeaths()
        + " | BDeaths: " + BigFish.getInstance().getDeaths()
        + " | Time: " + totalTime + "s\n";

    ImageGUI.getInstance().showMessage("Ranking", msg);
	}

	private void showRankingGame(long totalTime) {

		List<String> total = ranking.getFinalScores();
		total = ranking.top5(total);


		String msg = "=== Ranking FINAL ===\n\n";

		msg +=  "--- Top 5 ---\n";

		for (String s : total) {
    String[] d = s.split(";");

    msg += "Player: " + d[0]
        + " | SMov: " + d[2]
        + " | SDeaths: " + d[4]
        + " | BDeaths: " + d[5]
        + " | Time: " + d[6] + "s\n";
}

		msg += "\n--- O teu resultado ---\n";
		msg += "Player: " + playerName
			+ " | SMov: " + SmallFish.getInstance().getTotalMoves()
			+ " | BMov: " + BigFish.getInstance().getTotalMoves()
			+ " | SDeaths: " + SmallFish.getInstance().getTotalDeaths()
			+ " | BDeaths: " + BigFish.getInstance().getTotalDeaths()
			+ " | Time: " + totalTime + "s\n";

		ImageGUI.getInstance().showMessage("Ranking", msg);
	}
}

