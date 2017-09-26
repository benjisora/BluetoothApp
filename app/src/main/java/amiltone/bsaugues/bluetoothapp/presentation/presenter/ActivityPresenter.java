package amiltone.bsaugues.bluetoothapp.presentation.presenter;

import android.bluetooth.BluetoothDevice;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import amiltone.bsaugues.bluetoothapp.BluetoothApplication;
import amiltone.bsaugues.bluetoothapp.data.entity.BluetoothCommandResult;
import amiltone.bsaugues.bluetoothapp.data.repository.ContentRepository;
import amiltone.bsaugues.bluetoothapp.presentation.view.viewinterface.MainView;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by amiltonedev_dt013 on 26/09/2017.
 */

public class ActivityPresenter {

    private static final String TAG = "ActivityPresenter";

    private ContentRepository contentRepository;

    private MainView mainView;

    private List<BluetoothDevice> bluetoothDevices;

    private Subscription discoverBluetoothDevice;
    private Subscription listenBluetoothDevice;


    public ActivityPresenter() {
        contentRepository = BluetoothApplication.getInstance().getContentRepository();
        bluetoothDevices = new ArrayList<>();
    }

    public void setMainView(MainView mainView) {
        this.mainView = mainView;
    }

    private void activateBluetooth() {
        contentRepository.isBluetoothEnabled().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Boolean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(Boolean success) {
                        if (success) {
                            Log.d(TAG, "isBluetoothEnabled onNext: bluetooth is enabled");
                            if (!isDeviceConnected()) {
                                mainView.hideConnectedButtons();
                                discoverBluetoothDevice();
                            } else {
                                Log.d(TAG, "isBluetoothEnabled onNext: we are already connected to a device");
                                mainView.displayConnectedDeviceInfo(getCurrentDeviceName(), getCurrentDeviceAddress());
                            }
                        } else {
                            Log.d(TAG, "isBluetoothEnabled onNext: bluetooth disabled");
                            contentRepository.enableBluetooth();
                            discoverBluetoothDevice();
                        }
                    }
                });
    }

    public void discoverBluetoothDevice() {
        Log.d(TAG, "discoverBluetoothDevice : start discovering");
        discoverBluetoothDevice = contentRepository.discoverBluetoothDevice().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<BluetoothDevice>() {

                    @Override
                    public void onCompleted() {
                        stopDeviceDiscovery();
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        stopDeviceDiscovery();
                    }

                    @Override
                    public void onNext(BluetoothDevice bluetoothDevice) {
                        Log.d(TAG, "discoverBluetoothDevice onNext: Device discovered : " + bluetoothDevice.getAddress());

                        bluetoothDevices.add(bluetoothDevice);
                        mainView.displayDiscoveredDevice(bluetoothDevice);

                    }
                });
    }

    public void stopDeviceDiscovery() {
        if (!discoverBluetoothDevice.isUnsubscribed()) {
            discoverBluetoothDevice.unsubscribe();
            contentRepository.stopDeviceDiscovery();
        }
        Log.d(TAG, "stopDeviceDiscovery: stopped the discovery");
    }

    public void connectToDevice(String address) {
        contentRepository.connectToDevice(address);
        if (isDeviceConnected()) {
            Log.d(TAG, "connectToDevice: connected to device");
            mainView.displayConnectedDeviceInfo(getCurrentDeviceName(), getCurrentDeviceAddress());
            mainView.displayConnectedButtons();
            mainView.hideDiscoveryElements();
        } else {
            Log.d(TAG, "connectToDevice: didn't connect to device");
        }
    }

    private boolean isDeviceConnected() {
        return contentRepository.isDeviceConnected();
    }

    public void sendCommandToDevice(String command) {
        if (isDeviceConnected()) {
            sendCommand(command);
            listenToDevice();
        }
    }

    private void sendCommand(String command) {
        Log.d(TAG, "sendCommand: Sending a command to the server : " + command);
        contentRepository.sendCommand(command);
    }

    private void listenToDevice() {
        Log.d(TAG, "listenToDevice: listening to the device : ");
        listenBluetoothDevice = contentRepository.listenToDevice().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<BluetoothCommandResult>() {
                    @Override
                    public void onCompleted() {
                        stopListenToDevice();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "listenToDevice onNext: error receiving answer from device", e);
                        stopListenToDevice();
                    }

                    @Override
                    public void onNext(BluetoothCommandResult result) {
                        if (result.getResult() == BluetoothCommandResult.Result.SUCCESS) {
                            Log.d(TAG, "listenToDevice onNext: Received a GOOD answer from device");
                            stopListenToDevice();
                        } else {
                            Log.d(TAG, "listenToDevice onNext: Received a BAD answer from device");
                        }
                    }
                });
    }


    private void stopListenToDevice() {
        if (!listenBluetoothDevice.isUnsubscribed()) {
            listenBluetoothDevice.unsubscribe();
            contentRepository.stopListenToDevice();
        }
        Log.d(TAG, "stopListenToDevice: stopped the listening");
    }

    public void forgetAllBluetoothDevices() {
        contentRepository.forgetAllBluetoothDevices().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Boolean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "forgetAllBluetoothDevices onNext: error forgetting all bluetooth devices", e);
                    }

                    @Override
                    public void onNext(Boolean success) {
                        if (success) {
                            Log.d(TAG, "forgetAllBluetoothDevices onNext: forgot all bluetooth devices");
                        } else {
                            Log.d(TAG, "forgetAllBluetoothDevices onNext: was not connected to any device");
                        }
                    }
                });
    }


    private String getCurrentDeviceAddress() {
        return contentRepository.getCurrentDeviceAddress();
    }

    private String getCurrentDeviceName() {
        return contentRepository.getCurrentDeviceName();
    }

    public void initializeBluetoothConnexion() {
        activateBluetooth();
    }


}
