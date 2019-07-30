package com.smarttoolfactory.todoplus.addedittask

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.smarttoolfactory.todoplus.R.layout
import com.smarttoolfactory.todoplus.databinding.FragmentAddLocationBinding
import com.smarttoolfactory.todoplus.tasks.map.BaseMapFragment
import javax.inject.Inject


class AddLocationFragment() : BaseMapFragment<FragmentAddLocationBinding>() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var addTaskListViewModel: AddEditTaskViewModel

    /**
     * Marker to display touched position
     */
    private lateinit var marker: Marker


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        addTaskListViewModel = ViewModelProviders.of(activity!!, viewModelFactory).get(AddEditTaskViewModel::class.java)
    }

    override fun getLayoutId(): Int {
        return layout.fragment_add_location
    }


    override fun initMap(map: GoogleMap) {

        // Add a marker in Sydney and move the camera
        val istanbul = LatLng(41.01384, 28.94966)
        map.moveCamera(CameraUpdateFactory.newLatLng(istanbul))
        val zoom = CameraUpdateFactory.zoomTo(10f)
        map.animateCamera(zoom)

        // Set map rotation button
        map.uiSettings.isRotateGesturesEnabled = false
        // Set my location button
        map.uiSettings.isMyLocationButtonEnabled = true
        // Set navigation menu
        map.uiSettings.isZoomControlsEnabled = true
        map.uiSettings.isMapToolbarEnabled = true

        // TODO Not for production
        addMarker(map, istanbul)


        // Add marker whenever user
        map.setOnMapClickListener { latLng ->
            addMarker(map, latLng)
        }
    }

    private fun addMarker(map: GoogleMap, latLng: LatLng) {

        map.clear()

        val options = MarkerOptions().title("Selected").position(latLng)
            .draggable(false)

        marker = map.addMarker(options)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(com.smarttoolfactory.todoplus.R.menu.add_location_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }


    companion object {

        fun newInstance(): AddLocationFragment {

            val args = Bundle()

            val fragment = AddLocationFragment()
            fragment.arguments = args
            return fragment
        }
    }

}