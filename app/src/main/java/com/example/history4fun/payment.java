package com.example.history4fun;

import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.SimpleAdapter;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// TODO: BISOGNA SISTEMARE L'INTERFACCIA

public class payment extends AppCompatActivity {
    private Client client;
    private Utente user;
    private Ticket ticket            = null;
    private String museum_area       = null;
    // private EditText card_number_text = null;

    private void manage_page() {
        // 1. button listener per la conferma di acquisto di un nuovo biglietto e all'interno mettere un thread
            // Creare quindi l'oggetto ticket (facendo tutti i controlli del caso sull'interfaccia) e mandarlo al server per la registrazione nel database.
                // Se si prenota un biglietto per il giorno stesso, al termine di tutte le operazioni del server, riportare alla pagina della visita guidata!

        // 2. button listener per la conferma di avere già un biglietto, creare poi un thread in cui far partire la visita guidata (dopo tutti i controlli del caso)
        /*
            Thread t = new Thread(() -> {
                // Do your staff...
            });
            t.start();
         */
    }

    // ------------------------------------------------------------------------------ //

    String[] type = {"Seleziona un tipo", "Giurassico", "Preistoria", "Egiziano", "Greco", "Romano", "Full"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        Intent intent = getIntent();
        user        = (Utente) intent.getSerializableExtra("user");
        museum_area = (String) intent.getSerializableExtra("museum_area");

        Log.i("USER_ID_REQUESTER: ",  user.getUser_id());
        Log.i("MUSEUM_AREA_CHOSEN: ", museum_area);

        client = MainActivity.client;


        // card_number_text = (EditText) findViewById(R.id.CardNumber);

        Thread t = new Thread(this::manage_page);
        t.start();

        Spinner spinnerType = findViewById(R.id.spinnerType);

        List<Map<String, String>> data = new ArrayList<>();
        for (String typeName : type) {
            Map<String, String> item = new HashMap<>();
            item.put("type", typeName);
            data.add(item);
        }

        SimpleAdapter adapter = new SimpleAdapter(this, data, android.R.layout.simple_spinner_item, new String[]{"type"}, new int[]{android.R.id.text1});
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerType.setAdapter(adapter);

        spinnerType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedType = type[position];
                if (!selectedType.equals("Seleziona un tipo")) {
                    // Fai qualcosa con il tipo selezionato
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Nessuna opzione selezionata
            }
        });
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