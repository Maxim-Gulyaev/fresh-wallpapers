package com.maxim.freshwallpapers.di

import android.content.SharedPreferences
import com.google.gson.Gson
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@EntryPoint
@InstallIn(SingletonComponent::class)
interface LikedImageHelperEntryPoint {
    @LikedImagesPrefs
    fun sharedPreferences(): SharedPreferences
    fun gson(): Gson
}