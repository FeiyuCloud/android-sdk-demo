package com.feiyucloud.open.demo;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class TestVoipActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText mEditCallee;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_voip);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("Test VoIP");
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        mEditCallee = findViewById(R.id.edit_uid);
        findViewById(R.id.btn_dial_peer).setOnClickListener(this);
        findViewById(R.id.btn_callee_prepare).setOnClickListener(this);
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
        if (v.getId() == R.id.btn_dial_peer) {
            String uid = mEditCallee.getText().toString();
            if (TextUtils.isEmpty(uid)) {
                Toast.makeText(TestVoipActivity.this, "Callee uid empty", Toast.LENGTH_SHORT).show();
                return;
            }
            CallActivity.startDial(this, uid);
        } else if (v.getId() == R.id.btn_callee_prepare) {
            DemoApp.instance().createFYRtcEngine().calleePrepare(Utils.getUserId(this));
        }
    }

}
