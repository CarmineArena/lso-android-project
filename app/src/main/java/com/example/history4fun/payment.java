package com.example.history4fun;

import androidx.appcompat.app.AppCompatActivity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.os.Handler;
import android.util.Log;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.SimpleAdapter;
import android.widget.Spinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

// TODO: (DATA DI SCADENZA CARTA DI CREDITO) BOUND DAY FROM 1 TO 31 AND YEAR >= CURRENT_YEAR - VA DATTO DAVVERO?
// TODO: AGGIUNGIAMO UN LABEL PER DIRE I PREZZI DEI BIGLIETTI? (lo si può fare anche nella schermata precedente)

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
    private Button data                      = null;
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
                        invalidato = true;
                        n_followers_text.setText("0");
                    } else {
                        n_followers_text.setEnabled(true);
                        invalidato = false;
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

    private void showInfoDialog(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(payment.this);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setIcon(android.R.drawable.ic_dialog_info);
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
        data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR);
                int mMonth = c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(payment.this,
                        (view, year, monthOfYear, dayOfMonth) -> {
                            String selectedDate = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                            data.setText(selectedDate);
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });

        get_ticket_button.setOnClickListener(view -> {
            Thread t = new Thread(() -> {
                String card_number  = String.valueOf(card_number_text.getText());
                String prop_name    = String.valueOf(card_owner_name_text.getText());
                String prop_surname = String.valueOf(card_owner_surname_text.getText());
                String n_f          = String.valueOf(n_followers_text.getText());
                String cvc          = String.valueOf(cvc_card_number_text.getText());
                String month_exp_t  = String.valueOf(month_expire_text.getText());
                String year_exp_t   = String.valueOf(year_expire_text.getText());
                String ticket_date  = String.valueOf(data.getText()); // MySQL data format: YYYY-MM-DD

                if (card_number.length() != 10) {
                    showAlertDialog("CARD NUMBER ERROR", "Controllare la validità del numero della carta di credito dichiarata.");
                } else if (prop_name.isEmpty() || prop_surname.isEmpty() || isNumericString(prop_name) || isNumericString(prop_surname)) {
                    showAlertDialog("ERROR", "Controllare la validità del nome e cognome inseriti. Non sono ammessi numeri.");
                } else if (cvc.length() != 3) {
                    showAlertDialog("CARD SECURITY NUMBER ERROR", "Controllare la validità del codice di sicurezza inserito.");
                } else if (month_exp_t.isEmpty() || year_exp_t.isEmpty() || month_exp_t.length() != 2 || year_exp_t.length() != 4 || !isNumericString(month_exp_t) || !isNumericString(year_exp_t)) {
                    showAlertDialog("EXPIRATION DATE ERROR", "Controllare la validità della data di scadenza della carta di credito. Formato: MM-AAAA");
                } else if (ticket_date.isEmpty()) {
                    showAlertDialog("BOOKING DATE ERROR", "Controllare la validità della data di prenotazione.");
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
                            float cost = 0.0f;

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

                            if (getSelected_area().equals("full")) {
                                cost = 50.0f;
                                for (int i = 0; i < ticket.getFollowers(); i++) {
                                    cost += 50.0f;
                                }
                            } else {
                                for (int i = 0; i < ticket.getFollowers(); i++) {
                                    cost += 10.0f;
                                }
                            }
                            ticket.setCost(cost);

                            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                            Date currentDate = new Date();
                            String curr_date = dateFormat.format(currentDate);

                            Calendar calendar = Calendar.getInstance();
                            calendar.setTime(currentDate);
                            calendar.add(Calendar.DATE, -1);
                            Date current_Date = calendar.getTime();

                            try {
                                Date data_prenotazione_biglietto = dateFormat.parse(ticket_date);

                                /* NON PUOI PRENOTARE PER UN GIORNO PASSATO */
                                if (Objects.requireNonNull(data_prenotazione_biglietto).after(current_Date)) {
                                    ticket.setDate(ticket_date);
                                    client.send_json_get_ticket_msg("GET_TICKET", ticket);

                                    try {
                                        JSONObject myjson = client.receive_json();
                                        String flag = myjson.getString("flag");

                                        clearAllText();
                                        if (flag.equals("SUCCESS")) {
                                            JSONArray retrieved_data = myjson.getJSONArray("retrieved_data");
                                            JSONObject retrieved = retrieved_data.getJSONObject(0);

                                            String user_ticket_id  = retrieved.getString("ticket_id");
                                            float user_ticket_cost = (float) retrieved.getDouble("cost");

                                            // TODO: NON CAPISCO PERCHE' MA VIENE MOSTRATO ANCHE L'HTML
                                            String info_message = "<html><body><p>La prenotazione è andata a buon fine. Grazie!</p><p>Hai pagato: " + user_ticket_cost
                                                    + " euro.</p><p>Ticket ID per partecipare alla mostra: " + user_ticket_id + "</p></body></html>";
                                            showInfoDialog("PRENOTAZIONE AVVENUTA CON SUCCESSO", info_message);

                                            // TODO: AL TERMINE DELLE OPERAZIONI, IN CHE SCHERMATA DEVO RITORNARE?
                                            // TODO: se si prenota un biglietto per il giorno corrente, riportare alla pagina della visita guidata oppure alla home del museo?
                                        } else if (flag.equals("FAILURE")) {
                                            showAlertDialog("FAILURE", "Qualcosa è andato storto nel processo di registrazione della prenotazione.");
                                        }
                                    } catch (IOException | JSONException e) {
                                        e.printStackTrace();
                                    }
                                } else {
                                    showAlertDialog("ERROR", "Inserire una data di prenotazione valida.");
                                }
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            });
            t.start();
        });
    }

    private void manage_login_visit() {
        // TODO: button listener per la conferma di avere già un biglietto, creare poi un thread. Se si possiede un biglietto per il giorno corrente, far partire la visita guidata (dopo tutti i controlli del caso).
    }

    private void manage_page() {
        manage_spinner();
        manage_get_ticket();
        manage_login_visit();
    }

    private void clearAllText() {
        card_number_text.setText("");
        card_owner_name_text.setText("");
        card_owner_surname_text.setText("");
        month_expire_text.setText("");
        year_expire_text.setText("");
        cvc_card_number_text.setText("");
        n_followers_text.setText("");
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
        data                    = (Button) findViewById(R.id.DataVisita);
        spinnerType             = findViewById(R.id.spinnerType);

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