package com.example.taller2movil.camara

import android.content.ContentValues
import android.content.Context
import android.provider.MediaStore
import android.widget.Toast
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.example.taller2movil.datos.Foto
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun VistaCamara(
    camaraFrontal: Boolean,
    alPrepararCaptura: (ImageCapture) -> Unit,
    modifier: Modifier = Modifier
) {
    val contexto = LocalContext.current
    val ciclo = LocalLifecycleOwner.current

    key(camaraFrontal) {
        val previewView = remember {
            PreviewView(contexto).apply {
                scaleType = PreviewView.ScaleType.FILL_CENTER
                implementationMode = PreviewView.ImplementationMode.COMPATIBLE
            }
        }

        DisposableEffect(camaraFrontal, ciclo) {
            val providerFuture = ProcessCameraProvider.getInstance(contexto)
            val executor = ContextCompat.getMainExecutor(contexto)

            providerFuture.addListener({
                try {
                    val provider = providerFuture.get()
                    val selector = CameraSelector.Builder()
                        .requireLensFacing(
                            if (camaraFrontal) CameraSelector.LENS_FACING_FRONT
                            else CameraSelector.LENS_FACING_BACK
                        )
                        .build()

                    val preview = Preview.Builder().build().also {
                        it.surfaceProvider = previewView.surfaceProvider
                    }
                    val capture = ImageCapture.Builder()
                        .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
                        .build()

                    provider.unbindAll()
                    provider.bindToLifecycle(ciclo, selector, preview, capture)
                    alPrepararCaptura(capture)
                } catch (error: Exception) {
                    error.printStackTrace()
                    Toast.makeText(
                        contexto, "No se pudo iniciar la cámara",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }, executor)

            onDispose {
                try {
                    if (providerFuture.isDone) providerFuture.get().unbindAll()
                } catch (error: Exception) {
                    error.printStackTrace()
                }
            }
        }

        AndroidView(factory = { previewView }, modifier = modifier)
    }
}

fun guardarFoto(
    contexto: Context,
    imageCapture: ImageCapture,
    alGuardar: (Foto) -> Unit
) {
    val fecha = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
    val nombre = "Ruta_$fecha.jpg"
    val values = ContentValues().apply {
        put(MediaStore.Images.Media.DISPLAY_NAME, nombre)
        put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
        put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/RutaDeFotos")
    }

    val opciones = ImageCapture.OutputFileOptions.Builder(
        contexto.contentResolver,
        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
        values
    ).build()

    imageCapture.takePicture(
        opciones,
        ContextCompat.getMainExecutor(contexto),
        object : ImageCapture.OnImageSavedCallback {
            override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                val uri = output.savedUri
                if (uri != null) {
                    Toast.makeText(
                        contexto, "¡Foto guardada en la galería!",
                        Toast.LENGTH_SHORT
                    ).show()
                    alGuardar(
                        Foto(
                            nombre = nombre,
                            uri = uri.toString(),
                            latitud = 0.0,
                            longitud = 0.0
                        )
                    )
                } else {
                    Toast.makeText(
                        contexto, "No se obtuvo la URI de la foto",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onError(exception: ImageCaptureException) {
                Toast.makeText(
                    contexto, "Error al guardar: ${exception.message}",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    )
}
