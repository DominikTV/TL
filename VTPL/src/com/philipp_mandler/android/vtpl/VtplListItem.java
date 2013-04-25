package com.philipp_mandler.android.vtpl;

import android.view.LayoutInflater;
import android.view.View;

public interface VtplListItem {
	public int getViewType();
	public View getView(LayoutInflater inflater, View convertView);
	public VtplEntry getData();
}
