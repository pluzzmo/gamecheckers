package it.gianlucacarlesso.checkers.logic;

import java.util.ArrayList;
import java.util.Currency;

import android.graphics.Point;

public class Board {
	public static int NUM_BOX_ROW = 8;
	public static int PLAYER_ROWS = 3;
	public static int PAWN_ELIMINATED = 0;
	public static int NORMAL_MOVE = 1;
	public static int NO_MOVE = 2;

	public Piece[][] board = new Piece[NUM_BOX_ROW][NUM_BOX_ROW];

	public Piece[][] getBoard() {
		return board;
	}

	public int moveTo(Player player, Player playerOpposing, Point from, Point to, int playerInTurn, boolean isSpecialTurn) {
		ArrayList<Point> moves = player.getPiece(from.x, from.y).possibleMoves(board, isSpecialTurn);
		
		int result = NO_MOVE;
		for (int i = 0; i < moves.size(); i++) {
			if (moves.get(i).x == to.x && moves.get(i).y == to.y) {
				
				// Checking if I have to remove a checker of the opponent
				Piece deleted = null;
				if (Math.abs(from.x - to.x) > 1) {
					int dir_x = 1;
					if (from.x > to.x) {
						dir_x = -1;
					}

					int dir_y = 1;
					if (from.y > to.y) {
						dir_y = -1;
					}

					deleted = playerOpposing.deletePiece(from.x + dir_x, from.y + dir_y);
					result = PAWN_ELIMINATED;

				} else {
					result = NORMAL_MOVE;
				}

				boolean isDama = player.getPiece(from.x, from.y).dama;
				// The pawn has become a lady
				if (player.playerType == Player.PLAYER_BLACK
						&& to.x == board.length - 1) {
					isDama = true;
				} else if (player.playerType == Player.PLAYER_WHITE && to.x == 0) {
					isDama = true;
				}
				
				// Registry the movement to the player in turn
				player.moveTo(from, to, isDama, deleted);
				
				// executed movement, I interrupt the cycle
				i = moves.size();
			}
		}

		return result;
	}
}
