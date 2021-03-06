package com.thesis.dont.loyaltypointuser.controllers;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.gc.materialdesign.views.ButtonRectangle;
import com.romainpiel.shimmer.Shimmer;
import com.romainpiel.shimmer.ShimmerTextView;
import com.thesis.dont.loyaltypointuser.R;
import com.thesis.dont.loyaltypointuser.apis.GCMHelper;
import com.thesis.dont.loyaltypointuser.models.Global;
import com.thesis.dont.loyaltypointuser.models.User;
import com.thesis.dont.loyaltypointuser.models.UserModel;


public class RegisterActivity extends ActionBarActivity {
    EditText mUsername, mPassword, mConfirmPassword, mFullname, mPhone;

    ShimmerTextView loyal;
    TextView bag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Đổi font cho app Name
        loyal = (ShimmerTextView) findViewById(R.id.loyal);
        Typeface customFont1 = Typeface.createFromAsset(getAssets(), "fonts/sweet_pea.ttf");
        loyal.setTypeface(customFont1);

        Shimmer shimmer = new Shimmer();
        shimmer.start(loyal);

        bag = (TextView) findViewById(R.id.bag);
        Typeface customFont2 = Typeface.createFromAsset(getAssets(), "fonts/orange_juice.ttf");
        bag.setTypeface(customFont2);

        mUsername = (EditText) findViewById(R.id.username);
        mPassword = (EditText) findViewById(R.id.password);
        mConfirmPassword = (EditText) findViewById(R.id.confirmPassword);
        mFullname = (EditText) findViewById(R.id.fullname);
        mPhone = (EditText) findViewById(R.id.phone);

        ButtonRectangle registerBtn = (ButtonRectangle) findViewById(R.id.registerBtn);
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String username = mUsername.getText().toString();
                String password = mPassword.getText().toString();
                String confirmPassword = mConfirmPassword.getText().toString();
                String fullname = mFullname.getText().toString();
                String phone = mPhone.getText().toString();

                // Kiểm tra khác null
                if(Helper.checkNotNull(username, password, confirmPassword, fullname, phone)) {
                    Toast.makeText(RegisterActivity.this, "please enter all the information", Toast.LENGTH_LONG).show();
                    return;
                }

                // Kiểm tra password và confirmPassword
                if(!password.equals(confirmPassword)) {
                    Toast.makeText(RegisterActivity.this, "password are not the same in two fields", Toast.LENGTH_LONG).show();
                    return;
                }

                // Kiểm tra username hợp lệ
                if(Helper.checkUserName(username)) {
                    Toast.makeText(RegisterActivity.this, "user name is not valid", Toast.LENGTH_LONG).show();
                    return;
                }

                // Kiểm tra password hợp lệ
                if(Helper.checkPassword(password)) {
                    Toast.makeText(RegisterActivity.this, "password must contain at least 6 characters", Toast.LENGTH_LONG).show();
                    return;
                }

                // Đến đây thì thông tin người dùng nhập vào đã hoàn toàn hợp lệ
                // Gọi api để đăng kí tài khoản
                final String hashPass = Helper.hashPassphrase(password, username);
                String barCode = Helper.generateBarCode(username);
                User user = new User(username, hashPass, fullname, phone, null, null, barCode, null, null);
                UserModel.setOnRegisterResult(new UserModel.OnRegisterResult() {
                    @Override
                    public void onSuccess() {
                        // Đăng kí thành công
                        /*Intent i = new Intent(RegisterActivity.this, ShopsListActivity.class);
                        startActivity(i);*/

                        UserModel.setOnLoginResult(new UserModel.OnLoginResult() {
                            @Override
                            public void onSuccess(String token) {
                                // Đăng nhập thành công
                                Global.userToken = token;

                                GCMHelper.GCMregistration(RegisterActivity.this);

                                // Lưu token vào trong shared preferences
                                SharedPreferences preferences = getSharedPreferences(LoginActivity.LOGIN_STATE, MODE_PRIVATE);
                                SharedPreferences.Editor editor = preferences.edit();
                                editor.putString(LoginActivity.TOKEN, token);

                                editor.commit();

                                GCMHelper.GCMregistration(RegisterActivity.this);

                                // Vào trang shops list
                                Intent i = new Intent(RegisterActivity.this, CardsListActivity.class);
                                startActivity(i);
                                finish();
                            }

                            @Override
                            public void onError(final String error) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(RegisterActivity.this, error, Toast.LENGTH_LONG).show();
                                    }
                                });
                            }
                        });
                        UserModel.checkUser(username, hashPass);
                    }

                    @Override
                    public void onError(final Exception e) {

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                // Đăng kí không thành công
                                if(e == null) {
                                    // trùng tài khoản
                                    Toast.makeText(RegisterActivity.this, "user name existed", Toast.LENGTH_LONG).show();
                                }else {
                                    // lỗi linh tinh
                                    Toast.makeText(RegisterActivity.this, "register unsuccessfully", Toast.LENGTH_LONG).show();
                                }

                            }
                        });
                    }
                });
                UserModel.addUser(user);
            }
        });

        ButtonRectangle cancelBtn = (ButtonRectangle) findViewById(R.id.cancelBtn);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(i);
                RegisterActivity.this.finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(RegisterActivity.this, LoginActivity.class);
        startActivity(i);
        finish();
    }
}
