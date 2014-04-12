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
		TextView textView = (TextView) findViewById(R.id.about_text);
		CharSequence cs = Html.fromHtml(getString(R.string.about_body));
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
