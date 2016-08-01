package com.uber.yangz.nytsearchengine.models;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class FilterSetting implements Parcelable {

    // For time being, use public fields
    private String searchStr;
    private String sortOrder;
    private Date beginDate;
    private ArrayList<String> deskValues;


    public void setSearchStr(String searchStr) {
        this.searchStr = searchStr;
    }
    public void setBeginDate(Date beginDate) {
        this.beginDate = beginDate;
    }
    public void setSortOrder(String sortOrder) {
        this.sortOrder = sortOrder;
    }
    public void setDeskValues(ArrayList<String> deskValues) {
        this.deskValues = deskValues;
    }

    public String getSortOrder() {
        return sortOrder;
    }

    public ArrayList<String> getDeskValues() {
        return deskValues;
    }

    public String getSearchStr() {
        return searchStr;
    }

    public String getBeginDateStr() {
        if (beginDate != null) {
            String myFormat = "yyyyMMdd";
            SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
            return sdf.format(beginDate);
        }
        return null;
    }

    public String getDeskValuesStr() {
        return deskValues == null ? null : "news_desk:(\"" + TextUtils.join("\" \"", deskValues) + "\")";
    }

    public FilterSetting() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.searchStr);
        dest.writeString(this.sortOrder);
        dest.writeLong(this.beginDate != null ? this.beginDate.getTime() : -1);
        dest.writeStringList(this.deskValues);
    }

    protected FilterSetting(Parcel in) {
        this.searchStr = in.readString();
        this.sortOrder = in.readString();
        long tmpBeginDate = in.readLong();
        this.beginDate = tmpBeginDate == -1 ? null : new Date(tmpBeginDate);
        this.deskValues = in.createStringArrayList();
    }

    public static final Creator<FilterSetting> CREATOR = new Creator<FilterSetting>() {
        @Override
        public FilterSetting createFromParcel(Parcel source) {
            return new FilterSetting(source);
        }

        @Override
        public FilterSetting[] newArray(int size) {
            return new FilterSetting[size];
        }
    };
}
