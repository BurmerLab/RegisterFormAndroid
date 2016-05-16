package com.mytway.utility;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

public class Fonts extends TextView{

    public Fonts(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }
    public Fonts(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    public Fonts(Context context) {
        super(context);
    }

    public void setTypeface(Typeface tf, int style) {
        if (style == Typeface.BOLD) {
            super.setTypeface(Typeface.createFromAsset(getContext().getAssets(),    "fonts/Roboto-Bold.ttf"));
        }
        else if(style == Typeface.ITALIC)
        {
            super.setTypeface(Typeface.createFromAsset(getContext().getAssets(), "fonts/Roboto-Italic.ttf"));
        }
        else
        {
            super.setTypeface(Typeface.createFromAsset(getContext().getAssets(), "fonts/Roboto-Regular.ttf"));
        }
    }

}
