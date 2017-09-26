package amiltone.bsaugues.bluetoothapp.presentation.ui.activity;

import android.bluetooth.BluetoothDevice;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import amiltone.bsaugues.bluetoothapp.R;
import amiltone.bsaugues.bluetoothapp.presentation.presenter.ActivityPresenter;
import amiltone.bsaugues.bluetoothapp.presentation.ui.view.MainView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements MainView {

    @BindView(R.id.activity_main_device_discovered)
    public TextView deviceDiscovered;

    @BindView(R.id.activity_main_device_name)
    public TextView deviceName;

    @BindView(R.id.activity_main_device_address)
    public TextView deviceAddress;

    private ActivityPresenter activityPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        activityPresenter = new ActivityPresenter();
        activityPresenter.setMainView(this);

        activityPresenter.activateBluetooth();

    }

    @Override
    public void displayConnectedDeviceInfo(BluetoothDevice bluetoothDevice) {
        deviceName.setText(bluetoothDevice.getName());
        deviceAddress.setText(bluetoothDevice.getAddress());
    }

    @Override
    public void displayDiscoveredDevice() {
        deviceDiscovered.setBackgroundColor(Color.GREEN);
    }
}
