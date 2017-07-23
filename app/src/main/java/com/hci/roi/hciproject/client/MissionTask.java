package com.hci.roi.hciproject.client;

import android.os.AsyncTask;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * Created by Roi on 02/07/2017.
 */

public class MissionTask extends AsyncTask<Void, Void, Void> {
    private String message;
    private String ip = "ENTER IP HERE"; //afeka : 172.20.16.171
    public MissionTask(String str){
        message = str;
    }

    @Override
    protected Void doInBackground(Void... params) {
        //String str = "backspace";
        try {
            Socket socket = new Socket(
                    ip,
                    4444);

            //right log to the server in a socket.
            DataOutputStream DOS = new DataOutputStream(socket.getOutputStream());
            DOS.writeUTF(message);
            socket.close();
        } catch (IOException e) {
            //this.ioException = e;
        }
        return null;
    }
}

