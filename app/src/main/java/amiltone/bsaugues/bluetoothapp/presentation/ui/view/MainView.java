package amiltone.bsaugues.bluetoothapp.presentation.ui.view;

import android.bluetooth.BluetoothDevice;

/**
 * Created by amiltonedev_dt013 on 26/09/2017.
 */

public interface MainView {

    void displayConnectedDeviceInfo(BluetoothDevice bluetoothDevice);

    void displayDiscoveredDevice();

}
