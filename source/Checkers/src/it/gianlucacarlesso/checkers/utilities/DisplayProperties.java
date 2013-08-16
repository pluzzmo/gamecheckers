package it.gianlucacarlesso.checkers.utilities;

import android.content.Context;
import android.graphics.Point;
import android.view.Display;
import android.view.WindowManager;

public class DisplayProperties {
	@SuppressWarnings("deprecation")
	public static Point getMetrics(Context context) {
		WindowManager wm = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();

		return new Point(display.getWidth(), display.getHeight());
	}
}
