package com.nikita.oatmarks.screens.subject_list.recyclerview

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.nikita.oatmarks.R
import com.nikita.oatmarks.models.Mark
import com.nikita.oatmarks.screens.mark_details.MarkDetailsBottomFragment

//receiving the subject title, list of marks and context to display BottomSheetDialogFragment
class MarkAdapter(var subjectTitle: String, var marks: List<Mark>, var fragment: Fragment) : RecyclerView.Adapter<MarkAdapter.DataViewHolder>() {

    inner class DataViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener{

        init {
            itemView.setOnClickListener(this)
        }

        private val valueTextView: TextView = itemView.findViewById(R.id.mark_value)
        private lateinit var markDetailsFragment: MarkDetailsBottomFragment

        fun bind(subjectTitle: String, mark: Mark) {
            markDetailsFragment = MarkDetailsBottomFragment(subjectTitle, mark)

            valueTextView.setBackgroundColor(
                if (mark.value < 3) {
                    Color.parseColor("#ff6e6a")
            } else if (mark.value == 3){
                    Color.parseColor("#f4931c")
            } else {
                    Color.parseColor("#bbd036")
            })

            valueTextView.text =
                if (mark.value > 0) {
                    mark.value.toString()
                } else {
                    mark.comment
            }
        }

        override fun onClick(view: View?) {
            if (!markDetailsFragment.isAdded) {
                markDetailsFragment.show(fragment.childFragmentManager, "MarkDetailsBottomFragment()")
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_mark, parent, false)
        return DataViewHolder(view)
    }

    override fun getItemCount() = marks.size

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) {
        var mark = marks[position]
        holder.bind(subjectTitle, mark)
    }

}
