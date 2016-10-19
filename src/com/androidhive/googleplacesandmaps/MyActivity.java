package com.androidhive.googleplacesandmaps;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;

public class MyActivity extends Activity {


    /**
     * Called when the activity is first created.
     */


    MediaPlayer mp;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        // METHOD 1
//         mp=MediaPlayer.create(this,R.raw.death);
//         mp.start();

        /****** Create Thread that will sleep for 5 seconds *************/
        Thread background = new Thread() {
            public void run() {

                try {
                    // Thread will sleep for 5 seconds
                    sleep(2000);

                    // After 5 seconds redirect to another intent

                    Intent i=new Intent(MyActivity.this,MainActivity.class);
                    startActivity(i);
                    //Remove activity
                    finish();

                } catch (Exception e) {

                }
            }
        };


        // start thread
        background.start();


    }

    @Override
    protected void onDestroy() {
        
        super.onDestroy();
    }
}

