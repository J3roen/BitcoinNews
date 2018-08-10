package com.example.android.bitcoinnews;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class ArticleListAdapter extends RecyclerView.Adapter<ArticleListAdapter.ViewHolder> {
    private ArrayList<Article> mDataset;
    private Context mContext;

    /**
     * Provide a reference to the views for each data item
     * Complex data items may need more than one view per item, and
     * you provide access to all the views for a data item in a view holder
     */
    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView headerView;
        private TextView bodyView;
        private TextView sectionView;
        private Article article;
        private TextView dateView;
        private TextView authorView;

        public ViewHolder(View v) {
            super(v);
            this.headerView = (TextView) v.findViewById(R.id.article_header);
            this.bodyView = (TextView) v.findViewById(R.id.article_body);
            this.sectionView = (TextView) v.findViewById(R.id.article_section);
            this.dateView = (TextView) v.findViewById(R.id.article_date);
            this.authorView = (TextView) v.findViewById(R.id.article_author);
        }

        public void setListItem(Article listItem) {
            this.article = listItem;
        }

        @Override
        public void onClick(View v) {
            String url = article.getBody();
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            v.getContext().startActivity(i);
        }
    }

    /**
     * Provide a suitable constructor (depends on the kind of dataset)
     */
    public ArticleListAdapter(ArrayList<Article> data, Context mContext) {
        this.mDataset = data;
        this.mContext = mContext;
    }

    /**
     * Create new views (invoked by the layout manager)
     */
    @Override
    public com.example.android.bitcoinnews.ArticleListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        /** create a new view*/
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.article_list_item, parent, false);
        return new ViewHolder(itemView);
    }

    /**
     * Replace the contents of a view (invoked by the layout manager)
     */
    @SuppressLint("NewApi")
    @Override
    public void onBindViewHolder(com.example.android.bitcoinnews.ArticleListAdapter.ViewHolder holder, int position) {
        /** - get element from your dataset at this position
         / - replace the contents of the view with that element*/
        Article article = mDataset.get(position);
        holder.headerView.setText(article.getHeader());
        holder.bodyView.setText(article.getBody());
        holder.sectionView.setText(article.getSection());
        String s = mContext.getString(R.string.article_date_published);
        if (article.getDatePublished() != null) {
            StringBuilder sb = new StringBuilder(s);
            sb.append(article.getDatePublished().substring(0, article.getDatePublished().indexOf("T")));
            holder.dateView.setVisibility(View.VISIBLE);
            holder.dateView.setText(sb.toString());
        } else
            holder.dateView.setVisibility(View.GONE);
        s = mContext.getString(R.string.article_author_name);
        if (article.getAuthor() != null) {
            StringBuilder sb = new StringBuilder(s);
            holder.authorView.setText(sb.append(article.getAuthor()).toString());
            holder.authorView.setVisibility(View.VISIBLE);
        } else
            holder.authorView.setVisibility(View.GONE);
        holder.setListItem(article);

        //add onClickListener to parent Layout of item list
        ViewGroup parent = (ViewGroup) holder.headerView.getParent();
        parent.setOnClickListener(holder);
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}

