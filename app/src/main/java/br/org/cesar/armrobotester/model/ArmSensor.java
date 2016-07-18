package br.org.cesar.armrobotester.model;

/**
 * Created by bcb on 09/07/16.
 */
public class ArmSensor {

    // Dados são: Velocidade, Temperatura, Posição, Torque e ID.
    int mSpeed;
    int mTemperature;
    int mPosition;
    int mTorque;
    int mId;

    public ArmSensor(int id, int speed, int temperature, int pos, int torque) {
        mId = id;
        mSpeed = speed;
        mTemperature = temperature;
        mPosition = pos;
        mTorque = torque;
    }

    public int getSpeed() {
        return mSpeed;
    }

    public void setSpeed(int mSpeed) {
        this.mSpeed = mSpeed;
    }

    public int getTemperature() {
        return mTemperature;
    }

    public void setTemperature(int mTemperature) {
        this.mTemperature = mTemperature;
    }

    public int getPosition() {
        return mPosition;
    }

    public void setPosition(int mPosition) {
        this.mPosition = mPosition;
    }

    public int getTorque() {
        return mTorque;
    }

    public void setTorque(int mTorque) {
        this.mTorque = mTorque;
    }

    public int getId() {
        return mId;
    }

    public void setId(int mId) {
        this.mId = mId;
    }


}
