package edu.neu.madcourse.tourmemo.adapter;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import edu.neu.madcourse.tourmemo.MainActivity;
import edu.neu.madcourse.tourmemo.R;
import edu.neu.madcourse.tourmemo.model.Post;

public class UserPostAdapter extends RecyclerView.Adapter<UserPostAdapter.ViewHolder>{

    private Context mContext;
//    private List<String> mPosts;
//    private List<String> mPostCount;
    private List<Post> mUserPost;
    private FirebaseUser firebaseUser;

    public UserPostAdapter(Context mContext, List<Post> mUserPost) {
        this.mContext = mContext;
        this.mUserPost = mUserPost;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.user_post_item , parent , false);
        return new UserPostAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        Post userPost = mUserPost.get(position);

        holder.zipcode.setText(userPost.getZipcode());
        holder.name.setText(userPost.getSpotName());
        holder.description.setText(userPost.getDescription());
        Glide.with(mContext).load(userPost.getImageUrl()).placeholder(R.mipmap.ic_launcher).into(holder.image);

       holder.itemView.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Intent intent = new Intent(mContext, MainActivity.class);
               mContext.startActivity(intent);
           }
       });


        //holder.btnFollow.setVisibility(View.VISIBLE);

//        holder.post.setText("# " + mPosts.get(position));
//        holder.numPosts.setText(mPostCount.get(position) + " posts");

    }

    @Override
    public int getItemCount() {
        return mUserPost.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView zipcode;
        public TextView name;
        public TextView description;
        public ImageView image;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            zipcode = itemView.findViewById(R.id.txtZipcode);
            name = itemView.findViewById(R.id.txtName);
            description = itemView.findViewById(R.id.txtDes);
            image = itemView.findViewById(R.id.img);
        }
    }

//    public void filter (List<String> filterPosts , List<String> filterPostsCount) {
//        this.mPosts = filterPosts;
//        this.mPostCount = filterPostsCount;
//
//        notifyDataSetChanged();
//    }

}
