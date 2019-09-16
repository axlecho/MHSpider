package com.axlecho.mhspider.app.gallery

import com.hippo.glgallery.GalleryProvider

class MHGalleryProvider : GalleryProvider() {

    override fun start() {
        super.start()
    }

    override fun getError(): String {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onCancelRequest(index: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onForceRequest(index: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun size(): Int {
       return 10
    }

    override fun onRequest(index: Int) {
        notifyPageFailed(index, "Test")
    }
}