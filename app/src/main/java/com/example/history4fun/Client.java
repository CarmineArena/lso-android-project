package com.example.history4fun;

import java.net.*;
import java.io.*;

public class Client {
    private final int server_port = 6969;
    private Socket client_socket = null;
    private InetAddress server_address = null;
    PrintWriter out_toServer = null;
    BufferedReader in_fromServer = null;

    public Client() {
        connect();
    }

    public void connect() {
        try {
            server_address = InetAddress.getByName("10.0.2.2");
            client_socket = new Socket(server_address, server_port);

            out_toServer = new PrintWriter(client_socket.getOutputStream(), true);
            in_fromServer = new BufferedReader(new InputStreamReader(client_socket.getInputStream()));

            String message_to_send = "Hello! I am the client!";
            out_toServer.println(message_to_send);
            out_toServer.flush();
            String message_received = in_fromServer.readLine();

            out_toServer.close();
            in_fromServer.close();
        } catch(UnknownHostException e) {
            e.printStackTrace();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    public void send_flag(String flag) {
        try {
            out_toServer = new PrintWriter(client_socket.getOutputStream(), true);
            out_toServer.println(flag);
            out_toServer.flush();
            out_toServer.close();
            close_connection();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    public void close_connection() {
        try {
            client_socket.close();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }
}