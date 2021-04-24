package edu.neu.madcourse.tourmemo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.List;

import edu.neu.madcourse.tourmemo.R;
import edu.neu.madcourse.tourmemo.fragments.PostDetailFragment;
import edu.neu.madcourse.tourmemo.model.Post;
import edu.neu.madcourse.tourmemo.model.User;

public class UserPostAdapter extends RecyclerView.Adapter<UserPostAdapter.ViewHolder>{

    private Context mContext;
    private List<Post> mUserPost;
    private FirebaseUser firebaseUser;
    String usrName = "";

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

        String userID = userPost.getPublisher();


        FirebaseDatabase.getInstance().getReference().child("Users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    User cUser = snapshot1.getValue(User.class);

                    if(cUser.getId().equals(userID)) {
                        usrName = cUser.getUsername();


                    }
                }
                holder.userNamefill.setText(usrName);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        holder.zipcode.setText("zip code: "+ userPost.getZipcode());
        holder.spotNamefill.setText(userPost.getSpotName());
        Glide.with(mContext).load(userPost.getImageUrl()).placeholder(R.mipmap.ic_launcher).into(holder.image);

        isLiked(userPost.getPostId(), holder.likeButton);

        holder.likeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.likeButton.getTag().equals("like")) {
                    FirebaseDatabase.getInstance().getReference().child("Likes")
                            .child(firebaseUser.getUid()).child(userPost.getPostId()).setValue(true);

                    addNotification(userPost.getPostId(), userPost.getPublisher());
                } else {
                    FirebaseDatabase.getInstance().getReference().child("Likes")
                            .child(firebaseUser.getUid()).child(userPost.getPostId()).removeValue();
                }
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mContext.getSharedPreferences("PREFS", Context.MODE_PRIVATE).edit().putString("postId", userPost.getPostId()).apply();

                ((FragmentActivity)mContext).getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, new PostDetailFragment()).commit();
            }
        });

    }

    private void isLiked(String postId, final ImageView imageView) {
        FirebaseDatabase.getInstance().getReference().child("Likes").child(firebaseUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(postId).exists()) {
                    imageView.setImageResource(R.drawable.ic_liked);
                    imageView.setTag("liked");
                } else {
                    imageView.setImageResource(R.drawable.ic_like);
                    imageView.setTag("like");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    @Override
    public int getItemCount() {
        return mUserPost.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView zipcode;
        public TextView username;
        public TextView name;
        public TextView userNamefill;
        public TextView spotNamefill;


        public ImageView image;
        public ImageView likeButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            zipcode = itemView.findViewById(R.id.txtZipcode);
            username = itemView.findViewById(R.id.userName);
            name = itemView.findViewById(R.id.txtName);
            image = itemView.findViewById(R.id.img);
            likeButton = itemView.findViewById(R.id.like);
            userNamefill = itemView.findViewById(R.id.userNamefill);
            spotNamefill = itemView.findViewById(R.id.spotNamefill);
        }
    }

    private void addNotification(String postId, String publisherId) {
        HashMap<String, Object> map = new HashMap<>();

        map.put("userid", publisherId);
        map.put("text", "liked your post.");
        map.put("postid", postId);
        map.put("isPost", true);

        FirebaseDatabase.getInstance().getReference().child("Notifications").child(firebaseUser.getUid()).push().setValue(map);
    }


}
