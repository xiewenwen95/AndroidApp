package iss.workshop.thememorygame;


import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.IBinder;

import androidx.annotation.Nullable;

public class GameMusicService extends Service {
    private MediaPlayer player = null;

    public GameMusicService() {
    }

    @Override
    public void onCreate(){
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId){
        String action = intent.getAction();
        if(action != null){
            if(action.equalsIgnoreCase("gameEnd")){
                playGameEndSound();
            }
        }
        return super.onStartCommand(intent, flags, startId);
    }

    protected void playGameEndSound(){
        if(player == null){
            player = MediaPlayer.create(this, R.raw.gameend);
        }

        player.start();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                stopMediaPlayer();
            }
        }, 5000);
    }

    protected void stopMediaPlayer(){
        if(player != null){
            player.stop();
            player.release();
            player = null;
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}