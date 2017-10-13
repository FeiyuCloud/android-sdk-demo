package com.feiyucloud.open.demo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.feiyucloud.sdk.FYRtcEngine;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText mEditChannelId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mEditChannelId = (EditText) findViewById(R.id.edit_channel_id);
        findViewById(R.id.btn_join).setOnClickListener(this);

        String[] permissions = FYRtcEngine.PERMISSIONS;
        PermissionUtil.with(this).requestCode(1).permissions(permissions).request();

    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_join) {
            // channelId: 字符串，数字，_，长度不超过40位
            String channelId = mEditChannelId.getText().toString();
            if (TextUtils.isEmpty(channelId)) {
                Toast.makeText(this, "channel id is empty", Toast.LENGTH_SHORT).show();
            } else {
                ChannelActivity.joinChannel(this, channelId);
            }
        }
    }

}
