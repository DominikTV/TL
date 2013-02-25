package com.philipp_mandler.mes.vtpl;

public class VtplEntry {
	private String m_date;
	private String m_dayOfWeek;
	private String m_lesson;
	private String m_teacher;
	private String m_room;
	private String m_schoolClass;
	private String m_supplyTeacher;
	private String m_supplyRoom;
	private String m_attribute;
	private String m_info;	
	
	public VtplEntry(String date, String dayOfWeek, String lesson, String teacher, String room, String schoolClass, String supplyTeacher, String supplyRoom, String attribute, String info) {
		m_date = date;
		m_dayOfWeek = dayOfWeek;
		m_lesson = lesson;
		m_teacher = teacher;
		m_room = room;
		m_schoolClass = schoolClass;
		m_supplyTeacher = supplyTeacher;
		m_attribute = attribute;
		m_info = info;
	}
	
	public VtplEntry() {
		
	}
	
	public String toString() {
		return m_date + " " + m_dayOfWeek + " " + m_lesson + " " + m_teacher + " " + m_room + " " + m_schoolClass + " " + m_supplyTeacher + " " + m_supplyRoom + " " + m_attribute + " " + m_info;
	}

	public String getDate() {
		return m_date;
	}

	public void setDate(String m_date) {
		this.m_date = m_date;
	}

	public String getDayOfWeek() {
		return m_dayOfWeek;
	}

	public void setDayOfWeek(String m_dayOfWeek) {
		this.m_dayOfWeek = m_dayOfWeek;
	}

	public String getLesson() {
		return m_lesson;
	}

	public void setLesson(String m_lesson) {
		this.m_lesson = m_lesson;
	}

	public String getTeacher() {
		return m_teacher;
	}

	public void setTeacher(String m_teacher) {
		this.m_teacher = m_teacher;
	}

	public String getRoom() {
		return m_room;
	}

	public void setRoom(String m_room) {
		this.m_room = m_room;
	}

	public String getSchoolClass() {
		return m_schoolClass;
	}

	public void setSchoolClass(String m_schoolClass) {
		this.m_schoolClass = m_schoolClass;
	}

	public String getSupplyTeacher() {
		return m_supplyTeacher;
	}

	public void setSupplyTeacher(String m_supplyTeacher) {
		this.m_supplyTeacher = m_supplyTeacher;
	}

	public String getSupplyRoom() {
		return m_supplyRoom;
	}

	public void setSupplyRoom(String m_supplyRoom) {
		this.m_supplyRoom = m_supplyRoom;
	}

	public String getAttribute() {
		return m_attribute;
	}

	public void setAttribute(String m_attribute) {
		this.m_attribute = m_attribute;
	}

	public String getInfo() {
		return m_info;
	}

	public void setInfo(String m_info) {
		this.m_info = m_info;
	}
	
	
}
