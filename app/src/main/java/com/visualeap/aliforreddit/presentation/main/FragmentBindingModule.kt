package com.visualeap.aliforreddit.presentation.main

import com.visualeap.aliforreddit.presentation.di.FragmentScope
import com.visualeap.aliforreddit.presentation.main.frontPage.container.FrontPageContainerFragment
import com.visualeap.aliforreddit.presentation.main.frontPage.container.FrontPageContainerModule
import com.visualeap.aliforreddit.presentation.main.frontPage.FrontPageFragment
import com.visualeap.aliforreddit.presentation.main.frontPage.FrontPageModule
import com.visualeap.aliforreddit.presentation.main.login.LoginFragment
import com.visualeap.aliforreddit.presentation.main.login.LoginModule
import com.visualeap.aliforreddit.presentation.main.postDetail.PostDetailFragment
import com.visualeap.aliforreddit.presentation.main.postDetail.PostDetailModule
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class FragmentBindingModule {
    @FragmentScope
    @ContributesAndroidInjector(modules = [FrontPageModule::class])
    abstract fun frontPageFragment(): FrontPageFragment

    @FragmentScope
    @ContributesAndroidInjector(modules = [PostDetailModule::class])
    abstract fun postDetailFragment(): PostDetailFragment

    @FragmentScope
    @ContributesAndroidInjector(modules = [FrontPageContainerModule::class])
    abstract fun frontPageContainerFragment(): FrontPageContainerFragment

    @FragmentScope
    @ContributesAndroidInjector(modules = [LoginModule::class])
    abstract fun loginFragment(): LoginFragment
}