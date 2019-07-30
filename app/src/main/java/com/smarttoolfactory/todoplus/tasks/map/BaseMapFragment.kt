package com.smarttoolfactory.todoplus.tasks.map

import android.graphics.Point
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.smarttoolfactory.todoplus.R
import dagger.android.support.DaggerFragment


/**
 * Base Fragment that contains [GoogleMap].
 *
 * Life cycle till map is ready and loaded are
 *

 *  * onCreate()
 *  * onCreateView()
 *  * onViewCreated()
 *  * onResume()
 *  * Runnable inside onCreatVieW() -> post() width: 1080, height: 1680
 *  * onCameraMove() if initial position is set
 *  * onCameraIdle() if camera is isCameraMoved previously
 *  * **onMapLoaded()** after map loading is finished and projection of valid values are retrievable
 *
 */

// 🔥 T ViewBinding is required to not set ViewBinding for each derived class that extends this Fragment
abstract class BaseMapFragment<T : ViewDataBinding?> : DaggerFragment(), OnMapReadyCallback {


    protected  var map: GoogleMap? = null
    protected var dataBinding: T? = null

    /**
     * Point that contains width and height of the fragment.
     *
     *
     * Dimensions are required for getting projection to get coordinates of each side of the fragment
     *
     */
    private val dimensions = Point()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        println("BaseMapFragment onCreate()")

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
//        println("BaseMapFragment onCreateView()")

        // Each fragment can have it's seprate toolbar menu
        setHasOptionsMenu(true)

        dataBinding =
            DataBindingUtil.inflate<T>(inflater, getLayoutId(), container, false)

        val rootView = dataBinding?.root

        // Get width and height of the fragment
        rootView?.post {
            dimensions.x = rootView.width
            dimensions.y = rootView.height

            println("onCreateView() -> post() width: " + dimensions.x + ", height: " + dimensions.y)
        }

        return rootView
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        // Set up Map
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?

        mapFragment?.getMapAsync(this)

    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {

        map = googleMap
        initMap(googleMap)

    }

    /**
     * This method is invoked just after map is ready and map instance is retrieved
     */
    protected abstract fun initMap(map: GoogleMap)

    /**
     * Get layout id from derived class
     */
    protected abstract fun getLayoutId(): Int
}
