package com.example.history4fun;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import java.util.Calendar;

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
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });

        // Signals main thread requesting to show Dialog
        this.handler.post(new Runnable() {
            @Override
            public void run() {
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
    }

    private void manage_register() {
        // TODO: Controllare che ogni singolo campo vada bene sencondo la progettazione del database
        // TODO: relativamente alla data, accettiamo date di nascita valide fino a persone vecchie di 100 anni?
        // TODO: Non possiamo accettare valida una persona nata un giorno che viene dopo quello corrente

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

                // TODO: CALCOLARE L'ETA DELL'UTENTE
                // TODO: SE IL NICKNAME NON E' STATO SCELTO SCRIVERE NOME E COGNOME NELLA HOME E
                    // CAMBIARE "SECONDO NOME" in "NICKNAME"

                if ((name.isEmpty()) || (surname.isEmpty()) || (email.isEmpty()) || (password.isEmpty())
                        || (conf_password.isEmpty()) || (phone.isEmpty())) {
                    showAlertDialog("ERRORE", "Attenzione, almeno un campo obbligatorio è vuoto!");
                } else if (!conf_password.equals(password)) {
                    showAlertDialog("ERRORE", "Attenzione, la password e la password di conferma devono essere uguali!");
                } else {
                    Log.i("ALERT", "TUTTO OK");
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

        data_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR) - 20;
                int mMonth = c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(Sign_up.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                // String selectedDate = dayOfMonth + "-" + (monthOfYear + 1) + "-" + year;
                                String selectedDate = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                                data_button.setText(selectedDate);
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });

        Thread t = new Thread(() -> {
            manage_register();
        });
        t.start();
    }
}