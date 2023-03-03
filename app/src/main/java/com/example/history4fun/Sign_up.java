package com.example.history4fun;

import androidx.appcompat.app.AppCompatActivity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import java.util.Calendar;

public class Sign_up extends AppCompatActivity {
    private TextView name_text        = null;
    private TextView surname_text     = null;
    private TextView second_name_text = null;
    private TextView phone_text       = null;
    private TextView mail_text        = null;
    private TextView pass_text        = null;
    private TextView conf_pass_text   = null;
    private Button register_button    = null;
    private Button data_button        = null;

    // TODO: QUESTE VARIABILI SERVONO A QUALCOSA?
    private DatePickerDialog datePickerDialog;
    private Button dateButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        name_text        = findViewById(R.id.NameField);
        surname_text     = findViewById(R.id.SurnameField);
        second_name_text = findViewById(R.id.SecondNameField);
        phone_text       = findViewById(R.id.PhoneField);
        mail_text        = findViewById(R.id.MailField);
        pass_text        = findViewById(R.id.PwdField);
        conf_pass_text   = findViewById(R.id.ConfPwdField);
        register_button  = findViewById(R.id.button3);
        data_button      = findViewById(R.id.dataButton);

        Intent intent = getIntent();
        // Client client = (Client) intent.getSerializableExtra("client");

        /*
            Thread t = new Thread(() -> {

            });
            t.start();
        */

        // TODO: MA IO DOVE LO DEVO METTERE QUESTO COSO?
        data_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR) - 20;
                int mMonth = c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(Sign_up.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                String selectedDate = dayOfMonth + "-" + (monthOfYear + 1) + "-" + year;
                                data_button.setText(selectedDate);
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });







    }
}