package com.example.history4fun;

import androidx.appcompat.app.AppCompatActivity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class Forgotten_password extends AppCompatActivity {
    private static Client client;
    private Handler handler        = null;
    private EditText mail_text     = null;
    private EditText code_text     = null;
    private EditText new_pass_text = null;
    private Button mail_button     = null;
    private Button code_button     = null;
    private Button new_pass_button = null;
    private String code = null;

    private void setCode(String code) { this.code = code; }

    public String getCode() { return this.code; }

    // TODO: ARRIVATI ALLA CHIAMATA checkCode(), EMAIL_BUTTON NON DOVREBBE PIU' ESSERE CLICCABILE
    // TODO: ARRIVATI ALLA CHIAMATA changePassword(), EMAIL_BUTTON e CODE_BUTTON NON DOVREBBERO PIU' ESSERE CLICCABILI
    // TODO: PRIMA DEL RE-INDIRIZZAMENTO AL LOGIN, LO DOBBIAMO AVVISARE DEL CORRETTO AGGIORNAMENTO?

    private void showAlertDialog(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(Forgotten_password.this);
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
        AlertDialog.Builder builder = new AlertDialog.Builder(Forgotten_password.this);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setIcon(android.R.drawable.ic_dialog_info);
        builder.setPositiveButton("OK", (dialog, id) -> dialog.dismiss());

        this.handler.post(() -> {
            AlertDialog dialog = builder.create();
            dialog.show();
        });
    }

    private void changePassword(String email) {
        new_pass_button.setOnClickListener(v -> {
            Thread t = new Thread(() -> {
                String new_password = String.valueOf(new_pass_text.getText());
                if (new_password.isEmpty()) {
                    showAlertDialog("ERRORE", "Il campo non può essere vuoto!");
                } else {
                    client.send_json_new_password_msg("NEW_PASSWORD", new_password, email);

                    JSONObject myjson = null;
                    try {
                        myjson = client.receive_json();
                        String flag = myjson.getString("flag");

                        if (flag.equals("SUCCESS")) {
                            Intent intent = new Intent(Forgotten_password.this, MainActivity.class);
                            startActivity(intent);
                            // TODO: PERCHE' UNA VOLTA RE-INDIRIZZATO AL LOGIN I TEXTFIELD NON FUNZIONANO?
                        } else {
                            showAlertDialog("ERRORE", "Non è stato possibile aggiornare la password.");
                        }
                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
            t.start();
        });
    }

    private void checkCode(String email) {
        code_button.setOnClickListener(v -> {
            Thread t = new Thread(() -> {
                String code = String.valueOf(code_text.getText());
                Log.i("codice ricavato input: ", code);
                if (code.isEmpty()) {
                    showAlertDialog("ERRORE", "Il campo non può essere vuoto!");
                } else if (!code.equals(this.getCode())) {
                    showAlertDialog("ERRORE", "Il codice inserito non è corretto, riprovare");
                } else {
                    code_text.setText("");
                    showInfoDialog("CODICE INSERITO CORRETTO", "Ora puoi cambiare la password");
                    changePassword(email);
                }
            });
            t.start();
        });
    }

    private void manage_page() {
        mail_button.setOnClickListener(v -> {
            Thread t = new Thread(() -> {
                String email = String.valueOf(mail_text.getText());

                if (!email.isEmpty()) {
                    EmailValidator validator = new EmailValidator();
                    if (validator.validate(email)) {
                        client.send_json_forgot_password_msg("FRGTPASS", email);
                        JSONObject myjson = null;
                        try {
                            myjson = client.receive_json();
                            String flag = myjson.getString("flag");

                            if (flag.equals("SUCCESS")) {
                                String code = myjson.getJSONObject("retrieved_data").getJSONArray("code").getString(0);
                                Log.i("manage_page(): ", code);
                                setCode(code);
                                showInfoDialog("UTENTE REGISTRATO", "Per cambiare la password inserire questo codice: " + this.getCode());
                                mail_text.setText("");
                                checkCode(email);
                            } else {
                                showAlertDialog("ERRORE", "Operazione fallita... Riprovare.");
                            }
                        } catch (IOException | JSONException e) {
                            e.printStackTrace();
                        }
                    } else {
                        showAlertDialog("ERRORE", "L'email inserita non è valida");
                    }
                } else {
                    showAlertDialog("ERRORE","Il campo dell'email non può essere vuoto!");
                }
            });
            t.start();
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgotten_password);

        client = MainActivity.client;

        mail_text       = findViewById(R.id.MailField);
        code_text       = findViewById(R.id.CodeField);
        new_pass_text   = findViewById(R.id.PasswordField);
        mail_button     = findViewById(R.id.MailButton);
        code_button     = findViewById(R.id.CodeButton);
        new_pass_button = findViewById(R.id.PassowrdButton);

        this.handler = new Handler();

        Thread t = new Thread(this::manage_page);
        t.start();
    }
}