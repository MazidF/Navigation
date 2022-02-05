package com.example.netflix

import androidx.lifecycle.ViewModel

class ViewModelProfile : ViewModel() {
    lateinit var username: String
    lateinit var name: String
    lateinit var family: String
    lateinit var email: String
    lateinit var phone: String
    lateinit var birthday: String
    var hasBeenSet = false
}