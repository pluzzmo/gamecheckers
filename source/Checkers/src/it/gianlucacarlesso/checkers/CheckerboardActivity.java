package it.gianlucacarlesso.checkers;

import it.gianlucacarlesso.checkers.utilities.DisplayProperties;
import it.gianlucacarlesso.checkers.utilities.ImageUtilities;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.graphics.Point;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;

public class CheckerboardActivity extends Activity {
	public static String GAME_MODE = "game_mode";

	@SuppressWarnings("deprecation")
	@Override
	@TargetApi(16)
	@SuppressLint("NewApi")
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_checkerboard);

		// Set and rescale background
		View layout = getWindow().getDecorView().findViewById(
				android.R.id.content);
		Drawable image = getResources().getDrawable(
				R.drawable.background_activity);

		Point screen_size = DisplayProperties
				.getMetrics(getApplicationContext());
		image = ImageUtilities.resize(image, screen_size.x, screen_size.y);

		if (Build.VERSION.SDK_INT >= 16) {
			layout.setBackground(image);
		} else {
			layout.setBackgroundDrawable(image);
		}

		TextView player1 = (TextView) findViewById(R.id.player1);
		TextView player2 = (TextView) findViewById(R.id.player2);

		// Set font
		Typeface typface = Typeface.createFromAsset(getAssets(),
				"fonts/english.ttf");
		TextView title = (TextView) findViewById(R.id.title_app);
		title.setTypeface(typface, Typeface.BOLD);

		typface = Typeface.createFromAsset(getAssets(),
				"fonts/curse_casual.ttf");
		if (getIntent().getIntExtra(CheckerboardActivity.GAME_MODE, 0) == 0) {
			// IA vs IA game mode
			player1.setText(R.string.player_ia_1);
			player2.setText(R.string.player_ia_2);
		} else if (getIntent().getIntExtra(CheckerboardActivity.GAME_MODE, 0) == 1) {
			// Man vs IA game mode
			player1.setText(R.string.player);
			player2.setText(R.string.player_men);
		} else {
			// Man vs Man game mode
			player1.setText(R.string.player_1);
			player2.setText(R.string.player_1);
		}

		player1.setTypeface(typface);
		player2.setTypeface(typface);
	}

}
