package com.example.android.bitcoinnews;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class ArticleListAdapter extends RecyclerView.Adapter<ArticleListAdapter.ViewHolder> {
    private ArrayList<Article> mDataset;
    private Context mContext;

    ArticleListAdapter(ArrayList<Article> data, Context mContext) {
        this.mDataset = data;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public com.example.android.bitcoinnews.ArticleListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.article_list_item, parent, false);
        return new ViewHolder(itemView);
    }

    @SuppressLint("NewApi")
    @Override
    public void onBindViewHolder(@NonNull com.example.android.bitcoinnews.ArticleListAdapter.ViewHolder holder, int position) {

        Article article = mDataset.get(position);
        holder.headerView.setText(article.getHeader());
        holder.bodyView.setText(article.getBody());
        holder.sectionView.setText(article.getSection());
        String s = mContext.getString(R.string.article_date_published);
        if (article.getDatePublished() != null) {
            holder.dateView.setVisibility(View.VISIBLE);
            holder.dateView.setText(s + article.getDatePublished().substring(0, article.getDatePublished().indexOf("T")));
        } else
            holder.dateView.setVisibility(View.GONE);
        s = mContext.getString(R.string.article_author_name);
        if (article.getAuthor() != null) {
            holder.authorView.setText(s + article.getAuthor().toString());
            holder.authorView.setVisibility(View.VISIBLE);
        } else
            holder.authorView.setVisibility(View.GONE);
        holder.setListItem(article);

        //add onClickListener to parent Layout of item list
        ViewGroup parent = (ViewGroup) holder.headerView.getParent();
        parent.setOnClickListener(holder);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView headerView;
        private TextView bodyView;
        private TextView sectionView;
        private Article article;
        private TextView dateView;
        private TextView authorView;

        ViewHolder(View v) {
            super(v);
            this.headerView = v.findViewById(R.id.article_header);
            this.bodyView = v.findViewById(R.id.article_body);
            this.sectionView = v.findViewById(R.id.article_section);
            this.dateView = v.findViewById(R.id.article_date);
            this.authorView = v.findViewById(R.id.article_author);
        }

        void setListItem(Article listItem) {
            this.article = listItem;
        }

        @Override
        public void onClick(View v) {
            String url = article.getUrl();
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            v.getContext().startActivity(i);
        }
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}

