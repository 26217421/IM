package com.example.wangbw.mychat;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.example.lihanxiao.mychat.R;
import com.example.wangbw.mychat.ChatMsgEntity;
import com.example.wangbw.mychat.faceExpress.Expression;
import com.example.wangbw.mychat.faceExpress.RegularExpressionUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@SuppressLint("ValidFragment")
public class ChatFragment extends Fragment implements View.OnClickListener,AdapterView.OnItemClickListener{

    private EditText editText;
    private Button sendButton;
    private Button backButton;
    private Button faceButton;
    private TextView titleText;

    private ListView listView;//聊天信息的滑动列表
    private com.example.wangbw.mychat.ChatMsgListViewAdapter adapter;//聊天信息的适配器
    private List<ChatMsgEntity> list;//聊天信息的数据

    private GridView gridView;//表情的列表
    private SimpleAdapter gridAdapter;//表情列表的适配器
    private List<Map<String, Object>> gridList;//表情列表的数据

    private String toID;//选择聊天对象
    private MessageReceiver messageReceiver;//接受监听的对象

    public ChatFragment(String toID)
    {
        this.toID = toID;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat, container, false);

        editText = (EditText)view.findViewById(R.id.editText);
        sendButton = (Button)view.findViewById(R.id.btn_send);
        backButton = (Button)view.findViewById(R.id.btn_back);
        faceButton= (Button) view.findViewById(R.id.btn_face);
        titleText = (TextView)view.findViewById(R.id.title);

        listView = (ListView)view.findViewById(R.id.listview);
        gridView= (GridView) view.findViewById(R.id.gridview);

        sendButton.setOnClickListener(this);
        backButton.setOnClickListener(this);
        faceButton.setOnClickListener(this);
        titleText.setText("与" + toID + "聊天ing");

        list = new ArrayList<ChatMsgEntity>();
        adapter = new com.example.wangbw.mychat.ChatMsgListViewAdapter(getActivity(), list);
        listView.setAdapter(adapter);

        //填充表情数据
        gridList = new ArrayList<Map<String, Object>>();
        for (int i = 0; i < Expression.drawable.length; i++) {
            Map<String, Object> item = new HashMap<String, Object>();
            item.put("face", Expression.drawable[i]);
            gridList.add(item);
        }
        //第三个参数是单个grid的布局文件
        //第四个参数是Map对象的哪些key对应的value来生成列表项
        //第五个参数表示要填充的组件， Map对象key对应的资源与填充组件的顺序有对应关系
        gridAdapter = new SimpleAdapter(getActivity(), gridList, R.layout.grid_face,
                new String[]{"face"}, new int[]{R.id.expression});
        gridView.setAdapter(gridAdapter);
        gridView.setOnItemClickListener(this);

        RegularExpressionUtil.init(getActivity());

        initMessageReceiver();
        return view;
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btn_send) {
            send();
        }
        else if(v.getId() == R.id.btn_back) {
            FragmentManager fm = getFragmentManager();
            FragmentTransaction tx = fm.beginTransaction();
            tx.hide(this);
            tx.show(fm.findFragmentByTag("ChooseChatFragment"));
            tx.commit();
        }
        else if (v.getId()==R.id.btn_face){
            if (gridView.getVisibility()==View.VISIBLE){//可见时，隐藏表情列表
                gridView.setVisibility(View.GONE);
            }else {//不可见时显示表情列表
                gridView.setVisibility(View.VISIBLE);
            }
        }
    }

    private void send()
    {
        String content = editText.getText().toString();
        if(content.length() > 0) {
            ChatMsgEntity entity = new ChatMsgEntity();
            entity.setName(UserInfo.getID());
            entity.setDate(getDate());
//            entity.setMessage(content);
            entity.setMessage(RegularExpressionUtil.change(content));
            entity.setMsgType(true);

            list.add(entity);
            adapter.notifyDataSetChanged();//通知ListView，数据已发生改变
            listView.setSelection(listView.getCount() - 1);//发送一条消息时，ListView显示选择最后一项

            editText.setText("");

            try {
                JSONObject root = new JSONObject();
                root.put("content", content);
                root.put("fromID", UserInfo.getID());
                root.put("toID", toID);
                com.example.wangbw.mychat.SocketService.send(root.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private String getDate()
    {
        long time = System.currentTimeMillis();;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Date d = new Date(time);
        return format.format(d);
    }

    private void initMessageReceiver()
    {
        messageReceiver = new MessageReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("SocketClient");
        getActivity().registerReceiver(messageReceiver,filter);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String describe = Expression.describe[position];

        SpannableString ss = new SpannableString(describe);
        Drawable d = getResources().getDrawable(Expression.drawable[position]);
        d.setBounds(0, 0, d.getIntrinsicWidth()/2, d.getIntrinsicHeight()/2);
        ImageSpan span = new ImageSpan(d);
        ss.setSpan(span, 0, describe.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        editText.append(ss);
    }

    public class MessageReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            String content = intent.getStringExtra("jsonString");
            try
            {
                JSONObject root = new JSONObject(content);


                boolean a = (root.getString("toID").equalsIgnoreCase("Group")) && (toID.equalsIgnoreCase("Group"));
                boolean b = (!root.getString("toID").equalsIgnoreCase("Group")) && (toID.equalsIgnoreCase(root.getString("fromID")));
                if(a || b)
                {
                    if(content != null)
                    {
                        ChatMsgEntity entity = new ChatMsgEntity();
                        entity.setName(root.getString("fromID"));
                        entity.setDate(getDate());
//                        entity.setMessage(root.getString("content"));
                        SpannableString ss = RegularExpressionUtil.change(root.getString("content"));
                        entity.setMessage(ss);
                        entity.setMsgType(false);

                        list.add(entity);
                        adapter.notifyDataSetChanged();
                        listView.setSelection(listView.getCount() - 1);
                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
