package com.example.dig4634.gpsexample

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat

class MainActivity : AppCompatActivity(), LocationListener {
    val PERMISSION_REQUEST_CODE = 0
    var LibertyIsland: Location? = null
    var LibertyStatue: Location? = null
    var Ferry: Location? = null
    var TakenFerry = 0
    var VisitedLady = 0
    var VisitedMuseum = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        LibertyIsland = Location(LocationManager.GPS_PROVIDER)
        LibertyIsland!!.longitude = -74.0461
        LibertyIsland!!.latitude = 40.6909
        LibertyStatue = Location(LocationManager.GPS_PROVIDER)
        LibertyStatue!!.longitude = -74.0445
        LibertyStatue!!.latitude = 40.6892
        Ferry = Location(LocationManager.GPS_PROVIDER)
        Ferry!!.longitude = -74.0471
        Ferry!!.latitude = 40.6899
        val permissionGranted = (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED)
        if (permissionGranted) {

            //start Location Services
            Log.d("Example", "User granted permissions before. Start GPS now")
            startGPS()
        } else {

            //We need to request permissions
            Log.d("Example", "User never granted permissions before. Request now")
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), PERMISSION_REQUEST_CODE)
        }
    }

    fun startGPS() {
        val permissionGranted = (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED)
        if (permissionGranted) {
            val manager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
            manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 500, 1f, this)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                //The user clicked on the DENY button
                Log.d("Example", "User denied permissions just now. Exit")
                finish()
            } else {
                //The user clicked on the ALLOW button
                Log.d("Example", "User granted permissions right now. Start GPS now")
                startGPS()
            }
        }
    }

    override fun onLocationChanged(location: Location) {
        Log.d("Example", "New location received. Long:" + location.longitude + " Lat:" + location.latitude)
        if (location.distanceTo(LibertyIsland) < 50) //within 50 meters
        {
            Log.d("Example", "You are at the Liberty Island Museum")
            val my_intent = Intent(baseContext, DigitalWorldsActivity::class.java)
            if (TakenFerry == 0)
            {
                my_intent.putExtra("pictureID", R.drawable.error)
                my_intent.putExtra("caption", "You can't visit without taking a ferry first!\nYou need an ENTRANCE TICKET!")
            }
            else if (TakenFerry == 1 && VisitedLady == 0)
            {
                my_intent.putExtra("pictureID", R.drawable.museum)
                my_intent.putExtra("caption", "Let's visit the statue first and \nget a MAP before the museum.")
            }
            else if (TakenFerry == 1 && VisitedLady == 1)
            {
                my_intent.putExtra("pictureID", R.drawable.museum)
                my_intent.putExtra("caption", "Finally, let's get a SOUVENIR from the \nLiberty Island Museum!")
                VisitedMuseum = 1
            }

            startActivity(my_intent)
        } else if (location.distanceTo(LibertyStatue) < 50) //within 50 meters
        {
            Log.d("Example", "You are at the Statue.")
            val my_intent = Intent(baseContext, DigitalWorldsActivity::class.java)
            if (TakenFerry == 0)
            {
                my_intent.putExtra("pictureID", R.drawable.error)
                my_intent.putExtra("caption", "You can't visit without taking a ferry first!\nYou need an ENTRANCE TICKET!")
            }
            else if (TakenFerry == 1)
            {
                my_intent.putExtra("pictureID", R.drawable.statue)
                my_intent.putExtra("caption", "Welcome to the glorious Lady Liberty statue! \nPlease take your MAP and explore!")
                VisitedLady = 1
            }

            startActivity(my_intent)
        } else if (location.distanceTo(Ferry) < 50) //within 50 meters
        {
            Log.d("Example", "You are at the Ferry")
            val my_intent = Intent(baseContext, DigitalWorldsActivity::class.java)
            my_intent.putExtra("pictureID", R.drawable.ferry)
            my_intent.putExtra("caption", " ")


            if (TakenFerry ==0)
            {
                my_intent.putExtra("pictureID", R.drawable.ferry)
                my_intent.putExtra("caption", "Welcome to the Liberty Island Ferry Dock! \nHere is your ENTRANCE TICKET.")
                TakenFerry =1
            }

          else if (TakenFerry == 1 && VisitedLady == 1 && VisitedMuseum == 1)
        {
            my_intent.putExtra("pictureID", R.drawable.night)
            my_intent.putExtra("caption", "Time to go home. That was fun!")
        }
           else if (TakenFerry == 1 && VisitedMuseum == 0)
            {
                my_intent.putExtra("pictureID", R.drawable.ferry)
                my_intent.putExtra("caption", "Let's explore the whole island\nbefore going home.")
            }
            startActivity(my_intent)

        }
    }

    override fun onStatusChanged(s: String, i: Int, bundle: Bundle) {}
    override fun onProviderEnabled(s: String) {}
    override fun onProviderDisabled(s: String) {}
}