package com.air.imagesearch.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.air.imagesearch.R;
import com.air.imagesearch.models.ImageModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hetashah on 2/12/15.
 */
public class ImageSearchAdaptor extends ArrayAdapter<ImageModel> {

    private static class ViewHolder{
        ImageView imgVwImage;
        TextView txtVwTitle;
    }

    public ImageSearchAdaptor(Context context, ArrayList<ImageModel> images) {
        super(context, android.R.layout.simple_list_item_1, images);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageModel model = getItem(position);
        ViewHolder viewHolder;
        if(convertView == null) {
            viewHolder = new ViewHolder();
            Typeface fontRingm = Typeface.createFromAsset(getContext().getAssets(), "fonts/RINGM.ttf");

            // Do not attach to parent yet. Hence, we are passing false here
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_image_search,
                                                                    parent, false);

            viewHolder.imgVwImage = (ImageView) convertView.findViewById(R.id.imgVwImage);
            viewHolder.txtVwTitle = (TextView) convertView.findViewById(R.id.txtVwTitle);
            viewHolder.txtVwTitle.setTypeface(fontRingm);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.txtVwTitle.setText(Html.fromHtml(model.getTitle()));

        Picasso.with(getContext()).load(model.getUrl()).fit().centerCrop().placeholder(R.drawable.ic_loading).into(viewHolder.imgVwImage);
        return convertView;
    }
}
