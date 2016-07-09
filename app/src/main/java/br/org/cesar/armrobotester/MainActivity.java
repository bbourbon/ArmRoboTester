package br.org.cesar.armrobotester;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "ArmTester";
    private static final int REQUEST_BLUETOOTH_PERMISSION = 1;
    private static final int REQUEST_BLUETOOTH_ADMIN_PERMISSION = 2;
    private static final int REQUEST_ACCESS_LOCATION_PERMISSION = 4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

    }

    @Override
    protected void onResume() {
        super.onResume();

        if (!checkPermissions()) {
            Log.d(TAG, "Missing permission from user");
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private boolean CheckPermissionBluetooth() {
        ArrayList<String> permissions = new ArrayList<>();

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
        } else {
            return true;
        }

        if (permissions != null && permissions.size() > 0) {
            String[] permissionsArray = new String[permissions.size()];
            permissionsArray = permissions.toArray(permissionsArray);
            ActivityCompat.requestPermissions(this, permissionsArray,
                    REQUEST_BLUETOOTH_PERMISSION);
        }

        return false;
    }

    private boolean CheckPermissionBluetoothAdmin() {
        ArrayList<String> permissions = new ArrayList<>();

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
        } else {
            return true;
        }

        if (permissions != null && permissions.size() > 0) {
            String[] permissionsArray = new String[permissions.size()];
            permissionsArray = permissions.toArray(permissionsArray);
            ActivityCompat.requestPermissions(this, permissionsArray,
                    REQUEST_BLUETOOTH_ADMIN_PERMISSION);
        }

        return false;
    }

    private boolean CheckPermissionAccessCoarseLocation() {
        ArrayList<String> permissions = new ArrayList<>();

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_COARSE_LOCATION)) {
                // TODO: Add non-block dialog explain permission
                Log.e(TAG, "Explained Request Required (Bluetooth)");
                // TODO: try again
                return false;
            } else {
                permissions.add(Manifest.permission.ACCESS_COARSE_LOCATION);
            }
        } else {
            return true;
        }

        if (permissions != null && permissions.size() > 0) {
            String[] permissionsArray = new String[permissions.size()];
            permissionsArray = permissions.toArray(permissionsArray);
            ActivityCompat.requestPermissions(this, permissionsArray,
                    REQUEST_ACCESS_LOCATION_PERMISSION);
        }

        return false;
    }

    private boolean checkPermissions() {

        if (!CheckPermissionBluetooth()) return false;

        if (!CheckPermissionBluetoothAdmin()) return false;

        if (!CheckPermissionAccessCoarseLocation()) return false;

        return true;

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        boolean full_permission_granted = false;

        switch (requestCode) {
            case REQUEST_ACCESS_LOCATION_PERMISSION:
                Log.d(TAG, "Access Location Permission");
                break;
            case REQUEST_BLUETOOTH_ADMIN_PERMISSION:
                Log.d(TAG, "Bluetooth Admin");
                break;
            case REQUEST_BLUETOOTH_PERMISSION:
                Log.d(TAG, "Bluetooth");
                break;
            default:
                Log.w(TAG, "Request unknown!");
                break;
        }

        if (checkPermissions()) {
            Log.i(TAG, "Full permission granted");
        }

    }


}
