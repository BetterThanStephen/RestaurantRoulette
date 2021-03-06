package com.example.lukas.restaurantroulette

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.content_main.*
import java.util.*

const val FINE_LOCATION_PERMISSION = 101
const val COARSE_LOCATION_PERMISSION = 102

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private var mAuth: FirebaseAuth? = null;
    private var mUser: FirebaseUser? = null;

    private var locationManager : LocationManager? = null


    val RC_SIGN_IN = 123

    public override fun onStart() {
        super.onStart()
        val currentUser = mAuth?.currentUser
        updateUI(currentUser)
        //updateDrawerUI(currentUser) //Problem is null pointer exception

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == RC_SIGN_IN) {
            val response = IdpResponse.fromResultIntent(data)

            if(resultCode == Activity.RESULT_OK) {
                val user = FirebaseAuth.getInstance().currentUser

                this.updateUI(user)
            }
            else {
                this.updateUI(null)
            }
        }
    }

    private fun getLocation(){
        if(ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                    FINE_LOCATION_PERMISSION)
        }
        else{

        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when(requestCode){
            FINE_LOCATION_PERMISSION -> {
                if((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)){

                }
                else {
                    //No permission granted
                }
            }
        }
    }

    public fun updateUI(currentUser: FirebaseUser?){
        mUser = currentUser


        if (currentUser != null){
            logoutButton.visibility = View.VISIBLE
            loginButton.visibility = View.GONE
            textUserName.text = currentUser?.displayName
            textUserEMail.text = currentUser?.email
            textUserID.text = currentUser?.uid

            Picasso.with(this@MainActivity)
                    .load(currentUser?.photoUrl)
                    .into(imageProfile)
        }
        else {
            logoutButton.visibility = View.GONE
            loginButton.visibility = View.VISIBLE
            textUserName.text = "NOT LOGGED IN"
            textUserEMail.text = ""
            textUserID.text = ""
            imageProfile.setImageResource(R.drawable.common_google_signin_btn_text_disabled)
        }
    }

    private val locationListener: LocationListener = object : LocationListener{
        override fun onLocationChanged(location: Location){
            locationText.setText("Location: " + location.longitude + ", " + location?.latitude)
            Log.d("User Location", location.toString())
        }

        override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun onProviderEnabled(provider: String?) {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun onProviderDisabled(provider: String?) {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        mAuth = FirebaseAuth.getInstance()

        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager?

        fab.setOnClickListener{view ->
            try{
                locationManager?.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0L, 0f, locationListener)
            }
            catch (ex: SecurityException){
                Log.d("Tag", "Security Exception, no location available")
            }
        }

        getLocation()

        fun doLoginStuff(){
            val providers = Arrays.asList(
                    AuthUI.IdpConfig.Builder(AuthUI.EMAIL_PROVIDER).build(),
                    AuthUI.IdpConfig.Builder(AuthUI.PHONE_VERIFICATION_PROVIDER).build(),
                    AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER).build()

            )

            startActivityForResult(
                    AuthUI.getInstance()
                            .createSignInIntentBuilder()
                            .setAvailableProviders(providers)
                            .build(),
                    RC_SIGN_IN)



        }

        fun doLogoutStuff(){
            AuthUI.getInstance()
                    .signOut(this)
                    .addOnCompleteListener {
                        this.updateUI(null)
                    }
        }


        loginButton.setOnClickListener{ view ->
            doLoginStuff()
        }

        logoutButton.setOnClickListener { view ->
            doLogoutStuff()
        }

//        fab.setOnClickListener { view ->
//            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                    .setAction("Action", null).show()
//            launchActivity()
//        }

        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        when (item.itemId) {
            R.id.action_settings -> return true
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_camera -> {
                // Handle the camera action
            }
            R.id.nav_gallery -> {

            }
            R.id.nav_slideshow -> {

            }
            R.id.nav_manage -> {

            }
            R.id.nav_share -> {

            }
            R.id.nav_send -> {

            }
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    private fun launchActivity() {

        val intent = Intent(this, MapsActivity::class.java)
        startActivity(intent)
    }

}
