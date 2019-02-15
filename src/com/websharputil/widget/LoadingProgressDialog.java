
/**************************************************************************************
* [Project]
*       MyProgressDialog
* [Package]
*       com.lxd.widgets
* [FileName]
*       CustomProgressDialog.java
* [Copyright]
*       Copyright 2012 LXD All Rights Reserved.
* [History]
*       Version          Date              Author                        Record
*--------------------------------------------------------------------------------------
*       1.0.0           2012-4-27         lxd (rohsuton@gmail.com)        Create
**************************************************************************************/
	
package com.websharputil.widget;



import com.websharp.websharputil.R;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.TextView;




public class LoadingProgressDialog extends Dialog {
	private Context context = null;

	private static LoadingProgressDialog customProgressDialog = null;
	
	public LoadingProgressDialog(Context context){
		super(context);
		this.context = context;
	}
	
	public LoadingProgressDialog(Context context, int theme) {
        super(context, theme);
    }
	
	public static LoadingProgressDialog createDialog(Context context){
		customProgressDialog = new LoadingProgressDialog(context,R.style.LoadingProgressDialog);
		customProgressDialog.setContentView(R.layout.layout_loading_progressdialog);
		customProgressDialog.getWindow().getAttributes().gravity = Gravity.CENTER;
		
		return customProgressDialog;
	}
 
    public void onWindowFocusChanged(boolean hasFocus){
    	
    	if (customProgressDialog == null){
    		return;
    	}
    	
        //ImageView imageView = (ImageView) customProgressDialog.findViewById(R.id.loadingImageView);
        //AnimationDrawable animationDrawable = (AnimationDrawable) imageView.getBackground();
        //animationDrawable.start();
    }
 
    /**
     * 
     * [Summary]
     *       setTitile ����
     * @param strTitle
     * @return
     *
     */
    public LoadingProgressDialog setTitile(String strTitle){
    	return customProgressDialog;
    }
    
    /**
     * 
     * [Summary]
     *       setMessage ��ʾ����
     * @param strMessage
     * @return
     *
     */
    public LoadingProgressDialog setMessage(String strMessage){
    	TextView tvMsg = (TextView)customProgressDialog.findViewById(R.id.id_tv_loadingmsg);
    	
    	if (tvMsg != null){
    		tvMsg.setText(strMessage);
    	}
    	
    	return customProgressDialog;
    }
}
