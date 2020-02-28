package com.example.wangbw.mychat;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.lihanxiao.mychat.R;

import java.util.ArrayList;
import java.util.List;


public class ChooseChatFragment extends Fragment implements View.OnClickListener,AdapterView.OnItemClickListener{

    private TextView textView;
    private EditText editText;
    private Button buttonAdd;
    private Button buttonGroup;

    private ListView listView;
    private com.example.wangbw.mychat.FriendEntityViewAdapter adapter;
    private List<com.example.wangbw.mychat.FriendEntity> list;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_choose_chat, container, false);
        textView = (TextView)view.findViewById(R.id.titleText);
        editText = (EditText)view.findViewById(R.id.editText);
        buttonAdd = (Button)view.findViewById(R.id.buttonAdd);
        buttonGroup = (Button)view.findViewById(R.id.buttonGroup);
        listView = (ListView)view.findViewById(R.id.listview);

        textView.setText(UserInfo.getID() + "的通讯录");
        buttonAdd.setOnClickListener(this);
        buttonGroup.setOnClickListener(this);
        listView.setOnItemClickListener(this);

        list = new ArrayList<com.example.wangbw.mychat.FriendEntity>();
        adapter = new com.example.wangbw.mychat.FriendEntityViewAdapter(getActivity(), list);
        listView.setAdapter(adapter);

        return view;
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.buttonAdd)
        {
            if(editText.getText().toString() != null)
            {
                com.example.wangbw.mychat.FriendEntity fe = new com.example.wangbw.mychat.FriendEntity();
                fe.setID(editText.getText().toString());

                list.add(fe);
                adapter.notifyDataSetChanged();
                listView.setSelection(listView.getCount() - 1);

                editText.setText("");
            }
        }
        else if(v.getId() == R.id.buttonGroup)
        {
            toChatFragment("Group");
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        toChatFragment(list.get(position).getID());
    }

    private void toChatFragment(String tag)
    {
        FragmentManager fm = getFragmentManager();
        FragmentTransaction tx = fm.beginTransaction();
        tx.hide(this);
        if(fm.findFragmentByTag(tag) == null)
        {
            tx.add(R.id.content, new ChatFragment(tag), tag);
        }
        else
        {
            tx.show(fm.findFragmentByTag(tag));
        }
        tx.addToBackStack(null);
        tx.commit();
    }
}
