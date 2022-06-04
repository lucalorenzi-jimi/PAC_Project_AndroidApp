package com.example.myapplication;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ResetPWDDialog extends AppCompatDialogFragment {

    private EditText editTextEmail;
    CustomerAPI customerAPI;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_dialog, null);
        builder.setView(view).setTitle("Forgot password").setMessage("Insert the email of the account:").setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        }).setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(validateEmail()) {

                    Retrofit retrofit = new Retrofit.Builder().baseUrl("http://10.0.0.10:8080/").addConverterFactory(GsonConverterFactory.create()).build();

                    customerAPI = retrofit.create(CustomerAPI.class);
                    Toast.makeText(getContext(),"Check you inbox to reset the password.", Toast.LENGTH_LONG).show();
                    customerAPI.resetPWDCustomer(editTextEmail.getText().toString()).enqueue(new Callback<Customer>() {
                        @Override
                        public void onResponse(Call<Customer> call, Response<Customer> response) {
                            if(!response.isSuccessful()) {
                                Toast.makeText(getContext(),"This email is not related to any account.", Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(getContext(),"Check you inbox to reset the password.", Toast.LENGTH_LONG).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<Customer> call, Throwable t) {
                            Toast.makeText(getContext(),"Error contacting server, please retry.", Toast.LENGTH_LONG).show();
                        }
                    });

                    /*try {
                        Response<Customer> result = customerAPI.resetPWDCustomer(editTextEmail.getText().toString()).execute();
                        Toast.makeText(getContext(),result.toString(), Toast.LENGTH_LONG).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }*/

                } else {
                    Toast.makeText(getContext(),"Please enter a valid email address.", Toast.LENGTH_LONG).show();
                }
            }
        });

        editTextEmail = view.findViewById(R.id.editEmailForgotPWD);

        return builder.create();
    }

    public boolean validateEmail() {
        String emailInput = editTextEmail.getText().toString().trim();

        if (emailInput.isEmpty()) {
            editTextEmail.setError("Field can't be empty!");
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(emailInput).matches()) {
            editTextEmail.setError("Please enter a valid email address.");
            return false;
        } else {
            editTextEmail.setError(null);
            return true;
        }
    }
}
