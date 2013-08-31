package it.gianlucacarlesso.checkers.logic;

public class Board {
	public static final int PLAYER_PIECES = 12;
	public static int NUM_BOX_ROW = 8;
	public static int PLAYER_ROWS = 3;

	private Piece[] player_black = new Piece[PLAYER_PIECES];
	private Piece[] player_white = new Piece[PLAYER_PIECES];

	public Piece[][] board = new Piece[NUM_BOX_ROW][NUM_BOX_ROW];

	public Board() {
		// I make a connection between the player's pawns and pawns in
		// Damiera
		initBoard(0, 1, Piece.PLAYER_BLACK, true, player_black);
		initBoard(Board.NUM_BOX_ROW - Board.PLAYER_ROWS, 0, Piece.PLAYER_WHITE,
				false, player_white);
	}

	public Piece[] getPlayerBlack() {
		return player_black;
	}

	public Piece[] getPlayerWhite() {
		return player_white;
	}
	
	public Piece[][] getBoard() {
		return board;
	}

	private void initBoard(int xpos, int ypos, int player, boolean alternate,
			Piece[] player_pieces) {
		for (int i = 0; i < PLAYER_PIECES; i++) {
			player_pieces[i] = new Piece(xpos, ypos, player);
			board[xpos][ypos] = player_pieces[i];

			ypos += 2;
			if (ypos >= Board.NUM_BOX_ROW) {
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
}
