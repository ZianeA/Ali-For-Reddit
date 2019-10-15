package com.visualeap.aliforreddit.presentation.di

import com.visualeap.aliforreddit.presentation.main.MainActivity
import com.visualeap.aliforreddit.presentation.login.LoginActivity
import com.visualeap.aliforreddit.presentation.login.LoginModule
import com.visualeap.aliforreddit.presentation.main.FragmentBindingModule
import com.visualeap.aliforreddit.presentation.main.MainModule
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBindingModule {
    @ActivityScope
    @ContributesAndroidInjector(modules = [FragmentBindingModule::class, MainModule::class])
    abstract fun mainActivity(): MainActivity

    //TODO Don't forget to remove this if you remove LoginActivity
    @ActivityScope
    @ContributesAndroidInjector(modules = [LoginModule.ActivityModule::class])
    abstract fun loginActivity(): LoginActivity
}