package br.org.cesar.armrobotester.fragments;

import android.os.Bundle;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;

import br.org.cesar.armrobotester.R;

/**
 * Created by bcb on 19/07/16.
 */
public class TestArmPreferenceFragment extends PreferenceFragmentCompat {

    public TestArmPreferenceFragment() {

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
        // TODO: Get Bluetooth Paired Device

        // TODO: if empty list launch Bluetooth Settings

        CharSequence[] entries = {"Device-1", "Device-2"};
        CharSequence[] entryValues = {"00:11:22:33:44:55", "FF:EE:DD:CC:BB:AA"};
        listPreference.setEntries(entries);
        listPreference.setEntryValues(entryValues);
    }

    @Override
    public void onCreatePreferences(Bundle bundle, String s) {

    }
}
