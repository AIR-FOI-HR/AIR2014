package foi.hr.parksmart;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {

    private LayoutInflater layoutInflater;
    private List<BluetoothDevice> bluetoothLeDevice;
    private OnBluetoothDeviceListener mOnBluetoothDeviceListener;

    public Adapter(Context context, List<BluetoothDevice> bleDevices, OnBluetoothDeviceListener onBluetoothDeviceListener) {
        this.layoutInflater = LayoutInflater.from(context);
        this.bluetoothLeDevice = bleDevices;
        this.mOnBluetoothDeviceListener = onBluetoothDeviceListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.bluetooth_device_list_custom_view, parent, false);
        return new ViewHolder(view,mOnBluetoothDeviceListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String name = bluetoothLeDevice.get(position).getName();
        holder.txtDeviceName.setText(name);
        String address = bluetoothLeDevice.get(position).getAddress();
        holder.txtDeviceAddress.setText(address);
    }

    @Override
    public int getItemCount() {
        return bluetoothLeDevice.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView txtDeviceName,txtDeviceAddress;
        OnBluetoothDeviceListener onBluetoothDeviceListener;
        public ViewHolder(@NonNull View itemView, OnBluetoothDeviceListener onBluetoothDeviceListener) {
            super(itemView);
            txtDeviceName=(TextView) itemView.findViewById(R.id.txt_device_name);
            txtDeviceAddress=(TextView) itemView.findViewById(R.id.txt_MAC_address);
            this.onBluetoothDeviceListener = onBluetoothDeviceListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onBluetoothDeviceListener.onBluetoothDeviceClick(getAdapterPosition());
        }
    }

    public interface OnBluetoothDeviceListener {
        void onBluetoothDeviceClick(int position);
    }
}
