package com.visualeap.aliforreddit.presentation.di

import com.visualeap.aliforreddit.presentation.main.MainActivity
import com.visualeap.aliforreddit.presentation.main.login.LoginModule
import com.visualeap.aliforreddit.presentation.main.FragmentBindingModule
import com.visualeap.aliforreddit.presentation.main.MainModule
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBindingModule {
    @ActivityScope
    @ContributesAndroidInjector(modules = [FragmentBindingModule::class, MainModule::class])
    abstract fun mainActivity(): MainActivity
}