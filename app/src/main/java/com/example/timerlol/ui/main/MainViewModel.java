package com.example.timerlol.ui.main;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MainViewModel extends ViewModel {
    public MutableLiveData<Boolean> showImages = new MutableLiveData<>(true);
    public MutableLiveData<Integer> countImage = new MutableLiveData<>(0);

}