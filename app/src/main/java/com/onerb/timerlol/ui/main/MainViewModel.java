package com.onerb.timerlol.ui.main;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

public class MainViewModel extends ViewModel {

    public MutableLiveData<Boolean> showImages = new MutableLiveData<>(true);
    public MutableLiveData<String> region = new MutableLiveData<>(null);
    public MutableLiveData<String> regionRoute = new MutableLiveData<>(null);
    public MutableLiveData<String> summonerName = new MutableLiveData<>(null);
    public MutableLiveData<String> summonerId = new MutableLiveData<>(null);

    public MutableLiveData<StringBuilder> championsInfos = new MutableLiveData<>(null);

}