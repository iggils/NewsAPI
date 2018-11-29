package com.example.android.newsapi;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.ArrayList;

public class NewsAdapter extends ArrayAdapter<NewsArticle> {

    public NewsAdapter(@NonNull Context context, @NonNull ArrayList<NewsArticle> NewsList) {
        super(context, 0, NewsList);
    }


    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        // Check if the existing view is being reused, otherwise inflate the view

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.article_view, parent, false);
        }

        NewsArticle newsArticle = getItem(position);

        TextView titleNewsTextView =  convertView.findViewById(R.id.article_title);
        String title = newsArticle.getArticleTitle();
        titleNewsTextView.setText(title);

        TextView authorNewsTextView = (TextView)convertView.findViewById(R.id.article_author);
        String author = newsArticle.getArticleAuthor();
        authorNewsTextView.setText(author);

        TextView dateNewsTextView =(TextView) convertView.findViewById(R.id.article_date);
        String date = newsArticle.getArticleDate();
        dateNewsTextView.setText(date);



        // Return the whole list item layout
        return convertView;
    }
}
