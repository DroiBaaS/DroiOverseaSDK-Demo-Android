package com.droi.sample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.droi.nativeads.DroiAdAdapter;
import com.droi.nativeads.DroiNativeAdLoadedListener;
import com.droi.nativeads.DroiNativeAdPositioning;
import com.droi.nativeads.DroiStaticNativeAdRenderer;
import com.droi.nativeads.RequestParameters;
import com.droi.nativeads.ViewBinder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NativeActivity extends AppCompatActivity {
    ListView mylist;
    SimpleAdapter adapter;
    DroiAdAdapter mAdAdapter;
    RequestParameters myRequestParameters;
    String TAG = "droi";
    static int ITEM_COUNT = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_native);
        mylist = (ListView)findViewById(R.id.mylist);

        adapter = new SimpleAdapter(NativeActivity.this,
                getData(),
                R.layout.liet_item,
                new String[]{"title","info"},
                new int[]{R.id.title,R.id.content});
        createNativeAd();
    }

    private List<Map<String, Object>> getData() {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

        for(int i=0;i<ITEM_COUNT;i++){
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("title", "title"+i);
            map.put("info", "item"+i);
            list.add(map);
        }

        return list;
    }


    private  void createNativeAd(){
        //create binder
        ViewBinder viewBinder = new ViewBinder.Builder(R.layout.native_ad_list_item)
                .mainImageId(R.id.native_main_image)
                .iconImageId(R.id.native_icon_image)
                .titleId(R.id.native_title)
                .textId(R.id.native_text)
                .privacyInformationIconImageId(R.id.native_privacy_information_icon_image)
                .build();

        //createrender
        DroiStaticNativeAdRenderer adRenderer = new DroiStaticNativeAdRenderer(viewBinder);

        //get position
        DroiNativeAdPositioning.DroiServerPositioning adPositioning =
                DroiNativeAdPositioning.serverPositioning();

        //crerate adapter
        mAdAdapter = new DroiAdAdapter(this, adapter, adPositioning);

        //register render
        mAdAdapter.registerAdRenderer(adRenderer);
        mAdAdapter.setAdLoadedListener(new DroiNativeAdLoadedListener() {
            @Override
            public void onAdLoaded(int position) {
                Log.d(TAG,"native ad load at position:"+position);
            }

            @Override
            public void onAdRemoved(int position) {
                Log.d(TAG,"native ad onAdRemoved at position:"+position);
            }
        });
        mylist.setAdapter(mAdAdapter);
    }

    @Override
    public void onResume() {
        // Set up your request parameters
        myRequestParameters = new RequestParameters.Builder()
                .build();

        // Request ads when the user returns to this activity.
        mAdAdapter.loadAds("1DoaGOl0XD", myRequestParameters);
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        mAdAdapter.destroy();
        super.onDestroy();
    }
}
