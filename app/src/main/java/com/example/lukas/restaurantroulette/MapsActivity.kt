package com.example.lukas.restaurantroulette

import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.content.ContextCompat
import android.util.Log
import android.widget.Toast

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap

    private var locationManager : LocationManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
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
    override fun onMapReady(googleMap: GoogleMap) {

        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager?

        var location : Location? = null

        if(ContextCompat.checkSelfPermission( this, android.Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED){
            try{
                location = locationManager?.getLastKnownLocation(LocationManager.GPS_PROVIDER)
            }
            catch (e: Exception){

                Log.d("Exception", e.toString())
            }
        }
        else if(ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            try{
                location = locationManager?.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
            }
            catch (e: Exception){

                Log.d("Exception", e.toString())
            }
        }
        else{
                Log.d("Location Denied", "Location not found")

        }





        var lat = location?.latitude
        var long = location?.longitude

        if(lat == 0.0 || lat == null){
            lat = 30.0
        }

        if(long == 0.0 || long == null){
            long = -30.0
        }

        mMap = googleMap

        // Add a marker in Sydney and move the camera
        val mapLocation = LatLng(
                lat,
                long
        )
        mMap.addMarker(MarkerOptions().position(mapLocation).title("Marker in Sydney"))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(mapLocation))

        

    }

}
