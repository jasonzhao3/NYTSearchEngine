package com.uber.yangz.nytsearchengine.adapters;


import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.uber.yangz.nytsearchengine.R;
import com.uber.yangz.nytsearchengine.models.Article;

import java.util.ArrayList;

import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;


public class ArticlesAdaptor extends ArrayAdapter<Article> {

    // View lookup cache
    private static class ViewHolder {
        public ImageView ivThumbnail;
        public TextView tvHeadline;
    }

    public ArticlesAdaptor(Context context, ArrayList<Article> articles) {
        super(context, 0, articles);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Article article = getItem(position);
        ViewHolder viewHolder;

        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_article, parent, false);
            viewHolder.tvHeadline = (TextView) convertView.findViewById(R.id.tv_headline);
            viewHolder.ivThumbnail = (ImageView) convertView.findViewById(R.id.iv_thumbnail);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.tvHeadline.setText(article.getHeadline());
        String thumbnailUrl = article.getThumbNailUrl();
        if (!TextUtils.isEmpty(thumbnailUrl)) {
            Picasso.with(this.getContext()).
                    load(thumbnailUrl).
                    transform(new RoundedCornersTransformation(10, 10)).
                    into(viewHolder.ivThumbnail);
        }

        return convertView;
    }
}
