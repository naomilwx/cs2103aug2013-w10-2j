package nailit.common;

import org.joda.time.DateTime;

import nailit.common.NIConstants;

public class Reminder {
	private int ID;
	private DateTime date;
	private boolean isDeleted;
	public Reminder(int ID,DateTime date){
		this.ID = ID;
		this.date = date;
		this.isDeleted = false;
	}
	
	public int getID(){
		return ID;
	}
	public void delete(){
		if(this.isDeleted){
			
		}
		this.isDeleted = true;
	}
	public void recover(){
		if(isDeleted){
			isDeleted = false;
		}
	}
	public String changeToDiskFormat(){
		return "" + ID + NIConstants.NORMAL_FIELD_SPLITTER + date.toString();
	}
}
