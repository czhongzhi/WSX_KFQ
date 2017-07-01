package com.inetgoes.fangdd.opencity;


import com.inetgoes.fangdd.opencity.bean.City;
import com.inetgoes.fangdd.opencity.bean.District;
import com.inetgoes.fangdd.opencity.bean.Province;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;


public class CityNameUtils {
	private static List<Province> provinces;
	public static List<String> cityName(InputStream inputStream){
		Province province = null;
		List<City> citys =null;
		City city = null;
		List<District> districts = null;
		District district = null;
		List<String> b=new ArrayList<String>();
	 
		
		try {
			XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
			XmlPullParser  parser =factory.newPullParser(); 
			
			parser.setInput(inputStream,"utf-8");
			
			int event = parser.getEventType();
			while(event != XmlPullParser.END_DOCUMENT)
			{
				switch(event)
				{
				case XmlPullParser.START_DOCUMENT:
					provinces = new ArrayList<Province>();
					break;
				case XmlPullParser.START_TAG:
					String tagName = parser.getName();
					if("p".equals(tagName)){
						province = new Province();
						citys = new ArrayList<City>();
						
						int count = parser.getAttributeCount();
						for(int i=0;i<count;i++){
							String name = parser.getAttributeName(i);
							String value = parser.getAttributeValue(i);
							if("p_id".equals(name)){
								province.setId(value);
							}
						}
					}
					if("pn".equals(tagName)){
						province.setName(parser.nextText());
					}
					if("c".equals(tagName)){
						city = new City();
						districts = new ArrayList<District>();
						
						int count = parser.getAttributeCount();
						for(int i=0;i<count;i++){
							String name = parser.getAttributeName(i);
							String value = parser.getAttributeValue(i);
							if("c_id".equals(name))
							{
								city.setId(value);
								
							}
						}
					}
					if("cn".equals(tagName))
					{
						city.setName(parser.nextText());
						b.add(city.getName());
					}
					if("d".equals(tagName))
					{
						district = new District();
						int count = parser.getAttributeCount();
						for(int i=0;i<count;i++){
							String name = parser.getAttributeName(i);
							String value = parser.getAttributeValue(i);
							if("d_id".equals(name))
								district.setId(value);
						}
						district.setName(parser.nextText());
						districts.add(district);
						
					}
					
					break;
				case XmlPullParser.END_TAG:
					if("c".equals(parser.getName()))
					{
						city.setDistricts(districts);
						citys.add(city);
					}
					if("p".equals(parser.getName()))
					{
						province.setCitys(citys);
						provinces.add(province);
					}
					break;
				}
				event = parser.next();
			}
			
		} catch (XmlPullParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		
		return b;
		
	}
}
