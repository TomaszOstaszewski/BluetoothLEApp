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

public class MainActivity extends Activity
implements BluetoothAdapter.LeScanCallback {

	public enum SCAN_STATUS {
		STANDBY,
		SCANNING
	}
	public final static String EXTRA_MESSAGE = "com.example.myanotherapp.EXTRA_MESSAGE";

	//private SparseArray<BluetoothDevice> mDevices;
	private SCAN_STATUS mScanStatus = SCAN_STATUS.STANDBY;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		BluetoothManager bluetoothManager = (BluetoothManager )getSystemService(BLUETOOTH_SERVICE);
		mBluetoothAdapter = bluetoothManager.getAdapter();
		mRSSIControl = (EditText)findViewById(R.id.editText1);
		mAnnouncedName = (EditText)findViewById(R.id.editText3);
		mScanOnOffSwitch = (Switch)findViewById(R.id.switch1);
		
		if (null == mScanOnOffSwitch)
		{
			Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
			finish();
			return;
		}
		mScanOnOffSwitch.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				MainActivity mainAct = (MainActivity)buttonView.getContext();
				System.out.println(mainAct.toString());
				if (isChecked)
				{
					mBluetoothAdapter.startLeScan(mainAct);
					mScanStatus = SCAN_STATUS.SCANNING;
				}
				else
				{
					mBluetoothAdapter.stopLeScan(mainAct);
					mScanStatus = SCAN_STATUS.STANDBY;
				}
				System.out.println("status: "+ isChecked);
			}
		});
	}

	@Override
	protected void onResume() {
		super.onResume();
		mScanOnOffSwitch.setActivated(false);

		if (mBluetoothAdapter == null || !mBluetoothAdapter.isEnabled()) {
			//Bluetooth is disabled
			Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
			startActivity(enableBtIntent);
			finish();
			return;
		}
		mScanOnOffSwitch.setActivated(false);

		if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE))
		{
			Toast.makeText(this,  "No LE support", Toast.LENGTH_SHORT).show();
			finish();
			return;
		}

	}

	protected void onPause() {
		super.onPause();
		switch (mScanStatus) {
		case SCANNING:
			mBluetoothAdapter.stopLeScan(this);
			break;
		default:
			break;
		}
	}

	public void sendMessage(View aView)
	{
		mBluetoothAdapter.startLeScan(this);
		mScanStatus = SCAN_STATUS.SCANNING;
	}

	public void onLeScan(BluetoothDevice aDevice, int rssi, byte[] scanRecord)
	{
		final byte[] scanRecordCopy = scanRecord;
		final int rssiCopy = rssi;
		this.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				AdvIndData anEntry = new AdvIndData();
				BluetoothLEAdvIndParser.parseAdvInd(scanRecordCopy, anEntry);
				mRSSIControl.setText(Integer.toString(rssiCopy));
				System.out.println(anEntry.getAnnouncedName());
				mAnnouncedName.setText(anEntry.getAnnouncedName());
			}
		});
	}

	private Switch mScanOnOffSwitch;
	private EditText mRSSIControl;
	private EditText mAnnouncedName;
	private BluetoothAdapter mBluetoothAdapter; 
	EditText mEditText;
}