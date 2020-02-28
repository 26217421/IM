package com.example.wangbw.mychat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.lihanxiao.mychat.R;

import java.util.List;



public class ChatMsgListViewAdapter extends BaseAdapter {
    private List<ChatMsgEntity> list;
    private LayoutInflater layoutInflater;

    public ChatMsgListViewAdapter(Context context, List<ChatMsgEntity> list) {
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
        ChatMsgEntity chatMsgEntity = list.get(position);
        ViewHolder viewHolder;

        //if(convertView == null) {
        if(chatMsgEntity.getMsgType()) {
            convertView = layoutInflater.inflate(R.layout.chatting_item_msg_text_right, null);
        }
        else {
            convertView = layoutInflater.inflate(R.layout.chatting_item_msg_text_left, null);
        }

        viewHolder = new ViewHolder();
        viewHolder.tvSendTime = (TextView)convertView.findViewById(R.id.tv_sendtime);
        viewHolder.tvUserName = (TextView)convertView.findViewById(R.id.tv_username);
        viewHolder.tvContent = (TextView)convertView.findViewById(R.id.tv_chatcontent);

        //convertView.setTag(viewHolder);
        //}else {
        //viewHolder = (ViewHolder)convertView.getTag();
        //}

        viewHolder.tvSendTime.setText(chatMsgEntity.getDate());
        viewHolder.tvUserName.setText(chatMsgEntity.getName());
        viewHolder.tvContent.setText(chatMsgEntity.getMessage());
        return convertView;
    }

    static class ViewHolder {
        public TextView tvSendTime;
        public TextView tvUserName;
        public TextView tvContent;
    }
}
