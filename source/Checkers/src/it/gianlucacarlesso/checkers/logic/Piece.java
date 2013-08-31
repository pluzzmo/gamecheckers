package it.gianlucacarlesso.checkers.logic;

import java.util.ArrayList;

import android.content.Context;
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

	public ArrayList<Point> possibleMoves(Piece[][] _board, Context context) {
		ArrayList<Point> moves = new ArrayList<Point>();
		// Control the direction of movement of the counter
		int direction = 1;
		if (player == PLAYER_WHITE) {
			direction = -1;
		}
		
		// Control the move to the right of the counter
		if (y - 1 >= 0) {
			if (x + 1 * direction >= 0 && _board[x + 1 * direction][y - 1] == null) {
				moves.add(new Point(x + 1 * direction, y - 1));
			} else {
				// If the position in question is occupied by a pawn of the
				// current player, check if I can make a grab at least
				// double
				if (x + 1 * direction >= 0 && _board[x + 1 * direction][y - 1].player == player) {
					(new Piece(x + 1 * direction, y - 1, player))
							.possibleMoves(_board, context);
				}
			}
		}

		// Control the move to the left of the counter
		if (_board.length - y > 1) {
			if (x + 1 * direction >= 0 && _board[x + 1 * direction][y + 1] == null) {
				moves.add(new Point(x + 1 * direction, y + 1));
			} else {
				if (x + 1 * direction >= 0 && _board[x + 1 * direction][y + 1].player == player) {
					(new Piece(x + 1 * direction, y + 1, player))
							.possibleMoves(_board, context);
				}
			}
		}
		
		return moves;
	}
}
