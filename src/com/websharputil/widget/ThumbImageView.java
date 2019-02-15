package com.websharputil.widget;

import com.websharp.websharputil.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ImageView;

/**
 * 带有按下效果(半透明阴影)的Imageview
 * 
 * @类名称：ThumbImageView
 * @包名：com.websharputil.widget
 * @描述： TODO
 * @创建人： dengzh
 * @创建时间:2015-7-28 下午12:36:38
 * @版本 V1.0
 * @Copyright (c) 2015 by 苏州威博世网络科技有限公司.
 */
public class ThumbImageView extends ImageView {

	public boolean needFilter = true;

	public ThumbImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		TypedArray mTypedArray = context.obtainStyledAttributes(attrs,
				R.styleable.Websharp_ImageView);

		// 获取自定义属性和默认值
		needFilter = mTypedArray.getBoolean(
				R.styleable.Websharp_ImageView_needFilter, true);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getActionMasked()) {
		case MotionEvent.ACTION_DOWN:
			// 在按下事件中设置滤镜
			if (needFilter) {
				setFilter();
			}
			break;
		case MotionEvent.ACTION_UP:
			// 由于捕获了Touch事件，需要手动触发Click事件
			performClick();
		case MotionEvent.ACTION_CANCEL:
			// 在CANCEL和UP事件中清除滤镜
			if (needFilter) {
				removeFilter();
			}
			break;
		default:
			break;
		}
		return true;
	}

	/**
	 * 设置滤镜
	 */
	private void setFilter() {
		// 先获取设置的src图片
		Drawable drawable = getDrawable();
		// 当src图片为Null，获取背景图片
		if (drawable == null) {
			drawable = getBackground();
		}
		if (drawable != null) {
			// 设置滤镜
			drawable.setColorFilter(Color.LTGRAY, PorterDuff.Mode.MULTIPLY);
		}
	}

	/**
	 * 清除滤镜
	 */
	private void removeFilter() {
		// 先获取设置的src图片
		Drawable drawable = getDrawable();
		// 当src图片为Null，获取背景图片
		if (drawable == null) {
			drawable = getBackground();
		}
		if (drawable != null) {
			// 清除滤镜
			drawable.clearColorFilter();
		}
	}

}