package amiltone.bsaugues.bluetoothapp;

import android.app.Application;

import amiltone.bsaugues.bluetoothapp.data.ContentRepository;
import amiltone.bsaugues.bluetoothapp.data.manager.bluetooth.BluetoothManagerImpl;
import amiltone.bsaugues.bluetoothapp.data.manager.bluetooth.IOTBluetoothManager;

/**
 * Created by amiltonedev_dt013 on 25/09/2017.
 */

public class BluetoothApplication extends Application {

    private static BluetoothApplication ourInstance;
    private ContentRepository contentRepository;

    @Override
    public void onCreate() {
        super.onCreate();
        ourInstance = this;
        this.contentRepository = new ContentRepository(new BluetoothManagerImpl(getApplicationContext()));
    }

    public BluetoothApplication() {

    }

    public static BluetoothApplication getInstance() {
        return ourInstance;
    }

    public ContentRepository getContentRepository() {
        return contentRepository;
    }

}
