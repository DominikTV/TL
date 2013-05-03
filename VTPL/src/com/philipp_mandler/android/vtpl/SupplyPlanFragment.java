package com.philipp_mandler.android.vtpl;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Connection.Method;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.philipp_mandler.android.vtpl.VtplListAdapter.RowType;

public class SupplyPlanFragment extends ListFragment {
	
	private List<VtplListItem> m_vtplEntries = new ArrayList<VtplListItem>();
	private LayoutInflater m_inflater;
	VtplDetailFragment m_detailFragment;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {		
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		setHasOptionsMenu(true);
		
		m_vtplEntries = new ArrayList<VtplListItem>();
		
		VtplListAdapter adapter = new VtplListAdapter(inflater.getContext(), m_vtplEntries);		
		setListAdapter(adapter);
		
		m_inflater = inflater;
		
		return super.onCreateView(inflater, container, savedInstanceState);
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		try {				
			ObjectInputStream inputStream = new ObjectInputStream(getActivity().openFileInput("data.bin"));
			Object object = inputStream.readObject();
			if(object instanceof SaveData) {
				m_vtplEntries.clear();
				Date lastDay = null;
				for(VtplEntry dataEntry : ((SaveData)object).getData()) {
					if(lastDay == null || !lastDay.equals(dataEntry.getDate())) {
						m_vtplEntries.add(new VtplListHeaderItem(weekdayToString(dataEntry.getDate().getWeekday()) + ", " + dataEntry.getDate().getDay() + "." + dataEntry.getDate().getMonth() + "."));
					}
					lastDay = dataEntry.getDate();
					m_vtplEntries.add(new VtplListContentItem(dataEntry));
				}
				VtplListAdapter adapter = new VtplListAdapter(m_inflater.getContext(), m_vtplEntries);
				setListAdapter(adapter);
			}
			inputStream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(!Data.updated) {
			GetVtplData dataRequest = new GetVtplData();
			dataRequest.execute("http://www.fricke-consult.de/php/MES_VertretungsplanL.php");
			Data.updated = true;
		}
		super.onViewCreated(view, savedInstanceState);
	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.main, menu);
		super.onCreateOptionsMenu(menu, inflater);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {		
		if(item.getItemId() == R.id.action_refresh) {
			GetVtplData dataRequest = new GetVtplData();
			dataRequest.execute("http://www.fricke-consult.de/php/MES_VertretungsplanL.php");
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		if(l.getAdapter().getItemViewType(position) == RowType.LIST_ITEM.ordinal()) {
			if(getActivity().findViewById(R.id.main_single) == null) {
				m_detailFragment = new VtplDetailFragment();	
				m_detailFragment.setData(m_vtplEntries.get(position).getData());
				FragmentTransaction trans = getFragmentManager().beginTransaction();
				trans.replace(R.id.detailviewPlaceholder, m_detailFragment);
				trans.commit();
			}
			else {
				m_detailFragment = new VtplDetailFragment();	
				m_detailFragment.setData(m_vtplEntries.get(position).getData());
				FragmentTransaction trans = getFragmentManager().beginTransaction();
				trans.replace(R.id.detailviewPlaceholder, m_detailFragment);
				trans.setTransition(FragmentTransaction.TRANSIT_NONE);
				trans.commit();	
				getActivity().findViewById(R.id.frameLayout3).setVisibility(View.VISIBLE);
			}
		}		
		super.onListItemClick(l, v, position, id);
	}
	
	public class GetVtplData extends AsyncTask<String, Void, Document> {
    	
    	@Override
    	protected Document doInBackground(String... arg0) {    	        
    		try {
    			return Jsoup.connect(arg0[0]).data("p", "vtpl").method(Method.POST).execute().parse();  
    		} catch (IOException e) {
    			e.printStackTrace();
			}
    		return null;
    	}
    	
    	@Override
    	protected void onPostExecute(Document doc) {
    		if(doc == null) {
    			Toast.makeText(getActivity(), R.string.toast_plan_update_failed, Toast.LENGTH_LONG).show();
    			return;
    		}
    		
    		Elements elements = doc.select("tr");
    		List<VtplEntry> dataList = new ArrayList<VtplEntry>();
    		
    		String lastSchoolClass = "-";
    		String lastLesson = "";
    		for(Element element : elements) {
    			Elements tdElements = element.select("td");
    			Element data;
    			VtplEntry entry = new VtplEntry();
    			// parse day
    			if(tdElements.size() >= 10 && (data = tdElements.get(0)) != null) {
    				if(!data.html().contains("<b>")) {
	    				if(data.html().contains("&nbsp;")) {
	    					if(dataList.size() != 0)
	    						entry.setDate(dataList.get(dataList.size() - 1).getDate());
	    				}
	    				else {
	    					lastSchoolClass = "-";
	    					lastLesson = "";
	    					try {
								entry.setDate(new Date(data.text()));
							} catch (Exception e) {
								e.printStackTrace();
							}
	    				}
						
						// parse lesson
						if((data = tdElements.get(2)) != null) {
							if(data.html().contains("&nbsp;")) {
								entry.setLesson(lastLesson);
		    				}
							else {
								entry.setLesson(data.text());
								lastLesson = data.text();
							}
						}
						
						// parse teacher
						if((data = tdElements.get(3)) != null) {
							entry.setTeacher(data.text());			
						}
						
						// parse room
						if((data = tdElements.get(4)) != null) {
							entry.setRoom(data.text());
						}
						
						// parse class
						if((data = tdElements.get(5)) != null) {
							if(data.html().contains("&nbsp;"))
								entry.setSchoolClass(lastSchoolClass);
		    				else {
		    					entry.setSchoolClass(data.text());
		    					lastSchoolClass = data.text();
		    				}
						}
						
						// parse supply teacher		    			
						if((data = tdElements.get(6)) != null) {
							entry.setSupplyTeacher(data.text());
						}
						
						// parse supply room		    			
						if((data = tdElements.get(7)) != null) {
							entry.setSupplyRoom(data.text());
						}
						
						// parse attribute		    			
						if((data = tdElements.get(8)) != null) {
							entry.setAttribute(data.text());
						}
						
						// parse info		    			
						if((data = tdElements.get(9)) != null) {
							entry.setInfo(data.text());
						}
		    			
		    			dataList.add(entry);
    				}
    				
    				m_vtplEntries.clear();
    				
    				Date lastDay = null;
    				for(VtplEntry dataEntry : dataList) {
    					if(lastDay == null || !lastDay.equals(dataEntry.getDate())) {
    						m_vtplEntries.add(new VtplListHeaderItem(weekdayToString(dataEntry.getDate().getWeekday()) + ", " + dataEntry.getDate().getDay() + "." + dataEntry.getDate().getMonth() + "."));
    					}
    					lastDay = dataEntry.getDate();
    					m_vtplEntries.add(new VtplListContentItem(dataEntry));
    				}
					try {
						ObjectOutputStream outputStream = new ObjectOutputStream(getActivity().openFileOutput("data.bin", Context.MODE_PRIVATE));
						SaveData saveData = new SaveData(dataList);
						outputStream.writeObject(saveData);
						outputStream.close();
					} catch (Exception e) {
						e.printStackTrace();
					}    				
    			}
    		}
    		VtplListAdapter adapter = new VtplListAdapter(m_inflater.getContext(), m_vtplEntries);
			setListAdapter(adapter);
			
			Toast.makeText(getActivity(), R.string.toast_plan_updated, Toast.LENGTH_SHORT).show();
    	}
	}
	
	private String weekdayToString(Weekday day) {
		String weekday;		
		switch(day) {
		case Monday: weekday = getString(R.string.weekday_monday); break;
		case Tuesday: weekday = getString(R.string.weekday_tuesday); break;
		case Wednesday: weekday = getString(R.string.weekday_wednesday); break;
		case Thursday: weekday = getString(R.string.weekday_thursday); break;
		case Friday: weekday = getString(R.string.weekday_friday); break;
		case Saturday: weekday = getString(R.string.weekday_saturday); break;
		case Sunday: weekday = getString(R.string.weekday_sunday); break;
		default: weekday = ""; break;
		}		
		return weekday;
	}
}
