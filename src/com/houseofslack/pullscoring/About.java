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

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.widget.TextView;

public class About extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about);
		setHtmlText(R.id.about_body, R.string.about_body);
		setHtmlText(R.id.about_footer, R.string.about_footer);
		
	}
	
	private void setHtmlText(int textViewId, int stringId) {
		TextView textView = (TextView) findViewById(textViewId);
		CharSequence cs = Html.fromHtml(getString(stringId));
		textView.setText(cs);
		textView.setMovementMethod(LinkMovementMethod.getInstance());
	}
	
	public static class NamesFragment extends PreferenceFragment implements OnSharedPreferenceChangeListener {

		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);

			// Load the preferences from an XML resource
			addPreferencesFromResource(R.xml.prefs);
			
			setTitles(getPreferenceScreen().getSharedPreferences(), getString(R.string.team_name_key_0));
			setTitles(getPreferenceScreen().getSharedPreferences(), getString(R.string.team_name_key_1));
		}
		
		private void setTitles(SharedPreferences sharedPreferences, String key) {
			if (key.equals(getString(R.string.team_name_key_0))) {
				findPreference(key).setTitle(getString(R.string.team_name_title, sharedPreferences.getString(key, getString(R.string.default_team_name_0))));
			} else if (key.equals(getString(R.string.team_name_key_1))) {
				findPreference(key).setTitle(getString(R.string.team_name_title, sharedPreferences.getString(key, getString(R.string.default_team_name_1))));
			}
		}
		
		@Override
		public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
			setTitles(sharedPreferences, key);
		}

		@Override
		public void onResume() {
			super.onResume();
			// Set up a listener whenever a key changes
			getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
		}

		@Override
		public void onPause() {
			super.onPause();
			// Set up a listener whenever a key changes
			getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
		}
	}
}
