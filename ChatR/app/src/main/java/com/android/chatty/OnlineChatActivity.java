package com.android.chatty;

import android.app.Activity;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class OnlineChatActivity extends Activity {

    private Socket socket            = null;

    private Button sendbutton= null;
    private EditText sendedittext= null;

    Handler handler = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_online_chat);


        handler = new Handler();

        sendbutton = (Button)findViewById(R.id.sendbutton);
        sendedittext= (EditText)findViewById(R.id.sendeditext);


        connectToSocketFunction();


        sendbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                sendMessage(sendedittext.getText().toString());
                sendedittext.setText("");
            }
        });


        listenAlways();


    }

    private void connectToSocketFunction()
    {
        new Thread(new Runnable() {
            @Override
            public void run() {

                while(true) {
                    try {
                        socket = new Socket("192.168.43.45", 5000);

                        if(socket == null)
                            continue;

                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getApplicationContext(), "Connected To The Server Now", Toast.LENGTH_LONG).show();
                            }
                        });


                        break;
                    } catch (Exception u) {
                        u.printStackTrace();
                    }

                }

            }
        }).start();
    }

    private void sendMessage(final String str)
    {

        if(socket == null) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getApplicationContext(), "Not Connected To The Server", Toast.LENGTH_LONG).show();
                }
            });

            return;
        }

        new Thread(new Runnable() {
            @Override
            public void run() {

                while(true)
                {


                    try
                    {
                        DataOutputStream out = new DataOutputStream(socket.getOutputStream());
                        out.writeUTF(str);
                    }
                    catch(IOException i)
                    {
                        i.printStackTrace();
                    }

                    break;

                }

            }
        }).start();
    }

    private void listenAlways()
    {
        new Thread(new Runnable() {
            @Override
            public void run() {


                while(socket == null)
                    continue;

                while(true)
                {

                    try
                    {
                        DataInputStream in = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
                        final String temp = in.readUTF();

                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getApplicationContext(), temp+"", Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                    catch(IOException i)
                    {
                        i.printStackTrace();
                    }

                    break;

                }

            }
        }).start();

    }

}
