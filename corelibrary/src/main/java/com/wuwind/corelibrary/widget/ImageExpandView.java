package com.wuwind.corelibrary.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;

import com.wuwind.corelibrary.R;
import com.wuwind.corelibrary.utils.DisplayUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ImageExpandView<T> extends LinearLayout implements OnClickListener,
		OnLongClickListener {

	private Context context;
	private int witdthTotal = 0;
	private int screenWidth;
	private int imgWidth  =  100;;
	private int imgHeight  =  100;;
	private int marginRight  = 30;;
	private int marginTop = 30;;
	private int maxNum  = 2;
	private LinearLayout layout;
	private LayoutParams imagePparams;
	private List<T> datas = new ArrayList<T>();
	private Map<T, Bitmap> dataMap = new HashMap<T, Bitmap>();
	private ImageView lastImg;
	private ExpandViewListener listener;
	private boolean enable = true;

	public ImageExpandView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		screenWidth = DisplayUtil.getDisplayMetrics(context).x;
		int paddingLeft = getPaddingLeft();
		int paddingRight = getPaddingRight();
		screenWidth -= paddingLeft;
		screenWidth -= paddingLeft;
		screenWidth -= paddingRight;
		layout = new LinearLayout(context);
		imagePparams = new LayoutParams(imgWidth, imgHeight);
		imagePparams.setMargins(0, marginTop, marginRight, 0);
		this.setOrientation(VERTICAL);
		addView(layout);
		addBtn();
	}

	public void addListener(ExpandViewListener listener) {
		this.listener = listener;
	}

	public void addData(T t, Bitmap bitmap) {
		datas.add(t);
		dataMap.put(t, bitmap);
		lastImg.setScaleType(ScaleType.FIT_XY);
		lastImg.setImageBitmap(bitmap);
		lastImg.setTag(t);
		addBtn();
	}

	private void addBtn() {
		if (datas.size() >= maxNum) {
			return;
		}
		System.out.println("--addbtn");
		lastImg = new ImageView(context);
		lastImg.setScaleType(ScaleType.FIT_XY);
		lastImg.setImageResource(R.drawable.btn_add);
		lastImg.setOnClickListener(this);
		lastImg.setOnLongClickListener(this);
		refreshUI(lastImg);
	}

	public List<T> getDatas() {
		return datas;
	}

	private void refreshUI(ImageView img) {
		System.out.println("--refreshUI");
		img.setLayoutParams(imagePparams);
		witdthTotal += imgWidth;
		System.out.println("--witdthTotal" + witdthTotal);
		if (witdthTotal > screenWidth) {
			layout = new LinearLayout(context);
			witdthTotal = imgWidth;
			addView(layout);
		}
		witdthTotal += marginRight;
		layout.addView(img);
	}

	private void notifyDataSetChanged() {
		List<T> datasCopy = new ArrayList<T>(datas);
		datas.clear();
		removeAllViews();
		witdthTotal = 0;
		layout.removeAllViews();
		addView(layout);
		addBtn();
		for (T t : datasCopy) {
			System.out.println(t + "t--");
			System.out.println(dataMap.get(t) + "dataMap.get(t)--");
			addData(t, dataMap.get(t));
		}
	}

	// public void setDatas(List<T> datas) {
	// removeAllViews();
	//
	// }

	@Override
	public void onClick(View v) {
		if (listener == null)
			return;
		Object data = v.getTag();
		if (data == null) {
			if (enable)
				listener.addItem();
		} else
			listener.itemClick(data);
	}

	public interface ExpandViewListener {
		public void addItem();

		public void itemClick(Object data);
	}

	@Override
	public boolean onLongClick(View v) {
		delete(v.getTag());
		return true;
	}

	public void delete(Object object) {
		datas.remove(object);
		dataMap.remove(object);
		notifyDataSetChanged();
	}

	public void setEnable(boolean enable) {
		this.enable = enable;
	}
}
