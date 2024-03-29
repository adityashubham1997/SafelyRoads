package com.example.admin.myapplication;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.accountkit.Account;
import com.facebook.accountkit.AccountKit;
import com.facebook.accountkit.AccountKitCallback;
import com.facebook.accountkit.AccountKitError;
import com.facebook.accountkit.PhoneNumber;
import com.facebook.accountkit.ui.AccountKitActivity;
import com.facebook.accountkit.ui.AccountKitConfiguration;
import com.facebook.accountkit.ui.LoginType;
import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;

import java.util.Locale;

public class AccountActivity extends AppCompatActivity {

    TextView id;
    TextView infoLabel;
    TextView info;
    TextView nameText;
    TextView bloodGroup;
    TextView sex;
    String userNumber;
    String name;
     String email;
     String blood;
     String gender;
     public static  final String DEFAULT ="N/A";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        com.example.admin.myapplication.FontHelper.setCustomTypeface(findViewById(R.id.view_root));

        ActivityCompat.requestPermissions(AccountActivity.this, new String[]{Manifest.permission.RECEIVE_SMS, Manifest.permission.READ_SMS},1);

        AccountKitConfiguration.AccountKitConfigurationBuilder configurationBuilder =
                new AccountKitConfiguration.AccountKitConfigurationBuilder(LoginType.PHONE
                        , AccountKitActivity.ResponseType.TOKEN);

        // Add these code
        configurationBuilder.setReadPhoneStateEnabled(true);
        configurationBuilder.setReceiveSMS(true);
        id = (TextView) findViewById(R.id.id);
        infoLabel = (TextView) findViewById(R.id.info_label);
        info = (TextView) findViewById(R.id.info);
bloodGroup=(TextView)findViewById(R.id.bloodText);
nameText=(TextView)findViewById(R.id.NameText);
        sex=(TextView)findViewById(R.id.sex);
        final SignUpActivity signup = new SignUpActivity();
        AccountKit.getCurrentAccount(new AccountKitCallback<Account>() {
            @Override
            public void onSuccess(final Account account) {
                PhoneNumber phoneNumber = account.getPhoneNumber();
                userNumber = phoneNumber.toString();
                if (account.getPhoneNumber() != null) {
                    // if the phone number is available, display it
                    String formattedPhoneNumber = formatPhoneNumber(phoneNumber.toString());
                    info.setText(formattedPhoneNumber);
                    infoLabel.setText(R.string.phone_label);
                    getData();
                    nameText.setText(name);
                    bloodGroup.setText(blood);
                    id.setText(email);
                    sex.setText(gender);
                }

            }


            @Override
            public void onError(final AccountKitError error) {
                // display error
                String toastMessage = error.getErrorType().getMessage();
                Toast.makeText(AccountActivity.this, toastMessage, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void getData()
    {

        SharedPreferences sharedPreferences = getSharedPreferences("MyData", Context.MODE_PRIVATE);
        name = sharedPreferences.getString("name",DEFAULT);
        blood = sharedPreferences.getString("blood",DEFAULT);
        email = sharedPreferences.getString("email",DEFAULT);
        gender = sharedPreferences.getString("sex",DEFAULT);


    }


    public void onLogout(View view) {
        // logout of Account Kit
        AccountKit.logOut();
        launchLoginActivity();
    }
    public void onEditDetails(View view)
    {
        Intent intent = new Intent(this, com.example.admin.myapplication.SignUpActivity.class);
        startActivity(intent);
        finish();
    }
    private void launchLoginActivity() {
        Intent intent = new Intent(this, com.example.admin.myapplication.LoginActivity.class);
        startActivity(intent);
        finish();
    }
    public void launchAutomatic(View view)
    {
        Intent intent = new Intent(this, com.example.admin.myapplication.Automatic.class);
        startActivity(intent);
        //finish();
    }

    private String formatPhoneNumber(String phoneNumber) {
        // helper method to format the phone number for display
        try {
            PhoneNumberUtil pnu = PhoneNumberUtil.getInstance();
            Phonenumber.PhoneNumber pn = pnu.parse(phoneNumber, Locale.getDefault().getCountry());
            phoneNumber = pnu.format(pn, PhoneNumberUtil.PhoneNumberFormat.NATIONAL);
        } catch (NumberParseException e) {
            e.printStackTrace();
        }
        return phoneNumber;
    }

}
