package com.example.cally

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.cally.databinding.TodoDayBinding
import java.time.LocalDate


//TodoFragment의 주별 캘린더
class TodoWeeklyAdapter(val context: Context, var daysOfMonth: ArrayList<LocalDate?>?, var selectedDate: LocalDate): RecyclerView.Adapter<TodoWeeklyAdapter.TodoViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoViewHolder {
        val binding = TodoDayBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TodoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TodoViewHolder, position: Int) {
        val date = daysOfMonth?.get(position)
        val binding = holder.binding
        //일자 입력
        if(date == null)
            binding.tvDayOfMonth.text = ""
        else
        {
            binding.tvDayOfMonth.text = date.dayOfMonth.toString()
            if(date == selectedDate){
                holder.itemView.setBackgroundColor(Color.LTGRAY)
            }else{
                holder.itemView.setBackgroundColor(Color.WHITE)
            }
        }

        //일요일 날짜 글자색 변경
        if(position==0){
            binding.tvDayOfMonth.setTextColor(Color.RED)
        }

        /*//아이템뷰 클릭 이벤트
        holder.itemView.setOnClickListener {
            Log.d("Hwang", "$date")
            val todoFragment = (context as MainActivity).pagerAdapter.createFragment(1)
            val bundle = Bundle()
            bundle.putInt("year", date?.year!!)
            bundle.putInt("month", date.month.value)
            bundle.putInt("day", date.dayOfMonth)
            todoFragment.arguments = bundle
            Log.d("Hwang", "${todoFragment.arguments}")
            selectedDate = date
        }*/
    }

    override fun getItemCount(): Int = daysOfMonth?.size ?: 0

    class TodoViewHolder(val binding: TodoDayBinding): RecyclerView.ViewHolder(binding.root)
}
