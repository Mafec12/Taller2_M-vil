package com.example.taller2movil.ubicacion

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.os.Looper
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority

class Ubicacion(contexto: Context) {

    private val cliente =
        LocationServices.getFusedLocationProviderClient(contexto)

    private var callback: LocationCallback? = null

    @SuppressLint("MissingPermission")
    fun iniciar(
        alRecibirUbicacion: (Location) -> Unit,
        alError: (Exception) -> Unit
    ) {
        detener()

        val solicitud = LocationRequest.Builder(
            Priority.PRIORITY_HIGH_ACCURACY,
            5000L
        )
            .setMinUpdateIntervalMillis(2000L)
            .build()

        callback = object : LocationCallback() {
            override fun onLocationResult(resultado: LocationResult) {
                resultado.lastLocation?.let { ubicacion ->
                    alRecibirUbicacion(ubicacion)
                }
            }
        }

        try {
            cliente.requestLocationUpdates(
                solicitud,
                callback!!,
                Looper.getMainLooper()
            ).addOnFailureListener { error ->
                detener()
                alError(error)
            }
        } catch (error: SecurityException) {
            detener()
            alError(error)
        }
    }

    fun detener() {
        callback?.let { actual ->
            cliente.removeLocationUpdates(actual)
        }
        callback = null
    }
}
