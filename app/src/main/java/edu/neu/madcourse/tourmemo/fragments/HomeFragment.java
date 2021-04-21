package edu.neu.madcourse.tourmemo.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import edu.neu.madcourse.tourmemo.MainActivity;
import edu.neu.madcourse.tourmemo.PostActivity;
import edu.neu.madcourse.tourmemo.R;

public class HomeFragment extends Fragment {

    private ImageButton addPost;
    private RecyclerView postsRecycleView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_home, container, false);

        addPost = (ImageButton) view.findViewById(R.id.add_new_post);
        postsRecycleView = view.findViewById(R.id.recycler_view_posts);

        addPost.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                startActivity(new Intent(v.getContext(), PostActivity.class));
            }
        });

        return view;

    }
}