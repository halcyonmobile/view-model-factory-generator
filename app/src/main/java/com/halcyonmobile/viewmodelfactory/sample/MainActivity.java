package com.halcyonmobile.viewmodelfactory.sample;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;


import javax.inject.Inject;

import dagger.android.AndroidInjection;


public class MainActivity extends AppCompatActivity {

    @Inject
    MainViewModelFactoryBuilder factoryBuilder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AndroidInjection.inject(this);

        ViewModelProviders.of(this, factoryBuilder.build()).get(MainViewModel.class);
    }
}
