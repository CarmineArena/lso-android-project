package com.example.history4fun;

import androidx.appcompat.app.AppCompatActivity;
import android.app.AlertDialog;
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

    private void changePassword() {
        // TODO: REINDIRIZZARE AL LOGIN
    }

    private void checkCode() {
        // TODO: ABILITARE IL BOTTONE PER CONTROLLARE IL CODICE
    }

    private void checkEmail() {
        mail_button.setOnClickListener(v -> {
            Thread t = new Thread(() -> {
                String email = String.valueOf(mail_text.getText());

                if (!email.isEmpty()) {
                    EmailValidator validator = new EmailValidator();
                    if (validator.validate(email)) {
                        client.send_json_forgot_password_msg("FRGTPASS", email);

                        // 1. LEGGERE SULLA CLIENT SOCKET IL JSON RICEVUTO DAL SERVER
                        JSONObject myjson = null;
                        try {
                            myjson = client.receive_json();
                            String flag = myjson.getString("flag");

                            if (flag.equals("SUCCESS")) {
                                // 2. SE "SUCCESS" LEGGERE IL CODICE NEL JSON E EFFETTUARE I RELATIVI CONTROLLI
                                String code = myjson.getJSONObject("retrieved_data").getJSONArray("code").getString(0);
                                Log.i("CODE RECEIVED: ", code);

                                // TODO: 3. checkCode()
                                // TODO: 4. SE IL CODICE E' CORRETTO, PUOI MANDARE AL SERVER UNA FLAG "ALTER PASSWORD" PER ALTERARE IL DB.
                            } else {
                                // 2. SE "FAILURE MOSTRARE UN ALERTDIALOG
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

    private void manage_page() {
        checkEmail();
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

        code_button.setEnabled(false);
        new_pass_button.setEnabled(false);

        this.handler = new Handler();

        Thread t = new Thread(this::manage_page);
        t.start();
    }
}