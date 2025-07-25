package com.drag0n.testcoffee.presentation

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.drag0n.testcoffee.data.api.ApiCoffee
import com.drag0n.testcoffee.data.sharedPreferense.SharedPreferenseImp
import com.drag0n.testcoffee.domain.geo.GeoLocationImp
import com.drag0n.testcoffee.domain.model.CoffeeShopList
import com.drag0n.testcoffee.domain.model.Point
import com.drag0n.testcoffee.domain.model.User
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ViewModelCoffee @Inject constructor(
    val pref: SharedPreferenseImp,
    val api: ApiCoffee,
    val context: Context,
    val geo: GeoLocationImp
) : ViewModel() {

    private val _showLocationDialog = MutableStateFlow(false)
    val showLocationDialog: StateFlow<Boolean> = _showLocationDialog.asStateFlow()
    private var fLocotionClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(context)

    val CoffeeShopsLD = MutableLiveData<CoffeeShopList>()


    private val _myGeo = MutableStateFlow<Point?>(null)
    val myGeo = _myGeo.asStateFlow()

    fun hideLocationDialog() {
        _showLocationDialog.value = false
    }

    fun openLocationSettings() {
        val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        context.startActivity(intent)
        _showLocationDialog.value = false
    }


    fun saveUser(user: User) {
        pref.saveAuthorization(user)
    }

    fun deleteUser() {
        pref.deleteAuthorization()
    }

    fun isAuthorization(): Boolean {
        return pref.isAuthorization()
    }

    fun authRegister(user: User) {
        viewModelScope.launch {
            try {
                val response = api.authRegister(user)
                if (response.isSuccessful) saveUser(user)
                else toast()
            } catch (e: Exception) {
                toast()
            }
        }
    }

    fun authLogin(user: User) {
        viewModelScope.launch {
            try {
                val response = api.authLogin(user)
                if (response.isSuccessful) saveUser(user)
                else toast()
            } catch (e: Exception) {
                toast()
            }
        }
    }

    fun chekPermissionLocation(): Boolean {
        return geo.chekPermissionLocation()
    }

    fun chekLocation() {
        if (geo.isLocationEnabled()) getLocationGoogle()
        else {
            _showLocationDialog.value = true
        }
    }

    fun getLocationGoogle() {
        val ct = CancellationTokenSource()
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }

        fLocotionClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, ct.token)
            .addOnCompleteListener {
                try {
                    val lat = it.result.latitude.toString()
                    val lon = it.result.longitude.toString()
                    val point = Point(lat, lon)
                    _myGeo.value = point
                    Log.d("MyLog", "c геолокации${point}")
                } catch (_: Exception) {

                }

            }
    }

    fun getCoffeeShops() {
        viewModelScope.launch {
            try {
                val response = api.getCoffeeShops()
                if (response.isSuccessful) {
                    CoffeeShopsLD.postValue(response.body())
                    Log.d(
                        "MyLog", "isSiccessful: ${response.body().toString()}"
                    )
                }
            } catch (e: Exception) {
                toast()
            }
        }
    }


    private fun toast() {
        Toast.makeText(context, "Ошибка", Toast.LENGTH_SHORT).show()
    }
}