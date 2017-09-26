package amiltone.bsaugues.bluetoothapp.presentation.view.viewinterface;

import android.bluetooth.BluetoothDevice;

/**
 * Created by amiltonedev_dt013 on 26/09/2017.
 */

public interface MainView {

    void displayConnectedDeviceInfo(String name, String address);

    void displayDiscoveredDevice(BluetoothDevice device);

    void displayConnectedButtons();

    void hideConnectedButtons();

    void hideDiscoveryElements();


}
