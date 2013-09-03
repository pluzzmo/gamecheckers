package it.gianlucacarlesso.checkers.view;

import java.util.ArrayList;

import it.gianlucacarlesso.checkers.R;
import it.gianlucacarlesso.checkers.logic.Board;
import it.gianlucacarlesso.checkers.logic.Engine;
import it.gianlucacarlesso.checkers.logic.Piece;
import it.gianlucacarlesso.checkers.utilities.DisplayProperties;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class GBoard extends View {
	private static Point SIZE_BOARD_ORIGIN = new Point(1319, 1406);
	private static Point CENTER_BOARD_ORIGIN = new Point(649, 627);
	private static Point SIZE_BOX_ORIGIN = new Point(140, 140);
	private Engine engine = new Engine();

	private Context context;
	private Bitmap board;
	private Bitmap piece_black;
	private Bitmap piece_black_selected;
	private Bitmap piece_black_possible;
	private Bitmap piece_black_dama;
	private Bitmap piece_black_selected_dama;
	private Bitmap piece_black_possible_dama;
	private Bitmap piece_white;
	private Bitmap piece_white_selected;
	private Bitmap piece_white_possible;
	private Bitmap piece_white_dama;
	private Bitmap piece_white_selected_dama;
	private Bitmap piece_white_possible_dama;
	private float shadow = 17;
	private Point screen_size;
	private PointF[][] matrix = new PointF[Board.NUM_BOX_ROW][Board.NUM_BOX_ROW];
	private float correct = 0;
	private ArrayList<Point> moves = null;

	float box_size_x;
	float box_size_y;
	float board_center_x;
	float board_center_y;

	public GBoard(Context _context) {
		super(_context);
		context = _context;

		graphicInitialization();
	}

	public GBoard(Context _context, AttributeSet attrs) {
		super(_context, attrs);
		context = _context;

		graphicInitialization();
	}

	public GBoard(Context _context, AttributeSet attrs, int defStyle) {
		super(_context, attrs, defStyle);
		context = _context;

		graphicInitialization();
	}

	@Override
	public void onDraw(Canvas canvas) {
		float pos_x = 0;
		float pos_y = 0;
		pos_x = (int) (1.0 * board.getWidth() / screen_size.x * shadow / 2);

		// Draw picture of board
		canvas.drawBitmap(board, pos_x, pos_y, null);

		correct = (int) ((box_size_x + pos_x - piece_black.getWidth()) / 2.0);

		// Draw pieces
		drawPieces(canvas, (int) correct, pos_x, pos_y);

		Piece current_selected = engine.getCurrentSelected();
		
		if (current_selected != null) {
			Bitmap image_piece = null;
			if (Piece.PLAYER_BLACK == current_selected.player) {
				if (!current_selected.dama) {
					image_piece = piece_black_selected;
				} else {
					image_piece = piece_black_selected_dama;
				}
			} else if (Piece.PLAYER_WHITE == current_selected.player) {
				if (!current_selected.dama) {
					image_piece = piece_white_selected;
				} else {
					image_piece = piece_white_selected_dama;
				}
			}

			canvas.drawBitmap(
					image_piece,
					(float) ((float) matrix[current_selected.x][current_selected.y].x
							+ (pos_x / 2.0) + correct),
					(float) matrix[current_selected.x][current_selected.y].y
							+ (float) ((box_size_y - image_piece.getHeight()) / 2.0),
					null);

			// Retrieving all the possible moves of the pawn selected
			moves = engine.getNextPossibleMoves();

			// I visualize the images of possible moves depending on the
			// player's turn
			Bitmap possible_image = null;
			if (current_selected.player == Piece.PLAYER_BLACK) {
				if (!current_selected.dama) {
					possible_image = piece_black_possible;
				} else {
					possible_image = piece_black_possible_dama;
				}
			} else if (current_selected.player == Piece.PLAYER_WHITE) {
				if (!current_selected.dama) {
					possible_image = piece_white_possible;
				} else {
					possible_image = piece_white_possible_dama;
				}
			}

			for (int i = 0; i < moves.size(); i++) {
				canvas.drawBitmap(
						possible_image,
						(float) ((float) matrix[moves.get(i).x][moves.get(i).y].x
								+ (pos_x / 2.0) + correct),
						(float) matrix[moves.get(i).x][moves.get(i).y].y
								+ (float) ((box_size_y - image_piece
										.getHeight()) / 2.0), null);
			}
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// Recovering the coordinates of the touch
		float x = event.getX();
		float y = event.getY();

		for (int i = 0; i < Board.NUM_BOX_ROW; i++) {
			for (int j = 0; j < Board.NUM_BOX_ROW; j++) {
				if (matrix[i][j].x <= x
						&& matrix[i][j].x + box_size_x >= x
						// Individual touched the box and if present a pawn
						// visualize the boxes where they can move
						&& matrix[i][j].y <= y
						&& matrix[i][j].y + box_size_y >= y) {

						engine.executeAction(i, j, context);
						this.invalidate();

					i = Board.NUM_BOX_ROW + 1;
					j = Board.NUM_BOX_ROW + 1;
				} else {
					// engine.disabledCurrentSelected();
				}
			}
		}
		return false;
	}

	@Override
	protected void onMeasure(int w, int h) {
		// I need to set the size of the view depending on the object that sign,
		// otherwise occupy all the space
		if (board != null) {
			setMeasuredDimension(board.getWidth(), board.getHeight());
		}
	}

	private void graphicInitialization() {
		screen_size = DisplayProperties.getMetrics(context);

		// Load picture of board
		Resources res = getResources();
		board = BitmapFactory.decodeResource(res, R.drawable.board);

		float new_y_board = (int) (1.0 * screen_size.x / board.getWidth() * board
				.getHeight());

		board = Bitmap.createScaledBitmap(board, screen_size.x,
				(int) new_y_board, false);

		board_center_x = (float) (1.0 * board.getWidth() / SIZE_BOARD_ORIGIN.x * CENTER_BOARD_ORIGIN.x);
		board_center_y = (float) (1.0 * board.getHeight() / SIZE_BOARD_ORIGIN.y * CENTER_BOARD_ORIGIN.y);

		box_size_x = (float) (1.0 * board.getWidth() / SIZE_BOARD_ORIGIN.x * SIZE_BOX_ORIGIN.x);
		box_size_y = (float) (1.0 * board.getHeight() / SIZE_BOARD_ORIGIN.y * SIZE_BOX_ORIGIN.y);

		// Load pieces images
		piece_black = BitmapFactory.decodeResource(res,
				R.drawable.piece_black_horizontal);
		piece_black_selected = BitmapFactory.decodeResource(res,
				R.drawable.piece_black_horizontal_selected);
		piece_black_possible = BitmapFactory.decodeResource(res,
				R.drawable.piece_black_horizontal_possible);
		piece_black_dama = BitmapFactory.decodeResource(res,
				R.drawable.piece_black_horizontal_dama);
		piece_black_selected_dama = BitmapFactory.decodeResource(res,
				R.drawable.piece_black_horizontal_selected_dama);
		piece_black_possible_dama = BitmapFactory.decodeResource(res,
				R.drawable.piece_black_horizontal_possible_dama);
		piece_white = BitmapFactory.decodeResource(res,
				R.drawable.piece_white_horizontal);
		piece_white_selected = BitmapFactory.decodeResource(res,
				R.drawable.piece_white_horizontal_selected);
		piece_white_possible = BitmapFactory.decodeResource(res,
				R.drawable.piece_white_horizontal_possible);
		piece_white_dama = BitmapFactory.decodeResource(res,
				R.drawable.piece_white_horizontal_dama);
		piece_white_selected_dama = BitmapFactory.decodeResource(res,
				R.drawable.piece_white_horizontal_selected_dama);
		piece_white_possible_dama = BitmapFactory.decodeResource(res,
				R.drawable.piece_white_horizontal_possible_dama);

		float new_x_piece = (int) (board.getWidth() / (Board.NUM_BOX_ROW + 1.3));
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
		piece_black_dama = Bitmap.createScaledBitmap(piece_black_dama,
				(int) new_x_piece, (int) new_y_piece, false);
		piece_black_selected_dama = Bitmap.createScaledBitmap(
				piece_black_selected_dama, (int) new_x_piece,
				(int) new_y_piece, false);
		piece_black_possible_dama = Bitmap.createScaledBitmap(
				piece_black_possible_dama, (int) new_x_piece,
				(int) new_y_piece, false);
		piece_white = Bitmap.createScaledBitmap(piece_white, (int) new_x_piece,
				(int) new_y_piece, false);
		piece_white_selected = Bitmap.createScaledBitmap(piece_white_selected,
				(int) new_x_piece, (int) new_y_piece, false);
		piece_white_possible = Bitmap.createScaledBitmap(piece_white_possible,
				(int) new_x_piece, (int) new_y_piece, false);
		piece_white_dama = Bitmap.createScaledBitmap(piece_white_dama,
				(int) new_x_piece, (int) new_y_piece, false);
		piece_white_selected_dama = Bitmap.createScaledBitmap(
				piece_white_selected_dama, (int) new_x_piece,
				(int) new_y_piece, false);
		piece_white_possible_dama = Bitmap.createScaledBitmap(
				piece_white_possible_dama, (int) new_x_piece,
				(int) new_y_piece, false);

		// Calculating all the boxes of Damiera
		float xbox = board_center_x - box_size_x * Board.NUM_BOX_ROW / 2;
		float ybox = board_center_y - box_size_y * Board.NUM_BOX_ROW / 2;
		for (int i = 0; i < Board.NUM_BOX_ROW; i++) {
			for (int j = 0; j < Board.NUM_BOX_ROW; j++) {
				matrix[i][j] = new PointF(xbox, ybox);
				xbox += box_size_x;
			}
			xbox = board_center_x - box_size_x * Board.NUM_BOX_ROW / 2;
			ybox += box_size_y;
		}
	}

	private void drawPieces(Canvas canvas, float correct, float pos_x,
			float pos_y) {

		// Walk the entire board and drawing their pawns
		Bitmap image_piece = null;
		Piece[][] board_logic = engine.getBoardLogic();
		for (int i = 0; i < Board.NUM_BOX_ROW; i++) {
			for (int j = 0; j < Board.NUM_BOX_ROW; j++) {
				// Control in the current box there is a pawn
				if (board_logic[i][j] != null) {
					if (board_logic[i][j].player == Piece.PLAYER_BLACK) {
						if (!board_logic[i][j].dama) {
							image_piece = piece_black;
						} else {
							image_piece = piece_black_dama;
						}
					} else {
						if (!board_logic[i][j].dama) {
							image_piece = piece_white;
						} else {
							image_piece = piece_white_dama;
						}
					}

					canvas.drawBitmap(
							image_piece,
							matrix[i][j].x + (float) (pos_x / 2.0) + correct,
							matrix[i][j].y
									+ ((float) ((box_size_y - image_piece
											.getHeight()) / 2.0)), null);
				}
			}
		}
	}
}
