package com.oceanblue.ecommerces;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.oceanblue.ecommerces.Model.Users;

public class LoginActivity extends AppCompatActivity {

    private EditText txtPhoneNo,txtPassword;
    private Button btnLogin;
    private ProgressDialog loadingBar;

    private String phoneNo,passwod;
    private TextView AdminLink,NotAdminLink;

    private String parrentDbName = "Users";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        txtPhoneNo = (EditText)findViewById(R.id.login_Phone_number_input);
        txtPassword = (EditText)findViewById(R.id.login_password_input);
        btnLogin = (Button)findViewById(R.id.login_btn);
        AdminLink = (TextView) findViewById(R.id.admin_panel_link);
        NotAdminLink= (TextView) findViewById(R.id.not_admin_panel_link);
        loadingBar = new ProgressDialog(this);


        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginUser();
            }
        });


        AdminLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                btnLogin.setText("Login Admin");
                AdminLink.setVisibility(View.INVISIBLE);
                NotAdminLink.setVisibility(View.VISIBLE);
                parrentDbName = "Admins";

            }
        });
        NotAdminLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnLogin.setText("Login");
                AdminLink.setVisibility(View.VISIBLE);
                NotAdminLink.setVisibility(View.INVISIBLE);
                parrentDbName = "Users";
            }
        });
    }

    private void LoginUser()
    {
        phoneNo = txtPhoneNo.getText().toString();
        passwod = txtPassword.getText().toString();

        if(IsValidateFeilds()!= false)
        {
            loadingBar.setTitle("Login Account");
            loadingBar.setMessage("Please Wait,While We are checking the credential");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();

            AllowAccessToAccount(phoneNo,passwod);

        }


    }

    private void AllowAccessToAccount(final String phoneNo, final String passwod)
    {
        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();


        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.child(parrentDbName).child(phoneNo).exists())
                {
                    Users userData = dataSnapshot.child(parrentDbName).child(phoneNo).getValue(Users.class);
                    if(userData.getPhoneNo().equals(phoneNo))
                    {
                        if(userData.getPassword().equals(passwod))
                        {

                            if(parrentDbName.equals("Admins"))
                            {
                                Toast.makeText(LoginActivity.this,"Welcome Admin,Logged in Successfully",Toast.LENGTH_SHORT).show();
                                loadingBar.dismiss();

                                Intent intent = new Intent(LoginActivity.this,AdminCategoryActivity.class);
                                startActivity(intent);
                            }
                            else if(parrentDbName.equals("Users"))
                            {
                                Toast.makeText(LoginActivity.this,"Logged in Successfully",Toast.LENGTH_SHORT).show();
                                loadingBar.dismiss();

                                Intent intent = new Intent(LoginActivity.this,HomeActivity.class);
                                startActivity(intent);
                            }
                        }
                        else
                        {
                            loadingBar.dismiss();
                            Toast.makeText(LoginActivity.this,"Password is Incorrect",Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                else
                {
                    Toast.makeText(LoginActivity.this,"Account with this " +phoneNo+ " number do not exists",Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private boolean IsValidateFeilds()
    {
        boolean isValidate = true;
        if(TextUtils.isEmpty(phoneNo) && phoneNo.length()<10)
        {
            isValidate = false;
            Toast.makeText(this,"Please Enter Your Mobile Phone Number Correctly",Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(passwod))
        {
            isValidate = false;
            Toast.makeText(this,"Please Enter Validate Password",Toast.LENGTH_SHORT).show();
        }
        return isValidate;
    }
}
