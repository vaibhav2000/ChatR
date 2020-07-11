package com.android.chatty;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.ArrayList;

public class OnlineChatActivity extends AppCompatActivity {

    Socket s= null;
    static boolean flag=true;

    final private int UPADTE_LISTVIEW = 21312;
    final private int SHOW_THREAD_TOAST= 40392;

    private ImageButton messagesendbutton = null;
    private EditText editTextMessage = null;
    private ListView listhold= null;
    private CustomListAdapter listadapter=null;

    Handler handler = handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(msg.what==UPADTE_LISTVIEW){

                listadapter.add(new MessageData(2,msg.obj.toString()));
                showListbottom();

            }
            else
            if(msg.what==SHOW_THREAD_TOAST)
            {
                Toast.makeText(getApplicationContext(),msg.obj.toString(),Toast.LENGTH_SHORT).show();
            }
            super.handleMessage(msg);
        }
    };


    @SuppressLint("HandlerLeak")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_online_chat);

        setUpSocketStream();
        listenAlways();

        messagesendbutton = (ImageButton)findViewById(R.id.sendmessagebutton);
        editTextMessage = (EditText)findViewById(R.id.edittextField);
        listhold = (ListView)findViewById(R.id.messageListhold);



        listadapter = new CustomListAdapter(this, R.id.listtextHere, new ArrayList<MessageData>());
        listhold.setAdapter(listadapter);



        messagesendbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String str= "";
                str= editTextMessage.getText().toString();

                if(!str.equals(""))
                {listadapter.add(new MessageData(1,str));
                    Log.e("TAGGER",str);
                    editTextMessage.setText("");
                    showListbottom();
                    sendMessage(str);
                }

            }
        });


    }

    private int connectionTries;
    void setUpSocketStream()
    {

        flag=true;
        s=null;
        connectionTries=0;

        new Thread(new Runnable() {
            @Override
            public void run() {


                while(true) {
                    try {

                        if(s!=null)
                            return;

                        s = new Socket("192.168.43.45", 5000);
                        flag=false;

                        Message msg = handler.obtainMessage();
                        msg.what = SHOW_THREAD_TOAST;
                        msg.obj = "Server Connection Established";
                        handler.sendMessage(msg);

                        break;
                    } catch (Exception e) {

                        if(connectionTries==3)
                        {
                            Message msg = handler.obtainMessage();
                            msg.what = SHOW_THREAD_TOAST;
                            msg.obj = "Server Unavailable, Aborting Chat Feature";
                            handler.sendMessage(msg);


                            return;}

                        Message msg = handler.obtainMessage();
                        msg.what = SHOW_THREAD_TOAST;
                        msg.obj = "Error Connecting to the Chat Server, Retrying...";
                        handler.sendMessage(msg);
                        connectionTries++;

                        try {
                            Thread.sleep(5000);
                        } catch (InterruptedException ex) {
                            ex.printStackTrace();
                        }

                    }
                }
            }
        }).start();



    }

    void showListbottom()
    {
        listhold.post(new Runnable() {
            @Override
            public void run() {
                listhold.setSelection(listadapter.getCount()-1);
            }
        });
    }

    void sendMessage(final String str)
    {
        new Thread(new Runnable(){

            @Override
            public void run() {
                // TODO Auto-generated method stub

                while(true)
                {
                    if(flag)
                        continue;

                    try{

                        DataOutputStream outp= new DataOutputStream(s.getOutputStream());
                        outp.writeUTF(str);
                    }catch(Exception e){

                        Message msg = handler.obtainMessage();
                        msg.what = SHOW_THREAD_TOAST;
                        msg.obj = "Error Connecting To The Server";
                        handler.sendMessage(msg);

                        setUpSocketStream();

                    }

                    break;
                }

            }
        }).start();
    }

    void listenAlways()
    {
        new Thread(new Runnable(){

            @Override
            public void run() {

                while(true)
                {
                    if(flag)
                        continue;

                    try{

                        DataInputStream inp = new DataInputStream(s.getInputStream());
                        String str=  inp.readUTF();


                        Message msg = handler.obtainMessage();
                        msg.what = UPADTE_LISTVIEW;
                        msg.obj = str;
                        handler.sendMessage(msg);

                    }catch(Exception e){

                        Message msg = handler.obtainMessage();
                        msg.what = SHOW_THREAD_TOAST;
                        msg.obj = "Error Connecting To The Server";
                        handler.sendMessage(msg);

                        setUpSocketStream();


                    }

                }

            }
        }).start();
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.custommenu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    // handle button activities
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.mybutton) {
            startActivity(new Intent(getApplicationContext(),WebVideoChatActivity.class));


        }
        return super.onOptionsItemSelected(item);
    }

}
