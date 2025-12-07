package pt.iscte.poo.utils;

import java.awt.event.KeyEvent;
import java.io.Serializable;
import java.util.Random;

/**
 * @author POO2016
 * @version 07-Nov-2021
 * 
 * Cardinal directions
 *
 */
public enum Direction implements Serializable {
	RESTART(new Vector2D(0,0)), SPACE(new Vector2D(0,0)), LEFT(new Vector2D(-1,0)), UP(new Vector2D(0,-1)), RIGHT(new Vector2D(1,0)), DOWN(new Vector2D(0,1));

	private Vector2D vector;

	// Inicializa a direção com o vetor correspondente
	Direction(Vector2D vector) {
		this.vector = vector;
	}

	// Retorna o vetor associado à direção
	public Vector2D asVector() {
		return vector;
	}

	// Retorna a direção associada a uma tecla pressionada
	public static Direction directionFor(int keyCode) {
		switch(keyCode){
			case KeyEvent.VK_SPACE:
				return null;
			case KeyEvent.VK_DOWN:
				return DOWN;	
			case KeyEvent.VK_UP:
				return UP;
			case KeyEvent.VK_LEFT:
				return LEFT;
			case KeyEvent.VK_RIGHT:
				return RIGHT;
			case KeyEvent.VK_R:
				return RESTART;
		}

		throw new IllegalArgumentException();
	}

	// Verifica se a tecla é uma direção
	public static boolean isDirection(int lastKeyPressed) {		
		return lastKeyPressed >= KeyEvent.VK_LEFT && lastKeyPressed <= KeyEvent.VK_DOWN;  			
	}

	// Retorna a direção oposta
	public Direction opposite() {
		switch (this) {
			case UP: return DOWN;
			case DOWN: return UP;
			case LEFT: return RIGHT;
			default: return LEFT;
		}
	}

	// Retorna uma direção aleatória
	public static Direction random() {
		Random generator = new Random();
		return values()[generator.nextInt(values().length)];
	}

	// Retorna a direção correspondente a um vetor
	public static Direction forVector(Vector2D v) {
		for (Direction d : values())
			if (v.equals(d.asVector()))
				return d;
		throw new IllegalArgumentException();	
	}

	// Verifica se a tecla é a de troca de personagem (SPACE)
	public static boolean isSwitch(int k){
		return k == (KeyEvent.VK_SPACE);
	}

	// Verifica se a tecla é a de reiniciar o jogo (R)
	public static boolean isRestart(int k){
		return k == (KeyEvent.VK_R);
	}
}
