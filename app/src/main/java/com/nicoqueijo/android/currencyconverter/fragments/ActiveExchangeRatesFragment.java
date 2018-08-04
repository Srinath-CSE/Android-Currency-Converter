package com.nicoqueijo.android.currencyconverter.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nicoqueijo.android.currencyconverter.R;
import com.nicoqueijo.android.currencyconverter.adapters.ActiveExchangeRatesRecyclerViewAdapter;
import com.nicoqueijo.android.currencyconverter.helpers.HelperClass;
import com.nicoqueijo.android.currencyconverter.models.Currency;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

public class ActiveExchangeRatesFragment extends Fragment {

    private static final String TAG = ActiveExchangeRatesFragment.class.getSimpleName();

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private FloatingActionButton mFloatingActionButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.fragment_active_exchange_rates, container, false);

        mRecyclerView = mView.findViewById(R.id.recycler_view_active_rates);
        mFloatingActionButton = mView.findViewById(R.id.fab);
        mFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(v, "FAB clicked", Snackbar.LENGTH_SHORT).show();
                // TODO: Open dialog fragment containing list of all possible currencies
                // This dialog fragment should contain a SearchView on top and a
                // RecyclerView below it. Currencies that are already in the
                // ActiveExchangeRatesFragment should not be contenders for selection.
            }
        });

        SharedPreferences mSharedPreferencesRates = getContext().getSharedPreferences(getContext()
                .getPackageName().concat(".rates"), MODE_PRIVATE);
        List<Currency> mCurrencies = new ArrayList<>();
        Map<String, ?> keys = mSharedPreferencesRates.getAll();
        for (Map.Entry<String, ?> entry : keys.entrySet()) {
            Log.d(TAG, entry.getKey() + ": " + entry.getValue().toString());
            mCurrencies.add(new Currency(entry.getKey(),
                    HelperClass.getDouble(mSharedPreferencesRates, entry.getKey(), 0.0)));
        }
        Collections.sort(mCurrencies, new Comparator<Currency>() {
            @Override
            public int compare(Currency o1, Currency o2) {
                return o1.getCurrencyCode().compareTo(o2.getCurrencyCode());
            }
        });

        mAdapter = new ActiveExchangeRatesRecyclerViewAdapter(getContext(), mCurrencies);
        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(mLayoutManager);

        return mView;
    }
}