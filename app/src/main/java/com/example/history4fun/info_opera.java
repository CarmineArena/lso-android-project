package com.example.history4fun;

import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class info_opera extends AppCompatActivity {
    private final String jurassic1   = "00001";
    private final String jurassic2   = "00002";
    private final String jurassic3   = "00003";
    private final String prehistory1 = "00004";
    private final String prehistory2 = "00005";
    private final String prehistory3 = "00006";
    private final String egypt1      = "00007";
    private final String egypt2      = "00008";
    private final String egypt3      = "00009";
    private final String roman1      = "00010";
    private final String roman2      = "00011";
    private final String roman3      = "00012";
    private final String greek1      = "00013";
    private final String greek2      = "00014";
    private final String greek3      = "00015";

    private Client client;
    private String descrizione        = null;
    private TextView specific_text    = null;
    private ImageView immagine        = null;
    private EditText comment_area     = null;
    private LinearLayout linearLayout = null;
    private ImageButton sButton = null;
    private String chosen_area, userId;
    private int art_id, isExpert;
    private boolean should_call_on_destroy = true;

    private void show_experts_comments(String artId) {
        Thread t = new Thread(() -> {
            client.send_json_get_comment_by_id("GET_COMMENT", artId);
            try {
                JSONObject myjson = client.receive_json_multiple_records();
                String flag = myjson.getString("flag");
                Log.i("ARRAY: ", myjson.getJSONArray("retrieved_data").toString());

                switch (flag) {
                    case "SUCCESS":
                        JSONArray retrieved_data = myjson.getJSONArray("retrieved_data");
                        int len = retrieved_data.length();

                        // TODO: AUMENTARE LA DIMENSIONE DEI CARATTERI E CAMBIARE IL FONT
                        for(int i = 0; i < len; i++) {
                            JSONObject retrieved = retrieved_data.getJSONObject(i);
                            String name = retrieved.getString("name");
                            String surname = retrieved.getString("surname");
                            String comment = retrieved.getString("comment");

                            TextView newTextView = new TextView(this);
                            newTextView.setText(name + " " + surname + ": " + comment);
                            linearLayout.addView(newTextView);
                        }
                        break;
                    case "FAILURE":
                        TextView newTextView = new TextView(this);
                        newTextView.setText("Al momento non sono presenti commenti!");
                        linearLayout.addView(newTextView);
                        Log.i("NESSUN COMMENTO", " TROVATO");
                        break;
                }
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
        });
        t.start();
    }

    private void manage_user_comment(String artId) {
        sButton.setOnClickListener(view -> {
            Thread t = new Thread(() -> {
                String comment_text = String.valueOf(comment_area.getText()); // TODO: EFFETTUARE L'ESCAPE DI CARATTERI PROBLEMATICI PER MYSQL

                if (isExpert == 1) {
                    if (!comment_text.isEmpty()) {
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                        Date currentDate = new Date();
                        Calendar calendar = Calendar.getInstance();
                        calendar.setTime(currentDate);
                        calendar.add(Calendar.DATE, 0);
                        Date today = calendar.getTime();
                        String current_date = dateFormat.format(today);
                        client.send_json_add_comment_by_id("ADD_COMMENT", userId, artId, comment_text, current_date);

                        try {
                            JSONObject myjson = client.receive_json();
                            String flag = myjson.getString("flag");

                            switch (flag) {
                                case "SUCCESS":
                                    // TODO: IL COMMENTO VA INSERITO NEL LINEAR LAYOUT DEI COMMENTI (IN TOP POSSIBILMENTE)
                                    Log.i("INSERIMENTO RECORD: ", "RIUSCITO");
                                    break;
                                case "FAILURE":
                                    // TODO: MOSTRARE UN ALERT DIALOG
                                    break;
                            }
                        } catch (IOException | JSONException e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    // TODO: SAREBBE MEGLIO CHE L'UTENTE NON ESPERTO NON VISUALIZZO PROPRIO LA BARRA PER COMMENTARE
                    // TODO: MOSTRARE UN ALERT DIALOG PER DIRE CHE SE NON E' ESPERTO NON PUO' PUBBLICARE COMMENTI
                }
            });
            t.start();
        });
    }

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
        userId      = (String) intent.getSerializableExtra("user_id");

        String id = (String) intent.getSerializableExtra("art_id");
        try {
            art_id = Integer.parseInt(id);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

        sButton      = findViewById(R.id.sendButton);
        comment_area = findViewById(R.id.commentView);
        linearLayout = findViewById(R.id.imageContainer);
        immagine     = findViewById(R.id.opera);

        specific_text = findViewById(R.id.SpecificDescription);
        specific_text.setMovementMethod(new ScrollingMovementMethod());
        specific_text.setText(descrizione);

        Drawable drawable = null;

        ImageButton backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> onBackPressed());

        switch (chosen_area) {
            case "full":
            case "jurassic":
                if (art_id == 0) {
                    drawable = getResources().getDrawable(R.drawable.tyrannosaurus_rex);
                    show_experts_comments(jurassic1);
                    manage_user_comment(jurassic1);
                } else if (art_id == 1) {
                    drawable = getResources().getDrawable(R.drawable.hadrosauridae);
                    show_experts_comments(jurassic2);
                    manage_user_comment(jurassic2);
                } else {
                    drawable = getResources().getDrawable(R.drawable.sauropoda);
                    show_experts_comments(jurassic3);
                    manage_user_comment(jurassic3);
                }
                break;
            case "prehistory":
                if (art_id == 0) {
                    drawable = getResources().getDrawable(R.drawable.microlito);
                    show_experts_comments(prehistory1);
                    manage_user_comment(prehistory1);
                } else if (art_id == 1) {
                    drawable = getResources().getDrawable(R.drawable.stonehenge);
                    show_experts_comments(prehistory2);
                    manage_user_comment(prehistory2);
                } else {
                    drawable = getResources().getDrawable(R.drawable.homo_neanderthalensis);
                    show_experts_comments(prehistory3);
                    manage_user_comment(prehistory3);
                }
                break;
            case "egypt":
                if (art_id == 0) {
                    drawable = getResources().getDrawable(R.drawable.pyramidsofgiza_at_night);
                    show_experts_comments(egypt1);
                    manage_user_comment(egypt1);
                } else if (art_id == 1) {
                    drawable = getResources().getDrawable(R.drawable.sfinge);
                    show_experts_comments(egypt2);
                    manage_user_comment(egypt2);
                } else {
                    drawable = getResources().getDrawable(R.drawable.piramidi);
                    show_experts_comments(egypt3);
                    manage_user_comment(egypt3);
                }
                break;
            case "roman":
                if (art_id == 0) {
                    drawable = getResources().getDrawable(R.drawable.gaio_mario);
                    show_experts_comments(roman1);
                    manage_user_comment(roman1);
                } else if (art_id == 1) {
                    drawable = getResources().getDrawable(R.drawable.romolo_e_remo);
                    show_experts_comments(roman2);
                    manage_user_comment(roman2);
                } else {
                    drawable = getResources().getDrawable(R.drawable.augusto);
                    show_experts_comments(roman3);
                    manage_user_comment(roman3);
                }
                break;
            case "greek":
                if (art_id == 0) {
                    drawable = getResources().getDrawable(R.drawable.partenone);
                    show_experts_comments(greek1);
                    manage_user_comment(greek1);
                } else if (art_id == 1) {
                    drawable = getResources().getDrawable(R.drawable.hermes_con_dioniso);
                    show_experts_comments(greek2);
                    manage_user_comment(greek2);
                } else {
                    drawable = getResources().getDrawable(R.drawable.cratere);
                    show_experts_comments(greek3);
                    manage_user_comment(greek3);
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