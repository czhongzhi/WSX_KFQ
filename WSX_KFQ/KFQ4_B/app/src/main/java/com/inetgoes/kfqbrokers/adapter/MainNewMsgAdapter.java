package com.inetgoes.kfqbrokers.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.inetgoes.kfqbrokers.R;
import com.inetgoes.kfqbrokers.model.NewMsg;
import com.inetgoes.kfqbrokers.utils.DateFormatHelp;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by czz on 2015/12/1.
 */
public class MainNewMsgAdapter extends BaseAdapter {
    private List<NewMsg> list;
    private Context context;
    private LayoutInflater inflater;

    public MainNewMsgAdapter(Context context, List<NewMsg> list) {
        this.list = list;
        this.context = context;
        this.inflater = LayoutInflater.from(context);
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
    public View getView(final int position, View convertView, final ViewGroup parent) {
        ViewHopler vh = null;
        if (convertView == null) {
            vh = new ViewHopler();
            convertView = inflater.inflate(R.layout.item_main_message, parent, false);
            vh.tv_message_time = (TextView) convertView.findViewById(R.id.tv_message_time);
            vh.tv_message_evaluation = (TextView) convertView.findViewById(R.id.tv_message_evaluation);
            vh.iv_message_del = (ImageView) convertView.findViewById(R.id.iv_message_del);
            convertView.setTag(vh);
        } else {
            vh = (ViewHopler) convertView.getTag();
        }

        String time = DateFormatHelp.AppPayTime_Format.format(new Date(list.get(position).getTime()));
        vh.tv_message_time.setText(time);
        vh.tv_message_evaluation.setText(list.get(position).getContent());

        vh.iv_message_del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                list.remove(position);
                MainNewMsgAdapter.this.notifyDataSetChanged();
            }
        });

        return convertView;
    }

    private class ViewHopler {
        private TextView tv_message_time;
        private TextView tv_message_evaluation;
        private ImageView iv_message_del;

    }


}
