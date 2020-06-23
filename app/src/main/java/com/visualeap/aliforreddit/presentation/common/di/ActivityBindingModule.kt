package com.visualeap.aliforreddit.presentation.common.di

import com.visualeap.aliforreddit.presentation.common.main.MainActivity
import com.visualeap.aliforreddit.presentation.common.main.MainModule
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBindingModule {
    @ActivityScope
    @ContributesAndroidInjector(modules = [FragmentBindingModule::class, MainModule::class])
    abstract fun mainActivity(): MainActivity
}