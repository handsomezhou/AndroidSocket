package com.android.socketserver.thread;

import android.util.Log;

import com.android.socketserver.model.Client;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by handsomezhou on 2019/2/7.
 */

public class ServerListenThread extends Thread {
    private static final String TAG = "ServerListenThread";
    private int mPort;
    private ServerSocket mServerSocket;
    private OnServerListenThread mOnServerListenThread;
    public interface OnServerListenThread{
        void onAccept(Client client);
    }

    public ServerListenThread(int port) {
        mPort = port;
        init();
    }

    @Override
    public void run() {
        Socket socket = null;
        Client client = null;
        while (false == Thread.currentThread().isInterrupted()) {
            try {
                Log.i(TAG,"before accept()");
                socket= mServerSocket.accept();
                Log.i(TAG, "after accept()"+socket.getInetAddress().getHostAddress()+":"+socket.getInetAddress().getHostName());
                if(null!=mOnServerListenThread){
                    client=new Client(socket);
                    Log.i(TAG,"onAccept client id["+client.getId()+"]");
                    mOnServerListenThread.onAccept(client);
                }else {
                    Log.i(TAG,"null==mOnServerListenThread");
                }
            }catch (Exception ex){
                Log.i(TAG,ex.getMessage());
            }
        }

        free();
    }

    public OnServerListenThread getOnServerListenThread() {
        return mOnServerListenThread;
    }

    public void setOnServerListenThread(OnServerListenThread onServerListenThread) {
        mOnServerListenThread = onServerListenThread;
    }

    public void exit(){
        this.interrupt();
    }

    private void init(){
        initServerSocket();
    }

    private void free(){
        freeServerSocket();
    }

    private void initServerSocket(){
        try {
            mServerSocket = new ServerSocket(mPort);
        } catch (Exception ex) {
            System.out.print(this.getName() + "ex:" + ex.getMessage());
        }
    }

    private void freeServerSocket() {
        if (null != mServerSocket) {
            try {
                mServerSocket.close();
            } catch (IOException ex) {
                System.out.print(this.getName() + "ex:" + ex.getMessage());
            } finally {
                mServerSocket = null;
            }
        }
    }
}
