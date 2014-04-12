/*
The MIT License (MIT)

Copyright (c) 2014 Joshua Buergel

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
*/

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
