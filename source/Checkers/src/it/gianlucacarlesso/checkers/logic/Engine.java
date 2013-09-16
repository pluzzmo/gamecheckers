package it.gianlucacarlesso.checkers.logic;

import java.util.ArrayList;

import android.graphics.Point;
import android.util.Log;

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

	private Move moveIA(int current_deep) {
		int maxValue = -1000;
		int currentValue = -1000;
		Move move = null;

		if (current_deep < DEEP_SEARCH) {
			Piece[][] board = board_logic.getBoard();
			ArrayList<Point> moves = null;

			// Save the current state
			Board board_logic_tmp = null;
			if (board_logic != null) {
				board_logic_tmp = board_logic.clone();
			}

			Piece specialTurn_tmp = null;
			if (specialTurn != null) {
				specialTurn_tmp = specialTurn.clone();
			}

			int playerTurn_tmp = playerTurn;

			Piece current_selected_tmp = null;
			if (current_selected != null) {
				current_selected_tmp = current_selected.clone();
			}

			for (int i = 0; i < board.length; i++) {
				for (int j = 0; j < board.length; j++) {
					if (board[i][j] != null && board[i][j].player == playerTurn) {
						current_selected = new Piece(board[i][j].x,
								board[i][j].y, board[i][j].player,
								board[i][j].dama);
						Piece current_selected_tmp1 = current_selected.clone();
						moves = getNextPossibleMoves();
						for (int k = 0; k < moves.size(); k++) {
							executeAction(moves.get(k).x, moves.get(k).y);
							// moveIA(current_deep + 1, cur);
							currentValue = valueCurrentBoard();
							if (currentValue > maxValue) {
								int x = 1;
								maxValue = currentValue;
								move = new Move(moves.get(k),
										current_selected_tmp1.clone());
							}

							// Restore correct state
							board_logic = null;
							if (board_logic_tmp != null) {
								board_logic = board_logic_tmp.clone();
							}

							specialTurn = null;
							if (specialTurn_tmp != null) {
								specialTurn = specialTurn_tmp.clone();
							}

							playerTurn = playerTurn_tmp;

							current_selected = null;
							if (current_selected_tmp1 != null) {
								current_selected = current_selected_tmp1
										.clone();
							}

						}
						// Restore correct state
						board_logic = null;
						if (board_logic_tmp != null) {
							board_logic = board_logic_tmp.clone();
						}

						specialTurn = null;
						if (specialTurn_tmp != null) {
							specialTurn = specialTurn_tmp.clone();
						}

						playerTurn = playerTurn_tmp;

						current_selected = null;
						if (current_selected_tmp1 != null) {
							current_selected = current_selected_tmp1.clone();
						}
					}
				}
			}

			// Restore correct state
			board_logic = null;
			if (board_logic_tmp != null) {
				board_logic = board_logic_tmp.clone();
			}

			specialTurn = null;
			if (specialTurn_tmp != null) {
				specialTurn = specialTurn_tmp.clone();
			}

			playerTurn = playerTurn_tmp;

			current_selected = null;
			if (current_selected_tmp != null) {
				current_selected = current_selected_tmp.clone();
			}

		}

		return move;
	}

	public void moveIA() {
		// Save the current state
		Board board_logic_tmp = null;
		if (board_logic != null) {
			board_logic_tmp = board_logic.clone();
		}

		Piece specialTurn_tmp = null;
		if (specialTurn != null) {
			specialTurn_tmp = specialTurn.clone();
		}

		int playerTurn_tmp = playerTurn;

		Piece current_selected_tmp = null;
		if (current_selected != null) {
			current_selected_tmp = current_selected.clone();
		}

		Move move = moveIA(0);

		// Restore correct state
		board_logic = null;
		if (board_logic_tmp != null) {
			board_logic = board_logic_tmp.clone();
		}

		specialTurn = null;
		if (specialTurn_tmp != null) {
			specialTurn = specialTurn_tmp.clone();
		}

		playerTurn = playerTurn_tmp;

		current_selected = null;
		if (current_selected_tmp != null) {
			current_selected = current_selected_tmp.clone();
		}

		current_selected = move.piece;

		executeAction(move.point.x, move.point.y);
	}

	private int valueCurrentBoard() {
		Piece[][] board = board_logic.getBoard();

		int sizeBlack = 0;
		int sizeWhite = 0;

		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board.length; j++) {
				if (board[i][j] != null) {
					if (board[i][j].player == Piece.PLAYER_BLACK) {
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

	private class Move {
		public Point point;
		public Piece piece;

		public Move(Point _point, Piece _piece) {
			point = _point;
			piece = _piece.clone();
		}
	}
}
