package com.philipp_mandler.android.vtpl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Connection.Method;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

public class SupplyPlanFragment extends ListFragment {
	
	private List<VtplListItem> m_vtplEntries;
	private LayoutInflater m_inflater;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		GetVtplData dataRequest = new GetVtplData();
		dataRequest.execute("http://www.fricke-consult.de/php/MES_VertretungsplanL.php");
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
	    				else
	    					entry.setDate(data.text());
	    				
    			
						// parse day of week
						if((data = tdElements.get(1)) != null) {  	
							if(data.html().contains("&nbsp;")) {
		    					if(dataList.size() != 0)
		    						entry.setDayOfWeek(dataList.get(dataList.size() - 1).getDayOfWeek());
		    				}
		    				else
		    					entry.setDayOfWeek(data.text());
						}
						
						// parse lesson
						if((data = tdElements.get(2)) != null) {
							if(data.html().contains("&nbsp;")) {
		    					if(dataList.size() != 0)
		    						entry.setLesson(dataList.get(dataList.size() - 1).getLesson());
		    				}
							else
								entry.setLesson(data.text());
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
							if(data.html().contains("&nbsp;")) {
		    					if(dataList.size() != 0)
		    						entry.setSchoolClass(dataList.get(dataList.size() - 1).getSchoolClass());
		    				}
		    				else
		    					entry.setSchoolClass(data.text());
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
    				
    				String lastDay = "";
    				for(VtplEntry dataEntry : dataList) {
    					if(!lastDay.equals(dataEntry.getDate())) {
    						m_vtplEntries.add(new VtplListHeaderItem(dataEntry.getDate()));
    					}
    					lastDay = dataEntry.getDate();
    					m_vtplEntries.add(new VtplListContentItem(dataEntry));
    				}
    			}
    		}
    		VtplListAdapter adapter = new VtplListAdapter(m_inflater.getContext(), m_vtplEntries);
			setListAdapter(adapter);
			
			Toast.makeText(getActivity(), R.string.toast_plan_updated, Toast.LENGTH_SHORT).show();
    	}
	}
}
