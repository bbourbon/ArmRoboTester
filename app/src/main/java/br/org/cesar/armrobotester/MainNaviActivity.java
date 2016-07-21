package br.org.cesar.armrobotester;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Set;

import br.org.cesar.armrobotester.content.TestContent;
import br.org.cesar.armrobotester.fragments.StatusFragment;
import br.org.cesar.armrobotester.fragments.TestArmPreferenceFragment;
import br.org.cesar.armrobotester.fragments.TestsFragment;

public class MainNaviActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        TestsFragment.OnListFragmentInteractionListener, SharedPreferences.OnSharedPreferenceChangeListener {

    public static final String TAG = "BRACO";
    FloatingActionButton mFabOptions;
    FloatingActionButton mFabPlay;
    TextView mTextDeviceName;
    TextView mTextDeviceAddress;
    private SharedPreferences mSharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_navi);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mFabPlay = (FloatingActionButton) findViewById(R.id.fab_play);
        if (mFabPlay != null) {
            mFabPlay.setVisibility(FloatingActionButton.GONE);
            mFabPlay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Snackbar.make(view, "Starting Tests...", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            });
        }

        mFabOptions = (FloatingActionButton) findViewById(R.id.fab_options);
        if (mFabOptions != null) {
            mFabOptions.setVisibility(FloatingActionButton.GONE);
            mFabOptions.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Snackbar.make(view, "Add new test", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            });
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        if (drawer != null) {
            drawer.addDrawerListener(toggle);
            toggle.syncState();
        }

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        if (navigationView != null) {
            navigationView.setNavigationItemSelectedListener(this);
            View headerView = navigationView.getHeaderView(0);
            mTextDeviceName = (TextView) headerView.findViewById(R.id.text_bt_device_name);
            mTextDeviceAddress = (TextView) headerView.findViewById(R.id.text_bt_device_address);

        }


        this.mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (null != mSharedPreferences) {
            mSharedPreferences.registerOnSharedPreferenceChangeListener(this);
        }
        loadLastPairedDevice();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (null != mSharedPreferences) {
            mSharedPreferences.unregisterOnSharedPreferenceChangeListener(this);
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer != null && drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_navi, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            onSettings();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_tests) {
            MainNaviActivity.this.onTests();
        } else if (id == R.id.nav_calibrate) {
            MainNaviActivity.this.onCalibrate();
        } else if (id == R.id.nav_status) {
            MainNaviActivity.this.onStatus();
        } else if (id == R.id.nav_last_result) {
            MainNaviActivity.this.onLastResult();
        } else if (id == R.id.nav_settings) {
            MainNaviActivity.this.onSettings();
        } else if (id == R.id.nav_share) {
            MainNaviActivity.this.onShare();
        } else if (id == R.id.nav_export_sdcard) {
            MainNaviActivity.this.onExportResults();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer != null) {
            drawer.closeDrawer(GravityCompat.START);
        }
        return true;
    }

    private void onTests() {
        final TestsFragment testsFragment = new TestsFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.relative_for_fragments, testsFragment).commit();

        mFabOptions.setImageResource(R.drawable.ic_float_plus);
        mFabOptions.setOnClickListener(testsFragment);
        mFabOptions.setVisibility(FloatingActionButton.VISIBLE);
    }

    private void onCalibrate() {
        Toast.makeText(this, "Calibrate", Toast.LENGTH_SHORT).show();
    }

    private void onStatus() {
        final StatusFragment statusFragment = new StatusFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.relative_for_fragments, statusFragment).commit();

        mFabOptions.setImageResource(R.drawable.ic_float_refresh);
        mFabOptions.setOnClickListener(statusFragment);
        mFabOptions.setVisibility(FloatingActionButton.VISIBLE);

        //Toast.makeText(this, "Status", Toast.LENGTH_SHORT).show();
    }

    private void onLastResult() {
        Toast.makeText(this, "Last Result", Toast.LENGTH_SHORT).show();
    }

    private void onSettings() {
        final TestArmPreferenceFragment testArmPreferenceFragment = new TestArmPreferenceFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.relative_for_fragments, testArmPreferenceFragment).commit();
        //Toast.makeText(this, "Open Settings", Toast.LENGTH_SHORT).show();
    }

    private void onShare() {
        Toast.makeText(this, "Share Result", Toast.LENGTH_SHORT).show();
    }

    private void onExportResults() {
        Toast.makeText(this, "Exporting...", Toast.LENGTH_SHORT).show();
    }

    private void loadLastPairedDevice() {
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        String deviceMacAddress = mSharedPreferences.getString("bt_device", null);
        String deviceName = null;
        if (null != bluetoothAdapter && !TextUtils.isEmpty(deviceMacAddress)) {
            Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();
            for (BluetoothDevice device : pairedDevices) {
                if (deviceMacAddress.equals(device.getAddress())) {
                    deviceName = device.getName();
                }
            }
        }

        if (TextUtils.isEmpty(deviceName) && !TextUtils.isEmpty(deviceMacAddress)) {
            deviceName = deviceMacAddress;
        }

        if (!TextUtils.isEmpty(deviceName) && !TextUtils.isEmpty(deviceMacAddress)) {
            if (null != mTextDeviceName)
                mTextDeviceName.setText("Device: " + deviceName);
            if (null != mTextDeviceAddress)
                mTextDeviceAddress.setText("Address: " + deviceMacAddress);
        }

        Log.d(TAG, "Dev: " + deviceName + ", MAC: " + deviceMacAddress);
    }

    @Override
    public void onListFragmentInteraction(TestContent.MotionTestItem item) {

    }

    @Override
    public void onLongClick(TestContent.MotionTestItem item) {
        // TODO: Remove item
    }

    @Override
    public void onClick(TestContent.MotionTestItem item) {
        // TODO: Change Status
    }

    @Override
    public void onListFragmentItemCountChanged(int count) {
        if (mFabPlay != null) {
            mFabPlay.setVisibility(count > 0 ? FloatingActionButton.VISIBLE :
                    FloatingActionButton.GONE);
        }
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (null != sharedPreferences && !TextUtils.isEmpty(key)) {
            if (key.equals("bt_device")) {
                loadLastPairedDevice();
                Toast.makeText(this, sharedPreferences.getString("bt_device", "invalid"),
                        Toast.LENGTH_LONG).show();
            }
        }
    }
}