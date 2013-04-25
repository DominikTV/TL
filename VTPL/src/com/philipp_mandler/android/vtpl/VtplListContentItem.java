package com.philipp_mandler.android.vtpl;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.philipp_mandler.android.vtpl.VtplListAdapter.RowType;

public class VtplListContentItem implements VtplListItem {
	
	private VtplEntry m_data;

	public VtplListContentItem(VtplEntry data) {
		m_data = data;
	}
	
	@Override
	public int getViewType() {
		return RowType.LIST_ITEM.ordinal();
	}

	@Override
	public View getView(LayoutInflater inflater, View convertView) {
		View view;
		if(convertView == null) {
			view = (View)inflater.inflate(R.layout.list_item, null);
		}
		else {
			view = convertView;
		}
		
		view.setMinimumHeight(64);
		
		TextView textClass = (TextView)view.findViewById(R.id.text_class);
		TextView textLesson = (TextView)view.findViewById(R.id.text_lesson);
		TextView textRoom = (TextView)view.findViewById(R.id.text_room);
		TextView textSupplyRoom = (TextView)view.findViewById(R.id.text_supplyRoom);
		TextView textSupplyTeacher = (TextView)view.findViewById(R.id.text_supplyTeacher);
		TextView textTeacher = (TextView)view.findViewById(R.id.text_teacher);
		
		textClass.setText(m_data.getSchoolClass());
		textLesson.setText(m_data.getLesson() + ". Stunde");
		textRoom.setText(m_data.getRoom());
		textSupplyRoom.setText(m_data.getSupplyRoom());
		textSupplyTeacher.setText(m_data.getSupplyTeacher());
		textTeacher.setText(m_data.getTeacher());
		
		return view;
	}
	
	@Override
	public VtplEntry getData() {
		return m_data;
	}

}
