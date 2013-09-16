package it.gianlucacarlesso.checkers.logic;

import android.graphics.Point;

public class Move {
	public Point pointFrom;
	public Point pointTo;
	public Piece pawnDeleted;
	public boolean isDama;

	public Move(Point _pointFrom, Point _pointTo) {
		pointFrom = _pointFrom;
		pointTo = _pointTo;
		pawnDeleted = null;
		isDama = false;
	}

	public Move(Point _pointFrom, Point _pointTo, boolean _isDama,
			Piece _pawnDeleted) {
		pointFrom = _pointFrom;
		pointTo = _pointTo;
		pawnDeleted = _pawnDeleted;
		isDama = _isDama;
	}
}