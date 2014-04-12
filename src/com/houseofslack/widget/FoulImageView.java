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
}
