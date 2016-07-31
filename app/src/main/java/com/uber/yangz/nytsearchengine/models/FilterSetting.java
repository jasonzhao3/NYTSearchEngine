package com.uber.yangz.nytsearchengine.models;

import android.text.TextUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class FilterSetting {

    // For time being, use public fields
    private String sortOrder;
    private Date beginDate;
    private ArrayList<String> deskValues;


    public String getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(String sortOrder) {
        this.sortOrder = sortOrder;
    }

    public String getBeginDateStr() {
        if (beginDate != null) {
            String myFormat = "MM/dd/yy"; //In which you need put here
            SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
            return sdf.format(beginDate);
        } else {
            return "";
        }

    }

    public void setBeginDate(Date beginDate) {
        this.beginDate = beginDate;
    }

    public String getDeskValuesStr() {
        return "news_desk:(\"" + TextUtils.join("\" \"", deskValues) + "\")";
    }

    public void setDeskValues(ArrayList<String> deskValues) {
        this.deskValues = deskValues;
    }
}
