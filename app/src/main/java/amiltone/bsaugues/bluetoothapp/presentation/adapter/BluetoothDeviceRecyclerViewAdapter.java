package amiltone.bsaugues.bluetoothapp.presentation.adapter;

import android.bluetooth.BluetoothDevice;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import amiltone.bsaugues.bluetoothapp.R;
import amiltone.bsaugues.bluetoothapp.presentation.view.viewholder.BluetoothDeviceViewHolder;


public class BluetoothDeviceRecyclerViewAdapter extends RecyclerView.Adapter<BluetoothDeviceViewHolder> {

    private List<BluetoothDevice> mValues;
    private final BluetoothDeviceViewHolder.OnComicClickedListener onComicClickedListener;

    public BluetoothDeviceRecyclerViewAdapter(BluetoothDeviceViewHolder.OnComicClickedListener listener) {
        onComicClickedListener = listener;
        mValues = new ArrayList<>();
    }

    public void addItem(BluetoothDevice bluetoothDevice) {
        mValues.add(bluetoothDevice);
        notifyItemInserted(getItemCount() - 1);
    }


    public BluetoothDevice getItem(int position) {
        return mValues.get(position);
    }

    @Override
    public BluetoothDeviceViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_main_device_list_item, parent, false);
        return new BluetoothDeviceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final BluetoothDeviceViewHolder holder, int position) {
        holder.bind(mValues.get(position));
        holder.bind(onComicClickedListener);
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }


}
