package com.android.socketserver.model;

import java.net.Socket;

/**
 * Created by handsomezhou on 2019/2/7.
 *
 */

public class Client {
    private long mId;
    private Socket mSocket;

    public Client(Socket socket) {
        mId=socket.hashCode();
        mSocket = socket;
    }

    public long getId() {
        return mId;
    }

    public void setId(long id) {
        mId = id;
    }

    public Socket getSocket() {
        return mSocket;
    }

    public void setSocket(Socket socket) {
        mSocket = socket;
    }

    public String getKey(){
        return String.valueOf(mId);
    }

}
