package com.example.instagram;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

public class MainActivity extends AppCompatActivity {

    private EditText usernameInput;
    private EditText passwordInput;
    private Button loginBtn;
    private Button signupBtn;
    private ParseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        currentUser = ParseUser.getCurrentUser();
        if (currentUser != null) {
            final Intent intent = new Intent(MainActivity.this, HomeActivity.class);
            startActivity(intent);
            finish();
        }

        usernameInput = findViewById(R.id.username_et);
        passwordInput = findViewById(R.id.password_et);
        loginBtn = findViewById(R.id.login_btn);
        signupBtn = findViewById(R.id.signup_btn);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String username = usernameInput.getText().toString();
                final String password = passwordInput.getText().toString();

                login(username, password);
            }
        });

        signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SignUpActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }

    private void login(String user, String pass) {
        ParseUser.logInInBackground(user, pass, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                //System.out.println(e);
                if (e == null) {
                    Log.d("LogInActivity", "Login was successful!");
                    currentUser = ParseUser.getCurrentUser();
                    final Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Log.e("LogInActivity", "Login failed!");
                    e.printStackTrace();
                }
            }
        });
    }
}
