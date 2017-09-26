package amiltone.bsaugues.bluetoothapp.data.manager.bluetooth;

import android.bluetooth.BluetoothDevice;

/**
 * Created by amiltonedev_dt013 on 26/09/2017.
 */

public interface BluetoothManagerDiscoveryListener {

    void bluetoothDeviceFound(BluetoothDevice bluetoothDevice);

}