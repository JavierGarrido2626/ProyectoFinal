package com.example.proyectofinal_javiergarrido.ui.manual;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ManualViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public ManualViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is slideshow fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}