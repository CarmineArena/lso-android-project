package com.example.history4fun;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;

public class Home extends AppCompatActivity {
    private static Client client = MainActivity.client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Intent intent = getIntent();
        Utente user   = (Utente) intent.getSerializableExtra("user");

        // TODO: Abbiamo risolto il come passare gli oggetti alle nuove Activity. Completare questa activity.
    }
}