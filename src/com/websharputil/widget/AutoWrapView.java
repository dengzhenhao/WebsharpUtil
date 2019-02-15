package com.websharputil.widget;


import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

/**
 * 放入这个控件的view,会自动根据宽度来换行
 * @author dengzh
 *
 */
public class AutoWrapView extends ViewGroup {

	private final static String TAG = "AutoWrapView";

	private final static int VIEW_MARGIN = 10;

	public AutoWrapView(Context context) {
		super(context);

	}

	public AutoWrapView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);

	}

	public AutoWrapView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

		Log.d(TAG, "widthMeasureSpec = " + widthMeasureSpec
				+ " heightMeasureSpec" + heightMeasureSpec);
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		
		for (int index = 0; index < getChildCount(); index++) {
			final View child = getChildAt(index);
			child.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
		}
		
		int lengthX = 0;
		int height = 0;
		int row = 0;
		int maxWidth = getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec);
		
		for (int i = 0; i < getChildCount(); i++) {
			final View child = this.getChildAt(i);
			int width = child.getMeasuredWidth();
			if(i !=0){
					lengthX += width + VIEW_MARGIN;
				}else{
					lengthX +=width;
				}
			if (lengthX > maxWidth || i== (getChildCount()-1)) {
				lengthX = width ;
				row++;
				height += child.getMeasuredHeight()+VIEW_MARGIN;
				
			}
		}
		height = height == 0? 0: (height+VIEW_MARGIN);
		
		if(height<getDefaultSize(getSuggestedMinimumHeight(), heightMeasureSpec)){
			height = getDefaultSize(getSuggestedMinimumHeight(), heightMeasureSpec);
		}
		setMeasuredDimension(getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec),height);
		//setMeasuredDimension(200, 1000);
	}

	@Override
	protected void onLayout(boolean arg0, int arg1, int arg2, int arg3, int arg4) {
		Log.d(TAG, "changed = " + arg0 + " left = " + arg1 + " top = " + arg2
				+ " right = " + arg3 + " botom = " + arg4);
		final int count = getChildCount();
		int row = 0;// which row lay you view relative to parent
		int lengthX = arg1; // right position of child relative to parent
		int lengthY = arg2; // bottom position of child relative to parent
		int positionTop = 0;
		
		for (int i = 0; i < count; i++) {
			final View child = this.getChildAt(i);
			int width = child.getMeasuredWidth();
			int height = child.getMeasuredHeight();
			if(i !=0){
				lengthX += width + VIEW_MARGIN;
			}else{
				lengthX +=width;
			}
			
			lengthY = positionTop + VIEW_MARGIN + height
					+ arg2;
			// if it can't drawing on a same line , skip to next line
			if (lengthX > arg3) {
				lengthX = width + arg1;
				row++;
				positionTop += height+VIEW_MARGIN;
				lengthY = row * (height + VIEW_MARGIN) + VIEW_MARGIN + height
						+ arg2;
			}
			//child.layout(lengthX - width, lengthY - height, lengthX, lengthY);
			child.layout(lengthX - width,positionTop, lengthX, positionTop+height);
		}
		//Util.LogE("����:"+row);
		

	}

}