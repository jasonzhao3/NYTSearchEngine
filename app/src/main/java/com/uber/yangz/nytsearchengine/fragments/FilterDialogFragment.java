package com.uber.yangz.nytsearchengine.fragments;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;

import com.uber.yangz.nytsearchengine.R;
import com.uber.yangz.nytsearchengine.models.FilterSetting;

import java.util.ArrayList;
import java.util.Calendar;

public class FilterDialogFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener  {
    private static final String ARG_FILTER_SETTING = "filter_setting";
    private static final String DESK_VALUE_ARTS = "Arts";
    private static final String DESK_VALUE_FASHION = "Fashion & Style";
    private static final String DESK_VALUE_SPORTS = "Sports";


    private TextView tvBeginDate;
    private Spinner spSortOrder;
    private CheckBox cbArts;
    private CheckBox cbFashion;
    private CheckBox cbSports;
    private Button btnFilterSearch;

    private FilterSetting filterSetting;


    public FilterDialogFragment() {
        // Required empty public constructor
    }

    public static FilterDialogFragment newInstance(@NonNull FilterSetting filterSetting) {
        FilterDialogFragment fragment = new FilterDialogFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_FILTER_SETTING, filterSetting);
        fragment.setArguments(args);
        return fragment;
    }

    // 1. Defines the listener interface with a method passing back data result.
    public interface FilterDialogListener {
        void onFinishFilterDialog(FilterSetting filterSetting);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_filter, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tvBeginDate = (TextView) view.findViewById(R.id.tv_begin_date);
        spSortOrder = (Spinner) view.findViewById(R.id.sp_sort_order);
        cbArts = (CheckBox) view.findViewById(R.id.cb_arts);
        cbFashion = (CheckBox) view.findViewById(R.id.cb_fashion);
        cbSports = (CheckBox) view.findViewById(R.id.cb_sports);
        btnFilterSearch = (Button) view.findViewById(R.id.btn_filter_search);


        // Fetch arguments from bundle and set title
        filterSetting = getArguments().getParcelable(ARG_FILTER_SETTING);

        if (filterSetting.getBeginDateStr() != null) {
            tvBeginDate.setText(filterSetting.getBeginDateStr());
        }

        if (filterSetting.getSortOrder() != null) {
            // TODO: refactor to make it nicer
            spSortOrder.setSelection(0, true);
        }

        if (filterSetting.getDeskValues() != null) {
            for (String value : filterSetting.getDeskValues()) {
                if (value.equals(DESK_VALUE_ARTS)) {
                    cbArts.setChecked(true);
                }
                if (value.equals(DESK_VALUE_FASHION)) {
                    cbFashion.setChecked(true);
                }
                if (value.equals(DESK_VALUE_SPORTS)) {
                    cbSports.setChecked(true);
                }
            }
        }

        tvBeginDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });
        tvBeginDate.requestFocus();


        btnFilterSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                populateFilterSetting();
                FilterDialogListener listener = (FilterDialogListener) getActivity();
                listener.onFinishFilterDialog(filterSetting);
                dismiss();
            }
        });
    }

    private void populateFilterSetting() {
        filterSetting.setSortOrder(spSortOrder.getSelectedItem().toString());
        ArrayList<String> deskValues = new ArrayList<>();
        // TODO: refactor this series of ifs to make it more scalable
        if (cbArts.isChecked()) {
            deskValues.add(DESK_VALUE_ARTS);
        }
        if (cbFashion.isChecked()) {
            deskValues.add(DESK_VALUE_FASHION);
        }
        if (cbSports.isChecked()) {
            deskValues.add(DESK_VALUE_SPORTS);
        }
        filterSetting.setDeskValues(deskValues);
    }
    @Override
    public void onResume() {
        // resize the dialog fragment to be full screen
        ViewGroup.LayoutParams params = getDialog().getWindow().getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.MATCH_PARENT;
        getDialog().getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);
        super.onResume();
    }

    public void showDatePickerDialog() {
        DatePickerFragment datePicker = new DatePickerFragment();
        datePicker.setTargetFragment(FilterDialogFragment.this, 300);
        datePicker.show(getFragmentManager(), "datePicker");
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        final Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, monthOfYear);
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);

        filterSetting.setBeginDate(c.getTime());
        tvBeginDate.setText(filterSetting.getBeginDateStr());
    }
}
