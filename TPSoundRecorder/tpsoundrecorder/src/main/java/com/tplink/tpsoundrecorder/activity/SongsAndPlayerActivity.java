/*
 * Copyright (C) 2016, TP-LINK TECHNOLOGIES CO., LTD.
 *
 * add.java
 *
 * Description
 *
 * Author nongzhanfei
 *
 * Ver 1.0, 9/19/16, NongZhanfei, Create file
 */
package com.tplink.tpsoundrecorder.activity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tplink.tpsoundrecorder.R;
import com.tplink.tpsoundrecorder.adapter.SongsExpandableAdapter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class SongsAndPlayerActivity extends Activity implements View.OnClickListener {

    private ArrayList<String> mSongDatas;
    private ArrayList<List<Integer>> mFlagDatas;
    private ExpandableListView mELvSongs;
    private SongsExpandableAdapter mSongsExpandableAdapter;
    private RelativeLayout mRlPlayer;
    private TextView mTvPlayerInnerTimeStart;
    private TextView mTvPlayerInnerTimeEnd;
    private ProgressBar mPbPlayerInnerProgress;
    private ImageView mIvPlayerInnerFlagNode;
    private ImageButton mIbtPlayerInnerFlag;
    private ImageButton mIbtPlayerInnerStartOrPause;
    private ImageButton mIbtPlayerInnerStopAndGone;
    private boolean isSelecSong;
    private boolean isPreparePlaying;
    private int flagExtendGoupPosition = -1;
    private ImageView mIv_item_songs_playing;
    private TextView mTv_item_songs_calendar_bottom;
    private TextView mTv_item_songs_calendar_bottom1;
    private TextView mTv_item_songs_calendar_right;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.songs_list_player);
        initData();
        initView();
    }

    private void initData() {
        mSongDatas = getAllSongs();
        if (mSongDatas.size() < 0) {
            return;
        }
        mFlagDatas = getSongFlags();
        if (mSongDatas.size() < 0) {
            return;
        }
    }

    private ArrayList<List<Integer>> getSongFlags() {
        //打点列表，成组的标记
        ArrayList<List<Integer>> flagsList = new ArrayList<>();
        //循环获取所有歌曲的标记
        for (int i = 0; i < mSongDatas.size(); i++) {
            //单独的每首个的标记
            final ArrayList<Integer> childflags = new ArrayList<>();

            String songName = mSongDatas.get(i);

            SharedPreferences sp = getSharedPreferences(songName, MODE_PRIVATE);
            //先获取刚才保存的大小
            int timeCount = sp.getInt("timeCount", 0);

            for (int j = 0; j < timeCount; j++) {
                int time = sp.getInt("time" + j, 0);
//                Log.d("ABC", "time:" + time);
                childflags.add(time);
            }
            flagsList.add(childflags);
        }
        return flagsList;
    }

    private ArrayList<String> getAllSongs() {
        ArrayList<String> songs = new ArrayList<>();
        //目录：/data
        File songsDir = Environment.getDataDirectory();
        //包名
        String packageName = getPackageName();
        //目录：/data/data/包名/shared_prefs
        File shareDir = new File(songsDir + "/data/" + packageName + "/shared_prefs");
        //File file = new File(songsDir+"/data/"+packageName+"/shared_prefs","Recoding0X.xml");
        //文件：目录下的所有文件
        if (!shareDir.exists()) {
            return songs;
        }
        String[] list = shareDir.list();
        for (int i = 0; i < list.length; i++) {
            String s = list[i];
            //Recoding0X.xml不要.xml
            s = s.substring(0, s.indexOf("."));
            songs.add(s);
        }
        return songs;
    }

    private void initView() {
        mELvSongs = (ExpandableListView) findViewById(R.id.elv_songs);

        mRlPlayer = (RelativeLayout) findViewById(R.id.rl_player);
        mTvPlayerInnerTimeStart = (TextView) findViewById(R.id.tv_player_inner_time_start);
        mTvPlayerInnerTimeEnd = (TextView) findViewById(R.id.tv_player_inner_time_end);

        mPbPlayerInnerProgress = (ProgressBar) findViewById(R.id.pb_player_inner_progressBar);
        mIvPlayerInnerFlagNode = (ImageView) findViewById(R.id.iv_player_inner_flag_node_01);
        mIbtPlayerInnerFlag = (ImageButton) findViewById(R.id.ibt_player_inner_flag);
        mIbtPlayerInnerStartOrPause = (ImageButton) findViewById(R.id.ibt_player_inner_start_pause);
        mIbtPlayerInnerStopAndGone = (ImageButton) findViewById(R.id.ibt_player_inner_stop_gone);

        mIbtPlayerInnerFlag.setOnClickListener(this);
        mIbtPlayerInnerStartOrPause.setOnClickListener(this);
        mIbtPlayerInnerStopAndGone.setOnClickListener(this);


        //初始化不显示
        mRlPlayer.setVisibility(View.GONE);
        //设置"组"无箭头
        mELvSongs.setGroupIndicator(null);
        mSongsExpandableAdapter = new SongsExpandableAdapter(mSongDatas, mFlagDatas, SongsAndPlayerActivity.this);
        mELvSongs.setAdapter(mSongsExpandableAdapter);
        //监听各组的点击：1.正在播放的图标显示/隐藏;2.底部日期消失/显示;3.右侧日期显示/消失;4.播放器显示/消失：放在展开和关闭的监听里
        mELvSongs.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                TextView tv_item_songs_name = (TextView) v.findViewById(R.id.tv_item_songs_name);
//                Toast.makeText(SongsAndPlayerActivity.this, "SongName :" + tv_item_songs_name.getText(), Toast.LENGTH_SHORT).show();

                mIv_item_songs_playing = (ImageView) v.findViewById(R.id.iv_item_songs_playing);
                mTv_item_songs_calendar_bottom1 = (TextView) v.findViewById(R.id.tv_item_songs_calendar_bottom);
                mTv_item_songs_calendar_right = (TextView) v.findViewById(R.id.tv_item_songs_calendar_right);
                if (isSelecSong) {
                    playingGBottomVRightG();
                    isSelecSong = !isSelecSong;
                } else {
                    playingVBottomGRightV();
                    isSelecSong = !isSelecSong;
                }
                Log.d("AABB", "A");
                return false;
            }

        });

        //监听组的展开序号
        mELvSongs.setOnGroupExpandListener(
                new ExpandableListView.OnGroupExpandListener() {
                    @Override
                    public void onGroupExpand(int groupPosition) {
                        isPreparePlaying = true;
                        mRlPlayer.setVisibility(View.VISIBLE);
//                        if (flagExtendGoupPosition != -1) {
//                            mELvSongs.collapseGroup(flagExtendGoupPosition);
//                        }
//                        flagExtendGoupPosition = groupPosition;
//                        mAllextenedGouPosition.add(flagExtendGoupPosition);
                        Log.d("AABB", "B");

                    }
                });
        //监听组关闭的序号
        mELvSongs.setOnGroupCollapseListener(
                new ExpandableListView.OnGroupCollapseListener() {
                    @Override
                    public void onGroupCollapse(int groupPosition) {
                        isPreparePlaying = false;
                        mRlPlayer.setVisibility(View.GONE);
                    }
                });
    }

    //设置当前条目
    private void playingGBottomVRightG() {
        //1.
        mIv_item_songs_playing.setVisibility(View.GONE);
        //2.
        mTv_item_songs_calendar_bottom1.setVisibility(View.VISIBLE);
        //3.
        mTv_item_songs_calendar_right.setVisibility(View.GONE);
    }

    //设置当前条目
    private void playingVBottomGRightV() {
        //1.
        mIv_item_songs_playing.setVisibility(View.VISIBLE);
        //2.
        mTv_item_songs_calendar_bottom1.setVisibility(View.GONE);
        //3.
        mTv_item_songs_calendar_right.setVisibility(View.VISIBLE);
    }

    public void songListBack(View view) {
        finish();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ibt_player_inner_flag:
                Toast.makeText(this, "标记", Toast.LENGTH_SHORT).show();
                break;

            case R.id.ibt_player_inner_start_pause:
                //当前如果是播放，点击后就变暂停
                if (isPreparePlaying) {
                    mIbtPlayerInnerStartOrPause.setImageResource(R.mipmap.player_inner_pause);
                    Toast.makeText(this, "暂停", Toast.LENGTH_SHORT).show();
                    isPreparePlaying = !isPreparePlaying;
                } else {
                    mIbtPlayerInnerStartOrPause.setImageResource(R.mipmap.player_inner_start);
                    Toast.makeText(this, "播放", Toast.LENGTH_SHORT).show();
                    isPreparePlaying = !isPreparePlaying;
                }
                break;

            case R.id.ibt_player_inner_stop_gone:
                Toast.makeText(this, "停止和退出", Toast.LENGTH_SHORT).show();
                //关闭所打开的组
                mELvSongs.collapseGroup(flagExtendGoupPosition);
                //隐藏播放图标
                mIv_item_songs_playing.setVisibility(View.GONE);
                //隐藏播放器
                mRlPlayer.setVisibility(View.GONE);
                break;
        }
    }
}
