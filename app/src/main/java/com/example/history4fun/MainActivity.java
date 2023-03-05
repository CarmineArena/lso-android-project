package com.example.history4fun;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import android.widget.EditText;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import org.json.*;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    String MailString;
    static Client client         = null;
    private Handler handler      = null;
    private Button login_button  = null;
    private Button signup_button = null;
    private EditText mail_text   = null;
    private EditText pass_text   = null;

    private void showAlertDialog(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
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

    private Utente createUser(JSONArray retrieved_data) {
        String user_id = null, name = null, surname = null, email = null, password = null, phone_number = null;
        int age = 0, expert = 0;

        try {
            user_id      = retrieved_data.getJSONObject(0).getString("user_id");
            name         = retrieved_data.getJSONObject(0).getString("name");
            surname      = retrieved_data.getJSONObject(0).getString("surname");
            email        = retrieved_data.getJSONObject(0).getString("email");
            password     = retrieved_data.getJSONObject(0).getString("password");
            age          = retrieved_data.getJSONObject(0).getInt("age");
            phone_number = retrieved_data.getJSONObject(0).getString("phone_number");
            expert       = retrieved_data.getJSONObject(0).getInt("expert");
        } catch(JSONException e) {
            e.printStackTrace();
        }
        return new Utente(user_id, name, surname, email, password, age, phone_number, expert);
    }

    private void handle_login_button(Client client) {
        login_button.setOnClickListener(v -> {
            Thread t = new Thread(() -> {
                String email    = String.valueOf(mail_text.getText());
                String password = String.valueOf(pass_text.getText());

                if ((email.isEmpty()) || (password.isEmpty())) {
                    showAlertDialog("ERRORE", "Attenzione, non puoi lasciare campi vuoti!");
                } else {
                    client.send_json_login_msg("LOGIN", email, password);
                    try {
                        mail_text.setText("");
                        pass_text.setText("");
                        JSONObject myjson = client.receive_json();

                        String flag = myjson.getString("flag");
                        if (flag.equals("SUCCESS")) {
                            JSONArray retrieved_data = myjson.getJSONArray("retrieved_data");
                            Utente u = createUser(retrieved_data);

                            Intent intent = new Intent(MainActivity.this, Home.class);
                            intent.putExtra("user", u);
                            startActivity(intent);
                        } else if (flag.equals("FAILURE")) {
                            showAlertDialog("ERRORE", "L'utente dichiarato non esiste!");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
            t.start();
        });
    }

    private void handle_signup_button() {
        signup_button.setOnClickListener(view -> {
            Thread t = new Thread(() -> {
                /* It creates a new intent to get access to SecondActivity from MainActivity */
                Intent intent = new Intent(MainActivity.this, Sign_up.class);
                startActivity(intent);
            });
            t.start();
        });
    }

    private void manage_start_page(Client client) {
        handle_login_button(client);
        handle_signup_button();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.handler = new Handler();

        login_button  = (Button)   findViewById(R.id.button);
        signup_button = (Button)   findViewById(R.id.signup);
        mail_text     = (EditText) findViewById(R.id.MailText);
        pass_text     = (EditText) findViewById(R.id.PasswordText);

        Thread t = new Thread(() -> {
            this.client = Client.getInstance();
            if (!this.client.getError_Connection()) {
                manage_start_page(this.client);
            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("ERRORE DI CONNESSIONE");
                builder.setMessage("Non è stato possibile stabilire una connessione con il server.");
                builder.setIcon(android.R.drawable.ic_dialog_alert);
                builder.setPositiveButton("ESCI", (dialog, id) -> {
                    dialog.dismiss();
                    Runtime.getRuntime().exit(0);
                });

                this.handler.post(() -> {
                    AlertDialog dialog = builder.create();
                    dialog.show();
                });
            }
        });
        t.start();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        MailString = String.valueOf(mail_text.getText());
        // PwdString = String.valueOf(password.getText());

        outState.putString("MailKey", MailString);
        // outState.putString("PwdKey", PwdString);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mail_text.setText(savedInstanceState.getString("MailKey"));
        //password.setText(savedInstanceState.getString("PwdKey"));
    }
}