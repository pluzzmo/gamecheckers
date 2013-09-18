package it.gianlucacarlesso.checkers;

import it.gianlucacarlesso.checkers.logic.Player;
import it.gianlucacarlesso.checkers.utilities.DisplayProperties;
import it.gianlucacarlesso.checkers.utilities.ImageUtilities;
import it.gianlucacarlesso.checkers.view.GBoard;
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
	public static String STRATEGY_PLAYER_BLACK = "strategy_black";
	public static String STRATEGY_PLAYER_WHITE = "strategy_white";
	
	private GBoard gboard;

	@SuppressWarnings("deprecation")
	@Override
	@TargetApi(16)
	@SuppressLint("NewApi")
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		int layout_id = 0;
		if (getIntent().getIntExtra(CheckerboardActivity.GAME_MODE, 0) == 0) {
			// IA vs IA game mode
			layout_id = R.layout.activity_checkerboard_ia_vs_ia;
		} else if (getIntent().getIntExtra(CheckerboardActivity.GAME_MODE, 0) == 1) {
			// Man vs IA game mode
			layout_id = R.layout.activity_checkerboard_man_vs_ia;
		} else {
			// Man vs Man game mode
			layout_id = R.layout.activity_checkerboard_man_vs_man;
		}
		setContentView(layout_id);

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
		player1.setTypeface(typface);
		player2.setTypeface(typface);

		// Imposed on the game mode Damiera
		gboard = (GBoard) findViewById(R.id.board);
		gboard.setGameMode(getIntent().getIntExtra(
				CheckerboardActivity.GAME_MODE, 0));

		gboard.stringNamePlayerBlack = player1.getText().toString();
		gboard.stringNamePlayerWhite = player2.getText().toString();
		
		if (getIntent().getIntExtra(CheckerboardActivity.GAME_MODE, 0) == 0) {
			// IA vs IA game mode
			gboard.setStrategy(Player.PLAYER_BLACK, getIntent().getIntExtra(CheckerboardActivity.STRATEGY_PLAYER_BLACK, 0));
			gboard.setStrategy(Player.PLAYER_WHITE, getIntent().getIntExtra(CheckerboardActivity.STRATEGY_PLAYER_WHITE, 0));
		} else if (getIntent().getIntExtra(CheckerboardActivity.GAME_MODE, 0) == 1) {
			// Man vs IA game mode
			gboard.setStrategy(Player.PLAYER_BLACK, getIntent().getIntExtra(CheckerboardActivity.STRATEGY_PLAYER_BLACK, 0));
		}
	}

	   @Override
	    public void onDestroy()
	    {
	        super.onDestroy();
	        if(gboard.handlerIA != null && gboard.run_ia != null) {
	        	gboard.handlerIA.removeCallbacks(gboard.run_ia);
	        }
	    }
}
