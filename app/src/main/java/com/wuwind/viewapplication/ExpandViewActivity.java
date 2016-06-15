package com.wuwind.viewapplication;

import android.widget.TextView;

import com.wuwind.corelibrary.base.CoreActivity;
import com.wuwind.corelibrary.utils.LogUtil;
import com.wuwind.corelibrary.utils.ManifestConfig;
import com.wuwind.corelibrary.utils.PackageUtil;
import com.wuwind.corelibrary.utils.ToastUtil;
import com.wuwind.viewapplication.widget.ShimmerTextView;

import butterknife.Bind;

public class ExpandViewActivity extends CoreActivity {


    @Bind(R.id.last2View)
    TextView last2View;
    @Bind(R.id.lastView)
    ShimmerTextView lastView;

    @Override
    protected int setContentLayoutId() {
        return R.layout.activity_expand_view;
    }

    @Override
    protected void initView() {

        boolean isDebug = ManifestConfig.getBooleanMetaValue(this, "isDebug");

        ToastUtil.show(this, isDebug+"");

        LogUtil.e(0, isDebug+"");

        LogUtil.e(0, PackageUtil.getMyUUID(this));
        lastView.setDisplay(ShimmerTextView.bottom, 0xaaffff00, 100);
//        shimmer.setBaseAlpha(0);
//        shimmer.setDuration(2000);
//        shimmer.setDropoff(0.1f);
//        shimmer.setIntensity(0.35f);
//
//        shimmer.setBaseAlpha(0.1f);
//        shimmer.setDropoff(0.1f);
//        shimmer.setTilt(0);
//        shimmer.setDuration(2000);
//        shimmer.setMaskShape(ShimmerFrameLayout.MaskShape.LINEAR);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    protected void initMonitor() {

    }

}
