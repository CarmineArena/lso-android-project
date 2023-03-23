package com.example.history4fun;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

public class Forgotten_password extends AppCompatActivity {
    // private static Client client   = MainActivity.client;
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

    private void manage_password_retrieve(){
        mail_button.setOnClickListener(v -> {
            Thread t = new Thread(() -> {
                String email = String.valueOf(mail_text.getText());

                if (!email.isEmpty()) {
                    EmailValidator validator = new EmailValidator();
                    if (validator.validate(email)) {
                        EmailSender sender = new EmailSender("catapano.smn.2001@gmail.com");
                        sender.sendEmail();
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
        manage_password_retrieve();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgotten_password);

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