package it.gianlucacarlesso.checkers.view;

import it.gianlucacarlesso.checkers.R;
import it.gianlucacarlesso.checkers.logic.Board;
import it.gianlucacarlesso.checkers.logic.Piece;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class GPiece {
	private static Bitmap piece_black;
	private static Bitmap piece_black_selected;
	private static Bitmap piece_black_possible;
	private static Bitmap piece_white;
	private static Bitmap piece_white_selected;
	private static Bitmap piece_white_possible;
	
	private Context context;
	private Piece piece;

	public GPiece(Piece _piece, Context _context) {
		piece = _piece;
		context = _context;
		
		// Load pieces images
		Resources res = context.getResources();
		piece_black = BitmapFactory.decodeResource(res,
				R.drawable.piece_black_horizontal);
		piece_black_selected = BitmapFactory.decodeResource(res,
				R.drawable.piece_black_horizontal_selected);
		piece_black_possible = BitmapFactory.decodeResource(res,
				R.drawable.piece_black_horizontal_possible);
		piece_white = BitmapFactory.decodeResource(res,
				R.drawable.piece_white_horizontal);
		piece_white_selected = BitmapFactory.decodeResource(res,
				R.drawable.piece_white_horizontal_selected);
		piece_white_possible = BitmapFactory.decodeResource(res,
				R.drawable.piece_white_horizontal_possible);
	}

	public Piece getPiece() {
		return piece;
	}

	public Bitmap getImagePiece() {
		Bitmap image = piece_black;
		if (piece.player == Piece.PLAYER_WHITE) {
			image = piece_white;
		}
		return image;
	}

	public Bitmap getImagePieceSelected() {
		Bitmap image = piece_black_selected;
		if (piece.player == Piece.PLAYER_WHITE) {
			image = piece_white_selected;
		}
		return image;
	}

	public Bitmap getImagePiecePossible() {
		Bitmap image = piece_black_possible;
		if (piece.player == Piece.PLAYER_WHITE) {
			image = piece_white_possible;
		}
		return image;
	}
	
	public void rescaleImage(float box_size_x, int board_width) {
		float new_x_piece = (int) (board_width / (Board.NUM_BOX_ROW + 1.3));
		if (new_x_piece >= box_size_x) {
			new_x_piece = box_size_x - 5;
		}
		
		float new_y_piece = (int) (1.0 * piece_black.getHeight()
				/ piece_black.getWidth() * new_x_piece);

		piece_black = Bitmap.createScaledBitmap(piece_black, (int) new_x_piece,
				(int) new_y_piece, false);
		piece_black_selected = Bitmap.createScaledBitmap(piece_black_selected,
				(int) new_x_piece, (int) new_y_piece, false);
		piece_black_possible = Bitmap.createScaledBitmap(piece_black_possible,
				(int) new_x_piece, (int) new_y_piece, false);
		piece_white = Bitmap.createScaledBitmap(piece_white, (int) new_x_piece,
				(int) new_y_piece, false);
		piece_white_selected = Bitmap.createScaledBitmap(piece_white_selected,
				(int) new_x_piece, (int) new_y_piece, false);
		piece_white_possible = Bitmap.createScaledBitmap(piece_white_possible,
				(int) new_x_piece, (int) new_y_piece, false);
	}
}
