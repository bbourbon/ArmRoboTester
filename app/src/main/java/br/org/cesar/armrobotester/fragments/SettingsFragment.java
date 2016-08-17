package br.org.cesar.armrobotester.fragments;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;

import java.util.ArrayList;
import java.util.Set;

import br.org.cesar.armrobotester.R;

/**
 * Created by bcb on 19/07/16.
 */
public class SettingsFragment extends PreferenceFragmentCompat {

    public SettingsFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(
                R.xml.test_arm_preferences);

        final ListPreference listPreference = (ListPreference) findPreference("bt_device");
        //setDeviceList(listPreference);

        listPreference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                setDeviceList(listPreference);
                return false;
            }
        });
    }

    private void setDeviceList(ListPreference listPreference) {
        Context context = this.getContext();

        if (null != context) {
            ArrayList<CharSequence> names = new ArrayList<>();
            ArrayList<CharSequence> address = new ArrayList<>();

            BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            if (null != bluetoothAdapter) {
                Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();

                for (BluetoothDevice device : pairedDevices) {
                    names.add(device.getName());
                    address.add(device.getAddress());
                }
            }

            if (names.size() == address.size() && names.size() > 0) {
                CharSequence[] entries = names.toArray(new CharSequence[names.size()]);
                CharSequence[] entryValues =
                        address.toArray(new CharSequence[address.size()]);
                listPreference.setEntries(entries);
                listPreference.setEntryValues(entryValues);
            }
        }
    }

    @Override
    public void onCreatePreferences(Bundle bundle, String s) {

    }
}
