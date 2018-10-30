package com.feiyucloud.open.demo;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import com.feiyucloud.sdk.FYError;
import com.feiyucloud.sdk.FYRtcEngine;
import com.feiyucloud.sdk.FYRtcEventHandler;

import java.util.ArrayList;

public class DemoApp extends Application {

    private static DemoApp sInstance;
    private ArrayList<FYRtcEventHandler> mEventHandlers = new ArrayList<>();

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
    }

    public static DemoApp instance() {
        if (sInstance == null) {
            throw new RuntimeException("Application is null");
        }
        return sInstance;
    }

    public FYRtcEngine createFYRtcEngine() {
        String appId = getString(R.string.app_id);
        String appToken = getString(R.string.app_token);
        return FYRtcEngine.create(this, appId, appToken, mEventHandler);
    }

    public void addEventHandler(FYRtcEventHandler handler) {
        mEventHandlers.add(handler);
    }

    public void removeEventHandler(FYRtcEventHandler handler) {
        mEventHandlers.remove(handler);
    }


    private FYRtcEventHandler mEventHandler = new FYRtcEventHandler() {
        @Override
        public void onError(FYError error) {
            for (FYRtcEventHandler handler : mEventHandlers) {
                handler.onError(error);
            }
        }

        @Override
        public void onJoinChannelSuccess(String channelId, String uid) {
            for (FYRtcEventHandler handler : mEventHandlers) {
                handler.onJoinChannelSuccess(channelId, uid);
            }
        }

        @Override
        public void onLeaveChannel() {
            for (FYRtcEventHandler handler : mEventHandlers) {
                handler.onLeaveChannel();
            }
        }

        @Override
        public void onUserJoined(String uid) {
            for (FYRtcEventHandler handler : mEventHandlers) {
                handler.onUserJoined(uid);
            }
        }

        @Override
        public void onUserOffline(String uid) {
            for (FYRtcEventHandler handler : mEventHandlers) {
                handler.onUserOffline(uid);
            }
        }

        @Override
        public void onUserMuteAudio(String uid, boolean muted) {
            for (FYRtcEventHandler handler : mEventHandlers) {
                handler.onUserMuteAudio(uid, muted);
            }
        }

        @Override
        public void onIncomingCall(String caller) {
            CallActivity.startCallIncoming(instance(), caller);
        }

        @Override
        public void onCallConnect() {
            for (FYRtcEventHandler handler : mEventHandlers) {
                handler.onCallConnect();
            }
        }

        @Override
        public void onCallEnd() {
            for (FYRtcEventHandler handler : mEventHandlers) {
                handler.onCallEnd();
            }
        }

        @Override
        public void onCalleePrepareSuccess() {
            Toast.makeText(instance(), "CalleePrepare Success", Toast.LENGTH_SHORT).show();
        }
    };

}
