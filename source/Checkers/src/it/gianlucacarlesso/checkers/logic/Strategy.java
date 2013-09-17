package it.gianlucacarlesso.checkers.logic;

public class Strategy {
	public static double COINS = 5;

	// Assesses the number of tokens in the Damiera
	public static double simpleStrategy(Engine engine, int playerTurn) {
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

		values = values + player.pieces.size();
		values = values - playerOpposing.pieces.size();
		
		double oldValues = values;

		// Player turn
		for (int i = 0; i < player.pieces.size(); i++) {
			// I push the pieces to become lady giving a bonus to move as close
			// as
			// possible to the extreme opposing
			if (player.pieces.get(i).dama == false) {
				if (playerTurn == Player.PLAYER_BLACK) {
					values = values + Math.log(player.pieces.get(i).x + 1);
				} else {
					values = values
							+ 1
							* (engine.board.length - player.pieces
									.get(i).x);
				}
			}

			// If a pawn has become checkers, I assign a bonus
			if (player.pieces.get(i).dama == true) {
				values = values + 3 * COINS;
			}
		}
		
		values = values + Math.random() * 2;

		return values;
	}

}
