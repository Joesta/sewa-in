package za.co.robusttech.sewain.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

import za.co.robusttech.sewain.R;

public class PayPalActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_pal);

        Toast.makeText(this, PayPalActivity.class.getName(), Toast.LENGTH_LONG).show();
    }
}