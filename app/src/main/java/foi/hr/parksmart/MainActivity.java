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

    public Dialog dialog;
    TextView printWarning;
    BluetoothAdapter mBluetoothAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView greetings = (TextView) findViewById(R.id.textView);
        greetings.setText("Dobro došli!");
        ImageView myImageView = (ImageView) findViewById(R.id.imageView);
        myImageView.setImageResource(R.drawable.logo);

        dialog = new Dialog(MainActivity.this);
        dialog.setContentView(R.layout.bluetooth_message);

        new Handler().postDelayed(new Runnable() {
            public void run() {
                mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                if (!mBluetoothAdapter.isEnabled()) {
                    printWarning = (TextView) dialog.findViewById(R.id.txtWarning);
                    printWarning.setText("Bluetooth Vam nije uključen, kako bi nastavili dalje koristiti aplikaciju uključite bluetooth pritiskom na tipku 'UKLJUČI'");
                    dialog.show();
                }
                else {
                    GoToMainScreen();
                }
            }
        }, 1500);

        Button btnOn = (Button) dialog.findViewById(R.id.btnTurnOn);
        btnOn.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                final int REQUEST_ENABLE_BT = 1;
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            }
        });

    }
    /*
    Metoda onActivityResult(int requestCode, int resultCode, Intent data) poziva se nakon završavanja aktivonsti zahtjeva
    requestCode - označava stanje početnog zahtjeva
    resultCode - što vraća zahtjev
    data - dodatana podatak ukoliko postoji
    provjerava vraćeni zahtjev i izvršava jedan od IF uvjeta
     */
    protected  void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        if(resultCode==RESULT_OK){
            GoToMainScreen();
        }else if(resultCode==RESULT_CANCELED){
            printWarning = (TextView) dialog.findViewById(R.id.txtWarning);
            printWarning.setText("Ukoliko ne dopustite uključivanje ili sami ne uključite Bluetooth, aplikacija ne može nastaviti s daljnjim radom.");
            dialog.show();
        }
    }

    /*
    GoToMainScreen() poziva klasu Intent te se objekt salje u startActivity(intent) te omogućuje otvaranja novog zaslona
     */
    protected void GoToMainScreen(){
        Intent intent = new Intent(MainActivity.this, MainScreen.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }
}