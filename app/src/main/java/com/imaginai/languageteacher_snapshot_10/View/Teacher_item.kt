package com.imaginai.languageteacher.View

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.imaginai.languageteacher.databinding.FragmentTeacherItemBinding
import androidx.lifecycle.ViewModelProvider
import com.imaginai.languageteacher.ViewModel.TeacherViewModel


class teacher_item : Fragment() {


    private var _binding: FragmentTeacherItemBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: TeacherViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        viewModel = ViewModelProvider(this)[TeacherViewModel::class.java]

        _binding = FragmentTeacherItemBinding.inflate(inflater, container, false)


        return binding.root
    }

}