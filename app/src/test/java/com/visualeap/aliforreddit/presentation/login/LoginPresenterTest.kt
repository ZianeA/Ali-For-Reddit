package com.visualeap.aliforreddit.presentation.login

import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.ArgumentMatchers
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoRule

class LoginPresenterTest {

    @Rule
    @JvmField
    val rule: MockitoRule = MockitoJUnit.rule()

    @Mock
    private lateinit var view: LoginView

    private lateinit var presenter: LoginPresenter

    companion object{
        private const val REDIRECT_URL = "https://example.com"
    }

    @Before
    fun setUp() {
        presenter = LoginPresenter(view, "", REDIRECT_URL)
    }

    @Test
    fun passUrlToView() {
        //Act
        presenter.start()

        //Assert
        verify(view).showLoginPage(ArgumentMatchers.anyString())
    }

    @Test
    fun loginFinished_hideLoginUi() {
        //Arrange

        //Act
        presenter.onPageStarted(REDIRECT_URL)

        //Assert
        verify(view).hideLoginPage()
    }

    @Test
    fun `loginHasn'tFinished_don'tHideLoginUi`() {
        //Act
        presenter.onPageStarted("")

        //Assert
        verify(view, never()).hideLoginPage()
    }
}