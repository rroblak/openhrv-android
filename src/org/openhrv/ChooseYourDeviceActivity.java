package org.openhrv;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class ChooseYourDeviceActivity extends Activity {

	private SimpleAdapter adapter;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_your_device);
        
        loadDevicesIntoList();
    }

    private void loadDevicesIntoList() {
        List<Map<String, String>> myListData = getBluetoothLeDevices();
        
        adapter = new SimpleAdapter(this,
									myListData,
									android.R.layout.simple_list_item_2,
									new String[]{"device_name", "mac_id"},
									new int[]{android.R.id.text1, android.R.id.text2});

        ListView listView = (ListView) findViewById(R.id.device_list);
        listView.setAdapter(adapter);
    }
    
    private List<Map<String, String>> getBluetoothLeDevices() {
    	Map<String, String> device1 = new HashMap<String, String>();
        device1.put("device_name", "JAMBOX by Jawbone");
        device1.put("mac_id", "00:21:3C:73:B9:E7");
        Map<String, String> device2 = new HashMap<String, String>();
        device2.put("device_name", "Polar H7 250F40");
        device2.put("mac_id", "00:22:D0:25:0F:40");
        
        List<Map<String, String>> myListData = new ArrayList<Map<String, String>>();
        myListData.add(device1);
        myListData.add(device2);
        
        return myListData;
    }
    
    private BluetoothAdapter.LeScanCallback mLeScanCallback =
            new BluetoothAdapter.LeScanCallback() {
        @Override
        public void onLeScan(final BluetoothDevice device, int rssi,
                byte[] scanRecord) {
            runOnUiThread(new Runnable() {
               @Override
               public void run() {
                   adapter.addDevice(device);
                   adapter.notifyDataSetChanged();
               }
           });
       }
    };
    
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.choose_your_device, menu);
//        return true;
//    }
    
}
