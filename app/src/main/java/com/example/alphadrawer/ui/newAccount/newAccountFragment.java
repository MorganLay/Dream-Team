package com.example.alphadrawer.ui.newAccount;

import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.alphadrawer.MainActivity;
import com.example.alphadrawer.R;

public class newAccountFragment extends Fragment {

    private newAccountViewModel mViewModel;

    public static newAccountFragment newInstance() {
        return new newAccountFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.new_account_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        final Button newAccount = view.findViewById(R.id.newAccountButton);
        newAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), MainActivity.class);
                Boolean user = true;
                intent.putExtra("user", user);
                startActivity(intent);
            }
        });
    }

//    @Override
//    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
//        super.onActivityCreated(savedInstanceState);
//        mViewModel = new ViewModelProvider(this).get(newAccountViewModel.class);
//        // TODO: Use the ViewModel
//    }

}