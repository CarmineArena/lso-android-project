package com.example.history4fun;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

public class info_opera extends AppCompatActivity {
    private Client client;
    private String descrizione     = null;
    private TextView specific_text = null;
    private ImageView immagine     = null;
    private String chosen_area;
    private int art_id;

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_opera);

        client = MainActivity.client;

        Intent intent = getIntent();
        descrizione = (String) intent.getSerializableExtra("descrizione");
        chosen_area = (String) intent.getSerializableExtra("area");

        String id = (String) intent.getSerializableExtra("art_id");

        try {
            art_id = Integer.parseInt(id);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

        specific_text = findViewById(R.id.SpecificDescription);
        specific_text.setText(descrizione);

        immagine = findViewById(R.id.opera);

        Drawable drawable = null;

        switch (chosen_area) {
            case "jurassic":
                if (art_id == 0) {
                    drawable = getResources().getDrawable(R.drawable.tyrannosaurus_rex);
                } else if (art_id == 1) {
                    drawable = getResources().getDrawable(R.drawable.hadrosauridae);
                } else {
                    drawable = getResources().getDrawable(R.drawable.sauropoda);
                }
                break;
            case "prehistory":
                if (art_id == 0) {
                    drawable = getResources().getDrawable(R.drawable.microlito);
                } else if (art_id == 1) {
                    drawable = getResources().getDrawable(R.drawable.stonehenge);
                } else {
                    drawable = getResources().getDrawable(R.drawable.homo_neanderthalensis);
                }
                break;
            case "egypt":
                if (art_id == 0) {
                    drawable = getResources().getDrawable(R.drawable.pyramidsofgiza_at_night);
                } else if (art_id == 1) {
                    drawable = getResources().getDrawable(R.drawable.sfinge);
                } else {
                    drawable = getResources().getDrawable(R.drawable.piramidi);
                }
                break;
            case "roman":
                if (art_id == 0) {
                    drawable = getResources().getDrawable(R.drawable.gaio_mario);
                } else if (art_id == 1) {
                    drawable = getResources().getDrawable(R.drawable.romolo_e_remo);
                } else {
                    drawable = getResources().getDrawable(R.drawable.augusto);
                }
                break;
            case "greek":
                if (art_id == 0) {
                    drawable = getResources().getDrawable(R.drawable.partenone);
                } else if (art_id == 1) {
                    drawable = getResources().getDrawable(R.drawable.hermes_con_dioniso);
                } else {
                    drawable = getResources().getDrawable(R.drawable.cratere);
                }
                break;
            case "full":
                // TODO: DA IMPLEMENTARE IL FULLPACK
                Log.i("FULL OPERAS IMAGES: ", "TO BE IMPLEMENTED.");
                break;
        }

        immagine.setImageDrawable(drawable);
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