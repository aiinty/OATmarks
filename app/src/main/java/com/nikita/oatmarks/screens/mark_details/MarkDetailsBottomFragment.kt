package com.nikita.oatmarks.screens.mark_details

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.nikita.oatmarks.R
import com.nikita.oatmarks.models.Mark

class MarkDetailsBottomFragment(var subjectTitle: String, var mark: Mark) : BottomSheetDialogFragment() {

    private lateinit var markValueTextView: TextView
    private lateinit var subjectTitleTextView: TextView
    private lateinit var markDateTextView: TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = layoutInflater.inflate(R.layout.bottom_sheet_layout, container, false)

        markValueTextView = view.findViewById(R.id.mark_value)
        subjectTitleTextView = view.findViewById(R.id.subject_title)
        markDateTextView = view.findViewById(R.id.mark_date)

        subjectTitleTextView.text = subjectTitle
        if (mark.value != 0) {
            markValueTextView.text = mark.value.toString()
            markValueTextView.setBackgroundColor(
                if (mark.value < 3) {
                    getResources().getColor(R.color.oat_red)
                } else if (mark.value == 3) {
                    getResources().getColor(R.color.oat_orange)
                } else {
                    getResources().getColor(R.color.oat_green)
                })
        } else {
            markValueTextView.text = mark.comment.toString()
        }
        markDateTextView.text = mark.day.toString() + " " + when(mark.month) {
            0 -> getResources().getString(R.string.september)
            1 -> getResources().getString(R.string.october)
            2 -> getResources().getString(R.string.november)
            3 -> getResources().getString(R.string.december)
            4 -> getResources().getString(R.string.january)
            5 -> getResources().getString(R.string.february)
            6 -> getResources().getString(R.string.march)
            7 -> getResources().getString(R.string.april)
            8 -> getResources().getString(R.string.may)
            9 -> getResources().getString(R.string.june)
            else -> "emptyMonth"
        }
        return view
    }

}