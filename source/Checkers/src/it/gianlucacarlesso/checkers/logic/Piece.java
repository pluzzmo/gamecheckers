package it.gianlucacarlesso.checkers.logic;

import java.util.ArrayList;

import android.graphics.Point;

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

	public ArrayList<Point> possibleMoves(Piece[][] board) {
		ArrayList<Point> moves = new ArrayList<Point>();
		// Control the direction of movement of the counter
		int direction = 1;
		if (player == PLAYER_WHITE) {
			direction = -1;
		}

		// Control the move to the right of the counter
		if (y - 1 >= 0) {
			if (checkBound((x + 1 * direction), y - 1, board.length)
					&& board[x + 1 * direction][y - 1] == null) {
				moves.add(new Point(x + 1 * direction, y - 1));
			} else {
				// If the position in question is occupied by a pawn of the
				// current player, check if I can make a grab at least
				// double
				if (checkBound((x + 2 * direction), y - 2, board.length)
						&& board[x + 2 * direction][y - 2] == null
						&& board[x + 1 * direction][y - 1].player != player) {
					moves.add(new Point(x + 2 * direction, y - 2));
				}
			}
		}

		// Control the move to the left of the counter
		if (board.length - y > 1) {
			if (checkBound(x + 1 * direction, y + 1, board.length)
					&& board[x + 1 * direction][y + 1] == null) {
				moves.add(new Point(x + 1 * direction, y + 1));
			} else {
				if (checkBound(x + 2 * direction, y + 2, board.length)
						&& board[x + 2 * direction][y + 2] == null
						&& board[x + 1 * direction][y + 1].player != player) {
					moves.add(new Point(x + 2 * direction, y + 2));
				}
			}
		}

		return moves;
	}

	private boolean checkBound(int xx, int yy, int lenght) {
		return xx >= 0 && xx <= lenght && yy >= 0 && yy < lenght;
	}
}
