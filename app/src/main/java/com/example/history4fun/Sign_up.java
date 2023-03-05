package com.example.history4fun;

import androidx.appcompat.app.AppCompatActivity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

public class Sign_up extends AppCompatActivity {
    private static Client client      = MainActivity.client;
    private Handler handler           = null;
    private EditText name_text        = null;
    private EditText surname_text     = null;
    private EditText nickname_text    = null;
    private EditText phone_text       = null;
    private EditText mail_text        = null;
    private EditText pass_text        = null;
    private EditText conf_pass_text   = null;
    private Button register_button    = null;
    private Button data_button        = null;

    private void showAlertDialog(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(Sign_up.this);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setIcon(android.R.drawable.ic_dialog_alert);
        builder.setPositiveButton("OK", (dialog, id) -> dialog.dismiss());

        // Signals main thread requesting to show Dialog
        this.handler.post(() -> {
            AlertDialog dialog = builder.create();
            dialog.show();
        });
    }

    private void manage_register() {
        register_button.setOnClickListener(v -> {
            Thread t = new Thread(() -> {
                String name          = String.valueOf(name_text.getText());
                String surname       = String.valueOf(surname_text.getText());
                String email         = String.valueOf(mail_text.getText());
                String password      = String.valueOf(pass_text.getText());
                String conf_password = String.valueOf(conf_pass_text.getText());
                String phone         = String.valueOf(phone_text.getText());
                String nickname      = String.valueOf(nickname_text.getText());

                // MySQL data format: YYYY-MM-DD
                String birth_date    = String.valueOf(data_button.getText());

                // TODO: SE IL NICKNAME NON E' STATO SCELTO SCRIVERE NOME E COGNOME NELLA HOME E CAMBIARE "SECONDO NOME" in "NICKNAME"
                if ((name.isEmpty()) || (surname.isEmpty()) || (email.isEmpty()) || (password.isEmpty())
                        || (conf_password.isEmpty()) || (phone.isEmpty())) {
                    showAlertDialog("ERRORE", "Attenzione, almeno un campo obbligatorio è vuoto!");
                } else if (!conf_password.equals(password)) {
                    showAlertDialog("ERRORE", "Attenzione, la password e la password di conferma devono essere uguali!");
                    pass_text.setText("");
                    conf_pass_text.setText("");
                } else {
                    Log.i("ALERT", "TUTTO OK");

                    /* DATABASE CONTROLS */
                    if (name.length() > 30) {
                        showAlertDialog("ATTENZIONE", "Il nome può avere massimo 30 caratteri.");
                    } else if (surname.length() > 30) {
                        showAlertDialog("ATTENZIONE", "Il cognome può avere massimo 30 caratteri.");
                    } else if (email.length() > 50) {
                        showAlertDialog("ATTENZIONE", "L'email può avere massimo 50 caratteri.");
                    } else if (password.length() > 30) {
                        showAlertDialog("ATTENZIONE", "La password può avere massimo 30 caratteri.");
                    } else if (phone.length() > 10) {
                        showAlertDialog("ATTENZIONE", "Il numero di telefono deve essere di 10 caratteri.");
                    }

                    Date currentDate = Calendar.getInstance().getTime();
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    String dateString = dateFormat.format(currentDate);

                    // birth_date --> data selected as form's input
                    // dateString --> current date

                    int result = birth_date.compareTo(dateString);
                    if (result >= 0) {
                        showAlertDialog("ERRORE", "Inserire una data di nascita che sia valida!");
                    }

                    /* Age calculation */
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                    Date birth   = null;
                    Date current = null;
                    try {
                        birth   = formatter.parse(birth_date);
                        current = formatter.parse(dateString);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    long ageInMillis = Math.abs(current.getTime() - birth.getTime());
                    int years_age = (int) (ageInMillis / (24L * 60L * 60L * 1000L * 365L));

                    // TODO: Accettiamo date di nascita valide fino a persone vecchie di 100 anni?
                    // TODO: generare lo user_id
                    // TODO: provare che tutti i controlli funzionano
                    // TODO: comporre il json da mandare al server C
                }
            });
            t.start();
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        name_text        = findViewById(R.id.NameField);
        surname_text     = findViewById(R.id.SurnameField);
        nickname_text    = findViewById(R.id.SecondNameField);
        phone_text       = findViewById(R.id.PhoneField);
        mail_text        = findViewById(R.id.MailField);
        pass_text        = findViewById(R.id.PwdField);
        conf_pass_text   = findViewById(R.id.ConfPwdField);
        register_button  = findViewById(R.id.button3);
        data_button      = findViewById(R.id.dataButton);

        this.handler = new Handler();

        data_button.setOnClickListener(v -> {
            final Calendar c = Calendar.getInstance();
            int mYear = c.get(Calendar.YEAR) - 20;
            int mMonth = c.get(Calendar.MONTH);
            int mDay = c.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(Sign_up.this,
                    (view, year, monthOfYear, dayOfMonth) -> {
                        // String selectedDate = dayOfMonth + "-" + (monthOfYear + 1) + "-" + year;
                        String selectedDate = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                        data_button.setText(selectedDate);
                    }, mYear, mMonth, mDay);
            datePickerDialog.show();
        });

        Thread t = new Thread(this::manage_register);
        t.start();
    }
}