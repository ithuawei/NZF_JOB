/*
 * Copyright (C) 2016, TP-LINK TECHNOLOGIES CO., LTD.
 *
 * SongsExpandableAdapter.java
 * 
 * Description
 *  
 * Author nongzhanfei
 * 
 * Ver 1.0, 8/31/16, nongzhanfei, Create file
 */
package com.tplink.tpsoundrecorder.adapter;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.tplink.tpsoundrecorder.R;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class SongsExpandableAdapter extends BaseExpandableListAdapter {

    private ArrayList<String> mSongDatas;//父item组
    private ArrayList<List<Integer>> mFlagDatas;//子item组
    private Context mContext;
    //记录缓存Item，防止ListView叠加错乱
    private LinkedHashMap<Integer, View> lmp = new LinkedHashMap<Integer, View>();

    //进行增加或者删除后的Flag
    ArrayList<List<Integer>> mCurrentFlagDatas;

    public SongsExpandableAdapter(ArrayList<String> songDatas, ArrayList<List<Integer>> flagDatas, Context context) {
        mSongDatas = songDatas;
        mFlagDatas = flagDatas;
        mContext = context;
        //初始化的时候相同，后面刷新是另外不同的
        mCurrentFlagDatas = mFlagDatas;
    }

    @Override
    public int getGroupCount() {
        if (mSongDatas.size() > 0) {
            return mSongDatas.size();
        } else {
            return 0;
        }
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        //每组的子孩子的数量
        return mCurrentFlagDatas.get(groupPosition).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return getGroup(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        //单个条目=对应组上的子孩子.的子孩子的id
        return mCurrentFlagDatas.get(groupPosition).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        ViewHolder holder;
        //从lmp拿
        if (lmp.get(groupPosition) == null) {
            convertView = View.inflate(mContext, R.layout.song_item, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
            //setTag之后记录（务必放在setTag后面）
            lmp.put(groupPosition, convertView);
        } else {
            //赋值
            convertView = lmp.get(groupPosition);
            holder = (ViewHolder) convertView.getTag();
        }
        String songName = mSongDatas.get(groupPosition);
        //判断名字显示过长
        if (songName.length() > 15) {
            songName = songName.substring(0, 15) + "...";
        }

        holder.tvSongItemName.setText(songName);
//        holder.tvSongItemCalendarBottom.setText(songName);
//        holder.tvSongItemCalendarRight.setText(songName);
//        holder.tvSongItemTimeCount.setText(songName);
//        holder.ivSongItemPlaying.setVisibility(View.GONE);
//        holder.ivSongItemFlag.setVisibility(View.VISIBLE);

        return convertView;
    }

    class ViewHolder {

        TextView tvSongItemName;
        TextView tvSongItemCalendarBottom;
        TextView tvSongItemCalendarRight;
        TextView tvSongItemTimeCount;
        ImageView ivSongItemPlaying;
        ImageView ivSongItemFlag;

        ViewHolder(View convertView) {

            tvSongItemName = (TextView) convertView.findViewById(R.id.tv_item_songs_name);
            tvSongItemCalendarBottom = (TextView) convertView.findViewById(R.id.tv_item_songs_calendar_bottom);
            tvSongItemCalendarRight = (TextView) convertView.findViewById(R.id.tv_item_songs_calendar_right);
            tvSongItemTimeCount = (TextView) convertView.findViewById(R.id.tv_item_songs_timeCount);
            ivSongItemPlaying = (ImageView) convertView.findViewById(R.id.iv_item_songs_playing);
            ivSongItemFlag = (ImageView) convertView.findViewById(R.id.iv_item_songs_item_flag);
        }
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        Integer time = mCurrentFlagDatas.get(groupPosition).get(childPosition);
        return getFlagView(time + "", groupPosition, childPosition);
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    private View getFlagView(String timeFlag, final int groupPosition, final int childPosition) {
        Log.d("ABC", "getFlagView");
        View vSongFlagItem = View.inflate(mContext, R.layout.song_flag_item, null);
        TextView tvSongFlag = (TextView) vSongFlagItem.findViewById(R.id.tv_songs_flag);
        TextView tvFlagOrder = (TextView) vSongFlagItem.findViewById(R.id.tv_songs_flag_order);
        ImageButton ibtDeleteFlag = (ImageButton) vSongFlagItem.findViewById(R.id.ibt_delete_flag);

        int time = Integer.parseInt(timeFlag);
        int sec = time / 10;
        int min = sec / 60;

        String mTime = "" + time;
        String mMis = "" + mTime.charAt(mTime.length() - 1);//拿最末尾字符
        String mSec = "" + sec;
        if (mSec.length() == 1) {
            mSec = "0" + mSec;
        }
        String mMin = "" + min;
        if (mMin.length() == 1) {
            mMin = "0" + mMin;
        }
        final String mCurrenTime = mMin + ":" + mSec + "." + mMis;
        tvSongFlag.setText(mCurrenTime);
        vSongFlagItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int flags_time = mCurrentFlagDatas.get(groupPosition).get(childPosition);
                //播放的点
                Toast.makeText(mContext, "play time : "+flags_time, Toast.LENGTH_SHORT).show();
            }
        });

        tvFlagOrder.setText(childPosition + 1 + ".");//显示从0开始

        ibtDeleteFlag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //删除那一个大组的-》中的-》哪一个条目
                mCurrentFlagDatas.get(groupPosition).remove(childPosition);
                //刷新
                notifyDataSetChanged();
            }
        });

        //返回的是整条item样式
        return vSongFlagItem;
    }
}
