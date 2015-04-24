package com.thesis.dont.loyaltypointadmin.controllers;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.gc.materialdesign.views.ButtonRectangle;
import com.squareup.picasso.Picasso;
import com.thesis.dont.loyaltypointadmin.R;
import com.thesis.dont.loyaltypointadmin.models.Award;
import com.thesis.dont.loyaltypointadmin.models.Global;
import com.thesis.dont.loyaltypointadmin.models.History;
import com.thesis.dont.loyaltypointadmin.models.ShopModel;
import com.thesis.dont.loyaltypointadmin.models.UserModel;

public class BuyAwardDetailActivity extends ActionBarActivity {
    TextView dateTv, userFullnameTv, userPhoneTv, awardCodeTv, totalPointTv, awardNameTv, awardDetailTv, buyDetailTv;
    ImageView awardImageIv;
    ButtonRectangle printBtn, deleteBtn;

    History history;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_award_detail);
        Intent i = getIntent();
        history = i.getParcelableExtra(Global.HISTORY_OBJECT);

        dateTv = (TextView) findViewById(R.id.date);
        userFullnameTv = (TextView) findViewById(R.id.userFullName);
        userPhoneTv = (TextView) findViewById(R.id.userPhone);
        awardCodeTv = (TextView) findViewById(R.id.awardCode);
        awardNameTv = (TextView) findViewById(R.id.awardName);
        awardDetailTv = (TextView) findViewById(R.id.awardDetail);
        buyDetailTv = (TextView) findViewById(R.id.buyDetail);
        awardImageIv = (ImageView) findViewById(R.id.awardImage);
        printBtn = (ButtonRectangle) findViewById(R.id.printBtn);
        deleteBtn = (ButtonRectangle) findViewById(R.id.deleteBtn);
        totalPointTv = (TextView) findViewById(R.id.totalPoint);

        dateTv.setText(history.getTime());
        userFullnameTv.setText(history.getFullname());
        userPhoneTv.setText(history.getPhone());
        awardCodeTv.setText("ticket code: " + history.getId());
        totalPointTv.setText("total point: " + String.valueOf(history.getTotalPoint()));

        getAwardHistory();
    }

    private void getAwardHistory() {
        UserModel.getAwardHistory(Global.userToken, history.getId(), new UserModel.OngetAwardHistoryResult() {
            @Override
            public void onSuccess(final Award award, final int awardNumber) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        awardNameTv.setText(award.getName());
                        awardDetailTv.setText(award.getDescription());
                        buyDetailTv.setText(history.getFullname() + " used " + history.getTotalPoint() + " to buy " + awardNumber + " " + award.getName());
                        Picasso.with(BuyAwardDetailActivity.this).load(award.getImage()).placeholder(R.drawable.ic_award).into(awardImageIv);
                    }
                });
            }

            @Override
            public void onError(final String error) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(BuyAwardDetailActivity.this, error, Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }

}
