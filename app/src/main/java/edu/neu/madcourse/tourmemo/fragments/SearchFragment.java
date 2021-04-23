package edu.neu.madcourse.tourmemo.fragments;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.hendraanggrian.appcompat.widget.SocialAutoCompleteTextView;
import java.util.ArrayList;
import java.util.List;
import edu.neu.madcourse.tourmemo.R;
import edu.neu.madcourse.tourmemo.adapter.UserAdapter;
import edu.neu.madcourse.tourmemo.adapter.UserPostAdapter;
import edu.neu.madcourse.tourmemo.model.Post;
import edu.neu.madcourse.tourmemo.model.User;


public class SearchFragment extends Fragment {

    private RecyclerView recyclerViewUser;
    private List<User> mUsers;
    private UserAdapter userAdapter;

    private RecyclerView recyclerViewPosts;
    private List<Post> mUserPosts;
    private UserPostAdapter uPostAdapter;

    private RecyclerView recyclerViewPosts2;
    private List<Post> mUserPosts2;
    private UserPostAdapter uPostAdapter2;

    private SocialAutoCompleteTextView search_bar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        recyclerViewUser = view.findViewById(R.id.recycler_view_users);
        recyclerViewUser.setHasFixedSize(true);
        recyclerViewUser.setLayoutManager(new LinearLayoutManager(getContext()));

        mUsers = new ArrayList<>();
        userAdapter = new UserAdapter(getContext() , mUsers , true);
        recyclerViewUser.setAdapter(userAdapter);

        recyclerViewPosts = view.findViewById(R.id.recycler_view_posts);
        recyclerViewPosts.setHasFixedSize(true);
        recyclerViewPosts.setLayoutManager(new LinearLayoutManager(getContext()));

        mUserPosts = new ArrayList<>();
        uPostAdapter = new UserPostAdapter(getContext() , mUserPosts);
        recyclerViewPosts.setAdapter(uPostAdapter);

        recyclerViewPosts2 = view.findViewById(R.id.recycler_view_posts2);
        recyclerViewPosts2.setHasFixedSize(true);
        recyclerViewPosts2.setLayoutManager(new LinearLayoutManager(getContext()));

        mUserPosts2 = new ArrayList<>();
        uPostAdapter2 = new UserPostAdapter(getContext() , mUserPosts2);
        recyclerViewPosts2.setAdapter(uPostAdapter2);


        search_bar = view.findViewById(R.id.search_bar);


        search_bar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {


            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                searchPostByName(s.toString());
                searchUser(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
                searchPostByZipCode(s.toString());
            }
        });

        return view;
    }


    private void searchUser (String usrName) {
        Query query = FirebaseDatabase.getInstance().getReference().child("Users")
                .orderByChild("username").startAt(usrName).endAt(usrName + "\uf8ff");

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mUsers.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    User user = snapshot.getValue(User.class);
                    mUsers.add(user);

                }
                userAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    private void searchPostByZipCode (String zipcode) {
        Query query = FirebaseDatabase.getInstance().getReference().child("Posts")
                .orderByChild("zipcode").startAt(zipcode).endAt(zipcode + "\uf8ff");

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mUserPosts.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Post uerPost = snapshot.getValue(Post.class);
                    mUserPosts.add(uerPost);
                }
                uPostAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void searchPostByName (String name) {
        Query query = FirebaseDatabase.getInstance().getReference().child("Posts")
                .orderByChild("spotName").startAt(name).endAt(name + "\uf8ff");

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mUserPosts2.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Post uerPost = snapshot.getValue(Post.class);
                    mUserPosts2.add(uerPost);
                }
                uPostAdapter2.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}