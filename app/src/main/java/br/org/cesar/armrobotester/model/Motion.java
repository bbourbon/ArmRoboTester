package br.org.cesar.armrobotester.model;

import android.util.Log;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Locale;

import static br.org.cesar.armrobotester.MainNaviActivity.TAG;

/**
 * Created by bcb on 03/07/16.
 */
public class Motion {

    public static final int INVALID_VALUE = -1;
    public static final int MAX_ROTATION_VALUE = 360;
    public static final int MIN_ROTATION_VALUE = 0;

    private int rotationValue;
    private boolean shoulderMotion = false;
    private boolean elbowMotion = false;
    private boolean wristMotion = false;

    public Motion() {
        setRotationValue(INVALID_VALUE);
    }

    public Motion(boolean shoulder, boolean elbow, boolean wrist, int v) {
        shoulderMotion = shoulder;
        elbowMotion = elbow;
        wristMotion = wrist;
        setRotationValue(checkValue(v));
    }

    public static String decodeResult(byte[] inBuffer, int read) {
        // DECODE: Motion result values;
        String response = null;

        byte toDecode[];
        toDecode = Arrays.copyOf(inBuffer, read);

        try {
            response = new String(toDecode, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        Log.d(TAG, "Decoded response: " + response);
        return response;
    }

    private int checkValue(int v) {
        int result;

        if (v >= MIN_ROTATION_VALUE && v <= MAX_ROTATION_VALUE) {
            result = v;
        } else {
            result = INVALID_VALUE;
        }

        return result;
    }

    public int getRotationValue() {
        return rotationValue;
    }

    public void setRotationValue(int rotationValue) {
        this.rotationValue = checkValue(rotationValue);
    }

    @Override
    public String toString() {
        String s = null;

        try {
            s = new String(commandArduino(), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            Log.e(TAG, e.getLocalizedMessage());
        }

        return s;
    }

    public byte[] commandArduino() {
        //TODO: Convert any motion to a byte sequence
        // [ocp180] - [0,0,0,000] - [0|o,0|c,0|p,000-360]
        // o - Shoulder; c - Elbow; p - Wrist
        String command = "";
        if (shoulderMotion) {
            command += "o";
        } else {
            command += "0";
        }

        if (elbowMotion) {
            command += "c";
        } else {
            command += "0";
        }

        if (wristMotion) {
            command += "p";
        } else {
            command += "0";
        }

        String value = "000";
        if (rotationValue != INVALID_VALUE) {
            value = String.format(Locale.getDefault(), "%03d", rotationValue);
        }
        command += value;

        Log.d(TAG, "Command: " + command);
        byte com[] = command.getBytes();

        return com;
    }


    private void dec() {
        //M,1,T,000

        // M - Motor, 1-4 ID do motor, Sensor (T-temperatura, V-Velocidade, Q-Torque, A-Angulo
        // ### - valor do sensor

        // ROK
        // RFAIL


    }

}
