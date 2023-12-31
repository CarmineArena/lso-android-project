package com.example.history4fun;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

public class lista extends AppCompatActivity {
    private Client client;
    private Utente user;
    private String[] opera_descriptions; // LE DESCRIZIONI SONO IN ORDINE DI IDENFITICATIVO
    private String area_clicked_gui    = null;
    private Button first_opera_button  = null;
    private Button second_opera_button = null;
    private Button third_opera_button  = null;
    private boolean should_call_on_destroy = true;
    private int isExpert;
    private String opera_name;

    private String getOpera_name() {
        return this.opera_name;
    }

    private void setOperaName(String area, int index) {
        switch (area_clicked_gui) {
            case "jurassic":
                if (index == 1) {
                    opera_name = "Tyrannosaurus Rex";
                } else if (index == 2) {
                    opera_name = "Hadrosauridae";
                } else {
                    opera_name = "Sauropoda";
                }
                break;
            case "prehistory":
                if (index == 1) {
                    opera_name = "Microlito";
                } else if (index == 2) {
                    opera_name = "Stonehenge";
                } else {
                    opera_name = "Uomo di Neandertal";
                }
                break;
            case "egypt":
                if (index == 1) {
                    opera_name = "Necropoli di Giza";
                } else if (index == 2) {
                    opera_name = "Grande Sfinge di Giza";
                } else {
                    opera_name = "Piramidi";
                }
                break;
            case "roman":
                if (index == 1) {
                    opera_name = "Gaio Mario";
                } else if (index == 2) {
                    opera_name = "Romolo e Remo";
                } else {
                    opera_name = "Augusto";
                }
                break;
            case "greek":
                if (index == 1) {
                    opera_name = "Partenone";
                } else if (index == 2) {
                    opera_name = "Hermes con Dionisio";
                } else {
                    opera_name = "Cratere";
                }
                break;
        }
    }


    private void manage_page_single_area() {
        first_opera_button.setOnClickListener(view -> {
            Thread t = new Thread(() -> {
                Intent intent = new Intent(lista.this, info_opera.class);
                intent.putExtra("descrizione", opera_descriptions[0]);
                intent.putExtra("area", area_clicked_gui);
                intent.putExtra("isExpert", isExpert);
                intent.putExtra("art_id", "0");
                intent.putExtra("user_id", user.getUser_id());
                intent.putExtra("user_name", user.getName());
                intent.putExtra("user_surname", user.getSurname());

                setOperaName(area_clicked_gui, 1);
                intent.putExtra("opera_name", getOpera_name());
                startActivity(intent);
            });
            t.start();
        });

        second_opera_button.setOnClickListener(view -> {
            Thread t = new Thread(() -> {
                Intent intent = new Intent(lista.this, info_opera.class);
                intent.putExtra("descrizione", opera_descriptions[1]);
                intent.putExtra("area", area_clicked_gui);
                intent.putExtra("isExpert", isExpert);
                intent.putExtra("art_id", "1");
                intent.putExtra("user_id", user.getUser_id());
                intent.putExtra("user_name", user.getName());
                intent.putExtra("user_surname", user.getSurname());

                setOperaName(area_clicked_gui, 2);
                intent.putExtra("opera_name", getOpera_name());
                startActivity(intent);
            });
            t.start();
        });

        third_opera_button.setOnClickListener(view -> {
            Thread t = new Thread(() -> {
                Intent intent = new Intent(lista.this, info_opera.class);
                intent.putExtra("descrizione", opera_descriptions[2]);
                intent.putExtra("area", area_clicked_gui);
                intent.putExtra("isExpert", isExpert);
                intent.putExtra("art_id", "2");
                intent.putExtra("user_id", user.getUser_id());
                intent.putExtra("user_name", user.getName());
                intent.putExtra("user_surname", user.getSurname());

                setOperaName(area_clicked_gui, 3);
                intent.putExtra("opera_name", getOpera_name());
                startActivity(intent);
            });
            t.start();
        });
    }

    @SuppressLint({"UseCompatLoadingForDrawables", "SetTextI18n"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista);

        Intent intent = getIntent();
        user                      = (Utente)   intent.getSerializableExtra("user");
        String nickname           = (String)   intent.getSerializableExtra("user_nickname");
        String user_selected_area = (String)   intent.getSerializableExtra("area_chosen_ticket");
        area_clicked_gui          = (String)   intent.getSerializableExtra("area_clicked_on_gui");
        String user_ticket_type   = (String)   intent.getSerializableExtra("ticket_type");
        opera_descriptions        = (String[]) intent.getSerializableExtra("opera_descriptions");
        isExpert                  = (int)      intent.getSerializableExtra("isExpert");

        client = MainActivity.client;

        Button first_opera_btn  = findViewById(R.id.firstOperaView);
        Button second_opera_btn = findViewById(R.id.secondOperaView);
        Button third_opera_btn  = findViewById(R.id.thirdOperaView);

        ImageView first_img = findViewById(R.id.firstopera);
        ImageView second_img = findViewById(R.id.secondopera);
        ImageView third_img = findViewById(R.id.thirdopera);

        Drawable drawable1 = null;
        Drawable drawable2 = null;
        Drawable drawable3 = null;

        // ImageButton backButton = findViewById(R.id.backButton);
        // backButton.setOnClickListener(v -> onBackPressed());
        ImageButton backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> {
            Intent intent2 = new Intent(this, Home.class);
            intent2.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent2);
        });

        switch (area_clicked_gui) {
            case "jurassic":
                drawable1 = getResources().getDrawable(R.drawable.tyrannosaurus_rex);
                drawable2 = getResources().getDrawable(R.drawable.hadrosauridae);
                drawable3 = getResources().getDrawable(R.drawable.sauropoda);
                first_opera_btn.setText("Tyrannosaurus Rex");
                second_opera_btn.setText("Hadrosauridae");
                third_opera_btn.setText("Sauropoda");
                break;
            case "prehistory":
                drawable1 = getResources().getDrawable(R.drawable.microlito);
                drawable2 = getResources().getDrawable(R.drawable.stonehenge);
                drawable3 = getResources().getDrawable(R.drawable.homo_neanderthalensis);
                first_opera_btn.setText("Microlito");
                second_opera_btn.setText("Stonehenge");
                third_opera_btn.setText("Uomo di Neandertal");
                break;
            case "egypt":
                drawable1 = getResources().getDrawable(R.drawable.pyramidsofgiza_at_night);
                drawable2 = getResources().getDrawable(R.drawable.sfinge);
                drawable3 = getResources().getDrawable(R.drawable.piramidi);
                first_opera_btn.setText("Necropoli di Giza");
                second_opera_btn.setText("Grande Sfinge di Giza");
                third_opera_btn.setText("Piramidi");
                break;
            case "roman":
                drawable1 = getResources().getDrawable(R.drawable.gaio_mario);
                drawable2 = getResources().getDrawable(R.drawable.romolo_e_remo);
                drawable3 = getResources().getDrawable(R.drawable.augusto);
                first_opera_btn.setText("Gaio Mario");
                second_opera_btn.setText("Romolo e Remo");
                third_opera_btn.setText("Augusto");
                break;
            case "greek":
                drawable1 = getResources().getDrawable(R.drawable.partenone);
                drawable2 = getResources().getDrawable(R.drawable.hermes_con_dioniso);
                drawable3 = getResources().getDrawable(R.drawable.cratere);
                first_opera_btn.setText("Partenone");
                second_opera_btn.setText("Hermes con Dionisio");
                third_opera_btn.setText("Cratere");
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
    protected void onRestart() {
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