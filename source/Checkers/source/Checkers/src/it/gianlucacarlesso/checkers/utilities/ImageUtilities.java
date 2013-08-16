package it.gianlucacarlesso.checkers.utilities;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

public class ImageUtilities {

	@SuppressWarnings("deprecation")
	public static Drawable resize(Drawable image, int x, int y) {
		Bitmap d = ((BitmapDrawable) image).getBitmap();
		Bitmap bitmapOrig = Bitmap.createScaledBitmap(d, x, y, false);
		return new BitmapDrawable(bitmapOrig);
	}
}
