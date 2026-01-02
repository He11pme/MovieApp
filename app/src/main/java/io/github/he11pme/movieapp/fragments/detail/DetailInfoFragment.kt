package io.github.he11pme.movieapp.fragments.detail

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.github.he11pme.movieapp.databinding.FragmentDetailInfoBinding

class DetailInfoFragment : Fragment() {

    private lateinit var binding: FragmentDetailInfoBinding
    private val viewModel: DetailInfoViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // TODO: Use the ViewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDetailInfoBinding.inflate(layoutInflater, container, false)
        return binding.root
    }
}