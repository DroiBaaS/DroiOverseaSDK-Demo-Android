package com.droi.sample;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.droi.common.logging.DroiLog;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static com.droi.sample.DroiSQLiteHelper.COLUMN_AD_TYPE;
import static com.droi.sample.DroiSQLiteHelper.COLUMN_AD_UNIT_ID;
import static com.droi.sample.DroiSQLiteHelper.COLUMN_DESCRIPTION;
import static com.droi.sample.DroiSQLiteHelper.COLUMN_ID;
import static com.droi.sample.DroiSQLiteHelper.COLUMN_USER_GENERATED;
import static com.droi.sample.DroiSQLiteHelper.TABLE_AD_CONFIGURATIONS;
import static com.droi.sample.DroiSampleAdUnit.AdType;
import static com.droi.sample.DroiSampleAdUnit.AdType.BANNER;
import static com.droi.sample.DroiSampleAdUnit.AdType.CUSTOM_NATIVE;
import static com.droi.sample.DroiSampleAdUnit.AdType.INTERSTITIAL;
import static com.droi.sample.DroiSampleAdUnit.AdType.LIST_VIEW;
import static com.droi.sample.DroiSampleAdUnit.AdType.RECYCLER_VIEW;

class AdUnitDataSource {
    private Context mContext;
    private DroiSQLiteHelper mDatabaseHelper;
    private String[] mAllColumns = {
            COLUMN_ID,
            COLUMN_AD_UNIT_ID,
            COLUMN_DESCRIPTION,
            COLUMN_USER_GENERATED,
            COLUMN_AD_TYPE
    };

    AdUnitDataSource(final Context context) {
        mContext = context.getApplicationContext();
        mDatabaseHelper = new DroiSQLiteHelper(context);
        populateDefaultSampleAdUnits();
    }

    DroiSampleAdUnit createDefaultSampleAdUnit(final DroiSampleAdUnit sampleAdUnit) {
        return createSampleAdUnit(sampleAdUnit, false);
    }

    DroiSampleAdUnit createSampleAdUnit(final DroiSampleAdUnit sampleAdUnit) {
        return createSampleAdUnit(sampleAdUnit, true);
    }

    private DroiSampleAdUnit createSampleAdUnit(final DroiSampleAdUnit sampleAdUnit,
                                                 final boolean isUserGenerated) {
        final ContentValues values = new ContentValues();
        final int userGenerated = isUserGenerated ? 1 : 0;
        values.put(COLUMN_AD_UNIT_ID, sampleAdUnit.getAdUnitId());
        values.put(COLUMN_DESCRIPTION, sampleAdUnit.getDescription());
        values.put(COLUMN_USER_GENERATED, userGenerated);
        values.put(COLUMN_AD_TYPE, sampleAdUnit.getFragmentClassName());

        final SQLiteDatabase database = mDatabaseHelper.getWritableDatabase();
        final long insertId = database.insert(TABLE_AD_CONFIGURATIONS, null, values);
        final Cursor cursor = database.query(TABLE_AD_CONFIGURATIONS, mAllColumns,
                COLUMN_ID + " = " + insertId, null, null, null, null);
        cursor.moveToFirst();

        final DroiSampleAdUnit newAdConfiguration = cursorToAdConfiguration(cursor);
        cursor.close();
        database.close();

        if (newAdConfiguration != null) {
            DroiLog.d("Ad configuration added with id: " + newAdConfiguration.getId());
        }
        return newAdConfiguration;
    }

    void deleteSampleAdUnit(final DroiSampleAdUnit adConfiguration) {
        final long id = adConfiguration.getId();
        SQLiteDatabase database = mDatabaseHelper.getWritableDatabase();
        database.delete(TABLE_AD_CONFIGURATIONS, COLUMN_ID + " = " + id, null);
        DroiLog.d("Ad Configuration deleted with id: " + id);
        database.close();
    }

    List<DroiSampleAdUnit> getAllAdUnits() {
        final List<DroiSampleAdUnit> adConfigurations = new ArrayList<>();
        SQLiteDatabase database = mDatabaseHelper.getReadableDatabase();
        final Cursor cursor = database.query(TABLE_AD_CONFIGURATIONS,
                mAllColumns, null, null, null, null, null);
        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            final DroiSampleAdUnit adConfiguration = cursorToAdConfiguration(cursor);
            adConfigurations.add(adConfiguration);
            cursor.moveToNext();
        }

        cursor.close();
        database.close();
        return adConfigurations;
    }

    List<DroiSampleAdUnit> getDefaultAdUnits() {
        final List<DroiSampleAdUnit> adUnitList = new ArrayList<>();
        adUnitList.add(
                new DroiSampleAdUnit
                        .Builder(mContext.getString(R.string.ad_unit_id_banner), BANNER)
                        .description("Droi Banner Sample")
                        .build());
        adUnitList.add(
                new DroiSampleAdUnit
                        .Builder(mContext.getString(R.string.ad_unit_id_interstitial), INTERSTITIAL)
                        .description("Droi Interstitial Sample")
                        .build());
        adUnitList.add(
                new DroiSampleAdUnit
                        .Builder(mContext.getString(R.string.ad_unit_id_native), LIST_VIEW)
                        .description("Droi Ad Placer Sample")
                        .build());
        adUnitList.add(
                new DroiSampleAdUnit
                        .Builder(mContext.getString(R.string.ad_unit_id_native), RECYCLER_VIEW)
                        .description("Droi Recycler View Sample")
                        .build());
        adUnitList.add(
                new DroiSampleAdUnit
                        .Builder(mContext.getString(R.string.ad_unit_id_native), CUSTOM_NATIVE)
                        .description("Droi View Pager Sample")
                        .build());
        return adUnitList;
    }

    private void populateDefaultSampleAdUnits() {
        final HashSet<DroiSampleAdUnit> allAdUnits = new HashSet<>();
        for (final DroiSampleAdUnit adUnit : getAllAdUnits()) {
            allAdUnits.add(adUnit);
        }

        for (final DroiSampleAdUnit defaultAdUnit : getDefaultAdUnits()) {
            if (!allAdUnits.contains(defaultAdUnit)) {
                createDefaultSampleAdUnit(defaultAdUnit);
            }
        }
    }

    private DroiSampleAdUnit cursorToAdConfiguration(final Cursor cursor) {
        final long id = cursor.getLong(0);
        final String adUnitId = cursor.getString(1);
        final String description = cursor.getString(2);
        final int userGenerated = cursor.getInt(3);
        final AdType adType = AdType.fromFragmentClassName(cursor.getString(4));

        if (adType == null) {
            return null;
        }

        return new DroiSampleAdUnit.Builder(adUnitId, adType)
                .description(description)
                .isUserDefined(userGenerated == 1)
                .id(id)
                .build();
    }
}
