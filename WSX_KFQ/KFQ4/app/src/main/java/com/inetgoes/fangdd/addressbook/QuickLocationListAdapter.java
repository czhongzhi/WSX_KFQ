package com.inetgoes.fangdd.addressbook;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.inetgoes.fangdd.FangApplication;
import com.inetgoes.fangdd.R;
import com.inetgoes.fangdd.model.MyBrokers;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by czz on 2015/12/28.
 */
public class QuickLocationListAdapter extends BaseAdapter {

    private LayoutInflater layoutInflater;
    private View.OnClickListener onClickListener;
    private String[] stringArr;
    private Map<String, Integer> map = new HashMap<String, Integer>();
    private List<MyBrokers> myBrokers;

    public QuickLocationListAdapter(Context context, String[] arr, View.OnClickListener listener, Map<String, Integer> map, List<MyBrokers> myBrokers) {
        layoutInflater = LayoutInflater.from(context);
        this.onClickListener = listener;
        stringArr = arr;
        this.map = map;
        this.myBrokers = myBrokers;
    }

    public int getCount() {
        return stringArr == null ? 0 : stringArr.length;
    }

    public Object getItem(int position) {
//        if (stringArr != null) {
//            String string = map.get(stringArr[position]);
//            return string;
//        }
        return null;
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.list_item, null);
            holder = new ViewHolder();
            holder.firstCharHintTextView = (TextView) convertView.findViewById(R.id.text_first_char_hint);
            holder.nameTextView = (TextView) convertView.findViewById(R.id.text_website_name);
            holder.broker_star = (RatingBar) convertView.findViewById(R.id.broker_star);
            holder.broker_icon = (ImageView) convertView.findViewById(R.id.broker_icon);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        int index = map.get(stringArr[position]);
        holder.nameTextView.setText(myBrokers.get(index).getUsername());
        holder.broker_star.setRating((null == myBrokers.get(index).getStarlevel() ? 0 : myBrokers.get(index).getStarlevel()));
        if (!TextUtils.isEmpty(myBrokers.get(index).getUserimage())) {
            ImageLoader.getInstance().displayImage(myBrokers.get(index).getUserimage(), holder.broker_icon, FangApplication.options_R, FangApplication.animateFirstListener);
        }


        int idx = position - 1;
        char previewChar = idx >= 0 ? stringArr[idx].charAt(0) : ' ';  //前一个字符
        char currentChar = stringArr[position].charAt(0);                //当前字符
        if (currentChar != previewChar) {                                     //如果不相等时显示
            if (isWord(currentChar)) {
                if (position != 0) {
                    holder.firstCharHintTextView.setVisibility(View.VISIBLE);
                } else {
                    holder.firstCharHintTextView.setVisibility(View.GONE);
                }
                holder.firstCharHintTextView.setText(String.valueOf(currentChar));
            } else {
                if (isWord(previewChar)) {
                    holder.firstCharHintTextView.setVisibility(View.VISIBLE);
                    holder.firstCharHintTextView.setText("*");
                } else {
                    holder.firstCharHintTextView.setVisibility(View.GONE);
                }
            }
        } else {
            holder.firstCharHintTextView.setVisibility(View.GONE);
        }
        return convertView;
    }

    public final class ViewHolder {
        public TextView firstCharHintTextView;
        public TextView nameTextView;
        public RatingBar broker_star;
        public ImageView broker_icon;
    }

    public boolean isWord(char c) {
        Pattern pattern = Pattern.compile("^[A-Za-z]+$");
        Matcher isNum = pattern.matcher(String.valueOf(c));
        if (!isNum.matches()) {
            return false;
        } else {
            return true;
        }
    }

}
