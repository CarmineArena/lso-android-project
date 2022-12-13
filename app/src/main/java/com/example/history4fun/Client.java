package com.example.history4fun;

import java.net.*;
import java.io.*;

public class Client {
    private final int server_port = 6969;
    private Socket client_socket = null;
    private InetAddress server_address = null;

    public Client() {
        connect();
    }

    public void connect() {
        try {
            server_address = InetAddress.getByName("10.0.2.2");
            client_socket = new Socket(server_address, server_port);
        } catch(UnknownHostException e) {
            e.printStackTrace();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    public void handle_connection() {
        try {
            PrintWriter out_toServer = new PrintWriter(client_socket.getOutputStream(), true);
            BufferedReader in_fromServer = new BufferedReader(new InputStreamReader(client_socket.getInputStream()));

            String message_to_send = "Hello! I am the client!";
            out_toServer.println(message_to_send);
            out_toServer.flush();

            String message_received = in_fromServer.readLine();

            out_toServer.close();
            in_fromServer.close();
            client_socket.close();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }
}