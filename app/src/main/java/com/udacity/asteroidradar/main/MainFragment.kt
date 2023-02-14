package com.udacity.asteroidradar.main

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.udacity.asteroidradar.AsteroidAdapter
import com.udacity.asteroidradar.AsteroidListener
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.databinding.FragmentMainBinding

class MainFragment : Fragment() {

    private val viewModel: MainViewModel by lazy {
        ViewModelProvider(this)[MainViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentMainBinding.inflate(inflater)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        // Image of the day //
//        val imageOfDayService = retrofit.create(ImageOfDayService::class.java)
//        val imageCall = imageOfDayService.getImage(API_KEY)
//        imageCall.enqueue(object : Callback<PictureOfDay> {
//            override fun onResponse(call: Call<PictureOfDay>, response: Response<PictureOfDay>) {
//                if (response.isSuccessful) {
//                    val apiResponse = response.body()
//                    if (apiResponse != null) {
//                        val imageUrl = apiResponse.url
//                        if (apiResponse.media_type == "image") {
//                            Picasso.with(context).load(imageUrl)
//                                .into(binding.activityMainImageOfTheDay)
//                        }
//                    }
//                }
//            }
//
//            override fun onFailure(call: Call<PictureOfDay>, t: Throwable) {
//                //handle error
//            }
//        })
        // End of image of the day //


        // RecyclerView //
//        var asteroidsList: List<>
//        val asteroidsInterface = retrofit.create(AsteroidsInterface::class.java)
//        val asteroidsCall = asteroidsInterface.getAsteroidObject(API_KEY)
//        asteroidsCall.enqueue(object : Callback<Asteroid> {
//            override fun onResponse(call: Call<Asteroid>, response: Response<Asteroid>) {
//                if (response.isSuccessful) {
//                    val asteroidResponse = response.body()
//                    if (asteroidResponse != null) {
//                        Log.i("MyTag", asteroidResponse.toString())
//                    }
//                }
//            }
//
//            override fun onFailure(call: Call<Asteroid>, t: Throwable) {
//                TODO("Not yet implemented")
//            }
//        })

        val adapter = AsteroidAdapter(AsteroidListener {
            findNavController().navigate(MainFragmentDirections.actionShowDetail(it))
        })

        binding.asteroidRecycler.adapter = adapter
        viewModel.asteroids.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.submitList(it)
            }
        })

//        val adapter = AsteroidAdapter(asteroidsList)
//        binding.asteroidRecycler.adapter = adapter
//        binding.asteroidRecycler.layoutManager = LinearLayoutManager(context)
        // End of RecyclerView //

        //menu
        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_overflow_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return true
    }
}

