package com.android.chatty;


public class MessageData {
    int type;
    String message;

    MessageData(int type, String str)
    {
        this.type = type;
        this.message = str;
    }
}
