package com.websharputil.widget;

import com.websharp.websharputil.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

/**
 * 
 * @类名称：WiperSwitch
 * @包名：com.websharp.mix.widget
 * @描述： 滑动开关
 * @创建人： web
 * @创建时间:2014-6-30 上午10:43:41
 * @版本 V1.0
 */
public class WiperSwitch extends View implements OnTouchListener {
	private Bitmap bg_on, bg_off, slipper_btn;
	/**
	 * 按下时的x和当前的x
	 */
	private float downX, nowX;

	/**
	 * 记录用户是否在滑动
	 */
	private boolean onSlip = false;

	/**
	 * 当前的状态
	 */
	private boolean nowStatus = true;

	private int switch_on = R.drawable.switch_on;
	private int switch_off = R.drawable.switch_off;
	private int switch_point = R.drawable.switch_point;

	/**
	 * 监听接口
	 */
	private OnChangedListener listener;

	public WiperSwitch(Context context) {
		this(context, null);
	}

	public WiperSwitch(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public WiperSwitch(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		TypedArray mTypedArray = context.obtainStyledAttributes(attrs,
				R.styleable.WiperSwitch);

		switch_on = mTypedArray.getResourceId(
				R.styleable.WiperSwitch_switch_on, R.drawable.switch_on);
		switch_off = mTypedArray.getResourceId(
				R.styleable.WiperSwitch_switch_off, R.drawable.switch_off);
		switch_point = mTypedArray.getResourceId(
				R.styleable.WiperSwitch_switch_point, R.drawable.switch_point);
		init();
	}

	public void init() {

		// 载入图片资源
		bg_on = BitmapFactory.decodeResource(getResources(), switch_on);
		bg_off = BitmapFactory.decodeResource(getResources(), switch_off);
		slipper_btn = BitmapFactory
				.decodeResource(getResources(), switch_point);
		Matrix matrix = new Matrix();
		matrix.setScale(0.7f, 0.7f);
		slipper_btn = Bitmap.createBitmap(slipper_btn, 0, 0,
				slipper_btn.getWidth(), slipper_btn.getHeight(), matrix, true);
		bg_on = Bitmap.createBitmap(bg_on, 0, 0, bg_on.getWidth(),
				bg_on.getHeight(), matrix, true);
		bg_off = Bitmap.createBitmap(bg_off, 0, 0, bg_off.getWidth(),
				bg_off.getHeight(), matrix, true);
		setOnTouchListener(this);
	}

	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		Matrix matrix = new Matrix();
		Paint paint = new Paint();

		float x = 0;

		// 根据nowX设置背景，开或者关状态
		if (nowX >= (bg_on.getWidth() / 2)) {
			canvas.drawBitmap(bg_off, matrix, paint);// 画出关闭时的背景
		} else {
			canvas.drawBitmap(bg_on, matrix, paint);// 画出打开时的背景
		}

		if (onSlip) {// 是否是在滑动状态,
			if (nowX >= bg_on.getWidth())// 是否划出指定范围,不能让滑块跑到外头,必须做这个判断
				x = bg_on.getWidth() - slipper_btn.getWidth() / 2;// 减去滑块1/2的长度
			else
				x = nowX - slipper_btn.getWidth() / 2;
		} else {
			if (nowStatus) {// 根据当前的状态设置滑块的x值
				x = 1;
			} else {
				x = bg_on.getWidth() - slipper_btn.getWidth() - 1;
			}
		}

		// 对滑块滑动进行异常处理，不能让滑块出界
		if (x < 0) {
			x = 1;
		} else if (x > bg_on.getWidth() - slipper_btn.getWidth()) {
			x = bg_on.getWidth() - slipper_btn.getWidth() - 1;
		}

		// 画出滑块
		canvas.drawBitmap(slipper_btn, x, 0, paint);
	}

	public boolean onTouch(View v, MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN: {
			if (event.getX() > bg_off.getWidth()
					|| event.getY() > bg_off.getHeight()) {
				return false;
			} else {
				onSlip = true;
				downX = event.getX();
				nowX = downX;
			}
			break;
		}
		case MotionEvent.ACTION_MOVE: {
			nowX = event.getX();
			break;
		}
		case MotionEvent.ACTION_UP: {
			onSlip = false;
			if (event.getX() <= (bg_on.getWidth() / 2)) {
				nowStatus = true;
				nowX = 1;
			} else {
				nowStatus = false;
				nowX = bg_off.getWidth() - slipper_btn.getWidth() - 1;
			}

			if (listener != null) {
				listener.OnChanged(WiperSwitch.this, nowStatus);
			}
			break;
		}
		}
		// 刷新界面
		invalidate();
		return true;
	}

	/**
	 * 为WiperSwitch设置一个监听，供外部调用的方法
	 * 
	 * @param listener
	 */
	public void setOnChangedListener(OnChangedListener listener) {
		this.listener = listener;
	}

	/**
	 * 设置滑动开关的初始状态，供外部调用
	 * 
	 * @param checked
	 */
	public void setChecked(boolean checked) {
		if (checked) {
			nowX = 1;
		} else {
			nowX = bg_off.getWidth() - 1;
		}
		nowStatus = checked;
	}

	/**
	 * 回调接口
	 * 
	 * @author len
	 * 
	 */
	public interface OnChangedListener {
		public void OnChanged(WiperSwitch wiperSwitch, boolean checkState);
	}

}
