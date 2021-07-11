package com.example.prostosklad_mobile

import android.os.Bundle

import androidx.appcompat.app.AppCompatActivity
import com.example.prostosklad_mobile.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var activity: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity = ActivityMainBinding.inflate(layoutInflater)

        setContentView(activity.root)
    }
}