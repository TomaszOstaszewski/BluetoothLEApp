package com.example.myanotherapp;

public class AdvIndData {
	private int mRssi;
	private String mAnnouncedName;
	
	public AdvIndData(int rssi, String announcedName) {
		mRssi = rssi;
		mAnnouncedName = announcedName;
	}

	public AdvIndData() {
		mRssi = -255;
		mAnnouncedName = "";
	}

	public String getAnnouncedName() { return mAnnouncedName; }
	public int getRSSI() { return mRssi; }
		
	public void setAnnouncedName(String name) { mAnnouncedName = name; }
	public void setRSSI(int rssi) { mRssi = rssi; }

}
