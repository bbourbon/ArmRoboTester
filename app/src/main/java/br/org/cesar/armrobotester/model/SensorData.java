package br.org.cesar.armrobotester.model;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorManager;
import android.hardware.TriggerEvent;
import android.support.annotation.NonNull;

import java.util.Comparator;

/**
 * Created by bcb on 24/07/16.
 */
public class SensorData implements Comparator<SensorData> {

    public long mTimestamp = 0L;
    private int mAccuracy = 0;
    private Sensor mSensor;
    private float[] mValues;

    public SensorData(@NonNull Sensor sensor, @NonNull float[] values) {
        mSensor = sensor;
        mValues = new float[values.length];
        for (int i = 0; i < values.length; i++) {
            mValues[i] = values[i];
        }
    }

    public SensorData(@NonNull SensorEvent sensorEvent) {
        mSensor = sensorEvent.sensor;
        mTimestamp = sensorEvent.timestamp;
        this.mAccuracy = sensorEvent.accuracy;

        float[] values = sensorEvent.values;

        mValues = new float[values.length];
        for (int i = 0; i < values.length; i++) {
            mValues[i] = values[i];
        }
    }

    public SensorData(@NonNull TriggerEvent triggerEvent) {
        mSensor = triggerEvent.sensor;
        mTimestamp = triggerEvent.timestamp;
        float[] values = triggerEvent.values;

        mValues = new float[values.length];
        for (int i = 0; i < values.length; i++) {
            mValues[i] = values[i];
        }
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();

        if (mSensor != null) {
            builder.append("name: ").append(mSensor.getName()).append("\n");
            builder.append("TS: ").append(mTimestamp).append("\n");
        }

        if (mAccuracy >= SensorManager.SENSOR_STATUS_ACCURACY_LOW &&
                mAccuracy <= SensorManager.SENSOR_STATUS_ACCURACY_HIGH) {
            builder.append("acc: ").append(mAccuracy).append("\n");
        }

        if (mValues != null) {
            builder.append("Values: ");
            for (int i = 0; i < mValues.length; i++) {
                builder.append(mValues[i]);
                if (i < mValues.length - 1)
                    builder.append(", ");
            }
            builder.append("\n");
        }

        return builder.toString();
    }

    @Override
    public int compare(SensorData lhs, SensorData rhs) {

        if (null != lhs && null != rhs)
            return lhs.mSensor.getName().compareTo(rhs.mSensor.getName());

        return 0;
    }

    @Override
    public boolean equals(Object o) {
        if (null != o && o instanceof SensorData) {
            SensorData sensorData = (SensorData) o;
            if (this.mSensor == null || sensorData.mSensor == null)
                return false;
            if (this.mSensor == sensorData.mSensor)
                return true;
            if (this.mSensor.getName().equals(sensorData.mSensor.getName()))
                return true;

        }
        return false;
    }

}
