package br.org.cesar.armrobotester.helper;

/**
 * Created by dlmc on 09/08/16.
 */

public interface Constants {

    // Message types sent from the BluetoothChatService Handler
    int MESSAGE_STATE_CHANGE = 1;
    int MESSAGE_READ = 2;
    int MESSAGE_WRITE = 3;
    int MESSAGE_DEVICE_NAME = 4;
    int MESSAGE_TOAST = 5;
    int MESSAGE_CONNECTION_FAILED = 6;
    int MESSAGE_CONNECTION_LOST = 7;


    // Key names received from the BluetoothChatService Handler
    String DEVICE_NAME = "device_name";
    String TOAST = "toast";

}
