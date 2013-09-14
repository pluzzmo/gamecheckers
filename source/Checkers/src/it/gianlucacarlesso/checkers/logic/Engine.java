package it.gianlucacarlesso.checkers.logic;

import java.util.ArrayList;
import android.graphics.Point;

public class Engine {
	public static int GAME_MODE_IA_VS_IA = 0;
	public static int GAME_MODE_IA_VS_MAN = 1;
	public static int GAME_MODE_MAN_VS_MAN = 2;
	private static int DEEP_SEARCH = 1;

	private Board board_logic = new Board();
	private Piece specialTurn = null;
	private int playerTurn = Piece.PLAYER_WHITE;
	private Piece current_selected = null;
	private int gameMode;

	public Engine(int _gameMode) {
		gameMode = _gameMode;
	}

	public ArrayList<Point> getNextPossibleMoves() {
		if (current_selected != null) {
			return current_selected.possibleMoves(board_logic.getBoard(),
					specialTurn != null && specialTurn.x == current_selected.x
							&& specialTurn.y == current_selected.y);
		} else {
			return new ArrayList<Point>();
		}
	}

	public boolean executeAction(int x, int y) {
		ArrayList<Point> moves = getNextPossibleMoves();
		boolean disabled_current_selected = true;
		boolean isMoved = false;
		if (board_logic.board[x][y] != null) {
			if (board_logic.board[x][y].player == playerTurn
					|| (specialTurn != null
							&& specialTurn.x == board_logic.board[x][y].x && specialTurn.y == board_logic.board[x][y].y)) {
				current_selected = new Piece(x, y,
						board_logic.board[x][y].player,
						board_logic.board[x][y].dama);
				disabled_current_selected = false;
			}
		} else {
			if (moves != null) {
				// I run the player's move
				for (int k = 0; k < moves.size(); k++) {
					// Checking that the move is valid
					if (moves.get(k).x == x && moves.get(k).y == y) {
						Piece oldSpecialTurn = new Piece(current_selected.x,
								current_selected.y, current_selected.player,
								current_selected.dama);
						int result = board_logic.moveTo(current_selected,
								new Point(x, y),
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
								isMoved = true;
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
								&& current_selected.eatOpponentPawn(
										board_logic.getBoard()).size() > 0) {
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
		return isMoved;
	}

	public Point moveIA(int current_deep, Piece cur) {
		int maxValue = -1000;
		int currentValue = -1000;
		Point bestPoint = null;

		if (current_deep < DEEP_SEARCH) {
			Piece[][] board = board_logic.getBoard();
			ArrayList<Point> moves = null;

			// Save the current state
			Board board_logic_tmp = board_logic;
			Piece specialTurn_tmp = specialTurn;
			int playerTurn_tmp = playerTurn;
			Piece current_selected_tmp = current_selected;

			for (int i = 0; i < board.length; i++) {
				for (int j = 0; j < board.length; j++) {
					if (board[i][j] != null && board[i][j].player == playerTurn) {
						current_selected = new Piece(board[i][j].x, board[i][j].y, board[i][j].player, board[i][j].dama);
						moves = getNextPossibleMoves();
						for (int k = 0; k < moves.size(); k++) {
							executeAction(moves.get(k).x, moves.get(k).y);
//							moveIA(current_deep + 1, cur);
							currentValue = valueCurrentBoard();
							
								maxValue = currentValue;
								bestPoint = moves.get(k);
								cur = current_selected;
							
								
								board_logic = board_logic_tmp;
								specialTurn = specialTurn_tmp;
								playerTurn = playerTurn_tmp;
								current_selected = current_selected_tmp;
						}
						board_logic = board_logic_tmp;
						specialTurn = specialTurn_tmp;
						playerTurn = playerTurn_tmp;
						current_selected = current_selected_tmp;
					}
				}
			}

			// Restore correct state
			board_logic = board_logic_tmp;
			specialTurn = specialTurn_tmp;
			playerTurn = playerTurn_tmp;
			current_selected = current_selected_tmp;
		}

		return bestPoint;
	}

	public void moveIA() {
		// Save the current state
		Board board_logic_tmp = board_logic;
		Piece specialTurn_tmp = specialTurn;
		int playerTurn_tmp = playerTurn;
		Piece current_selected_tmp = current_selected;

		Piece cur = new Piece(0, 0);
		Point point = moveIA(0, cur);
		
		// Restore correct state
		board_logic = board_logic_tmp;
		specialTurn = specialTurn_tmp;
		playerTurn = playerTurn_tmp;
		current_selected = current_selected_tmp;
		
		executeAction(point.x, point.y);
	}

	private int valueCurrentBoard() {
		Piece[][] board = board_logic.getBoard();
		
		int sizeBlack = 0;
		int sizeWhite = 0;
		
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board.length; j++) {
				if(board[i][j]!= null) {
					if(board[i][j].player == Piece.PLAYER_BLACK) {
					sizeBlack++;
					} else {
						sizeWhite++;
					}
				}
			}
		}

		return sizeBlack - sizeWhite;
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

	public int getGameMode() {
		return gameMode;
	}

	public int getPlayerTurn() {
		return playerTurn;
	}
}
