package amiltone.bsaugues.bluetoothapp.data.manager.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

/**
 * Created by amiltonedev_dt013 on 25/09/2017.
 */

public class IOTBluetoothManager {

    private static final String MY_BLUETOOTH_UUID = "00001101-0000-1000-8000-00805F9B34FB";

    private static final long FRAME_LISTENING_INTERVAL_MILLIS = 100;
    private static final byte CR = (byte) 0x0D;
    private static final byte LF = (byte) 0x0A;
    private static final byte FRAME_DATA_START = (byte) 0x7E;
    private static final byte DATA_LENGTH_OFFSET = 3;

    private BluetoothAdapter bluetoothAdapter;
    private BluetoothManagerDiscoveryListener bluetoothManagerDiscoveryListener;
    private BluetoothManagerListenToDeviceListener bluetoothManagerListenToDeviceListener;
    private BluetoothSocket currentBluetoothSocket;

    private Timer frameListeningTimer;
    private List<Byte> frameBuffer;

    private final BroadcastReceiver bluetoothActionFoundReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                bluetoothManagerDiscoveryListener.bluetoothDeviceFound(device);
            }
        }
    };

    public IOTBluetoothManager() {
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        frameBuffer = new ArrayList<>();
    }

    private boolean isBluetoothAvailable() {
        return bluetoothAdapter != null;
    }

    public void enableBluetooth(){
        if(this.bluetoothAdapter != null && !this.bluetoothAdapter.isEnabled())
        this.bluetoothAdapter.enable();
    }

    /**
     * Discover :
     */


    public void addBluetoothManagerDiscoveryListener(BluetoothManagerDiscoveryListener listener) {
        this.bluetoothManagerDiscoveryListener = listener;
    }

    public void registerDiscoveryReceiver(Context context) {
        IntentFilter filterFound = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        context.registerReceiver(bluetoothActionFoundReceiver, filterFound);
    }

    public void unregisterDiscoveryReceiver(Context context) {
        context.unregisterReceiver(bluetoothActionFoundReceiver);
    }

    public void startDiscovery() {
        bluetoothAdapter.startDiscovery();
    }

    public void stopDiscovery() {
        bluetoothAdapter.cancelDiscovery();
    }

    /**
     * Connect :
     */

    public void connectToDevice(BluetoothDevice device) throws Exception {
        try {
            UUID uuid = UUID.fromString(MY_BLUETOOTH_UUID);

            currentBluetoothSocket = device.createRfcommSocketToServiceRecord(uuid);
            currentBluetoothSocket.connect();
        } catch (Exception e) {
            if (currentBluetoothSocket != null) {
                try {
                    currentBluetoothSocket.close();
                } catch (IOException e1) {

                    throw e1;
                }
            }
            throw e;
        }
    }

    public void connectToDevice(String deviceAddress) throws Exception {
        try {
            BluetoothDevice device = bluetoothAdapter.getRemoteDevice(deviceAddress);

            UUID uuid = UUID.fromString(MY_BLUETOOTH_UUID);

            currentBluetoothSocket = device.createRfcommSocketToServiceRecord(uuid);
            currentBluetoothSocket.connect();
        } catch (Exception e) {
            if (currentBluetoothSocket != null) {
                try {
                    currentBluetoothSocket.close();
                } catch (IOException e1) {
                    throw e1;
                }
            }
            throw e;
        }
    }

    public void disconnectFromDevice() throws Exception {
        if (currentBluetoothSocket != null) {
            currentBluetoothSocket.close();
            currentBluetoothSocket = null;
        }
    }

    public boolean isConnected() {
        if (currentBluetoothSocket != null) {
            if (!currentBluetoothSocket.isConnected()) {
                return false;
            }
            return true;
        }
        return false;
    }

    /**
     * Send AT command :
     */

    public void sendATCommand(String ATCommand) throws Exception {
        OutputStream outputStream = currentBluetoothSocket.getOutputStream();
        outputStream.write(ATCommand.getBytes());
    }

    /**
     * Listening :
     */

    public void addBluetoothManagerListenListener(BluetoothManagerListenToDeviceListener listener) {
        this.bluetoothManagerListenToDeviceListener = listener;
    }

    public void listenToDevice() throws Exception {
        stopTimerListeningDevice();

        //We reset the input stream
        int bytesAvailable = currentBluetoothSocket.getInputStream().available();
        if (bytesAvailable > 0) {
            byte[] buffer = new byte[bytesAvailable];
            currentBluetoothSocket.getInputStream().read(buffer);
        }

        frameListeningTimer = new Timer();
        frameListeningTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                try {
                    InputStream inputStream = currentBluetoothSocket.getInputStream();

                    int bytesAvailable = inputStream.available();
                    if (bytesAvailable > 0) {
                        byte[] buffer = new byte[bytesAvailable];

                        inputStream.read(buffer);
                        savePreviousBuffer(buffer);

                        sendBuffer();
                        resetBuffer();


                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, FRAME_LISTENING_INTERVAL_MILLIS, FRAME_LISTENING_INTERVAL_MILLIS);
    }

    private void savePreviousBuffer(byte[] buffer) {
        for (byte b : buffer) {
            frameBuffer.add(b);
        }
    }

    private int checkDataFrame(int frameStartIndex) throws Exception {
        int dataLength = frameBuffer.get(frameStartIndex + DATA_LENGTH_OFFSET) & 0xff;
        if (frameBuffer.get(frameStartIndex + DATA_LENGTH_OFFSET + (dataLength + 1) + 1) == FRAME_DATA_START) {
            return frameStartIndex + DATA_LENGTH_OFFSET + (dataLength + 1) + 1;
        } else {
            throw new Exception();
        }
    }

    private void resetBuffer() {
        frameBuffer = new ArrayList<>();
    }

    private void sendBuffer() {
        if (bluetoothManagerListenToDeviceListener != null) {
            bluetoothManagerListenToDeviceListener.frameReceived(toPrimitives(frameBuffer));
        }
    }

    public static byte[] toPrimitives(List<Byte> bytes) {
        byte[] primitiveBytes = new byte[bytes.size()];

        for (int i = 0; i < bytes.size(); i++) {
            primitiveBytes[i] = bytes.get(i);
        }
        return primitiveBytes;
    }

    public void stopListenToDevice() {
        stopTimerListeningDevice();
    }

    private void stopTimerListeningDevice() {
        if (frameListeningTimer != null) {
            frameListeningTimer.cancel();
            frameListeningTimer = null;
        }
    }

    public boolean isBluetoothEnabled() {
        return bluetoothAdapter != null && bluetoothAdapter.isEnabled();
    }

    //Used for testing purposes
    void listenToDevice(final InputStream inputStream) throws IOException {
        stopListenToDevice();
        //We reset the input stream
        int bytesAvailable = inputStream.available();
        if (bytesAvailable > 0) {
            byte[] buffer = new byte[bytesAvailable];
            inputStream.read(buffer);
        }
        frameListeningTimer = new Timer();
        frameListeningTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                try {

                    int bytesAvailable = inputStream.available();
                    if (bytesAvailable > 0) {
                        byte[] buffer = new byte[bytesAvailable];

                        inputStream.read(buffer);

                        bluetoothManagerListenToDeviceListener.frameReceived(buffer);

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, 10, 10);
    }

}
