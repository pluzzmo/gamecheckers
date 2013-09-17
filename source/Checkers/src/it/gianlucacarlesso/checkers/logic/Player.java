package it.gianlucacarlesso.checkers.logic;

import java.util.ArrayList;
import java.util.Stack;

import android.graphics.Point;

public class Player {
	public static final int PLAYER_PIECES = 12;
	public static int PLAYER_BLACK = 0;
	public static int PLAYER_WHITE = 1;

	public static int NORMAL_MOVE = 1;
	public static int NO_MOVE = 2;

	public ArrayList<Piece> pieces = new ArrayList<Piece>(PLAYER_PIECES);
	public Stack<Move> moves = new Stack<Move>();
	public int playerType;

	public Player(int _playerType) {
		playerType = _playerType;

		// Black player
		int xpos = 0;
		int ypos = 1;
		boolean alternate = true;

		if (playerType == PLAYER_WHITE) {
			// White player
			xpos = Engine.NUM_BOX_ROW - Engine.PLAYER_ROWS;
			ypos = 0;
			alternate = false;
		}

		for (int i = 0; i < PLAYER_PIECES; i++) {
			pieces.add(i, new Piece(xpos, ypos, playerType));

			ypos += 2;
			if (ypos >= Engine.NUM_BOX_ROW) {
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

	public boolean moveTo(Player playerOpposing, ArrayList<Move> sequence) {
		boolean isMoved = false;
		for (int i = 0; i < sequence.size(); i++) {
			Move move = sequence.get(i);
			Point from = move.pointFrom;
			Point to = move.pointTo;
			Piece deleted = move.pawnDeleted;

			if (deleted != null) {
				playerOpposing.deletePiece(deleted.x, deleted.y);
			}

			// Check if the pawn can become a dama
			boolean isDama = false;
			if (playerType == Player.PLAYER_BLACK
					&& to.x == Engine.NUM_BOX_ROW - 1) {
				isDama = true;
			} else if (playerType == Player.PLAYER_WHITE && to.x == 0) {
				isDama = true;
			}

			// // Checking if I have to remove a checker of the opponent
			// Piece deleted = null;
			// if (Math.abs(from.x - to.x) > 1) {
			// int dir_x = 1;
			// if (from.x > to.x) {
			// dir_x = -1;
			// }
			//
			// int dir_y = 1;
			// if (from.y > to.y) {
			// dir_y = -1;
			// }
			//
			// deleted = playerOpposing.deletePiece(from.x + dir_x, from.y
			// + dir_y);
			// result = PAWN_ELIMINATED;
			//
			// } else {
			// result = NORMAL_MOVE;
			// }

			moves.add(new Move(from, to, isDama, deleted));

			for (int j = 0; j < pieces.size(); j++) {
				if (pieces.get(j).x == from.x && pieces.get(j).y == from.y) {
					pieces.get(j).x = to.x;
					pieces.get(j).y = to.y;
					if (pieces.get(j).dama == false && isDama == true) {
						pieces.get(j).dama = isDama;
					}
				}
			}

			isMoved = true;
		}

		return isMoved;
	}

	public Piece getPiece(int x, int y) {
		Piece piece = null;
		for (int i = 0; i < pieces.size(); i++) {
			if (pieces.get(i).x == x && pieces.get(i).y == y) {
				piece = pieces.get(i);
				i = pieces.size();
			}
		}
		return piece;
	}

	public Piece deletePiece(int x, int y) {
		Piece piece = null;
		for (int i = 0; i < pieces.size(); i++) {
			if (pieces.get(i).x == x && pieces.get(i).y == y) {
				piece = pieces.remove(i);
				i = pieces.size();
			}
		}
		return piece;
	}

	public void revertMoveTo(Player playerOpposing, ArrayList<Move> sequence) {
		for (int j = sequence.size() - 1; j >= 0; j--) {
			Move move = sequence.get(j);

			// boolean isDama = false;
			// if (move.isDama) {
			// isDama = true;
			// }
			Point from = move.pointTo;
			Point to = move.pointFrom;
			for (int k = 0; k < pieces.size(); k++) {
				if (pieces.get(k).x == from.x && pieces.get(k).y == from.y) {
					pieces.get(k).x = to.x;
					pieces.get(k).y = to.y;
					pieces.get(k).dama = move.isDama;
				}
			}

			if (move.pawnDeleted != null) {
				playerOpposing.pieces.add(move.pawnDeleted);
			}

			if (moves.size() > 0) {
				moves.pop();
			}
		}
	}
}
