package com.example.android.linkup.login.register_parameters_selection;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.linkup.R;
import com.example.android.linkup.utils.Base64Converter;

import java.util.ArrayList;
import android.graphics.Bitmap;
import android.widget.ImageView;

public class PhotoSelectionAdapter extends RecyclerView.Adapter<PhotoSelectionViewHolder> {

    public interface Listener {
        void onItemClick(String item, ImageView view);
    }

    private ArrayList<Bitmap> photos;
    private ArrayList<String> base64Photos;
    private Context context;
    private PhotoSelectionAdapter.Listener mListener;


    public PhotoSelectionAdapter (ArrayList<String> base64Photos, Context context, PhotoSelectionAdapter.Listener listener) {
        this.photos = new ArrayList<>();
        this.base64Photos = base64Photos;

        for (int i = 0 ; i < base64Photos.size() ; i++) {
            String photo = base64Photos.get(i);
            Base64Converter converter = new Base64Converter();
            Bitmap bitmap = converter.Base64ToBitmap(photo);
            bitmap = converter.resizeBitmap(bitmap,600);
            photos.add(bitmap);
        }

        this.context = context;
        this.mListener = listener;
    }

    @Override
    public PhotoSelectionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = View.inflate(context, R.layout.photo_selection_item, null);
        return new PhotoSelectionViewHolder(v);
    }

    @Override
    public void onBindViewHolder(PhotoSelectionViewHolder holder, int position) {
        Bitmap photo = photos.get(position);
        String base64Photo = base64Photos.get(position);
        holder.bind(base64Photo, photo, mListener);
        holder.setIsRecyclable(false);
    }

    @Override
    public int getItemCount() {
        return photos.size();
    }
}
