package com.example.history4fun;

import androidx.appcompat.app.AppCompatActivity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;

public class Forgotten_password extends AppCompatActivity {
    private static Client client;
    private Handler handler        = null;
    private EditText edit_text     = null;
    private Button button          = null;

    private String code = null;
    private String email = null;
    private int flag = 0;
    private boolean should_call_on_destroy = true;

    private void setCode(String code) { this.code = code; }

    public String getCode() { return this.code; }

    private void setEmail(String email) { this.email = email; }

    public String getEmail() { return this.email; }

    private void setFlag(int flag) { this.flag = flag; }

    public int getFlag() { return this.flag; }

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

    private void goBackLogin() {
        AlertDialog.Builder builder = new AlertDialog.Builder(Forgotten_password.this);
        builder.setTitle("CAMBIO PASSWORD RIUSCITO");
        builder.setMessage("Ora verrai riportato al Login.");
        builder.setIcon(android.R.drawable.ic_dialog_info);
        builder.setPositiveButton("VAI", (dialog, id) -> {
            dialog.dismiss();
            Intent intent = new Intent(Forgotten_password.this, MainActivity.class);
            startActivity(intent);
        });

        this.handler.post(() -> {
            AlertDialog dialog = builder.create();
            dialog.show();
        });
    }

    private void changePassword() {
        String new_password = String.valueOf(edit_text.getText());
        if (new_password.isEmpty()) {
            showAlertDialog("ERRORE", "Il campo non può essere vuoto!");
        } else {
            client.send_json_new_password_msg("NEW_PASSWORD", new_password, getEmail());
            JSONObject myjson;
            try {
                myjson = client.receive_json();
                String flag = myjson.getString("flag");
                if (flag.equals("SUCCESS")) {
                    goBackLogin();
                } else { showAlertDialog("ERRORE", "Non è stato possibile aggiornare la password."); }
            } catch (IOException | JSONException e) { e.printStackTrace(); }
        }
    }

    private void checkCode() {
        String code = String.valueOf(edit_text.getText());
        if (code.isEmpty()) {
            showAlertDialog("ERRORE", "Il campo non può essere vuoto!");
        } else if (!code.equals(this.getCode())) {
            showAlertDialog("ERRORE", "Il codice inserito non è corretto, riprovare");
        } else {
            setFlag(this.flag + 1);
            edit_text.setText("");
            edit_text.setHint("Nuova Password");
            showInfoDialog("CODICE INSERITO CORRETTO", "Ora puoi cambiare la password");
        }
    }

    private void checkEmail() {
        String email = String.valueOf(edit_text.getText());
        if (!email.isEmpty()) {
            EmailValidator validator = new EmailValidator();
            if (validator.validate(email)) {
                setEmail(email);
                client.send_json_forgot_password_msg("FRGTPASS", email);
                JSONObject myjson;
                try {
                    myjson = client.receive_json();
                    String flag = myjson.getString("flag");
                    if (flag.equals("SUCCESS")) {
                        String code = myjson.getJSONObject("retrieved_data").getJSONArray("code").getString(0);
                        Log.i("Code auth received: ", code);
                        setCode(code);
                        setFlag(this.flag + 1);
                        edit_text.setText("");
                        edit_text.setHint("Codice");

                        showInfoDialog("UTENTE REGISTRATO", "Per cambiare la password inserire questo codice: " + this.getCode());
                    } else { showAlertDialog("ERRORE", "Operazione fallita... Riprovare."); }
                } catch (IOException | JSONException e) { e.printStackTrace(); }
            } else { showAlertDialog("ERRORE", "L'email inserita non è valida"); }
        } else { showAlertDialog("ERRORE","Il campo dell'email non può essere vuoto!"); }
    }

    private void manage_page() {
        button.setOnClickListener(v -> {
            Thread t = new Thread(() -> {
                if (getFlag() == 0) {
                    checkEmail();
                } else if (getFlag() == 1) {
                    checkCode();
                } else {
                    changePassword();
                }
            });
            t.start();
        });
    }

    // ------------------------------------------------------------------------------ //

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgotten_password);

        client = MainActivity.client;

        edit_text  = findViewById(R.id.field);
        button     = findViewById(R.id.button);

        this.handler = new Handler();

        ImageButton backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> onBackPressed());

        Thread t = new Thread(this::manage_page);
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