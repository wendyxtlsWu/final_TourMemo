package edu.neu.madcourse.tourmemo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private EditText email;
    private EditText password;
    private Button button_login;
    private TextView registerUser;
    private ProgressBar progressBar;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        button_login = findViewById(R.id.login);
        registerUser = findViewById(R.id.register_user);
        progressBar = findViewById(R.id.progressBar);

        mAuth = FirebaseAuth.getInstance();

        progressBar.setVisibility(View.INVISIBLE);

        registerUser.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, RegisterActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));
        });

        button_login.setOnClickListener(v -> {
            progressBar.setVisibility(View.VISIBLE);

            String email_string = email.getText().toString();
            String password_string = password.getText().toString();

            if (!TextUtils.isEmpty(email_string) && !TextUtils.isEmpty(password_string)) {
                login(email_string, password_string);
            } else {
                Toast.makeText(LoginActivity.this, "Please input the correct information", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void login(String username, String password) {
        mAuth.signInWithEmailAndPassword(username, password).addOnCompleteListener((task -> {
           if (task.isSuccessful()) {
               Toast.makeText(LoginActivity.this, "Login success!", Toast.LENGTH_SHORT).show();
               Intent intent = new Intent(LoginActivity.this , MainActivity.class);
               intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
               startActivity(intent);
               finish();
           }
        })).addOnFailureListener(e -> {
            Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
        });
    }
}