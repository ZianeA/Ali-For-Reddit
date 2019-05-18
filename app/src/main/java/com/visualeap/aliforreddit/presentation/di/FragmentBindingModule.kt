package com.visualeap.aliforreddit.presentation.di

import com.visualeap.aliforreddit.presentation.main.frontPage.FrontPageFragment
import com.visualeap.aliforreddit.presentation.main.frontPage.FrontPageModule
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class FragmentBindingModule {

    @FragmentScope
    @ContributesAndroidInjector(modules = [FrontPageModule::class])
    abstract fun frontPageFragment(): FrontPageFragment
}