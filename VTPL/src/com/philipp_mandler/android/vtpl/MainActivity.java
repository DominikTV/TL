package com.philipp_mandler.android.vtpl;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.View;

public class MainActivity extends FragmentActivity {

	// Arrow designed by Jardson Araújo from The Noun Project
	
	ViewPager m_viewPager;
	
	private DeviceSize m_deviceSize = DeviceSize.single;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		if(findViewById(R.id.main_single) == null)
			m_deviceSize = DeviceSize.multi;
		
		if(m_deviceSize == DeviceSize.multi) {
			
		}
		else {
			
		}
	}
	
	@Override
	public void onBackPressed() {
		if(findViewById(R.id.frameLayout3).getVisibility() == View.VISIBLE) {
			findViewById(R.id.frameLayout3).setVisibility(View.INVISIBLE);
		}
		else
			super.onBackPressed();
	}
}
