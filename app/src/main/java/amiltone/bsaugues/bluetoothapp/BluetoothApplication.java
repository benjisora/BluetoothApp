package amiltone.bsaugues.bluetoothapp;

import amiltone.bsaugues.bluetoothapp.data.ContentRepository;
import amiltone.bsaugues.bluetoothapp.data.manager.bluetooth.BluetoothManagerImpl;

/**
 * Created by amiltonedev_dt013 on 25/09/2017.
 */

public class BluetoothApplication {

    private static BluetoothApplication ourInstance;
    private ContentRepository contentRepository;


    BluetoothApplication() {
        this.contentRepository = new ContentRepository(new BluetoothManagerImpl());
    }

    public BluetoothApplication getInstance() {
        if (ourInstance == null) {
            ourInstance = new BluetoothApplication();
        }
        return ourInstance;
    }


}
