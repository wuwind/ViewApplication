package com.wuwind.corelibrary.widget;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewConfiguration;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.wuwind.corelibrary.R;
import com.wuwind.corelibrary.utils.LogUtil;


/**
 * 可进行下拉刷新的自定义控件。
 *
 * @author guolin
 */
public class RecyclerRefreshableView2 extends LinearLayout implements OnTouchListener {

    public static final int STATUS_PULL_TO_REFRESH = 0;//下拉状态

    public static final int STATUS_RELEASE_TO_REFRESH = 1;//释放立即刷新状态

    public static final int STATUS_REFRESHING = 2;//正在刷新状态

    public static final int STATUS_NORMAL = 3;//刷新完成或未刷新状态

    public static final int STATUS_RELEASE_TO_LOAD = 4;//刷新完成或未刷新状态

    public static final int STATUS_LOAD_FINISHED = 5;//刷新完成或未刷新状态

    public static final int STATUS_LOADING = 6;//刷新完成或未刷新状态

    public static final int STATUS_MOVE_DOWN = 7;//刷新完成或未刷新状态

    public static final int STATUS_MOVE_UP = 8;//刷新完成或未刷新状态

    public static final int STATUS_PULL_TO_LOAD = 9;//刷新完成或未刷新状态

    public static final int SCROLL_SPEED = -20;//下拉头部回滚的速度

    public static final long ONE_MINUTE = 60 * 1000;//一分钟的毫秒值，用于判断上次的更新时间

    public static final long ONE_HOUR = 60 * ONE_MINUTE;//一小时的毫秒值，用于判断上次的更新时间

    public static final long ONE_DAY = 24 * ONE_HOUR;//一天的毫秒值，用于判断上次的更新时间

    public static final long ONE_MONTH = 30 * ONE_DAY;//一月的毫秒值，用于判断上次的更新时间

    public static final long ONE_YEAR = 12 * ONE_MONTH;//一年的毫秒值，用于判断上次的更新时间

    private static final String UPDATED_AT = "updated_at";//上次更新时间的字符串常量，用于作为SharedPreferences的键值

    private PullToRefreshListener refreshListener;//下拉刷新的回调接口
    private PullToLoadListener loadListener;//上拉加载的回调接口

    private View header, footer;//下拉头的View

    private RecyclerView recyclerView;//需要去下拉刷新的RecyclerView

    private ProgressBar progressBar;//刷新时显示的进度条

    private ImageView arrow;//指示下拉和释放的箭头

    private TextView description;//指示下拉和释放的文字描述

    private TextView updateAt;//上次更新时间的文字描述

    private MarginLayoutParams headerLayoutParams, footerLayoutParams, recyclerViewParams;//下拉头的布局参数

    private long lastUpdateTime;//上次更新时间的毫秒值

    private int mId = -1;//为了防止不同界面的下拉刷新在上次更新时间上互相有冲突，使用id来做区分

    private int hideHeaderHeight, footerHeight;//下拉头的高度

    /**
     * 当前处理什么状态，可选值有STATUS_PULL_TO_REFRESH, STATUS_RELEASE_TO_REFRESH,
     * STATUS_REFRESHING 和 STATUS_NORMAL
     */
    private int currentStatus = STATUS_NORMAL;

    private int lastStatus = currentStatus;//记录上一次的状态是什么，避免进行重复操作

    private float yDown;//手指按下时的屏幕纵坐标

    private int touchSlop;//在被判定为滚动之前用户手指可以移动的最大值。

    private boolean loadOnce;//是否已加载过一次layout，这里onLayout中的初始化只需加载一次

    private boolean ableToPull;//当前是否可以下拉，只有RecyclerView滚动到头的时候才允许下拉

    private LinearLayoutManager mLayoutManager;

    private  boolean isLoading;
    /**
     * 下拉刷新控件的构造函数，会在运行时动态添加一个下拉头的布局。
     *
     * @param context
     * @param attrs
     */
    public RecyclerRefreshableView2(Context context, AttributeSet attrs) {
        super(context, attrs);
        touchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
        setOrientation(VERTICAL);
    }

    private void initHeader() {
        header = getChildAt(0);
        progressBar = (ProgressBar) header.findViewById(R.id.progress_bar);
        arrow = (ImageView) header.findViewById(R.id.arrow);
        description = (TextView) header.findViewById(R.id.description);
        updateAt = (TextView) header.findViewById(R.id.updated_at);
    }

    /**
     * 进行一些关键性的初始化操作，比如：将下拉头向上偏移进行隐藏，给RecyclerView注册touch事件。
     */
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if (changed && !loadOnce) {
            initHeader();
            hideHeaderHeight = -header.getMeasuredHeight();
            headerLayoutParams = (MarginLayoutParams) header.getLayoutParams();
            headerLayoutParams.topMargin = hideHeaderHeight;
            recyclerView = (RecyclerView) getChildAt(1);
            recyclerView.setOnTouchListener(this);
            mLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
            recyclerView.addOnScrollListener(new MScrollListener());
            footer = getChildAt(2);
            footerHeight = -footer.getMeasuredHeight();
            footerLayoutParams = (MarginLayoutParams) footer.getLayoutParams();
            footerLayoutParams.bottomMargin = footerHeight;
            footer.setLayoutParams(footerLayoutParams);
            header.setLayoutParams(headerLayoutParams);
//            loadOnce = true;
            int recyclerViewMeasuredHeight = recyclerView.getMeasuredHeight() - hideHeaderHeight - footerHeight;
            LogUtil.e("recyclerViewMeasuredHeight:" + recyclerViewMeasuredHeight);
            int recyclerViewMeasuredWidth = recyclerView.getMeasuredWidth();
            LogUtil.e("recyclerViewMeasuredWidth:" + recyclerViewMeasuredWidth);
            LayoutParams layoutParams = (LayoutParams) recyclerView.getLayoutParams();
            layoutParams.width = recyclerViewMeasuredWidth;
            layoutParams.height = recyclerViewMeasuredHeight;
            recyclerView.setLayoutParams(layoutParams);
            recyclerViewParams = (MarginLayoutParams) recyclerView.getLayoutParams();
        }
    }

    private class MScrollListener extends RecyclerView.OnScrollListener {
        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            int mItemCount = mLayoutManager.getItemCount();  //总条数
            int lastVisiblePos = mLayoutManager.findLastVisibleItemPosition();
            View lastChild = mLayoutManager.findViewByPosition(lastVisiblePos);
            if (lastVisiblePos == mItemCount - 1 && lastChild.getBottom() == mLayoutManager.getHeight()) {
                if(isLoading)
                    return;
                LogUtil.e("加载" + recyclerView.getHeight());
                footerLayoutParams.bottomMargin = 0;
                footer.setLayoutParams(footerLayoutParams);
                recyclerViewParams.topMargin = footerHeight;
                recyclerView.setLayoutParams(recyclerViewParams);
                isLoading = true;
            } else {
                if(!isLoading)
                    return;
                LogUtil.e("隐藏加载");
                footerLayoutParams.bottomMargin = footerHeight;
                footer.setLayoutParams(footerLayoutParams);
                recyclerViewParams.topMargin = 0;
                recyclerView.setLayoutParams(recyclerViewParams);
                isLoading = false;
            }
        }
    }


    /**
     * 当RecyclerView被触摸时调用，其中处理了各种下拉刷新的具体逻辑。
     */
    @Override
    public boolean onTouch(View v, MotionEvent event) {

        int action = event.getAction();

        if (action == MotionEvent.ACTION_DOWN) {
            yDown = event.getRawY();
        } else if (action == MotionEvent.ACTION_MOVE) {

            setIsPullAble(event);

            float yMove = event.getRawY();
            int distance = (int) (yMove - yDown);
            if (Math.abs(distance) < touchSlop) {
                return false;
            }

            int currentMoveStatus = -1;
            if (distance > 0) {
                currentMoveStatus = STATUS_MOVE_DOWN;
            } else if (distance < 0) {
                currentMoveStatus = STATUS_MOVE_UP;
            }

            switch (currentStatus) {
                case STATUS_NORMAL:
                    if (currentMoveStatus == STATUS_MOVE_DOWN && ableToPull) {
                        currentStatus = STATUS_PULL_TO_REFRESH;
                        return true;
                    }
                    break;
                //可刷新
                case STATUS_PULL_TO_REFRESH:
                case STATUS_RELEASE_TO_REFRESH:
                    LogUtil.e("可刷新");
                    if (currentMoveStatus == STATUS_MOVE_DOWN) {
                        LogUtil.e("下滑");
                        refresh(distance);
                        refreshHeader();
                        return true;
                    } else {
                        LogUtil.e("山花");
                    }
                    //可加载
                    break;
                case STATUS_REFRESHING:
                    LogUtil.e("正在刷新");
                    if (currentMoveStatus == STATUS_MOVE_DOWN) {
                        if (headerLayoutParams.topMargin > 0 || !ableToPull)
                            return false;
                        downRefresh(distance);
                    } else if (currentMoveStatus == STATUS_MOVE_UP) {
                        if (headerLayoutParams.topMargin <= hideHeaderHeight)
                            return false;
                        upRefresh(distance);
                        LogUtil.e("山花");
                    }
                    return true;
                default:
                    break;
            }

        } else if (action == MotionEvent.ACTION_UP) {
            return actionUp();
        }
        return false;
    }

    private void refreshHeader() {
        if (currentStatus == STATUS_PULL_TO_REFRESH || currentStatus == STATUS_RELEASE_TO_REFRESH) {
            updateHeaderView();
            // 当前正处于下拉或释放状态，要让RecyclerView失去焦点，否则被点击的那一项会一直处于选中状态
            recyclerView.setPressed(false);
            recyclerView.setFocusable(false);
            recyclerView.setFocusableInTouchMode(false);
            lastStatus = currentStatus;
            // 当前正处于下拉或释放状态，通过返回true屏蔽掉RecyclerView的滚动事件
        }
    }

    private void downRefresh(int distance) {
        LogUtil.e("distance" + distance);
        LogUtil.e("topMargin" + headerLayoutParams.topMargin);
        headerLayoutParams.topMargin = distance + hideHeaderHeight;
        if (headerLayoutParams.topMargin > 0)
            headerLayoutParams.topMargin = 0;
        header.setLayoutParams(headerLayoutParams);
    }

    private boolean upRefresh(int distance) {
        // 通过偏移下拉头的topMargin值，来实现下拉效果
        headerLayoutParams.topMargin = distance;
        if (headerLayoutParams.topMargin < hideHeaderHeight)
            headerLayoutParams.topMargin = hideHeaderHeight;
        header.setLayoutParams(headerLayoutParams);
        return true;
    }

    //刷新
    private void refresh(int distance) {
        LogUtil.e("刷新");
        if (headerLayoutParams.topMargin > 0) {
            currentStatus = STATUS_RELEASE_TO_REFRESH;
        } else {
            currentStatus = STATUS_PULL_TO_REFRESH;
        }
        // 通过偏移下拉头的topMargin值，来实现下拉效果
        headerLayoutParams.topMargin = (distance / 2) + hideHeaderHeight;
        header.setLayoutParams(headerLayoutParams);
    }


    private boolean actionUp() {
        switch (currentStatus) {
            case STATUS_RELEASE_TO_REFRESH:
                // 松手时如果是释放立即刷新状态，就去调用正在刷新的任务
                currentStatus = STATUS_REFRESHING;
                new RefreshingTask().execute();
                return true;
            case STATUS_REFRESHING:
                if (headerLayoutParams.topMargin < 0) {
                    //隐藏
                    handler.sendEmptyMessage(0);
                } else {
                    handler.sendEmptyMessage(1);
                }
                return false;
            case STATUS_PULL_TO_REFRESH:
                // 松手时如果是下拉状态，就去调用隐藏下拉头的任务
                new HideHeaderTask().execute();
                return true;
            default:
                return false;
        }
    }


    /**
     * 给下拉刷新控件注册一个监听器。
     *
     * @param listener 监听器的实现。
     * @param id       为了防止不同界面的下拉刷新在上次更新时间上互相有冲突， 请不同界面在注册下拉刷新监听器时一定要传入不同的id。
     */
    public void setOnRefreshListener(PullToRefreshListener listener, int id) {
        refreshListener = listener;
        mId = id;
    }

    public void setOnLoadListener(PullToLoadListener listener, int id) {
        loadListener = listener;
        mId = id;
    }

    /**
     * 当所有的刷新逻辑完成后，记录调用一下，否则你的RecyclerView将一直处于正在刷新状态。
     */
    public void finishRefreshing() {
        currentStatus = STATUS_NORMAL;
//		preferences.edit().putLong(UPDATED_AT + mId, System.currentTimeMillis()).commit();
        new HideHeaderTask().execute();
    }

    /**
     * 当所有的刷新逻辑完成后，记录调用一下，否则你的RecyclerView将一直处于正在刷新状态。
     */
    public void finishLoading() {
        currentStatus = STATUS_LOAD_FINISHED;
    }

    /**
     * 根据当前RecyclerView的滚动状态来设定 {@link #ableToPull}
     * 的值，每次都需要在onTouch中第一个执行，这样可以判断出当前应该是滚动RecyclerView，还是应该进行下拉。
     *
     * @param event
     */
    private void setIsPullAble(MotionEvent event) {
        View firstChild = recyclerView.getChildAt(0);
        if (firstChild != null) {
            int firstVisiblePos = mLayoutManager.findFirstVisibleItemPosition();
            LogUtil.e(2, "firstChild.getTop()" + firstChild.getTop());
            if (firstVisiblePos == 0 && firstChild.getTop() == 0) {
                LogUtil.e("|可以刷新");
                if (!ableToPull) {
                    yDown = event.getRawY();
                }
                // 如果首个元素的上边缘，距离父布局值为0，就说明RecyclerView滚动到了最顶部，此时应该允许下拉刷新
                ableToPull = true;
                return;
            }
            ableToPull = false;
            return;
        } else {
            // 如果RecyclerView中没有元素，也应该允许下拉刷新
            ableToPull = true;
            return;
        }
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    //隐藏
                    headerLayoutParams.topMargin += SCROLL_SPEED;
                    if (headerLayoutParams.topMargin <= hideHeaderHeight) {
                        headerLayoutParams.topMargin = hideHeaderHeight;
                        header.setLayoutParams(headerLayoutParams);
                        break;
                    }
                    header.setLayoutParams(headerLayoutParams);
                    sendEmptyMessageAtTime(0, 10);
                    break;
                case 1:
                    //出现
                    headerLayoutParams.topMargin += SCROLL_SPEED;
                    if (headerLayoutParams.topMargin <= 0) {
                        headerLayoutParams.topMargin = 0;
                        header.setLayoutParams(headerLayoutParams);
                        break;
                    }
                    header.setLayoutParams(headerLayoutParams);
                    sendEmptyMessageAtTime(1, 10);
                    break;

                default:
                    break;
            }
        }
    };

    /**
     * 更新下拉头中的信息。
     */
    private void updateHeaderView() {
        if (lastStatus != currentStatus) {
            if (currentStatus == STATUS_PULL_TO_REFRESH) {
                description.setText(getResources().getString(R.string.pull_to_refresh));
                arrow.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
                rotateArrow();
            } else if (currentStatus == STATUS_RELEASE_TO_REFRESH) {
                description.setText(getResources().getString(R.string.release_to_refresh));
                arrow.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
                rotateArrow();
            } else if (currentStatus == STATUS_REFRESHING) {
                description.setText(getResources().getString(R.string.refreshing));
                progressBar.setVisibility(View.VISIBLE);
                arrow.clearAnimation();
                arrow.setVisibility(View.GONE);
            }
            refreshUpdatedAtValue();
        }
    }

    /**
     * 根据当前的状态来旋转箭头。
     */
    private void rotateArrow() {
        float pivotX = arrow.getWidth() / 2f;
        float pivotY = arrow.getHeight() / 2f;
        float fromDegrees = 0f;
        float toDegrees = 0f;
        if (currentStatus == STATUS_PULL_TO_REFRESH) {
            fromDegrees = 180f;
            toDegrees = 360f;
        } else if (currentStatus == STATUS_RELEASE_TO_REFRESH) {
            fromDegrees = 0f;
            toDegrees = 180f;
        }
        RotateAnimation animation = new RotateAnimation(fromDegrees, toDegrees, pivotX, pivotY);
        animation.setDuration(100);
        animation.setFillAfter(true);
        arrow.startAnimation(animation);
    }

    /**
     * 刷新下拉头中上次更新时间的文字描述。
     */
    private void refreshUpdatedAtValue() {
//		lastUpdateTime = preferences.getLong(UPDATED_AT + mId, -1);
        long currentTime = System.currentTimeMillis();
        long timePassed = currentTime - lastUpdateTime;
        long timeIntoFormat;
        String updateAtValue;
        if (lastUpdateTime == -1) {
            updateAtValue = getResources().getString(R.string.not_updated_yet);
        } else if (timePassed < 0) {
            updateAtValue = getResources().getString(R.string.time_error);
        } else if (timePassed < ONE_MINUTE) {
            updateAtValue = getResources().getString(R.string.updated_just_now);
        } else if (timePassed < ONE_HOUR) {
            timeIntoFormat = timePassed / ONE_MINUTE;
            String value = timeIntoFormat + "分钟";
            updateAtValue = String.format(getResources().getString(R.string.updated_at), value);
        } else if (timePassed < ONE_DAY) {
            timeIntoFormat = timePassed / ONE_HOUR;
            String value = timeIntoFormat + "小时";
            updateAtValue = String.format(getResources().getString(R.string.updated_at), value);
        } else if (timePassed < ONE_MONTH) {
            timeIntoFormat = timePassed / ONE_DAY;
            String value = timeIntoFormat + "天";
            updateAtValue = String.format(getResources().getString(R.string.updated_at), value);
        } else if (timePassed < ONE_YEAR) {
            timeIntoFormat = timePassed / ONE_MONTH;
            String value = timeIntoFormat + "个月";
            updateAtValue = String.format(getResources().getString(R.string.updated_at), value);
        } else {
            timeIntoFormat = timePassed / ONE_YEAR;
            String value = timeIntoFormat + "年";
            updateAtValue = String.format(getResources().getString(R.string.updated_at), value);
        }
        updateAt.setText(updateAtValue);
    }

    /**
     * 正在刷新的任务，在此任务中会去回调注册进来的下拉刷新监听器。
     */
    class RefreshingTask extends AsyncTask<Void, Integer, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            int topMargin = headerLayoutParams.topMargin;
            while (true) {
                topMargin = topMargin + SCROLL_SPEED;
                if (topMargin <= 0) {
                    topMargin = 0;
                    break;
                }
                publishProgress(topMargin);
                SystemClock.sleep(10);
            }
            publishProgress(0);
            if (refreshListener != null) {
                refreshListener.onRefresh();
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... topMargin) {
            updateHeaderView();
            headerLayoutParams.topMargin = topMargin[0];
            header.setLayoutParams(headerLayoutParams);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    finishRefreshing();
                }
            }, 5000);
        }
    }

    /**
     * 隐藏下拉头的任务，当未进行下拉刷新或下拉刷新完成后，此任务将会使下拉头重新隐藏。
     */
    class HideHeaderTask extends AsyncTask<Void, Integer, Integer> {

        @Override
        protected Integer doInBackground(Void... params) {
            int topMargin = headerLayoutParams.topMargin;
            while (true) {
                topMargin = topMargin + SCROLL_SPEED;
                if (topMargin <= hideHeaderHeight) {
                    topMargin = hideHeaderHeight;
                    break;
                }
                publishProgress(topMargin);
                SystemClock.sleep(10);
            }
            return topMargin;
        }

        @Override
        protected void onProgressUpdate(Integer... topMargin) {
            headerLayoutParams.topMargin = topMargin[0];
            header.setLayoutParams(headerLayoutParams);
        }

        @Override
        protected void onPostExecute(Integer topMargin) {
            headerLayoutParams.topMargin = topMargin;
            header.setLayoutParams(headerLayoutParams);
            currentStatus = STATUS_NORMAL;
        }
    }

    /**
     * 正在刷新的任务，在此任务中会去回调注册进来的下拉刷新监听器。
     */
//    class LoadingTask extends AsyncTask<Void, Integer, Void> {
//
//        @Override
//        protected Void doInBackground(Void... params) {
////            int bottomMargin = footerLayoutParams.bottomMargin;
//            while (true) {
//                bottomMargin = bottomMargin + SCROLL_SPEED;
//                if (bottomMargin <= 0) {
//                    bottomMargin = 0;
//                    break;
//                }
//                publishProgress(bottomMargin);
//                SystemClock.sleep(10);
//            }
//            currentStatus = STATUS_LOADING;
//            publishProgress(0);
//            if (loadListener != null) {
//                loadListener.onLoad();
//            }
//            return null;
//        }
//
//        @Override
//        protected void onProgressUpdate(Integer... bottomMargin) {
//            updateHeaderView();
//            footerLayoutParams.bottomMargin = bottomMargin[0];
//            header.setLayoutParams(headerLayoutParams);
//            recyclerViewParams.topMargin = -bottomMargin[0] + footerHeight;
//            recyclerView.setLayoutParams(recyclerViewParams);
//        }
//
//        @Override
//        protected void onPostExecute(Void aVoid) {
//            new Handler().postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    finishLoading();
//                }
//            }, 5000);
//        }
//    }

//    class HideFooterTask extends AsyncTask<Void, Integer, Integer> {
//        @Override
//        protected Integer doInBackground(Void... params) {
//            int bottomMargin = footerLayoutParams.bottomMargin;
//            while (true) {
//                bottomMargin = bottomMargin + SCROLL_SPEED;
//                if (bottomMargin <= footerHeight) {
//                    bottomMargin = footerHeight;
//                    break;
//                }
//                publishProgress(bottomMargin);
//                SystemClock.sleep(10);
//            }
//            return bottomMargin;
//        }
//
//        @Override
//        protected void onProgressUpdate(Integer... values) {
//            footerLayoutParams.bottomMargin = values[0];
//            footer.setLayoutParams(footerLayoutParams);
//            recyclerViewParams.topMargin = -(values[0] - footerHeight);
//            recyclerView.setLayoutParams(recyclerViewParams);
//        }
//
//        @Override
//        protected void onPostExecute(Integer bottomMargin) {
//            footerLayoutParams.bottomMargin = bottomMargin;
//            footer.setLayoutParams(footerLayoutParams);
//            currentStatus = STATUS_NORMAL;
//            recyclerViewParams.topMargin = 0;
//            recyclerView.setLayoutParams(recyclerViewParams);
//        }
//    }

    /**
     * 下拉刷新的监听器，使用下拉刷新的地方应该注册此监听器来获取刷新回调。
     */
    public interface PullToRefreshListener {
        void onRefresh();
    }

    public interface PullToLoadListener {
        void onLoad();
    }

}
