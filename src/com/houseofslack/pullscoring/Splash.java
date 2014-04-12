package com.houseofslack.pullscoring;

import android.os.Bundle;
import android.view.View;
import android.app.Activity;
import android.content.Intent;

public class Splash extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
	}

	// do not change the name of this method without also updating the XML
	public void buttonRules(View unused) {
		startActivity(new Intent(this, Rules.class));
	}

	// do not change the name of this method without also updating the XML
	public void buttonAbout(View unused) {
		startActivity(new Intent(this, About.class));
	}

	// do not change the name of this method without also updating the XML
	public void buttonResume(View unused) {
		startActivity(new Intent(this, Scoring.class));
	}

	// do not change the name of this method without also updating the XML
	public void buttonNewGame(View unused) {
		Intent intent = new Intent(this, Scoring.class);
		intent.putExtra(Scoring.RESET_SCORES_FLAG, true);
		startActivity(intent);
	}
}
