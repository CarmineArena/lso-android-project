package com.example.history4fun;

import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.SpannableStringBuilder;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class info_opera extends AppCompatActivity {

    private Client client;
    private Handler handler;
    private EditText comment_area     = null;
    private LinearLayout linearLayout = null;
    private ImageButton sButton       = null;
    private String userId, user_name, user_surname;
    private int art_id, isExpert;
    private boolean should_call_on_destroy = true;

    private void showAlertDialog(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(info_opera.this);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setIcon(android.R.drawable.ic_dialog_alert);
        builder.setPositiveButton("OK", (dialog, id) -> dialog.dismiss());

        this.handler.post(() -> {
            AlertDialog dialog = builder.create();
            dialog.show();
        });
    }

    private void show_experts_comments(String artId) {
        @SuppressLint("SetTextI18n") Thread t = new Thread(() -> {
            client.send_json_get_comment_by_id("GET_COMMENT", artId);
            try {
                JSONObject myjson = client.receive_json_multiple_records();
                String flag = myjson.getString("flag");
                Log.i("ARRAY: ", myjson.getJSONArray("retrieved_data").toString());

                switch (flag) {
                    case "SUCCESS":
                        JSONArray retrieved_data = myjson.getJSONArray("retrieved_data");
                        int len = retrieved_data.length();

                        for(int i = 0; i < len; i++) {
                            JSONObject retrieved = retrieved_data.getJSONObject(i);
                            String name = retrieved.getString("name");
                            String surname = retrieved.getString("surname");
                            String comment = retrieved.getString("comment");

                            // Typeface customFont = Typeface.createFromAsset(getAssets(), "Karma-Bold.ttf");
                            TextView newTextView = new TextView(this);
                            // TODO: HERE CHANGE newTextView TEXTVIEW FONT
                            newTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
                            newTextView.setText(name + " " + surname + ": " + comment);
                            // newTextView.setTypeface(customFont);
                            linearLayout.addView(newTextView);
                        }
                        break;
                    case "FAILURE":
                        TextView newTextView = new TextView(this);
                        // TODO: HERE CHANGE newTextView TEXTVIEW FONT
                        newTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
                        newTextView.setText("Al momento non sono presenti commenti!");
                        linearLayout.addView(newTextView);
                        Log.i("NESSUN COMMENTO", " TROVATO");
                        break;
                }
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
        });
        t.start();
    }

    private void manage_user_comment(String artId) {
        if (isExpert == 1) {
            sButton.setOnClickListener(view -> {
                @SuppressLint("SetTextI18n") Thread t = new Thread(() -> {
                    String comment_text = String.valueOf(comment_area.getText());

                    if (isExpert == 1) {
                        if (!comment_text.isEmpty()) {
                            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                            Date currentDate = new Date();
                            Calendar calendar = Calendar.getInstance();
                            calendar.setTime(currentDate);
                            calendar.add(Calendar.DATE, 0);
                            Date today = calendar.getTime();
                            String current_date = dateFormat.format(today);
                            client.send_json_add_comment_by_id("ADD_COMMENT", userId, artId, comment_text, current_date);

                            try {
                                JSONObject myjson = client.receive_json();
                                String flag = myjson.getString("flag");

                                switch (flag) {
                                    case "SUCCESS":
                                        comment_area.setText("");
                                        runOnUiThread(() -> {
                                            TextView new_comment = new TextView(this);
                                            // TODO: HERE CHANGE new_comment TEXTVIEW FONT
                                            new_comment.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
                                            new_comment.setText(user_name + " " + user_surname + ": " + comment_text);
                                            linearLayout.addView(new_comment,3); // WE ADD THE COMMENT TO THE TOP OF THE LIST
                                        });
                                        break;
                                    case "FAILURE":
                                        showAlertDialog("COMMENT ERROR", "Qualcosa è andato storto nella pubblicazione del commento.");
                                        break;
                                }
                            } catch (IOException | JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
                t.start();
            });
        } else {
            sButton.setVisibility(View.GONE);
            sButton.setEnabled(false);
            comment_area.setVisibility(View.GONE);
            comment_area.setEnabled(false);
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_opera);

        this.handler = new Handler();
        client = MainActivity.client;

        Intent intent = getIntent();
        String descrizione = (String) intent.getSerializableExtra("descrizione");
        String chosen_area = (String) intent.getSerializableExtra("area");
        isExpert           = (int)    intent.getSerializableExtra("isExpert");
        userId             = (String) intent.getSerializableExtra("user_id");
        user_name          = (String) intent.getSerializableExtra("user_name");
        user_surname       = (String) intent.getSerializableExtra("user_surname");

        String id = (String) intent.getSerializableExtra("art_id");
        try {
            art_id = Integer.parseInt(id);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

        sButton      = findViewById(R.id.sendButton);
        comment_area = findViewById(R.id.commentView);
        linearLayout = findViewById(R.id.imageContainer);
        ImageView immagine = findViewById(R.id.opera);

        TextView specific_text = findViewById(R.id.SpecificDescription);
        specific_text.setMovementMethod(new ScrollingMovementMethod());
        specific_text.setText(descrizione);

        Drawable drawable = null;

        ImageButton backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> onBackPressed());

        switch (chosen_area) {
            case "full":
            case "jurassic":
                if (art_id == 0) {
                    drawable = getResources().getDrawable(R.drawable.tyrannosaurus_rex);
                    String jurassic1 = "00001";
                    show_experts_comments(jurassic1);
                    manage_user_comment(jurassic1);
                } else if (art_id == 1) {
                    drawable = getResources().getDrawable(R.drawable.hadrosauridae);
                    String jurassic2 = "00002";
                    show_experts_comments(jurassic2);
                    manage_user_comment(jurassic2);
                } else {
                    drawable = getResources().getDrawable(R.drawable.sauropoda);
                    String jurassic3 = "00003";
                    show_experts_comments(jurassic3);
                    manage_user_comment(jurassic3);
                }
                break;
            case "prehistory":
                if (art_id == 0) {
                    drawable = getResources().getDrawable(R.drawable.microlito);
                    String prehistory1 = "00004";
                    show_experts_comments(prehistory1);
                    manage_user_comment(prehistory1);
                } else if (art_id == 1) {
                    drawable = getResources().getDrawable(R.drawable.stonehenge);
                    String prehistory2 = "00005";
                    show_experts_comments(prehistory2);
                    manage_user_comment(prehistory2);
                } else {
                    drawable = getResources().getDrawable(R.drawable.homo_neanderthalensis);
                    String prehistory3 = "00006";
                    show_experts_comments(prehistory3);
                    manage_user_comment(prehistory3);
                }
                break;
            case "egypt":
                if (art_id == 0) {
                    drawable = getResources().getDrawable(R.drawable.pyramidsofgiza_at_night);
                    String egypt1 = "00007";
                    show_experts_comments(egypt1);
                    manage_user_comment(egypt1);
                } else if (art_id == 1) {
                    drawable = getResources().getDrawable(R.drawable.sfinge);
                    String egypt2 = "00008";
                    show_experts_comments(egypt2);
                    manage_user_comment(egypt2);
                } else {
                    drawable = getResources().getDrawable(R.drawable.piramidi);
                    String egypt3 = "00009";
                    show_experts_comments(egypt3);
                    manage_user_comment(egypt3);
                }
                break;
            case "roman":
                if (art_id == 0) {
                    drawable = getResources().getDrawable(R.drawable.gaio_mario);
                    String roman1 = "00010";
                    show_experts_comments(roman1);
                    manage_user_comment(roman1);
                } else if (art_id == 1) {
                    drawable = getResources().getDrawable(R.drawable.romolo_e_remo);
                    String roman2 = "00011";
                    show_experts_comments(roman2);
                    manage_user_comment(roman2);
                } else {
                    drawable = getResources().getDrawable(R.drawable.augusto);
                    String roman3 = "00012";
                    show_experts_comments(roman3);
                    manage_user_comment(roman3);
                }
                break;
            case "greek":
                if (art_id == 0) {
                    drawable = getResources().getDrawable(R.drawable.partenone);
                    String greek1 = "00013";
                    show_experts_comments(greek1);
                    manage_user_comment(greek1);
                } else if (art_id == 1) {
                    drawable = getResources().getDrawable(R.drawable.hermes_con_dioniso);
                    String greek2 = "00014";
                    show_experts_comments(greek2);
                    manage_user_comment(greek2);
                } else {
                    drawable = getResources().getDrawable(R.drawable.cratere);
                    String greek3 = "00015";
                    show_experts_comments(greek3);
                    manage_user_comment(greek3);
                }
                break;
        }

        immagine.setImageDrawable(drawable);
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
}