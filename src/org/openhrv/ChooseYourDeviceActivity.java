package org.openhrv;

import java.util.ArrayList;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class ChooseYourDeviceActivity extends Activity {

	private BluetoothAdapter mBluetoothAdapter;
	private LeDeviceListAdapter mLeDeviceListAdapter;
	private Handler mHandler;
	private boolean mScanning;
	
	// Scan for 10 seconds
    private static final long SCAN_PERIOD = 10000;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.activity_choose_your_device);
        
        mBluetoothAdapter = ((BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE)).getAdapter();
        
        mHandler = new Handler();
    }

    @Override
    protected void onResume() {
        super.onResume();

        mLeDeviceListAdapter = new LeDeviceListAdapter();
        
        ListView listView = (ListView) findViewById(R.id.device_list);
        listView.setAdapter(mLeDeviceListAdapter);

        scanLeDevice(true);
    }
    
    private void scanLeDevice(final boolean enable) {
        if (enable) {
            // Stops scanning after a pre-defined scan period.
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    stopScanning();
                    
                    invalidateOptionsMenu();
                }
            }, SCAN_PERIOD);

            startScanning();
        } else {
            stopScanning();
        }
        invalidateOptionsMenu();
    }
    
    private void startScanning() {
    	TextView statusView = (TextView) this.findViewById(R.id.device_list_status);
    	statusView.setText(R.string.searching_for_devices);
    	
    	mScanning = true;
        mBluetoothAdapter.startLeScan(mLeScanCallback);
    }
    
    private void stopScanning() {
    	TextView statusView = (TextView) this.findViewById(R.id.device_list_status);
    	statusView.setText(R.string.devices_detected);
    	
    	mScanning = false;
        mBluetoothAdapter.stopLeScan(mLeScanCallback);
    }
    
    private class LeDeviceListAdapter extends BaseAdapter {
        private ArrayList<BluetoothDevice> mLeDevices;
        private LayoutInflater mInflator;

        public LeDeviceListAdapter() {
            super();
            mLeDevices = new ArrayList<BluetoothDevice>();
            mInflator = ChooseYourDeviceActivity.this.getLayoutInflater();
        }

        public void addDevice(BluetoothDevice device) {
            if(!mLeDevices.contains(device)) {
                mLeDevices.add(device);
            }
        }        

        public BluetoothDevice getDevice(int position) {
            return mLeDevices.get(position);
        }

        public void clear() {
            mLeDevices.clear();
        }

        @Override
        public int getCount() {
            return mLeDevices.size();
        }

        @Override
        public Object getItem(int i) {
            return mLeDevices.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            ViewHolder viewHolder;

            if (view == null) {
                view = mInflator.inflate(android.R.layout.simple_list_item_2, null);
                viewHolder = new ViewHolder();
                viewHolder.deviceName = (TextView) view.findViewById(android.R.id.text1);
                viewHolder.deviceAddress = (TextView) view.findViewById(android.R.id.text2);
                view.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) view.getTag();
            }

            BluetoothDevice device = mLeDevices.get(i);
            final String deviceName = device.getName();
            if (deviceName != null && deviceName.length() > 0)
                viewHolder.deviceName.setText(deviceName);
            else
                viewHolder.deviceName.setText(R.string.unknown_device);
            viewHolder.deviceAddress.setText(device.getAddress());

            return view;
        }
    }
    
    private BluetoothAdapter.LeScanCallback mLeScanCallback =
            new BluetoothAdapter.LeScanCallback() {

        @Override
        public void onLeScan(final BluetoothDevice device, int rssi, byte[] scanRecord) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mLeDeviceListAdapter.addDevice(device);
                    mLeDeviceListAdapter.notifyDataSetChanged();
                }
            });
        }
    };
    
    static class ViewHolder {
        TextView deviceName;
        TextView deviceAddress;
    }
}
