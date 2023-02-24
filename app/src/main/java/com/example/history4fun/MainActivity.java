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
    private Handler handler = null;
    String MailString;
    private Client client        = null;
    private Button login_button  = null;
    private Button signup_button = null;
    private EditText mail_text   = null;
    private EditText pass_text   = null;

    private void handle_login_button(Client client) {
        login_button.setOnClickListener(v -> {
            Thread t = new Thread(() -> {
                String email    = String.valueOf(mail_text.getText());
                String password = String.valueOf(pass_text.getText());

                if ((email.isEmpty()) || (password.isEmpty())) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

                    builder.setTitle("ERRORE");
                    builder.setMessage("Attenzione, non puoi lasciare campi vuoti!");

                    builder.setIcon(android.R.drawable.ic_dialog_alert);
                    // builder.setIcon(android.R.drawable.ic_dialog_info);

                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // When we click "OK"
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
                } else {
                    client.send_json_login_msg("LOGIN", email, password);
                    try {
                        mail_text.setText("");
                        pass_text.setText("");
                        JSONObject myjson = client.receive_json();
                        /* TODO:
                            1. Analizzare il campo flag dell'oggetto json ricevuto sulla socket e gestire di conseguenza.
                            2. Se leggi SUCCESS, devi creare l'oggetto utente con tutti i dati e passarlo alla schermata "Home".
                        */
                        Intent intent = new Intent(MainActivity.this, Home.class);
                        startActivity(intent);
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

    private void handle_signup_button(Client client) {
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
        handle_signup_button(client);
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
            this.client = new Client();
            manage_start_page(this.client);
        });
        t.start();

        // final Button signup = (Button) findViewById(R.id.signup);
        /*
            signup.setOnClickListener(view -> {
                // Crea l'intent ha passare dalla MainActivity alla SecondActivity
                Intent intent = new Intent(MainActivity.this, Sign_up.class);
                startActivity(intent);
            });
        */
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