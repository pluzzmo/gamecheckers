package it.gianlucacarlesso.checkers.logic;

import java.util.ArrayList;
import java.util.Stack;

import android.graphics.Point;

public class Player {
	public static final int PLAYER_PIECES = 12;
	public static int PLAYER_BLACK = 0;
	public static int PLAYER_WHITE = 1;
	
	public ArrayList<Piece> pieces = new ArrayList<Piece>(PLAYER_PIECES);
	public Stack<Move> moves = new Stack<Move>();
	public int playerType;
	
	
	public Player(int _playerType) {
		playerType = _playerType;
		
		// Black player
		int xpos = 0;
		int ypos = 1;
		boolean alternate = true;
		
		if(playerType == PLAYER_WHITE) {
			// White player
			xpos = Board.NUM_BOX_ROW - Board.PLAYER_ROWS;
			ypos = 0;
			alternate = false;
		}
		
		
		for (int i = 0; i < PLAYER_PIECES; i++) {
			pieces.add(i, new Piece(xpos, ypos, playerType));

			ypos += 2;
			if (ypos >= Board.NUM_BOX_ROW) {
				if (alternate) {
					ypos = 0;
					alternate = false;
				} else {
					ypos = 1;
					alternate = true;
				}

				xpos += 1;
			}
		}
	}
	
	public void moveTo(Point from, Point to, boolean isDama, Piece deleted) {
		moves.add(new Move(from, to, isDama, deleted));
		
		for(int i=0;i<pieces.size();i++){
			if(pieces.get(i).x == from.x && pieces.get(i).y == from.y) {
				pieces.get(i).x = to.x;
				pieces.get(i).y = to.y;
				pieces.get(i).dama = isDama;
			}
		}
	}
	
	public Piece getPiece(int x, int y) {
		Piece piece = null;
		for(int i=0; i<pieces.size();i++) {
			if(pieces.get(i).x == x && pieces.get(i).y == y) {
				piece = pieces.get(i);
				i = pieces.size();
			}
		}
		return piece;
	}
	
	public Piece deletePiece(int x, int y) {
		Piece piece = null;
		for(int i=0; i<pieces.size();i++) {
			if(pieces.get(i).x == x && pieces.get(i).y == y) {
				piece = pieces.remove(i);
				i = pieces.size();
			}
		}	
		return piece;
	}
}
