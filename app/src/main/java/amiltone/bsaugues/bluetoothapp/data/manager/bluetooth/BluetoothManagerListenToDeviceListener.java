package amiltone.bsaugues.bluetoothapp.data.manager.bluetooth;

/**
 * Created by amiltonedev_dt013 on 26/09/2017.
 */

public interface BluetoothManagerListenToDeviceListener {
    void frameReceived(byte[] frame);
}
