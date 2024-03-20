package com.nikita.oatmarks.screens.search_student

import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.*
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.nikita.oatmarks.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

private const val TAG = "SearchStudentFragment"

class SearchStudentFragment : Fragment() {

    interface Callbacks {
        fun onSearchProcessStarted()
        fun onSearchProcessEnded()
    }

    private var callbacks: Callbacks? = null

    private lateinit var surnameEditText: EditText
    private lateinit var searchButton: Button
    private lateinit var resolver: ContentResolver

    private val searchStudentViewModel by lazy {
        ViewModelProvider(this).get(SearchStudentViewModel::class.java)
    }
    private val readTable = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        result.data?.data?.let { uri ->
            readTable(uri)
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        resolver = requireActivity().contentResolver
        callbacks = context as Callbacks?
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_search_student, container, false)

        surnameEditText = view.findViewById(R.id.student_surname)
        searchButton = view.findViewById(R.id.search_student)

        return view
    }

    override fun onStart() {
        super.onStart()

        searchButton.setOnClickListener {
            if (!surnameEditText.text.isNullOrBlank()) {
                readTable()
            } else {
                Toast.makeText(context, R.string.toast_empty_field, Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDetach() {
        super.onDetach()
        callbacks = null
    }

    private fun readTable() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        intent.type = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
        readTable.launch(intent)
    }

    private fun readTable(uri: Uri){
            try {
                GlobalScope.launch(Dispatchers.IO) {
                    val inputStream = resolver.openInputStream(uri)
                    if (inputStream != null) {
                        callbacks?.onSearchProcessStarted()

                        searchStudentViewModel.studentSurname = surnameEditText.text.toString().trim()
                        searchStudentViewModel.marksTable = MarksTable(inputStream)

                        searchStudentViewModel.getStudentSubjects(this@SearchStudentFragment)

                        callbacks?.onSearchProcessEnded()
                    }
                }

            } catch (exception: Exception) {
                exception.printStackTrace()
            }
    }
}