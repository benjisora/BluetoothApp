package amiltone.bsaugues.bluetoothapp.data;

import android.bluetooth.BluetoothDevice;

import java.util.concurrent.TimeUnit;

import amiltone.bsaugues.bluetoothapp.data.entity.BluetoothCommandResult;
import amiltone.bsaugues.bluetoothapp.data.manager.bluetooth.BluetoothManager;
import rx.Observable;
import rx.functions.Func0;
import rx.functions.Func1;

/**
 * Created by amiltonedev_dt013 on 25/09/2017.
 */

public class ContentRepository {

    private static final long TIMEOUT = 10000;

    private BluetoothManager bluetoothManager;


    public ContentRepository(BluetoothManager bluetoothManager) {
        this.bluetoothManager = bluetoothManager;
    }


    public void enableBluetooth(){
        bluetoothManager.enableBluetooth();
    }

    public void connectToDevice(String deviceAdress) {
        try {
            this.bluetoothManager.connectToDevice(deviceAdress);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //TODO:create class
    public Observable<BluetoothCommandResult> listenToDevice() {
        return Observable.defer(new Func0<Observable<BluetoothCommandResult>>() {
            @Override
            public Observable<BluetoothCommandResult> call() {
                return bluetoothManager.listenToDevice().map(new Func1<byte[], BluetoothCommandResult>() {
                    @Override
                    public BluetoothCommandResult call(byte[] bytes) {
                        //TODO : analyser le retour
                        return new BluetoothCommandResult(BluetoothCommandResult.Result.SUCCESS);
                    }
                }).timeout(TIMEOUT, TimeUnit.MILLISECONDS, Observable.<BluetoothCommandResult>empty());
            }
        });
    }

    public Observable<BluetoothDevice> discoverBluetoothDevice() {
        return Observable.defer(new Func0<Observable<BluetoothDevice>>() {
            @Override
            public Observable<BluetoothDevice> call() {
                try {
                    return bluetoothManager.discoverBluetoothDevice().timeout(TIMEOUT, TimeUnit.MILLISECONDS, Observable.<BluetoothDevice>empty());
                } catch (Exception e) {
                    return Observable.error(e);
                }
            }
        });
    }

    public void disconnectFromDevice() {
        try {
            bluetoothManager.disconnectFromBluetoothDevice();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Observable<Boolean> forgetAllBluetoothDevices() {
        return Observable.defer(new Func0<Observable<Boolean>>() {
            @Override
            public Observable<Boolean> call() {
                try {
                    return bluetoothManager.forgetAllBluetoothDevices();
                } catch (Exception e) {
                    return Observable.error(e);
                }
            }
        });
    }

    public Observable<Boolean> isBluetoothEnabled() {
        return bluetoothManager.isBluetoothEnabled();
    }

    public String getCurrentDeviceAddress() {
        return bluetoothManager.getCurrentDeviceAddress();
    }

    public String getCurrentDeviceName() {
        return bluetoothManager.getCurrentDeviceName();
    }

    public void sendCommand(String command) {
        try {
            bluetoothManager.sendATCommandToDevice(command);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void stopDeviceDiscovery() {
        try {
            bluetoothManager.stopDevicesDiscovery();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void stopListenToDevice() {
        bluetoothManager.stopListenToDevice();
    }

    public boolean isDeviceConnected() {
        try {
            return bluetoothManager.isDeviceConnected();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


}
