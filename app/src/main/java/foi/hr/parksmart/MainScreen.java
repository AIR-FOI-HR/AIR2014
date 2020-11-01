package foi.hr.parksmart;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.view.View.OnClickListener;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainScreen extends AppCompatActivity  {

    private static final int REQUEST_PHONE_CALL = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);

        FloatingActionButton sosGumb = findViewById(R.id.btnSos);
        sosGumb.setOnClickListener((View v) -> {
            if (ContextCompat.checkSelfPermission(MainScreen.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(MainScreen.this, new String[]{Manifest.permission.CALL_PHONE},REQUEST_PHONE_CALL);
            }
            else {
                callSos(v);
            }
        });
    }


    public void hideButton(View view) {
        FloatingActionButton BtnZvuk = findViewById(R.id.btnZvuk);
        FloatingActionButton BtnZvukOff = findViewById(R.id.btnZvukOff);
        BtnZvuk.setVisibility(View.GONE);
        BtnZvukOff.setVisibility(View.VISIBLE);
    }

    public void showButton(View view) {
        FloatingActionButton BtnZvuk = findViewById(R.id.btnZvuk);
        FloatingActionButton BtnZvukOff = findViewById(R.id.btnZvukOff);
        BtnZvukOff.setVisibility(View.GONE);
        BtnZvuk.setVisibility(View.VISIBLE);
    }

    private void callSos(View view) {
        String sosPhoneNumber = "tel:" + "+385112";
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse(sosPhoneNumber));
        startActivity(callIntent);
    }
}