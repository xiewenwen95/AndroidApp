package iss.workshop.thememorygame;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.SystemClock;
import android.view.View;
import android.widget.Chronometer;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

@SuppressWarnings("unchecked")
public class GameActivity extends AppCompatActivity implements View.OnClickListener {

    ObjectAnimator animator1;
    ObjectAnimator animator2;
    ArrayList<Integer> shuffled;
    ArrayList<ImageButton> Pressed;
    private int[] gameBtnsID = new int[12];
    private ImageButton[] gameBtns = new ImageButton[12];
    private TextView result;
    private TextView infoText;
    private Chronometer timer;
    private int chosenCount;
    private int currChosenPic,prevChosenPic,matchedCount;
    private Bitmap currBit,prevBit;
    private ImageButton currButt,prevButt;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        initializeGame();
        result = findViewById(R.id.result);
        result.setText(getString(R.string.result,0));

        infoText = findViewById(R.id.infoText);
        timer = findViewById(R.id.timer);
        timer.setBase(SystemClock.elapsedRealtime());
        timer.start();

        for(int i =0; i<12; i++){
            gameBtnsID[i] = getResources().getIdentifier("gameBtn"+ (i+1),"id",this.getPackageName());
            gameBtns[i] = findViewById(gameBtnsID[i]);
            gameBtns[i].setOnClickListener(this);
        }
        File dir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);

        ArrayList<Integer> selectedIndices = (ArrayList<Integer>) getIntent().getSerializableExtra("idx");
        shuffled = shuffleList(selectedIndices);

        for(int i=0; i<12; i++){
            ImageButton btn = findViewById(gameBtnsID[i]);
            btn.setImageResource(R.drawable.memorygame);
        }
    }
    @Override
    public void onClick(View view){
        int currChosenPic = view.getId();
        int currentButtonIndex = currentClickedIndex(currChosenPic);
        currButt = findViewById(currChosenPic);

        if(chosenCount < 2)
        {
            chosenCount++;
            imageClicked(currentButtonIndex);
            if(chosenCount== 1)
            {
                prevBit = currBit;
                prevButt = currButt;
                prevChosenPic=currChosenPic;
            }
            // to avoid double click
            if (currChosenPic == prevChosenPic){
                chosenCount = 1;
            }
            if(currChosenPic != prevChosenPic)//if picture chosen not equivalent to previous chosen
            {
                if(!currBit.sameAs(prevBit) && prevButt != null) // User Made the Wrong guess
                {

                    setButtonsUnclickable();
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            // flip both img to memory game img and make sound
                            Bitmap pic = BitmapFactory.decodeResource(getResources(), R.drawable.memorygame);
                            flipCard(currButt,pic);
                            flipCard(prevButt,pic);
                            matchWrongSound();

                            chosenCount = 0; // set chosencount to 0 so that user can pick 2 again
                            prevChosenPic= 0;  // set previous button to 0 aka no button/picture picked
                            setButtonsClickable();
                        }
                    }, 1000);
                }
                else // user made right guess
                {
                    // disable matched imgbutton
                    matchedCount++;
                    // make matched sound here
                    matchRightSound();
                    Toast.makeText(this,"Image Matched!",Toast.LENGTH_SHORT).show();
                    result.setText(String.valueOf(matchedCount) + " out of 6 matched");
                    if(matchedCount == 6)
                    {
                        //stop timer and send time taken to End
                        timer.stop();
                        long elapsedTime = SystemClock.elapsedRealtime() - timer.getBase();
                        Intent intent = new Intent(this, EndActivity.class);
                        intent.putExtra("timeScore", elapsedTime);
                        startActivity(intent);
                    }
                    if(prevButt != null && currButt!=null){
                        Pressed.add(prevButt);
                        Pressed.add(currButt);
                    }

                    prevButt.setClickable(false); // disable matched imgbutton
                    currButt.setClickable(false);

                }

                chosenCount=0;
                prevChosenPic=0;
            }

        }
    }

    protected void setButtonsUnclickable()
    {
        for(int i=0; i<12; i++){
            ImageButton btn = findViewById(gameBtnsID[i]);
            btn.setClickable(false);
        }
    }
    protected void setButtonsClickable()
    {
        for(int i=0; i<12; i++){
            ImageButton btn = findViewById(gameBtnsID[i]);
            btn.setClickable(true);
            if(Pressed!=null)
            {
                for(ImageButton lockbtn : Pressed)
                {
                    if(lockbtn == btn)
                    {
                        btn.setClickable(false);
                    }
                }
            }
        }
    }

    protected void imageClicked(int id){
        File dir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        ImageButton btn = findViewById(gameBtnsID[id]);
        int index = shuffled.get(id).intValue();
        File fileSource =new File(dir,"img"+index);
        Bitmap pic = BitmapFactory.decodeFile(fileSource.getAbsolutePath());
        currBit = pic;
        flipCard(btn,currBit);

    }
    protected int currentClickedIndex(int id)
    {
        if(id==R.id.gameBtn1) {return 0;}
        if(id==R.id.gameBtn2) {return 1;}
        if(id==R.id.gameBtn3) {return 2;}
        if(id==R.id.gameBtn4) {return 3;}
        if(id==R.id.gameBtn5) {return 4;}
        if(id==R.id.gameBtn6) {return 5;}
        if(id==R.id.gameBtn7) {return 6;}
        if(id==R.id.gameBtn8) {return 7;}
        if(id==R.id.gameBtn9) {return 8;}
        if(id==R.id.gameBtn10) {return 9;}
        if(id==R.id.gameBtn11) {return 10;}
        else{ return 11;}
    }

    protected void initializeGame()
    {
        Pressed = new ArrayList();
        chosenCount=0;
        currChosenPic=0;
        matchedCount=0;
        currBit = null;
        prevBit = null;
    }
    protected ArrayList<Integer> shuffleList(ArrayList<Integer> arr){
        ArrayList<Integer> shuffled = new ArrayList<>();

        for(int i=0; i<6; i++){
            shuffled.add(arr.get(i));
            shuffled.add(arr.get(i));
        }
        Collections.shuffle(shuffled);
        return shuffled;
    }
    // This is for flip animation, 2 rotation Y animation, one for back img another for front img
    protected void flipCard(ImageButton imageButton, Bitmap image){

        animator1 = ObjectAnimator.ofFloat(imageButton, "rotationY",  0,90);
        animator2 = ObjectAnimator.ofFloat(imageButton, "rotationY",  270,360);
        animator1.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                imageButton.setImageBitmap(image);
                imageButton.setScaleType(ImageView.ScaleType.CENTER_CROP);
            }
        });
        AnimatorSet bouncer = new AnimatorSet();
        bouncer.play(animator1).before(animator2);
        bouncer.setDuration(200);
        bouncer.start();
    }
    // correct matching
    protected void matchRightSound(){
        MediaPlayer mMediaPlayer;
        mMediaPlayer=MediaPlayer.create(this, R.raw.correct);
        mMediaPlayer.start();
    }
    // wrong matching
    protected void matchWrongSound(){
        MediaPlayer mMediaPlayer;
        mMediaPlayer=MediaPlayer.create(this, R.raw.wrong);
        mMediaPlayer.start();
    }
}
