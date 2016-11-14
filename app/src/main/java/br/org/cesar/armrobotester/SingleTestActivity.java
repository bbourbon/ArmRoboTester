package br.org.cesar.armrobotester;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorEventListener2;
import android.hardware.SensorManager;
import android.hardware.TriggerEvent;
import android.hardware.TriggerEventListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.ParcelUuid;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import br.org.cesar.armrobotester.comm.BluetoothHelper;
import br.org.cesar.armrobotester.fragments.SingleTestFragment;
import br.org.cesar.armrobotester.model.Motion;
import br.org.cesar.armrobotester.model.TestCase;

/**
 * Created by bcb on 06/09/16.
 */

public class SingleTestActivity extends AppCompatActivity implements SensorEventListener,
        SensorEventListener2 {

    private static final String TAG_SINGLE_TEST_FRAG = "tag_instruction_fragment";
    private static final String TAG = "Single Test";
    final SingleTestFragment mSingleTestFragment = new SingleTestFragment();
    Handler.Callback mCallBack = new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {

            Log.d(TAG, "Message: " + message);
            switch(message.what) {

            }

            return true;
        }
    };
    private Button mButtonReset;
    private Button mButtonRun;
    private BluetoothDevice mBluetoothDevice;
    private BluetoothAdapter mBluetoothAdapter;
    private TestCase mTestCase;
    private Handler mHandler = new Handler(mCallBack);
    private TestCaseAsyncTask mTestCaseAsyncTask;


    private SensorManager mSensorManager;
    private boolean mRecordData = false;
    private TriggerEventListener mTriggerEventListener;
    private ArrayList<Sensor> mOneShotSensorList;
    private ArrayList<Sensor> mContinuousSensorList;
    private ArrayList<Sensor> mOnChangeSensorList;
    private ArrayList<Sensor> mSpecialTriggerSensorList;


    private String mCurrentReportFileName;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.single_test_layout);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("Single Test");
        }

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

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        mTriggerEventListener = new TriggerEventListener() {
            @Override
            public void onTrigger(TriggerEvent event) {
                this.onTrigger(event);
            }
        };

        // Standard name should change every time startRecord method is called;
        mCurrentReportFileName = "report.txt";

        mOneShotSensorList = new ArrayList<>();
        mContinuousSensorList = new ArrayList<>();
        mOnChangeSensorList = new ArrayList<>();
        mSpecialTriggerSensorList = new ArrayList<>();

    }

    @Override
    protected void onResume() {

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager
                .beginTransaction()
                .replace(R.id.fragment_placement, mSingleTestFragment)
                .commit();


        preInitSensors();

        super.onResume();
    }

    @Override
    protected void onPause() {
        if (null != mSensorManager) {
            mSensorManager.unregisterListener(this);
        }
        super.onPause();
    }

    private void reset() {
        if (mSingleTestFragment != null) {
            mSingleTestFragment.reset();
        }

        mTestCaseAsyncTask.cancel(true);
        stopRecordSensors();
    }


    private void run() {

        mBluetoothDevice = mSingleTestFragment.getBluetoothDevice();
        mTestCase = mSingleTestFragment.getTestCase();

        if (mBluetoothDevice == null || mTestCase == null) {
            Toast.makeText(this, "Test Case incomplete or bluetooth device is not selected",
                    Toast.LENGTH_SHORT).show();
        }

        if (mTestCaseAsyncTask != null) {
            AsyncTask.Status status = mTestCaseAsyncTask.getStatus();
            if (status == AsyncTask.Status.RUNNING) return;
        }

        runTestCase();
    }

    private synchronized void runTestCase() {


        this.mTestCaseAsyncTask =
                new TestCaseAsyncTask(this, mBluetoothDevice, mTestCase);

        if (mTestCaseAsyncTask.hasSppSupport()) {
            Log.d(TAG, mBluetoothDevice.getName() + " has SPP support");
            mTestCaseAsyncTask.execute();
        } else {
            Toast.makeText(this, "Target Device doesn't support BT SPP", Toast.LENGTH_SHORT).show();
        }


    }

    private void startRecordSensors() {
        initSensors();
        startRecord();
    }

    private void stopRecordSensors() {
        stopRecord();
    }

    private void runTestCase2() {
        BluetoothHelper bluetoothHelper = new BluetoothHelper(this, this.mHandler);

        bluetoothHelper.connect(mBluetoothDevice, false);
        bluetoothHelper.start();

        for (Motion m: mTestCase.getMotionTestList()) {
            bluetoothHelper.write(m.commandArduino());
        }

    }


    // Listener methods
    @Override
    public void onFlushCompleted(Sensor sensor) {
        log(TAG, "Flush Completed for: " + sensor.getName());
        String message = "action: flush, sensor: " + sensor.getName();
        message += ", type: " + sensor.getStringType() + ";";
        saveToFile(message);

    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        StringBuilder builder = new StringBuilder();
        log(TAG, "Sensor changed: " + event.sensor.getName());
        builder.append("action: sensor change");
        builder.append(", sensor: ").append(event.sensor.getName());
        builder.append(", type: ").append(event.sensor.getStringType());

        int accuracy = event.accuracy;
        long timestamp = event.timestamp;

        log(TAG, "acc: " + accuracy);
        log(TAG, "timestamp: " + timestamp);
        builder.append(", acc: ").append(accuracyToString(accuracy));
        builder.append(", timestamp: ").append(timestamp);


        for (int i = 0; i < event.values.length; i++) {
            float v = event.values[i];
            log(TAG, "value[" + i + "]: " + v);
            builder.append(", value[").append(i).append("]: ").append(v);
        }

        String message = builder.toString() + ";";

        saveToFile(message);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        log(TAG, "Accuracy Changed for: " + sensor.getName());
        log(TAG, "new acc: " + accuracy);

        String message = "action: accuracy changed, sensor: " + sensor.getName();
        message += ", type: " + sensor.getStringType();
        message += ", accuracy: " + accuracyToString(accuracy) + ";";

        saveToFile(message);
    }


    // Sensors preparation
    public void initSensors() {
        if (mSensorManager == null) return;
        mRecordData = false;

        List<Sensor> sensorList = mSensorManager.getSensorList(Sensor.TYPE_ALL);

        for (Sensor s : mContinuousSensorList) {
            mSensorManager.registerListener(this, s, SensorManager.SENSOR_DELAY_UI);
        }

        for (Sensor s : mOnChangeSensorList) {
            mSensorManager.registerListener(this, s, SensorManager.SENSOR_DELAY_UI);
        }

        for (Sensor s : mOneShotSensorList) {
            if (null != mTriggerEventListener) {
                mSensorManager.requestTriggerSensor(this.mTriggerEventListener, s);
            }
        }

        for (Sensor s : mSpecialTriggerSensorList) {
            mSensorManager.registerListener(this, s, SensorManager.SENSOR_DELAY_UI
                    , 100);
        }

        if (null != sensorList) {
            for (Sensor s : sensorList) {
                Sensor defaultSensor = mSensorManager.getDefaultSensor(s.getType());
                if (defaultSensor.getName().equals(s.getName())) {
                    Log.d(TAG, "Sensor: " + s.getName() + " is default "
                            + s.getStringType() + "  sensor");
                } else {
                    Log.w(TAG, "Sensor: " + s.getName() + " isn't default "
                            + s.getStringType() + "  sensor");
                }
            }
        }

    }

    private void preInitSensors() {
        if (mSensorManager == null) return;
        mRecordData = false;

        mContinuousSensorList.clear();
        mOnChangeSensorList.clear();
        mSpecialTriggerSensorList.clear();
        mOneShotSensorList.clear();

        List<Sensor> sensorList = mSensorManager.getSensorList(Sensor.TYPE_ALL);
        if (null != sensorList) {
            Log.d(TAG, "Sensors List: - BEGIN ------------------------------------------------- ");
            for (Sensor s : sensorList) {
                Log.d(TAG, "Sensor: " + s.getName()
                        + ", Type: " + s.getStringType()
                        + ", Vendor: " + s.getVendor()
                        + ", Version: " + s.getVersion());

                Sensor defaultSensorForType = mSensorManager.getDefaultSensor(s.getType());

                switch (defaultSensorForType.getReportingMode()) {
                    case Sensor.REPORTING_MODE_CONTINUOUS:
                        Log.d(TAG, "Sensor reporting mode: Continuous");
                        mContinuousSensorList.add(defaultSensorForType);
                        break;
                    case Sensor.REPORTING_MODE_ON_CHANGE:
                        Log.d(TAG, "Sensor reporting mode: On Change");
                        mOnChangeSensorList.add(defaultSensorForType);
                        break;
                    case Sensor.REPORTING_MODE_ONE_SHOT:
                        Log.d(TAG, "Sensor reporting mode: One Shot");
                        if (null != this.mOneShotSensorList) {
                            this.mOneShotSensorList.add(defaultSensorForType);
                        }
                        break;
                    case Sensor.REPORTING_MODE_SPECIAL_TRIGGER:
                        Log.d(TAG, "Sensor reporting mode: Special Trigger");
                        mSpecialTriggerSensorList.add(defaultSensorForType);
                        break;
                    default:
                        Log.d(TAG, "Mode Unknown!");
                }

            }
            Log.d(TAG, "Sensors List: - END --------------------------------------------------- ");

        }

    }

    // Record methods
    public void startRecord() {
        this.mRecordData = true;
        initReportFile();
        saveToFile("BEING RECORDING -----------------------------------------------------------");
    }

    public void stopRecord() {
        saveToFile("END RECORDING -----------------------------------------------------------");
        this.mRecordData = false;

        if (mSensorManager == null) return;

        // Unregister Sensor Listener
        mSensorManager.unregisterListener(this);

        // Unregister Trigger Sensor Listener
        for (Sensor s : mOneShotSensorList) {
            if (null != mTriggerEventListener) {
                mSensorManager.cancelTriggerSensor(mTriggerEventListener, s);
            }
        }
        mOneShotSensorList.clear();
    }

    public String getFileName() {
        return mCurrentReportFileName;
    }

    // Private methods
    private void onTrigger(TriggerEvent event) {
        StringBuilder builder = new StringBuilder();
        log(TAG, "Trigger Event for: " + event.sensor.getName());
        builder.append("action: trigger, sensor: ").append(event.sensor.getName());
        builder.append(", type: ").append(event.sensor.getStringType());


        long timestamp = event.timestamp;

        log(TAG, "timestamp: " + timestamp);
        builder.append(", timestamp: ").append(timestamp);

        for (int i = 0; i < event.values.length; i++) {
            float v = event.values[i];
            log(TAG, "value[" + i + "]: " + v);
            builder.append(", value[").append(i).append("]: ").append(v);
        }

        // TODO: Re-trigger listener?

        String message = builder.toString() + ";";
        saveToFile(message);

    }

    private String accuracyToString(int accuracy) {
        String accString;
        switch (accuracy) {
            case SensorManager.SENSOR_STATUS_ACCURACY_LOW:
                accString = "low";
                break;
            case SensorManager.SENSOR_STATUS_ACCURACY_MEDIUM:
                accString = "medium";
                break;
            case SensorManager.SENSOR_STATUS_ACCURACY_HIGH:
                accString = "high";
                break;
            default:
                accString = "unknown";
                break;
        }
        return accString;
    }

    // Register methods
    private synchronized void log(String tag, String message) {
        if (mRecordData) {
            Log.d(TAG, "[" + tag + "]" + message);
        }
    }

    private void initReportFile() {
        Long tsLong = System.currentTimeMillis() / 1000;
        String now = tsLong.toString();

        mCurrentReportFileName = "report-" + now + ".txt";

        File file = new File(this.getFilesDir(), mCurrentReportFileName);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                Log.e(TAG, "Couldn't create new file:" + e.getLocalizedMessage());
                return;
            }
        }
    }

    // TODO: Create a buffer to avoid excessive IO operations
    private void saveToFile(String data) {
        Context context = this;
        FileOutputStream out = null;

        if (TextUtils.isEmpty(data) || !mRecordData) return;

        Long tsLong = System.currentTimeMillis();
        String now = tsLong.toString();

        try {
            out = context.openFileOutput(mCurrentReportFileName,
                    Context.MODE_APPEND | Context.MODE_PRIVATE);

            String line = "[" + now + "]" + data + "\n";

            out.write(line.getBytes());
            out.close();
        } catch (FileNotFoundException e) {
            Log.e(TAG, "File not Found: " + e.getLocalizedMessage());
            return;
        } catch (IOException e) {
            Log.e(TAG, "Error writing on file: " + e.getLocalizedMessage());
        } finally {
            if (null != out) {
                try {
                    out.close();
                } catch (IOException e) {
                    Log.e(TAG, "Fail to close Output Stream: " + e.getLocalizedMessage());
                }
            }
        }

    }



    /**
     * AsyncTask send Bluetooth Commands
     */
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
                if (mSocket != null && mSocket.isConnected()) {
                    mSocket.close();
                }

                // Serial Port Protocol
                if (hasSppSupport()) {
                    mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                    mBluetoothAdapter.cancelDiscovery();

                    mSocket = mBluetoothDevice.createRfcommSocketToServiceRecord(sppUUID);

                    mSocket.connect();

                    if (mSocket.isConnected()) {
                        outputStream = mSocket.getOutputStream();
                        inputStream = mSocket.getInputStream();
                    }
                    Log.d(TAG, "Connected to: " + mBluetoothDevice.getName());

                    startRecordSensors();
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
            if (mSocket == null || mTestCase == null) return null;

            try {
                byte inBuffer[] = new byte[24];
                Arrays.fill(inBuffer, (byte)0);

                int read = -1;
                InputStream inputStream = mSocket.getInputStream();
                OutputStream outputStream = mSocket.getOutputStream();

                List<Motion> motionList = mTestCase.getMotionTestList();

                for (Motion motion : motionList) {
                    byte command[] = motion.commandArduino();
                    Log.d(TAG, "write: " + command);
                    outputStream.write(command);

                    this.publishProgress(null);

                    read = inputStream.read(inBuffer);
                    String out = Motion.decodeResult(inBuffer, read);
                    Log.d(TAG, "read: " + out);
                    this.publishProgress(null);
                }

                inputStream.close();
                outputStream.close();
                mSocket.close();

            } catch (IOException e) {
                Log.e(TAG, "Exception: " + e.getLocalizedMessage(), e);
            } finally {

                try {
                    inputStream.close();
                    outputStream.close();
                    mSocket.close();

                    inputStream = null;
                    outputStream = null;
                    mSocket = null;
                } catch (IOException e) {
                    Log.e(TAG, "Exception: " + e.getLocalizedMessage(), e);
                }

            }

            return null;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            stopRecordSensors();
            super.onPostExecute(aVoid);
        }

        @Override
        protected void onCancelled() {
            stopRecordSensors();
            Toast.makeText(SingleTestActivity.this, "Test Cancelled", Toast.LENGTH_LONG).show();
            super.onCancelled();
        }

        @Override
        protected void onCancelled(Void aVoid) {
            stopRecordSensors();
            Toast.makeText(SingleTestActivity.this, "Test Cancelled", Toast.LENGTH_LONG).show();
            super.onCancelled(aVoid);
        }
    }


}
