package emperior.elfe.core;

import java.util.Date;

public class LogFileEntry {

	private EntryType type;
	private Date date;
	private Date time;
	private String value;
	private int indexNumberInAllEntries;
	
	public LogFileEntry(EntryType type, Date date, Date time, String value) {
		this.type = type;
		this.date = date;
		this.time = time;
		this.value = value;
	}
	
	public int getIndexNumberInAllEntries() {
		return indexNumberInAllEntries;
	}
	
	public void setIndexNumberInAllEntries(int index) {
		this.indexNumberInAllEntries = index;
	}
	
	public EntryType getType() {
		return type;
	}
	
	public Date getDate() {
		return date;
	}

	public Date getTime() {
		return time;
	}

	public String getValue() {
		return value;
	}
	
}
