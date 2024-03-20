package com.nikita.oatmarks.screens.subject_list

import android.content.Context
import android.os.Bundle
import android.view.*
import android.widget.ImageButton
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nikita.oatmarks.R
import com.nikita.oatmarks.enums.Semester
import com.nikita.oatmarks.models.*
import com.nikita.oatmarks.screens.subject_list.recyclerview.SubjectAdapter

private const val TAG = "SubjectListFragment"

class SubjectListFragment : Fragment() {

    interface Callbacks {
        fun onOpenSearchFragmentButtonPressed()
    }

    private var callbacks: Callbacks? = null

    private lateinit var subjectRecyclerView: RecyclerView
    private lateinit var studentSearchButton: ImageButton

    private lateinit var semesterFilter: RadioGroup
    private lateinit var allTimeToggleButton: RadioButton
    private lateinit var firstSemesterToggleButton: RadioButton
    private lateinit var secondSemesterToggleButton: RadioButton

    private lateinit var emptyRecyclerTextView: TextView

    private var adapter: SubjectAdapter? = null
    private val subjectListViewModel: SubjectListViewModel by lazy {
        ViewModelProvider(this).get(SubjectListViewModel::class.java)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callbacks = context as Callbacks?
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_subject_list, container, false)

        semesterFilter = view.findViewById(R.id.filter)

        subjectRecyclerView = view.findViewById(R.id.subject_recycler_view)
        studentSearchButton = view.findViewById(R.id.search_button)
        subjectRecyclerView.layoutManager = LinearLayoutManager(context)

        allTimeToggleButton = view.findViewById(R.id.button_all_time)
        firstSemesterToggleButton = view.findViewById(R.id.button_first_semester)
        secondSemesterToggleButton = view.findViewById(R.id.button_second_semester)

        emptyRecyclerTextView = view.findViewById(R.id.empty_list)

        return view
    }

    private fun updateUI(subjects: List<SubjectWithMarks>) {
        if (subjects.isNotEmpty()) {
            emptyRecyclerTextView.visibility = View.GONE

            adapter = SubjectAdapter(subjects,
                when (semesterFilter.checkedRadioButtonId) {
                    R.id.button_first_semester -> Semester.FIRST
                    R.id.button_second_semester -> Semester.SECOND
                    else -> Semester.ALL
                }, this)
            subjectRecyclerView.adapter = adapter
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        subjectListViewModel.subjectsLiveData.observe(
            viewLifecycleOwner, Observer {
                subjectsWithMarks -> subjectsWithMarks?.let {
                    updateUI(subjectsWithMarks)
                }
            }
        )
    }

    override fun onStart() {
        super.onStart()

        studentSearchButton.setOnClickListener {
            callbacks?.onOpenSearchFragmentButtonPressed()
        }

        allTimeToggleButton.setOnClickListener {
            subjectListViewModel.subjectsLiveData.removeObservers(viewLifecycleOwner)
            subjectListViewModel.subjectsLiveData.observe(
                viewLifecycleOwner, Observer {
                        subjectsWithMarks -> subjectsWithMarks?.let {
                    updateUI(subjectsWithMarks)
                }
                }
            )
        }

        firstSemesterToggleButton.setOnClickListener {
            subjectListViewModel.subjectsLiveData.removeObservers(viewLifecycleOwner)
            subjectListViewModel.subjectsLiveData.observe(
                viewLifecycleOwner, Observer {
                        subjectsWithMarks -> subjectsWithMarks?.let {
                    updateUI(subjectsWithMarks)
                }
                }
            )
        }

        secondSemesterToggleButton.setOnClickListener {
            subjectListViewModel.subjectsLiveData.removeObservers(viewLifecycleOwner)
            subjectListViewModel.subjectsLiveData.observe(
                viewLifecycleOwner, Observer {
                        subjectsWithMarks -> subjectsWithMarks?.let {
                    updateUI(subjectsWithMarks)
                }
                }
            )
        }

    }

    override fun onDetach() {
        super.onDetach()
        callbacks = null
    }

    companion object {
        fun newInstance(): SubjectListFragment {
            return SubjectListFragment()
        }
    }
}