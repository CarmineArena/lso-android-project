package com.example.history4fun;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;

public class lista extends AppCompatActivity {
    private Client client;
    private Utente user;
    private String[] opera_descriptions; // LE DESCRIZIONI SONO IN ORDINE DI IDENFITICATIVO
    private String nickname         = null;
    private String chosen_area      = null;
    private String user_ticket_type = null;
    private ImageView first_img     = null;
    private ImageView second_img    = null;
    private ImageView third_img     = null;
    private Button first_opera_button  = null;
    private Button second_opera_button = null;
    private Button third_opera_button  = null;

    private void manage_full_page() {

    }

    private void manage_page_single_area() {
        first_opera_button.setOnClickListener(view -> {
            Thread t = new Thread(() -> {
                Intent intent = new Intent(lista.this, info_opera.class);
                intent.putExtra("descrizione", opera_descriptions[0]);
                intent.putExtra("area", chosen_area);
                intent.putExtra("art_id", "0");
                startActivity(intent);
            });
            t.start();
        });

        second_opera_button.setOnClickListener(view -> {
            Thread t = new Thread(() -> {
                Intent intent = new Intent(lista.this, info_opera.class);
                intent.putExtra("descrizione", opera_descriptions[1]);
                intent.putExtra("area", chosen_area);
                intent.putExtra("art_id", "1");
                startActivity(intent);
            });
            t.start();
        });

        third_opera_button.setOnClickListener(view -> {
            Thread t = new Thread(() -> {
                Intent intent = new Intent(lista.this, info_opera.class);
                intent.putExtra("descrizione", opera_descriptions[2]);
                intent.putExtra("area", chosen_area);
                intent.putExtra("art_id", "2");
                startActivity(intent);
            });
            t.start();
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista);

        Intent intent = getIntent();
        user               = (Utente) intent.getSerializableExtra("user");
        nickname           = (String) intent.getSerializableExtra("user_nickname");
        chosen_area        = (String) intent.getSerializableExtra("area");
        user_ticket_type   = (String) intent.getSerializableExtra("ticket_type");
        opera_descriptions = (String[]) intent.getSerializableExtra("opera_descriptions");

        client = MainActivity.client;

        first_img  = findViewById(R.id.firstopera);
        second_img = findViewById(R.id.secondopera);
        third_img  = findViewById(R.id.thirdopera);

        Drawable drawable1 = null;
        Drawable drawable2 = null;
        Drawable drawable3 = null;

        switch (chosen_area) {
            case "jurassic":
                drawable1 = getResources().getDrawable(R.drawable.tyrannosaurus_rex);
                drawable2 = getResources().getDrawable(R.drawable.hadrosauridae);
                drawable3 = getResources().getDrawable(R.drawable.sauropoda);
                break;
            case "prehistory":
                drawable1 = getResources().getDrawable(R.drawable.microlito);
                drawable2 = getResources().getDrawable(R.drawable.stonehenge);
                drawable3 = getResources().getDrawable(R.drawable.homo_neanderthalensis);
                break;
            case "egypt":
                drawable1 = getResources().getDrawable(R.drawable.pyramidsofgiza_at_night);
                drawable2 = getResources().getDrawable(R.drawable.sfinge);
                drawable3 = getResources().getDrawable(R.drawable.piramidi);
                break;
            case "roman":
                drawable1 = getResources().getDrawable(R.drawable.gaio_mario);
                drawable2 = getResources().getDrawable(R.drawable.romolo_e_remo);
                drawable3 = getResources().getDrawable(R.drawable.augusto);
                break;
            case "greek":
                drawable1 = getResources().getDrawable(R.drawable.partenone);
                drawable2 = getResources().getDrawable(R.drawable.hermes_con_dioniso);
                drawable3 = getResources().getDrawable(R.drawable.cratere);
                break;
            case "full":
                // TODO: DA FARE IL FULLPACK
                Log.i("FULL OPERAS IMAGES: ", "TO BE IMPLEMENTED.");
                break;
        }

        first_img.setImageDrawable(drawable1);
        second_img.setImageDrawable(drawable2);
        third_img.setImageDrawable(drawable3);

        first_opera_button  = findViewById(R.id.firstOperaView);
        second_opera_button = findViewById(R.id.secondOperaView);
        third_opera_button  = findViewById(R.id.thirdOperaView);

        Thread t = new Thread(this::manage_page_single_area);
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
}