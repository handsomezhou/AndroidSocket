package com.android.socketserver.thread;

import android.util.Log;

import com.android.socketserver.model.Client;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
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
                if(null==mServerSocket){
                    Log.i(TAG,"null==mServerSocket");
                    initServerSocket();
                }else {
                    if(false==mServerSocket.isBound()){
                        Log.i(TAG,"null!=mServerSocket isBound["+mServerSocket.isBound()+"]");
                        mServerSocket.setReuseAddress(true);
                        mServerSocket.bind(new InetSocketAddress(mPort));
                    }
                }

                Log.i(TAG,"before accept() isClosed["+mServerSocket.isClosed()+"]");
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
            Log.i(TAG,"initServerSocket port1["+mPort+"]");

            /**
             * reference:
             * https://stackoverflow.com/questions/24615704/socket-eaddrinuse-address-already-in-use
             */
            mServerSocket = new ServerSocket();
            mServerSocket.setReuseAddress(true);
            mServerSocket.bind(new InetSocketAddress(mPort));

            Log.i(TAG,"initServerSocket port2["+mPort+"]");
        } catch (Exception ex) {
            Log.i(TAG,"initServerSocket port3["+mPort+"]"+"ex:" + ex.getMessage());
        }

        Log.i(TAG,"initServerSocket port4["+mPort+"]");
    }

    private void freeServerSocket() {
        if (null != mServerSocket) {
            try {
                mServerSocket.setReuseAddress(true);
                mServerSocket.close();
            } catch (IOException ex) {
                System.out.print(this.getName() + "ex:" + ex.getMessage());
            } finally {
                mServerSocket = null;
            }
        }
    }
}
