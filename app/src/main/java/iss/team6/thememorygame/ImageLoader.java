package iss.team6.thememorygame;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Environment;
import android.widget.ImageView;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

public class ImageLoader extends AsyncTask<Object, Void, Bitmap> {
    ImageView imageView;
    String imageUrl;
    private Context context;
    static String filename;
    static MainActivity mainActivity;

    public ImageLoader(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    public ImageLoader() {

    }

    @Override
    protected Bitmap doInBackground(Object... objects) {
        imageView = (ImageView) objects[0];
        imageUrl = (String) objects[1];
        return downloadImage();
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        super.onPostExecute(bitmap);
        imageView.setImageBitmap(bitmap);
    }

    private Bitmap downloadImage() {
        Bitmap bitmap = null;
        InputStream stream;
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inSampleSize = 1;

        try {
            stream = getHttpConnection(imageUrl);
            bitmap = BitmapFactory.decodeStream(stream, null, bmOptions);
            stream.close();
            int width= imageView.getMeasuredWidth();
            int height= imageView.getMeasuredHeight();
            bitmap=Bitmap.createScaledBitmap(bitmap,width,height,false);
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return bitmap;
    }

    private InputStream getHttpConnection(String urlString) throws IOException {
        InputStream stream = null;
        URL url = new URL(urlString);
        URLConnection connection = url.openConnection();

        try {
            HttpURLConnection httpConnection = (HttpURLConnection) connection;
            httpConnection.setRequestMethod("GET");
            httpConnection.connect();

            if (httpConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                stream = httpConnection.getInputStream();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return stream;
    }
    public static void saveImageToFile(Bitmap bitmap,String filename){
        Bitmap.CompressFormat compressFormat= Bitmap.CompressFormat.JPEG;
        int quality=100;
        OutputStream fout=null;
        int savingLimitation=0;
        do{
            try{
                fout=new FileOutputStream(Environment.getExternalStorageDirectory().getPath()+"/"+filename+".jpg");
            }
            catch (FileNotFoundException exception){
                exception.printStackTrace();
            }
            bitmap.compress(compressFormat,quality,fout);
            try{
                fout.flush();
                fout.close();
            }
            catch (IOException ioException){
                ioException.printStackTrace();
            }
            savingLimitation++;}
        while(savingLimitation<6);
        mainActivity.startGame();
    }
    public static Bitmap getBitmapFromFile(){
        try{
            return BitmapFactory.decodeStream(new FileInputStream(Environment.getExternalStorageDirectory().getPath()+"/"+filename+".jpg"));
        }
        catch(FileNotFoundException fileNotFoundException){
            fileNotFoundException.printStackTrace();
        }
        return null;
    }
}
