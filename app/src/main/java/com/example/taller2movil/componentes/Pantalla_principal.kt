
package com.example.taller2movil.componentes

import android.location.Location
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import android.Manifest
import android.content.pm.PackageManager
import com.example.taller2movil.datos.Foto
import com.example.taller2movil.ubicacion.ServicioUbicacion
import com.example.taller2movil.ubicacion.Ubicacion

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaPrincipal() {
    val contexto = LocalContext.current

    var rutaIniciada by remember {
        mutableStateOf(false)
    }

    var camaraFrontal by remember {
        mutableStateOf(false)
    }

    var ubicacionActual by remember {
        mutableStateOf<Location?>(null)
    }

    var mensajeUbicacion by remember {
        mutableStateOf("Esperando inicio de ruta")
    }

    val fotos = remember {
        mutableStateListOf<Foto>()
    }

    val servicioUbicacion = remember {
        ServicioUbicacion(contexto)
    }

    val seguimiento = remember {
        Ubicacion(contexto)
    }

    DisposableEffect(Unit) {
        onDispose {
            seguimiento.detener()
        }
    }

    fun iniciarRuta() {
        val permisoFino = ContextCompat.checkSelfPermission(
            contexto,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        val permisoAproximado = ContextCompat.checkSelfPermission(
            contexto,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        if (!permisoFino && !permisoAproximado) {
            Toast.makeText(
                contexto,
                "Se necesita permiso de ubicación",
                Toast.LENGTH_LONG
            ).show()
            return
        }

        rutaIniciada = true
        mensajeUbicacion = "Obteniendo ubicación..."

        // Obtener una primera ubicación
        servicioUbicacion.obtenerUbicacion { ubicacion ->
            if (ubicacion != null) {
                ubicacionActual = ubicacion
                mensajeUbicacion =
                    "Ubicación obtenida correctamente"
            } else {
                mensajeUbicacion =
                    "No se pudo obtener la ubicación"
            }
        }

        // Comenzar el seguimiento
        seguimiento.iniciar(
            alRecibirUbicacion = { ubicacion ->
                ubicacionActual = ubicacion
                mensajeUbicacion =
                    "Ubicación actualizada"
            },
            alError = {
                mensajeUbicacion =
                    "Error obteniendo ubicación"
            }
        )
    }

    fun limpiarRuta() {
        seguimiento.detener()
        rutaIniciada = false
        ubicacionActual = null
        mensajeUbicacion = "Esperando inicio de ruta"
        fotos.clear()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Ruta de fotos",
                        fontWeight = FontWeight.Bold
                    )
                }
            )
        }
    ) { espacio ->

        BoxWithConstraints(
            modifier = Modifier
                .fillMaxSize()
                .padding(espacio)
                .padding(12.dp)
        ) {
            val mapa: @Composable (Modifier) -> Unit =
                { mod ->
                    PanelMapa(
                        rutaIniciada = rutaIniciada,
                        cantidadFotos = fotos.size,
                        latitud = ubicacionActual?.latitude,
                        longitud = ubicacionActual?.longitude,
                        mensajeUbicacion = mensajeUbicacion,
                        alIniciar = { iniciarRuta() },
                        alLimpiar = { limpiarRuta() },
                        modifier = mod
                    )
                }

            val camara: @Composable (Modifier) -> Unit =
                { mod ->
                    PanelCamara(
                        fotos = fotos,
                        camaraFrontal = camaraFrontal,
                        alTomarFoto = { foto ->
                            fotos.add(foto)
                        },
                        alCambiarCamara = {
                            camaraFrontal = !camaraFrontal
                        },
                        modifier = mod
                    )
                }

            if (maxWidth >= 700.dp) {
                Row(
                    modifier = Modifier.fillMaxSize(),
                    horizontalArrangement =
                        Arrangement.spacedBy(12.dp)
                ) {
                    mapa(Modifier.weight(1f))
                    camara(Modifier.weight(1f))
                }
            } else {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement =
                        Arrangement.spacedBy(12.dp)
                ) {
                    mapa(Modifier.weight(1f))
                    camara(Modifier.weight(1f))
                }
            }
        }
    }
}
