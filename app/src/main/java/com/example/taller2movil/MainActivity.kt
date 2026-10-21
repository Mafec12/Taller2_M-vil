package com.example.taller2movil

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.taller2movil.ui.theme.Taller2movilTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            Taller2movilTheme {
                PantallaPrincipal()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaPrincipal() {

    var rutaIniciada by remember { mutableStateOf(false) }
    var cantidadFotos by remember { mutableStateOf(0) }
    var camaraFrontal by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Ruta de fotos",
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

            if (maxWidth >= 700.dp) {

                Row(
                    modifier = Modifier.fillMaxSize(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {

                    PanelMapa(
                        rutaIniciada = rutaIniciada,
                        cantidadFotos = cantidadFotos,
                        alIniciar = {
                            rutaIniciada = true
                        },
                        alLimpiar = {
                            rutaIniciada = false
                            cantidadFotos = 0
                        },
                        modifier = Modifier.weight(1f)
                    )

                    PanelCamara(
                        cantidadFotos = cantidadFotos,
                        camaraFrontal = camaraFrontal,
                        alTomarFoto = {
                            cantidadFotos++
                        },
                        alCambiarCamara = {
                            camaraFrontal = !camaraFrontal
                        },
                        modifier = Modifier.weight(1f)
                    )
                }

            } else {

                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {

                    PanelMapa(
                        rutaIniciada = rutaIniciada,
                        cantidadFotos = cantidadFotos,
                        alIniciar = {
                            rutaIniciada = true
                        },
                        alLimpiar = {
                            rutaIniciada = false
                            cantidadFotos = 0
                        },
                        modifier = Modifier.weight(1f)
                    )

                    PanelCamara(
                        cantidadFotos = cantidadFotos,
                        camaraFrontal = camaraFrontal,
                        alTomarFoto = {
                            cantidadFotos++
                        },
                        alCambiarCamara = {
                            camaraFrontal = !camaraFrontal
                        },
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}

@Composable
fun PanelMapa(
    rutaIniciada: Boolean,
    cantidadFotos: Int,
    alIniciar: () -> Unit,
    alLimpiar: () -> Unit,
    modifier: Modifier = Modifier
) {

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
                text = "Mapa de la ruta",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(10.dp))

            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .background(
                        color = Color(0xFFE7EFE8),
                        shape = RoundedCornerShape(12.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Text(
                        text = "📍",
                        style = MaterialTheme.typography.headlineLarge
                    )

                    Text("Aquí se mostrará el mapa")

                    Text(
                        text = if (rutaIniciada) {
                            "Ruta iniciada"
                        } else {
                            "Esperando inicio de ruta"
                        },
                        color = Color.DarkGray
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text("Fotos registradas: $cantidadFotos")

            Spacer(modifier = Modifier.height(8.dp))

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

@Composable
fun PanelCamara(
    cantidadFotos: Int,
    camaraFrontal: Boolean,
    alTomarFoto: () -> Unit,
    alCambiarCamara: () -> Unit,
    modifier: Modifier = Modifier
) {

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
                text = "Cámara",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(10.dp))

            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .background(
                        color = Color(0xFF20252B),
                        shape = RoundedCornerShape(12.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Text(
                        text = "📷",
                        style = MaterialTheme.typography.headlineLarge,
                        color = Color.White
                    )

                    Text(
                        text = "Aquí se mostrará la cámara",
                        color = Color.White
                    )

                    Text(
                        text = if (camaraFrontal) {
                            "Cámara frontal"
                        } else {
                            "Cámara trasera"
                        },
                        color = Color.LightGray
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {

                Button(
                    onClick = alTomarFoto,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Tomar foto")
                }

                Button(
                    onClick = alCambiarCamara,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Cambiar cámara")
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text("Fotos recientes")

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {

                if (cantidadFotos == 0) {

                    Text(
                        text = "Todavía no hay fotos",
                        color = Color.Gray
                    )

                } else {

                    repeat(minOf(cantidadFotos, 5)) { indice ->

                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = Color(0xFFE2E8F0)
                        ) {

                            Text(
                                text = "Foto ${indice + 1}",
                                modifier = Modifier.padding(8.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}
