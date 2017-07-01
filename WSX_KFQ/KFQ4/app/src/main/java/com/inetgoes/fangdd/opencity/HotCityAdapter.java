package com.inetgoes.fangdd.opencity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.inetgoes.fangdd.R;

public class HotCityAdapter extends BaseAdapter {
	private Context context;
	private String[] hotCityName;
	
	
	 

	public HotCityAdapter(Context context, String[] hotCityName) {
		super();
		this.context = context;
		this.hotCityName = hotCityName;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return hotCityName.length;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return hotCityName[position];
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder=null;
		if (convertView==null) {
			convertView=LayoutInflater.from(context).inflate(R.layout.gridview_item, null);
			holder=new ViewHolder(convertView);
			convertView.setTag(holder);
		}else {
			holder=(ViewHolder) convertView.getTag();
		}
		holder.gridName.setText(hotCityName[position]);
		
		return convertView;
	}
	private class ViewHolder{
		private TextView gridName;
		public ViewHolder(View view) {
			super();
			gridName = (TextView) view.findViewById(R.id.gridView_item);
		}
		
		
	}
}
