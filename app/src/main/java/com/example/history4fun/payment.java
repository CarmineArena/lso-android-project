package com.example.history4fun;

import androidx.appcompat.app.AppCompatActivity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.os.Handler;
import android.util.Log;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class payment extends AppCompatActivity {
    private Client client;
    private Utente user;
    private boolean invalidato               = false;
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

    private void setSelected_area(String selected_area) { this.museum_area = selected_area; }

    private String getSelected_area() { return this.museum_area; }

    private void setUser(Utente u) { this.user = u; }

    private Utente getUser() { return this.user; }

    private void setUserTypeChoice(String choice) { this.user_type_choice = choice; }

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
                        n_followers_text.setEnabled(false);
                        invalidato = !invalidato;
                        n_followers_text.setHint("0");
                    } else {
                        n_followers_text.setEnabled(true);
                        invalidato = !invalidato;
                        n_followers_text.setHint("N°");
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

                if (card_number.length() != 10) {
                    showAlertDialog("CARD NUMBER ERROR", "Controllare la validità del numero della carta di credito dichiarata.");
                } else if (prop_name.isEmpty() || prop_surname.isEmpty() || isNumericString(prop_name) || isNumericString(prop_surname)) {
                    showAlertDialog("ERROR", "Controllare la validità del nome e cognome inseriti. Non sono ammessi numeri.");
                } else if (cvc.length() != 3) {
                    showAlertDialog("CARD SECURITY NUMBER ERROR", "Controllare la validità del codice di sicurezza inserito.");
                } else if (month_exp_t.isEmpty() || year_exp_t.isEmpty() || month_exp_t.length() != 2 || year_exp_t.length() != 4 || !isNumericString(month_exp_t) || !isNumericString(year_exp_t)) {
                    showAlertDialog("EXPIRATION DATE ERROR", "Controllare la validità del numero della carta di credito dichiarata.");
                } else {
                    String user_type = getUserTypeChoice().toString();
                    if (user_type.equals(null) || user_type.equals("Seleziona un tipo")) {
                        showAlertDialog("ERROR", "Selezionare il tipo di biglietto da acquistare.");
                    } else {
                        int n_followers = 0;
                        try {
                            n_followers = Integer.parseInt(n_f);
                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                        }

                        if (n_followers < 0 || n_followers > 30) {
                            showAlertDialog("ERROR", "Il numero di accompagnatori deve essere un numero positivo ed al massimo 30.");
                        } else {
                            // TODO: Settare la data
                            // TODO: settare costo totale del biglietto (10 euro singolo: sommare 10 per ogni avventore. Full pack 50)
                            ticket = new Ticket();
                            ticket.setTicket_id(new CharsetGenerator(5).get_generated_random_string());
                            ticket.setUser(getUser());

                            TicketType enum_value = null;
                            if (getUserTypeChoice().equals("Singolo")) {
                                enum_value = TicketType.guest;
                            } else if (getUserTypeChoice().equals("Gruppo")) {
                                enum_value = TicketType.group;
                            } else if (getUserTypeChoice().equals("Famiglia")) {
                                enum_value = TicketType.family;
                            } else if (getUserTypeChoice().equals("Scuola")) {
                                enum_value = TicketType.school;
                            } else if (getUserTypeChoice().equals("Esperto")) {
                                enum_value = TicketType.expert;
                            }
                            ticket.setType(enum_value);

                            if (invalidato) {
                                ticket.setFollowers(0);
                            } else {
                                ticket.setFollowers(n_followers);
                            }

                            MuseumArea enum_val = null;
                            if (getSelected_area().equals("full")) {
                                enum_val = MuseumArea.full;
                            } else if (getSelected_area().equals("jurassic")) {
                                enum_val = MuseumArea.jurassic;
                            } else if (getSelected_area().equals("prehistory")) {
                                enum_val = MuseumArea.prehistory;
                            } else if (getSelected_area().equals("egypt")) {
                                enum_val = MuseumArea.egypt;
                            } else if (getSelected_area().equals("roman")) {
                                enum_val = MuseumArea.roman;
                            } else if (getSelected_area().equals("greek")) {
                                enum_val = MuseumArea.greek;
                            }
                            ticket.setArea(enum_val);

                            // TODO: Mandare al server la richiesta di acquisto (json). Salvare (il server) nel database l'acquisto e mostrare un alertDialog
                                // se si prenota un biglietto per il giorno stesso, al termine di tutte le operazioni del server, riportare alla pagina della visita guidata.
                        }
                    }
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

        final Button Data = (Button) findViewById(R.id.DataVisita);
        Data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR);
                int mMonth = c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(payment.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                String selectedDate = dayOfMonth + "-" + (monthOfYear + 1) + "-" + year;
                                Data.setText(selectedDate);
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
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
}