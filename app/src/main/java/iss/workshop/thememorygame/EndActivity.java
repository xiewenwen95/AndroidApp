package iss.workshop.thememorygame;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.time.Duration;

public class EndActivity extends AppCompatActivity {

    TextView gameEnded;
    TextView timeScore;
    Button newGame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end);


        //get music player to play game end sound
        Intent musicIntent = new Intent(this, GameMusicService.class);
        musicIntent.setAction("gameEnd");
        startService(musicIntent);

        //get time taken to complete game
        Intent intent=getIntent();
        Long scoreTime = intent.getLongExtra("timeScore", 0);

        //convert to MM:SS format string for display
        String timeTaken = convertToTime(scoreTime);

        gameEnded = findViewById(R.id.gameEnded);
        timeScore = findViewById(R.id.timeScore);
        timeScore.setText("Time taken: " + timeTaken);

        newGame = findViewById(R.id.newGame);
        newGame.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });

    }

    public String convertToTime(long scoreTime){
        Duration duration = Duration.ofMillis(scoreTime);
        long seconds = duration.getSeconds();
        long HH = seconds/3600;
        long MM = (seconds % 3600) / 60;
        long SS = seconds%60;
        String inMMSS = String.format("%02d:%02d", MM, SS);

        return inMMSS;
    }

}