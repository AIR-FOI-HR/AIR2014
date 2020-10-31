package foi.hr.parksmart;

import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothAdapter;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String pozdrav = "Dobro došli";
        TextView gritings = (TextView) findViewById(R.id.textView);
        gritings.setText(pozdrav);
        ImageView myImageView = (ImageView) findViewById(R.id.imageView);
        myImageView.setImageResource(R.drawable.logo);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

                    if (!mBluetoothAdapter.isEnabled()) {
                        String pozdravx="BLUETOOTH nije ukljucen ";
                        TextView gritings = (TextView) findViewById(R.id.textView);
                        gritings.setText(pozdravx);
                    }
                 }
            },5000);
    }
}