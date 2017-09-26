package amiltone.bsaugues.bluetoothapp.presentation.presenter;

import android.bluetooth.BluetoothDevice;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import amiltone.bsaugues.bluetoothapp.BluetoothApplication;
import amiltone.bsaugues.bluetoothapp.data.ContentRepository;
import amiltone.bsaugues.bluetoothapp.data.entity.BluetoothCommandResult;
import amiltone.bsaugues.bluetoothapp.presentation.ui.view.MainView;
import rx.Subscriber;
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

    public ActivityPresenter() {
        contentRepository = BluetoothApplication.getInstance().getContentRepository();
        bluetoothDevices = new ArrayList<>();
    }

    public void setMainView(MainView mainView) {
        this.mainView = mainView;
    }

    public void activateBluetooth() {
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
                            Log.d(TAG, "isBluetoothEnabled onNext: the bluetooth is enabled");
                            discoverBluetoothDevice();
                        } else {
                            Log.d(TAG, "isBluetoothEnabled onNext: the bluetooth is disabled");
                            contentRepository.enableBluetooth();
                        }

                    }
                });
    }

    public void discoverBluetoothDevice() {
        contentRepository.discoverBluetoothDevice().subscribeOn(Schedulers.io())
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
                        Log.d(TAG, "discoverBluetoothDevice onNext: " + bluetoothDevice.getAddress());

                        bluetoothDevices.add(bluetoothDevice);
                        mainView.displayConnectedDeviceInfo(bluetoothDevice);

                        //connectToDevice(bluetoothDevice.getAddress());

                    }
                });
    }

    private void stopDeviceDiscovery() {
        contentRepository.stopDeviceDiscovery();
    }

    public void connectToDevice(String address) {
        contentRepository.connectToDevice(address);
    }

    public boolean isDeviceConnected() {
        return contentRepository.isDeviceConnected();
    }

    public void listenToDevice() {
        contentRepository.listenToDevice().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<BluetoothCommandResult>() {
                    @Override
                    public void onCompleted() {
                        stopListenToDevice();
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        stopListenToDevice();
                    }

                    @Override
                    public void onNext(BluetoothCommandResult result) {
                        if (result.getResult() == BluetoothCommandResult.Result.SUCCESS) {
                            Log.d(TAG, "onNext: ");
                            mainView.displayDiscoveredDevice();
                        }
                    }
                });
    }

    public void sendCommand(String command) {
        if (isDeviceConnected()) {
            contentRepository.sendCommand(command);
        }
    }

    public void stopListenToDevice() {
        contentRepository.stopListenToDevice();
    }

    public void disconnectFromDevice() {
        contentRepository.disconnectFromDevice();
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

                    }

                    @Override
                    public void onNext(Boolean success) {

                    }
                });
    }

    public String getCurrentDeviceAddress() {
        return contentRepository.getCurrentDeviceAddress();
    }

    public String getCurrentDeviceName() {
        return contentRepository.getCurrentDeviceName();
    }


}
