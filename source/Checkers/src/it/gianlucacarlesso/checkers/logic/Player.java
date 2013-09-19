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
	public static int DELETED_MOVE = 3;

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
			pieces.add(i, new Piece(xpos, ypos, playerType, false));

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

	public int moveTo(Player playerOpposing, ArrayList<Move> sequence) {
		int isMoved = NO_MOVE;
		for (int i = 0; i < sequence.size(); i++) {
			Move move = sequence.get(i);
			Point from = move.pointFrom;
			Point to = move.pointTo;
			Piece deleted = move.pawnDeleted;

			if (deleted != null) {
				playerOpposing.deletePiece(deleted.x, deleted.y);
				isMoved = DELETED_MOVE;
			} else {
				isMoved = NORMAL_MOVE;
			}

			Piece piece = getPiece(from.x, from.y);
			boolean isDama = piece.dama;
			// Check if the pawn can become a dama
			if (piece.dama == false && playerType == Player.PLAYER_BLACK
					&& to.x == Engine.NUM_BOX_ROW - 1) {
				isDama = true;
			} else if (piece.dama == false && playerType == Player.PLAYER_WHITE
					&& to.x == 0) {
				isDama = true;
			}

			moves.add(new Move(from, to, isDama, deleted));

			piece.x = to.x;
			piece.y = to.y;
			piece.dama = isDama;
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

			Point from = move.pointTo;
			Point to = move.pointFrom;

			Piece piece = getPiece(from.x, from.y);
			piece.x = to.x;
			piece.y = to.y;
			piece.dama = move.isDama;

			if (move.pawnDeleted != null) {
				playerOpposing.pieces.add(move.pawnDeleted);
			}

			if (moves.size() > 0) {
				moves.pop();
			}
		}
	}
}
