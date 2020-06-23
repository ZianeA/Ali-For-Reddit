package com.visualeap.aliforreddit.presentation.common.di

import com.visualeap.aliforreddit.presentation.frontPage.container.FrontPageContainerFragment
import com.visualeap.aliforreddit.presentation.frontPage.container.FrontPageContainerModule
import com.visualeap.aliforreddit.presentation.frontPage.FrontPageFragment
import com.visualeap.aliforreddit.presentation.frontPage.FrontPageModule
import com.visualeap.aliforreddit.presentation.login.LoginFragment
import com.visualeap.aliforreddit.presentation.login.LoginModule
import com.visualeap.aliforreddit.presentation.postDetail.PostDetailFragment
import com.visualeap.aliforreddit.presentation.postDetail.PostDetailModule
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