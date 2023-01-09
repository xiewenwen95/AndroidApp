package iss.team6.thememorygame;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import static iss.team6.thememorygame.ImageLoader.saveImageToFile;

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
import android.widget.AdapterView;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {
    private Button fetchBtn;
    private EditText fetchUrl;
    private GridView gridView;
    private ImageLoader imageLoader;
    private ProgressBar progressBar;
    private Button restartBtn;
    private DialogFragment dialogFragment;

    private Map<Integer,String> saveImgMap = new HashMap();

    public Map<Integer, String> getSaveImgMap() {
        return saveImgMap;
    }

    //the inputUrl will be passed to the fetch method,which will later use Picasso lib to download the first 20 images
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fetchUrl=(EditText)findViewById(R.id.fetchUrl);
        fetchBtn=(Button) findViewById(R.id.fetchBtn);
        progressBar=(ProgressBar)findViewById(R.id.progressBar);
        restartBtn=(Button) findViewById(R.id.restartBtn);
        gridView=(GridView)findViewById(R.id.girdView);//initialize all ui elements
        imageLoader = new ImageLoader(MainActivity.this);
        saveImgMap = new HashMap<>();
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
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ImageAdapter adapter = (ImageAdapter) gridView.getAdapter();
                String imgurlpath= (String) adapter.getItem(position);

                if (!saveImgMap.containsKey(position)){

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            String filename = "image" + UUID.randomUUID().toString();
                            Bitmap bitmap =imageLoader.downloadImage(imgurlpath);
                            saveImageToFile(bitmap, filename);
                            //download
                            saveImgMap.put(position,filename);
                            if (saveImgMap.size()>=6){
                                startGame();
                            }
                        }
                    }).start();

                }else{
                    Toast.makeText(MainActivity.this,"image downloaded",Toast.LENGTH_SHORT).show();
                }
            }
        });}

    @Override
    protected void onRestart() {
        super.onRestart();
        finish();
        //startActivity(getIntent());
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
    private Elements imgElement;
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

            for(int i=0; i < imgElement.size();i++) {

                String src = imgElement.get(i).attr("src");
                if (src.endsWith("jpeg") || src.endsWith("jpg")) {
                    imageUrls.add(src);

                    progressBar.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            progressBar.setProgress(progressBar.getProgress() + 1);
                        }
                    }, i * new Random().nextInt((100 - 50) + 1) + 50);
                    if (imageUrls.size() == 20) {
                        break;
                    }
                }
            }
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
