package it.gianlucacarlesso.checkers.logic;

import java.util.ArrayList;
import java.util.Stack;

import android.graphics.Point;
import android.util.Log;

public class Engine {
	public static int GAME_MODE_IA_VS_IA = 0;
	public static int GAME_MODE_IA_VS_MAN = 1;
	public static int GAME_MODE_MAN_VS_MAN = 2;
	public static int PLAYERS_PAR = -2;
	public static int NO_WINNER = -1;
	private static int DEEP_SEARCH = 1;
	private static int MAX_MOVES_NO_MOVES = 15;
	public int moves_no_moves = 0;

	public static int NUM_BOX_ROW = 8;
	public static int PLAYER_ROWS = 3;
	public Piece[][] board = new Piece[NUM_BOX_ROW][NUM_BOX_ROW];
	public Player playerBlack;
	public Player playerWhite;
	private int playerTurn = Player.PLAYER_WHITE;
	private Piece current_selected = null;
	private int gameMode;

	public Engine(int _gameMode) {
		gameMode = _gameMode;
		playerBlack = new Player(Player.PLAYER_BLACK);
		playerWhite = new Player(Player.PLAYER_WHITE);

		// I make a connection between the player's pawns and pawns in
		boardPlayersSync();
	}

	private void boardPlayersSync() {
		// Reset board
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board.length; j++) {
				board[i][j] = null;
			}
		}

		Piece piece = null;
		// Sync Black Player
		for (int i = 0; i < playerBlack.pieces.size(); i++) {
			piece = playerBlack.pieces.get(i);
			board[piece.x][piece.y] = piece;
		}

		// Sync White Player
		for (int i = 0; i < playerWhite.pieces.size(); i++) {
			piece = playerWhite.pieces.get(i);
			board[piece.x][piece.y] = piece;
		}
	}

	public ArrayList<ArrayList<Move>> getNextPossibleMoves() {
		boardPlayersSync();
		if (current_selected != null) {
			return current_selected.possibleMoves(board);
		} else {
			return new ArrayList<ArrayList<Move>>();
		}
	}

	public boolean estimateBox(Point point) {
		boolean result = false;
		if (board[point.x][point.y] != null) {
			if (board[point.x][point.y].player == playerTurn) {
				// I selected a new box from where to make my move
				current_selected = board[point.x][point.y];
			} else {
				current_selected = null;
			}
			result = false;
		} else if (current_selected != null && board[point.x][point.y] == null) {
			ArrayList<ArrayList<Move>> moves = getNextPossibleMoves();
			ArrayList<Move> sequence = null;
			for (int i = 0; i < moves.size(); i++) {
				sequence = moves.get(i);
				Point first = sequence.get(0).pointFrom;
				Point last = sequence.get(sequence.size() - 1).pointTo;
				if (first.x == current_selected.x
						&& first.y == current_selected.y && last.x == point.x
						&& last.y == point.y) {
					i = moves.size();
				}
			}
			result = executeAction(sequence);
		}

		return result;
	}

	public boolean executeAction(ArrayList<Move> sequence) {
		int isMoved = Player.NO_MOVE;
		// I run the player's move
		Player player = null;
		Player playerOpposing = null;
		if (Player.PLAYER_BLACK == playerTurn) {
			player = playerBlack;
			playerOpposing = playerWhite;
		} else {
			player = playerWhite;
			playerOpposing = playerBlack;
		}

		isMoved = player.moveTo(playerOpposing, sequence);

		boolean res = false;
		if (isMoved != Player.NO_MOVE) {
			res = true;
			if (playerTurn == Player.PLAYER_BLACK) {
				playerTurn = Player.PLAYER_WHITE;
			} else {
				playerTurn = Player.PLAYER_BLACK;
			}
			current_selected = null;

			if (isMoved == Player.DELETED_MOVE) {
				moves_no_moves = 0;
			} else {
				moves_no_moves++;
			}
		}

		boardPlayersSync();
		return res;
	}

	private ArrayList<Move> moveIA(int current_deep) {
		double maxValue = -100000;
		double newValue = -100000;
		double oldValue = -100000;
		ArrayList<Move> sequence = null;
		ArrayList<Move> sequenceTmp = null;

		if (current_deep < DEEP_SEARCH) {
			ArrayList<ArrayList<Move>> moves = null;
			Stack<ArrayList<Move>> warehouse = new Stack<ArrayList<Move>>();

			Player player = null;
			Player playerOpposing = null;
			if (Player.PLAYER_BLACK == playerTurn) {
				player = playerBlack;
				playerOpposing = playerWhite;
			} else {
				player = playerWhite;
				playerOpposing = playerBlack;
			}

			int oldPlayerTurn = playerTurn;
			for (int i = 0; i < player.pieces.size(); i++) {
				current_selected = player.pieces.get(i);
				int currentPlayerTurn = oldPlayerTurn;
				moves = getNextPossibleMoves();

				for (int k = 0; k < moves.size(); k++) {
					oldValue = valueCurrentBoard(playerTurn);

					sequenceTmp = moves.get(k);
					executeAction(sequenceTmp);
					playerTurn = oldPlayerTurn;
					warehouse.add(sequenceTmp);
					// moveIA(current_deep + 1, cur);
					newValue = valueCurrentBoard(playerTurn);
					if (newValue >= oldValue && newValue > maxValue) {
						maxValue = newValue;
						sequence = sequenceTmp;
					}

					for (int j = 0; j < warehouse.size(); j++) {
						player.revertMoveTo(playerOpposing, warehouse.pop());
					}
					playerTurn = currentPlayerTurn;
					boardPlayersSync();
				}

			}
			playerTurn = oldPlayerTurn;
		}
		return sequence;
	}

	public void moveIA() {
		int move_no_moves_dump = moves_no_moves;
		ArrayList<Move> sequence = moveIA(0);
		moves_no_moves = move_no_moves_dump;
		// current_selected = move.piece;

		current_selected = board[sequence.get(0).pointFrom.x][sequence.get(0).pointFrom.y];
		executeAction(sequence);
	}

	private double valueCurrentBoard(int player_in_turn) {

		return Strategy.simpleStrategy(this, player_in_turn);
	}

	public boolean isElementInBoard(Point point) {
		if (board[point.x][point.y] != null) {
			return true;
		}

		return false;
	}

	public Piece[][] getBoardLogic() {
		return board;
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

	public int thereIsWinner() {
		int winner = NO_WINNER;
		if (playerBlack.pieces.size() == 0) {
			winner = Player.PLAYER_WHITE;
		}

		if (playerWhite.pieces.size() == 0) {
			winner = Player.PLAYER_BLACK;
		}

		if (moves_no_moves > MAX_MOVES_NO_MOVES) {
			winner = PLAYERS_PAR;
		}
		return winner;
	}
}
