package com.example.instagram;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.instagram.fragments.PostDetailsFragment;
import com.example.instagram.model.Post;
import com.parse.ParseFile;

import java.util.List;

import static com.example.instagram.HomeActivity.fragmentManager;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder> {

    private List<Post> feed;
    Context context;

    public PostAdapter(Context context, List<Post> posts) {
        feed = posts;
    }

    //only used when a user needs a new row
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View postView = inflater.inflate(R.layout.post_item, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(postView);
        return viewHolder;

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Post post = feed.get(i);
        viewHolder.bind(post);
    }

    @Override
    public int getItemCount() {
        return feed.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView ivPostedImage;
        TextView tvUsername;
        TextView tvCaption;

        public ViewHolder(View itemView) {
            super(itemView);
            ivPostedImage = itemView.findViewById(R.id.ivPostImage);
            tvUsername = itemView.findViewById(R.id.tvUsername);
            tvCaption = itemView.findViewById(R.id.tvCaption);

        }

        public void bind(final Post post) {
            tvUsername.setText(post.getUser().getUsername());
            tvCaption.setText(post.getDescription());
            ParseFile photo = post.getImage();
            if (photo != null) {
                Glide.with(context)
                        .load(photo.getUrl())
                        .into(ivPostedImage);
            }
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //go to the PostDetailsFragment
                    Bundle bundle = new Bundle();
                    bundle.putParcelable("Post", post);
                    Fragment fragment = new PostDetailsFragment();
                    fragment.setArguments(bundle);
                    fragmentManager.beginTransaction().replace(R.id.flContainer, fragment).commit();
                }
            });
        }
    }

    public void clear() {
        feed.clear();
        notifyDataSetChanged();
    }

    public void addAll(List<Post> list) {
        feed.addAll(list);
        notifyDataSetChanged();
    }
}
