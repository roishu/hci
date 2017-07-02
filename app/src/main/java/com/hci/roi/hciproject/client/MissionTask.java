package com.hci.roi.hciproject.client;

import android.os.AsyncTask;

/**
 * Created by Roi on 02/07/2017.
 */

public class MissionTask extends AsyncTask<String, String, TCPClient> {
    @Override
    protected TCPClient doInBackground(String... strings) {
        return null;
    }
}

/*

    public class ConnectTask extends AsyncTask<String, String, TcpClient> {
        public byte[] encodedImage;
        public int color;
        private StringBuffer resultImageString = new StringBuffer();

        public ConnectTask(byte[] en_image , int position){
            encodedImage=en_image;
            switch(position){
                case 0: color=Color.YELLOW; break; //van_gogh
                case 1: color=Color.CYAN; break; //starry_night
                case 2: color=Color.MAGENTA; break; //sketch
                case 3: color=Color.WHITE; break; //toast
            }

        }


        @Override
        protected TcpClient doInBackground(String... message) {

            //we create a TCPClient object
            mTcpClient = new TcpClient(new TcpClient.OnMessageReceived() {
                @Override
                //here the messageReceived method is implemented
                public void messageReceived(String message) {
                    //this method calls the onProgressUpdate
                    publishProgress(message);
                    resultImageString.append(message);
                    Log.d("messageReceived", ""  + "| " + resultImageString.length()); //values .. the same;
                    //Log.d("messageReceived", "publishProgress WAS ENDED"); //each cycle !


                }
            });
            if (encodedImage!=null)
                mTcpClient.decodedImage=encodedImage;
            //Log.e("ENCODED_IMAGE_SEND",encodedImageR);
            mTcpClient.color = color;
            mTcpClient.run();
            Log.d("doInBackground", "WAS ENDED");
            return null;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(TcpClient tcpClient) {

            super.onPostExecute(tcpClient);
            Log.d("onPostExecute", "WAS CALLED");
            if(hudWork){
                byte[] decodedString = Base64.decode(resultImageString.toString(), Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                Bitmap transferResult = Bitmap.createScaledBitmap(decodedByte,mainImage.getWidth() ,mainImage.getHeight(),true);
                mainImage.setImageBitmap(transferResult);
                hudWork = false;
                hud.dismiss();
            }
        }

        @Override
        protected void onCancelled(TcpClient tcpClient) {
            super.onCancelled(tcpClient);
        }
    }//end of task !

 */
