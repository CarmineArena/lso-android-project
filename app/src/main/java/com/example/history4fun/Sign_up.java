package com.example.history4fun;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class Sign_up extends AppCompatActivity {
    private static Client client;
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
    private boolean should_call_on_destroy = true;

    private void showAlertDialog(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(Sign_up.this);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setIcon(android.R.drawable.ic_dialog_alert);
        builder.setPositiveButton("OK", (dialog, id) -> dialog.dismiss());

        this.handler.post(() -> {
            AlertDialog dialog = builder.create();
            dialog.show();
        });
    }

    private void goToHome(Utente u, String nick) {
        AlertDialog.Builder builder = new AlertDialog.Builder(Sign_up.this);
        builder.setTitle("REGISTRAZIONE EFFETTUATA");
        builder.setMessage("Ora avrai accesso alla Home del Museo.");
        builder.setIcon(android.R.drawable.ic_dialog_alert);
        builder.setPositiveButton("VAI", (dialog, id) -> {
            dialog.dismiss();
            Intent intent = new Intent(Sign_up.this, Home.class);
            intent.putExtra("user", u);
            intent.putExtra("user_nickname", nick);
            startActivity(intent);
        });

        this.handler.post(() -> {
            AlertDialog dialog = builder.create();
            dialog.show();
        });
    }

    private void goBackLogin() {
        AlertDialog.Builder builder = new AlertDialog.Builder(Sign_up.this);
        builder.setTitle("UTENTE GIA' REGISTRATO");
        builder.setMessage("Ora verrai riportato al Login.");
        builder.setIcon(android.R.drawable.ic_dialog_info);
        builder.setPositiveButton("VAI", (dialog, id) -> {
            dialog.dismiss();
            Intent intent = new Intent(Sign_up.this, MainActivity.class);
            startActivity(intent);
        });

        this.handler.post(() -> {
            AlertDialog dialog = builder.create();
            dialog.show();
        });
    }

    private void manage_register() {
        Log.i("SIGN_UP", "manage_register() called.");
        register_button.setOnClickListener(v -> {
            Thread t = new Thread(() -> {
                String name          = String.valueOf(name_text.getText());
                String surname       = String.valueOf(surname_text.getText());
                String email         = String.valueOf(mail_text.getText());
                String password      = String.valueOf(pass_text.getText());
                String conf_password = String.valueOf(conf_pass_text.getText());
                String phone         = String.valueOf(phone_text.getText());
                String nickname      = String.valueOf(nickname_text.getText());
                String birth_date    = String.valueOf(data_button.getText()); // MySQL data format: YYYY-MM-DD

                EmailValidator validator = new EmailValidator();

                if ((name.isEmpty()) || (surname.isEmpty()) || (email.isEmpty()) || (password.isEmpty())
                        || (conf_password.isEmpty()) || (phone.isEmpty())) {
                    showAlertDialog("ERRORE", "Attenzione, almeno un campo obbligatorio è vuoto!");
                } else if (!conf_password.equals(password)) {
                    showAlertDialog("ERRORE", "Attenzione, la password e la password di conferma devono essere uguali!");
                    pass_text.setText("");
                    conf_pass_text.setText("");
                } else {
                    if (name.length() > 30) {
                        showAlertDialog("ATTENZIONE", "Il nome può avere massimo 30 caratteri.");
                    } else if (surname.length() > 30) {
                        showAlertDialog("ATTENZIONE", "Il cognome può avere massimo 30 caratteri.");
                    } else if (email.length() > 50) {
                        showAlertDialog("ATTENZIONE", "L'email può avere massimo 50 caratteri.");
                    } else if (!validator.validate(email)) {
                        showAlertDialog("ERRORE", "L'email inserita non è valida.");
                    } else if (password.length() > 30) {
                        showAlertDialog("ATTENZIONE", "La password può avere massimo 30 caratteri.");
                    } else if (phone.length() != 10) {
                        showAlertDialog("ATTENZIONE", "Il numero di telefono deve essere di 10 caratteri.");
                    } else {
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                        Date currentDate = new Date();
                        String curr_date = dateFormat.format(currentDate);

                        Calendar calendar = Calendar.getInstance();
                        calendar.setTime(currentDate);
                        calendar.add(Calendar.DATE, -1);
                        Date maxDate = calendar.getTime();

                        try {
                            Date birthDate = dateFormat.parse(birth_date);

                            /* NON PUOI REGISTRARTI SE SEI NATO NEL GIORNO CORRENTE O ADDIRITTURA DOPO IL GIORNO CORRENTE */
                            if (Objects.requireNonNull(birthDate).after(maxDate)) {
                                showAlertDialog("ERRORE", "Inserire una data di nascita che sia valida!");
                            } else {
                                @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                                Date birth   = null;
                                Date current = null;
                                try {
                                    birth   = formatter.parse(birth_date);
                                    current = formatter.parse(curr_date);
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }

                                long ageInMillis = Math.abs(Objects.requireNonNull(current).getTime() - Objects.requireNonNull(birth).getTime());
                                int years_age = (int) (ageInMillis / (24L * 60L * 60L * 1000L * 365L));

                                CharsetGenerator generator = new CharsetGenerator(10);
                                String user_id = generator.get_generated_random_string();

                                if (surname.contains("'"))
                                    surname = surname.replace("'", "''");

                                client.send_json_register_msg("REGISTER", user_id, name, surname, email, password, years_age, phone, 0);
                                try {
                                    JSONObject myjson = client.receive_json();
                                    String flag = myjson.getString("flag");

                                    name_text.setText("");
                                    surname_text.setText("");
                                    mail_text.setText("");
                                    pass_text.setText("");
                                    conf_pass_text.setText("");
                                    phone_text.setText("");
                                    nickname_text.setText("");
                                    data_button.setText("");

                                    if (flag.equals("SUCCESS")) {
                                        String nick = null;
                                        if (!nickname.isEmpty()) nick = nickname;

                                        JSONArray retrieved_data = myjson.getJSONArray("retrieved_data");
                                        Utente u = MainActivity.createUser(retrieved_data);
                                        goToHome(u, nick);
                                    } else if (flag.equals("FAILURE")) {
                                        goBackLogin();
                                    }
                                } catch (IOException | JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
            t.start();
        });
    }

    // ------------------------------------------------------------------------------ //

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

        client  = MainActivity.client;
        this.handler = new Handler();

        ImageButton backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> onBackPressed());

        data_button.setOnClickListener(v -> {
            final Calendar c = Calendar.getInstance();
            int mYear        = c.get(Calendar.YEAR);
            int mMonth       = c.get(Calendar.MONTH);
            int mDay         = c.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(Sign_up.this,
                    (view, year, monthOfYear, dayOfMonth) -> {
                        String selectedDate = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                        data_button.setText(selectedDate);
                    }, mYear, mMonth, mDay);
            datePickerDialog.show();
        });

        Thread t = new Thread(this::manage_register);
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

    // ------------------------------------------------------------------------------ //
}