package com.example.history4fun;

import android.util.Log;
import org.json.*;
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
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void send_simple_msg(String message) {
        Log.i("SEND_SMP_MSG", " send_simple_message() called.");
        try {
            PrintWriter out_toServer = new PrintWriter(getClientSocket().getOutputStream(), true);
            out_toServer.println(message);
            Log.i("SEND_MSG", message + " sent to server.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void send_json_login_msg(String flag, String email, String password) {
        Log.i("SEND_JSON_LOG", " send_json_login_msg() called.");
        try {
            // PrintWriter out_toServer = new PrintWriter(getClientSocket().getOutputStream(), true);
            JSONObject json = new JSONObject()
                    .put("flag", flag)
                    .put("email", email)
                    .put("password", password);

            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(getClientSocket().getOutputStream()));
            String myjson = json.toString();
            writer.write(myjson);
            writer.flush();
            Log.i("SEND_JSON_LOG", myjson + " sent to server.");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public JSONArray receive_json_array() throws IOException, JSONException {
        Log.i("REC_JSON_ARRAY", "receive_json_array() called.");
        BufferedReader input = new BufferedReader(new InputStreamReader(getClientSocket().getInputStream()));
        StringBuilder jsonStr = new StringBuilder();

        int cc;
        while ((cc = input.read()) != -1) {
            char c = (char) cc;
            jsonStr.append(c);

            if (c == ']') break;
        }

        JSONArray myjson = new JSONArray(jsonStr.toString());
        Log.i("REC_JSON_ARRAY", myjson.toString() + " received from server.");
        return myjson;
    }

    public void close_connection() {
        try {
            getClientSocket().close();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }
}