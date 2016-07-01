package com.martin.district.adapter;

import android.content.Context;

import com.martin.district.db.DistrictInfo;

import java.util.List;

import kankan.wheel.widget.adapters.AbstractWheelTextAdapter;

/**
 * Created by martin on 16/6/8.
 */
public class ListWheelAdapter<T extends DistrictInfo> extends AbstractWheelTextAdapter {

    private List<DistrictInfo> list;

    public ListWheelAdapter(Context context, List<DistrictInfo> list) {
        super(context);
        this.list = list;
    }

    @Override
    protected CharSequence getItemText(int index) {
        DistrictInfo info = list.get(index);
        return info.getName();
    }

    @Override
    public int getItemsCount() {
        return list == null ? 0 : list.size();
    }
}
