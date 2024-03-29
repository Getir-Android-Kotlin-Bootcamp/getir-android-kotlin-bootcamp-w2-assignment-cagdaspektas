package com.example.mapproject

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.widget.EditText
import androidx.fragment.app.FragmentActivity
import com.example.mapproject.databinding.ActivityMapsBinding
import com.example.mapproject.util.BitmapHelper
import com.google.android.gms.common.api.Status
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener
import java.util.Locale


class MapsActivity : FragmentActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    //todo ilk baştaki kamera konumu
    var destination = LatLng(41.015137, 28.979530)
    //todo zoom seviyesi 15 f sokak görünümüdür.
    val zoomLevel = 15f

    //todo sayın asistanlar için bilgilendirme kısımlarına todo bırakıyorum here we assign our icon to marker (we did it because marker icon needs to be a bitmap but our icon is svg)
    private val bicycleIcon: BitmapDescriptor by lazy {
        BitmapHelper.vectorToBitmap(this, R.drawable.pin_radar)
    }

    @SuppressLint("ResourceAsColor")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)


      val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map_Fragment) as SupportMapFragment
        mapFragment.getMapAsync(this)

        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), getString(R.string.API_KEY), Locale.US);
        }

        val autocompleteFragment =
            supportFragmentManager.findFragmentById(R.id.place_autocomplete_fragment)
                    as AutocompleteSupportFragment

        autocompleteFragment.setPlaceFields(listOf(Place.Field.ID, Place.Field.NAME,Place.Field.LAT_LNG))
        autocompleteFragment.setHint("Where is your location?")
        autocompleteFragment.setOnPlaceSelectedListener(object : PlaceSelectionListener {
            override fun onPlaceSelected(place: Place) {
                destination=place.latLng
                mMap.addMarker(MarkerOptions().position(destination).title(place.name).icon(bicycleIcon))
                mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(destination,zoomLevel))
            }

            override fun onError(status: Status) {

                Log.i(TAG, "An error occurred: $status")
            }
        })


    }


    override fun onMapReady(googleMap: GoogleMap) {
        //todo bu kısımda map tamamlandığı zaman olanı söylüyoruz lat ve lng vererek başlangıç noktasını ayarlıyoruz
        mMap = googleMap


        mMap.addMarker(MarkerOptions().position(destination).title("Marker in Sydney").icon(bicycleIcon))
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(destination,zoomLevel))
    }




}

