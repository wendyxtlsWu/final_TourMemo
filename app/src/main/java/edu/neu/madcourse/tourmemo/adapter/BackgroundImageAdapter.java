package edu.neu.madcourse.tourmemo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import edu.neu.madcourse.tourmemo.R;
import edu.neu.madcourse.tourmemo.model.ImageCard;

public class BackgroundImageAdapter extends RecyclerView.Adapter<MainBackgroundAdapter.ViewHolder> {

    Context mContext;
    List<ImageCard> mData;


    public BackgroundImageAdapter(Context mContext, List<ImageCard> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_item, parent, false);
        return new ViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Glide.with(mContext).load(mData.get(position).getImg())
                .into(holder.backgroundImage);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public ImageView backgroundImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            backgroundImage = itemView.findViewById(R.id.imageView);
        }

    }
}
