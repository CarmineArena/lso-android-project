package com.example.history4fun;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.PopupMenu;

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
    private boolean should_call_on_destroy = true;

    // ------------------------------------------------------------------------------ //

    private void manage_page() {
        full_pack_area_button.setOnClickListener(view -> {
            Thread t = new Thread(() -> {
                Intent intent = new Intent(Home.this, payment.class);
                intent.putExtra("user", user);
                intent.putExtra("user_nickname", nickname);
                intent.putExtra("museum_area", "full");
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

        full_pack_area_button  = (Button) findViewById(R.id.FullPackButton);
        jurassic_area_button   = (Button) findViewById(R.id.GiurassicButton);
        prehistory_area_button = (Button) findViewById(R.id.PreistoricButton);
        egypt_area_button      = (Button) findViewById(R.id.EgyptButton);
        roman_area_button      = (Button) findViewById(R.id.RomanButton);
        greek_area_button      = (Button) findViewById(R.id.GreekButton);

        client = MainActivity.client;

        Thread t = new Thread(this::manage_page);

        ImageButton imageButton = findViewById(R.id.settingsButton);
        imageButton.setOnClickListener(v -> {
            PopupMenu popupMenu = new PopupMenu(this, v);
            MenuInflater inflater = popupMenu.getMenuInflater();
            inflater.inflate(R.menu.menu_popup, popupMenu.getMenu());

            popupMenu.setOnMenuItemClickListener(item -> {
                switch (item.getItemId()) {
                    case R.id.menu_item_1:
                        // Azione per l'elemento 1
                        return true;
                    case R.id.menu_item_2:
                        // Azione per l'elemento 2
                        return true;
                    case R.id.menu_item_3:
                        // Azione per l'elemento 3
                        return true;
                    default:
                        return false;
                }
            });

            popupMenu.show();
        });

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