package com.example.instagram.fragments;

import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.instagram.R;
import com.example.instagram.model.Post;
import com.parse.FindCallback;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class PostDetailsFragment extends Fragment {

    TextView tvTimestamp;
    ImageView ivPostedImage;
    TextView tvUsername;
    TextView tvCaption;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.post_details_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Post myPost = getArguments().getParcelable("Post");



        ivPostedImage = view.findViewById(R.id.imageView3);
        tvUsername = view.findViewById(R.id.tvUsername2);
        tvCaption = view.findViewById(R.id.tvCaption3);
        tvTimestamp = view.findViewById(R.id.tvTimestamp);

        tvUsername.setText(myPost.getUser().getUsername());
        tvCaption.setText(myPost.getDescription());

        Post.Query date = new Post.Query();
        date.whereEqualTo("objectId", myPost.getObjectId());
        date.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> posts, com.parse.ParseException e) {
                if (e == null) {
                    Post p = posts.get(0);
                    String date = p.getCreatedAt().toString();
                    StringBuilder dateSB = new StringBuilder(date);
                    StringBuilder dateAfter = dateSB.delete(10, 23);

                    tvTimestamp.setText(dateAfter);
                } else {
                    Log.e("QueryIssue", "Something is wrong here!");
                    e.printStackTrace();
                }
            }
        });
        if (myPost.getImage() != null) {
            Glide.with(getContext())
                    .load(myPost.getImage().getUrl())
                    .into(ivPostedImage);
        }

    }

    public String getRelativeTimeAgo(String rawJsonDate) {
        String twitterFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
        SimpleDateFormat sf = new SimpleDateFormat(twitterFormat, Locale.ENGLISH);
        sf.setLenient(true);

        String relativeDate = "";
        try {
            long dateMillis = sf.parse(rawJsonDate).getTime();
            relativeDate = DateUtils.getRelativeTimeSpanString(dateMillis,
                    System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS).toString();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return relativeDate;
    }
}
//you want to take the post that you were watching and make it detailed