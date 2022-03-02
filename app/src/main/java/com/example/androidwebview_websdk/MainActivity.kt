package com.example.androidwebview_websdk

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.os.Message
import android.view.KeyEvent
import android.view.View
import android.view.WindowManager
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebView.WebViewTransport
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        webView.webViewClient = WebViewClient();
        // this will load the url of the website
        webView.loadUrl("https://f20f-91-220-189-227.ngrok.io");

        webView.settings.javaScriptEnabled = true;
        webView.settings.setSupportMultipleWindows(true);
        webView.settings.javaScriptCanOpenWindowsAutomatically = true
        WebView.setWebContentsDebuggingEnabled(true);


        webView.webChromeClient = object : WebChromeClient() {
            override fun onCreateWindow(
                view: WebView,
                isDialog: Boolean,
                isUserGesture: Boolean,
                resultMsg: Message
            ): Boolean {
                val newWebView = WebView(this@MainActivity)
                val webSettings = newWebView.settings
                webSettings.javaScriptEnabled = true;

                // Other configuration comes here, such as setting the WebViewClient
                val dialog = Dialog(this@MainActivity)
                dialog.setOnKeyListener(DialogInterface.OnKeyListener { dialog, keyCode, event ->
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                        newWebView.evaluateJavascript("(function() { window.close() })();", null)
                        dialog.dismiss();
                    }
                    false
                })

                dialog.setContentView(newWebView);
                dialog.show();
                dialog.window?.setLayout(
                    WindowManager.LayoutParams.MATCH_PARENT,
                    WindowManager.LayoutParams.MATCH_PARENT,
//                  WindowManager.LayoutParams.FIRST_SUB_WINDOW
                );

                newWebView.webViewClient = WebViewClient();


                newWebView.webChromeClient = object : WebChromeClient() {

                    override fun onCloseWindow(window: WebView) {
//                        newWebView.evaluateJavascript("(function() { document.getElementById('btn_close').click(); })();", null)
                        dialog.dismiss();
                    }
                }
                (resultMsg.obj as WebViewTransport).webView = newWebView
                resultMsg.sendToTarget()
                return true
            }
        }

    }
}
