package foi.hr.parksmart;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Timer;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String pozdrav = "Dobro došli";
        TextView greetings = (TextView) findViewById(R.id.textView);
        greetings.setText(pozdrav);
        ImageView myImageView = (ImageView) findViewById(R.id.imageView);
        myImageView.setImageResource(R.drawable.logo);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                if (!mBluetoothAdapter.isEnabled()) {
                    Dialog dialog = new Dialog(MainActivity.this);
                    dialog.setContentView(R.layout.bluetooth_message);
                    dialog.setTitle("Poruka");
                    dialog.show();


                    do{
                        Button btnOn = (Button) dialog.findViewById(R.id.btnTurnOn);
                        btnOn.setOnClickListener(new View.OnClickListener() {
                            @Override

                            public void onClick(View v) {
                                final int REQUEST_ENABLE_BT = 1;
                                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
                            }
                        });
                    }while(mBluetoothAdapter.isEnabled());


                }
                else{
                    Intent intent = new Intent(MainActivity.this, MainScreen.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                }
            }
        }, 1000);

    }
}