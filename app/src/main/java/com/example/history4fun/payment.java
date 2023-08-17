package com.example.history4fun;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.os.Handler;
import android.util.Log;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class payment extends AppCompatActivity {
    private Client client;
    private Utente user;
    private Handler handler                  = null;
    private Ticket ticket                    = null;
    private String museum_area               = null;
    private String user_type_choice          = null;
    private EditText card_number_text        = null;
    private EditText card_owner_name_text    = null;
    private EditText card_owner_surname_text = null;
    private EditText month_expire_text       = null;
    private EditText year_expire_text        = null;
    private EditText cvc_card_number_text    = null;
    private EditText n_followers_text        = null;
    private Button get_ticket_button         = null;
    private Button login_visit_button        = null;
    private Spinner spinnerType              = null;
    String[] type = {"Seleziona un tipo", "Singolo", "Gruppo", "Famiglia", "Scuola", "Esperto"};

    private void setSelected_area(String selected_area) {
        this.museum_area = selected_area;
    }

    private String getSelected_area() { return this.museum_area; }

    private void setUser(Utente u) {
        this.user = u;
    }

    private Utente getUser() { return this.user; }

    private void setUserTypeChoice(String choice) {
        this.user_type_choice = choice;
    }

    private String getUserTypeChoice() { return this.user_type_choice; }

    private void manage_spinner() {
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
                    setUserTypeChoice(selectedType);
                    if (selectedType.equals("Singolo")) {
                        // TODO: INVALIDARE IL CAMPO PER LA SELEZIONE DEL NUMERO DI ACCOMPAGNATORI (nel database va inserito "0")
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                setUserTypeChoice(null);
            }
        });
    }

    private void showAlertDialog(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(payment.this);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setIcon(android.R.drawable.ic_dialog_alert);
        builder.setPositiveButton("OK", (dialog, id) -> dialog.dismiss());

        this.handler.post(() -> {
            AlertDialog dialog = builder.create();
            dialog.show();
        });
    }

    private boolean isNumericString(String string) {
        Pattern pattern = Pattern.compile("^[0-9]+$");
        Matcher matcher = pattern.matcher(string);
        return matcher.matches();
    }

    private void manage_get_ticket() {
        get_ticket_button.setOnClickListener(view -> {
            Thread t = new Thread(() -> {
                String card_number  = card_number_text.getText().toString();
                String prop_name    = card_owner_name_text.getText().toString();
                String prop_surname = card_owner_surname_text.getText().toString();
                String n_f          = n_followers_text.getText().toString();
                String cvc          = cvc_card_number_text.getText().toString();
                String month_exp_t  = month_expire_text.getText().toString();
                String year_exp_t   = year_expire_text.getText().toString();

                // TODO: ottenere data e fare i controlli, se si prenota un biglietto per il giorno stesso, al termine di tutte le operazioni del server, riportare alla pagina della visita guidata.

                if (card_number.length() != 10) {
                    showAlertDialog("CARD NUMBER ERROR", "Controllare la validità del numero della carta di credito dichiarata.");
                } else if (prop_name.isEmpty() || prop_surname.isEmpty() || isNumericString(prop_name) || isNumericString(prop_surname)) {
                    showAlertDialog("ERROR", "Controllare la validità del nome e cognome inseriti. Non sono ammessi numeri.");
                } else if (cvc.length() != 3) {
                    showAlertDialog("CARD SECURITY NUMBER ERROR", "Controllare la validità del codice di sicurezza inserito.");
                } else if (month_exp_t.isEmpty() || year_exp_t.isEmpty() || month_exp_t.length() != 2 || year_exp_t.length() != 4 || !isNumericString(month_exp_t) || !isNumericString(year_exp_t)) {
                    showAlertDialog("EXPIRATION DATE ERROR", "Controllare la validità del numero della carta di credito dichiarata.");
                } else {
                    // 1. Ottenere lo user_type_choice dallo spinenr e controllare che non sia null
                    // 2. convertire ad intero n_f e controllare >= 30 (se il campo non è stato invalidato)
                    // 4. creare oggetto Ticket e mandarlo al server (poi lavora alla comunicazione con il server e al codice del server)
                }
            });
            t.start();
        });
    }

    private void manage_login_visit() {
        // TODO: button listener per la conferma di avere già un biglietto, creare poi un thread in cui far partire la visita guidata (dopo tutti i controlli del caso)
    }

    private void manage_page() {
        manage_spinner();
        manage_get_ticket();
        manage_login_visit();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        this.handler = new Handler();

        Intent intent = getIntent();
        user        = (Utente) intent.getSerializableExtra("user");
        museum_area = (String) intent.getSerializableExtra("museum_area");
        setSelected_area(museum_area);
        setUser(user);

        Log.i("USER_ID_REQUESTER: ",  user.getUser_id());
        Log.i("MUSEUM_AREA_CHOSEN: ", museum_area);

        client = MainActivity.client;

        card_number_text        = (EditText) findViewById(R.id.CardNumber);
        card_owner_name_text    = (EditText) findViewById(R.id.CardOwner);
        card_owner_surname_text = (EditText) findViewById(R.id.editTextTextPersonName);
        month_expire_text       = (EditText) findViewById(R.id.editTextExpiryMonth);
        year_expire_text        = (EditText) findViewById(R.id.editTextExpiryYear);
        cvc_card_number_text    = (EditText) findViewById(R.id.editTextNumber2);
        n_followers_text        = (EditText) findViewById(R.id.editTextNumber);
        get_ticket_button       = (Button)   findViewById(R.id.buybotton);
        login_visit_button      = (Button)   findViewById(R.id.enterbutton);
        spinnerType = findViewById(R.id.spinnerType);

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
}