package com.example.myanotherapp;

import android.util.Log;
import java.io.*;

public class BluetoothLEAdvIndParser {

	public static final short GAP_ADTYPE_FLAGS = 0x01; // Flags Bluetooth Core Specification:
	//Vol. 3, Part C, section 8.1.3 (v2.1 + EDR, 3.0 + HS and 4.0)
	//Vol. 3, Part C, sections 11.1.3 and 18.1 (v4.0)
	//Core Specification Supplement, Part A, section 1.3

	public static final short GAP_ADTYPE_16BIT_MORE = 0x02;	// Incomplete List of 16-bit Service Class UUIDs	Bluetooth Core Specification:
	// Vol. 3, Part C, section 8.1.1 (v2.1 + EDR, 3.0 + HS and 4.0)
	// Vol. 3, Part C, sections 11.1.1 and 18.2 (v4.0)
	// Core Specification Supplement, Part A, section 1.1

	public static final short GAP_ADTYPE_LOCAL_NAME_COMPLETE  = 0x09;

	public static final short GAP_ADTYPE_MANUFACTURER_SPECIFIC = 0xff;//	Manufacturer Specific Data

	public class GAP_ADTYPE_FLAGS_TYPES {
		public static final short GAP_ADTYPE_FLAGS_LIMITED = 0x01; //<  Discovery Mode: LE Limited Discoverable Mode.
		public static final short GAP_ADTYPE_FLAGS_GENERAL = 0x02; //<  Discovery Mode: LE General Discoverable Mode.
		public static final short GAP_ADTYPE_FLAGS_BREDR_NOT_SUPPORTED = 0x04; //<  Discovery Mode: BR/EDR Not Supported. 
	}

	private static String bytesToHex(byte[] in) {
		final StringBuilder builder = new StringBuilder();
		for(byte b : in) {
			builder.append(String.format("%02x", b));
		}
		return builder.toString();
	}

	private static boolean parseAdvIndEntry(byte[] arrayData, AdvIndData anEntry) {
		try {
			switch ((short)arrayData[0]) {
			case GAP_ADTYPE_FLAGS:
				//Log.d("GAP_ADTYPE_FLAGS", bytesToHex(java.util.Arrays.copyOfRange(arrayData, 1, arrayData.length-1)));
				break;
			case GAP_ADTYPE_16BIT_MORE:
				//Log.d("GAP_ADTYPE_16BIT_MORE", bytesToHex(arrayData));
				break;
			case GAP_ADTYPE_LOCAL_NAME_COMPLETE:
				anEntry.setAnnouncedName(new String(java.util.Arrays.copyOfRange(arrayData, 1, arrayData.length-1), "US-ASCII"));
				//Log.d("GAP_ADTYPE_LOCAL_NAME_COMPLETE" + " ", anEntry.getAnnouncedName());
				break;
			default:
				//Log.d("parseAdvIndEntry: " + arrayData.length + " ", bytesToHex(arrayData));
				break;
			}
		}
		catch (UnsupportedEncodingException ex) {
			System.err.println(ex.toString());
		}
		return false;
	}

	public static boolean parseAdvInd(byte[] arrayData, AdvIndData anEntry) {
		int idx = 0;
		try {
			while (idx < arrayData.length) {
				int length = arrayData[idx++];
				if (length > 0)
				{
					parseAdvIndEntry(java.util.Arrays.copyOfRange(arrayData, idx, idx + length), anEntry);
					idx += length;
				}
				else
				{
					break;
				}
			}
		}
		catch (ArrayIndexOutOfBoundsException ex) {
			System.err.println(ex.toString());
		}
		//Log.d("GAP_ADTYPE_LOCAL_NAME_COMPLETE" + " ", anEntry.getAnnouncedName());
		return false;
	}
}
