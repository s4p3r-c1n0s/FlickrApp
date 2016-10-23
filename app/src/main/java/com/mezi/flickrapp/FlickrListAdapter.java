package com.mezi.flickrapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.mezi.simplefifothread.TaskProcessor;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Created by umangjeet on 21/10/16.
 */

public class FlickrListAdapter extends ArrayAdapter {

    private List<String> flickrUrls;

    Context ctx;

    //ExecutorService service = Executors.newSingleThreadExecutor();
    TaskProcessor service = TaskProcessor.getInstance(10,60000);

    public FlickrListAdapter(Context context, int resource, List<String> objects) {
        super(context, resource, objects);
        this.flickrUrls = objects;
        this.ctx = context;

    }

    public int getCount() {
        return flickrUrls.size();
    }

    public Object getItem(int position) {
        return flickrUrls.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    class ViewHolder
    {
        ImageView ivFlickr;
        TextView tvFlickr;
        Future imgWrkRslt;
    }


    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder;

        if(convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater lf = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = lf.inflate(R.layout.image_item, null);
            viewHolder.ivFlickr = (ImageView) convertView.findViewById(R.id.flickr_img);
            viewHolder.tvFlickr = (TextView) convertView.findViewById(R.id.flickr_text);
            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            viewHolder.imgWrkRslt.cancel(true);
        }
        viewHolder.ivFlickr.setImageResource(android.R.drawable.ic_menu_report_image);
        viewHolder.tvFlickr.setText((String) getItem(position));
        ImageWorker imgWrk = new ImageWorker((String) getItem(position), viewHolder.ivFlickr);
        //viewHolder.imgWrkRslt = service.submit(imgWrk);
        viewHolder.imgWrkRslt = service.queueRequest(imgWrk);


        // do some business logic here
        return convertView;
    }
}
