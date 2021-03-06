package edu.neu.madcourse.tourmemo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

public class OptionsActivity extends AppCompatActivity {
    TextView settings;
    Button logOut_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);

        settings = findViewById(R.id.settings);
        logOut_button = findViewById(R.id.logout);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Options");
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setTitle("Options");
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        logOut_button.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(OptionsActivity.this, StartActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });
    }
}