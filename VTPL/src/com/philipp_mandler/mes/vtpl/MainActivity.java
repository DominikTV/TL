package com.philipp_mandler.mes.vtpl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
    
    public void onButtonClick(View v) {
    	GetVtplData dataRequest = new GetVtplData();
    	dataRequest.execute("http://www.fricke-consult.de/php/MES_VertretungsplanL.php");
    }    
    
    public class GetVtplData extends AsyncTask<String, Void, String> {
    	
    	@Override
    	protected String doInBackground(String... arg0) {
        	HttpClient httpclient = new DefaultHttpClient();
        	HttpPost httpPost = new HttpPost(arg0[0]);
            HttpResponse response;
    		try {
    			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
    	        nameValuePairs.add(new BasicNameValuePair("p", "vtpl"));
    	        httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
    	        
    			response = httpclient.execute(httpPost);
    			StatusLine statusLine = response.getStatusLine();
    	        if(statusLine.getStatusCode() == HttpStatus.SC_OK){
    	            ByteArrayOutputStream out = new ByteArrayOutputStream();
    	            response.getEntity().writeTo(out);
    	            out.close();
    	            return out.toString();
    	        } else{
    	            //Closes the connection.
    	            response.getEntity().getContent().close();
    	            throw new IOException(statusLine.getReasonPhrase());
    	        }
    		} catch (ClientProtocolException e) {
    			e.printStackTrace();
    		} catch (IOException e) {
    			e.printStackTrace();
    		}	
    		
    		return null;
    	}
    	
    	@Override
    	protected void onPostExecute(String result) {
    		TableLayout resultView = (TableLayout)findViewById(R.id.table);
    		resultView.removeAllViews();
    		
    		Document doc = Jsoup.parse(result);
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
    			}
    		}
    		
    		int padding = 10;
    		
    		TableRow headerRow = new TableRow(getApplicationContext());
    		
    		TableRow.LayoutParams rowLayoutParams = new TableRow.LayoutParams();
    		rowLayoutParams.width = TableRow.LayoutParams.FILL_PARENT;
			rowLayoutParams.weight = 1;
    		
			TextView column_date = new TextView(getApplicationContext());
			column_date.setText("Datum");
			column_date.setPadding(0, padding, padding, 0);
			column_date.setLayoutParams(rowLayoutParams);
			headerRow.addView(column_date);
			
			TextView column_dayOfWeek = new TextView(getApplicationContext());
			column_dayOfWeek.setText("Tag");
			column_dayOfWeek.setPadding(0, padding, padding, 0);
			column_date.setLayoutParams(rowLayoutParams);
			headerRow.addView(column_dayOfWeek);
			
			TextView column_lesson = new TextView(getApplicationContext());
			column_lesson.setText("Stunde");
			column_lesson.setPadding(0, padding, padding, 0);
			column_lesson.setLayoutParams(rowLayoutParams);
			headerRow.addView(column_lesson);
			
			TextView column_teacher = new TextView(getApplicationContext());
			column_teacher.setText("Lehrer");
			column_teacher.setPadding(0, padding, padding, 0);
			column_teacher.setLayoutParams(rowLayoutParams);
			headerRow.addView(column_teacher);
			
			TextView column_room = new TextView(getApplicationContext());
			column_room.setText("Raum");
			column_room.setPadding(0, padding, padding, 0);
			column_room.setLayoutParams(rowLayoutParams);
			headerRow.addView(column_room);
			
			TextView column_schoolClass = new TextView(getApplicationContext());
			column_schoolClass.setText("Kurs");
			column_schoolClass.setPadding(0, padding, padding, 0);
			column_schoolClass.setLayoutParams(rowLayoutParams);
			headerRow.addView(column_schoolClass);
			
			TextView column_supplyTeacher = new TextView(getApplicationContext());
			column_supplyTeacher.setText("Vertr. Lehrer");
			column_supplyTeacher.setPadding(0, padding, padding, 0);
			column_supplyTeacher.setLayoutParams(rowLayoutParams);
			headerRow.addView(column_supplyTeacher);
			
			TextView column_supplyRoom = new TextView(getApplicationContext());
			column_supplyRoom.setText("Vertr. Raum");
			column_supplyRoom.setPadding(0, padding, padding, 0);
			column_supplyRoom.setLayoutParams(rowLayoutParams);
			headerRow.addView(column_supplyRoom);
			
			TextView column_attribute = new TextView(getApplicationContext());
			column_attribute.setText("Merkmal");
			column_attribute.setPadding(0, padding, padding, 0);
			column_attribute.setLayoutParams(rowLayoutParams);
			headerRow.addView(column_attribute);
			
			TextView column_info = new TextView(getApplicationContext());
			column_info.setText("Info");
			column_info.setPadding(0, padding, padding, 0);
			column_info.setLayoutParams(rowLayoutParams);
			headerRow.addView(column_info);   			
			
			resultView.addView(headerRow);
    	
    		for(VtplEntry out : dataList) {
    			TableRow row = new TableRow(getApplicationContext());
    			
    			column_date = new TextView(getApplicationContext());
    			column_date.setText(out.getDate());
    			column_date.setPadding(0, padding, padding, 0);
    			row.addView(column_date);
    			
    			column_dayOfWeek = new TextView(getApplicationContext());
    			column_dayOfWeek.setText(out.getDayOfWeek());
    			column_dayOfWeek.setPadding(0, padding, padding, 0);
    			row.addView(column_dayOfWeek);
    			
    			column_lesson = new TextView(getApplicationContext());
    			column_lesson.setText(out.getLesson());
    			column_lesson.setPadding(0, padding, padding, 0);
    			row.addView(column_lesson);
    			
    			column_teacher = new TextView(getApplicationContext());
    			column_teacher.setText(out.getTeacher());
    			column_teacher.setPadding(0, padding, padding, 0);
    			row.addView(column_teacher);
    			
    			column_room = new TextView(getApplicationContext());
    			column_room.setText(out.getRoom());
    			column_room.setPadding(0, padding, padding, 0);
    			row.addView(column_room);
    			
    			column_schoolClass = new TextView(getApplicationContext());
    			column_schoolClass.setText(out.getSchoolClass());
    			column_schoolClass.setPadding(0, padding, padding, 0);
    			row.addView(column_schoolClass);
    			
    			column_supplyTeacher = new TextView(getApplicationContext());
    			column_supplyTeacher.setText(out.getSupplyTeacher());
    			column_supplyTeacher.setPadding(0, padding, padding, 0);
    			row.addView(column_supplyTeacher);
    			
    			column_supplyRoom = new TextView(getApplicationContext());
    			column_supplyRoom.setText(out.getSupplyRoom());
    			column_supplyRoom.setPadding(0, padding, padding, 0);
    			row.addView(column_supplyRoom);
    			
    			column_attribute = new TextView(getApplicationContext());
    			column_attribute.setText(out.getAttribute());
    			column_attribute.setPadding(0, padding, padding, 0);
    			row.addView(column_attribute);
    			
    			column_info = new TextView(getApplicationContext());
    			column_info.setText(out.getInfo());
    			column_info.setPadding(0, padding, padding, 0);
    			row.addView(column_info);   			
    			
    			resultView.addView(row);
    		}
    	}

    }
}
