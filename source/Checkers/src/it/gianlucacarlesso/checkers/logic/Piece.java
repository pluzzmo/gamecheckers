package it.gianlucacarlesso.checkers.logic;

public class Piece {
	public static int PLAYER_BLACK = 0;
	public static int PLAYER_WHITE = 1;
	
	public int x;
	public int y;
	public int player;

	public Piece(int _x, int _y) {
		x = _x;
		y = _y;
		player = -1;
	}
	
	public Piece(int _x, int _y, int _player) {
		x = _x;
		y = _y;
		player = _player;
	}
}
