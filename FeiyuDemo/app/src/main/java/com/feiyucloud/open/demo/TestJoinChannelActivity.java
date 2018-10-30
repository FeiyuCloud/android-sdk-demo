package com.feiyucloud.open.demo;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class TestJoinChannelActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText mEditChannelId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_join_channel);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("Test Join Channel");
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        mEditChannelId = findViewById(R.id.edit_channel_id);
        findViewById(R.id.btn_join).setOnClickListener(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_join) {
            // // 长度不超过40的字符串，支持的字符集范围: a-z,A-Z,0-9,_,-
            String channelId = mEditChannelId.getText().toString();
            if (TextUtils.isEmpty(channelId)) {
                Toast.makeText(this, "channel id is empty", Toast.LENGTH_SHORT).show();
            } else {
                ChannelActivity.joinChannel(this, channelId);
            }
        }
    }

}
