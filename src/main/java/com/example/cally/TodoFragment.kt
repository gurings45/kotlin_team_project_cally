package com.example.cally

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cally.databinding.FragmentTodoBinding
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.*

//할일 내용 넣는 프래그먼트
class TodoFragment : Fragment() {
    lateinit var binding: FragmentTodoBinding
    lateinit var todoWeeklyAdapter: TodoWeeklyAdapter
    lateinit var todoRecyclerAdapter: TodoRecyclerAdapter
    var days: ArrayList<LocalDate?>? = null
    val todoUtil = TodoUtil()
    val firebase = Firebase()
    var selectedDate: LocalDate = LocalDate.now()
    var calendar: Calendar = Calendar.getInstance()
    var todoDataList: MutableList<TodoData> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    @SuppressLint("NotifyDataSetChanged", "SetTextI18n", "ClickableViewAccessibility")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTodoBinding.inflate(inflater, container, false)
        setWeekView()

        //현재 일자 받기
        calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        //요일 표시 형식 지정
        var dayName: String = SimpleDateFormat("E", Locale.KOREA).format(calendar.time)
        //최초 날짜는 현재 일자로 지정
        binding.tvTodoDate.text = "${month+1}월 ${day}일 ${dayName}"

        //할일 목록 어뎁터 생성 및 연결
        todoRecyclerAdapter =TodoRecyclerAdapter(todoDataList)
        binding.recyclerView.adapter = todoRecyclerAdapter
        binding.recyclerView.layoutManager = LinearLayoutManager(context)

        // 리사이클러뷰에 스와이프, 드래그 기능 달기
        val swipeHelperCallback = SwipeHelperCallBack(todoRecyclerAdapter).apply {
            // 스와이프한 뒤 고정시킬 위치 지정
            setClamp(resources.displayMetrics.widthPixels.toFloat()/3)    // 1080 / 3 = 360
        }
        ItemTouchHelper(swipeHelperCallback).attachToRecyclerView(binding.recyclerView)

        // 다른 곳 터치 시 기존 선택했던 뷰 닫기
        binding.recyclerView.setOnTouchListener { _, _ ->
            swipeHelperCallback.removePreviousClamp(binding.recyclerView)
            false
        }

        //할일 목록 가져오기
        todoDataList = firebase.selectTodoList(todoRecyclerAdapter, calendar.time)

        //월 클릭시 이벤트 등록: DatePickerDialog 띄우기
        binding.linearLayout.setOnClickListener {
            val dateEvent = context?.let { it1 ->
                DatePickerDialog(
                    it1,
                    { _, year, monthOfYear, dayOfMonth ->
                        //월이 0부터 시작하여 1을 더해주어야함
                        //선택한 날짜 세팅
                        calendar.set(year, monthOfYear, dayOfMonth)
                        dayName = SimpleDateFormat("E", Locale.KOREA).format(calendar.time)

                        //선택한 날짜로 일자 변경
                        binding.tvTodoDate.text = "${monthOfYear+1}월 ${dayOfMonth}일 $dayName"

                        //선택한 날짜로 상단 월 변경
                        binding.tvTodoMonth.text = "${year}년 ${monthOfYear+1}월"

                        //선택한 날짜로 스캐줄러 업데이트
                        selectedDate = LocalDate.of(year, monthOfYear+1, dayOfMonth)

                        todoUtil.selectedDate = this.selectedDate
                        days = todoUtil.daysInWeekArray(todoUtil.selectedDate!!)
                        todoWeeklyAdapter.daysOfMonth = days
                        todoWeeklyAdapter.selectedDate = this.selectedDate
                        todoWeeklyAdapter.notifyDataSetChanged()

                        //할일 목록 가져오기
                        todoDataList = firebase.selectTodoList(todoRecyclerAdapter, calendar.time)
                        todoRecyclerAdapter.todoDataList = todoDataList
                        todoRecyclerAdapter.notifyDataSetChanged()
                    },
                    year,
                    month,
                    day
                )
            }

            //다이얼로그 띄우기
            if (dateEvent != null) {
                dateEvent.show()
            }
        }

        //FloatingActionButton 이벤트 등록: TodoDialog 화면 띄우기
        binding.floatingActionButton.setOnClickListener {
            val todoAddDialog = TodoAddDialog(requireContext(), calendar.time, todoRecyclerAdapter)
            todoAddDialog.showDialog()
        }

        /*binding.weeklyRecycler.setOnClickListener {
            if(arguments != null){
                val year = arguments?.getInt("year")
                val month = arguments?.getInt("month")
                val day = arguments?.getInt("day")
                calendar.set(year!!, month!!, day!!)

                binding.tvTodoDate.text = "${SimpleDateFormat("MM", Locale.getDefault()).format(calendar.time)}월" +
                        "${SimpleDateFormat("dd", Locale.getDefault()).format(calendar.time)}일 "+
                        SimpleDateFormat("E", Locale.KOREA).format(calendar.time)
            }
        }*/

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        //할일 목록 가져오기
        todoDataList = firebase.selectTodoList(todoRecyclerAdapter, calendar.time)
        todoRecyclerAdapter.todoDataList = todoDataList
        todoRecyclerAdapter.notifyDataSetChanged()
    }

    //화면 설정
    private fun setWeekView() {
        days = todoUtil.daysInWeekArray(selectedDate)
        todoWeeklyAdapter = TodoWeeklyAdapter(requireContext(), days, selectedDate)
        val layoutManager: RecyclerView.LayoutManager = GridLayoutManager(context, 7)
        binding.weeklyRecycler.layoutManager =layoutManager
        binding.weeklyRecycler.adapter = todoWeeklyAdapter
    }
}