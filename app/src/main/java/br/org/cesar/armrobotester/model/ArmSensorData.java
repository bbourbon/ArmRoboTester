package br.org.cesar.armrobotester.model;

/**
 * Created by bcb on 09/07/16.
 */
public final class ArmSensorData extends ArmSensor {


    private long mTimeStamp;

    public ArmSensorData(int id, int speed, int temperature, int pos, int torque,
                         long timeStamp) {
        super(id, speed, temperature, pos, torque);
        mTimeStamp = timeStamp;
    }

    public long getTimeStamp() {
        return mTimeStamp;
    }

    public void setTimeStamp(long mTimeStamp) {
        this.mTimeStamp = mTimeStamp;
    }

}
