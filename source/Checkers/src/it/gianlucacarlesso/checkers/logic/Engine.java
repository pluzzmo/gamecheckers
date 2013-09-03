package it.gianlucacarlesso.checkers.logic;

import java.util.ArrayList;
import android.content.Context;
import android.graphics.Point;

public class Engine {
	private Board board_logic = new Board();
	private Piece specialTurn = null;
	private int playerTurn = Piece.PLAYER_WHITE;
	private Piece current_selected = null;
	
	public Engine() {
		
	}
	
	public ArrayList<Point> getNextPossibleMoves() {
		if(current_selected!= null) {
		return current_selected.possibleMoves(board_logic.getBoard(),
		specialTurn != null && specialTurn.x == current_selected.x
				&& specialTurn.y == current_selected.y);
		} else {
			return new ArrayList<Point>();
		}
	}
	
	public void executeAction(int x, int y, Context context) {
		ArrayList<Point> moves = getNextPossibleMoves();
		boolean disabled_current_selected = true;
		if (board_logic.board[x][y] != null) {
			if (board_logic.board[x][y].player == playerTurn
					|| (specialTurn != null
							&& specialTurn.x == board_logic.board[x][y].x && specialTurn.y == board_logic.board[x][y].y)) {
				current_selected = new Piece(x, y,
						board_logic.board[x][y].player, board_logic.board[x][y].dama);
				disabled_current_selected = false;
			}
		} else {
			if (moves != null) {
				// I run the player's move
				for (int k = 0; k < moves.size(); k++) {
					// Checking that the move is valid
					if (moves.get(k).x == x && moves.get(k).y == y) {
						Piece oldSpecialTurn = new Piece(
								current_selected.x,
								current_selected.y, current_selected.player, current_selected.dama);
						int result = board_logic.moveTo(
								current_selected, new Point(x, y),
								context,
								current_selected == specialTurn);

						// If the player who moved is that of the
						// current round: shift change, but if I ran
						// a double move with the previous player
						// does not shift change.
						if (result != Board.NO_MOVE
								&& (specialTurn == null || !(specialTurn != null
										&& specialTurn.x == oldSpecialTurn.x && specialTurn.y == oldSpecialTurn.y))) {
							if (playerTurn == Piece.PLAYER_BLACK) {
								playerTurn = Piece.PLAYER_WHITE;
							} else {
								playerTurn = Piece.PLAYER_BLACK;
							}
							disabled_current_selected = true;
						} else {
							if (specialTurn != null
									&& specialTurn.x == oldSpecialTurn.x
									&& specialTurn.y == oldSpecialTurn.y) {
								disabled_current_selected = true;
							} else {
								disabled_current_selected = false;
							}
						}

						if (result == Board.PAWN_ELIMINATED
								&& current_selected
										.eatOpponentPawn(
												board_logic
														.getBoard())
										.size() > 0) {
							specialTurn = current_selected;
						} else {
							specialTurn = null;
						}

						k = moves.size();
					}
				}
			}
		}
		
		if (disabled_current_selected == true) {
			disabledCurrentSelected();
		}		
	}
	
	public void disabledCurrentSelected() {
		current_selected = null;
	}
	
	public Piece[][] getBoardLogic() {
		return board_logic.board;
	}
	
	public Piece getCurrentSelected() {
		return current_selected;
	}
}
