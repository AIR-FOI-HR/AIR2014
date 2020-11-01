package foi.hr.parksmart;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);
    }


    public void hideButton(View view) {
        FloatingActionButton BtnZvuk = (FloatingActionButton) findViewById(R.id.btnZvuk);
        FloatingActionButton BtnZvukOff = (FloatingActionButton) findViewById(R.id.btnZvukOff);
        BtnZvuk.setVisibility(View.GONE);
        BtnZvukOff.setVisibility(View.VISIBLE);
    }

    public void showButton(View view) {
        FloatingActionButton BtnZvuk = (FloatingActionButton) findViewById(R.id.btnZvuk);
        FloatingActionButton BtnZvukOff = (FloatingActionButton) findViewById(R.id.btnZvukOff);
        BtnZvukOff.setVisibility(View.GONE);
        BtnZvuk.setVisibility(View.VISIBLE);
    }
}