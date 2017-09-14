package com.puc.font;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

public class TextViewComfortaa extends TextView {
	public TextViewComfortaa(Context context) {
		super(context);
		setFont();
	}
	public TextViewComfortaa(Context context, AttributeSet attrs) {
		super(context, attrs);
		setFont();
	}
	public TextViewComfortaa(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		setFont();
	}

	private void setFont() {
		Typeface font = Typeface.createFromAsset(getContext().getAssets(), "fonts/Montserrat-Regular.ttf");
		setTypeface(font, Typeface.NORMAL);
	}
}