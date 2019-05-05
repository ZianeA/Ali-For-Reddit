package com.visualeap.aliforreddit.core.di

import com.visualeap.aliforreddit.presentation.frontPage.FrontPageFragment
import com.visualeap.aliforreddit.presentation.frontPage.FrontPageModule
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class FragmentBindingModule {

    @FragmentScope
    @ContributesAndroidInjector(modules = [FrontPageModule::class])
    abstract fun frontPageFragment(): FrontPageFragment
}