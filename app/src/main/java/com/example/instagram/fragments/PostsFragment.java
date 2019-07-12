package com.example.instagram.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.instagram.PostAdapter;
import com.example.instagram.R;
import com.example.instagram.model.Post;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

public class PostsFragment extends Fragment {

    RecyclerView rvPosts;
    PostAdapter postAdapter;
    List<Post> feedPosts;
    SwipeRefreshLayout swipeContainer;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.posts_fragment,container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        rvPosts = view.findViewById(R.id.rvPosts);
        feedPosts = new ArrayList<>();
        postAdapter = new PostAdapter(getContext(), feedPosts);
        rvPosts.setAdapter(postAdapter);
        rvPosts.setLayoutManager(new LinearLayoutManager(getContext()));

        queryPosts();

        swipeContainer = view.findViewById(R.id.swipeContainer);
        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.
                fetchTimelineAsync(0);
            }
        });
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

    }

    protected void fetchTimelineAsync(int page) {
        postAdapter.clear();

        ParseQuery<Post> postQuery = new ParseQuery<>(Post.class);
        postQuery.include(Post.KEY_USER);
        postQuery.setLimit(20);
        postQuery.addDescendingOrder(Post.KEY_CREATED_AT);
        postQuery.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> posts, ParseException e) {
                if (e == null) {
                    for(int i = 0; i < posts.size(); i++) {
                        Post post = posts.get(i);
                        Log.d("HomeActivity", "Post["
                                + i
                                + "] = "
                                + post.getDescription()
                                + "\n username = "
                                + post.getUser().getUsername()); //why is the description not showing up
                    }
                    postAdapter.addAll(posts);
                    swipeContainer.setRefreshing(false);
                } else {
                    Log.e("QueryIssue", "Something is wrong here!");
                    e.printStackTrace();
                }
            }
        });

    }


    protected void queryPosts() {
        ParseQuery<Post> postQuery = new ParseQuery<>(Post.class);
        postQuery.include(Post.KEY_USER);
        postQuery.setLimit(20);
        postQuery.addDescendingOrder(Post.KEY_CREATED_AT);
        postQuery.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> posts, ParseException e) {
                if (e == null) {
                    for(int i = 0; i < posts.size(); i++) {
                        Post post = posts.get(i);
                        Log.d("HomeActivity", "Post["
                                + i
                                + "] = "
                                + post.getDescription()
                                + "\n username = "
                                + post.getUser().getUsername()); //why is the description not showing up
                    }

                    feedPosts.addAll(posts);
                    postAdapter.notifyDataSetChanged();
                } else {
                    Log.e("QueryIssue", "Something is wrong here!");
                    e.printStackTrace();
                }
            }
        });
    }
}
