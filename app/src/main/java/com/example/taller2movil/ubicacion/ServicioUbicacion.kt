
package com.example.taller2movil.ubicacion

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource

class ServicioUbicacion(contexto: Context) {

    private val cliente =
        LocationServices.getFusedLocationProviderClient(contexto)

    @SuppressLint("MissingPermission")
    fun obtenerUbicacion(
        alObtener: (Location?) -> Unit
    ) {
        val cancelacion = CancellationTokenSource()

        cliente.getCurrentLocation(
            Priority.PRIORITY_HIGH_ACCURACY,
            cancelacion.token
        ).addOnSuccessListener { ubicacion ->
            alObtener(ubicacion)
        }.addOnFailureListener {
            alObtener(null)
        }
    }
}
