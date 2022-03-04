package com.finite.digi_libraryphcet.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SharedViewModel : ViewModel() {

    var vmBookId = MutableLiveData<String>("default")
    var scancode = "default"
}