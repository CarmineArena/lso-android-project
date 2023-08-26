package com.example.history4fun;

import androidx.appcompat.app.AppCompatActivity;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class lista extends AppCompatActivity {
    private Client client;
    // private String[] imagePaths;
    private String area = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista);

        Intent intent = getIntent();
        area        = (String) intent.getSerializableExtra("area");
        // magePaths  = (String[]) intent.getSerializableExtra("image_paths");

        client = MainActivity.client;

        ImageView first  = findViewById(R.id.firstopera);
        ImageView second = findViewById(R.id.secondopera);
        ImageView third  = findViewById(R.id.thirdopera);

        // first.setImageResource(R.drawable.tyrannosaurus_rex);
        // second.setImageResource(R.drawable.sauropoda);
        // third.setImageResource(R.drawable.hadrosauridae);

        Drawable drawable1 = getResources().getDrawable(R.drawable.tyrannosaurus_rex);
        first.setImageDrawable(drawable1);

        Drawable drawable2 = getResources().getDrawable(R.drawable.sauropoda);
        second.setImageDrawable(drawable2);

        Drawable drawable3 = getResources().getDrawable(R.drawable.hadrosauridae);
        third.setImageDrawable(drawable3);

        /* LinearLayout imageContainer = findViewById(R.id.image_container);

        for (String imagePath : imagePaths) {
            ImageView imageView = new ImageView(this);
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
            imageView.setImageBitmap(bitmap);

            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(0, 0, 0, 16);
            imageView.setLayoutParams(layoutParams);
            imageContainer.addView(imageView);
        } */
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
}