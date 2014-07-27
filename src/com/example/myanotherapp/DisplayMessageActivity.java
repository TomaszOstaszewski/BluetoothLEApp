package com.example.myanotherapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class DisplayMessageActivity extends Activity {

    private static final int textSize = 40;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_display_message);
		Intent anIntentThatCreatedMe = getIntent();
		mTextView = (TextView)findViewById(R.id.display_activity_tv_1);
		String message = anIntentThatCreatedMe.getStringExtra(MainActivity.EXTRA_MESSAGE);

        mTextView.setTextSize(textSize);
		mTextView.setText(message);
	}
	TextView mTextView;
}
