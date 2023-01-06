package iss.team6.thememorygame;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.Toast;

//import com.squareup.picasso.Picasso;
//import com.squareup.picasso.Target;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private Button fetchBtn;
    private EditText fetchUrl;
    private GridView gridView;
    private ImageLoader imageLoader;
    private ProgressBar progressBar;
    private Button restartBtn;
    private DialogFragment dialogFragment;
    //the inputUrl will be passed to the fetch method,which will later use Picasso lib to download the first 20 images
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fetchUrl=(EditText)findViewById(R.id.fetchUrl);
        fetchBtn=(Button) findViewById(R.id.fetchBtn);
        gridView=(GridView)findViewById(R.id.girdView);
        imageLoader = new ImageLoader(MainActivity.this);
        progressBar=(ProgressBar)findViewById(R.id.progressBar);
        restartBtn=(Button) findViewById(R.id.restartBtn);
        fetchBtn.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                fetch();
            }
         });
        restartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                startActivity(getIntent());
            }
        });
    }
    public void fetch() {
        String imageUrl=fetchUrl.getText().toString();
        new ImageUrlParser((ProgressBar)findViewById(R.id.progressBar),20).execute(imageUrl);
    }
 public void startGame(){
    dialogFragment= new MyDialogFragment();
    dialogFragment.show(getSupportFragmentManager(),"MyDialogueFragment");

 }


private class ImageUrlParser extends AsyncTask<String,Void,ArrayList<String>> {
    private ProgressBar progressBar;

    private int toaalImgs;

    public ImageUrlParser(ProgressBar progressBar, int toaalImgs) {
        this.progressBar = (ProgressBar)findViewById(R.id.progressBar);
        this.toaalImgs = 20;
    }

    @Override
    protected ArrayList<String> doInBackground(String... strings) {
        String imgUrl=strings[0];
        ArrayList<String>imageUrls=new ArrayList<>(20);
        try{
            Document doc=Jsoup.connect(imgUrl).get();
            Elements imgElement=doc.select("img");

            for(int i=0;i<20&& i < imgElement.size();i++){
                imageUrls.add(imgElement.get(i).attr("src"));
            }
            int progress=1;

            progressBar.post(new Runnable() {
                @Override
                public void run() {
                    progressBar.setProgress(progress);
                }
            });
        }
        catch(MalformedURLException malformedURLException){
            malformedURLException.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return imageUrls;
    }
    @Override
    protected void onPostExecute(ArrayList<String> imageUrls) {
        super.onPostExecute(imageUrls);
        gridView.setAdapter(new ImageAdapter( imageUrls,MainActivity.this));
    }



}

}
