package com.feiyucloud.open.demo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.feiyucloud.sdk.FYError;
import com.feiyucloud.sdk.FYRtcEngine;
import com.feiyucloud.sdk.FYRtcEventHandler;

import java.lang.ref.WeakReference;

public class CallActivity extends AppCompatActivity implements View.OnClickListener,
        CompoundButton.OnCheckedChangeListener {

    static void startDial(Context context, String callee) {
        Intent intent = new Intent();
        intent.setClass(context, CallActivity.class);
        intent.putExtra("uid", callee);
        intent.putExtra("incoming_call", false);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public static void startCallIncoming(Context context, String caller) {
        Intent intent = new Intent();
        intent.setClass(context, CallActivity.class);
        intent.putExtra("uid", caller);
        intent.putExtra("incoming_call", true);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    private FYRtcEngine mEngine;
    private InternalHandler mHandler;
    private TextView mTextState;
    private int mCallDuration = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
                | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        setContentView(R.layout.activity_call);

        mEngine = DemoApp.instance().createFYRtcEngine();
        DemoApp.instance().addEventHandler(mEventHandler);
        Intent intent = getIntent();
        String uid = intent.getStringExtra("uid");
        boolean incomingCall = intent.getBooleanExtra("incoming_call", false);

        TextView textDisplay = findViewById(R.id.text_display);
        textDisplay.setText(uid);
        mTextState = findViewById(R.id.text_state);
        setView(incomingCall);

        if (incomingCall) {
            mTextState.setText("来电");
        } else {
            mTextState.setText("正在呼叫");
            mEngine.dialPeer(uid, Utils.getUserId(this), null);
        }

        mHandler = new InternalHandler(this);

        findViewById(R.id.btn_end_call).setOnClickListener(this);
        findViewById(R.id.btn_answer_call).setOnClickListener(this);
        findViewById(R.id.btn_reject_call).setOnClickListener(this);

        ((ToggleButton) findViewById(R.id.toggle_speaker)).setOnCheckedChangeListener(this);
        ((ToggleButton) findViewById(R.id.toggle_mute_local)).setOnCheckedChangeListener(this);
    }

    private void setView(boolean incomingCall) {
        if (incomingCall) {
            findViewById(R.id.layout_incoming_control).setVisibility(View.VISIBLE);
            findViewById(R.id.layout_calling_control).setVisibility(View.GONE);
        } else {
            findViewById(R.id.layout_incoming_control).setVisibility(View.GONE);
            findViewById(R.id.layout_calling_control).setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_end_call:
            case R.id.btn_reject_call:
                mEngine.endCall();
                break;

            case R.id.btn_answer_call:
                setView(false);
                mEngine.answerCall();
                break;
        }
    }

    @Override
    public void onBackPressed() {
    }

    private FYRtcEventHandler mEventHandler = new FYRtcEventHandler() {
        @Override
        public void onError(FYError error) {
            Toast.makeText(CallActivity.this, "Error:" + error, Toast.LENGTH_SHORT).show();
            CallActivity.this.finish();
        }

        @Override
        public void onCallConnect() {
            findViewById(R.id.toggle_speaker).setVisibility(View.VISIBLE);
            findViewById(R.id.toggle_mute_local).setVisibility(View.VISIBLE);
            mHandler.sendEmptyMessage(1);
        }

        @Override
        public void onCallEnd() {
            Toast.makeText(CallActivity.this, "CallEnd", Toast.LENGTH_SHORT).show();
            CallActivity.this.finish();
        }
    };

    private void counter() {
        mCallDuration++;
        mTextState.setText(DateUtils.formatElapsedTime(mCallDuration));
        mHandler.sendEmptyMessageDelayed(1, 1000);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        int id = buttonView.getId();
        if (id == R.id.toggle_speaker) {
            mEngine.enableSpeaker(isChecked);
        } else if (id == R.id.toggle_mute_local) {
            mEngine.muteLocalAudio(isChecked);
        }
    }

    private static class InternalHandler extends Handler {
        private WeakReference<CallActivity> r;

        private InternalHandler(CallActivity activity) {
            r = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            CallActivity activity = r.get();
            if (activity != null && msg.what == 1) {
                activity.counter();
            }
        }
    }

}
