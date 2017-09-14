package com.puc.font;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.Button;

public class ButtonMontserrat_bld extends Button {

	public ButtonMontserrat_bld(Context context) {
		super(context);
		setFont();
	}
	public ButtonMontserrat_bld(Context context, AttributeSet attrs) {
		super(context, attrs);
		setFont();
	}
	public ButtonMontserrat_bld(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		setFont();
	}

	private void setFont() {
		Typeface font = Typeface.createFromAsset(getContext().getAssets(), "fonts/Montserrat-Bold.ttf");
		setTypeface(font, Typeface.NORMAL);
	}

}