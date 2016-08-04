package br.org.cesar.armrobotester.fragments;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorEventListener2;
import android.hardware.SensorManager;
import android.hardware.TriggerEvent;
import android.hardware.TriggerEventListener;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import br.org.cesar.armrobotester.MainNaviActivity;
import br.org.cesar.armrobotester.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class SensorActivityFragment extends Fragment implements SensorEventListener,
        SensorEventListener2 {

    private static final String TAG = MainNaviActivity.TAG;
    private SensorManager mSensorManager;
    private boolean mRecordData = false;
    private TriggerEventListener mTriggerEventListener;
    private ArrayList<Sensor> mOneShotSensorList;
    private ArrayList<Sensor> mContinuosSensorList;
    private ArrayList<Sensor> mOnChangeSensorList;
    private ArrayList<Sensor> mSpecialTriggerSensorList;


    private String mCurrentReportFileName;

    public SensorActivityFragment() {

        mTriggerEventListener = new TriggerEventListener() {
            @Override
            public void onTrigger(TriggerEvent event) {
                SensorActivityFragment.this.onTrigger(event);
            }
        };

        // Standard name should change every time record method is called;
        mCurrentReportFileName = "report.txt";

        mOneShotSensorList = new ArrayList<>();
        mContinuosSensorList = new ArrayList<>();
        mOnChangeSensorList = new ArrayList<>();
        mSpecialTriggerSensorList = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        return inflater.inflate(R.layout.fragment_sensor, container, false);
    }

    @Override
    public void onAttach(Context context) {
        mSensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        preInitSensors();

        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        if (null != mSensorManager) {
            mSensorManager.unregisterListener(this);
        }
        super.onDetach();
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
        builder.append(", sensor: " + event.sensor.getName());
        builder.append(", type: " + event.sensor.getStringType());

        int accuracy = event.accuracy;
        long timestamp = event.timestamp;

        log(TAG, "acc: " + accuracy);
        log(TAG, "timestamp: " + timestamp);
        builder.append(", acc: " + accuracyToString(accuracy));
        builder.append(", timestamp: " + timestamp);


        for (int i = 0; i < event.values.length; i++) {
            float v = event.values[i];
            log(TAG, "value[" + i + "]: " + v);
            builder.append(", value[" + i + "]: " + v);
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


    public void initSensors() {
        if (mSensorManager == null) return;
        mRecordData = false;

        List<Sensor> sensorList = mSensorManager.getSensorList(Sensor.TYPE_ALL);

        for (Sensor s : mContinuosSensorList) {
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

        mContinuosSensorList.clear();
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
                        mContinuosSensorList.add(defaultSensorForType);
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

    public void record() {
        this.mRecordData = true;
        initReportFile();
        saveToFile("BEING RECORDING -----------------------------------------------------------");
    }

    public void stopRecord() {
        saveToFile("END RECORDING -----------------------------------------------------------");
        this.mRecordData = false;

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
        builder.append("action: trigger, sensor: " + event.sensor.getName());
        builder.append(", type: " + event.sensor.getStringType());


        long timestamp = event.timestamp;

        log(TAG, "timestamp: " + timestamp);
        builder.append(", timestamp: " + timestamp);

        for (int i = 0; i < event.values.length; i++) {
            float v = event.values[i];
            log(TAG, "value[" + i + "]: " + v);
            builder.append(", value[" + i + "]: " + v);
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

        File file = new File(getContext().getFilesDir(), mCurrentReportFileName);
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
        Context context = getContext();
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
}
