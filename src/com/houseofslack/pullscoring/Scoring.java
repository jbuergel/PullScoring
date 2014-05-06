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

import java.io.StringWriter;
import java.util.LinkedList;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.houseofslack.widget.FoulImageView;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class Scoring extends Activity {
	public static final String RESET_SCORES_FLAG = "resetScoresFlag";
	private static final String TEAM_0_SCORES_KEY = "team0ScoresKey";
	private static final String TEAM_1_SCORES_KEY = "team1ScoresKey";
	private static final String TEAM_0_FOULS_KEY = "team0FoulsKey";
	private static final String TEAM_1_FOULS_KEY = "team1FoulsKey";
	private static final int PENALTY_SCORE = -20;
	List<Integer> team0Scores = new LinkedList<Integer>();
	List<Integer> team1Scores = new LinkedList<Integer>();
	String mLeftTeamName = null;
	String mRightTeamName = null;
	int mCurrentFocus = R.id.left_score_input;
	int[] mLeftFouls = new int[]{R.id.team_0_foul_0, R.id.team_0_foul_1, R.id.team_0_foul_2, R.id.team_0_foul_3, R.id.team_0_foul_4};
	int[] mRightFouls = new int[]{R.id.team_1_foul_0, R.id.team_1_foul_1, R.id.team_1_foul_2, R.id.team_1_foul_3, R.id.team_1_foul_4};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_scoring);
		// check if we're resetting the scores
		if (getIntent().getBooleanExtra(RESET_SCORES_FLAG, false)) {
			SharedPreferences.Editor edit = PreferenceManager.getDefaultSharedPreferences(this).edit();
			edit.remove(TEAM_0_SCORES_KEY);
			edit.remove(TEAM_1_SCORES_KEY);
			edit.remove(TEAM_0_FOULS_KEY);
			edit.remove(TEAM_1_FOULS_KEY);
			edit.commit();
			setIntent(new Intent(this, Scoring.class));
		}
	}
	
	@SuppressWarnings("unchecked")
	@Override
	protected void onResume() {
		super.onResume();
		// put the team names into the appropriate slots - these can be modified in preferences by the about screen
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
		mLeftTeamName = prefs.getString(getString(R.string.team_name_key_0), getString(R.string.default_team_name_0));
		mRightTeamName = prefs.getString(getString(R.string.team_name_key_1), getString(R.string.default_team_name_1));
		// fetch our scores from the preferences
		if ((prefs.getString(TEAM_0_SCORES_KEY, null) != null) && (prefs.getString(TEAM_1_SCORES_KEY, null) != null) &&
			(prefs.getString(TEAM_0_FOULS_KEY, null) != null) && (prefs.getString(TEAM_1_FOULS_KEY, null) != null)) {
			ObjectMapper objectMapper = new ObjectMapper();
			try {
				team0Scores = objectMapper.readValue(prefs.getString(TEAM_0_SCORES_KEY, null), List.class);
				team1Scores = objectMapper.readValue(prefs.getString(TEAM_1_SCORES_KEY, null), List.class);
				arrayToCheckBoxes(mLeftFouls, objectMapper.readValue(prefs.getString(TEAM_0_FOULS_KEY, null), boolean[].class));
				arrayToCheckBoxes(mRightFouls, objectMapper.readValue(prefs.getString(TEAM_1_FOULS_KEY, null), boolean[].class));
			} catch (Exception e) {
			}
		}
		
		// draw the scores
		redrawScores();
		// make sure the first input is selected
		clickLeftInput(null);
	}
	
	private String objectToJson(ObjectMapper objectMapper, Object list) {
		StringWriter writer = new StringWriter();
		try {
			objectMapper.writeValue(writer, list);
		} catch (Exception e) {
		}			
		return writer.toString();
	}
	
	private boolean[] checkBoxesToArray(int[] ids) {
		boolean[] values = new boolean[ids.length];
		for (int ii = 0; ii < ids.length; ii++) {
			FoulImageView foulImageView = (FoulImageView) findViewById(ids[ii]);
			values[ii] = foulImageView.isFoul();
		}
		return values;
	}
	
	private void arrayToCheckBoxes(int[] ids, boolean[] values) {
		for (int ii = 0; ii < ids.length; ii++) {
			FoulImageView foulImageView = (FoulImageView) findViewById(ids[ii]);
			foulImageView.setFoul(values[ii]);
		}
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		SharedPreferences.Editor edit = PreferenceManager.getDefaultSharedPreferences(this).edit();
		ObjectMapper objectMapper = new ObjectMapper();
		edit.putString(TEAM_0_SCORES_KEY, objectToJson(objectMapper, team0Scores));
		edit.putString(TEAM_1_SCORES_KEY, objectToJson(objectMapper, team1Scores));
		edit.putString(TEAM_0_FOULS_KEY, objectToJson(objectMapper, checkBoxesToArray(mLeftFouls)));
		edit.putString(TEAM_1_FOULS_KEY, objectToJson(objectMapper, checkBoxesToArray(mRightFouls)));
		edit.commit();
	}
	
	// fetches a score from one of the inputs
	private int getScore(int inputId) {
		TextView input = (TextView) findViewById(inputId);
		int value = 0;
		try {
			value = Integer.valueOf(input.getText().toString());
		} catch (NumberFormatException ex) {
			// do nothing, we've already set to the default
		}
		return value;
	}
	
	// puts a score into one of the inputs
	private void setScore(int inputId, int newScore) {
		TextView input = (TextView) findViewById(inputId);
		String value = String.valueOf(newScore);
		input.setText(value);
	}
	
	// clears the data from one of the inputs
	private void clearScore(int inputId) {
		TextView input = (TextView) findViewById(inputId);
		input.setText("");
	}
	
	// clears the data from both inputs
	private void clearScores() {
		clearScore(R.id.left_score_input);
		clearScore(R.id.right_score_input);
	}
	
	// puts the scores into one of the linear layouts in the app
	// the scores passed in are the individual scores from the hands, not totals, so we have to run through them
	// as we go along
	private void redrawOneSetOfScores(LinearLayout layout, List<Integer> scores, int scoreLayoutId, int scrollLayoutId, String teamName, int teamAndScoreId) {
		LayoutInflater inflater = getLayoutInflater();
		layout.removeAllViews();
		int totalScore = 0;
		for (int score : scores) {
			totalScore += score;
			TextView textView = (TextView) inflater.inflate(scoreLayoutId, null);
			textView.setText(String.valueOf(score));
			layout.addView(textView);
		}
		TextView textView = (TextView) findViewById(teamAndScoreId);
		textView.setText(getString(R.string.total_score, teamName, totalScore));
		// set the scroll view to the bottom of the display
		final ScrollView scrollView = (ScrollView) findViewById(scrollLayoutId);
		scrollView.post(new Runnable() {
			@Override
			public void run() {
				scrollView.fullScroll(ScrollView.FOCUS_DOWN);
			}
		});
	}
	
	// redraws the scores into the linear layouts that provide the running totals
	// note that a 5-foul set is considered a sepaarate "score" for these purposes, allowing
	// the user to undo a 5-foul score if necessary
	private void redrawScores() {
		redrawOneSetOfScores((LinearLayout) findViewById(R.id.team_0_scores), team0Scores, R.layout.single_score_left, R.id.team_0_scroll, mLeftTeamName, R.id.team_0_name_and_score);
		redrawOneSetOfScores((LinearLayout) findViewById(R.id.team_1_scores), team1Scores, R.layout.single_score_right, R.id.team_1_scroll, mRightTeamName, R.id.team_1_name_and_score);
	}
	
	// do not change the name of this method without updating the XML
	public void clickGoUndo(View unused) {
		int team0Score = getScore(R.id.left_score_input);
		int team1Score = getScore(R.id.right_score_input);
		
		// if both scores are 0, this is an undo, not a go
		// but, if there are no scores, do nothing
		if ((team0Score == 0) && (team1Score == 0)) {
			// attempted undo, check for any scores in the list
			if (!team0Scores.isEmpty() && !team1Scores.isEmpty()) {
				AlertDialog.Builder builder = new AlertDialog.Builder(this);
				builder.setMessage(R.string.undo_confirm_message);
				builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						// remove the last scores from the list
						team0Scores.remove(team0Scores.size() - 1);
						team1Scores.remove(team1Scores.size() - 1);
						redrawScores();
						// make sure we update the button in the middle
						setGoButtonCaption();
						// make sure the first input is selected
						clickLeftInput(null);
					}
				});
				builder.setNegativeButton(R.string.cancel, null);
				builder.show();
			}
		} else {
			// we have scores (at least one), add them to the lists, clear the strings,
			// and then redraw
			team0Scores.add(team0Score);
			team1Scores.add(team1Score);
			clearScores();
			redrawScores();
			// make sure we update the button in the middle
			setGoButtonCaption();
			// make sure the first input is selected
			clickLeftInput(null);
		}
	}
	
	// adds a digit to the end of the score
	// ignores trying to add a 0 to an empty score. Also caps the max digits at 4.
	private void addDigitToCurrentInput(int digit) {
		int currentScore = getScore(mCurrentFocus);
		
		// special case - if things are empty, throw out any zeros
		if ((currentScore == 0) && (digit == 0)) {
			return;
		}
		// if we're up to four digits already, ignore
		if (currentScore >= 1000) {
			return;
		}
		// OK, we're good. Get the current score, multiple by ten, and then add the digit
		int newScore = currentScore * 10 + digit;
		setScore(mCurrentFocus, newScore);
		// make sure we update the button in the middle
		setGoButtonCaption();
	}

	// do not change the name of this method without updating the XML
	public void click1(View unused) {
		addDigitToCurrentInput(1);
	}

	// do not change the name of this method without updating the XML
	public void click2(View unused) {
		addDigitToCurrentInput(2);
	}

	// do not change the name of this method without updating the XML
	public void click3(View unused) {
		addDigitToCurrentInput(3);
	}

	// do not change the name of this method without updating the XML
	public void click4(View unused) {
		addDigitToCurrentInput(4);
	}

	// do not change the name of this method without updating the XML
	public void click5(View unused) {
		addDigitToCurrentInput(5);
	}

	// do not change the name of this method without updating the XML
	public void click6(View unused) {
		addDigitToCurrentInput(6);
	}

	// do not change the name of this method without updating the XML
	public void click7(View unused) {
		addDigitToCurrentInput(7);
	}

	// do not change the name of this method without updating the XML
	public void click8(View unused) {
		addDigitToCurrentInput(8);
	}

	// do not change the name of this method without updating the XML
	public void click9(View unused) {
		addDigitToCurrentInput(9);
	}

	// do not change the name of this method without updating the XML
	public void click0(View unused) {
		addDigitToCurrentInput(0);
	}

	// do not change the name of this method without updating the XML
	public void clickMinus(View unused) {
		int currentScore = getScore(mCurrentFocus);
		
		// special case - if things are empty, throw out any zeros
		if (currentScore == 0) {
			return;
		}
		// OK, we're good. Flip the sign.
		setScore(mCurrentFocus, currentScore * -1);
		// make sure we update the button in the middle
		setGoButtonCaption();
	}

	// do not change the name of this method without updating the XML
	public void clickDelete(View unused) {
		int currentScore = getScore(mCurrentFocus);
		
		// special case - if things are empty, throw out the delete
		if (currentScore == 0) {
			return;
		}
		// OK, we're good. Get the current score and divide by ten
		int newScore = currentScore / 10;
		// if it's 0 now, just clear
		if (newScore == 0) {
			clearScore(mCurrentFocus);
		} else {
			setScore(mCurrentFocus, newScore);
		}
		// make sure we update the button in the middle
		setGoButtonCaption();
	}

	private void checkFouls(boolean left) {
		int[] fouls = left ? mLeftFouls : mRightFouls;
		List<Integer> targetScores = left ? team0Scores : team1Scores;
		List<Integer> otherScores = left ? team1Scores : team0Scores;

		// see if there are any non-fouls in the buttons
		for (int ii = 0; ii < fouls.length; ii++) {
			FoulImageView foulImageView = (FoulImageView) findViewById(fouls[ii]);
			if (!foulImageView.isFoul()) {
				return;
			}
		}
		// if we get here, we have five fouls. Ouch! Time to ding them. We do this by adding
		// a penalty score to their side and a zero to the non-penalty side
		targetScores.add(PENALTY_SCORE);
		otherScores.add(0);
		redrawScores();
		// clear the fouls
		for (int ii = 0; ii < fouls.length; ii++) {
			FoulImageView foulImageView = (FoulImageView) findViewById(fouls[ii]);
			foulImageView.toggleFoul();
		}
	}
	
	// do not change the name of this method without updating the XML
	public void clickTeam0Foul0(View view) {
		FoulImageView foulView = (FoulImageView) view;
		foulView.toggleFoul();
		checkFouls(true);
	}

	// do not change the name of this method without updating the XML
	public void clickTeam0Foul1(View view) {
		FoulImageView foulView = (FoulImageView) view;
		foulView.toggleFoul();
		checkFouls(true);
	}

	// do not change the name of this method without updating the XML
	public void clickTeam0Foul2(View view) {
		FoulImageView foulView = (FoulImageView) view;
		foulView.toggleFoul();
		checkFouls(true);
	}

	// do not change the name of this method without updating the XML
	public void clickTeam0Foul3(View view) {
		FoulImageView foulView = (FoulImageView) view;
		foulView.toggleFoul();
		checkFouls(true);
	}

	// do not change the name of this method without updating the XML
	public void clickTeam0Foul4(View view) {
		FoulImageView foulView = (FoulImageView) view;
		foulView.toggleFoul();
		checkFouls(true);
	}

	// do not change the name of this method without updating the XML
	public void clickTeam1Foul0(View view) {
		FoulImageView foulView = (FoulImageView) view;
		foulView.toggleFoul();
		checkFouls(false);
	}

	// do not change the name of this method without updating the XML
	public void clickTeam1Foul1(View view) {
		FoulImageView foulView = (FoulImageView) view;
		foulView.toggleFoul();
		checkFouls(false);
	}

	// do not change the name of this method without updating the XML
	public void clickTeam1Foul2(View view) {
		FoulImageView foulView = (FoulImageView) view;
		foulView.toggleFoul();
		checkFouls(false);
	}

	// do not change the name of this method without updating the XML
	public void clickTeam1Foul3(View view) {
		FoulImageView foulView = (FoulImageView) view;
		foulView.toggleFoul();
		checkFouls(false);
	}

	// do not change the name of this method without updating the XML
	public void clickTeam1Foul4(View view) {
		FoulImageView foulView = (FoulImageView) view;
		foulView.toggleFoul();
		checkFouls(false);
	}
	
	// switches the input focus - note that the input focus isn't a "real" input focus.
	// we're trying to avoid popping up the Android keyboard here.
	private void changeInputFocus(int oldInputId, int newInputId) {
		TextView oldInput = (TextView) findViewById(oldInputId);
		TextView newInput = (TextView) findViewById(newInputId);
		newInput.setBackground(getResources().getDrawable(R.drawable.score_input_focused));
		newInput.setTextColor(getResources().getColor(R.color.focused_text_color));
		oldInput.setBackground(getResources().getDrawable(R.drawable.score_input_normal));
		oldInput.setTextColor(getResources().getColor(R.color.normal_text_color));
		mCurrentFocus = newInputId;
	}

	// do not change the name of this method without updating the XML
	public void clickLeftInput(View unused) {
		changeInputFocus(R.id.right_score_input, R.id.left_score_input);
	}

	// do not change the name of this method without updating the XML
	public void clickRightInput(View unused) {
		changeInputFocus(R.id.left_score_input, R.id.right_score_input);
	}

	// changes the button on the go/undo button to the correct thing
	private void setGoButtonCaption() {
		int team0Score = getScore(R.id.left_score_input);
		int team1Score = getScore(R.id.right_score_input);
		Button button = (Button) findViewById(R.id.button_go);
		if ((team0Score == 0) && (team1Score == 0)) {
			button.setText(getString(R.string.undo));
		} else {
			button.setText(getString(R.string.go));
		}
	}
}
