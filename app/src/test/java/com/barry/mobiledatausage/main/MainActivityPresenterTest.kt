package com.barry.mobiledatausage.main

import com.barry.mobiledatausage.MainActivityContract
import com.barry.mobiledatausage.MainActivityPresenter
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations


class MainActivityPresenterTest {

    @Mock
    private lateinit var mView: MainActivityContract.View

    private lateinit var mMainActivityPresenter: MainActivityPresenter

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)

        mMainActivityPresenter = MainActivityPresenter(mView)
    }

    @Test
    fun callDataUsageApi_showProgress() {
        mMainActivityPresenter.callDataUsageApi()

        verify<MainActivityContract.View>(mView).showProgressDialog()
    }

    @After
    @Throws
    fun tearDown() {

    }
}
