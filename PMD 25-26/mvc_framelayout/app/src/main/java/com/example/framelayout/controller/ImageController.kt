package com.example.framelayout.controller

import com.example.framelayout.model.ImageModel


// CONTROLLER: logika eta View-Model komunikazioa
class ImageController(private val view: ImageViewLayer) {

    private val model = ImageModel()

    interface ImageViewLayer {
        fun showImage()
        fun hideImage()
    }

    fun toggleImage() {
        model.isVisible = !model.isVisible
        if (model.isVisible) view.showImage()
        else view.hideImage()
    }
}