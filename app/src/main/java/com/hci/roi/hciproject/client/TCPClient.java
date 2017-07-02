package com.hci.roi.hciproject.client;

/**
 * Created by Roi on 02/07/2017.
 */

public class TCPClient {
}
/**

package com.example.roi.finalprojectV1;

import android.graphics.Color;
import android.util.Base64;
import android.util.Base64InputStream;
import android.util.Log;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.nio.CharBuffer;
import java.nio.charset.StandardCharsets;


public class TcpClient {
    final static String RED_HEX = "#e74c3c" , GREEN_HEX = "#2ecc71" , BLUE_HEX = "#3498db";
    public int color_mode;
    public static final String SERVER_IP = "10.0.2.2";// "127.0.0.1"; //server IP address
    public static final String HOME_IP = "192.168.1.35";
    public static final String TEMP_IP = "172.20.17.109";
    public static final int SERVER_PORT = 4444;
    // message to send to the server
    private String mServerMessage;
    // sends message received notifications
    private OnMessageReceived mMessageListener = null;
    // while this is true, the server will continue running
    public int color =0;
    // used to send messages
    private PrintWriter mBufferOut;
    private DataOutputStream imageOutstream;


    // used to read messages from the server
    private BufferedReader mBufferIn;
    private Socket socket;
    private InputStream stream;
    public byte[] decodedImage;

    public TcpClient(OnMessageReceived listener) {
        mMessageListener = listener;
    }


    public void sendMessage(String message) {
        if (mBufferOut != null && !mBufferOut.checkError()) {
            mBufferOut.println(message);
            mBufferOut.flush();
        }
        else{
            Log.e("TCP-SEND-MESSAGE","Error");
        }
    }

    public void stopClient() {
        if (mBufferOut != null) {
            mBufferOut.flush();
            mBufferOut.close();
        }

        mMessageListener = null;
        mBufferIn = null;
        mBufferOut = null;
        mServerMessage = null;
    }

    public boolean IsConnected()
    {
        if (socket!= null){
            return socket.isConnected();
        }
        else
            return false;
    }

    public void run() {
        try {
            //here you must put your computer's IP address.
            InetAddress serverAddr = InetAddress.getByName(TEMP_IP); //or HOME_IP

            Log.e("TCP Client", "C: Connecting...");

            //create a socket to make the connection with the server
            socket = new Socket(serverAddr, SERVER_PORT);
            Log.e("TCP Client", "C: Conneced!");
            try {
                if(color == Color.parseColor(RED_HEX))
                    color_mode=1;
                if(color == Color.parseColor(GREEN_HEX))
                    color_mode=2;
                if(color == Color.parseColor(BLUE_HEX))
                    color_mode=3;
                if (color == Color.YELLOW)//van_gogh
                    color_mode=40;
                if (color == Color.CYAN)//starry_night
                    color_mode=41;
                if (color == Color.MAGENTA)//sketch
                    color_mode=42;
                if (color == Color.WHITE)//toast
                    color_mode=43;
                //sends the message to the server
                mBufferOut = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
                Log.e("TCP Client", "C: Sending Message...(color_mode="+color_mode+", length" + decodedImage.length+")");
                if(decodedImage!=null)
                {
                    imageOutstream=new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
                    //color_mode
                    imageOutstream.writeBytes(String.format("%08x", color_mode)); //decodedImage.length
                    //image_length
                    Log.e("IMAGE SIZE" , String.format("%08x", decodedImage.length));
                    imageOutstream.writeBytes(String.format("%08x", decodedImage.length)); //decodedImage.length
                    //image
                    imageOutstream.write(decodedImage,0,decodedImage.length);
                    imageOutstream.flush();
                }

                stream = socket.getInputStream();
                //in this while the client listens for the messages sent by the server
                byte[] size = new byte[10];
                stream.read(size);
                int total = Integer.decode(new String(size));
                Log.d("STREAM READ" , new String(size)+"|"+total);

                byte[] c = new byte[1024];
                int tmp, len = 0;
                byte[] sb = new byte[total+1024];
                while(len < total) {
                    tmp = stream.read(sb, len, 1024);
                    len += tmp;
                }
                mMessageListener.messageReceived(new String(sb, 0, total));
                //socket.close();


                Log.e("RESPONSE FROM SERVER", "S: Received Message: '" + mServerMessage + "'");

            } catch (Exception e) {

                Log.e("TCP", "S: Error", e);

            } finally {
                //the socket must be closed. It is not possible to reconnect to this socket
                // after it is closed, which means a new socket instance has to be created.
                socket.close();
                Log.d("SOCKET: " , "Closed ("+IsConnected()+")");
            }

        } catch (Exception e) {

            Log.e("TCP", "C: Error", e);

        }

    }

    //Declare the interface. The method messageReceived(String message) will must be implemented in the MyActivity
    //class at on asynckTask doInBackground
    public interface OnMessageReceived {
        public void messageReceived(String message);
    }



}


 */