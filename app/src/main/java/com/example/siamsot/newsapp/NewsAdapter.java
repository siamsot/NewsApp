package com.example.siamsot.newsapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class NewsAdapter extends ArrayAdapter<News> {
    int mConvertViewRes;

    public NewsAdapter(Context context, int convertViewResource, List<News> news) {
        super(context, 0, news);
        mConvertViewRes = convertViewResource;
    }

    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        // Check if an existing view is being reused, otherwise inflate the view
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.news_list, parent, false);
        }
        News currentNews = getItem(position);
        TextView title = listItemView.findViewById(R.id.header);
        title.setText(currentNews.getTitle());
        TextView dateView = listItemView.findViewById(R.id.date);
        String date = currentNews.getDate();
        String dateWithoutTime = date.split("T")[0];
        dateView.setText(dateWithoutTime);
        TextView author = listItemView.findViewById(R.id.author);
        author.setText(currentNews.getAuthor());
        TextView section = listItemView.findViewById(R.id.section);
        section.setText(currentNews.getSection());
        return listItemView;
    }
}
