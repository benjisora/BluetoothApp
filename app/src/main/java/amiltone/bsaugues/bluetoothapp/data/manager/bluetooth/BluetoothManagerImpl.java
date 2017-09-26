package amiltone.bsaugues.bluetoothapp.data.manager.bluetooth;

import android.bluetooth.BluetoothDevice;
import android.content.Context;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import rx.Observable;
import rx.Subscriber;

/**
 * Created by amiltonedev_dt013 on 26/09/2017.
 */

public class BluetoothManagerImpl implements BluetoothManager {

    private Context context;

    private IOTBluetoothManager iotBluetoothManager;

    private Map<String, BluetoothDevice> bluetoothDeviceMap;

    private String currentDeviceAddress;
    private String currentDeviceName;

    public BluetoothManagerImpl(Context context){
        this.context = context;
        iotBluetoothManager = new IOTBluetoothManager();
        bluetoothDeviceMap = new HashMap<>();
    }


    @Override
    public void enableBluetooth() {
        iotBluetoothManager.enableBluetooth();
    }

    @Override
    public Observable<BluetoothDevice> discoverBluetoothDevice() throws Exception {
        return Observable.create(new Observable.OnSubscribe<BluetoothDevice>() {
            @Override
            public void call(final Subscriber<? super BluetoothDevice> subscriber) {
                try {
                    bluetoothDeviceMap.clear();
                    iotBluetoothManager.registerDiscoveryReceiver(context);
                    iotBluetoothManager.addBluetoothManagerDiscoveryListener(new BluetoothManagerDiscoveryListener() {
                        @Override
                        public void bluetoothDeviceFound(BluetoothDevice bluetoothDevice) {

                            bluetoothDeviceMap.put(bluetoothDevice.getAddress(), bluetoothDevice);
                            subscriber.onNext(bluetoothDevice);
                        }
                    });
                    iotBluetoothManager.startDiscovery();

                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    @Override
    public void stopDevicesDiscovery() throws Exception {
        iotBluetoothManager.stopDiscovery();
        iotBluetoothManager.unregisterDiscoveryReceiver(context);
    }

    @Override
    public void connectToDevice(String deviceAddress) throws Exception {
        BluetoothDevice bluetoothDevice = bluetoothDeviceMap.get(deviceAddress);
        if (bluetoothDevice != null) {
            iotBluetoothManager.connectToDevice(bluetoothDevice);
            currentDeviceName = bluetoothDevice.getName();
        } else {
            iotBluetoothManager.connectToDevice(deviceAddress);
        }

        this.currentDeviceAddress = deviceAddress;
    }

    @Override
    public void sendATCommandToDevice(String command) throws Exception {
        iotBluetoothManager.sendATCommand(command);
    }

    @Override
    public Observable<byte[]> listenToDevice() {
        return Observable.create(new Observable.OnSubscribe<byte[]>() {
            @Override
            public void call(final Subscriber<? super byte[]> subscriber) {
                try {
                    iotBluetoothManager.addBluetoothManagerListenListener(new BluetoothManagerListenToDeviceListener() {
                        @Override
                        public void frameReceived(byte[] frame) {
                            subscriber.onNext(frame);
                        }
                    });
                    iotBluetoothManager.listenToDevice();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    @Override
    public void stopListenToDevice() {
        iotBluetoothManager.stopListenToDevice();
    }

    @Override
    public void disconnectFromBluetoothDevice() throws Exception {
        iotBluetoothManager.disconnectFromDevice();
        this.currentDeviceAddress = null;
    }

    @Override
    public Observable<Boolean> forgetAllBluetoothDevices() throws Exception {
        if (isDeviceConnected()) {
            bluetoothDeviceMap.clear();
            stopListenToDevice();
            disconnectFromBluetoothDevice();
            return Observable.just(true);
        } else {
            return Observable.just(false);
        }
    }

    @Override
    public Observable<Boolean> isBluetoothEnabled() {
        Boolean bluetoothEnabled = iotBluetoothManager.isBluetoothEnabled();
        return Observable.just(bluetoothEnabled);
    }

    @Override
    public boolean isDeviceConnected() throws Exception {
        return iotBluetoothManager.isConnected();
    }

    @Override
    public String getCurrentDeviceAddress() {
        return currentDeviceAddress;
    }

    @Override
    public String getCurrentDeviceName() {
        return currentDeviceName;
    }
}
