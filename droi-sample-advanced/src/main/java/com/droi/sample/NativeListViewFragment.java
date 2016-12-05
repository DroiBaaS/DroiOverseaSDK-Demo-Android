package com.droi.sample;

import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.droi.nativeads.DroiAdAdapter;
import com.droi.nativeads.DroiStaticNativeAdRenderer;
import com.droi.nativeads.RequestParameters;
import com.droi.nativeads.ViewBinder;

import java.util.EnumSet;

import static com.droi.nativeads.DroiNativeAdPositioning.DroiServerPositioning;
import static com.droi.nativeads.RequestParameters.NativeAdAsset;

public class NativeListViewFragment extends Fragment {
    private DroiAdAdapter mAdAdapter;
    private DroiSampleAdUnit mAdConfiguration;
    private RequestParameters mRequestParameters;

    @Override
    public View onCreateView(final LayoutInflater inflater,
            final ViewGroup container,
            final Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        mAdConfiguration = DroiSampleAdUnit.fromBundle(getArguments());
        final View view = inflater.inflate(R.layout.native_list_view_fragment, container, false);
        final ListView listView = (ListView) view.findViewById(R.id.native_list_view);
        final DetailFragmentViewHolder views = DetailFragmentViewHolder.fromView(view);
        views.mLoadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // If your app already has location access, include it here.
                final Location location = null;
                final String keywords = views.mKeywordsField.getText().toString();

                // Setting desired assets on your request helps native ad networks and bidders
                // provide higher-quality ads.
                final EnumSet<NativeAdAsset> desiredAssets = EnumSet.of(
                        NativeAdAsset.TITLE,
                        NativeAdAsset.TEXT,
                        NativeAdAsset.ICON_IMAGE,
                        NativeAdAsset.MAIN_IMAGE,
                        NativeAdAsset.CALL_TO_ACTION_TEXT);

                mRequestParameters = new RequestParameters.Builder()
                .location(location)
                .keywords(keywords)
                .desiredAssets(desiredAssets)
                .build();

        mAdAdapter.loadAds(mAdConfiguration.getAdUnitId(), mRequestParameters);
    }
});
        final String adUnitId = mAdConfiguration.getAdUnitId();
        views.mDescriptionView.setText(mAdConfiguration.getDescription());
        views.mAdUnitIdView.setText(adUnitId);

        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_list_item_1);
        for (int i = 0; i < 100; ++i) {
            adapter.add("Item " + i);
        }

        // Create an ad adapter that gets its positioning information from the Droi Ad Server.
        // This adapter will be used in place of the original adapter for the ListView.
        mAdAdapter = new DroiAdAdapter(getActivity(), adapter, new DroiServerPositioning());

        // Set up a renderer that knows how to put ad data in your custom native view.
        final DroiStaticNativeAdRenderer staticAdRender = new DroiStaticNativeAdRenderer(
                new ViewBinder.Builder(R.layout.native_ad_list_item)
                        .titleId(R.id.native_title)
                        .textId(R.id.native_text)
                        .mainImageId(R.id.native_main_image)
                        .iconImageId(R.id.native_icon_image)
                        .callToActionId(R.id.native_cta)
                        .privacyInformationIconImageId(R.id.native_privacy_information_icon_image)
                        .build());

        // Register the renderers with the DroiAdAdapter and then set the adapter on the ListView.
//        mAdAdapter.registerAdRenderer(videoAdRenderer);
        mAdAdapter.registerAdRenderer(staticAdRender);
        listView.setAdapter(mAdAdapter);

        mAdAdapter.loadAds(mAdConfiguration.getAdUnitId(), mRequestParameters);
        return view;
    }

    @Override
    public void onDestroyView() {
        // You must call this or the ad adapter may cause a memory leak.
        mAdAdapter.destroy();
        super.onDestroyView();
    }
}