package com.example.history4fun;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import androidx.appcompat.app.AppCompatActivity;

public class Home extends AppCompatActivity {
    private Utente user;
    private String nickname;
    private static Client client;
    private Button jurassic_area_button    = null;
    private Button prehistory_area_button  = null;
    private Button egypt_area_button       = null;
    private Button roman_area_button       = null;
    private Button greek_area_button       = null;
    private Button full_pack_area_button   = null;
    private boolean should_call_on_destroy = true;

    // ------------------------------------------------------------------------------ //

    private void manage_page() {
        full_pack_area_button.setOnClickListener(view -> {
            Thread t = new Thread(() -> {
                Intent intent = new Intent(Home.this, payment.class);
                intent.putExtra("user", user);
                intent.putExtra("user_nickname", nickname);
                intent.putExtra("museum_area", "full");
                intent.putExtra("isExpert", user.isExpert());
                startActivity(intent);
            });
            t.start();
        });

        jurassic_area_button.setOnClickListener(view -> {
            Thread t = new Thread(() -> {
                Intent intent = new Intent(Home.this, payment.class);
                intent.putExtra("user", user);
                intent.putExtra("user_nickname", nickname);
                intent.putExtra("museum_area", "jurassic");
                intent.putExtra("isExpert", user.isExpert());
                startActivity(intent);
            });
            t.start();
        });

        prehistory_area_button.setOnClickListener(view -> {
            Thread t = new Thread(() -> {
                Intent intent = new Intent(Home.this, payment.class);
                intent.putExtra("user", user);
                intent.putExtra("user_nickname", nickname);
                intent.putExtra("museum_area", "prehistory");
                intent.putExtra("isExpert", user.isExpert());
                startActivity(intent);
            });
            t.start();
        });

        egypt_area_button.setOnClickListener(view -> {
            Thread t = new Thread(() -> {
                Intent intent = new Intent(Home.this, payment.class);
                intent.putExtra("user", user);
                intent.putExtra("user_nickname", nickname);
                intent.putExtra("museum_area", "egypt");
                intent.putExtra("isExpert", user.isExpert());
                startActivity(intent);
            });
            t.start();
        });

        roman_area_button.setOnClickListener(view -> {
            Thread t = new Thread(() -> {
                Intent intent = new Intent(Home.this, payment.class);
                intent.putExtra("user", user);
                intent.putExtra("user_nickname", nickname);
                intent.putExtra("museum_area", "roman");
                intent.putExtra("isExpert", user.isExpert());
                startActivity(intent);
            });
            t.start();
        });

        greek_area_button.setOnClickListener(view -> {
            Thread t = new Thread(() -> {
                Intent intent = new Intent(Home.this, payment.class);
                intent.putExtra("user", user);
                intent.putExtra("user_nickname", nickname);
                intent.putExtra("museum_area", "greek");
                intent.putExtra("isExpert", user.isExpert());
                startActivity(intent);
            });
            t.start();
        });

        ImageButton backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> onBackPressed());
    }

    // ------------------------------------------------------------------------------ //

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Intent intent = getIntent();
        user     = (Utente) intent.getSerializableExtra("user");
        nickname = (String) intent.getSerializableExtra("user_nickname");

        full_pack_area_button  = findViewById(R.id.FullPackButton);
        jurassic_area_button   = findViewById(R.id.GiurassicButton);
        prehistory_area_button = findViewById(R.id.PreistoricButton);
        egypt_area_button      = findViewById(R.id.EgyptButton);
        roman_area_button      = findViewById(R.id.RomanButton);
        greek_area_button      = findViewById(R.id.GreekButton);

        client = MainActivity.client;

        ImageButton settingsButton = findViewById(R.id.settingsButton);
        settingsButton.setOnClickListener(v -> {
            PopupMenu popupMenu = new PopupMenu(this, v);
            MenuInflater inflater = popupMenu.getMenuInflater();
            inflater.inflate(R.menu.menu_popup, popupMenu.getMenu());
            Menu menu = popupMenu.getMenu();

            menu.clear();
            menu.add(Menu.NONE, 1, Menu.NONE, "Nome utente: " + user.getName());
            menu.add(Menu.NONE, 2, Menu.NONE, "Età: " + user.getAge());
            menu.add(Menu.NONE, 3, Menu.NONE, "Email: " + user.getEmail());
            menu.add(Menu.NONE, 4, Menu.NONE, (user.isExpert() == 0) ? "Esperto: No" : "Esperto: Si");
            popupMenu.show();
        });

        Thread t = new Thread(this::manage_page);
        t.start();
    }

    @Override
    protected void onStart() {
        super.onStart();
        should_call_on_destroy = true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        should_call_on_destroy = true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        should_call_on_destroy = false;
    }

    @Override
    protected void onStop() {
        super.onStop();
        should_call_on_destroy = false;
    }

    @Override
    protected void onRestart(){
        super.onRestart();
        should_call_on_destroy = true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (should_call_on_destroy) {
            Thread t = new Thread(() -> {
                client.send_json_close_connection("STOP_CONNECTION");
                client.close_connection();
            });
            t.start();
        }
    }
    // ------------------------------------------------------------------------------ //
}