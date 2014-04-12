package com.houseofslack.pullscoring;

import android.app.Activity;
import android.os.Bundle;
import android.text.Html;
import android.widget.TextView;

public class Rules extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_rules);
		TextView textView = (TextView) findViewById(R.id.body);
		CharSequence cs = Html.fromHtml(getString(R.string.rules_body));
		textView.setText(cs);
	}
}
