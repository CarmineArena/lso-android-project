package com.example.history4fun;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;

public class Home extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Intent intent = getIntent();
        Client client = (Client) intent.getSerializableExtra("client");
        Utente user   = (Utente) intent.getSerializableExtra("user");
    }
}