package com.ISSAatOfek.EX4;

import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

public class TcpClient {

    private String SERVER_IP; //your computer IP address
    private int SERVER_PORT;
    private Socket socket;
    // message to send to the server
    private String mServerMessage;
    // sends message received notifications
    private OnMessageReceived mMessageListener = null;
    // while this is true, the server will continue running
    private boolean mRun = false;
    // used to send messages
    private PrintWriter mBufferOut;
    // used to read messages from the server
    private BufferedReader mBufferIn;
    pathContainer pathContainer;
    public TcpClient(String ip, int port, OnMessageReceived listener) {
        SERVER_IP = ip;
        SERVER_PORT = port;
        mMessageListener = listener;
        pathContainer = new pathContainer();
    }

    public void sendMessage(final String x, final String y) {
        try {
            if (null != socket) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        PrintWriter out = null;
                        try {
                            out = new PrintWriter(new BufferedWriter(
                                    new OutputStreamWriter(socket.getOutputStream())),
                                    true);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        out.println(pathContainer.getAileronPath() + x + "\r\n");
                        out.println(pathContainer.getElevator() + y + "\r\n");
                    }
                }).start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void run() {

        mRun = true;

        try {
            InetAddress serverAddr = InetAddress.getByName(SERVER_IP);
            this.socket = new Socket(serverAddr, SERVER_PORT);
            try {
                mBufferOut = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
                mBufferIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                while (mRun) {
                    mServerMessage = mBufferIn.readLine();
                    if (mServerMessage != null && mMessageListener != null) {
                        mMessageListener.messageReceived(mServerMessage);
                    }
                }
            } catch (Exception e) {
                Log.e("TCP", "S: Error", e);
            } finally {
                socket.close();
            }
        } catch (Exception e) {
            Log.e("TCP", "C: Error", e);
        }
    }
    public void Stop() {
        try {
            if(socket != null) {
                socket.close();
            } else {
                return;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public interface OnMessageReceived {
        public void messageReceived(String message);
    }
}