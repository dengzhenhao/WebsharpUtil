package com.websharputil.widget;


import com.websharputil.common.ConvertUtil;
import com.websharputil.common.LogUtil;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.TranslateAnimation;
import android.widget.ScrollView;

public class ScrollViewVertical extends ScrollView {
	private View inner;
	private float y;
	private Rect normal = new Rect();
	private int DISTANCE_MOVE = 60;

	public ScrollViewVertical(Context context) {
		super(context);
	}

	public ScrollViewVertical(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	protected void onFinishInflate() {
		if (getChildCount() > 0) {
			inner = getChildAt(0);
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		//Util.LogD("�����");
		if (inner == null) {
			//Util.LogD("�����2");
			return super.onTouchEvent(ev);
		} else {
			commOnTouchEvent(ev);

		}
		return super.onTouchEvent(ev);
	}

	public void commOnTouchEvent(MotionEvent ev) {
		int action = ev.getAction();
		switch (action) {
		case MotionEvent.ACTION_DOWN:
			y = ev.getY();
			break;
		case MotionEvent.ACTION_UP:
			if (isNeedAnimation()) {
				animation();
			}
			break;
		case MotionEvent.ACTION_MOVE:
			final float preY = y;
			float nowY = ev.getY();
			int deltaY = (int) (preY - nowY);
			// ����
			scrollBy(0,ConvertUtil.px2dip(getContext(),(float)deltaY));

			y = nowY;
			// �����������ϻ�������ʱ�Ͳ����ٹ�������ʱ�ƶ�����
			LogUtil.d("1:%s" , (inner.getTop() - deltaY));
			LogUtil.d("2:%s" , (inner.getBottom() - deltaY));
			//if (isNeedMove()) {
				if (normal.isEmpty()) {
					normal.set(inner.getLeft(), inner.getTop(),
							inner.getRight(), inner.getBottom());
				}

				if (Math.abs(inner.getTop()-deltaY-0.0)<=DISTANCE_MOVE) {
					// �ƶ�����
					inner.layout(inner.getLeft(), inner.getTop() - deltaY,
							inner.getRight(), inner.getBottom() - deltaY);
				}
			//}
			break;
		default:
			break;
		}
	}

	
	public void animation() {
		TranslateAnimation ta = new TranslateAnimation(0, 0, inner.getTop(),
				normal.top);
		ta.setDuration(100);
		ta.setInterpolator(new AccelerateInterpolator());
		inner.startAnimation(ta);

		inner.layout(normal.left, normal.top, normal.right, normal.bottom);
		normal.setEmpty();
	}

	public boolean isNeedAnimation() {
		return !normal.isEmpty();
	}

	public boolean isNeedMove() {
		int offset = inner.getMeasuredHeight() - getHeight();
		int scrollY = getScrollY();
		if (scrollY == 0 || scrollY == offset) {
			return true;
		}
		return false;
	}
}
