package com.feiyucloud.open.demo;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.feiyucloud.sdk.FYError;
import com.feiyucloud.sdk.FYRtcEngine;
import com.feiyucloud.sdk.FYRtcEventHandler;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

public class ChannelActivity extends AppCompatActivity implements View.OnClickListener,
        CompoundButton.OnCheckedChangeListener {

    private FYRtcEngine mEngine;
    private InternalHandler mHandler;
    private TextView mTextState;
    private RecyclerView mRecyclerView;
    private EventAdapter mEventAdapter;

    private ArrayList<String> mEventList = new ArrayList<>();
    private static final int MSG_COUNTER = 1;
    private static final int MSG_EVENT = 2;

    private String mUserId = Build.SERIAL;
    private int mCallDuration = 0;

    static void joinChannel(Context context, String channelId) {
        Intent intent = new Intent(context, ChannelActivity.class);
        intent.putExtra("channel_id", channelId);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        setContentView(R.layout.activity_channel);

        Intent intent = getIntent();
        String channelId = intent.getStringExtra("channel_id");

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        TextView textChannelId = (TextView) findViewById(R.id.text_channel_id);
        textChannelId.setText(channelId);
        mTextState = (TextView) findViewById(R.id.text_state);
        mTextState.setText("正在加入");

        ((ToggleButton) findViewById(R.id.toggle_speaker)).setOnCheckedChangeListener(this);
        ((ToggleButton) findViewById(R.id.toggle_mute_other)).setOnCheckedChangeListener(this);
        ((ToggleButton) findViewById(R.id.toggle_mute_local)).setOnCheckedChangeListener(this);
        findViewById(R.id.btn_leave_channel).setOnClickListener(this);
        mHandler = new InternalHandler(this);

        mEventAdapter = new EventAdapter(this);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this,
                OrientationHelper.VERTICAL, false);
        layoutManager.setAutoMeasureEnabled(true);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mEventAdapter);

        String appId = getString(R.string.app_id);
        String appToken = getString(R.string.app_token);
        mEngine = FYRtcEngine.create(this, appId, appToken, mRtcEventHandler);
        mEngine.joinChannel(channelId, mUserId, null);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mHandler != null) {
            if (mHandler.hasMessages(MSG_COUNTER)) {
                mHandler.removeMessages(MSG_COUNTER);
            }
            if (mHandler.hasMessages(MSG_EVENT)) {
                mHandler.removeMessages(MSG_EVENT);
            }
        }
        FYRtcEngine.destroy();
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return (keyCode == KeyEvent.KEYCODE_BACK) || super.onKeyDown(keyCode, event);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_leave_channel) {
            mEngine.leaveChannel();
            finish();
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
        int id = compoundButton.getId();
        if (id == R.id.toggle_speaker) {
            mEngine.setEnableSpeaker(isChecked);
        } else if (id == R.id.toggle_mute_local) {
            mEngine.muteLocalAudio(isChecked);
        } else if (id == R.id.toggle_mute_other) {
            mEngine.muteOtherRemoteAudio(mUserId, isChecked);
        }
    }

    private FYRtcEventHandler mRtcEventHandler = new FYRtcEventHandler() {
        @Override
        public void onError(FYError error) {
            Toast.makeText(ChannelActivity.this, "Error:" + error, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onJoinChannelSuccess(String channelId, String uid) {
            mUserId = uid;
            mHandler.sendEmptyMessage(1);
        }

        @Override
        public void onLeaveChannel(RtcStats stats) {
            Toast.makeText(ChannelActivity.this, "LeaveChannel", Toast.LENGTH_SHORT).show();
            ChannelActivity.this.finish();
        }

        @Override
        public void onUserJoined(String uid) {
            handleEvent(uid + ": 加入频道");
        }

        @Override
        public void onUserOffline(String uid) {
            handleEvent(uid + ": 离开频道");
        }

        @Override
        public void onUserMuteAudio(String uid, boolean muted) {
            if (mUserId.equals(uid)) {
                uid = "我";
            }
            if (muted) {
                handleEvent(uid + ": 被静音");
            } else {
                handleEvent(uid + ": 静音解除");
            }
        }
    };

    private void handleEvent(String event) {
        Message msg = mHandler.obtainMessage(MSG_EVENT);
        msg.obj = event;
        msg.sendToTarget();
    }

    private void counter() {
        mCallDuration++;
        mTextState.setText(DateUtils.formatElapsedTime(mCallDuration));
        mHandler.sendEmptyMessageDelayed(1, 1000);
    }

    private void addEvent(String event) {
        mEventList.add(DateUtils.formatElapsedTime(mCallDuration) + " - " + event + "\n");
        mEventAdapter.notifyDataSetChanged();
        mRecyclerView.scrollToPosition(mEventList.size() - 1);
    }

    private static class InternalHandler extends Handler {
        private WeakReference<ChannelActivity> r;

        private InternalHandler(ChannelActivity a) {
            r = new WeakReference<>(a);
        }

        @Override
        public void handleMessage(Message msg) {
            ChannelActivity activity = r.get();
            if (activity != null) {
                if (msg.what == MSG_COUNTER) {
                    activity.counter();
                } else if (msg.what == MSG_EVENT) {
                    activity.addEvent((String) msg.obj);
                }
            }
        }
    }

    private class EventAdapter extends RecyclerView.Adapter<EventViewHolder> {
        private Context context;

        EventAdapter(Context ctx) {
            this.context = ctx;
        }

        @Override
        public EventViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View item = LayoutInflater.from(context).inflate(R.layout.item_event, parent, false);
            return new EventViewHolder(item);
        }

        @Override
        public void onBindViewHolder(EventViewHolder holder, int position) {
            position = holder.getAdapterPosition();
            holder.textEvent.setText(mEventList.get(position));
        }

        @Override
        public int getItemCount() {
            return mEventList.size();
        }

    }

    private static class EventViewHolder extends RecyclerView.ViewHolder {
        TextView textEvent;

        EventViewHolder(View itemView) {
            super(itemView);
            textEvent = (TextView) itemView.findViewById(R.id.text_event);
        }
    }

}
