package com.mifos.mifosxdroid.passcode;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.AppCompatButton;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.mifos.mifosxdroid.R;
import com.mifos.mifosxdroid.core.MifosBaseActivity;
import com.mifos.mifosxdroid.core.util.Toaster;
import com.mifos.mifosxdroid.login.LoginActivity;
import com.mifos.mifosxdroid.online.DashboardActivity;
import com.mifos.utils.Constants;
import com.mifos.utils.EncryptionUtil;
import com.mifos.utils.Network;
import com.mifos.utils.PassCodeView;
import com.mifos.utils.PrefManager;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PassCodeActivity extends MifosBaseActivity {

    @BindView(R.id.cl_rootview)
    NestedScrollView clRootview;

    @BindView(R.id.btn_login)
    AppCompatButton btnLogin;

    @BindView(R.id.btn_forgot_passcode)
    AppCompatButton btnForgotPasscode;

    @BindView(R.id.pv_passcode)
    PassCodeView passCodeView;

    @BindView(R.id.btn_skip)
    AppCompatButton btnSkip;

    @BindView(R.id.btn_save)
    AppCompatButton btnSave;

    @BindView(R.id.tv_passcode)
    TextView tvPasscodeIntro;

    @BindView(R.id.iv_visibility)
    ImageView ivVisibility;

    private int counter = 0;
    private boolean isInitialScreen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pass_code);
        getActivityComponent().inject(this);
        ButterKnife.bind(this);

        isInitialScreen = getIntent().getBooleanExtra(Constants.INTIAL_LOGIN, false);

        if (PrefManager.getPassCodeStatus()) {
            btnSkip.setVisibility(View.GONE);
            btnSave.setVisibility(View.GONE);
            tvPasscodeIntro.setVisibility(View.GONE);
            btnLogin.setVisibility(View.VISIBLE);
            btnForgotPasscode.setVisibility(View.VISIBLE);
        }
    }

    @OnClick(R.id.btn_skip)
    public void skip() {
        startDashBoardActivity();
    }

    @OnClick(R.id.btn_save)
    public void savePassCode() {
        if (isPassCodeLengthCorrect()) {
            PrefManager.setPassCode(EncryptionUtil.getHash(passCodeView.getPasscode()));
            startDashBoardActivity();
        }
    }

    @OnClick(R.id.btn_login)
    public void loginUsingPassCode() {

        if (!isInternetAvailable()) {
            return;
        }

        if (counter == 3) {
            Toaster.show(clRootview, R.string.incorrect_passcode_more_than_three);
            PrefManager.clearPrefs();
            startLoginActivity();
            return;
        }

        if (isPassCodeLengthCorrect()) {
            String passwordEntered = EncryptionUtil.getHash(passCodeView.getPasscode());
            if (PrefManager.getPassCode().equals(passwordEntered)) {
                startDashBoardActivity();
            } else {
                counter++;
                passCodeView.clearPasscodeField();
                Toaster.show(clRootview, R.string.incorrect_passcode);
            }
        }
    }

    @OnClick(R.id.btn_forgot_passcode)
    public void forgotPassCode() {
        PrefManager.clearPrefs();
        startLoginActivity();
    }

    private boolean isInternetAvailable() {
        if (Network.isOnline(this)) {
            return true;
        } else {
            Toaster.show(clRootview, getString(R.string.error_not_connected_internet));
            return false;
        }
    }

    @OnClick(R.id.btn_one)
    public void clickedOne() {
        passCodeView.enterCode(getString(R.string.one));
    }

    @OnClick(R.id.btn_two)
    public void clickedTwo() {
        passCodeView.enterCode(getString(R.string.two));
    }

    @OnClick(R.id.btn_three)
    public void clickedThree() {
        passCodeView.enterCode(getString(R.string.three));
    }

    @OnClick(R.id.btn_four)
    public void clickedFour() {
        passCodeView.enterCode(getString(R.string.four));
    }

    @OnClick(R.id.btn_five)
    public void clickedFive() {
        passCodeView.enterCode(getString(R.string.five));
    }

    @OnClick(R.id.btn_six)
    public void clickedSix() {
        passCodeView.enterCode(getString(R.string.six));
    }

    @OnClick(R.id.btn_seven)
    public void clickedSeven() {
        passCodeView.enterCode(getString(R.string.seven));
    }

    @OnClick(R.id.btn_eight)
    public void clickedEight() {
        passCodeView.enterCode(getString(R.string.eight));
    }

    @OnClick(R.id.btn_nine)
    public void clickedNine() {
        passCodeView.enterCode(getString(R.string.nine));
    }

    @OnClick(R.id.btn_zero)
    public void clickedZero() {
        passCodeView.enterCode(getString(R.string.zero));
    }

    @OnClick(R.id.btn_back)
    public void clickedBackSpace() {
        passCodeView.backSpace();
    }

    @OnClick(R.id.iv_visibility)
    public void visibilityChange() {
        passCodeView.revertPassCodeVisibility();
        if (!passCodeView.passcodeVisible()) {
            ivVisibility.setColorFilter(ContextCompat.getColor(PassCodeActivity.this,
                    R.color.light_grey));
        } else {
            ivVisibility.setColorFilter(ContextCompat.getColor(PassCodeActivity.this,
                    R.color.gray_dark));
        }
    }
    private boolean isPassCodeLengthCorrect() {
        if (passCodeView.getPasscode().length() == 4) {
            return true;
        }
        Toaster.show(clRootview, getString(R.string.error_passcode));
        return false;
    }

    private void startDashBoardActivity() {
        startActivity(new Intent(this, DashboardActivity.class));
        finish();
    }

    private void startLoginActivity() {
        Intent i = new Intent(PassCodeActivity.this, LoginActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
        finish();
    }

    @Override
    public void onBackPressed() {
        //enabling back press only for initial login.
        if (isInitialScreen) {
            super.onBackPressed();
        }
    }
}
