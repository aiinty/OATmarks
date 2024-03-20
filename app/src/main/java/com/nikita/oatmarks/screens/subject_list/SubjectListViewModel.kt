package com.nikita.oatmarks.screens.subject_list

import androidx.lifecycle.ViewModel
import com.nikita.oatmarks.database.SubjectRepository

class SubjectListViewModel : ViewModel() {

    private val subjectRepository = SubjectRepository.get()
    var subjectsLiveData = subjectRepository.getAllSubjectsWithMarks()

}