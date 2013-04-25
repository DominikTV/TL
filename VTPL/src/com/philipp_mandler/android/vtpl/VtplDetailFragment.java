package com.philipp_mandler.android.vtpl;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class VtplDetailFragment extends Fragment {
	
	private boolean m_created = false;
	
	VtplEntry m_data;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_detail, container, false);
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		m_created = true;
		if(m_data != null)
			showData();
		super.onViewCreated(view, savedInstanceState);
	}
	
	public void setData(VtplEntry data) {
		m_data = data;
		if(m_created) {
			showData();
		}
	}
	
	private void showData() {
		((TextView)getActivity().findViewById(R.id.schoolClass)).setText(m_data.getSchoolClass());
		((TextView)getActivity().findViewById(R.id.date)).setText(m_data.getDate());
		((TextView)getActivity().findViewById(R.id.lesson)).setText(m_data.getLesson());
		((TextView)getActivity().findViewById(R.id.teacher)).setText(m_data.getTeacher());
		((TextView)getActivity().findViewById(R.id.supplyTeacher)).setText(m_data.getSupplyTeacher());
		((TextView)getActivity().findViewById(R.id.room)).setText(m_data.getRoom());
		((TextView)getActivity().findViewById(R.id.supplyRoom)).setText(m_data.getSupplyRoom());
		((TextView)getActivity().findViewById(R.id.info)).setText(m_data.getInfo());
	}
}
