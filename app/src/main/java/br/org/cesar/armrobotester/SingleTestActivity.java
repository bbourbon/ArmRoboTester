package br.org.cesar.armrobotester;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.ParcelUuid;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.UUID;

import br.org.cesar.armrobotester.fragments.SingleTestFragment;
import br.org.cesar.armrobotester.model.Motion;
import br.org.cesar.armrobotester.model.TestCase;

/**
 * Created by bcb on 06/09/16.
 */

public class SingleTestActivity extends AppCompatActivity {

    private static final String TAG_SINGLE_TEST_FRAG = "tag_instruction_fragment";
    private static final String TAG = "Single Test";
    final SingleTestFragment mSingleTestFragment = new SingleTestFragment();
    private Button mButtonReset;
    private Button mButtonRun;
    private BluetoothDevice mBluetoothDevice;
    private TestCase mTestCase;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.single_test_layout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_wizard);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("Single Test");

        mButtonReset = (Button) findViewById(R.id.button_reset);
        mButtonReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reset();
            }
        });

        mButtonRun = (Button) findViewById(R.id.button_run);
        mButtonRun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                run();
            }
        });
    }

    @Override
    protected void onResume() {

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager
                .beginTransaction()
                .replace(R.id.fragment_placement, mSingleTestFragment)
                .commit();
        super.onResume();
    }

    private void run() {

        mBluetoothDevice = mSingleTestFragment.getBluetoothDevice();
        mTestCase = mSingleTestFragment.getTestCase();

        if (mBluetoothDevice == null || mTestCase == null) {
            Toast.makeText(this, "Test Case incomplete or bluetooth device is not selected",
                    Toast.LENGTH_SHORT).show();
        } else {
            Log.d(TAG, "BT Device: " + mBluetoothDevice.getName() +
                    " [" + mBluetoothDevice.getAddress() + "]");

            Log.d(TAG, "Test Case: " + mTestCase.getName() + "[" + mTestCase.getId() + "]");
            for (Motion m : mTestCase.getMotionTestList()) {
                Log.d(TAG, "Motion: " + m.toString());
            }
        }

        runTestCase();
    }

    private void runTestCase() {

        TestCaseAsyncTask testCaseAsyncTask =
                new TestCaseAsyncTask(this, mBluetoothDevice, mTestCase);

        if (testCaseAsyncTask.hasSppSupport()) {
            Log.d(TAG, mBluetoothDevice.getName() + " has SPP support");

            testCaseAsyncTask.execute();
        }
    }

    private void reset() {
        if (mSingleTestFragment != null) {
            mSingleTestFragment.reset();
        }
    }

    private class TestCaseAsyncTask extends AsyncTask<Void, Void, Void> {

        final UUID sppUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
        BluetoothDevice mDevice;
        BluetoothSocket mSocket;
        Context mContext;
        TestCase mTestCase;

        private OutputStream outputStream = null;
        private InputStream inputStream = null;


        public TestCaseAsyncTask(Context context, BluetoothDevice device, TestCase test) {
            mContext = context;
            mDevice = device;
            mTestCase = test;
        }

        public boolean hasSppSupport() {
            boolean result = false;
            if (this.mDevice == null) return false;

            ParcelUuid[] uuids = this.mDevice.getUuids();

            if (uuids != null) {
                for (ParcelUuid uuid : uuids) {
                    String log = "Device [" + mBluetoothDevice.getName() + "] UUID: ";
                    log += uuid.toString();
                    Log.d(TAG, log);

                    result = result || (sppUUID.equals(uuid.getUuid()));
                }
            }
            Log.d(TAG, "Device has SPP? " + result);
            return result;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            try {
                // Serial Port Protocol
                if (hasSppSupport()) {
                    mSocket = mBluetoothDevice.createRfcommSocketToServiceRecord(sppUUID);

                    mSocket.connect();

                    if (mSocket.isConnected()) {
                        outputStream = mSocket.getOutputStream();
                        inputStream = mSocket.getInputStream();
                    }
                }
            } catch (IOException e) {
                Log.e(TAG, "Exception: " + e.getLocalizedMessage());
                if (outputStream != null) {
                    try {
                        outputStream.close();
                    } catch (IOException ioex) {
                        Log.e(TAG, ioex.getLocalizedMessage(), ioex);
                    }
                }

                if (inputStream != null) {
                    try {
                        inputStream.close();
                    } catch (IOException ioex) {
                        Log.e(TAG, ioex.getLocalizedMessage(), ioex);
                    }
                }
                mSocket = null;
            }
        }

        @Override
        protected Void doInBackground(Void... voids) {
            if (mSocket == null) return null;

            try {
                byte inBuffer[] = new byte[1024];
                int read = -1;
                InputStream inputStream = mSocket.getInputStream();
                OutputStream outputStream = mSocket.getOutputStream();

                List<Motion> motionList = mTestCase.getMotionTestList();

                for (Motion motion : motionList) {
                    byte command[] = motion.commandArduino();

                    outputStream.write(command);
                    read = inputStream.read(inBuffer);
                    Motion.decodeResult(inBuffer, read);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }

        @Override
        protected void onCancelled(Void aVoid) {
            super.onCancelled(aVoid);
        }
    }
}
