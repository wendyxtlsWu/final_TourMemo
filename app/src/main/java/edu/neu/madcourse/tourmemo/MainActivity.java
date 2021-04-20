package edu.neu.madcourse.tourmemo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;


import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import edu.neu.madcourse.tourmemo.adapter.BackgroundImageAdapter;
import edu.neu.madcourse.tourmemo.fragments.CollectionsFragment;
import edu.neu.madcourse.tourmemo.fragments.HomeFragment;
import edu.neu.madcourse.tourmemo.fragments.NotificationFragment;
import edu.neu.madcourse.tourmemo.fragments.ProfileFragment;
import edu.neu.madcourse.tourmemo.fragments.SearchFragment;
import edu.neu.madcourse.tourmemo.model.ImageCard;

public class MainActivity extends AppCompatActivity {

    /* I cannot understand this part so I comment all of them. In my opinion, the main activity would
    not contain collections directly. I prefer to put collection into the fragment in main activity.
    * */

//    private RecyclerView recyclerView;
//    private BackgroundImageAdapter adapter;
//    private LinearLayoutManager manager;
//    private List<ImageCard> appList;
//
//    final int time = 1000;
//
//    int[] covers = new int[]{
//            R.drawable.d1,
//            R.drawable.d3,
//            R.drawable.d4,
//            R.drawable.g1,
//            R.drawable.g2,
//            R.drawable.g3,
//            R.drawable.g4,
//            R.drawable.g5,
//            R.drawable.g6,
//            R.drawable.j1,
//            R.drawable.l1,
//            R.drawable.l2,
//            R.drawable.l3,
//            R.drawable.l4,
//            R.drawable.m1,
//            R.drawable.m2,
//            R.drawable.m3,
//            R.drawable.m4,
//            R.drawable.o1,
//            R.drawable.o2,
//            R.drawable.o3,
//            R.drawable.o4,
//            R.drawable.s1,
//            R.drawable.l2,
//            R.drawable.l3,
//            R.drawable.l4,
//
//    };
//
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//
//        recyclerView = findViewById(R.id.recyclerView);
//
//        appList = new ArrayList<>();
//        adapter = new BackgroundImageAdapter(this, appList);
//        manager = new LinearLayoutManager(this);
//        recyclerView .setLayoutManager(manager);
//        recyclerView .setAdapter(adapter);
//
//
//        final Timer timer = new Timer();
//        timer.schedule(new TimerTask() {
//
//            @Override
//            public void run() {
//
//                if (manager.findLastCompletelyVisibleItemPosition() < (adapter.getItemCount() - 1)) {
//                    manager.smoothScrollToPosition(recyclerView, new RecyclerView.State(), manager.findLastCompletelyVisibleItemPosition() + 1);
//                } else if (manager.findLastCompletelyVisibleItemPosition() == (adapter.getItemCount() - 1)) {
//                    manager.smoothScrollToPosition(recyclerView, new RecyclerView.State(), 0);
//                }
//            }
//        }, 0, time);
//
//        IntializeDataIntoRecyclerView();
//
//
//
//
//    }
//
//
//    private void IntializeDataIntoRecyclerView() {
//
//        for (int c : covers) {
//            ImageCard a = new ImageCard(c);
//            appList.add(a);
//        }
//
//        adapter.notifyDataSetChanged();
//
//    }
//
//
//    @Override
//    protected void onStart() {
//        super.onStart();
//
//        if (FirebaseAuth.getInstance().getCurrentUser() != null){
//            startActivity(new Intent(MainActivity.this , MainActivity.class));
//            finish();
//        }
//    }

    /* Below is my code -Zihan */
    private BottomNavigationView bottomNavigationView;
    private Fragment selectorFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottom_navigation);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                switch (menuItem.getItemId()){
                    case R.id.nav_home :
                        selectorFragment = new HomeFragment();
                        break;

                    case R.id.nav_search :
                        selectorFragment = new SearchFragment();
                        break;

                    case R.id.nav_collection :
                        selectorFragment = new CollectionsFragment();
                        break;

                    case R.id.nav_notification :
                        selectorFragment = new NotificationFragment();
                        break;

                    case R.id.nav_profile :
                        selectorFragment = new ProfileFragment();
                        break;
                }

                if (selectorFragment != null){
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container , selectorFragment).commit();
                }

                return true;

            }
        });

        Bundle intent = getIntent().getExtras();
        if (intent != null) {
            String profileId = intent.getString("publisherId");

            getSharedPreferences("PROFILE", MODE_PRIVATE).edit().putString("profileId", profileId).apply();

            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ProfileFragment()).commit();
            bottomNavigationView.setSelectedItemId(R.id.nav_profile);
        } else {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container , new HomeFragment()).commit();
        }
    }
}