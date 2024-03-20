package com.nikita.oatmarks.screens.search_student

import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nikita.oatmarks.R
import com.nikita.oatmarks.database.SubjectRepository
import kotlinx.coroutines.launch

private const val TAG = "SearchStudentViewModel"

class SearchStudentViewModel : ViewModel() {

    var studentSurname: String = ""
    lateinit var marksTable: MarksTable
    private val subjectRepository = SubjectRepository.get()

    fun getStudentSubjects(fragment: Fragment) {
        val subjects = marksTable.searchStudentSubjects(studentSurname)
        viewModelScope.launch {
            if (subjects.isNotEmpty()) {
                subjectRepository.insertSubjectsWithMarks(subjects)
            } else {
                Toast.makeText(fragment.context, R.string.toast_student_not_found, Toast.LENGTH_SHORT).show()
            }
        }
    }
}

