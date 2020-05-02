package com.vabpack.talkv;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private Button onlineChatoption = null;
    private Button offineChatoption = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        onlineChatoption= (Button)findViewById(R.id.onlinechatoption);
        offineChatoption= (Button)findViewById(R.id.offlinechatoption);


        onlineChatoption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

             startActivity(new Intent(getApplicationContext(),OnlineChatActivity.class));

            }
        });


        offineChatoption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }


}
