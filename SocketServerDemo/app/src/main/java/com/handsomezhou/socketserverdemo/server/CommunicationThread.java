package com.handsomezhou.socketserverdemo.server;

import android.text.TextUtils;
import android.util.Log;

import com.android.socketserver.model.Client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;

/**
 * Created by handsomezhou on 2019/2/11.
 */

public class CommunicationThread extends Thread {
    private static final String TAG = "CommunicationThread";
    private long mC2SReceiveMsgCount=0;
    private long mS2CSendMsgCount =0;
    private Socket clientSocket;

    private BufferedReader input;
    private BufferedWriter output;
    private Client mClient;

    public CommunicationThread(Client client) {
        mClient = client;
        clientSocket=mClient.getSocket();
    }

    @Override
    public void run() {

        while (!Thread.currentThread().isInterrupted()) {

            try {

                String readStr = input.readLine();

                if(TextUtils.isEmpty(readStr)){
                    boolean isConnected=clientSocket.isConnected();
                    boolean isBound=clientSocket.isBound();
                    boolean isInputShutdown=clientSocket.isInputShutdown();

                    boolean isOutputShutdown=clientSocket.isOutputShutdown();
                    Log.i(TAG,"C->S receive["+readStr+"]["+mC2SReceiveMsgCount+"] from["+clientSocket.getInetAddress().toString()+"] isConnected["+isConnected+"]isInputShutdown["+isInputShutdown+"]isOutputShutdown["+isOutputShutdown+"] isBound["+isBound+"]");
                    if(isConnected==false||false==isOutputShutdown||false==isInputShutdown){
                        String tips=clientSocket.getInetAddress().toString()+" 该客户端已经断开,服务端关闭该连接";
                        Log.i(TAG,tips);
                        clientSocket.close();
                       // updateConversationHandler.post(new updateUIThread(tips,tips));
                        break;
                    }

                }

                mC2SReceiveMsgCount++;
                String c2SReceiveMsgStr="C->S receive ["+mC2SReceiveMsgCount+"] from["+clientSocket.getInetAddress().toString()+"]:\n"+"["+readStr+"]";
                Log.i(TAG,c2SReceiveMsgStr);

                mS2CSendMsgCount++;
                String s2CSendMsgStr="S->C send ["+mC2SReceiveMsgCount+"] to["+clientSocket.getInetAddress().toString()+"]:\n"+"["+readStr+"]:";
                Log.i(TAG,s2CSendMsgStr);
                PrintWriter out = new PrintWriter(new BufferedWriter(
                        new OutputStreamWriter(clientSocket.getOutputStream())),
                        true);
                String writeStr=readStr;
                out.println(writeStr);

                //updateConversationHandler.post(new updateUIThread(c2SReceiveMsgStr,s2CSendMsgStr));

            } catch (SocketException ex){
                Log.i(TAG,ex.toString());
                boolean isConnected=clientSocket.isConnected();
                boolean isBound=clientSocket.isBound();
                boolean isInputShutdown=clientSocket.isInputShutdown();
                boolean isOutputShutdown=clientSocket.isOutputShutdown();

                if(isConnected==false||false==isOutputShutdown||false==isInputShutdown){
                    String tips=clientSocket.getInetAddress().toString()+" 该客户端已经断开,服务端关闭该连接2";
                    Log.i(TAG,tips);
                    try {
                        clientSocket.close();
                    }catch (IOException ioe) {

                    }
                    //updateConversationHandler.post(new updateUIThread(tips,tips));
                }
                break;
            }catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
