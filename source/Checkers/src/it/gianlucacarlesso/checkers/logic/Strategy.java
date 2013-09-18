package it.gianlucacarlesso.checkers.logic;

public class Strategy {
	public static int SIMPLE_STRATEGY = 0;
	public static int AVARAGE_STRATEGY = 1;

	// Assesses the number of tokens in the Damiera
	public static double simpleStrategy(Engine engine, int playerTurn) {
		double values = 0;

		Player player = null;
		if (Player.PLAYER_BLACK == playerTurn) {
			player = engine.playerBlack;
		} else {
			player = engine.playerWhite;
		}

		for (int i = 0; i < player.pieces.size(); i++) {
			// Prize the dama
			if (player.pieces.get(i).dama) {
				values = values + 2;
			} else {
				values = values + 1;
			}
		}
		return values;
	}
	
	public static double avarageStrategy(Engine engine, int playerTurn) {
		double values = 0;

		Player player = null;
		Player playerOpposing = null;
		if (Player.PLAYER_BLACK == playerTurn) {
			player = engine.playerBlack;
			playerOpposing = engine.playerWhite;
		} else {
			player = engine.playerWhite;
			playerOpposing = engine.playerBlack;
		}

		for (int i = 0; i < player.pieces.size(); i++) {
			// Prize the dama
			if (player.pieces.get(i).dama) {
				values = values + 10;
			} else {
				values = values + 1;
				
				if((player.pieces.get(i).player == Player.PLAYER_BLACK && player.pieces.get(i).x > (Engine.NUM_BOX_ROW / 2) - 1) || player.pieces.get(i).player == Player.PLAYER_WHITE && player.pieces.get(i).x < Engine.NUM_BOX_ROW - (Engine.NUM_BOX_ROW / 2)) {
					values = values + 5;
				}
			}
		}
		
		for (int i = 0; i < playerOpposing.pieces.size(); i++) {
			// Prize the dama
			if (!playerOpposing.pieces.get(i).dama) {
				if((playerOpposing.pieces.get(i).player == Player.PLAYER_BLACK && playerOpposing.pieces.get(i).x > (Engine.NUM_BOX_ROW / 2) - 1) || playerOpposing.pieces.get(i).player == Player.PLAYER_WHITE && playerOpposing.pieces.get(i).x < Engine.NUM_BOX_ROW - (Engine.NUM_BOX_ROW / 2)) {
					values = values - 7;
				}
			}
		}

		// values = values + Math.random() * 2;

		return values;
	}

}
