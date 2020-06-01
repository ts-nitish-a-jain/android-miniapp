package com.rakuten.tech.mobile.miniapp.display

import android.content.Context
import android.webkit.WebView
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import com.rakuten.tech.mobile.miniapp.TEST_MA_ID
import com.rakuten.tech.mobile.miniapp.js.MiniAppMessageBridge
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.amshove.kluent.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class RealMiniAppDisplayTest {
    private lateinit var context: Context
    private lateinit var basePath: String
    private lateinit var realDisplay: RealMiniAppDisplay
    private val miniAppMessageBridge: MiniAppMessageBridge = mock()

    @Before
    fun setup() {
        context = getApplicationContext()
        basePath = context.filesDir.path
        realDisplay = RealMiniAppDisplay(
            context,
            basePath = basePath,
            appId = TEST_MA_ID,
            miniAppMessageBridge = miniAppMessageBridge
        )
    }

    @Test
    fun `when destroyView be called then the miniAppWebView should be disposed`() = runBlockingTest {
        val miniAppWebView: MiniAppWebView = mock()
        realDisplay.miniAppWebView = miniAppWebView
        realDisplay.destroyView()

        verify(miniAppWebView, times(1)).destroyView()
        realDisplay.miniAppWebView shouldBe null
    }

    @Test
    fun `should provide the exact context to MiniAppWebView`() = runBlockingTest {
        val displayer = Mockito.spy(realDisplay)
        val testContext = displayer.context
        When calling displayer.isContextValid(testContext) itReturns true
        val miniAppWebView = displayer.getMiniAppView(testContext) as MiniAppWebView

        miniAppWebView.context shouldBe testContext
    }

    @Test
    fun `getMiniAppView() should be null when the context provider is not activity context`() =
        runBlockingTest {
            realDisplay.getMiniAppView() shouldBe null
        }

    @Test
    fun `for a given basePath, getMiniAppView should not return WebView to the caller`() =
        runBlockingTest {
            realDisplay.getMiniAppView(context) shouldNotHaveTheSameClassAs WebView::class
        }
}
