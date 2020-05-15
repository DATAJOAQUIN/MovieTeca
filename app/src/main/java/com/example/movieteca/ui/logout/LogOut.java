package com.example.movieteca.ui.logout;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.movieteca.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;

public class LogOut extends Fragment {
    private FirebaseAuth mAuth;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        mAuth=FirebaseAuth.getInstance();
        mAuth.signOut();
        Intent intent =new Intent(getContext(), LoginActivity.class);
        startActivity(intent);
        getActivity().finish();

        return super.onCreateView(inflater, container, savedInstanceState);
    }
}
