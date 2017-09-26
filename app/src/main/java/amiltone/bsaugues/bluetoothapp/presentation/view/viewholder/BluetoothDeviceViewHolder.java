package amiltone.bsaugues.bluetoothapp.presentation.view.viewholder;

import android.bluetooth.BluetoothDevice;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import amiltone.bsaugues.bluetoothapp.R;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by amiltonedev_dt013 on 21/09/2017.
 */

public class BluetoothDeviceViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.activity_main_device_list_item_name)
    public TextView deviceName;

    @BindView(R.id.activity_main_device_list_item_address)
    public TextView deviceAddress;


    private final View mView;

    public interface OnComicClickedListener {
        void onDeviceClicked(int position);
    }

    public BluetoothDeviceViewHolder(View view) {
        super(view);
        mView = view;
        ButterKnife.bind(this, mView);
    }

    public void bind(BluetoothDevice bluetoothDevice){
        deviceName.setText(bluetoothDevice.getName());
        deviceAddress.setText(bluetoothDevice.getAddress());
    }

    public void bind(final OnComicClickedListener onComicClickedListener){

        mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onComicClickedListener != null) {
                    onComicClickedListener.onDeviceClicked(getAdapterPosition());
                }
            }
        });
    }


}