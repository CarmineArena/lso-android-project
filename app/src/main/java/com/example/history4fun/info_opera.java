package com.example.history4fun;

import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class info_opera extends AppCompatActivity {
    private Client client;
    private String descrizione     = null;
    private TextView specific_text = null;
    private ImageView immagine     = null;
    private String chosen_area;
    private int art_id, isExpert;
    private boolean should_call_on_destroy = true;

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_opera);

        client = MainActivity.client;

        Intent intent = getIntent();
        descrizione = (String) intent.getSerializableExtra("descrizione");
        chosen_area = (String) intent.getSerializableExtra("area");
        isExpert    = (int)    intent.getSerializableExtra("isExpert");

        String id = (String) intent.getSerializableExtra("art_id");
        try {
            art_id = Integer.parseInt(id);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

        specific_text = findViewById(R.id.SpecificDescription);
        specific_text.setText(descrizione);

        // TODO: MOSTRARE NEL LINEAR LAYOUT I TOP 1 o 2 COMMENTI + POSSIBILITA' DI COMMENTARE SE SEI ESPERTO
/* IT WORKS: FOLLOW THIS CODE
        LinearLayout linearLayout = findViewById(R.id.imageContainer);
        TextView nuovoTextView1 = new TextView(this);
        nuovoTextView1.setText("Nuovo TextView 1");

        TextView nuovoTextView2 = new TextView(this);
        nuovoTextView2.setText("Nuovo TextView 2");

        // Aggiungi i nuovi TextView al LinearLayout
        linearLayout.addView(nuovoTextView1);
        linearLayout.addView(nuovoTextView2);
*/
        immagine = findViewById(R.id.opera);

        Drawable drawable = null;

        ImageButton backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> onBackPressed());

        switch (chosen_area) {
            case "full":
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
        }

        immagine.setImageDrawable(drawable);
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
}