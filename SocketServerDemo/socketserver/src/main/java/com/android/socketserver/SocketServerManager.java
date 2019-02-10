package com.android.socketserver;

import android.text.TextUtils;
import android.util.Log;

import com.android.socketserver.constant.SocketConstant;
import com.android.socketserver.constant.SocketServerStatus;
import com.android.socketserver.model.Client;
import com.android.socketserver.thread.ServerListenThread;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by handsomezhou on 2019/2/6.
 * Socket Server Manager
 */

public class SocketServerManager {
    private static final String TAG = "SocketServerManager";
    /**
     * Reference:
     * http://www.runoob.com/design-pattern/singleton-pattern.html
     */
    private static SocketServerManager instance = new SocketServerManager();

    private int mPort = SocketConstant.PORT;
    private int mSocketServerStatus = SocketServerStatus.UNKNOWN;
    private Map<String, Client> mClientMap;
    private ServerListenThread mServerListenThread;
    private OnSocketServer mOnSocketServer;

    public interface OnSocketServer {
        void onConnect(Client client);

        //void onDisconnect(Client client);
        //void onConnect(List<Client> clients);
        void onDisconnect(List<Client> clients);
    }

    private SocketServerManager() {
        initSocketServerManager();
    }

    public static SocketServerManager getInstance() {
        return instance;
    }

    public int getPort() {
        return mPort;
    }

    public void setPort(int port) {
        mPort = port;
    }

    public int getSocketServerStatus() {
        return mSocketServerStatus;
    }

    public void setSocketServerStatus(int socketServerStatus) {
        mSocketServerStatus = socketServerStatus;
    }


    private void add(Client client) {
        do {

            if (null == client) {
                break;
            }

            if (null == mClientMap) {
                initSocketMap();
            }

            String key = client.getKey();
            boolean containsKey = mClientMap.containsKey(key);
            if (true == containsKey) {
                break;
            }

            mClientMap.put(key, client);
            if (null != mOnSocketServer) {
                mOnSocketServer.onConnect(client);
            }

        } while (false);

        return;
    }

    /**
     * @param client
     * @return
     */
    private Client remove(Client client) {
        Client removeClient = null;
        do {

            if (null == client) {
                break;
            }

            List<Client> clients = new ArrayList<>();
            clients.add(client);

            List<Client> removeClients = remove(clients);
            if (removeClients.size() > 0) {
                removeClient = removeClients.get(0);
            }

        } while (false);

        return removeClient;
    }

    private List<Client> remove(List<Client> clients) {
        List<Client> removeClients = new ArrayList<>();

        do {
            if (null == clients || clients.size() <= 0) {
                break;
            }

            Client removeClient = null;
            for (Client client : clients) {
                Object key = client.getKey();
                boolean containsKey = mClientMap.containsKey(key);
                if (false == containsKey) {
                    continue;
                }
                removeClient = mClientMap.remove(key);
                if (null != removeClient) {
                    removeClients.add(removeClient);
                }
            }

            if (null != mOnSocketServer) {
                mOnSocketServer.onDisconnect(removeClients);
            }

        } while (false);

        return removeClients;
    }

    public List<Client> removeAllClients() {
        List<Client> allClients = getAllClients();
        List<Client> removeClients=remove(allClients);
        return removeClients;
    }


    public List<Client> getAllClients() {
        List<Client> clients = new ArrayList<>();
        do {
            if (null == mClientMap || mClientMap.size() <= 0) {
                break;
            }

            clients.addAll(mClientMap.values());
            mClientMap.values();
        } while (false);
        return clients;
    }

    public Client get(String key) {
        Client client = null;
        do {
            if (true == TextUtils.isEmpty(key)) {
                break;
            }

            boolean containsKey = mClientMap.containsKey(key);
            if (false == containsKey) {
                break;
            }

            client = mClientMap.get(key);
        } while (false);

        return client;
    }

    public ServerListenThread getServerListenThread() {
        return mServerListenThread;
    }

    public void setServerListenThread(ServerListenThread serverListenThread) {
        mServerListenThread = serverListenThread;
    }

    public OnSocketServer getOnSocketServer() {
        return mOnSocketServer;
    }

    public void setOnSocketServer(OnSocketServer onSocketServer) {
        mOnSocketServer = onSocketServer;
    }

    private void initSocketServerManager() {
        initSocketMap();
    }

    private void initSocketMap() {
        if (null == mClientMap) {
            mClientMap = new HashMap<>();
        } else {
            mClientMap.clear();
        }
    }

    public boolean startSocketServer() {
        boolean startSocketServerSucess = false;
        do {
            if (getSocketServerStatus() == SocketServerStatus.START) {
                Log.i(TAG, "getSocketServerStatus()==SocketServerStatus.START no necessary to start again.");
                break;
            }
            initSocketMap();
            mServerListenThread = new ServerListenThread(mPort);
            mServerListenThread.setOnServerListenThread(new ServerListenThread.OnServerListenThread() {
                @Override
                public void onAccept(Client client) {
                    add(client);
                }
            });
            mServerListenThread.start();

            setSocketServerStatus(SocketServerStatus.START);
            startSocketServerSucess = true;
            Log.i(TAG, "start Socket Server...");
        } while (false);

        return startSocketServerSucess;
    }

    public boolean stopSocketServer() {
        boolean stopSocketServerSuccess = false;
        do {
            if (getSocketServerStatus() == SocketServerStatus.STOP) {
                Log.i(TAG, "getSocketServerStatus()==SocketServerStatus.STOP no necessary to stop again.");
                break;
            }
            if (null != mServerListenThread) {
                mServerListenThread.exit();
                mServerListenThread = null;
            }

            removeAllClients();
            initSocketMap();
            setSocketServerStatus(SocketServerStatus.STOP);
            stopSocketServerSuccess = true;
            Log.i(TAG, "stop Socket Server...");
        } while (false);
        return stopSocketServerSuccess;
    }

    public boolean restartSocketServer() {
        boolean restartSocketServerSuccess = false;
        Log.i(TAG, "start:restart Socket Server...");
        stopSocketServer();
        restartSocketServerSuccess= startSocketServer();
        Log.i(TAG, "end:restart Socket Server...");

        return restartSocketServerSuccess;
    }

    public boolean disconnect(Client client) {
        Client c = remove(client);

        return (null != c);
    }
}
