package com.example.myanotherapp;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

/**
 * 
 * @author Tomek
 *
 */
public class MainActivity extends Activity
implements BluetoothAdapter.LeScanCallback {

	/** 
	 * 
	 * @author 
	 *
	 */
	public enum SCAN_STATUS {
		STANDBY, //< In standby 
		SCANNING //< Doing scanning
	}
	public final static String EXTRA_MESSAGE = "com.example.myanotherapp.EXTRA_MESSAGE";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		BluetoothManager bluetoothManager = (BluetoothManager )getSystemService(BLUETOOTH_SERVICE);
		mBluetoothAdapter = bluetoothManager.getAdapter();
		mRSSIControl = (EditText)findViewById(R.id.editText1);
		mAnnouncedName = (EditText)findViewById(R.id.editText3);
		mScanOnOffSwitch = (Switch)findViewById(R.id.switch1);

		if (null == mScanOnOffSwitch) {
			Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
			finish();
			return;
		}
		// Install handler for switch toggle
		mScanOnOffSwitch.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				MainActivity mainAct = (MainActivity)buttonView.getContext();
				if (isChecked)	{
					mainAct.startScanning();
				}
				else {
					mainAct.stopScanning();
				}
			}
		});
	}

	@Override
	protected void onResume() {
		super.onResume();
		mScanOnOffSwitch.setActivated(false);

		if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) 	{
			Toast.makeText(this,  "No LE support", Toast.LENGTH_SHORT).show();
			finish();
			return;
		}

		if (mBluetoothAdapter == null || !mBluetoothAdapter.isEnabled()) {
			//Bluetooth is disabled
			Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
			startActivity(enableBtIntent);
			finish();
			return;
		}
		mScanOnOffSwitch.setActivated(false);
	}

	/**
	 * @brief
	 */
	protected void onPause() {
		super.onPause();
		stopScanning();
	}

	/**
	 * @brief
	 * @param
	 * @param
	 * @param
	 */
	public void onLeScan(BluetoothDevice aDevice, int rssi, byte[] scanRecord)	{
		final byte[] scanRecordCopy = scanRecord;
		final int rssiCopy = rssi;
		this.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				AdvIndData anEntry = new AdvIndData();
				BluetoothLEAdvIndParser.parseAdvInd(scanRecordCopy, anEntry);
				mRSSIControl.setText(Integer.toString(rssiCopy));
				mAnnouncedName.setText(anEntry.getAnnouncedName());
			}
		});
	}

	/**
	 * 
	 * @return
	 */
	private boolean startScanning()	{
		boolean bRetVal = false;
		switch (mScanStatus) {
		case STANDBY:
			bRetVal = mBluetoothAdapter.startLeScan(this);
			if (bRetVal) {
				mScanStatus = SCAN_STATUS.SCANNING;
			}
			Log.d(Thread.currentThread().getStackTrace()[0].getMethodName(), mScanStatus.toString());
			break;
		case SCANNING:	
		default:
			break;
		}
		return bRetVal;
	}

	/**
	 * 
	 * @return
	 */
	private boolean stopScanning()	{
		switch (mScanStatus) {
		case SCANNING:
			mBluetoothAdapter.stopLeScan(this);
			mScanStatus = SCAN_STATUS.STANDBY;
			Log.d(Thread.currentThread().getStackTrace()[0].getMethodName(), mScanStatus.toString());
			return true;
		case STANDBY:
		default:
			return false;
		}
	}

	private SCAN_STATUS mScanStatus = SCAN_STATUS.STANDBY;
	private Switch mScanOnOffSwitch;
	private EditText mRSSIControl;
	private EditText mAnnouncedName;
	private BluetoothAdapter mBluetoothAdapter; 
}