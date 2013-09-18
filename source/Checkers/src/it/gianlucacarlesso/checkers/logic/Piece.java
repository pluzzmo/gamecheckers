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

	public ArrayList<ArrayList<Move>> possibleMoves(Piece[][] board) {
		ArrayList<ArrayList<Move>> moves = new ArrayList<ArrayList<Move>>();
		// Control the direction of movement of the counter
		int direction = 1;
		if (player == Player.PLAYER_WHITE) {
			direction = -1;
		}

		// Control the move to the right of the counter
		checkRight(board, direction, moves);

		// Control the move to the left of the counter
		checkLeft(board, direction, moves);

		if (dama) {
			// The Dama can move in all four directions
			direction = -direction;
			checkRight(board, direction, moves);
			checkLeft(board, direction, moves);
		}

		return moves;
	}

	private void eatOpponentPawn(Piece[][] board,
			ArrayList<ArrayList<Move>> moves, ArrayList<Move> sequence) {
		int direction = 1;
		if (player == Player.PLAYER_WHITE) {
			direction = -1;
		}

		Point current = sequence.get(sequence.size() - 1).pointTo;

		// Control the move to the right of the counter
		if (current.y - 1 >= 0
				&& checkBound((current.x + 2 * direction), current.y - 2,
						board.length)
				&& board[current.x + 2 * direction][current.y - 2] == null
				&& board[current.x + 1 * direction][current.y - 1] != null
				&& board[current.x + 1 * direction][current.y - 1].player != player) {
			// A pawn can not eat a Dama
			if (!(!dama && board[current.x + 1 * direction][current.y - 1].dama)) {
				ArrayList<Move> sequenceBuild = new ArrayList<Move>();
				for (int i = 0; i < sequence.size(); i++) {
					sequenceBuild.add(sequence.get(i));
				}

				sequenceBuild.add(new Move(current, new Point(current.x + 2
						* direction, current.y - 2), dama, board[current.x + 1
						* direction][current.y - 1]));

				moves.add(sequenceBuild);
				eatOpponentPawn(board, moves, sequenceBuild);
			}
		}

		// Control the move to the left of the counter
		if (board.length - current.y > 1
				&& checkBound(current.x + 2 * direction, current.y + 2,
						board.length)
				&& board[current.x + 2 * direction][current.y + 2] == null
				&& board[current.x + 1 * direction][current.y + 1] != null
				&& board[current.x + 1 * direction][current.y + 1].player != player) {
			// A pawn can not eat a Dama
			if (!(!dama && board[current.x + 1 * direction][current.y + 1].dama)) {
				ArrayList<Move> sequenceBuild = new ArrayList<Move>();
				for (int i = 0; i < sequence.size(); i++) {
					sequenceBuild.add(sequence.get(i));
				}

				sequenceBuild.add(new Move(current, new Point(current.x + 2
						* direction, current.y + 2), dama, board[current.x + 1
						* direction][current.y + 1]));

				moves.add(sequenceBuild);
				eatOpponentPawn(board, moves, sequenceBuild);
			}
		}
	}

	private boolean checkBound(int xx, int yy, int lenght) {
		return xx >= 0 && xx < lenght && yy >= 0 && yy < lenght;
	}

	private void checkRight(Piece[][] board, int direction,
			ArrayList<ArrayList<Move>> moves) {
		if (y - 1 >= 0) {
			if (checkBound((x + 1 * direction), y - 1, board.length)
					&& board[x + 1 * direction][y - 1] == null) {
				ArrayList<Move> sequence = new ArrayList<Move>();
				sequence.add(new Move(new Point(x, y), new Point(x + 1
						* direction, y - 1), dama, null));
				moves.add(sequence);
			} else {
				// If the position in question is occupied by a pawn of the
				// current player, check if I can make a grab at least
				// double
				if (checkBound((x + 2 * direction), y - 2, board.length)
						&& board[x + 2 * direction][y - 2] == null
						&& board[x + 1 * direction][y - 1].player != player) {
					// A pawn can not eat a Dama
					if (!(!dama && board[x + 1 * direction][y - 1].dama)) {
						ArrayList<Move> sequence = new ArrayList<Move>();
						sequence.add(new Move(new Point(x, y), new Point(x + 2
								* direction, y - 2), dama, new Piece(x + 1
								* direction, y - 1,
								board[x + 1 * direction][y - 1].player)));
						moves.add(sequence);
						eatOpponentPawn(board, moves, sequence);
					}
				}
			}
		}
	}

	private void checkLeft(Piece[][] board, int direction,
			ArrayList<ArrayList<Move>> moves) {
		if (board.length - y > 1) {
			if (checkBound(x + 1 * direction, y + 1, board.length)
					&& board[x + 1 * direction][y + 1] == null) {
				ArrayList<Move> sequence = new ArrayList<Move>();
				sequence.add(new Move(new Point(x, y), new Point(x + 1
						* direction, y + 1), dama, null));
				moves.add(sequence);
			} else {
				if (checkBound(x + 2 * direction, y + 2, board.length)
						&& board[x + 2 * direction][y + 2] == null
						&& board[x + 1 * direction][y + 1].player != player) {
					// A pawn can not eat a Dama
					if (!(!dama && board[x + 1 * direction][y + 1].dama)) {
						ArrayList<Move> sequence = new ArrayList<Move>();
						sequence.add(new Move(new Point(x, y), new Point(x + 2
								* direction, y + 2), dama, new Piece(x + 1
								* direction, y + 1,
								board[x + 1 * direction][y + 1].player)));
						moves.add(sequence);
						eatOpponentPawn(board, moves, sequence);
					}
				}
			}
		}
	}
}
