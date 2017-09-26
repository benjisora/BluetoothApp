package amiltone.bsaugues.bluetoothapp.presentation.ui.activity;

import android.bluetooth.BluetoothDevice;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import amiltone.bsaugues.bluetoothapp.R;
import amiltone.bsaugues.bluetoothapp.presentation.adapter.BluetoothDeviceRecyclerViewAdapter;
import amiltone.bsaugues.bluetoothapp.presentation.presenter.ActivityPresenter;
import amiltone.bsaugues.bluetoothapp.presentation.view.viewholder.BluetoothDeviceViewHolder;
import amiltone.bsaugues.bluetoothapp.presentation.view.viewinterface.MainView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class MainActivity extends AppCompatActivity implements MainView, BluetoothDeviceViewHolder.OnComicClickedListener {

    @BindView(R.id.activity_main_device_discovered)
    public TextView deviceDiscovered;

    @BindView(R.id.activity_main_discovery_list_title)
    public TextView discoveryListTitle;

    @BindView(R.id.activity_main_device_list)
    public RecyclerView recyclerView;

    @BindView(R.id.activity_main_device_send_command_button)
    public Button sendCommandButton;

    private ActivityPresenter activityPresenter;
    private BluetoothDeviceRecyclerViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        activityPresenter = new ActivityPresenter();
        activityPresenter.setMainView(this);

        recyclerView = findViewById(R.id.activity_main_device_list);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new BluetoothDeviceRecyclerViewAdapter(this);
        recyclerView.setAdapter(adapter);

        activityPresenter.initializeBluetoothConnexion();

    }

    @OnClick(R.id.activity_main_device_send_command_button)
    public void sendCommandClick() {
        activityPresenter.sendCommandToDevice("test");
    }

    @Override
    public void displayConnectedDeviceInfo(String name, String address) {
        deviceDiscovered.setBackgroundColor(Color.GREEN);
        deviceDiscovered.setText("connected to : " + name + " (" + address +")");
    }

    @Override
    public void displayDiscoveredDevice(BluetoothDevice device) {
        adapter.addItem(device);
    }

    @Override
    public void displayConnectedButtons() {
        sendCommandButton.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideConnectedButtons() {
        sendCommandButton.setVisibility(View.GONE);
    }

    @Override
    public void hideDiscoveryElements() {
        discoveryListTitle.setVisibility(View.GONE);
        recyclerView.setVisibility(View.GONE);
    }

    @Override
    public void onDeviceClicked(int position) {
        activityPresenter.stopDeviceDiscovery();
        activityPresenter.connectToDevice(adapter.getItem(position).getAddress());
    }

}
