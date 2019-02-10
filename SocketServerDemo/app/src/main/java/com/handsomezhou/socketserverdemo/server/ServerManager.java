package com.handsomezhou.socketserverdemo.server;

import com.android.socketserver.SocketServerManager;
import com.android.socketserver.model.Client;
import com.handsomezhou.socketserverdemo.util.LogUtil;

import java.util.List;

/**
 * Created by handsomezhou on 2019/2/9.
 */

public class ServerManager {
    private static final String TAG = "ServerManager";
    /**
     * Reference:
     * http://www.runoob.com/design-pattern/singleton-pattern.html
     */
    private static ServerManager instance = new ServerManager();

    private ServerManager() {
        initServerManager();
    }

    public static ServerManager getInstance() {
        return instance;
    }

    private void initServerManager() {
        SocketServerManager.getInstance().setOnSocketServer(new SocketServerManager.OnSocketServer() {
            @Override
            public void onConnect(Client client) {
                LogUtil.i(TAG,"client "+client.getSocket().getInetAddress().getHostAddress()+":"+client.getSocket().getInetAddress().getHostName());
            }

            @Override
            public void onDisconnect(List<Client> clients) {

            }
        });


    }

    public boolean startServer(){
        return SocketServerManager.getInstance().startSocketServer();
    }

    public boolean stopServer(){
        return SocketServerManager.getInstance().stopSocketServer();
    }

    public boolean restartServer(){
        return SocketServerManager.getInstance().restartSocketServer();
    }






}
