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

    void handle_login_button(Client client) {
        login_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String email = String.valueOf(mail_text.getText());
                String password = String.valueOf(pass_text.getText());

                client.send_flag("LOGIN");
                // handle errors!
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        login_button = (Button) findViewById(R.id.button);
        mail_text = (EditText) findViewById(R.id.MailText);
        pass_text = (EditText) findViewById(R.id.editTextTextPassword);

        Thread client_thread = new Thread(() -> {
            client = new Client();
            handle_login_button(client);
        });
        client_thread.start();
    }
}