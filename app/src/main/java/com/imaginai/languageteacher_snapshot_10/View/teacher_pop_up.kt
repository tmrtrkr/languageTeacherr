import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.DialogFragment
import com.imaginai.languageteacher.Model.Teacher
import com.imaginai.languageteacher.databinding.FragmentTeacherPopUpBinding
import androidx.lifecycle.ViewModelProvider
import com.imaginai.languageteacher.ViewModel.TeacherViewModel


class TeacherPopUpFragment : DialogFragment() {
    interface TeacherCreationListener {
        fun onTeacherCreated(teacher: Teacher)
    }

    private var listener: TeacherCreationListener? = null
    private var selectedLanguage: String? = null
    private var selectedLevel: String? = null
    private val names = arrayOf("Alice", "Bob", "Charlie", "Diana", "Edward") // Rastgele isimler

    private lateinit var viewModel: TeacherViewModel


    private var _binding: FragmentTeacherPopUpBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentTeacherPopUpBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupSpinners()

        viewModel = ViewModelProvider(this)[TeacherViewModel::class.java]


        binding.button3.setOnClickListener {
            val name = names.random() // Rastgele bir isim seç
            val language = selectedLanguage ?: "Unknown"
            val level = selectedLevel ?: "Unknown"


            val newTeacher = Teacher(name = name, language = language, level = level)

            newTeacher.createAssistant()

            Log.d("teacher_pop_up.createTeacher","Teacher Created " + "AssistantID: " + newTeacher.teacherID + " ThreadID: " + newTeacher.threadID)

            listener?.onTeacherCreated(newTeacher)
            viewModel.addTeacher(newTeacher)
            println(viewModel.getTeachers())
            dismiss()
        }


        binding.button4.setOnClickListener{
            dismiss()
        }
    }

    fun setTeacherCreationListener(listener: TeacherCreationListener) {
        this.listener = listener
    }

    private fun setupSpinners() {

        val languages = arrayOf("English", "Turkish", "French", "German", "Italian")
        val languageAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, languages)
        languageAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.LanguageList.adapter = languageAdapter

        binding.LanguageList.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                selectedLanguage = parent.getItemAtPosition(position) as String
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        // Seviye seçimleri için Spinner kurulumu
        val levels = arrayOf("Beginner", "Intermediate", "Advanced")
        val levelAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, levels)
        levelAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.LanguageLevel.adapter = levelAdapter

        binding.LanguageLevel.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                selectedLevel = parent.getItemAtPosition(position) as String
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
    }

}

