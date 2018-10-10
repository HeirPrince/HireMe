package com.nassaty.hireme.viewmodels;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

public class SharedViewModel extends ViewModel {
	private final MutableLiveData<String> selected = new MutableLiveData<>();

	public void select(String query) {
		selected.setValue(query);
	}

	public LiveData<String> getSelected() {
		return selected;
	}
}
