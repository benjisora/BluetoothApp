package amiltone.bsaugues.bluetoothapp.data.manager.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by amiltonedev_dt013 on 25/09/2017.
 */

public class BluetoothManagerImpl implements BluetoothManager {

    private BluetoothAdapter bluetoothAdapter;
    private List<BluetoothDevice> bluetoothDevices;

    public BluetoothManagerImpl() {
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    }

    private boolean isBluetoothAvailable() {
        return bluetoothAdapter != null;
    }

    @Override
    public List<BluetoothDevice> getBondedDevices() {
        bluetoothDevices = new ArrayList<>(bluetoothAdapter.getBondedDevices());
        return bluetoothDevices;
    }


    @Override
    public void enableBluetooth() {
        if (isBluetoothAvailable() && !bluetoothAdapter.isEnabled()) {
            bluetoothAdapter.enable();
        }
    }

}
