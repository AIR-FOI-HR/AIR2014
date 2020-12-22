package foi.hr.parksmart;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {
    private LayoutInflater layoutInflater;
    private List<String> deviceName = new ArrayList<>();
    private List<String> deviceAddress = new ArrayList<>();
    private OnBluetoothDeviceListener mOnBluetoothDeviceListener;

    public Adapter(Context context, List<BluetoothDevice> bleDevices, OnBluetoothDeviceListener onBluetoothDeviceListener) {
        this.layoutInflater = LayoutInflater.from(context);
        for (BluetoothDevice bleDevice : bleDevices)
        {
            deviceName.add(bleDevice.getName());
            deviceAddress.add(bleDevice.getAddress());
        }

        this.mOnBluetoothDeviceListener=onBluetoothDeviceListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.bluetooth_device_list_custom_view, parent, false);

        return new ViewHolder(view,mOnBluetoothDeviceListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String name = deviceName.get(position);
        holder.txtDeviceName.setText(name);
        String address = deviceAddress.get(position);
        holder.txtDeviceAddress.setText(address);
    }

    @Override
    public int getItemCount() {
        return deviceName.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView txtDeviceName,txtDeviceAddress;
        OnBluetoothDeviceListener onBluetoothDeviceListener;
        public ViewHolder(@NonNull View itemView, OnBluetoothDeviceListener onBluetoothDeviceListener) {
            super(itemView);
            txtDeviceName=(TextView) itemView.findViewById(R.id.txt_device_name);
            txtDeviceAddress=(TextView) itemView.findViewById(R.id.txt_MAC_address);
            this.onBluetoothDeviceListener=onBluetoothDeviceListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            //Log.i("onClick", "radi");
            onBluetoothDeviceListener.onBluetoothDeviceClick(getAdapterPosition());
        }
    }

    public interface OnBluetoothDeviceListener {
        void onBluetoothDeviceClick(int position);
    }
}