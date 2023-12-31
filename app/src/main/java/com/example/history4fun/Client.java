package com.example.history4fun;

import android.os.Build;
import android.util.Log;
import org.json.*;
import java.net.*;
import java.io.*;

public class Client {
    private static Client instance;
    private static final int SERVER_PORT = 6969;
    private static final String SERVER_IP_EMULATOR =  "10.0.2.2"; // "172.18.241.161" (WSL);
    private Socket client_socket           = null;
    private InetAddress server_address     = null;
    private boolean error_connection       = false;

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
    /*
        public static boolean isEmulator() {
            return Build.FINGERPRINT.startsWith("generic")
                    || Build.FINGERPRINT.startsWith("unknown")
                    || Build.MODEL.contains("google_sdk")
                    || Build.MODEL.toLowerCase().contains("emulator")
                    || Build.BOARD == "QC_Reference_Phone";
        }
    */

    public void connect() {
        try {
            int timeout_ms   = 5000;
            int server_port  = SERVER_PORT;
            String server_ip = SERVER_IP_EMULATOR;

            InetAddress server_address = InetAddress.getByName(server_ip);
            Log.i("CONNECTION ADDR: ", server_ip);
            Log.i("CONNECTION PORT: ", String.valueOf(server_port));
            setServer_address(server_address);

            Socket client_socket = new Socket();
            client_socket.connect(new InetSocketAddress(getServer_address(), server_port), timeout_ms); // client_socket.setSoTimeout(timeout_ms);
            setClient_socket(client_socket);
        } catch (SocketTimeoutException e) {
            Log.d("SocketTimeoutException", "Client --> connect()");
            setError_connection(true);
            e.printStackTrace();
        } catch (ConnectException e) {
            Log.e("ConnectException", "Client --> connect()");
            setError_connection(true);
            e.printStackTrace();
        } catch (IOException e) {
            Log.d("IOException", "Client --> connect()");
            setError_connection(true);
            e.printStackTrace();
        }
    }

    public static synchronized Client getInstance() {
        if (instance == null) {
            instance = new Client();
        }
        return instance;
    }

    public void send_json_add_comment_by_id(String flag, String userId, String operaId, String comment_text, String comment_date) {
        Log.i("SEND_JSON_ADD_COMMENT", " send_json_add_comment_by_id() called.");
        try {
            JSONObject json = new JSONObject()
                    .put("flag", flag)
                    .put("user_id", userId)
                    .put("artifact_id", operaId)
                    .put("comment", comment_text)
                    .put("comment_date", comment_date);

            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(getClientSocket().getOutputStream()));
            String myjson = json.toString();
            writer.write(myjson);
            writer.flush();
            Log.i("SEND_JSON_ADD_COMMENT", myjson + " sent to server.");
        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }
    }

    public void send_json_get_comment_by_id(String flag, String operaId) {
        Log.i("SEND_JSON_GET_COMMENT", " send_json_get_comment_by_id() called.");
        try {
            JSONObject json = new JSONObject()
                    .put("flag", flag)
                    .put("artifact_id", operaId);

            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(getClientSocket().getOutputStream()));
            String myjson = json.toString();
            writer.write(myjson);
            writer.flush();
            Log.i("SEND_JSON_GET_COMMENT", myjson + " sent to server.");
        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }
    }

    public void send_json_check_ticket_acquired(String flag, String user_id, String current_date, String selected_area) {
        Log.i("SEND_JSON_CHK_ACQRD_TKT", " send_json_check_ticket_acquired() called.");
        try {
            JSONObject json = new JSONObject()
                    .put("flag", flag)
                    .put("user_id", user_id)
                    .put("ticket_date", current_date)
                    .put("area", selected_area);

            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(getClientSocket().getOutputStream()));
            String myjson = json.toString();
            writer.write(myjson);
            writer.flush();
            Log.i("SEND_JSON_CHK_ACQRD_TKT", myjson + " sent to server.");
        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }
    }

    public void send_json_get_opera_descriptions(String flag, String user_selected_area, String type_description) {
        Log.i("SEND_JSON_GET_TT_TYPE", " send_json_get_ticket_type() called.");
        try {
            JSONObject json = new JSONObject()
                    .put("flag", flag)
                    .put("area", user_selected_area)
                    .put("description", type_description);

            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(getClientSocket().getOutputStream()));
            String myjson = json.toString();
            writer.write(myjson);
            writer.flush();
            Log.i("SEND_JSON_GET_TT_TYPE", myjson + " sent to server.");
        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }
    }

    public void send_json_get_ticket_type(String flag, String user_id, String current_date) {
        Log.i("SEND_JSON_GET_TT_TYPE", " send_json_get_ticket_type() called.");
        try {
            JSONObject json = new JSONObject()
                    .put("flag", flag)
                    .put("user_id", user_id)
                    .put("ticket_date", current_date);

            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(getClientSocket().getOutputStream()));
            String myjson = json.toString();
            writer.write(myjson);
            writer.flush();
            Log.i("SEND_JSON_GET_TT_TYPE", myjson + " sent to server.");
        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }
    }

    public void send_json_get_ticket_msg(String flag, Ticket ticket) {
        Log.i("SEND_JSON_GET_TICKET", " send_json_get_ticket_msg() called.");
        try {
            JSONObject json = new JSONObject()
                    .put("flag", flag)
                    .put("ticket_id", ticket.getTicket_id())
                    .put("user_id", ticket.getUser().getUser_id())
                    .put("n_followers", ticket.getFollowers())
                    .put("ticket_date", ticket.getDate())
                    .put("type", ticket.getType().toString())
                    .put("cost", ticket.getCost())
                    .put("area", ticket.getArea().toString());

            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(getClientSocket().getOutputStream()));
            String myjson = json.toString();
            writer.write(myjson);
            writer.flush();
            Log.i("SEND_JSON_GET_TICKET", myjson + " sent to server.");
        } catch (JSONException | IOException e) {
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
        } catch (JSONException | IOException e) {
                e.printStackTrace();
        }
    }

    public void send_json_register_msg(String flag, String u_id, String name, String surname, String email, String password,
                                       int age, String phone, int expert) {
        Log.i("SEND_JSON_REG", " send_json_register_msg() called.");
        try {
            JSONObject json = new JSONObject()
                    .put("flag", flag)
                    .put("user_id", u_id)
                    .put("name", name)
                    .put("surname", surname)
                    .put("email", email)
                    .put("password", password)
                    .put("age", age)
                    .put("phone_number", phone)
                    .put("expert", expert);

            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(getClientSocket().getOutputStream()));
            String myjson = json.toString();
            writer.write(myjson);
            writer.flush();
            Log.i("SEND_JSON_REG", myjson + " sent to server.");
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
    }

    public void send_json_forgot_password_msg(String flag, String email) {
        Log.i("SEND_JSON_FRGT", " send_json_forgot_password_msg() called.");
        try {
            JSONObject json = new JSONObject()
                    .put("flag", flag)
                    .put("email", email);

            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(getClientSocket().getOutputStream()));
            String myjson = json.toString();
            writer.write(myjson);
            writer.flush();
            Log.i("SEND_JSON_FRGT", myjson + " sent to server.");
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
    }

    public void send_json_new_password_msg(String flag, String new_password, String email) {
        Log.i("SEND_JSON_NEWPASS", " send_json_new_password_msg() called.");
        try {
            JSONObject json = new JSONObject()
                    .put("flag", flag)
                    .put("new_password", new_password)
                    .put("email", email);

            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(getClientSocket().getOutputStream()));
            String myjson = json.toString();
            writer.write(myjson);
            writer.flush();
            Log.i("SEND_JSON_NEWPASS", myjson + " sent to server.");
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
    }

    public void send_json_close_connection(String flag) {
        Log.i("SEND_JSON_CLOSE_CONN", " send_json_close_connection() called.");
        try {
            JSONObject json = new JSONObject()
                    .put("flag", flag);

            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(getClientSocket().getOutputStream()));
            String myjson = json.toString();
            writer.write(myjson);
            writer.flush();
            Log.i("REQUEST_TO_CLOSE_CONN", myjson + " sent to server.");
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
    }

    public JSONObject receive_json_multiple_records() throws IOException, JSONException {
        // Log.i("REC_JSON_ARRAY", "receive_json_opera_descriptions() called.");
        BufferedReader input = new BufferedReader(new InputStreamReader(getClientSocket().getInputStream()));
        StringBuilder jsonStr = new StringBuilder();

        int cc;
        int curlyBraceCount = 0;
        while ((cc = input.read()) != -1) {
            char c = (char) cc;
            jsonStr.append(c);

            if (c == '{') {
                curlyBraceCount++;
            } else if (c == '}') {
                curlyBraceCount--;
                if (curlyBraceCount == 0) {
                    break;
                }
            }
        }

        JSONObject jsn = new JSONObject(jsonStr.toString());
        Log.i("REC_JSON_ARRAY", jsn + " received from server.");
        return jsn;
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