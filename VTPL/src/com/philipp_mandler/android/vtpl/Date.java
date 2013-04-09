package com.philipp_mandler.android.vtpl;

public class Date {
	
	private int m_day;
	private int m_month;
	private int m_year;
	private Weekday m_weekday;
	
	
	public Date() {
		m_day = 0;
		m_month = 0;
		m_year = 0;
		setWeekday(Weekday.Monday);
	}
	
	public Date(String date) throws Exception {
		String[] parsed = date.split("\\.");
		if(parsed.length == 3) {
			if(Integer.getInteger(parsed[0]) == null || Integer.getInteger(parsed[1]) == null || Integer.getInteger(parsed[2])== null) {
				throw new Exception("String wrong formated.");
			}
			
			m_day = Integer.getInteger(parsed[0]);
			m_month = Integer.getInteger(parsed[1]);
			m_year = Integer.getInteger(parsed[2]);
			calculateWeekday();
		}
		
	}
	
	public Date(int day, int month, int year) {
		m_day = day;
		m_month = month;
		m_year = year;
		calculateWeekday();		
	}
	
	public int getday() {
		return m_day;
	}
	public void setday(int day) {
		m_day = day;
		calculateWeekday();
	}
	
	public int getmonth() {
		return m_month;
	}
	public void setmonth(int month) {
		m_month = month;
		calculateWeekday();
	}
	
	public int getyear() {
		return m_year;
	}
	public void setyear(int year) {
		m_year = year;
		calculateWeekday();
	}

	public Weekday getWeekday() {
		return m_weekday;
	}
	public void setWeekday(Weekday weekday) {
		m_weekday = weekday;
	}

	public void setDate(String date) throws Exception {
		String[] parsed = date.split("\\.");
		if(parsed.length == 3) {
			if(Integer.getInteger(parsed[0]) == null || Integer.getInteger(parsed[1]) == null || Integer.getInteger(parsed[2])== null) {
				throw new Exception("String wrong formated.");
			}
			
			m_day = Integer.getInteger(parsed[0]);
			m_month = Integer.getInteger(parsed[1]);
			m_year = Integer.getInteger(parsed[2]);
			calculateWeekday();
		}
		
	}
	public String getDate() {
		String date;
		date = m_day + "." + m_month + "." + m_year;
		
		return date;
	}

	private void calculateWeekday() {
		
		int month, year=m_year;
		 
		if(m_month<=2)
			{month=m_month+10;
			year=m_year-1;}
		else
			month=m_month-2;
 
		int c = year/100;
		int y = year%100;
 
		int h = (((26*month-2)/10)+m_day+y+y/4+c/4-2*c)%7;
 
		if(h<0)
			h=h+7;
 
		switch(h)
		{
		case 0 : setWeekday(Weekday.Monday);	//Sonntag
				break;
		case 1 : setWeekday(Weekday.Monday);
		break;
		case 2 : setWeekday(Weekday.Tuesday);
		break;
		case 3 : setWeekday(Weekday.Wednesday);
		break;
		case 4 : setWeekday(Weekday.Thursday);
		break;
		case 5 : setWeekday(Weekday.Friday);
		break;
		case 6 : setWeekday(Weekday.Monday);	//Samstag
		break;     
		}
		
	}
}
