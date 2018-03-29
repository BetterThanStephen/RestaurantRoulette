package com.example.lukas.restaurantroulette

import android.annotation.SuppressLint
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions


class MapsActivity : AppCompatActivity(), OnMapReadyCallback{


    private val LOCATION_REQUEST_CODE = 101

    private lateinit var mMap: GoogleMap

    private var locationManager: LocationManager? = null

    var mLocation: Location? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
    }



    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @SuppressLint("MissingPermission")
    override fun onMapReady(googleMap: GoogleMap) {

        mMap = googleMap


        //val place = LatLng(30.0,30.0)
        val place = LatLng(mLocation!!.latitude, mLocation!!.longitude)
        mMap.addMarker(MarkerOptions().position(place).title("Marker in Sydney"))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(place))
        mMap.uiSettings.isZoomControlsEnabled = true

    }

}
