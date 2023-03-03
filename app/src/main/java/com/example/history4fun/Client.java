package com.example.history4fun;

import android.util.Log;
import org.json.*;
import java.net.*;
import java.io.*;

public class Client {
    private static Client instance;
    private static final int server_port  = 6969;
    private static final String server_ip = "10.0.2.2"; // "172.30.219.119"
    private Socket client_socket          = null;

    private InetAddress server_address = null;
    private boolean error_connection   = false;

    /* GETTERS AND SETTERS */
    public Socket getClientSocket() { return this.client_socket; }

    public void setClient_socket(Socket client_socket) {
        this.client_socket = client_socket;
    }

    public InetAddress getServer_address() {
        return server_address;
    }

    public void setServer_address(InetAddress server_address) { this.server_address = server_address; }

    public boolean getError_Connection() { return this.error_connection; }

    public void setError_connection(boolean error_connection) { this.error_connection = error_connection; }

    /* CONSTRUCTOR */
    private Client() { connect(); }

    /* METHODS */
    public void connect() {
        try {
            int timeout_ms = 5000;
            InetAddress server_address = InetAddress.getByName(this.server_ip);
            setServer_address(server_address);

            Socket client_socket = new Socket();
            client_socket.connect(new InetSocketAddress(getServer_address(), this.server_port), timeout_ms);
            client_socket.setSoTimeout(timeout_ms);
            setClient_socket(client_socket);
        } catch (SocketTimeoutException e) {
            Log.d("SocketTimeoutException", "Client --> connect()");
            setError_connection(true);
        } catch (ConnectException e) {
            Log.e("ConnectException", "Client --> connect()");
            e.printStackTrace();
        } catch (IOException e) {
            Log.d("IOException", "Client --> connect()");
            e.printStackTrace();
        }
    }

    public static synchronized Client getInstance() {
        if (instance == null) {
            instance = new Client();
        }
        return instance;
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

    public JSONObject receive_json() throws IOException, JSONException {
        Log.i("REC_JSON_ARRAY", "receive_json_array() called.");
        BufferedReader input = new BufferedReader(new InputStreamReader(getClientSocket().getInputStream()));
        StringBuilder jsonStr = new StringBuilder();

        int cc, i = 0;
        while ((cc = input.read()) != -1) {
            char c = (char) cc;
            jsonStr.append(c);

            if (c == '}') {
                i++;
                if (i == 2) break;
            }
        }
        JSONObject jsn = new JSONObject(jsonStr.toString());
        Log.i("REC_JSON_ARRAY", jsn + " received from server.");
        return jsn;
    }

    public void close_connection() {
        try {
            getClientSocket().close();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }
}