package com.philipp_mandler.android.vtpl;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;

public class MainActivity extends FragmentActivity {

	// Arrow designed by Jardson Araújo from The Noun Project
	
	ViewPager m_viewPager;
	
	DeviceSize m_deviceSize = DeviceSize.single;

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


}
