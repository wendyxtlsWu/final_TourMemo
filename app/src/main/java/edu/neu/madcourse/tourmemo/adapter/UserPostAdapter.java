package edu.neu.madcourse.tourmemo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import edu.neu.madcourse.tourmemo.R;

public class UserPostAdapter extends RecyclerView.Adapter<UserPostAdapter.ViewHolder>{

    private Context mContext;
    private List<String> mPosts;
    private List<String> mPostCount;

    public UserPostAdapter(Context mContext, List<String> mPosts, List<String> mPostCount) {
        this.mContext = mContext;
        this.mPosts = mPosts;
        this.mPostCount = mPostCount;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.u_post_item , parent , false);
        return new UserPostAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.post.setText("# " + mPosts.get(position));
        holder.numPosts.setText(mPostCount.get(position) + " posts");

    }

    @Override
    public int getItemCount() {
        return mPosts.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView post;
        public TextView numPosts;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            post = itemView.findViewById(R.id.user_post);
            numPosts = itemView.findViewById(R.id.num_posts);
        }
    }

    public void filter (List<String> filterPosts , List<String> filterPostsCount) {
        this.mPosts = filterPosts;
        this.mPostCount = filterPostsCount;

        notifyDataSetChanged();
    }

}
