package com.example.instagram.fragments;

import android.util.Log;

import com.example.instagram.model.Post;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

public class ProfileFragment extends PostsFragment {

    @Override
    protected void queryPosts() {
        ParseQuery<Post> postQuery = new ParseQuery<>(Post.class);
        postQuery.include(Post.KEY_USER);
        postQuery.setLimit(20);
        postQuery.whereEqualTo(Post.KEY_USER, ParseUser.getCurrentUser());
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

    protected void fetchTimelineAsync(int page) {
        postAdapter.clear();

        ParseQuery<Post> postQuery = new ParseQuery<>(Post.class);
        postQuery.include(Post.KEY_USER);
        postQuery.addDescendingOrder(Post.KEY_CREATED_AT);
        postQuery.whereEqualTo(Post.KEY_USER, ParseUser.getCurrentUser());
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
}
