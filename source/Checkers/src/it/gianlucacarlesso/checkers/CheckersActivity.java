package it.gianlucacarlesso.checkers;

import it.gianlucacarlesso.checkers.logic.Engine;
import it.gianlucacarlesso.checkers.logic.Strategy;
import it.gianlucacarlesso.checkers.utilities.DisplayProperties;
import it.gianlucacarlesso.checkers.utilities.ImageUtilities;

import java.util.Arrays;
import java.util.List;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

public class CheckersActivity extends Activity {

	@SuppressWarnings("deprecation")
	@Override
	@TargetApi(16)
	@SuppressLint("NewApi")
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_checkers);

		// Set activity background
		View layout = getWindow().getDecorView().findViewById(
				android.R.id.content);
		Drawable image = getResources().getDrawable(
				R.drawable.background_checkers_activity);

		Point screen_size = DisplayProperties
				.getMetrics(getApplicationContext());
		image = ImageUtilities.resize(image, screen_size.x, screen_size.y);

		if (Build.VERSION.SDK_INT >= 16) {
			layout.setBackground(image);
		} else {
			layout.setBackgroundDrawable(image);
		}

		// Set font
		Typeface typface = Typeface.createFromAsset(getAssets(),
				"fonts/english.ttf");
		TextView title = (TextView) findViewById(R.id.title_app);
		title.setTypeface(typface, Typeface.BOLD);

		typface = Typeface.createFromAsset(getAssets(),
				"fonts/curse_casual.ttf");

		Button button = (Button) findViewById(R.id.button_iavsia);
		button.setTypeface(typface);

		button = (Button) findViewById(R.id.button_manvsia);
		button.setTypeface(typface);

		button = (Button) findViewById(R.id.button_manvsman);
		button.setTypeface(typface);

		title = (TextView) findViewById(R.id.copyright);
		title.setTypeface(typface);

		TextView options = (TextView) findViewById(R.id.options);
		options.setTypeface(typface);

		TextView depth = (TextView) findViewById(R.id.depth);
		depth.setTypeface(typface);

		TextView strategy1 = (TextView) findViewById(R.id.strategy1);
		strategy1.setTypeface(typface);

		TextView strategy2 = (TextView) findViewById(R.id.strategy2);
		strategy2.setTypeface(typface);

		// Imposed on the content and appearance of the elements of the options
		Spinner depth_value = (Spinner) findViewById(R.id.depth_value);
		List<String> list = Arrays.asList(getResources().getStringArray(
				R.array.depth_array));

		ArrayAdapter<String> adapter_depth = new ArrayAdapter<String>(this,

		R.layout.spinner, list) {

			public View getView(int position, View convertView, ViewGroup parent) {

				View v = super.getView(position, convertView, parent);

				Typeface externalFont = Typeface.createFromAsset(getAssets(),
						"fonts/curse_casual.ttf");

				((TextView) v).setTypeface(externalFont);

				return v;

			}

			public View getDropDownView(int position, View convertView,
					ViewGroup parent) {

				View v = super.getDropDownView(position, convertView, parent);

				Typeface externalFont = Typeface.createFromAsset(getAssets(),
						"fonts/curse_casual.ttf");

				((TextView) v).setTypeface(externalFont);

				return v;

			}

		};
		adapter_depth.setDropDownViewResource(R.layout.spinner_drop);
		depth_value.setAdapter(adapter_depth);
		depth_value.setOnItemSelectedListener(new OnItemSelectedListener() {
        	public void onItemSelected(AdapterView<?> adapter, View view,int pos, long id) {
        		String selected = (String)adapter.getItemAtPosition(pos);
        		Engine.DEEP_SEARCH = Integer.valueOf(selected);
        	}
        	public void onNothingSelected(AdapterView<?> arg0) {}
		});

		Spinner strategy1_value = (Spinner) findViewById(R.id.strategy1_value);
		list = Arrays.asList(getResources().getStringArray(
				R.array.strategy_array));

		ArrayAdapter<String> adapter_strategy = new ArrayAdapter<String>(this,

		R.layout.spinner, list) {

			public View getView(int position, View convertView, ViewGroup parent) {

				View v = super.getView(position, convertView, parent);

				Typeface externalFont = Typeface.createFromAsset(getAssets(),
						"fonts/curse_casual.ttf");

				((TextView) v).setTypeface(externalFont);

				return v;

			}

			public View getDropDownView(int position, View convertView,
					ViewGroup parent) {

				View v = super.getDropDownView(position, convertView, parent);

				Typeface externalFont = Typeface.createFromAsset(getAssets(),
						"fonts/curse_casual.ttf");

				((TextView) v).setTypeface(externalFont);

				return v;

			}

		};
		adapter_strategy.setDropDownViewResource(R.layout.spinner_drop);
		strategy1_value.setAdapter(adapter_strategy);
		strategy1_value.setOnItemSelectedListener(new OnItemSelectedListener() {
        	public void onItemSelected(AdapterView<?> adapter, View view,int pos, long id) {
        		String selected = (String)adapter.getItemAtPosition(pos);
        		if(selected.compareTo(getResources().getString(R.string.simplestrategy)) == 0) {
        		Engine.playerBlackStrategy = Strategy.SIMPLE_STRATEGY;
        		} else {
        			if(selected.compareTo(getResources().getString(R.string.avaragestrategy)) == 0) {
                		Engine.playerBlackStrategy = Strategy.AVARAGE_STRATEGY;
        			}
        		}
        	}
        	public void onNothingSelected(AdapterView<?> arg0) {}
		});

		Spinner strategy2_value = (Spinner) findViewById(R.id.strategy2_value);
		list = Arrays.asList(getResources().getStringArray(
				R.array.strategy_array));

		adapter_strategy.setDropDownViewResource(R.layout.spinner_drop);
		strategy2_value.setAdapter(adapter_strategy);
		strategy2_value.setOnItemSelectedListener(new OnItemSelectedListener() {
        	public void onItemSelected(AdapterView<?> adapter, View view,int pos, long id) {
        		String selected = (String)adapter.getItemAtPosition(pos);
        		if(selected.compareTo(getResources().getString(R.string.simplestrategy)) == 0) {
        		Engine.playerWhiteStrategy = Strategy.SIMPLE_STRATEGY;
        		} else {
        			if(selected.compareTo(getResources().getString(R.string.avaragestrategy)) == 0) {
                		Engine.playerWhiteStrategy = Strategy.AVARAGE_STRATEGY;
        			}
        		}
        	}
        	public void onNothingSelected(AdapterView<?> arg0) {}
		});

		// TODO REMOVE
		// Intent intent = new Intent(this, CheckerboardActivity.class);
		// intent.putExtra(CheckerboardActivity.GAME_MODE, 0);
		// startActivity(intent);
	}

	public void startCheckerBoard(View view) {
		Intent intent = new Intent(this, CheckerboardActivity.class);

		if (view.getId() == R.id.button_iavsia) {
			intent.putExtra(CheckerboardActivity.GAME_MODE, 0);
			intent.putExtra(CheckerboardActivity.STRATEGY_PLAYER_BLACK,
					Strategy.AVARAGE_STRATEGY);
			intent.putExtra(CheckerboardActivity.STRATEGY_PLAYER_WHITE,
					Strategy.AVARAGE_STRATEGY);
		} else if (view.getId() == R.id.button_manvsia) {
			intent.putExtra(CheckerboardActivity.GAME_MODE, 1);
			intent.putExtra(CheckerboardActivity.STRATEGY_PLAYER_BLACK,
					Strategy.AVARAGE_STRATEGY);
		} else {
			intent.putExtra(CheckerboardActivity.GAME_MODE, 2);
		}
		startActivity(intent);
	}

}
