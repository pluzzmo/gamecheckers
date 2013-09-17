package it.gianlucacarlesso.checkers.logic;

import java.util.ArrayList;
import java.util.Stack;

import android.graphics.Point;

public class Engine {
	public static int GAME_MODE_IA_VS_IA = 0;
	public static int GAME_MODE_IA_VS_MAN = 1;
	public static int GAME_MODE_MAN_VS_MAN = 2;
	private static int DEEP_SEARCH = 1;

	public Board board_logic;
	public Player playerBlack;
	public Player playerWhite;
	private Piece specialTurn = null;
	private int playerTurn = Player.PLAYER_WHITE;
	private Piece current_selected = null;
	private int gameMode;

	public Engine(int _gameMode) {
		gameMode = _gameMode;
		board_logic = new Board();
		playerBlack = new Player(Player.PLAYER_BLACK);
		playerWhite = new Player(Player.PLAYER_WHITE);

		// I make a connection between the player's pawns and pawns in
		boardPlayersSync();
	}

	private void boardPlayersSync() {
		// Reset board
		for (int i = 0; i < board_logic.board.length; i++) {
			for (int j = 0; j < board_logic.board.length; j++) {
				board_logic.board[i][j] = null;
			}
		}

		Piece piece = null;
		// Sync Black Player
		for (int i = 0; i < playerBlack.pieces.size(); i++) {
			piece = playerBlack.pieces.get(i);
			board_logic.board[piece.x][piece.y] = piece;
		}

		// Sync White Player
		for (int i = 0; i < playerWhite.pieces.size(); i++) {
			piece = playerWhite.pieces.get(i);
			board_logic.board[piece.x][piece.y] = piece;
		}
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

	public boolean estimateBox(Point point) {
		boolean result = false;
		if (board_logic.board[point.x][point.y] != null) {
			if (board_logic.board[point.x][point.y].player == playerTurn) {
				// I selected a new box from where to make my move
				current_selected = board_logic.board[point.x][point.y];
			} else if(specialTurn != null && specialTurn.x == point.x && specialTurn.y == point.y) {
				current_selected = board_logic.board[point.x][point.y];
			} else {
				current_selected = null;
			}
			result = false;
		} else if (current_selected != null
				&& board_logic.board[point.x][point.y] == null) {
			if(specialTurn != null && specialTurn.player == current_selected.player) {
				// if they are in a special round, I have to reverse the variable that handles the turn
				if(playerTurn == Player.PLAYER_BLACK) {
					playerTurn = Player.PLAYER_WHITE;
				} else {
					playerTurn = Player.PLAYER_BLACK;
				}
			}
			result = executeAction(new Move(new Point(current_selected.x,
					current_selected.y), point));
		}

		return result;
	}

	public boolean executeAction(Move move) {
		ArrayList<Point> moves = getNextPossibleMoves();
		boolean disabled_current_selected = true;
		boolean isMoved = false;

		if (moves != null) {
			// I run the player's move
			for (int k = 0; k < moves.size(); k++) {
				// Checking that the move is valid
				if (moves.get(k).x == move.pointTo.x
						&& moves.get(k).y == move.pointTo.y) {
					Piece oldSpecialTurn = new Piece(current_selected.x,
							current_selected.y, current_selected.player,
							current_selected.dama);

					Player player = null;
					Player playerOpposing = null;
					if (Player.PLAYER_BLACK == playerTurn) {
						player = playerBlack;
						playerOpposing = playerWhite;
					} else {
						player = playerWhite;
						playerOpposing = playerBlack;
					}

					int result = board_logic.moveTo(player, playerOpposing,
							new Point(move.pointFrom.x, move.pointFrom.y),
							new Point(move.pointTo.x, move.pointTo.y),
							playerTurn, specialTurn != null && current_selected.x == specialTurn.x && current_selected.y == specialTurn.y, specialTurn);

					// If the player who moved is that of the
					// current round: shift change, but if I ran
					// a double move with the previous player
					// does not shift change.
					if (result != Board.NO_MOVE
							&& (specialTurn == null || !(specialTurn != null
									&& specialTurn.x == oldSpecialTurn.x && specialTurn.y == oldSpecialTurn.y))) {
						if (playerTurn == Player.PLAYER_BLACK) {
							playerTurn = Player.PLAYER_WHITE;
						} else {
							playerTurn = Player.PLAYER_BLACK;
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

					boardPlayersSync();
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

		if (disabled_current_selected == true) {
			disabledCurrentSelected();
		}

		boardPlayersSync();
		return isMoved;
	}

	private Move moveIA(int current_deep) {
		double maxValue = -1000;
		double newValue = -1000;
		double oldValue = -1000;
		Move move = null;

		if (current_deep < DEEP_SEARCH) {
			ArrayList<Point> moves = null;
			Stack<Move> warehouse = new Stack<Move>();

			Player player = null;
			Player playerOpposing = null;
			if (Player.PLAYER_BLACK == playerTurn) {
				player = playerBlack;
				playerOpposing = playerWhite;
			} else {
				player = playerWhite;
				playerOpposing = playerBlack;
			}

			for (int i = 0; i < player.pieces.size(); i++) {
				Piece current_selectedOld = new Piece(current_selected.x,
						current_selected.y, current_selected.player,
						current_selected.dama);
				current_selected = player.pieces.get(i);
				moves = getNextPossibleMoves();

				for (int k = 0; k < moves.size(); k++) {
					oldValue = valueCurrentBoard(playerTurn);
					executeAction(new Move(new Point(current_selected.x,
							current_selected.y), moves.get(k)));
					warehouse.add(player.moves.peek());
					// moveIA(current_deep + 1, cur);
					newValue = valueCurrentBoard(playerTurn);
					if (newValue > oldValue && newValue > maxValue) {
						maxValue = newValue;
						move = new Move(new Point(current_selected.x,
								current_selected.y), moves.get(k));
					}

					// restore
					current_selected = new Piece(current_selectedOld.x,
							current_selectedOld.y, current_selectedOld.player,
							current_selectedOld.dama);
				}

			}
		}

		return move;
	}

	public void moveIA() {
		Move move = moveIA(0);
		// current_selected = move.piece;

		executeAction(move);
	}

	private double valueCurrentBoard(int player_in_turn) {

		return Strategy.simpleStrategy(this, player_in_turn);
	}

	public void disabledCurrentSelected() {
		current_selected = null;
	}

	public boolean isElementInBoard(Point point) {
		if (board_logic.getBoard()[point.x][point.y] != null) {
			return true;
		}

		return false;
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
