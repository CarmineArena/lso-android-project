package com.example.history4fun;

import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.content.Intent;
import android.os.Bundle;

public class payment extends AppCompatActivity {
    private Client client;
    private Utente user;
    private String museum_area = null;

    // ------------------------------------------------------------------------------ //

    // TODO: CREARE OGGETTO TICKET E ALLEGARCI L'UTENTE CHE VIENE PASSATO ALL'ACTIVITY
    // TODO: BISOGNA INSERIRE I CAMPI PER SCEGLIERE LA DATA DI PRENOTAZIONE!
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        Intent intent = getIntent();
        user        = (Utente) intent.getSerializableExtra("user");
        museum_area = (String) intent.getSerializableExtra("museum_area");

        client = MainActivity.client;

        // Log.i("USER_ID_REQUEST: ", user.getUser_id());
        // Log.i("MUSEUM_AREA: ", museum_area);

    }

    @Override
    protected void onStart() { super.onStart(); }

    @Override
    protected void onResume() { super.onResume(); }

    @Override
    protected void onPause() { super.onPause(); }

    @Override
    protected void onStop() { super.onStop(); }

    @Override
    protected void onRestart(){ super.onRestart(); }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Thread t = new Thread(() -> {
            client.send_json_close_connection("STOP_CONNECTION");
            client.close_connection();
        });
        t.start();
    }

    // ------------------------------------------------------------------------------ //
}