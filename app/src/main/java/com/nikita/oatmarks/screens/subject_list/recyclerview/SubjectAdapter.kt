package com.nikita.oatmarks.screens.subject_list.recyclerview

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nikita.oatmarks.R
import com.nikita.oatmarks.enums.Semester
import com.nikita.oatmarks.models.Mark
import com.nikita.oatmarks.models.SubjectWithMarks
import java.math.RoundingMode
import java.text.DecimalFormat

//receiving list of the subject, marks filter(Semester) and context for MarkAdapter to display BottomSheetDialogFragment with details of mark
class SubjectAdapter(var subjectWithMarks: List<SubjectWithMarks>, var semesterFilter: Semester, var fragment: Fragment) : RecyclerView.Adapter<SubjectAdapter.DataViewHolder>() {

    //data holder class, that will be created to fill the recyclerview
    inner class DataViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        //format for average mark display
        private val decimalFormat = DecimalFormat("#.##")
        //finding views
        private val titleTextView: TextView = itemView.findViewById(R.id.subject_title)
        private val marksRecyclerView: RecyclerView = itemView.findViewById(R.id.subject_marks)
        private val emptyMarksTextView: TextView = itemView.findViewById(R.id.empty_text)
        private val averageTextView: TextView = itemView.findViewById(R.id.average_mark)

        init {
            decimalFormat.roundingMode = RoundingMode.CEILING
        }

        fun bind(subjectWithMarks: SubjectWithMarks) {
            titleTextView.text = subjectWithMarks.subject.title

            val marks: MutableList<Mark> = mutableListOf()
            var averageMark = 0.0
            var numericalMarksCount = 0

            for (mark in subjectWithMarks.marks){
                //applying filter for marks
                if (mark.month / 4 >= semesterFilter.ordinal - 1) {
                    marks += mark
                    //if mark is not a comment mark
                    if (mark.value != 0) {
                        averageMark += mark.value
                        numericalMarksCount++
                    }
                }
            }
            //counting average mark depending on the number of numerical marks(excepting comment-marks)
            if (numericalMarksCount != 0) {
                averageTextView.visibility = View.VISIBLE
                averageMark /= numericalMarksCount
            } else {
                averageTextView.visibility = View.GONE
            }

            if (marks.size >= 1) {
                emptyMarksTextView.visibility = View.GONE
                marksRecyclerView.visibility = View.VISIBLE
                if (averageTextView.visibility == View.VISIBLE) {
                    averageTextView.text = decimalFormat.format(averageMark).toString()
                    averageTextView.setBackgroundColor(
                        if (averageMark < 2.5) {
                            Color.parseColor("#ff6e6a")
                        } else if (averageMark < 3.5){
                            Color.parseColor("#f4931c")
                        } else {
                            Color.parseColor("#bbd036")
                        })
                }

                //creating markadapter to fill the marks recyclerview
                val markAdapter = MarkAdapter(subjectWithMarks.subject.title, marks, fragment)

                marksRecyclerView.layoutManager = LinearLayoutManager(itemView.context, LinearLayoutManager.HORIZONTAL, false)
                marksRecyclerView.adapter = markAdapter
            } else {
                marksRecyclerView.visibility = View.GONE
                averageTextView.visibility = View.GONE
                emptyMarksTextView.visibility = View.VISIBLE
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_subject, parent, false)
        return DataViewHolder(view)
    }

    override fun getItemCount() = subjectWithMarks.size

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) {
        val subject = subjectWithMarks[position]
        holder.bind(subject)
    }
}