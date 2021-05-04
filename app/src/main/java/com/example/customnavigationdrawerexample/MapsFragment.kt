package com.example.customnavigationdrawerexample

import android.content.Intent
import androidx.fragment.app.Fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.customnavigationdrawerexample.apis.ApiClient
import com.example.customnavigationdrawerexample.data.model.LoginRequest
import com.example.customnavigationdrawerexample.data.model.LoginResponse
import com.example.customnavigationdrawerexample.data.model.MapFields
import com.example.customnavigationdrawerexample.helpers.MarkerClusterItem

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.ClusterItem
import com.google.maps.android.clustering.ClusterManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MapsFragment : Fragment() {
    private lateinit var clusterManager: ClusterManager<ClusterItem>
    private lateinit var mMap: GoogleMap
    private lateinit var apiClient: ApiClient
    private val callback = OnMapReadyCallback { googleMap ->
        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */
//        val sydney = LatLng(-34.0, 151.0)
//        googleMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
//        googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
        mMap = googleMap
        googleMap.uiSettings.isZoomControlsEnabled = true
        setUpClusterer(googleMap)
        getFields()
    }
    private fun getFields() {
        apiClient.getApiService(requireContext()).getFields()
            .enqueue(object : Callback<MapFields> {
                override fun onFailure(call: Call<MapFields>, t: Throwable) {
                    // Error fetching posts
                }

                override fun onResponse(call: Call<MapFields>, response: Response<MapFields>) {
//                    arr.forEach {
//                        println(it)
//                    }
                }
            })
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_maps, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        apiClient= ApiClient()
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
    }


    private fun setUpClusterer(map: GoogleMap) {
        // Position the map.
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(51.503186, -0.126446), 5f))

        // Initialize the manager with the context and the map.
        // (Activity extends context, so we can pass 'this' in the constructor.)
        clusterManager = ClusterManager(context, map)

        // Point the map's listeners at the listeners implemented by the cluster
        // manager.
        map.setOnCameraIdleListener(clusterManager)
        map.setOnMarkerClickListener(clusterManager)

        // Add cluster items (markers) to the cluster manager.
        addItems()
    }

    private fun addItems() {

        // Set some lat/lng coordinates to start with.
        var lat = 51.5145160
        var lng = -0.1270060

        // Add ten cluster items in close proximity, for purposes of this example.
        for (i in 0..9) {
            val offset = i / 60.0
            lat += offset
            lng += offset
            val offsetItem =
                MarkerClusterItem(lat, lng, "Title $i", "Snippet $i")
            clusterManager.addItem(offsetItem)
        }
    }

}