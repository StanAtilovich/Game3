package ru.stan.game3

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class HomeViewModel: ViewModel() {
    var tvScore2Value: MutableLiveData<Int> = MutableLiveData(0)
}