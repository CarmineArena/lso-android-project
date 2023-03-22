package com.example.history4fun;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

public class Forgotten_password extends AppCompatActivity {
    private static Client client = MainActivity.client;
    private EditText mail_text     = null;
    private EditText code_text     = null;
    private EditText new_pass_text = null;
    private Button mail_button     = null;
    private Button code_button     = null;
    private Button new_pass_button = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgotten_password);

       mail_text       = findViewById(R.id.MailField);
       code_text       = findViewById(R.id.CodeField);
       new_pass_text   = findViewById(R.id.PasswordField);
       mail_button     = findViewById(R.id.MailButton);
       code_button     = findViewById(R.id.CodeButton);
       new_pass_button = findViewById(R.id.PassowrdButton);
    }
}