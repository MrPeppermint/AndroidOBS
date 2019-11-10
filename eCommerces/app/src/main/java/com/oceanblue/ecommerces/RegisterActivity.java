package com.oceanblue.ecommerces;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {


    private TextView txtPassword,txtPhoneNumber,txtUserName;
    private Button btnCreateAccount;
    private ProgressDialog loadingBar;

    private String userName,phoneNo,passwod;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        btnCreateAccount = (Button)findViewById(R.id.register_btn);
        txtPassword = (TextView)findViewById(R.id.register_password_input);
        txtPhoneNumber = (TextView)findViewById(R.id.register_Phone_number_input);
        txtUserName = (TextView)findViewById(R.id.register_username_input);
        loadingBar = new ProgressDialog(this);


        btnCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateAccount();
            }
        });
    }

    private void CreateAccount() {

        userName = txtUserName.getText().toString();
        phoneNo = txtPhoneNumber.getText().toString();
        passwod = txtPassword.getText().toString();

        if(IsValidateFeilds()!= false)
        {
            loadingBar.setTitle("Create Account");
            loadingBar.setMessage("Please Wait,While We are checking the credential");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();

            ValidatePhoneNumber(userName,phoneNo,passwod);
        }

    }

    private boolean IsValidateFeilds()
    {
        boolean isValidate = true;

        if(TextUtils.isEmpty(userName))
        {
            isValidate = false;
            Toast.makeText(this,"Please Enter Your Name",Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(phoneNo) && phoneNo.length()<10)
        {
            isValidate = false;
            Toast.makeText(this,"Please Enter Your Mobile Phone Number",Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(passwod))
        {
            isValidate = false;
            Toast.makeText(this,"Please Enter Validate Password",Toast.LENGTH_SHORT).show();
        }
        return isValidate;
    }

    private void ValidatePhoneNumber(final String userName, final String phoneNo, final String password)
    {

        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();

        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(!(dataSnapshot.child("Users").child(phoneNo).exists()))
                {
                    HashMap<String, Object> userdataHashMap = new HashMap<>();
                    userdataHashMap.put("phoneNo",phoneNo);
                    userdataHashMap.put("userName",userName);
                    userdataHashMap.put("password",password);

                    RootRef.child("Users").child(phoneNo).updateChildren(userdataHashMap)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task)
                                {
                                    if(task.isSuccessful())
                                    {
                                        Toast.makeText(RegisterActivity.this,"Congratulations, Your Account has been created",Toast.LENGTH_SHORT).show();
                                        loadingBar.dismiss();

                                        ClearFields();

                                        Intent intent = new Intent(RegisterActivity.this,LoginActivity.class);
                                        startActivity(intent);
                                    }
                                    else
                                    {
                                        loadingBar.dismiss();
                                        Toast.makeText(RegisterActivity.this,"Network Error: Please try again",Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
                else
                {
                    Toast.makeText(RegisterActivity.this,"This " +phoneNo+ " Number already exists",Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
                    Toast.makeText(RegisterActivity.this,"Please try again using another phone number",Toast.LENGTH_SHORT).show();

                   /* Intent intent = new Intent(RegisterActivity.this,MainActivity.class);
                    startActivity(intent);*/
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void ClearFields()
    {
        txtPassword.setText("");
        txtPhoneNumber.setText("");
        txtUserName.setText("");
    }

}



