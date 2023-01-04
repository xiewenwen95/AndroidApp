package iss.team6.thememorygame;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private Button fetchBtn;
    private EditText fetchUrl;

    private RecyclerView recyclerView;


    private ImageView[] imageViews = new ImageView[20];
    private List<String>imgUrls=new ArrayList<>();
    private int count = 0;

    private SeekBar seekBar;

    //the inputUrl will be passed to the fetch method,which will later use Picasso lib to download the first 20 images
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

       fetchUrl=(EditText)findViewById(R.id.fetchUrl);

       fetchBtn=(Button) findViewById(R.id.fetchBtn);

       recyclerView= findViewById(R.id.recyclerView);

       seekBar=(SeekBar)findViewById(R.id.seekBar);

        //  set the grid layout manager on the RecyclerView
        //GridLayoutManager layoutManager = new GridLayoutManager(this, 4, RecyclerView.VERTICAL, false);

        recyclerView.setLayoutManager(new GridLayoutManager(this,4));


        recyclerView.setAdapter(new ImageAdapter(imgUrls,this));

        recyclerView.setHasFixedSize(true);
    }

    public void fetch(View v) {

        fetchBtn.setOnClickListener(view -> {
            for(int i=0;i<20;i++){
            String url = fetchUrl.getText().toString()+i;
                imgUrls.add(url);
            try {
                URL imageUrl = new URL(url);
                loadImages(url,imageViews[count]);
            } catch (MalformedURLException e) {
                Toast.makeText(MainActivity.this, "Invalid URL", Toast.LENGTH_SHORT).show();
            }
            }
        });
    }
    //use Picasso to download imgs,and attach the seekbar,which shows the progress of downloading processing
    public  void loadImages(String url,ImageView imageView) {
        for (int i = 0; i < 20; i++) {
            Picasso.get()
            .load(url).resize(90,90)//using  com.squareup.picasso.Callback interface to set two conditions as well as a seekbar to track downloading progress ,load the image into the list of imageViews
            .into(new Target() {
                @Override
                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                    imageViews[count].setImageBitmap(bitmap);
                }
                @Override
                public void onBitmapFailed(Exception e, Drawable errorDrawable) {
                    Toast.makeText(MainActivity.this, "Unable to save images", Toast.LENGTH_SHORT).show();
                }
                @Override
                public void onPrepareLoad(Drawable placeHolderDrawable) {

                }
            });
            Picasso.get().load(url).into(imageView);
        }
    }
}
