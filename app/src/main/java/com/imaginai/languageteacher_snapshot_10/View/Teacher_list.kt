package com.imaginai.languageteacher.View

import TeacherPopUpFragment
import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import com.imaginai.languageteacher.databinding.FragmentTeacherListBinding
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.imaginai.languageteacher.Model.Teacher
import com.imaginai.languageteacher.R
import com.imaginai.languageteacher.ViewModel.TeacherViewModel

class teacher_list : Fragment(), TeacherPopUpFragment.TeacherCreationListener {

    private var _binding: FragmentTeacherListBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: TeacherViewModel

    private var teacherList: List<Teacher> = emptyList()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        viewModel = ViewModelProvider(this)[TeacherViewModel::class.java]


        _binding = FragmentTeacherListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        viewModel.teachers.observe(viewLifecycleOwner, Observer { teachers ->
            teacherList = teachers
            updateUI(teachers)
        })
        super.onViewCreated(view, savedInstanceState)
        //viewModel = ViewModelProvider(this)[TeacherViewModel::class.java]




        binding.newTeacherBTN.setOnClickListener {

            //Limit teachers to 6
            if (binding.TeacherListLayout.childCount >= 6) {
                //Show Limit exceed alert
                showLimitReachedDialog()
            }
            //Else open teacher creation pop-up
            else {
                //declare pop-up
                val popUp = TeacherPopUpFragment()

                //initilize pop-up using its constructor
                popUp.setTeacherCreationListener(this)

                //Show new teacher creation pop-up
                popUp.show(childFragmentManager, "TeacherPopUpFragment")}

        }

        viewModel.fetchDataAndUpdateUI { data ->

            binding.textView4.text = data
        }
    }



    override fun onTeacherCreated(teacher: Teacher) {

        teacherList += teacher

        // Teacher oluşturulduğunda UI güncelleme işlemleri
        val inflater = LayoutInflater.from(context)
        val newTeacherView = inflater.inflate(R.layout.fragment_teacher_item, binding.TeacherListLayout, false)
        newTeacherView.findViewById<TextView>(R.id.TeacherName).text = teacher.name
        newTeacherView.findViewById<TextView>(R.id.TeacherLanguage).text = teacher.language
        newTeacherView.findViewById<TextView>(R.id.TeacherLevel).text = teacher.level


        newTeacherView.findViewById<ImageButton>(R.id.TeacherDelete).setOnClickListener {
            teacherList = teacherList - teacher
            viewModel.deleteTeacher(teacher)
            removeTeacherView(newTeacherView)
        }

        newTeacherView.findViewById<ImageButton>(R.id.GoToClass).setOnClickListener{

            val action = teacher_listDirections.actionTeacherListToClassRoom2WithTeacher(teacher)
            findNavController().navigate(action)
        }



        binding.TeacherListLayout.addView(newTeacherView)
    }



    private fun showLimitReachedDialog() {
        AlertDialog.Builder(context)
            .setTitle("Limit Reached")
            .setMessage("Maximum number of teachers has reached (6).")
            .setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
            .show()
    }


    private fun updateUI(teachers: List<Teacher>) {
        Log.d("TeacherListFragment", "Updating UI with ${teachers.size} teachers")

        // Clear the current list of teachers
        binding.TeacherListLayout.removeAllViews()

        for (teacher in teachers) {
            val teacherView = LayoutInflater.from(context).inflate(R.layout.fragment_teacher_item, binding.TeacherListLayout, false)
            teacherView.findViewById<TextView>(R.id.TeacherName).text = teacher.name
            teacherView.findViewById<TextView>(R.id.TeacherLanguage).text = teacher.language
            teacherView.findViewById<TextView>(R.id.TeacherLevel).text = teacher.level

            // Button handlers
            teacherView.findViewById<ImageButton>(R.id.TeacherDelete).setOnClickListener {
                teacherList = teacherList - teacher
                viewModel.deleteTeacher(teacher)
                removeTeacherView(teacherView)
            }

            teacherView.findViewById<ImageButton>(R.id.GoToClass).setOnClickListener{
                val action = teacher_listDirections.actionTeacherListToClassRoom2WithTeacher(teacher)
                findNavController().navigate(action)
            }

            binding.TeacherListLayout.addView(teacherView)
        }
    }


    override fun onResume() {
        super.onResume()
        // Önceki öğretmen öğesini kaldır
        binding.TeacherListLayout.removeAllViews()
    }




    private fun removeTeacherView(teacherView: View) {
        binding.TeacherListLayout.removeView(teacherView)
        teacherList = teacherList.filterNot { teacher ->
            teacher.name == teacherView.findViewById<TextView>(R.id.TeacherName).text.toString()
        }
    }

}