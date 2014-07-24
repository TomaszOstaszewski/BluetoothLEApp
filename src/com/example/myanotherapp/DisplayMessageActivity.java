package com.example.myanotherapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class DisplayMessageActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_display_message);
		Intent anIntentThatCreatedMe = getIntent();
		mTextView = (TextView)findViewById(R.id.display_activity_tv_1);
	}
	TextView mTextView;
}
