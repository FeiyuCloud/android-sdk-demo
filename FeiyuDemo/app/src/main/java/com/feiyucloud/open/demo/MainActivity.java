package com.feiyucloud.open.demo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.feiyucloud.sdk.FYRtcEngine;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText mEditUid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 长度不超过40的字符串，支持的字符集范围: a-z,A-Z,0-9,_,-
        mEditUid = findViewById(R.id.edit_uid);
        findViewById(R.id.btn_save).setOnClickListener(this);
        findViewById(R.id.btn_test_join_channel).setOnClickListener(this);
        findViewById(R.id.btn_test_voip).setOnClickListener(this);

        String[] permissions = FYRtcEngine.PERMISSIONS;
        PermissionUtil.with(this).requestCode(1).permissions(permissions).request();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mEditUid.setText(Utils.getUserId(this));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_save:
                Utils.saveUserId(this, mEditUid.getText().toString());
                Toast.makeText(this, "save success", Toast.LENGTH_SHORT).show();
                break;

            case R.id.btn_test_join_channel:
                startActivity(new Intent(this, TestJoinChannelActivity.class));
                break;

            case R.id.btn_test_voip:
                startActivity(new Intent(this, TestVoipActivity.class));
                break;
        }
    }

}
