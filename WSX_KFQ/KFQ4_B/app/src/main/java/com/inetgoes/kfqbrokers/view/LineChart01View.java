/**
 * Copyright 2014  XCL-Charts
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 	
 * @Project XCL-Charts 
 * @Description Android图表基类库
 * @author XiongChuanLiang<br/>(xcl_168@aliyun.com)
 * @Copyright Copyright (c) 2014 XCL-Charts (www.xclcharts.com)
 * @license http://www.apache.org/licenses/  Apache v2 License
 * @version 1.0
 */
package com.inetgoes.kfqbrokers.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

import com.inetgoes.kfqbrokers.R;

import org.xclcharts.chart.LineChart;
import org.xclcharts.chart.LineData;
import org.xclcharts.event.click.PointPosition;
import org.xclcharts.renderer.XEnum;

import java.util.LinkedList;

/**
 * @ClassName LineChart01View
 * @Description  折线图的例子
 * @author XiongChuanLiang<br/>(xcl_168@aliyun.com)
 */
public class LineChart01View extends DemoView {
	
	private String TAG = "LineChart01View";
	private LineChart chart = new LineChart();

    private int baseColor = Color.parseColor("#00a1fd");
    private int axisMax = 8;   //数据轴最大值
    private int axisSteps = 2;  //数据轴刻度间隔

	//标签集合
	private LinkedList<String> labels = new LinkedList<String>();
	private LinkedList<LineData> chartData = new LinkedList<LineData>();

	private Paint mPaintTooltips = new Paint(Paint.ANTI_ALIAS_FLAG);
	
	
	public LineChart01View(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		initView();
	}
	
	public LineChart01View(Context context, AttributeSet attrs){   
        super(context, attrs);   
        initView();
	 }
	 
	 public LineChart01View(Context context, AttributeSet attrs, int defStyle) {
			super(context, attrs, defStyle);
			initView();
	 }

    public LineChart getChart(){
        return chart;
    }


	 private void initView()
	 {
		 	chartLabels();
			chartDataSet();	
			chartRender();
			
			//綁定手势滑动事件
			//this.bindTouch(this,chart);
	 }
	 

	@Override  
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {  
        super.onSizeChanged(w, h, oldw, oldh);  
       //图所占范围大小
        chart.setChartRange(w,h);
    }

	private void chartRender()
	{
		try {				
			
			//设置绘图区默认缩进px值,留置空间显示Axis,Axistitle....		
			int [] ltrb = getBarLnDefaultSpadding();
			//chart.setPadding(ltrb[0], ltrb[1], ltrb[2], ltrb[3]);
            chart.setPadding(ltrb[0], 36, 15, 36);
			
			//限制Tickmarks可滑动偏移范围
//			chart.setXTickMarksOffsetMargin(ltrb[2] - 20.f);
//			chart.setYTickMarksOffsetMargin(ltrb[3] - 20.f);
            //chart.setXTickMarksOffsetMargin(0.f);
            //chart.setYTickMarksOffsetMargin(0.f);

			//显示边框
			//chart.showRoundBorder();


            //图表缩放
            //chart.disableScale();//禁用
            //chart.enableScale();//启用

            //手势滑动
            //chart.disablePanMode(); //禁用
			
			
			//设定数据源
			chart.setCategories(labels);								
			chart.setDataSource(chartData);
			
			//数据轴最大值
			chart.getDataAxis().setAxisMax(axisMax);
			//数据轴刻度间隔
			chart.getDataAxis().setAxisSteps(axisSteps);



			//背景网格
			chart.getPlotGrid().showHorizontalLines();
			chart.getPlotGrid().showVerticalLines();
			//chart.getPlotGrid().showEvenRowBgColor();
			//chart.getPlotGrid().showOddRowBgColor();
            chart.setBackgroundColor(Color.WHITE);

			chart.getPlotGrid().getHorizontalLinePaint().setStrokeWidth(0.8f);
            chart.getPlotGrid().getVerticalLinePaint().setStrokeWidth(0.1f);
			chart.getPlotGrid().setHorizontalLineStyle(XEnum.LineStyle.SOLID);
			chart.getPlotGrid().setVerticalLineStyle(XEnum.LineStyle.SOLID);
			
			chart.getPlotGrid().getHorizontalLinePaint().setColor(getResources().getColor(R.color.divider_font_tint));
			chart.getPlotGrid().getVerticalLinePaint().setColor(getResources().getColor(R.color.divider_font_tint));
			
//			chart.setTitle("折线图(Line Chart)");
//			chart.addSubtitle("(XCL-Charts Demo)");
			
			//chart.getAxisTitle().setLowerTitle("(月份)");
			
			//激活点击监听
			chart.ActiveListenItemClick();
			//为了让触发更灵敏，可以扩大5px的点击监听范围
			chart.extPointClickRange(10);
			chart.showClikedFocus();
												
			//绘制十字交叉线
			chart.showDyLine();
			chart.getDyLine().setDyLineStyle(XEnum.DyLineStyle.Vertical);

			/*			
			//想隐藏轴的可以下面的函数来隐藏
			chart.getDataAxis().hide();
			chart.getCategoryAxis().hide();
			//想设置刻度线属性的可用下面函数
			chart.getDataAxis().getTickMarksPaint()
			chart.getCategoryAxis().getTickMarksPaint()
			//想设置刻度线标签属性的可用下面函数 
			chart.getDataAxis().getAxisTickLabelPaint()	
			chart.getCategoryAxis().getAxisTickLabelPaint()
			*/

            //隐藏轴线或轴上的刻度/标签
            chart.getDataAxis().hideAxisLine();
            chart.getDataAxis().hideTickMarks();
            chart.getCategoryAxis().hideAxisLine();
            chart.getCategoryAxis().hideTickMarks();

			chart.getPlotArea().extWidth(0.f);

			//调整轴显示位置
			//chart.setDataAxisLocation(XEnum.AxisLocation.RIGHT);
			//chart.setCategoryAxisLocation(XEnum.AxisLocation.TOP);
            chart.setDataAxisLocation(XEnum.AxisLocation.LEFT);
            chart.setCategoryAxisLocation(XEnum.AxisLocation.BOTTOM);


			//收缩绘图区右边分割的范围，让绘图区的线不显示出来
			chart.getClipExt().setExtRight(0.f);
            //隐藏图例
            chart.getPlotLegend().hide();




            //test x坐标从刻度线而不是轴开始
			//chart.setXCoordFirstTickmarksBegin(true);
			//chart.getCategoryAxis().showTickMarks();
			//chart.getCategoryAxis().setVerticalTickPosition(XEnum.VerticalAlign.MIDDLE);
			
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			Log.e(TAG, e.toString());
		}
	}

    public LinkedList<Double> dataSeries2;

	private void chartDataSet()
	{
		
		//Line 1
		/*LinkedList<Double> dataSeries1= new LinkedList<Double>();
		dataSeries1.add(20d);
		dataSeries1.add(10d);
		dataSeries1.add(31d);
		dataSeries1.add(40d);
		dataSeries1.add(0d);
		LineData lineData1 = new LineData("方块",dataSeries1,Color.rgb(234, 83, 71));
		lineData1.setLabelVisible(true);
		lineData1.setDotStyle(XEnum.DotStyle.RECT);
		lineData1.getDotLabelPaint().setColor(Color.BLUE);
		lineData1.getDotLabelPaint().setTextSize(22);
		lineData1.getDotLabelPaint().setTextAlign(Align.LEFT);
		lineData1.setItemLabelRotateAngle(45.f);

		lineData1.getLabelOptions().setLabelBoxStyle(XEnum.LabelBoxStyle.TEXT);*/
		
		//lineData1.getLabelOptions().
		
		//lineData1.setDataSet(dataSeries);
		//this.invalidate();
		
		//Line 2
		dataSeries2= new LinkedList<Double>();
		dataSeries2.add((double)0);
		dataSeries2.add((double)2);
		dataSeries2.add((double)1);
		dataSeries2.add((double)6);
		dataSeries2.add((double)4);
        dataSeries2.add((double)4);
        dataSeries2.add((double)0);
        dataSeries2.add((double)2);
        dataSeries2.add((double)1);
        dataSeries2.add((double)6);
        dataSeries2.add((double)4);
        LineData lineData2 = new LineData("圆环",dataSeries2,baseColor);
		lineData2.setDotStyle(XEnum.DotStyle.RING);				
		lineData2.getPlotLine().getDotPaint().setColor(baseColor);
		//lineData2.setLabelVisible(true); //每个点的标签
		lineData2.getPlotLine().getPlotDot().setRingInnerColor(Color.WHITE);
		lineData2.setLineStyle(XEnum.LineStyle.SOLID);
        //设置线的粗细
        lineData2.getPlotLine().getLinePaint().setStrokeWidth(4);
		// 设置点大小
		lineData2.getPlotLine().getPlotDot().setDotRadius(10);
						
		//Line 3
/*		LinkedList<Double> dataSeries3= new LinkedList<Double>();
		dataSeries3.add(65d);
		dataSeries3.add(75d);
		dataSeries3.add(55d);
		dataSeries3.add(65d);
		dataSeries3.add(95d);
		LineData lineData3 = new LineData("圆点",dataSeries3,Color.rgb(123, 89, 168));
		lineData3.setDotStyle(XEnum.DotStyle.DOT);
		lineData3.setDotRadius(20);*/
		//lineData3.setLabelVisible(true);
		//lineData3.getDotLabelPaint().setTextAlign(Align.CENTER);				
		
		//Line 4
/*		LinkedList<Double> dataSeries4= new LinkedList<Double>();
		dataSeries4.add(50d);
		dataSeries4.add(60d);
		dataSeries4.add(80d);
		dataSeries4.add(84d);
		dataSeries4.add(90d);
		LineData lineData4 = new LineData("棱形",dataSeries4,Color.rgb(84, 206, 231));		
		lineData4.setDotStyle(XEnum.DotStyle.PRISMATIC);
		//把线弄细点
		lineData4.getLinePaint().setStrokeWidth(2);
		
		lineData4.getLabelOptions().setLabelBoxStyle(XEnum.LabelBoxStyle.CIRCLE);
		lineData4.getLabelOptions().getBox().getBackgroundPaint().setColor(Color.GREEN);	
		lineData4.setLabelVisible(true);	*/
		
		//Line 5
/*		LinkedList<Double> valuesE= new LinkedList<Double>();
		valuesE.add(0d);
		valuesE.add(80d);
		valuesE.add(85d);
		valuesE.add(90d);
		LineData lineData5 = new LineData("定制",valuesE,Color.rgb(234, 142, 43));
		lineData5.setDotRadius(15);
		lineData5.setDotStyle(XEnum.DotStyle.TRIANGLE);*/
				
		//Line 2
/*		LinkedList<Double> dataSeries6= new LinkedList<Double>();
		dataSeries6.add((double)50); 
		dataSeries6.add((double)52); 
		dataSeries6.add((double)53); 	
		dataSeries6.add((double)55); 
		dataSeries6.add((double)40); 
		LineData lineData6 = new LineData("圆环2",dataSeries6,Color.rgb(75, 166, 51));
		lineData6.setDotStyle(XEnum.DotStyle.RING2);				
		lineData6.getPlotLine().getDotPaint().setColor(Color.RED);
		lineData6.setLabelVisible(true);		
		//lineData6.getPlotLine().getPlotDot().setRingInnerColor(Color.GREEN);
		//lineData6.getPlotLine().getPlotDot().setRing2InnerColor(Color.GREEN);
		//lineData6.setLineStyle(XEnum.LineStyle.DASH);
		lineData6.getDotLabelPaint().setColor(Color.rgb(212, 64, 39));
		lineData6.getLabelOptions().getBox().getBackgroundPaint().setColor(Color.rgb(57, 172, 241));
		lineData6.getLabelOptions().getBox().setBorderLineColor(Color.YELLOW);*/
		
		
		//chartData.add(lineData1);
		chartData.add(lineData2);
		//chartData.add(lineData3);
		//chartData.add(lineData4);
		//chartData.add(lineData5);
		//chartData.add(lineData6);
	}
	
	private void chartLabels()
	{
		labels.add("1");
		labels.add("2");
		labels.add("3");
		labels.add("4");
		labels.add("5");
        labels.add("6");
        labels.add("7");
        labels.add("8");
        labels.add("9");
        labels.add("10");
        labels.add("11");
        labels.add("12");
	}
	
	@Override
    public void render(Canvas canvas) {
        try{
            chart.render(canvas);
        } catch (Exception e){
        	Log.e(TAG, e.toString());
        }
    }

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub		
				
		if(event.getAction() == MotionEvent.ACTION_UP) 
		{			
			triggerClick(event.getX(),event.getY());
		}
		super.onTouchEvent(event);
		return true;
	}
	
	
	//触发监听
	private void triggerClick(float x,float y)
	{		
		
		//交叉线
		if(chart.getDyLineVisible())chart.getDyLine().setCurrentXY(x,y);		
		if(!chart.getListenItemClickStatus())
		{
			//交叉线
			if(chart.getDyLineVisible())this.invalidate();
		}else{			
			PointPosition record = chart.getPositionRecord(x,y);			
			if( null == record)
			{
				if(chart.getDyLineVisible())this.invalidate();
				return;
			}
	
			LineData lData = chartData.get(record.getDataID());
			Double lValue = lData.getLinePoint().get(record.getDataChildID());
		
			float r = record.getRadius();
			chart.showFocusPointF(record.getPosition(),r + r*0.5f);		
			chart.getFocusPaint().setStyle(Style.STROKE);
			chart.getFocusPaint().setStrokeWidth(3);		
			if(record.getDataID() >= 3)
			{
				chart.getFocusPaint().setColor(Color.BLUE);
			}else{
				chart.getFocusPaint().setColor(Color.RED);
			}		
			
			//在点击处显示tooltip
			mPaintTooltips.setColor(getResources().getColor(R.color.divider_font_mid));
			//chart.getToolTip().setCurrentXY(x,y);
			chart.getToolTip().setCurrentXY(record.getPosition().x,record.getPosition().y); 
			
//			chart.getToolTip().addToolTip(" Key:"+lData.getLineKey(),mPaintTooltips);
//			chart.getToolTip().addToolTip(" Label:"+lData.getLabel(),mPaintTooltips);
//			chart.getToolTip().addToolTip(" Current Value:" +Double.toString(lValue),mPaintTooltips);
				
			
			//当前标签对应的其它点的值
			int cid = record.getDataChildID();
			String xLabels = "";			
			for(LineData data : chartData)
			{
				if(cid < data.getLinePoint().size())
				{
					xLabels = Double.toString(data.getLinePoint().get(cid));
                    chart.getToolTip().addToolTip(xLabels,mPaintTooltips);
//					chart.getToolTip().addToolTip("Line:"+data.getLabel()+","+ xLabels,mPaintTooltips);
				}
			}
			
			
			this.invalidate();
		}
		
		
	}
	
	
}
