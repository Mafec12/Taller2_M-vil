package com.example.taller2movil.componentes

import android.net.Uri
import android.widget.ImageView
import android.widget.Toast
import androidx.camera.core.ImageCapture
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.taller2movil.camara.VistaCamara
import com.example.taller2movil.camara.guardarFoto
import com.example.taller2movil.datos.Foto

@Composable
fun PanelCamara(
    fotos: List<Foto>,
    camaraFrontal: Boolean,
    alTomarFoto: (Foto) -> Unit,
    alCambiarCamara: () -> Unit,
    modifier: Modifier = Modifier
) {
    val contexto = LocalContext.current

    var capturaLista by remember {
        mutableStateOf(false)
    }

    var imagenCaptura by remember {
        mutableStateOf<ImageCapture?>(null)
    }

    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {
        Column(
            Modifier
                .fillMaxSize()
                .padding(12.dp)
        ) {
            Text(
                text = "Cámara",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )

            Spacer(Modifier.height(10.dp))

            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .background(
                        Color(0xFF20252B),
                        RoundedCornerShape(12.dp)
                    )
            ) {
                VistaCamara(
                    camaraFrontal = camaraFrontal,
                    alPrepararCaptura = {
                        imagenCaptura = it
                        capturaLista = true
                    },
                    modifier = Modifier.fillMaxSize()
                )
            }

            Spacer(Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = {
                        val captura = imagenCaptura

                        if (captura != null && capturaLista) {
                            guardarFoto(
                                contexto,
                                captura
                            ) { foto ->
                                alTomarFoto(foto)
                            }
                        } else {
                            Toast.makeText(
                                contexto,
                                "La cámara todavía no está lista",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    },
                    modifier = Modifier.weight(1f),
                    enabled = capturaLista
                ) {
                    Text("Tomar foto")
                }

                Button(
                    onClick = {
                        capturaLista = false
                        imagenCaptura = null
                        alCambiarCamara()
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Cambiar cámara")
                }
            }

            Spacer(Modifier.height(8.dp))

            Text(
                text = "Fotos recientes (${fotos.size})",
                fontWeight = FontWeight.SemiBold
            )

            Spacer(Modifier.height(6.dp))

            if (fotos.isEmpty()) {
                Text(
                    text = "Todavía no hay fotos",
                    color = Color.Gray
                )
            } else {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(
                            rememberScrollState()
                        ),
                    horizontalArrangement =
                        Arrangement.spacedBy(8.dp)
                ) {
                    fotos.asReversed().forEach { foto ->
                        Column(
                            modifier = Modifier.size(
                                width = 90.dp,
                                height = 112.dp
                            )
                        ) {
                            AndroidView(
                                factory = { context ->
                                    ImageView(context).apply {
                                        scaleType =
                                            ImageView.ScaleType.CENTER_CROP
                                        setImageURI(Uri.parse(foto.uri))
                                    }
                                },
                                update = { imageView ->
                                    imageView.setImageURI(
                                        Uri.parse(foto.uri)
                                    )
                                },
                                modifier = Modifier
                                    .size(90.dp, 82.dp)
                            )

                            Spacer(
                                Modifier.height(4.dp)
                            )

                            Text(
                                text = foto.nombre,
                                style = MaterialTheme
                                    .typography.labelSmall,
                                maxLines = 2
                            )
                        }
                    }
                }
            }
        }
    }
}
