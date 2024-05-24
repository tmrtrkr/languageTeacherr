package com.imaginai.languageteacher.View

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.aallam.openai.api.BetaOpenAI
import com.imaginai.languageteacher.databinding.FragmentClassRoomBinding
import com.imaginai.languageteacher_snapshot_10.Service.openAI_Service


class ClassRoom : Fragment() {

    private val args: ClassRoomArgs by navArgs()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    private var _binding: FragmentClassRoomBinding? = null
    private val binding get() = _binding!!

    @OptIn(BetaOpenAI::class)
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentClassRoomBinding.inflate(inflater, container, false)

        val teacher = args.Teacher


        binding.TeacherName.text = teacher.name

        binding.ClassRoomBack.setOnClickListener {
            findNavController().popBackStack()
        }

    /*    binding.MicBtn.setOnClickListener{

            openAI_Service().conversationPipeLine(teacher.teacherID,teacher.threadID)


        } */



        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}