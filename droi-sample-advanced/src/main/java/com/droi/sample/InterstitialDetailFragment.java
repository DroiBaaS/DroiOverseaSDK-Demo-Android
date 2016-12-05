package com.droi.sample;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.droi.mobileads.DroiErrorCode;
import com.droi.mobileads.DroiInterstitial;

import static com.droi.mobileads.DroiInterstitial.InterstitialAdListener;
import static com.droi.sample.Utils.hideSoftKeyboard;
import static com.droi.sample.Utils.logToast;

public class InterstitialDetailFragment extends Fragment implements InterstitialAdListener {
    private DroiInterstitial mDroiInterstitial;
    private Button mShowButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        final DroiSampleAdUnit adConfiguration =
                DroiSampleAdUnit.fromBundle(getArguments());
        final View view = inflater.inflate(R.layout.interstitial_detail_fragment, container, false);
        final DetailFragmentViewHolder views = DetailFragmentViewHolder.fromView(view);
        hideSoftKeyboard(views.mKeywordsField);

        final String adUnitId = adConfiguration.getAdUnitId();
        views.mDescriptionView.setText(adConfiguration.getDescription());
        views.mAdUnitIdView.setText(adUnitId);
        views.mLoadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mShowButton.setEnabled(false);
                if (mDroiInterstitial == null) {
                    mDroiInterstitial = new DroiInterstitial(getActivity(), adUnitId);
                    mDroiInterstitial.setInterstitialAdListener(InterstitialDetailFragment.this);
                }
                final String keywords = views.mKeywordsField.getText().toString();
                mDroiInterstitial.setKeywords(keywords);
                mDroiInterstitial.load();
            }
        });
        mShowButton = (Button) view.findViewById(R.id.interstitial_show_button);
        mShowButton.setEnabled(false);
        mShowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDroiInterstitial.show();
            }
        });

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        if (mDroiInterstitial != null) {
            mDroiInterstitial.destroy();
            mDroiInterstitial = null;
        }
    }

    // InterstitialAdListener implementation
    @Override
    public void onInterstitialLoaded(DroiInterstitial interstitial) {
        mShowButton.setEnabled(true);
        logToast(getActivity(), "Interstitial loaded.");
    }

    @Override
    public void onInterstitialFailed(DroiInterstitial interstitial, DroiErrorCode errorCode) {
        final String errorMessage = (errorCode != null) ? errorCode.toString() : "";
        logToast(getActivity(), "Interstitial failed to load: " + errorMessage);
    }

    @Override
    public void onInterstitialShown(DroiInterstitial interstitial) {
        mShowButton.setEnabled(false);
        logToast(getActivity(), "Interstitial shown.");
    }

    @Override
    public void onInterstitialClicked(DroiInterstitial interstitial) {
        logToast(getActivity(), "Interstitial clicked.");
    }

    @Override
    public void onInterstitialDismissed(DroiInterstitial interstitial) {
        logToast(getActivity(), "Interstitial dismissed.");
    }
}
