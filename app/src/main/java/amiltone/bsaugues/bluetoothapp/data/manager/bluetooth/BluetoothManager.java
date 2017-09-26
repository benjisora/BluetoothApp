package amiltone.bsaugues.bluetoothapp.data.manager.bluetooth;

import android.bluetooth.BluetoothDevice;

import java.util.List;

import rx.Observable;

/**
 * Created by amiltonedev_dt013 on 25/09/2017.
 */

public interface BluetoothManager {

    void enableBluetooth();

    Observable<BluetoothDevice> discoverBluetoothDevice() throws Exception;

    void stopDevicesDiscovery() throws Exception;

    void connectToDevice(String deviceAddress) throws Exception;

    void sendATCommandToDevice(String command) throws Exception;

    Observable<byte[]> listenToDevice();

    void stopListenToDevice();

    void disconnectFromBluetoothDevice() throws Exception;

    Observable<Boolean> forgetAllBluetoothDevices() throws Exception;

    Observable<Boolean> isBluetoothEnabled();

    boolean isDeviceConnected() throws Exception;

    String getCurrentDeviceAddress();

    String getCurrentDeviceName();

}
