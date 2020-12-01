package foi.hr.parksmart;

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
    private List<String> deviceName;
    private List<String> deviceAddress;

    public Adapter(Context context, List<String> deviceName, List<String> deviceAddress) {
        this.layoutInflater = LayoutInflater.from(context);
        this.deviceName = deviceName;
        this.deviceAddress = deviceAddress;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.bluetooth_device_list_custom_view, parent, false);

        return new ViewHolder(view);
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

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtDeviceName,txtDeviceAddress;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtDeviceName=(TextView) itemView.findViewById(R.id.txt_device_name);
            txtDeviceAddress=(TextView) itemView.findViewById(R.id.txt_MAC_address);
        }
    }

}