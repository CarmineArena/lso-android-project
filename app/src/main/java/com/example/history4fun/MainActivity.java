package com.example.history4fun;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private Client client = null;
    private Button login_button = null;
    private EditText mail_text = null;
    private EditText pass_text = null;

    private void handle_login_button(Client client) {
        login_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Thread t = new Thread(() -> {
                    String email = String.valueOf(mail_text.getText());
                    String password = String.valueOf(pass_text.getText());

                    // handle errors!
                    client.send_flag("LOGIN");
                });
                t.start();
            }
        });
    }

    private void manage_home_page(Client client) {
        handle_login_button(client);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        login_button = (Button)   findViewById(R.id.button);
        mail_text    = (EditText) findViewById(R.id.MailText);
        pass_text    = (EditText) findViewById(R.id.editTextTextPassword);

        Thread t = new Thread(() -> {
            this.client = new Client();
            manage_home_page(this.client);
        });
        t.start();
    }
}