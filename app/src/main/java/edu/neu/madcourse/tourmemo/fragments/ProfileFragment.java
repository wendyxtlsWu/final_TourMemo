package edu.neu.madcourse.tourmemo.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import de.hdodenhof.circleimageview.CircleImageView;
import edu.neu.madcourse.tourmemo.EditProfileActivity;
import edu.neu.madcourse.tourmemo.OptionsActivity;
import edu.neu.madcourse.tourmemo.R;
import edu.neu.madcourse.tourmemo.model.Post;
import edu.neu.madcourse.tourmemo.model.User;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashSet;
import java.util.Set;

public class ProfileFragment extends Fragment {

    private CircleImageView imageProfile;
    private ImageView options_button;
    private TextView posts;
    private TextView followers;
    private TextView following;
    private TextView bio;

    Button editProfile_button;

    private TextView display_email;
    private TextView display_username;
    private TextView display_points;
    private TextView display_cities;

    private FirebaseUser fireBaseUser;

    String profileId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        // get the instance of current user
        fireBaseUser = FirebaseAuth.getInstance().getCurrentUser();

        String data = getContext().getSharedPreferences("PROFILE", Context.MODE_PRIVATE).getString("profileId", "none");

        if (data.equals("none")) {
            profileId = fireBaseUser.getUid();
        } else {
            profileId = data;
            getContext().getSharedPreferences("PROFILE", Context.MODE_PRIVATE).edit().clear().apply();
        }

        options_button = view.findViewById(R.id.options);
        // first linear layout
        imageProfile = view.findViewById(R.id.image_profile);
        posts = view.findViewById(R.id.posts);
        followers = view.findViewById(R.id.followers);
        following = view.findViewById(R.id.following);
        bio = view.findViewById(R.id.bio);

        editProfile_button = view.findViewById(R.id.edit_profile);

        display_email = view.findViewById(R.id.display_email);
        display_username = view.findViewById(R.id.display_username);
        display_points = view.findViewById(R.id.display_points);
        display_cities = view.findViewById(R.id.display_cities);

        getUserInfo();
        getFollowersAndFollowingCount();
        getCityCount();
        getPostCount();

        // navigate to edit profile
        editProfile_button.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), EditProfileActivity.class);
            startActivity(intent);
        });

        // navigate to options
        options_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), OptionsActivity.class));
            }
        });

        return view;
    }


    // get basic information of the user
    private void getUserInfo() {
        FirebaseDatabase.getInstance().getReference().child("Users").child(profileId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);

                assert user != null;
                Picasso.get().load(user.getImageurl()).into(imageProfile);
                display_email.setText(user.getEmail());
                display_username.setText(user.getUsername());
                if (user.getPoints() == null) display_points.setText(String.valueOf(0));
                else display_points.setText(String.valueOf(user.getPoints()));

                bio.setText(user.getBio());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // intentionally empty
            }
        });

    }

    // get the number of followers and followings
    private void getFollowersAndFollowingCount() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Follow").child(profileId);

        ref.child("followers").addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                followers.setText("" + dataSnapshot.getChildrenCount());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        ref.child("following").addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                following.setText("" + dataSnapshot.getChildrenCount());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    // obtain the number of different zip codes
    private void getCityCount() {
        FirebaseDatabase.getInstance().getReference().child("Posts").addValueEventListener(new ValueEventListener() {
            final Set<String> zipCodes = new HashSet<>();
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Post post = snapshot.getValue(Post.class);
                    assert post != null;
                    if (post.getPublisher().equals(profileId))
                        zipCodes.add(post.getZipcode());
                }
                display_cities.setText(String.valueOf(zipCodes.size()));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    // obtain the number of posts
    private void getPostCount() {
        FirebaseDatabase.getInstance().getReference().child("Posts").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int count = 0;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Post post = snapshot.getValue(Post.class);

                    assert post != null;
                    if (post.getPublisher().equals(profileId)) count++;
                }
                posts.setText(String.valueOf(count));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}