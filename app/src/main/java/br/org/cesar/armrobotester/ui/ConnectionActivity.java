package br.org.cesar.armrobotester.ui;

import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import br.org.cesar.armrobotester.R;
import br.org.cesar.armrobotester.comm.BluetoothHelper;
import br.org.cesar.armrobotester.fragments.ConnectionFragment;
import br.org.cesar.armrobotester.helper.Constants;
import br.org.cesar.armrobotester.model.Motion;
import br.org.cesar.armrobotester.model.TestCase;

/**
 * Created by bcb on 28/08/16.
 */

public class ConnectionActivity extends AppCompatActivity implements Handler.Callback{

    private static final String TAG_CONNECTION_FRAG = "connection_fragment";
    private static final String TAG = "Connection";
    private Handler mHandler;
    private ConnectionFragment connectionFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wizard_layout);

        mHandler = new Handler(this);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Connection");
        actionBar.setDisplayHomeAsUpEnabled(true);

        Button buttonAction = (Button) findViewById(R.id.button_action);
        buttonAction.setText("Calibrate");
        buttonAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                test();
            }
        });

        Button buttonNext = (Button) findViewById(R.id.button_next);
        buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onNextScreen();
            }
        });

    }

    private void test() {
        BluetoothDevice device = null;
        if (connectionFragment != null ) {
            device = connectionFragment.getBluetoothDevice();
        }

        if (device == null) {
            Toast.makeText(this, "Device is not selected!", Toast.LENGTH_SHORT).show();
            return;
        }

        TestCase test = new TestCase();
        test.setName("Calibrate");
        test.setId(0);

        List<Motion> motions = new ArrayList<Motion>();
        motions.add(new Motion(false, false, false, 0));
        motions.add(new Motion(true, false, true, 45));
        motions.add(new Motion(true, true, true, 180));

        test.setMotionList(motions);


        BluetoothHelper bluetoothHelper = new BluetoothHelper(this, this.mHandler);
        bluetoothHelper.connect(device, false);

        bluetoothHelper.start();

        int state = bluetoothHelper.getState();
        while (state != BluetoothHelper.STATE_CONNECTED) {
            //wait
            // TODO : Create timeout
        }

        for (Motion m : motions) {
            bluetoothHelper.write(m.commandArduino());
        }

    }

    @Override
    protected void onResume() {
        this.connectionFragment = new ConnectionFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.fragment_placement, this.connectionFragment, TAG_CONNECTION_FRAG)
                .commit();

        super.onResume();
    }

    private void onNextScreen() {
        Intent intent = new Intent(this, ExecutionActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean handleMessage(Message message) {
        if (message == null) return false;

        switch (message.what) {
            case Constants.MESSAGE_STATE_CHANGE:
                Log.d(TAG, "");
                break;
            case Constants.MESSAGE_READ:
                break;
            case Constants.MESSAGE_WRITE:
                break;
            case Constants.MESSAGE_DEVICE_NAME:
                break;
            case Constants.MESSAGE_TOAST:
                break;
            case Constants.MESSAGE_CONNECTION_FAILED:
                break;
            case Constants.MESSAGE_CONNECTION_LOST:
                break;
            default:

                Log.d(TAG, "Message: " + message.what);
        }
        return true;
    }
}
