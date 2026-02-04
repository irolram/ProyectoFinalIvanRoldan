package com.example.proyectofinalivanroldan.util


import androidx.annotation.OptIn
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.common.InputImage

/**
 * Analizador de frames de cámara optimizado para la detección de simbología QR.
 * * Implementa la interfaz [ImageAnalysis.Analyzer] de CameraX, integrando el SDK de
 * [ML Kit] para el procesamiento de imágenes en tiempo real. La clase gestiona la
 * conversión de buffers de imagen nativos a [InputImage] y procesa de forma
 * asíncrona los metadatos de los códigos de barras, notificando mediante un
 * callback la detección exitosa del identificador del tutor.
 */
class QrAnalyzer(
    private val onQrDetected: (String) -> Unit
) : ImageAnalysis.Analyzer {

    private val scanner = BarcodeScanning.getClient()

    @OptIn(ExperimentalGetImage::class)
    override fun analyze(imageProxy: ImageProxy) {
        val mediaImage = imageProxy.image
        if (mediaImage != null) {
            val image = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)
            scanner.process(image)
                .addOnSuccessListener { barcodes ->
                    for (barcode in barcodes) {
                        barcode.rawValue?.let { onQrDetected(it) }
                    }
                }
                .addOnCompleteListener {
                    imageProxy.close()
                }
        }
    }
}