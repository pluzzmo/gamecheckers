package it.gianlucacarlesso.checkers.logic;

public class Strategy {
	public static double COINS = 5;
	
	// Assesses the number of tokens in the Damiera
	public static double simpleStrategy(Engine engine,
			int playerTurn) {
		double values = 0;
		
		Player player = null;
		Player playerOpposing = null;
		if(Player.PLAYER_BLACK == playerTurn) {
			player = engine.playerBlack;
			playerOpposing = engine.playerWhite;
		} else {
			player = engine.playerWhite;
			playerOpposing = engine.playerBlack;
		}
		
		values = values + player.pieces.size();
		values = values - playerOpposing.pieces.size() * 1.25;

		// Player turn
		for(int i = 0; i<player.pieces.size(); i++) {
			// I push the pieces to become lady giving a bonus to move as close as
			// possible to the extreme opposing
			if (player.pieces.get(i).dama == false) {
				if (playerTurn == Player.PLAYER_BLACK) {
					values = values + 0.25 * player.pieces.get(i).x;
				} else {
					values = values + 0.25 * (engine.board_logic.board.length - player.pieces.get(i).x);
				}
			}	
			
			// If a pawn has become checkers, I assign a bonus
			if (player.pieces.get(i).dama == true) {
				values = values + 3 * COINS;
			}
		}
		
		// Player opposing
		for(int i = 0; i<playerOpposing.pieces.size(); i++) {
			// I push the pieces to become lady giving a bonus to move as close as
			// possible to the extreme opposing
			if (playerOpposing.pieces.get(i).dama == false) {
				if (playerTurn == Player.PLAYER_BLACK) {
					values = values - 0.25 * playerOpposing.pieces.get(i).x;
				} else {
					values = values - 0.25 * (engine.board_logic.board.length - playerOpposing.pieces.get(i).x);
				}
			}	
			
			// If a pawn has become checkers, I assign a bonus
			if (playerOpposing.pieces.get(i).dama == true) {
				values = values - 3 * COINS;
			}
		}
		
		return values;
	}

}
