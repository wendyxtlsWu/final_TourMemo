package edu.neu.madcourse.tourmemo.fragments;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.hendraanggrian.appcompat.widget.SocialAutoCompleteTextView;
import java.util.ArrayList;
import java.util.List;
import edu.neu.madcourse.tourmemo.R;
import edu.neu.madcourse.tourmemo.adapter.UserAdapter;
import edu.neu.madcourse.tourmemo.adapter.UserPostAdapter;
import edu.neu.madcourse.tourmemo.model.User;


public class SearchFragment extends Fragment {

    private RecyclerView recyclerViewUser;
    private List<User> mUsers;
    private UserAdapter userAdapter;

    private RecyclerView recyclerViewPosts;
    private List<String> mPosts;
    private List<String> mPostsCount;
    private UserPostAdapter uPostAdapter;

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

        mPosts = new ArrayList<>();
        mPostsCount = new ArrayList<>();
        uPostAdapter = new UserPostAdapter(getContext() , mPosts ,  mPostsCount);
        recyclerViewPosts.setAdapter(uPostAdapter);


        search_bar = view.findViewById(R.id.search_bar);

        readUsers();
        readPosts();

        search_bar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                searchUser(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
                searchPost(s.toString());
            }
        });

        return view;
    }


    private void readUsers() {

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Users");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (TextUtils.isEmpty(search_bar.getText().toString())){
                    mUsers.clear();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                        User user = snapshot.getValue(User.class);
                        mUsers.add(user);
                    }

                    userAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

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

    private void readPosts() {

        FirebaseDatabase.getInstance().getReference().child("HashPosts").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mPosts.clear();
                mPostsCount.clear();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    mPosts.add(snapshot.getKey());
                    mPostsCount.add(snapshot.getChildrenCount() + "");
                }

                uPostAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void searchPost (String text) {
        List<String> mSearchPosts = new ArrayList<>();
        List<String> mSearchPostsCount = new ArrayList<>();

        for (String s : mPosts) {
            if (s.toLowerCase().contains(text.toLowerCase())){
                mSearchPosts.add(s);
                mSearchPostsCount.add(mPostsCount.get(mPosts.indexOf(s)));
            }
        }

        uPostAdapter.filter(mSearchPosts , mSearchPostsCount);
    }
}