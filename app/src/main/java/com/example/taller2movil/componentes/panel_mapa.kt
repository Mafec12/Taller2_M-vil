package com.example.taller2movil.componentes

import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.viewinterop.AndroidView
import com.mapbox.maps.MapView
import com.mapbox.maps.Style
import com.mapbox.maps.CameraOptions
import com.mapbox.geojson.Point
import com.mapbox.maps.plugin.annotation.annotations
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationOptions
import com.mapbox.maps.plugin.annotation.generated.createPointAnnotationManager
import com.example.taller2movil.datos.Foto

@Composable
fun PanelMapa(
    rutaIniciada: Boolean,
    fotos: List<Foto>,
    latitud: Double?,
    longitud: Double?,
    mensajeUbicacion: String,
    alIniciar: () -> Unit,
    alLimpiar: () -> Unit,
    modifier: Modifier = Modifier
) {
    var mapaListo by remember { mutableStateOf(false) }

    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp)
        ) {
            Text(
                "Mapa de la ruta",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )

            Spacer(Modifier.height(10.dp))

            AndroidView(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                factory = { ctx: Context ->
                    MapView(ctx).also { mapa ->
                        mapa.mapboxMap.loadStyle(
                            Style.MAPBOX_STREETS
                        ) {
                            mapaListo = true
                        }
                    }
                },
                update = { mapa ->
                    if (mapaListo) {
                        if (latitud != null && longitud != null) {
                            mapa.mapboxMap.setCamera(
                                CameraOptions.Builder()
                                    .center(
                                        Point.fromLngLat(
                                            longitud,
                                            latitud
                                        )
                                    )
                                    .zoom(15.0)
                                    .build()
                            )
                        }

                        // Los marcadores se agregan
                        // en el siguiente paso.
                    }
                }
            )

            Spacer(Modifier.height(10.dp))

            Text(
                mensajeUbicacion,
                color = Color.DarkGray
            )

            if (latitud != null && longitud != null) {
                Text("Latitud: %.6f".format(latitud))
                Text("Longitud: %.6f".format(longitud))
            }

            Spacer(Modifier.height(8.dp))

            Text("Fotos registradas: ${fotos.size}")

            Spacer(Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = alIniciar,
                    modifier = Modifier.weight(1f),
                    enabled = !rutaIniciada
                ) {
                    Text("Iniciar ruta")
                }

                Button(
                    onClick = alLimpiar,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Limpiar")
                }
            }
        }
    }
}
