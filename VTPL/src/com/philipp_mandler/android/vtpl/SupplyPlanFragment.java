package com.philipp_mandler.android.vtpl;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class SupplyPlanFragment extends Fragment {
		
		public SupplyPlanFragment() {
			
		}
		
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_supplyplan, container, false);
			return rootView;
		}
	}