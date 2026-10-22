
package com.example.taller2movil.componentes

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun PanelMapa(
    rutaIniciada: Boolean,
    cantidadFotos: Int,
    latitud: Double?,
    longitud: Double?,
    mensajeUbicacion: String,
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
            Modifier
                .fillMaxSize()
                .padding(12.dp)
        ) {
            Text(
                "Mapa de la ruta",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )

            Spacer(Modifier.height(10.dp))

            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .background(
                        Color(0xFFF8E8F0),
                        RoundedCornerShape(12.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment =
                        Alignment.CenterHorizontally
                ) {
                    Text(
                        "📍",
                        style =
                            MaterialTheme.typography.headlineLarge
                    )

                    Text("Mapa pendiente de integrar")

                    Text(
                        mensajeUbicacion,
                        color = Color.DarkGray
                    )

                    if (latitud != null && longitud != null) {
                        Spacer(Modifier.height(8.dp))

                        Text(
                            "Latitud: %.6f".format(latitud)
                        )

                        Text(
                            "Longitud: %.6f".format(longitud)
                        )
                    }
                }
            }

            Spacer(Modifier.height(10.dp))

            Text("Fotos registradas: $cantidadFotos")

            Spacer(Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement =
                    Arrangement.spacedBy(8.dp)
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
