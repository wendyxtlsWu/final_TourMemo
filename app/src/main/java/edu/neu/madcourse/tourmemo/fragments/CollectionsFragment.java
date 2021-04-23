package edu.neu.madcourse.tourmemo.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import edu.neu.madcourse.tourmemo.R;
import edu.neu.madcourse.tourmemo.adapter.PhotoAdapter;
import edu.neu.madcourse.tourmemo.model.User;
import edu.neu.madcourse.tourmemo.model.Post;


public class CollectionsFragment extends Fragment {
    private RecyclerView recyclerViewPosts;
    private PhotoAdapter photoAdapterPosts;
    private List<Post> myPhotoList;

    private RecyclerView recyclerViewLikes;
    private PhotoAdapter postAdapterLikes;
    private List<Post> myLikePosts;

    private ImageView myPosts;
    private ImageView likedPictures;

    private FirebaseUser fUser;

    String profileId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_collections, container, false);

        fUser = FirebaseAuth.getInstance().getCurrentUser();

        recyclerViewPosts = view.findViewById(R.id.recycler_view_pictures);
        recyclerViewPosts.setHasFixedSize(true);
        recyclerViewPosts.setLayoutManager(new GridLayoutManager(getContext(), 3));
        myPhotoList = new ArrayList<>();
        photoAdapterPosts = new PhotoAdapter(getContext(), myPhotoList);
        recyclerViewPosts.setAdapter(photoAdapterPosts);

        recyclerViewLikes = view.findViewById(R.id.recycler_view_liked);
        recyclerViewLikes.setHasFixedSize(true);
        recyclerViewLikes.setLayoutManager(new GridLayoutManager(getContext(), 3));
        myLikePosts = new ArrayList<>();
        postAdapterLikes = new PhotoAdapter(getContext(), myLikePosts);
        recyclerViewLikes.setAdapter(postAdapterLikes);


        myPosts = view.findViewById(R.id.my_pictures);
        likedPictures = view.findViewById(R.id.saved_pictures);


        String data = getContext().getSharedPreferences("PROFILE", Context.MODE_PRIVATE).getString("profileId", "none");

        if (data.equals("none")) {
            profileId = fUser.getUid();
        } else {
            profileId = data;
            getContext().getSharedPreferences("PROFILE", Context.MODE_PRIVATE).edit().clear().apply();
        }


        getMyPosts();
        getLikedPosts();


        recyclerViewPosts.setVisibility(View.VISIBLE);
        recyclerViewLikes.setVisibility(View.GONE);

        myPosts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerViewPosts.setVisibility(View.VISIBLE);
                recyclerViewLikes.setVisibility(View.GONE);
            }
        });

        likedPictures.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerViewPosts.setVisibility(View.GONE);
                recyclerViewLikes.setVisibility(View.VISIBLE);
            }
        });


        return view;
    }


    private void getMyPosts() {

        FirebaseDatabase.getInstance().getReference().child("Posts").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                myPhotoList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Post post = snapshot.getValue(Post.class);

                    if (post.getPublisher().equals(profileId)) {
                        myPhotoList.add(post);
                    }
                }

                Collections.reverse(myPhotoList);
                photoAdapterPosts.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void getLikedPosts() {

        final List<String> savedIds = new ArrayList<>();

        FirebaseDatabase.getInstance().getReference().child("Likes").child(fUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    savedIds.add(snapshot.getKey());
                }

                FirebaseDatabase.getInstance().getReference().child("Posts").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot1) {
                        myLikePosts.clear();

                        for (DataSnapshot snapshot1 : dataSnapshot1.getChildren()) {
                            Post post = snapshot1.getValue(Post.class);

                            for (String id : savedIds) {
                                if (post.getPostId().equals(id)) {
                                    myLikePosts.add(post);
                                }
                            }
                        }

                        postAdapterLikes.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

}