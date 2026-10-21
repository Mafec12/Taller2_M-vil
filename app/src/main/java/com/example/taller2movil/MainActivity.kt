package com.example.taller2movil

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.example.taller2movil.componentes.PantallaPrincipal
import com.example.taller2movil.ui.theme.Taller2movilTheme

class MainActivity : ComponentActivity() {

    private val permisosConcedidos = mutableStateOf(false)
    private val permisosRechazados = mutableStateOf(false)

    private val solicitarPermisos =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { resultados ->
            val camara = resultados[Manifest.permission.CAMERA] == true
            val ubicacion =
                resultados[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
                        resultados[Manifest.permission.ACCESS_COARSE_LOCATION] == true

            permisosConcedidos.value = camara && ubicacion
            permisosRechazados.value = !permisosConcedidos.value
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        comprobarPermisos()

        setContent {
            Taller2movilTheme {
                if (permisosConcedidos.value) {
                    PantallaPrincipal()
                } else {
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = Color(0xFFFFF7FB)
                    ) {
                        Column(
                            modifier = Modifier.fillMaxSize().padding(24.dp),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "Ruta de fotos",
                                style = MaterialTheme.typography.headlineMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(Modifier.height(16.dp))
                            Text(
                                if (permisosRechazados.value)
                                    "Esta aplicación necesita permisos de cámara y ubicación para funcionar. Puedes volver a solicitarlos."
                                else
                                    "Para utilizar la aplicación, necesitamos permiso para acceder a la cámara y a tu ubicación."
                            )
                            Spacer(Modifier.height(20.dp))
                            Button(onClick = { pedirPermisos() }) {
                                Text("Conceder permisos")
                            }
                        }
                    }
                }
            }
        }
    }

    private fun comprobarPermisos() {
        val camara = ContextCompat.checkSelfPermission(
            this, Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED

        val ubicacion =
            ContextCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED ||
                    ContextCompat.checkSelfPermission(
                        this, Manifest.permission.ACCESS_COARSE_LOCATION
                    ) == PackageManager.PERMISSION_GRANTED

        permisosConcedidos.value = camara && ubicacion
    }

    private fun pedirPermisos() {
        solicitarPermisos.launch(
            arrayOf(
                Manifest.permission.CAMERA,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        )
    }
}
