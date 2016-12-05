package com.droi.sample;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.droi.mobileads.DroiErrorCode;
import com.droi.mobileads.DroiView;

import static com.droi.mobileads.DroiView.BannerAdListener;
import static com.droi.sample.Utils.hideSoftKeyboard;
import static com.droi.sample.Utils.logToast;

/**
 * A base class for creating banner style ads with various height and width dimensions.
 * <p>
 * A subclass simply needs to specify the height and width of the ad in pixels, and this class will
 * inflate a layout containing a programmatically rescaled {@link DroiView} that will be used to
 * display the ad.
 */
public abstract class AbstractBannerDetailFragment extends Fragment implements BannerAdListener {
    private DroiView mDroiView;
    private DroiSampleAdUnit mDroiSampleAdUnit;

    public abstract int getWidth();

    public abstract int getHeight();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        final View view = inflater.inflate(R.layout.banner_detail_fragment, container, false);
        final DetailFragmentViewHolder views = DetailFragmentViewHolder.fromView(view);

        mDroiSampleAdUnit = DroiSampleAdUnit.fromBundle(getArguments());
        mDroiView = (DroiView) view.findViewById(R.id.banner_droiview);
        LinearLayout.LayoutParams layoutParams =
                (LinearLayout.LayoutParams) mDroiView.getLayoutParams();
        layoutParams.width = getWidth();
        layoutParams.height = getHeight();
        mDroiView.setLayoutParams(layoutParams);

        hideSoftKeyboard(views.mKeywordsField);

        final String adUnitId = mDroiSampleAdUnit.getAdUnitId();
        views.mDescriptionView.setText(mDroiSampleAdUnit.getDescription());
        views.mAdUnitIdView.setText(adUnitId);
        views.mLoadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String keywords = views.mKeywordsField.getText().toString();
                loadDroiView(adUnitId, keywords);
            }
        });
        mDroiView.setBannerAdListener(this);
        loadDroiView(adUnitId, null);

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        if (mDroiView != null) {
            mDroiView.destroy();
            mDroiView = null;
        }
    }

    private void loadDroiView(final String adUnitId, final String keywords) {
        mDroiView.setAdUnitId(adUnitId);
        mDroiView.setKeywords(keywords);
        mDroiView.loadAd();
    }

    private String getName() {
        if (mDroiSampleAdUnit == null) {
            return DroiSampleAdUnit.AdType.BANNER.getName();
        }
        return mDroiSampleAdUnit.getHeaderName();
    }

    // BannerAdListener
    @Override
    public void onBannerLoaded(DroiView banner) {
        logToast(getActivity(), getName() + " loaded.");
    }

    @Override
    public void onBannerFailed(DroiView banner, DroiErrorCode errorCode) {
        final String errorMessage = (errorCode != null) ? errorCode.toString() : "";
        logToast(getActivity(), getName() + " failed to load: " + errorMessage);
    }

    @Override
    public void onBannerClicked(DroiView banner) {
        logToast(getActivity(), getName() + " clicked.");
    }

    @Override
    public void onBannerExpanded(DroiView banner) {
        logToast(getActivity(), getName() + " expanded.");
    }

    @Override
    public void onBannerCollapsed(DroiView banner) {
        logToast(getActivity(), getName() + " collapsed.");
    }
}
