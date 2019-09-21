package com.axlecho.mhspider.app

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.axlecho.mhspider.app.gallery.MHGalleryAdapter
import com.axlecho.mhspider.app.gallery.MHGalleryProvider
import com.hippo.glview.view.GLRootView


class MainActivity : AppCompatActivity() {
    private val provider = MHGalleryProvider()
    private val adapter = MHGalleryAdapter(provider)
    private lateinit var glRootView: GLRootView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        glRootView = findViewById(R.id.gl_root_view)
        provider.setGLRoot(glRootView)
        provider.start()

    }

    override fun onPause() {
        super.onPause()
        glRootView.onPause()
    }

    override fun onResume() {
        super.onResume()
        glRootView.onResume()
    }
}
