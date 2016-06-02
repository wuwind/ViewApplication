package com.wuwind.corelibrary.adapter;

import android.support.v7.widget.RecyclerView.ViewHolder;
import android.util.SparseArray;
import android.view.View;

public class RecyclerViewHolder extends ViewHolder {

	private SparseArray<View> views;

	public RecyclerViewHolder(View itemView) {
		super(itemView);
		views = new SparseArray<View>();
	}

	@SuppressWarnings("unchecked")
	public <T> T getView(int id) {
		View view = views.get(id);
		if (null == view) {
			view = itemView.findViewById(id);
			views.put(id, view);
		}
		return (T) view;
	}

}