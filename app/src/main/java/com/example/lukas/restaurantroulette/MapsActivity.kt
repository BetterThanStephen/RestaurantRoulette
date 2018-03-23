package com.example.lukas.restaurantroulette

import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.util.Log
import android.widget.Toast
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.places.GeoDataApi

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.PlaceDetectionClient;




class MapsActivity : AppCompatActivity(), OnMapReadyCallback {



    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
    }

    private lateinit var lastlocation: Location
    private lateinit var mMap: GoogleMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    //private lateinit var mGeoDataClient: GeoDataClient
    //private lateinit var mPlaceDetectionClient: PlaceDetectionClient

    private var locationManager : LocationManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        //mGeoDataClient = Places.getGeoDataClient(this, null)
        //mPlaceDetectionClient = Places.getPlaceDetectionClient(this, null)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)


    }

    private fun setUpMap() {
        if (ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERMISSION_REQUEST_CODE)
            return
        }

        mMap.isMyLocationEnabled = true

        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            if(location != null){
                lastlocation = location
                val currentLatLng = LatLng(location.latitude, location.latitude)
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 12f))
            }
        }

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

        mMap.uiSettings.isZoomControlsEnabled


        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager?

        var location : Location? = null

        if(ContextCompat.checkSelfPermission( this, android.Manifest.permission.ACCESS_FINE_LOCATION ) == PackageManager.PERMISSION_GRANTED){
            try{
                //location = locationManager?.getLastKnownLocation(LocationManager.GPS_PROVIDER)
            }
            catch (e: Exception) {
                Log.d("Exception", e.toString())
            }
        }
        else if(ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED){
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

<<<<<<< HEAD
=======
        Log.d("Location", location.toString())
        Log.d("location perm coarse", ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION).toString())
        Log.d("location perm fine", ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION).toString())



>>>>>>> bcf30a6300002a3acffbf6cb21cecb1e872a1f0c
        var lat = location?.latitude
        var long = location?.longitude

        if(lat == 0.0 || lat == null){
            lat = 30.0
        }

        if(long == 0.0 || long == null){
            long = -30.0
        }

        mMap = googleMap

         //Add a marker in Sydney and move the camera
        val mapLocation = LatLng(
                lat,
                long
        )

<<<<<<< HEAD
=======
        val place = LatLng(40.73, -73.99)
        mMap.addMarker(MarkerOptions().position(place).title("Marker in Sydney"))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(place))
          mMap.uiSettings.isZoomControlsEnabled = true
          setUpMap()

>>>>>>> bcf30a6300002a3acffbf6cb21cecb1e872a1f0c
    }

}
