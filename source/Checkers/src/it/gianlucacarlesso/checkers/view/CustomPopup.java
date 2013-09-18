package it.gianlucacarlesso.checkers.view;

import it.gianlucacarlesso.checkers.R;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public abstract class CustomPopup extends Dialog implements OnClickListener {

	private Context context;
	private TextView textMessage;
	private Button yesButton;
	private Button noButton;
	private Typeface typface;

	public CustomPopup(Context cotext, String text) {
		super(cotext, android.R.style.Theme_Translucent_NoTitleBar);

		context = cotext;

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		setContentView(R.layout.custom_popup);

		// Customize the font of buttons and text
		typface = Typeface.createFromAsset(context.getAssets(),
				"fonts/curse_casual.ttf");

		textMessage = (TextView) findViewById(R.id.TextMessage);
		textMessage.setTypeface(typface);
		textMessage.setText(text);

		yesButton = (Button) findViewById(R.id.OkButton);
		yesButton.setTypeface(typface);
		yesButton.setOnClickListener(this);

		noButton = (Button) findViewById(R.id.NoButton);
		noButton.setTypeface(typface);
		noButton.setOnClickListener(this);

		// I prevent the deletion of the pop-up without selecting an
		setCanceledOnTouchOutside(false);
		setCancelable(false);
	}

	public TextView getTextMessage() {
		return textMessage;
	}

	public void setTextMessage(TextView textMessage) {
		this.textMessage = textMessage;
	}

	public Button getYesButton() {
		return yesButton;
	}

	public void setYesButton(Button yesButton) {
		this.yesButton = yesButton;
	}

	public Button getNoButton() {
		return noButton;
	}

	public void setNoButton(Button noButton) {
		this.noButton = noButton;
	}

	public abstract void onClick(View v);

	@SuppressWarnings("deprecation")
	public void setBackgroundDrawable(Drawable background) {
		LinearLayout linearLayout = (LinearLayout) findViewById(R.id.custom_popup_layout);

		// Backward compatibility to API previous 16
		linearLayout.setBackgroundDrawable(background);
	}
}