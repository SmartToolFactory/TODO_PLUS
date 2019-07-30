package com.smarttoolfactory.todoplus.tasks.map

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.smarttoolfactory.todoplus.R
import com.smarttoolfactory.todoplus.data.model.Task
import com.smarttoolfactory.todoplus.databinding.FragmentMapBinding
import com.smarttoolfactory.todoplus.tasks.TaskListViewModel
import javax.inject.Inject

class TaskMapFragment : BaseMapFragment<FragmentMapBinding>() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var taskListViewModel: TaskListViewModel

    /**
     * List that contains tasks.
     * This is here because map can be loaded after database so it should require a non-empty list
     */
    private  var listTask: List<Task>? = null
    private val listMarkers = mutableListOf<Marker>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        taskListViewModel = ViewModelProviders.of(activity!!, viewModelFactory).get(TaskListViewModel::class.java)


        taskListViewModel.items.observe(this, Observer {
            listTask = it

            addMarkers(listTask)
        })

    }

    private fun addMarkers(tasks: List<Task>?) {

        if (map == null || tasks == null) return

        map?.apply {
            this.clear()
            tasks
                .forEach {task->
                    task
                        .takeIf {
                        task.locationSet
                    }?.apply {
                        val latLng = LatLng(latitude, longitude)

                        val options = MarkerOptions().title("Selected").position(latLng)
                            .draggable(false)

                        val marker = map?.addMarker(options)

                        marker?.apply {
                            marker.tag = task
                            listMarkers.add(this)

                        }
                    }

                }

        }


    }

    override fun initMap(map: GoogleMap) {

        // Set camera position
        val istanbul = LatLng(41.01384, 28.94966)
        map.moveCamera(CameraUpdateFactory.newLatLng(istanbul))
        val zoom = CameraUpdateFactory.zoomTo(4f)
        map.animateCamera(zoom)

        // Set map rotation button
        map.uiSettings.isRotateGesturesEnabled = false
        // Set my location button
        map.uiSettings.isMyLocationButtonEnabled = true
        // Set navigation menu
//        map.uiSettings.isZoomControlsEnabled = true
//        map.uiSettings.isMapToolbarEnabled = true

        addMarkers(listTask)

    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_map
    }

    /**
     * This method is sending arguments via bundle to Fragment
     */
    companion object {

        fun newInstance(): TaskMapFragment {

            val args = Bundle()

            val fragment = TaskMapFragment()
            fragment.arguments = args
            return fragment
        }
    }


}