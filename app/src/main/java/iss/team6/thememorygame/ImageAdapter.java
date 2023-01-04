package iss.team6.thememorygame;


import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


public class ImageAdapter extends RecyclerView.Adapter<ViewHolder> {

//    public ImageView[] imageViews = new ImageView[20];//create a list of ImageViews to be used for loading images in Picasso
//
//    private List<Drawable> images = new ArrayList<>();
    private List<String> imageUrls;
    private MainActivity mainActivity;

    public ImageAdapter(List<String> imageUrls, MainActivity mainActivity) {
        this.imageUrls = imageUrls;
        this.mainActivity = mainActivity;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_layout, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //Picasso.get().load().resize(90,90).into(holder.imageView);
        //holder.bind(imageViews[position]);
        String imageUrl = imageUrls.get(position);
        mainActivity.loadImages(imageUrl, holder.imageView);
    }

    @Override
    public int getItemCount() {
        return imageUrls.size();
    }
}
class ViewHolder extends RecyclerView.ViewHolder {
   ImageView imageView;

    ViewHolder(View itemView) {
        super(itemView);
        imageView = itemView.findViewById(R.id.iv_picasso);

    }
    public void bind(ImageView imageView) {
        this.imageView = imageView;
    }
}

