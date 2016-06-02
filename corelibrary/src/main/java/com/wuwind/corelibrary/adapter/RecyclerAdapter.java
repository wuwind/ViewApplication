package com.wuwind.corelibrary.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public abstract class RecyclerAdapter<T, H extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<H> {

    private List<T> datas;
    private int resource;
    private OnItemClickListener itemClickListener;

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public RecyclerAdapter(List<T> datas, int resource) {
        this(datas, resource, null);
    }

    public RecyclerAdapter(List<T> datas, int resource, OnItemClickListener itemClickListener) {
        this.datas = datas;
        this.resource = resource;
        this.itemClickListener = itemClickListener;
    }

    @Override
    public int getItemCount() {
        return datas == null ? 0 : datas.size();
    }

    @Override
    public void onBindViewHolder(final H vh, final int position) {
        onBindViewHolder(vh, datas.get(position), position);
        if (itemClickListener != null)
            vh.itemView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemClickListener.onItemClick(v, position);
                }
            });
    }

    @Override
    public H onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(resource, parent, false);
        return onCreateViewHolder(itemView);
    }

    public abstract void onBindViewHolder(H vh, T data, int position);

    public abstract H onCreateViewHolder(View itemView);

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.itemClickListener = listener;
    }

    public void setDatas(List<T> datas) {
        this.datas = datas;
        this.notifyDataSetChanged();
    }

    public List<T> getDatas() {
        return datas;
    }

    public void addData(T t) {
        if (datas == null)
            datas = new ArrayList<T>();
        datas.add(t);
        this.notifyItemInserted(datas.size());
    }

}
