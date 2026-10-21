
package com.example.taller2movil.componentes

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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.taller2movil.datos.Foto

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaPrincipal() {

    var rutaIniciada by remember {
        mutableStateOf(false)
    }

    var camaraFrontal by remember {
        mutableStateOf(false)
    }

    val fotos = remember {
        mutableStateListOf<Foto>()
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

            val mapa: @Composable (Modifier) -> Unit = { mod ->
                PanelMapa(
                    rutaIniciada = rutaIniciada,
                    cantidadFotos = fotos.size,
                    alIniciar = {
                        rutaIniciada = true
                    },
                    alLimpiar = {
                        rutaIniciada = false
                        fotos.clear()
                    },
                    modifier = mod
                )
            }

            val camara: @Composable (Modifier) -> Unit = { mod ->
                PanelCamara(
                    fotos = fotos.toList(),
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
