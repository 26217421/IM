package com.example.wangbw.mychat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.lihanxiao.mychat.R;

import java.util.List;


public class FriendEntityViewAdapter extends BaseAdapter {
    private List<FriendEntity> list;
    private LayoutInflater layoutInflater;

    public FriendEntityViewAdapter(Context context, List<FriendEntity> list) {
        this.list = list;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        FriendEntity friendEntity = list.get(position);
        ViewHolder viewHolder;

        if(convertView == null) {

            convertView = layoutInflater.inflate(R.layout.list_textview, null);

            viewHolder = new ViewHolder();
            viewHolder.tvID = (TextView)convertView.findViewById(R.id.id);

            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder)convertView.getTag();
        }

        viewHolder.tvID.setText(friendEntity.getID());

        return convertView;
    }

    static class ViewHolder {
        public TextView tvID;
    }
}
