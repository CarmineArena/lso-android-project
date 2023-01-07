package com.example.history4fun;

import java.net.*;
import java.io.*;

public class Client {
    private final String message_to_send = "Hello! I am the client!";
    private final int server_port = 6969;
    private Socket client_socket = null;
    private InetAddress server_address = null;

    /* CONSTRUCTOR */
    public Client() {
        connect();
    }

    /* GETTERS AND SETTERS */
    public Socket getClientSocket() {
        return this.client_socket;
    }

    public void setClient_socket(Socket client_socket) {
        this.client_socket = client_socket;
    }

    public InetAddress getServer_address() {
        return server_address;
    }

    public void setServer_address(InetAddress server_address) {
        this.server_address = server_address;
    }

    /* METHODS */
    public void connect() {
        try {
            InetAddress server_address = InetAddress.getByName("10.0.2.2");
            setServer_address(server_address);

            Socket client_socket = new Socket(getServer_address(), this.server_port);
            setClient_socket(client_socket);

            OutputStreamWriter out_toServer = new OutputStreamWriter(getClientSocket().getOutputStream());
            BufferedReader in_fromServer = new BufferedReader(new InputStreamReader(getClientSocket().getInputStream()));

            out_toServer.write(this.message_to_send);
            out_toServer.flush();
            out_toServer.close();
            in_fromServer.close();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    public void send_flag(String flag) {
        try {
            OutputStreamWriter out_toServer = new OutputStreamWriter(getClientSocket().getOutputStream());
            out_toServer.write(flag);
            out_toServer.flush();
            out_toServer.close();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    public void close_connection() { // for now it is unused
        try {
            getClientSocket().close();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }
}