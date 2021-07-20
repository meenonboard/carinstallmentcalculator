package com.PonRod;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.TextView;

public class About extends Activity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		   super.onCreate(savedInstanceState);
	       setContentView(R.layout.aboutview);
           TextView text_1 = (TextView) findViewById(R.id.aboutTextView1);
           TextView text_1_2 = (TextView) findViewById(R.id.aboutTextView1_1);
           TextView text_1_3 = (TextView) findViewById(R.id.aboutTextView1_2);
           TextView text_2 = (TextView) findViewById(R.id.aboutTextView2);
           TextView text_3 = (TextView) findViewById(R.id.aboutTextView3);
           TextView text_4 = (TextView) findViewById(R.id.aboutTextView4);
           TextView text_5 = (TextView) findViewById(R.id.aboutTextView5);
           TextView textImageRight = (TextView) findViewById(R.id.aboutImageRight);
           Typeface font = Typeface.createFromAsset(getAssets(), "CircularBold.ttf");  
           text_1.setTypeface(font);
           text_1_2.setTypeface(font);
           text_1_3.setTypeface(font);
           text_2.setTypeface(font);
           text_3.setTypeface(font);
           text_4.setTypeface(font);
           text_5.setTypeface(font);
           textImageRight.setTypeface(font);
	}
}
