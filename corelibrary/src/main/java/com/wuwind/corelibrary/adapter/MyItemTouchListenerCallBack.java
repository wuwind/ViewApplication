package com.wuwind.corelibrary.adapter;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.support.v7.widget.helper.ItemTouchHelper;

public class MyItemTouchListenerCallBack extends ItemTouchHelper.Callback {

	@Override
	public int getMovementFlags(RecyclerView arg0, ViewHolder arg1) {
		
		int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
		int swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END;
		return makeMovementFlags(dragFlags, swipeFlags);
	}

	@Override
	public boolean onMove(RecyclerView arg0, ViewHolder arg1, ViewHolder arg2) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onSwiped(ViewHolder arg0, int arg1) {
		// TODO Auto-generated method stub

	}
	
	@Override
	public boolean isLongPressDragEnabled() {
		return super.isLongPressDragEnabled();
	}
	
	@Override
	public boolean isItemViewSwipeEnabled() {
		// TODO Auto-generated method stub
		return true;
	}
	
	public interface ItemTouchHelperAdapter {
	    void onItemMove(int fromPosition, int toPosition);
	    void onItemDismiss(int position);
	}
	

}
