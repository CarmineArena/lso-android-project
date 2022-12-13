package com.example.history4fun;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import java.lang.Thread;

public class MainActivity extends AppCompatActivity {
    private Client client = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Thread client_thread = new Thread(() -> {
            client = new Client();
            client.connect();
            client.handle_connection();
        });
        client_thread.start();
    }
}