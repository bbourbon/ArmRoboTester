package br.org.cesar.armrobotester;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothClass;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.ParcelUuid;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;

public class DeviceConnectionActivity extends Activity implements View.OnClickListener {


    private static final String TAG = "ArmTester";

    private static final int REQUEST_ENABLE_BT = 1;
    private static final int REQUEST_DISCOVERABLE_BT = 2;

    private static final int REQUEST_BLUETOOTH_PERMISSION = 1024;

    BluetoothAdapter mBluetoothAdapter;
    ArrayAdapter<String> mArrayAdapter;
    Button mButtonFind;
    Button mButtonStop;
    Button mButtonConnect;
    ListView mListViewDevices;
    boolean mFullStopped = false;

    BroadcastReceiver mBluetoothDeviceBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            DeviceConnectionActivity.this.onHandleBtDeviceBroadcast(intent);
        }
    };

    BroadcastReceiver mBluetoothAdapterBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            DeviceConnectionActivity.this.onHandleBtAdapterBroadcast(intent);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_connection);
        mArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);

        mButtonFind = (Button) findViewById(R.id.button_start);
        if (mButtonFind != null) mButtonFind.setOnClickListener(this);
        mButtonStop = (Button) findViewById(R.id.button_stop);
        if (mButtonStop != null) mButtonStop.setOnClickListener(this);
        mButtonConnect = (Button) findViewById(R.id.button_connect);
        if (mButtonConnect != null) mButtonConnect.setOnClickListener(this);
        mListViewDevices = (ListView) findViewById(R.id.listview_devices);

        mListViewDevices.setAdapter(mArrayAdapter);
    }

    @Override
    protected void onPause() {
        super.onPause();

        try {
            if (mBluetoothAdapterBroadcastReceiver != null) {
                unregisterReceiver(mBluetoothAdapterBroadcastReceiver);
            }

            if (mBluetoothDeviceBroadcastReceiver != null) {
                unregisterReceiver(mBluetoothDeviceBroadcastReceiver);
            }
        } catch (Exception ex) {
            Log.e(TAG, "Exception unregisterring receivers", ex);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        boolean permitted = checkPermissions();
        if (permitted) {
            init();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, "onActivityResult");
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_ENABLE_BT) {
            if (resultCode == Activity.RESULT_OK) {
                discoverDevices();
            } else {
                Log.w(TAG, "Bluetooth still disabled! Can't continue.");
            }
        }

        if (requestCode == REQUEST_DISCOVERABLE_BT) {
            if (resultCode != Activity.RESULT_CANCELED) {
                Log.d(TAG, "Discovery time: " + resultCode);
                mBluetoothAdapter.startDiscovery();
            } else {
                Log.w(TAG, "Discoverable not enabled!");
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        boolean full_permission_granted = false;
        if (REQUEST_BLUETOOTH_PERMISSION == requestCode && permissions.length > 0) {
            full_permission_granted = true;
            for (int i = 0; i < permissions.length; i++) {

                boolean result = (grantResults[i] == PackageManager.PERMISSION_GRANTED);
                Log.d(TAG, "Permission:" + permissions[i] + " granted? " + result);
                full_permission_granted = full_permission_granted && (result);
            }
        }

        if (full_permission_granted) {
            init();
        } else {
            checkPermissions();
        }

    }

    @Override
    public void onClick(View v) {
        if (v == null) return;

        if (v.equals(mButtonFind)) {
            mFullStopped = false;
            if (mBluetoothAdapter != null) {
                enableAndDiscovery();
            }
        }

        if (v.equals(mButtonStop)) {
            mFullStopped = true;
            this.fullStopDiscovery();
        }

        if (v.equals(mButtonConnect)) {
            mFullStopped = true;
            //TODO: Connect to selected devices
        }
    }


    /* - PRIVATE METHODS - */
    private void init() {
        Log.d(TAG, "init");

        IntentFilter btDeviceIntentFilter = new IntentFilter(BluetoothDevice.ACTION_ACL_CONNECTED);
        btDeviceIntentFilter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECT_REQUESTED);
        btDeviceIntentFilter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED);
        btDeviceIntentFilter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
        btDeviceIntentFilter.addAction(BluetoothDevice.ACTION_CLASS_CHANGED);
        btDeviceIntentFilter.addAction(BluetoothDevice.ACTION_FOUND);
        btDeviceIntentFilter.addAction(BluetoothDevice.ACTION_NAME_CHANGED);
        btDeviceIntentFilter.addAction(BluetoothDevice.ACTION_PAIRING_REQUEST);
        btDeviceIntentFilter.addAction(BluetoothDevice.ACTION_UUID);

        IntentFilter btAdapterIntentFilter = new IntentFilter(
                BluetoothAdapter.ACTION_CONNECTION_STATE_CHANGED);
        btAdapterIntentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        btAdapterIntentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        btAdapterIntentFilter.addAction(BluetoothAdapter.ACTION_LOCAL_NAME_CHANGED);
        btAdapterIntentFilter.addAction(BluetoothAdapter.ACTION_SCAN_MODE_CHANGED);
        btAdapterIntentFilter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);

        // Register bluetooth broadcast receivers
        registerReceiver(mBluetoothAdapterBroadcastReceiver, btAdapterIntentFilter);
        registerReceiver(mBluetoothDeviceBroadcastReceiver, btDeviceIntentFilter);

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();


    }

    private void enableAndDiscovery() {
        Log.d(TAG, "enableAndDiscovery");
        if (mBluetoothAdapter.isEnabled() == false) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        } else {
            discoverDevices();
        }
    }

    private void discoverDevices() {
        Log.d(TAG, "discoverDevices");
        mArrayAdapter.clear();
        pairedDevices();

        Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        startActivityForResult(enableBtIntent, REQUEST_DISCOVERABLE_BT);

    }

    private void pairedDevices() {
        Log.d(TAG, "pairedDevices");
        Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
        // If there are paired devices
        if (pairedDevices.size() > 0) {
            // Loop through paired devices
            for (BluetoothDevice device : pairedDevices) {
                // Add the name and address to an array adapter to show in a ListView
                String btDevices = device.getName() + "\n" + device.getAddress();
                Log.d(TAG, btDevices);
                mArrayAdapter.add(btDevices);
            }
        }
    }

    private void fullStopDiscovery() {
        if (mBluetoothAdapter != null) {
            if (mBluetoothAdapter.isDiscovering()) {
                mBluetoothAdapter.cancelDiscovery();
            }
        }
    }

    private void onHandleBtDeviceBroadcast(Intent intent) {
        if (intent == null) return;
        String action = intent.getAction();
        Log.d(TAG, "[BT Device action]:" + action);

        // ACL Actions
        if (BluetoothDevice.ACTION_ACL_CONNECTED.equals(action)) {
            BluetoothDevice btDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
            String name = btDevice != null ? btDevice.getName() : "";

            Log.d(TAG, "ACL device connected: " + name);
        }
        if (BluetoothDevice.ACTION_ACL_DISCONNECT_REQUESTED.equals(action)) {
            BluetoothDevice btDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
            String name = btDevice != null ? btDevice.getName() : "";

            Log.d(TAG, "ACL device disconnect requested: " + name);
        }
        if (BluetoothDevice.ACTION_ACL_DISCONNECTED.equals(action)) {
            BluetoothDevice btDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
            String name = btDevice != null ? btDevice.getName() : "";

            Log.d(TAG, "ACL device disconnect requested: " + name);
        }


        if (BluetoothDevice.ACTION_BOND_STATE_CHANGED.equals(action)) {
            BluetoothDevice btDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
            String name = btDevice != null ? btDevice.getName() : "";

            int bond = intent.getIntExtra(BluetoothDevice.EXTRA_BOND_STATE, -1);
            int oldBond = intent.getIntExtra(BluetoothDevice.EXTRA_PREVIOUS_BOND_STATE, -1);
            if (!TextUtils.isEmpty(name) || bond != -1 || oldBond != -1) {
                Log.d(TAG, "[bond]Name: " + name + ", old : " + oldBond + ", new : "
                        + bond);
            }
        }
        if (BluetoothDevice.ACTION_CLASS_CHANGED.equals(action)) {
            BluetoothDevice btDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
            BluetoothClass btClass = intent.getParcelableExtra(BluetoothDevice.EXTRA_CLASS);

            if (btDevice != null && btClass != null) {
                Log.d(TAG, "Device: " + btDevice.getName() + ", class: " +
                        btClass.getDeviceClass());
            }
        }
        if (BluetoothDevice.ACTION_FOUND.equals(action)) {
            BluetoothDevice btDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
            BluetoothClass btClass = intent.getParcelableExtra(BluetoothDevice.EXTRA_CLASS);
            String name = intent.getStringExtra(BluetoothDevice.EXTRA_NAME);
            short invalid = -1;
            short rssi = intent.getShortExtra(BluetoothDevice.EXTRA_RSSI, invalid);

            Log.d(TAG, "Device Found!");
        }
        if (BluetoothDevice.ACTION_NAME_CHANGED.equals(action)) {
            BluetoothDevice btDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
            String name = intent.getStringExtra(BluetoothDevice.EXTRA_NAME);

            Log.d(TAG, "Device name change to: " + name);
        }
        if (BluetoothDevice.ACTION_PAIRING_REQUEST.equals(action)) {
            int key = intent.getIntExtra(BluetoothDevice.EXTRA_PAIRING_KEY, -1);
            int variant = intent.getIntExtra(BluetoothDevice.EXTRA_PAIRING_VARIANT, -1);


            Log.d(TAG, "Pairing request. Key: " + key + ", variant: " + variant);

        }
        if (BluetoothDevice.ACTION_UUID.equals(action)) {
            BluetoothDevice btDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
            ParcelUuid parcelUuid = intent.getParcelableExtra(BluetoothDevice.EXTRA_UUID);

            if (btDevice != null) {
                Log.d(TAG, "Device name: " + btDevice.getName() + "[" + btDevice.getAddress() + "]");
            }

            if (parcelUuid != null) {
                UUID uuid = parcelUuid.getUuid();
                if (uuid != null) Log.d(TAG, "BT device UUID: " + uuid.toString());
            }
        }


    }

    private void onHandleBtAdapterBroadcast(Intent intent) {
        if (intent == null) return;

        String action = intent.getAction();
        Log.d(TAG, "[BT Adapter action]:" + action);

        if (BluetoothAdapter.ACTION_CONNECTION_STATE_CHANGED.equals(action)) {
            int connection = intent.getIntExtra(BluetoothAdapter.EXTRA_CONNECTION_STATE, -1);
            int oldConnection = intent.getIntExtra(
                    BluetoothAdapter.EXTRA_PREVIOUS_CONNECTION_STATE, -1);
            BluetoothDevice btDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
            String name = btDevice != null ? btDevice.getName() : "";
            if (!TextUtils.isEmpty(name) || connection != -1 || oldConnection != -1) {
                Log.d(TAG, "[Connection]Name: " + name + ", old : " + oldConnection + ", new : "
                        + connection);
            }
        }

        if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {

            if (!mFullStopped && mBluetoothAdapter != null) {
                mBluetoothAdapter.cancelDiscovery();
                mBluetoothAdapter.startDiscovery();
                Log.d(TAG, "restarting discovery");
            } else {
                Log.d(TAG, "Discovery Finished");
            }
        }

        if (BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action)) {
            Log.d(TAG, "Discovery Started");
        }

        if (BluetoothAdapter.ACTION_LOCAL_NAME_CHANGED.equals(action)) {
            String localName = intent.getStringExtra(BluetoothAdapter.EXTRA_LOCAL_NAME);
            Log.d(TAG, "Local name changed to: " + localName);
        }

        if (BluetoothAdapter.ACTION_SCAN_MODE_CHANGED.equals(action)) {
            int mode = intent.getIntExtra(BluetoothAdapter.EXTRA_SCAN_MODE, -1);
            int oldMode = intent.getIntExtra(BluetoothAdapter.EXTRA_PREVIOUS_SCAN_MODE, -1);

            Log.d(TAG, "Scan mode changed from: " + oldMode + ", to: " + mode);
        }

        if (BluetoothAdapter.ACTION_STATE_CHANGED.equals(action)) {
            int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, -1);
            int oldState = intent.getIntExtra(BluetoothAdapter.EXTRA_PREVIOUS_STATE, -1);

            Log.d(TAG, "State changed from: " + oldState + ", to: " + state);
        }

    }

    private boolean checkPermissions() {
        boolean full_permission_granted = true;
        ArrayList<String> permissions = new ArrayList<>();

        // Check Bluetooth permission
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.BLUETOOTH)) {
                // TODO: Add non-block dialog explain permission
                Log.e(TAG, "Explained Request Required (Bluetooth)");
                // TODO: try again
                return false;
            } else {
                permissions.add(Manifest.permission.BLUETOOTH);
            }
            full_permission_granted = false;
        }

        // Check Bluetooth Admin permission
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_ADMIN)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.BLUETOOTH_ADMIN)) {
                // TODO: Add non-block dialog explain permission
                Log.e(TAG, "Explained Request Required (Bluetooth Admin)");

                // TODO: try again
                return false;
            } else {
                permissions.add(Manifest.permission.BLUETOOTH_ADMIN);
            }
            full_permission_granted = false;
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_COARSE_LOCATION)) {
                // TODO: Add non-block dialog explain permission
                Log.e(TAG, "Explained Request Required (Access Coarse Location)");

                // TODO: try again
                return false;
            } else {
                permissions.add(Manifest.permission.ACCESS_COARSE_LOCATION);
            }
            full_permission_granted = false;
        }


        if (permissions != null && permissions.size() > 0) {
            String[] permissionsArray = new String[permissions.size()];
            permissionsArray = permissions.toArray(permissionsArray);
            ActivityCompat.requestPermissions(this, permissionsArray,
                    REQUEST_BLUETOOTH_PERMISSION);
        }

        return full_permission_granted;

    }


}
