package com.philipp_mandler.android.vtpl;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;

public class MainActivity extends FragmentActivity {

	// Arrow designed by Jardson Ara?jo from The Noun Project

	
	public MainActivity() {
		Log.i("Acctivity", "Activity konstructor");
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
	}
	
	@Override
	public void onBackPressed() {
		if(findViewById(R.id.frameLayout3) != null && findViewById(R.id.frameLayout3).getVisibility() == View.VISIBLE) {
			findViewById(R.id.frameLayout3).setVisibility(View.INVISIBLE);
		}
		else
			super.onBackPressed();
	}
	
	public void onFrameLayout3Clicked(View view) {
		if(findViewById(R.id.frameLayout3).getVisibility() == View.VISIBLE)
			findViewById(R.id.frameLayout3).setVisibility(View.INVISIBLE);
	}
}
