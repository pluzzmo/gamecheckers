package it.gianlucacarlesso.checkers.logic;

import java.util.ArrayList;

import android.graphics.Point;

public class Piece {
	public int x;
	public int y;
	public int player;
	public boolean dama = false;

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

	public Piece(int _x, int _y, int _player, boolean _dama) {
		x = _x;
		y = _y;
		player = _player;
		dama = _dama;
	}

	public ArrayList<Point> possibleMoves(Piece[][] board, boolean isSpecialTurn) {
		ArrayList<Point> moves = new ArrayList<Point>();
		// Control the direction of movement of the counter
		int direction = 1;
		if (player == Player.PLAYER_WHITE) {
			direction = -1;
		}

		// Control the move to the right of the counter
		checkRight(board, direction, moves, isSpecialTurn);

		// Control the move to the left of the counter
		checkLeft(board, direction, moves, isSpecialTurn);

		if (dama) {
			// The Dama can move in all four directions
			direction = -direction;
			checkRight(board, direction, moves, isSpecialTurn);
			checkLeft(board, direction, moves, isSpecialTurn);
		}

		return moves;
	}

	public ArrayList<Point> eatOpponentPawn(Piece[][] board) {
		ArrayList<Point> moves = new ArrayList<Point>();
		// Control the direction of movement of the counter
		int direction = 1;
		if (player == Player.PLAYER_WHITE) {
			direction = -1;
		}

		// Control the move to the right of the counter
		if (y - 1 >= 0 && checkBound((x + 2 * direction), y - 2, board.length)
				&& board[x + 2 * direction][y - 2] == null
				&& board[x + 1 * direction][y - 1] != null
				&& board[x + 1 * direction][y - 1].player != player) {
			// A pawn can not eat a Dama
			if (!(!dama && board[x + 1 * direction][y - 1].dama)) {
				moves.add(new Point(x + 2 * direction, y - 2));
			}
		}

		// Control the move to the left of the counter
		if (board.length - y > 1
				&& checkBound(x + 2 * direction, y + 2, board.length)
				&& board[x + 2 * direction][y + 2] == null
				&& board[x + 1 * direction][y + 1] != null
				&& board[x + 1 * direction][y + 1].player != player) {
			// A pawn can not eat a Dama
			if (!(!dama && board[x + 1 * direction][y + 1].dama)) {
				moves.add(new Point(x + 2 * direction, y + 2));
			}
		}

		return moves;
	}

	private boolean checkBound(int xx, int yy, int lenght) {
		return xx >= 0 && xx < lenght && yy >= 0 && yy < lenght;
	}

	private void checkRight(Piece[][] board, int direction,
			ArrayList<Point> moves, boolean isSpecialTurn) {
		if (y - 1 >= 0) {
			if (checkBound((x + 1 * direction), y - 1, board.length)
					&& board[x + 1 * direction][y - 1] == null) {
				// If it is a special round this can not I run, I can only eat
				// the opponent's checkers
				if (!isSpecialTurn) {
					moves.add(new Point(x + 1 * direction, y - 1));
				}
			} else {
				// If the position in question is occupied by a pawn of the
				// current player, check if I can make a grab at least
				// double
				if (checkBound((x + 2 * direction), y - 2, board.length)
						&& board[x + 2 * direction][y - 2] == null
						&& board[x + 1 * direction][y - 1].player != player) {
					// A pawn can not eat a Dama
					if (!(!dama && board[x + 1 * direction][y - 1].dama)) {
						moves.add(new Point(x + 2 * direction, y - 2));
					}
				}
			}
		}
	}

	private void checkLeft(Piece[][] board, int direction,
			ArrayList<Point> moves, boolean isSpecialTurn) {
		if (board.length - y > 1) {
			if (checkBound(x + 1 * direction, y + 1, board.length)
					&& board[x + 1 * direction][y + 1] == null) {
				if (!isSpecialTurn) {
					moves.add(new Point(x + 1 * direction, y + 1));
				}
			} else {
				if (checkBound(x + 2 * direction, y + 2, board.length)
						&& board[x + 2 * direction][y + 2] == null
						&& board[x + 1 * direction][y + 1].player != player) {
					// A pawn can not eat a Dama
					if (!(!dama && board[x + 1 * direction][y + 1].dama)) {
						moves.add(new Point(x + 2 * direction, y + 2));
					}
				}
			}
		}
	}
}
