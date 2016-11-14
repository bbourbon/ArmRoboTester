package br.org.cesar.armrobotester.fragments;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.ParcelUuid;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;

import br.org.cesar.armrobotester.R;
import br.org.cesar.armrobotester.comm.BluetoothHelper;

import static android.content.ContentValues.TAG;

/**
 * Created by bcb on 28/08/16.
 */

public class ConnectionFragment extends Fragment implements Handler.Callback,
        AdapterView.OnItemSelectedListener {

    private static final UUID SPP_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    private BluetoothDevice mBluetoothDevice;
    private BluetoothHelper mBluetoothHelper;
    private Handler mHandler;

    private Spinner mSpinnerPairedDevices;
    private ArrayList<String> mPairedDeviceList;
    private ArrayAdapter<String> mAdapterPairedDevices;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mHandler = new Handler((Handler.Callback) this);
        mBluetoothHelper = new BluetoothHelper(getContext(), this.mHandler);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_connection, container, false);

        mSpinnerPairedDevices = (Spinner)view.findViewById(R.id.spinner_paired_devices);

        initPairedDevices();
        initSpinner();

        return view;
    }

    private void initPairedDevices() {
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (!bluetoothAdapter.isEnabled()) {
            Toast.makeText(getContext(), "Please Enable Bluetooth!", Toast.LENGTH_SHORT);
            return;
        }

        Set<BluetoothDevice> bondedDevices = bluetoothAdapter.getBondedDevices();
        for (BluetoothDevice device : bondedDevices) {


            String name = device.getName();
            if (TextUtils.isEmpty(name)) name = device.getAddress();
            if (hasSppSupport(device)) {
                mPairedDeviceList.add(name);
            }
        }
    }

    private void initSpinner() {
        // Create an ArrayAdapter using the string array and a default spinner layout
        mAdapterPairedDevices = new ArrayAdapter<>(this.getContext(),
                android.R.layout.simple_spinner_item, mPairedDeviceList);

        // Specify the layout to use when the list of choices appears
        mAdapterPairedDevices.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        mSpinnerPairedDevices.setAdapter(mAdapterPairedDevices);

        // Listener for selection
        mSpinnerPairedDevices.setOnItemSelectedListener(this);

    }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
        if (adapterView == mSpinnerPairedDevices && mSpinnerPairedDevices != null) {
            if (mAdapterPairedDevices.getCount() < 1) {
                Toast.makeText(getContext(), "No bluetooth device paired!",
                        Toast.LENGTH_SHORT).show();
            } else {
                String deviceName = mAdapterPairedDevices.getItem(position);
                mBluetoothDevice = getPairedDeviceByName(deviceName);
            }

        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    public boolean hasSppSupport(BluetoothDevice device) {
        boolean result = false;
        if (device == null) return false;

        ParcelUuid[] uuids = device.getUuids();

        if (uuids != null) {
            for (ParcelUuid uuid : uuids) {
                String log = "Device [" + device.getName() + "] UUID: ";
                log += uuid.toString();
                Log.d(TAG, log);

                result = result || (SPP_UUID.equals(uuid.getUuid()));
            }
        }
        Log.d(TAG, "Device has SPP? " + result);
        return result;
    }


    private BluetoothDevice getPairedDeviceByName(String deviceName) {
        BluetoothDevice bluetoothDevice = null;

        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        Set<BluetoothDevice> bondedDevices = bluetoothAdapter.getBondedDevices();
        for (BluetoothDevice device : bondedDevices) {
            String name = device.getName();
            if (!TextUtils.isEmpty(name) && name.equals(deviceName))
                bluetoothDevice = device;
        }


        return bluetoothDevice;
    }

    public BluetoothDevice getBluetoothDevice() {
        return mBluetoothDevice;
    }


    @Override
    public boolean handleMessage(Message message) {
        return false;
    }
}
