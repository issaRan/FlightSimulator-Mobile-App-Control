package com.ISSAatOfek.EX4;

import android.os.AsyncTask;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class JoyStickActivity extends AppCompatActivity implements JoyStickView.JoystickListener{
    private TcpClient tcpClient;

    protected void onCreate(Bundle savedInstanceState) {
        new connectToServer().execute();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_joystick);
    }
    public void onJoystickMoved(float xPercent, float yPercent, int source) {
        tcpClient.sendMessage(Float.toString(xPercent),Float.toString(yPercent));

    }
    @Override
    protected void onDestroy(){
        super.onDestroy();
        tcpClient.Stop();;
    }
    public class connectToServer extends AsyncTask<String,String,TcpClient>
    {
        @Override
        protected TcpClient doInBackground (String...strings){
            tcpClient = new TcpClient(getIntent().getStringExtra("ip"),Integer.parseInt(getIntent().getStringExtra("port")),new TcpClient.OnMessageReceived() {
                @Override
                public void messageReceived(String message) {
                    publishProgress(message);
                }
            });
            tcpClient.run();
            return null;
        }
        @Override
        protected void onProgressUpdate (String...values){
            super.onProgressUpdate(values);
        }
    }
}
