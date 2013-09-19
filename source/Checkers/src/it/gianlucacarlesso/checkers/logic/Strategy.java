package it.gianlucacarlesso.checkers.logic;

public class Strategy {
	public static int SIMPLE_STRATEGY = 0;
	public static int AVARAGE_STRATEGY = 1;

	// Assesses the number of tokens in the Damiera
	public static double simpleStrategy(Engine engine, int playerTurn,
			boolean isTheEnd) {
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
				values = values + 2;
			} else {
				values = values + 1;
			}
		}

		if (isTheEnd) {
			// Attack if they are in a position of advantage, otherwise run away
			values += sumDistances(player, playerOpposing);
		}

		return values;
	}

	public static double averageStrategy(Engine engine, int playerTurn,
			boolean isTheEnd) {
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
				values = values + 12;
			} else {
				values = values + 1;

				if ((player.pieces.get(i).player == Player.PLAYER_BLACK && player.pieces
						.get(i).x > (Engine.NUM_BOX_ROW / 2) - 1)
						|| player.pieces.get(i).player == Player.PLAYER_WHITE
						&& player.pieces.get(i).x < Engine.NUM_BOX_ROW
								- (Engine.NUM_BOX_ROW / 2)) {
					values = values + 4;
				}
			}
		}

		for (int i = 0; i < playerOpposing.pieces.size(); i++) {
			// Prize the dama
			if (!playerOpposing.pieces.get(i).dama) {
				if ((playerOpposing.pieces.get(i).player == Player.PLAYER_BLACK && playerOpposing.pieces
						.get(i).x > (Engine.NUM_BOX_ROW / 2) - 1)
						|| playerOpposing.pieces.get(i).player == Player.PLAYER_WHITE
						&& playerOpposing.pieces.get(i).x < Engine.NUM_BOX_ROW
								- (Engine.NUM_BOX_ROW / 2)) {
					values = values - 8;
				}
			}
		}

		if (isTheEnd) {
			// Attack if they are in a position of advantage, otherwise run away
			values += sumDistances(player, playerOpposing);
		}

		return values;
	}

	private static int sumDistances(Player player, Player playerOpposing) {
		int values = 0;

		// I only watch dama
		int damaPlayer = 0;
		for (int i = 0; i < player.pieces.size(); i++) {
			if (player.pieces.get(i).dama) {
				damaPlayer++;
			} else {
				damaPlayer += 0.25;
			}
		}

		int damaPlayerOpposing = 0;
		for (int i = 0; i < playerOpposing.pieces.size(); i++) {
			if (playerOpposing.pieces.get(i).dama) {
				damaPlayerOpposing++;
			} else {
				damaPlayerOpposing += 0.25;
			}
		}
		
		if (damaPlayer >= damaPlayerOpposing) {
			// i'm at an advantage
			for (int i = 0; i < player.pieces.size(); i++) {
				if (player.pieces.get(i).dama) {
					Piece piecePlayer = player.pieces.get(i);
					double distance = 0;
					for (int j = 0; j < playerOpposing.pieces.size(); j++) {
						Piece piecePlayerOpposing = playerOpposing.pieces.get(j);
						distance = Math.pow((double)(piecePlayer.x - piecePlayerOpposing.x),2);
						distance += Math.pow((double)(piecePlayer.y - piecePlayerOpposing.y),2);
						distance = Math.pow(distance, 0.5);
						// distance = Math.pow(distance, 10);
						distance = Math.pow(7/distance, 3);
					}
					values += distance;
				}
			}
		} else {
				// i'm at an disadvantage
				for (int i = 0; i < player.pieces.size(); i++) {
					if (player.pieces.get(i).dama) {
						Piece piecePlayer = player.pieces.get(i);
						double distance = 0;
						for (int j = 0; j < playerOpposing.pieces.size(); j++) {
							Piece piecePlayerOpposing = playerOpposing.pieces.get(j);
							distance = Math.pow((double)(piecePlayer.x - piecePlayerOpposing.x),2);
							distance += Math.pow((double)(piecePlayer.y - piecePlayerOpposing.y),2);
							distance = Math.pow(distance, 0.5);
							distance = Math.pow(distance/7, 0.25);
						}
						values += distance;
					}
				}
		}
		
		
		return values;
	}
}
