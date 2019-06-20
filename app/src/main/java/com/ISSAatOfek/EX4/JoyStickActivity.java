package com.ISSAatOfek.EX4;

import android.os.AsyncTask;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

// The Joystick Screen Activity
public class JoyStickActivity extends AppCompatActivity implements JoyStickView.JoystickListener{
    // The Tcp Client for communication.
    private TcpClient tcpClient;

    // The function that runs on creation.
    protected void onCreate(Bundle savedInstanceState) {
        // Connect to the server.
        new connectToServer().execute();
        // Init AppCompactActivity
        super.onCreate(savedInstanceState);
        // Setting the content view to the joystick's layout.
        setContentView(R.layout.activity_joystick);
    }
    // The function called when the joystick moved.
    public void onJoystickMoved(float xPercent, float yPercent, int source) {
        // Send joystick's values to the server.
        tcpClient.sendMessage(Float.toString(xPercent),Float.toString(yPercent));

    }
    // The function called upon destruction of the activity.
    @Override
    protected void onDestroy(){
        super.onDestroy();
        // Stop the connection to the server.
        tcpClient.Stop();;
    }
    // AsyncTask for connecting to the server
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
