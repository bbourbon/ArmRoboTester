package br.org.cesar.armrobotester.model;

import android.hardware.Sensor;

import java.util.Comparator;

/**
 * Created by bcb on 24/07/16.
 */
public class SensorData implements Comparator<SensorData> {

    public long timestamp;
    private Sensor mSensor;

    public SensorData(Sensor sensor) {
        mSensor = sensor;
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
