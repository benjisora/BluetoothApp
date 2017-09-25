package amiltone.bsaugues.bluetoothapp.data.manager.bluetooth;

import android.bluetooth.BluetoothDevice;

import java.util.List;

/**
 * Created by amiltonedev_dt013 on 25/09/2017.
 */

public interface BluetoothManager {

    void enableBluetooth();

    List<BluetoothDevice> getBondedDevices();

}
