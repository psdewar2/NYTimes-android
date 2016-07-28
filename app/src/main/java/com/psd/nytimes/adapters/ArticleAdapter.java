package com.psd.nytimes.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.psd.nytimes.R;
import com.psd.nytimes.models.Article;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by PSD on 7/25/16.
 */
public class ArticleAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<Article> articleList;

    // Store the context for easy access
    private Context mContext;

    //listener member variables
    private static OnItemClickListener mClickListener;

    //constructor
    public ArticleAdapter(Context context, List<Article> articles) {
        mContext = context;
        articleList = articles;
    }

    //item_article.xml
    public static class ArticleViewHolder extends RecyclerView.ViewHolder {
        ImageView articleImageView;
        TextView tvTitle;
        public ArticleViewHolder(View view) {
            super(view);
            articleImageView = (ImageView) view.findViewById(R.id.articleImageView);
            tvTitle = (TextView) view.findViewById(R.id.tvTitle);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mClickListener != null) {
                        mClickListener.onItemClick(v, getLayoutPosition());
                    }
                }
            });
        }
    }

    // Define the listener interface for normal/long clicks
    public interface OnItemClickListener {
        void onItemClick(View itemView, int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mClickListener = listener;
    }

    private Context getContext() {
        return mContext;
    }

    @Override
    public int getItemCount() {
        return articleList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    //creates different RecyclerView.ViewHolder objects based on the item view type
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        switch (viewType) {
            default:
                viewHolder = new ArticleViewHolder(inflater.inflate(R.layout.item_article, parent, false));
                break;
        }
        return viewHolder;
    }

    //updates the RecyclerView.ViewHolder contents with the item at the given position
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Article article = articleList.get(position);
        switch (holder.getItemViewType()) {
            default:
                ArticleViewHolder aVH = (ArticleViewHolder) holder;
                aVH.articleImageView.setImageResource(0);
                aVH.tvTitle.setText(article.getHeadline());

                //populate thumbnail by remotely downloading image
                String thumbnail = article.getThumbnail();
                Log.d("thumbnail url", thumbnail);
                if (!TextUtils.isEmpty(thumbnail)) {
                    Picasso.with(getContext()).load(thumbnail).into(aVH.articleImageView);
                }
                break;
        }
    }

}