package iss.team6.thememorygame;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import android.widget.GridView;
import android.widget.ImageView;
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

    //private ImageView[] imageViews = new ImageView[20];
    //private List<String>imgUrls=new ArrayList<>();
    //private int count = 0;

    private SeekBar seekBar;

    //the inputUrl will be passed to the fetch method,which will later use Picasso lib to download the first 20 images
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fetchUrl=(EditText)findViewById(R.id.fetchUrl);
        fetchBtn=(Button) findViewById(R.id.fetchBtn);
        gridView=(GridView)findViewById(R.id.girdView);
        fetchBtn.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                fetch();
            }
         });
        imageLoader = new ImageLoader();
    }

    private void fetch() {
        String imageUrl=fetchUrl.getText().toString();
        new ImageUrlParser().execute(imageUrl);
    }
private class ImageUrlParser extends AsyncTask<String,Void,ArrayList<String>> {

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
        } catch (IOException e) {
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
