package com.example.history4fun;

import android.util.Log;
import java.net.*;
import java.io.*;

public class Client {
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

    public void setServer_address(InetAddress server_address) { this.server_address = server_address; }

    /* METHODS */
    public void connect() {
        try {
            InetAddress server_address = InetAddress.getByName("10.0.2.2");
            setServer_address(server_address);

            Socket client_socket = new Socket(getServer_address(), this.server_port);
            setClient_socket(client_socket);
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    public void send_msg(String message) {
        try {
            PrintWriter out_toServer = new PrintWriter(getClientSocket().getOutputStream(), true);
            out_toServer.println(message);
            Log.i("SEND_MSG", message + " has been sent to server.");
        } catch (IOException e) { e.printStackTrace(); }
    }

    public void respond_specific_message(String what_to_send, String specific_msg) {
        if (what_to_send != null && specific_msg != null) {
            try {
                BufferedReader input = new BufferedReader(new InputStreamReader(getClientSocket().getInputStream()));
                String response;
                while ((response = input.readLine()) != null) {
                    if (response.equals(specific_msg)) {
                        Log.i("REC_MSG", specific_msg + " has been sent received from the server.");
                        send_msg(what_to_send);
                        Log.i("SEND_MSG", what_to_send + " has been sent to server.");
                        break;
                    }
                }
            } catch (IOException e) { e.printStackTrace(); }
        } else {
            // manage this error
        }
    }

    public void close_connection() {
        try {
            getClientSocket().close();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }
}