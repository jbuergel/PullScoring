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

package com.houseofslack.widget;

import com.houseofslack.pullscoring.R;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

public class FoulImageView extends ImageView {
	Context mContext = null;
	boolean mIsFoul = false;
	
	public FoulImageView(Context context) {
		super(context);
		mContext = context;
		setFoulDrawable();
	}

	public FoulImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		setFoulDrawable();
	}

	public FoulImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		mContext = context;
		setFoulDrawable();
	}

	public boolean isFoul() {
		return mIsFoul;
	}
	
	private void setFoulDrawable() {
		if (mIsFoul) {
			setImageDrawable(mContext.getResources().getDrawable(R.drawable.is_foul));
		} else {
			setImageDrawable(mContext.getResources().getDrawable(R.drawable.not_foul));
		}
	}
	
	public void toggleFoul() {
		mIsFoul = !mIsFoul;
		setFoulDrawable();
	}
	
	public void setFoul(boolean newFoul) {
		mIsFoul = newFoul;
		setFoulDrawable();
	}
}
