package com.droi.sample;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.droi.mobileads.DroiErrorCode;
import com.droi.mobileads.DroiInterstitial;
import com.droi.mobileads.DroiView;

public class MainActivity extends AppCompatActivity implements DroiView.BannerAdListener, View.OnClickListener ,DroiInterstitial.InterstitialAdListener{
    
    private DroiView droiView;
    private DroiInterstitial mInterstitial;
    String TAG="Droi";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        droiView = (DroiView) findViewById(R.id.adview);
        droiView.setAdUnitId("3emqDaOGLJ");
        droiView.loadAd();
        droiView.setBannerAdListener(this);

        findViewById(R.id.droi_interstitial_button).setOnClickListener(this);
        findViewById(R.id.droi_native_button).setOnClickListener(this);
    }

    @Override
    public void onBannerLoaded(DroiView banner) {
        Log.d(TAG,"droi bannner onBannerLoaded");
    }

    @Override
    public void onBannerFailed(DroiView banner, DroiErrorCode errorCode) {
        Log.d(TAG,"droi bannner onBannerFailed"+errorCode.toString());
    }

    @Override
    public void onBannerClicked(DroiView banner) {
        Log.d(TAG,"droi bannner onBannerClicked");
    }

    @Override
    public void onBannerExpanded(DroiView banner) {
        Log.d(TAG,"droi bannner onBannerExpanded");
    }

    @Override
    public void onBannerCollapsed(DroiView banner) {
        Log.d(TAG,"droi bannner onBannerCollapsed");
    }

    private void createinterstitial(){
        Log.d(TAG,"create interstitial ad");
        mInterstitial = new DroiInterstitial(this, "2KUVyBUhUL");
        mInterstitial.setInterstitialAdListener(MainActivity.this);
        mInterstitial.load();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if(id == R.id.droi_interstitial_button){
            if(mInterstitial == null){
                createinterstitial();
            }else{
                mInterstitial.load();
            }
        }else if(id == R.id.droi_native_button){
            Intent droi_native = new Intent(MainActivity.this,NativeActivity.class);
            droi_native.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(droi_native);
        }

    }

    @Override
    public void onInterstitialLoaded(DroiInterstitial interstitial) {
        Log.d(TAG,"droi onInterstitialLoaded");
        if(interstitial.isReady()){
            mInterstitial.show();
        }
    }

    @Override
    public void onInterstitialFailed(DroiInterstitial interstitial, DroiErrorCode errorCode) {
        Log.d(TAG,"droi onInterstitialFailed");
    }

    @Override
    public void onInterstitialShown(DroiInterstitial interstitial) {
        Log.d(TAG,"droi onInterstitialShown");
    }

    @Override
    public void onInterstitialClicked(DroiInterstitial interstitial) {
        Log.d(TAG,"droi onInterstitialClicked");
    }

    @Override
    public void onInterstitialDismissed(DroiInterstitial interstitial) {
        Log.d(TAG,"droi onInterstitialDismissed");
        interstitial = null;
    }

    @Override
    protected void onDestroy() {
        if(mInterstitial != null){
            mInterstitial.destroy();
            mInterstitial = null;
        }

        if(droiView!= null){
            droiView.destroy();
            droiView = null;
        }

        super.onDestroy();
    }
}
