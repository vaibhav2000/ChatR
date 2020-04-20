package com.android.chatty;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity {

    private Button onlineChatBut =null;
    private Button wifip2pChatBut =null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        onlineChatBut = (Button)findViewById(R.id.onLineChatbut);
        wifip2pChatBut= (Button)findViewById(R.id.wifiP2PChatbut);


        onlineChatBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), OnlineChatActivity.class));

            }
        });


        wifip2pChatBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                   startActivity(new Intent(getApplicationContext(), WifiP2PActivity.class));
            }
        });

    }
}
