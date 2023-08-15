package com.example.history4fun;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class Home extends AppCompatActivity {
    private Utente user;
    private String nickname;
    private static Client client;
    private Button jurassic_area_button   = null;
    private Button prehistory_area_button = null;
    private Button egypt_area_button      = null;
    private Button roman_area_button      = null;
    private Button greek_area_button      = null;
    private Button full_pack_area_button  = null;

    // ------------------------------------------------------------------------------ //

    private void manage_page() {
        full_pack_area_button.setOnClickListener(view -> {
            Thread t = new Thread(() -> {
                Intent intent = new Intent(Home.this, payment.class);
                intent.putExtra("user", user);
                intent.putExtra("museum_area", "full");
                startActivity(intent);
            });
            t.start();
        });

        jurassic_area_button.setOnClickListener(view -> {
            Thread t = new Thread(() -> {
                Intent intent = new Intent(Home.this, payment.class);
                intent.putExtra("user", user);
                intent.putExtra("museum_area", "jurassic");
                startActivity(intent);
            });
            t.start();
        });

        prehistory_area_button.setOnClickListener(view -> {
            Thread t = new Thread(() -> {
                Intent intent = new Intent(Home.this, payment.class);
                intent.putExtra("user", user);
                intent.putExtra("museum_area", "prehistory");
                startActivity(intent);
            });
            t.start();
        });

        egypt_area_button.setOnClickListener(view -> {
            Thread t = new Thread(() -> {
                Intent intent = new Intent(Home.this, payment.class);
                intent.putExtra("user", user);
                intent.putExtra("museum_area", "egypt");
                startActivity(intent);
            });
            t.start();
        });

        roman_area_button.setOnClickListener(view -> {
            Thread t = new Thread(() -> {
                Intent intent = new Intent(Home.this, payment.class);
                intent.putExtra("user", user);
                intent.putExtra("museum_area", "roman");
                startActivity(intent);
            });
            t.start();
        });

        greek_area_button.setOnClickListener(view -> {
            Thread t = new Thread(() -> {
                Intent intent = new Intent(Home.this, payment.class);
                intent.putExtra("user", user);
                intent.putExtra("museum_area", "greek");
                startActivity(intent);
            });
            t.start();
        });
    }

    // ------------------------------------------------------------------------------ //

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Intent intent = getIntent();
        user     = (Utente) intent.getSerializableExtra("user");
        nickname = (String) intent.getSerializableExtra("user_nickname");

        // TODO: COMPLETARE QUESTA ACTIVITY
        full_pack_area_button  = (Button) findViewById(R.id.FullPackButton);
        jurassic_area_button   = (Button) findViewById(R.id.GiurassicButton);
        prehistory_area_button = (Button) findViewById(R.id.PreistoricButton);
        egypt_area_button      = (Button) findViewById(R.id.EgyptButton);
        roman_area_button      = (Button) findViewById(R.id.RomanButton);
        greek_area_button      = (Button) findViewById(R.id.GreekButton);

        client = MainActivity.client;

        Thread t = new Thread(this::manage_page);
        t.start();
    }

    @Override
    protected void onStart() { super.onStart(); }

    @Override
    protected void onResume() { super.onResume(); }

    @Override
    protected void onPause() { super.onPause(); }

    @Override
    protected void onStop() { super.onStop(); }

    @Override
    protected void onRestart(){ super.onRestart(); }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Thread t = new Thread(() -> {
            client.send_json_close_connection("STOP_CONNECTION");
            client.close_connection();
        });
        t.start();
    }

    // ------------------------------------------------------------------------------ //
}