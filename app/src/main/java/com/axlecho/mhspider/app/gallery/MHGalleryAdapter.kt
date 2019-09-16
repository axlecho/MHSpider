package com.axlecho.mhspider.app.gallery

import com.hippo.glgallery.GalleryPageView
import com.hippo.glgallery.GalleryView
import com.hippo.glview.image.ImageTexture

class MHGalleryAdapter(private val provider: MHGalleryProvider) : GalleryView.Adapter() {
    private val mUploader: ImageTexture.Uploader


    override fun size(): Int {
        return provider.size()
    }

    override fun onUnbind(view: GalleryPageView?, index: Int) {
        provider.cancelRequest(index)
        view?.setImage(null)
        view?.setError(null, null)
    }

    override fun getError(): String {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onBind(view: GalleryPageView?, index: Int) {
        provider.request(index)
        view?.showInfo()
        view?.setImage(null)
        view?.setPage(index + 1)
        view?.setProgress(GalleryPageView.PROGRESS_INDETERMINATE)
        view?.setError(null, null)

//        val page = mGalleryView?.findPageByIndex(index)
//        page.setImage()
    }
}