package com.puc.font;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

public class TextViewMontserrat_bld extends TextView {

	public TextViewMontserrat_bld(Context context) {
		super(context);
		setFont();
	}
	public TextViewMontserrat_bld(Context context, AttributeSet attrs) {
		super(context, attrs);
		setFont();
	}
	public TextViewMontserrat_bld(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		setFont();
	}

	private void setFont() {
		Typeface font = Typeface.createFromAsset(getContext().getAssets(), "fonts/Montserrat-Bold.ttf");
		setTypeface(font, Typeface.NORMAL);
	}

}