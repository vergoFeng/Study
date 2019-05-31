package com.vergo.demo.exam.pwdinput;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;

import com.vergo.demo.exam.R;

import java.util.ArrayList;
import java.util.List;

public class PwdInputActivity extends AppCompatActivity {
    private List<Integer> listNumber;

    private PwdInputView mPwdInputView;
    private GridView mGridView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pwd_input_activity);

        mPwdInputView = findViewById(R.id.paypwd_et);
        mGridView = findViewById(R.id.keyboadr_gv);

        //初始化数据
        listNumber = new ArrayList<>();
        for (int i = 1; i < 10; i++) {
            listNumber.add(i);
        }
        listNumber.add(10);
        listNumber.add(0);
        listNumber.add(R.drawable.del_btn);

        mGridView.setAdapter(adapter);
    }

    BaseAdapter adapter = new BaseAdapter() {
        @Override
        public int getCount() {
            return listNumber.size();
        }
        @Override
        public Object getItem(int position) {
            return listNumber.get(position);
        }
        @Override
        public long getItemId(int position) {
            return position;
        }
        @SuppressLint("ClickableViewAccessibility")
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            final ViewHolder holder;
            if (convertView == null) {
                convertView = View.inflate(parent.getContext(), R.layout.gridview_item, null);
                holder = new ViewHolder();
                holder.btnNumber = convertView.findViewById(R.id.btNumber);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.btnNumber.setText(listNumber.get(position).toString());
            if (position == 9) {
                holder.btnNumber.setText("");
                holder.btnNumber.setBackgroundColor(Color.parseColor("#e3e7ee"));
            }
            if (position == 11) {
                holder.btnNumber.setText("");
                holder.btnNumber.setBackgroundResource(listNumber.get(position));
            }

            holder.btnNumber.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (position < 11 && position != 9) {//0-9按钮
                        mPwdInputView.inputPwd(listNumber.get(position).toString());
                    } else if(position == 11) {//删除
                        mPwdInputView.deletePwd();
                    }
                }
            });

            return convertView;
        }
    };
    static class ViewHolder {
        public TextView btnNumber;
    }
}
