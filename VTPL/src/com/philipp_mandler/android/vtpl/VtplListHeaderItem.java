package com.philipp_mandler.android.vtpl;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.philipp_mandler.android.vtpl.VtplListAdapter.RowType;

public class VtplListHeaderItem implements VtplListItem {

	String m_title;
	
	public VtplListHeaderItem(String title) {
		m_title = title;
	}
	
	public int getViewType() {
		return RowType.HEADER_ITEM.ordinal();
	}

	public View getView(LayoutInflater inflater, View convertView) {
		View view;
		if(convertView == null) {
			view = (View)inflater.inflate(R.layout.list_header, null);
		}
		else {
			view = convertView;
		}

		view.setMinimumHeight(32);
		
		TextView textTitle = (TextView)view.findViewById(R.id.list_header_title);
		textTitle.setText(m_title);
		
		return view;
	}
	
	@Override
	public VtplEntry getData() {
		return null;
	}

}
