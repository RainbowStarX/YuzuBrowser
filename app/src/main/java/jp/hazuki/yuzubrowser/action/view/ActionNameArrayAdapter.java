/*
 * Copyright (C) 2017 Hazuki
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package jp.hazuki.yuzubrowser.action.view;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;

import jp.hazuki.yuzubrowser.R;
import jp.hazuki.yuzubrowser.action.ActionNameArray;
import jp.hazuki.yuzubrowser.action.SingleAction;
import jp.hazuki.yuzubrowser.utils.ThemeUtils;

public class ActionNameArrayAdapter extends BaseAdapter {

    private final ActionNameArray nameArray;
    private final boolean[] checked;
    private final LayoutInflater inflater;
    private OnSettingButtonListener mListener;

    private final PorterDuffColorFilter iconColor;
    private final PorterDuffColorFilter disableIconColor;

    public ActionNameArrayAdapter(Context context, ActionNameArray array) {
        nameArray = array;
        inflater = LayoutInflater.from(context);
        checked = new boolean[getCount()];

        int color = ThemeUtils.getColorFromThemeRes(context, R.attr.iconColor);
        iconColor = new PorterDuffColorFilter(color, PorterDuff.Mode.SRC_ATOP);
        disableIconColor = new PorterDuffColorFilter(color & 0xffffff | 0x88000000, PorterDuff.Mode.SRC_ATOP);
    }

    @Override
    public int getCount() {
        return nameArray.actionList.length;
    }

    @Override
    public Object getItem(int position) {
        return nameArray.actionList[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public String getName(int position) {
        return nameArray.actionList[position];
    }

    public int getItemValue(int position) {
        return nameArray.actionValues[position];
    }

    public boolean isChecked(int position) {
        return checked[position];
    }

    public void clearChoices() {
        for (int i = 0; i < checked.length; i++) {
            checked[i] = false;
        }
        notifyDataSetChanged();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.select_action_item, parent, false);
            holder = new ViewHolder();
            holder.text = (TextView) convertView.findViewById(R.id.nameTextView);
            holder.setting = (ImageButton) convertView.findViewById(R.id.settingsButton);
            holder.checkBox = (CheckBox) convertView.findViewById(R.id.checkBox);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.text.setText(getName(position));

        holder.checkBox.setChecked(isChecked(position));

        if (SingleAction.checkSubPreference(getItemValue(position))) {
            holder.setting.setVisibility(View.VISIBLE);
            holder.setting.setClickable(isChecked(position));
            holder.setting.setColorFilter(isChecked(position) ? iconColor : disableIconColor);
            holder.setting.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener != null)
                        mListener.onSettingClick(position);
                }
            });
        } else {
            holder.setting.setVisibility(View.GONE);
        }

        return convertView;
    }

    public boolean toggleCheck(int position) {
        checked[position] = !checked[position];
        notifyDataSetChanged();
        return checked[position];
    }

    public void setChecked(int position, boolean value) {
        checked[position] = value;
    }

    public void setListener(OnSettingButtonListener mListener) {
        this.mListener = mListener;
    }

    static class ViewHolder {
        TextView text;
        ImageButton setting;
        CheckBox checkBox;
    }

    interface OnSettingButtonListener {
        void onSettingClick(int position);
    }
}
