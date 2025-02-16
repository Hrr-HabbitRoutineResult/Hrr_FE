package com.example.hrr_android.challenge.ui.record.progress.calendar

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.example.hrr_android.R
import com.example.hrr_android.databinding.ViewCalendarBinding
import java.util.Calendar

/**
 * 커스텀 캘린더 뷰
 * 월별 달력을 표시하고 날짜 선택 기능을 제공하는 커스텀 뷰
 */
class CustomCalendarView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private val binding: ViewCalendarBinding
    private var currentDate = Calendar.getInstance() // 현재 표시되는 달력의 년월 정보
    private var selectedDate: Calendar? = null       // 사용자가 선택한 날짜 정보

    init {
        orientation = VERTICAL
        binding = ViewCalendarBinding.inflate(LayoutInflater.from(context), this)

        initializeCalendar()
    }

    /**
     * 캘린더 초기화를 담당하는 함수
     * 클릭 리스너 설정, 캘린더 UI 업데이트를 순차적으로 실행
     */
    private fun initializeCalendar() {
        setupClickListeners()
        updateCalendar()
    }

    /**
     * 이전/다음 달 버튼의 클릭 리스너 설정
     * 이전 달 버튼: 현재 표시된 달에서 한 달 이전으로 이동
     * 다음 달 버튼: 현재 표시된 달에서 한 달 이후로 이동
     */
    private fun setupClickListeners() {
        binding.btnCalendarPrev.setOnClickListener {
            currentDate.add(Calendar.MONTH, -1)
            updateCalendar()
        }

        binding.btnCalendarNext.setOnClickListener {
            currentDate.add(Calendar.MONTH, 1)
            updateCalendar()
        }
    }

    /**
     * 캘린더 UI를 업데이트하는 함수
     * 헤더의 년월 텍스트와 날짜 그리드를 현재 선택된 달에 맞게 업데이트
     */
    private fun updateCalendar() {
        updateHeaderText()
        updateCalendarGrid()
    }

    /**
     * 캘린더 헤더의 년월 텍스트를 업데이트
     * 형식: "YYYY.MM"
     */
    private fun updateHeaderText() {
        binding.tvCalendarHeader.text = "${currentDate.get(Calendar.YEAR)}.${
            String.format("%02d", currentDate.get(Calendar.MONTH) + 1)
        }"
    }

    /**
     * 캘린더 그리드 전체를 업데이트
     * 이전 달, 현재 달, 다음 달의 날짜를 모두 포함하여 표시
     */
    private fun updateCalendarGrid() {
        binding.llCalendarDates.removeAllViews()

        val firstDayWeekday = getFirstDayWeekday()
        val lastDayOfMonth = currentDate.getActualMaximum(Calendar.DAY_OF_MONTH)
        val prevMonth = (currentDate.clone() as Calendar).apply { add(Calendar.MONTH, -1) }
        val prevMonthLastDay = prevMonth.getActualMaximum(Calendar.DAY_OF_MONTH)

        createCalendarGrid(firstDayWeekday, lastDayOfMonth, prevMonth, prevMonthLastDay)
    }

    /**
     * 현재 달의 1일이 무슨 요일인지 계산
     * @return 일요일(0) ~ 토요일(6)
     */
    private fun getFirstDayWeekday(): Int {
        return (currentDate.clone() as Calendar).apply {
            set(Calendar.DAY_OF_MONTH, 1)
        }.get(Calendar.DAY_OF_WEEK) - 1
    }

    /**
     * 캘린더 그리드를 생성하는 함수
     * 6주 분량의 날짜를 그리드 형태로 생성하여 표시
     */
    private fun createCalendarGrid(
        firstDayWeekday: Int,
        lastDayOfMonth: Int,
        prevMonth: Calendar,
        prevMonthLastDay: Int
    ) {
        var day = 1
        var nextMonthDay = 1

        // 6주 분량의 날짜를 그리드로 생성
        for (i in 0 until 6) {
            val weekRow = createWeekRow()

            // 각 주의 7일을 생성
            for (j in 0 until 7) {
                val position = i * 7 + j
                val dayTextView = createDayTextView(weekRow)

                when {
                    position < firstDayWeekday -> {
                        setupPrevMonthDay(dayTextView, prevMonth, prevMonthLastDay, firstDayWeekday, position)
                    }
                    day <= lastDayOfMonth -> {
                        setupCurrentMonthDay(dayTextView, day++)
                    }
                    else -> {
                        setupNextMonthDay(dayTextView, nextMonthDay++)
                    }
                }

                weekRow.addView(dayTextView)
            }
            binding.llCalendarDates.addView(weekRow)
        }
    }

    /**
     * 한 주를 나타내는 LinearLayout을 생성
     * 가로 방향으로 7개의 날짜가 균등하게 배치되도록 설정
     */
    private fun createWeekRow() = LinearLayout(context).apply {
        layoutParams = LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT)
        orientation = HORIZONTAL
        weightSum = 7f
    }

    /**
     * 날짜를 표시할 TextView를 생성
     */
    private fun createDayTextView(parent: LinearLayout) =
        LayoutInflater.from(context).inflate(R.layout.item_calendar_day, parent, false) as TextView

    /**
     * 이전 달의 날짜를 설정
     * 회색으로 표시되며, 클릭 시 해당 날짜가 선택됨
     */
    private fun setupPrevMonthDay(
        dayTextView: TextView,
        prevMonth: Calendar,
        prevMonthLastDay: Int,
        firstDayWeekday: Int,
        position: Int
    ) {
        val prevDay = prevMonthLastDay - (firstDayWeekday - position - 1)
        dayTextView.apply {
            text = prevDay.toString()
            setTextColor(ContextCompat.getColor(context, R.color.grey_400))
        }
        setupDayClickListener(dayTextView, prevMonth, prevDay)
    }

    /**
     * 현재 달의 날짜를 설정
     * 기본 텍스트 색상으로 표시되며, 선택된 날짜인 경우 배경이 강조됨
     */
    private fun setupCurrentMonthDay(dayTextView: TextView, day: Int) {
        dayTextView.apply {
            text = day.toString()
            // 선택된 날짜인 경우 배경과 텍스트 색상 변경
            if (isSelectedDate(currentDate, day)) {
                setBackgroundResource(R.drawable.bg_calendar_selected_filled)
                setTextColor(ContextCompat.getColor(context, R.color.white))
            } else {
                // 선택되지 않은 날짜는 배경 제거하고 기본 텍스트 색상 사용
                background = null
                setTextColor(ContextCompat.getColor(context, R.color.text_secondary))
            }
        }
        setupDayClickListener(dayTextView, currentDate, day)
    }

    /**
     * 다음 달의 날짜를 설정
     * 회색으로 표시되며, 클릭 시 해당 날짜가 선택됨
     */
    private fun setupNextMonthDay(dayTextView: TextView, nextMonthDay: Int) {
        val nextMonth = (currentDate.clone() as Calendar).apply { add(Calendar.MONTH, 1) }
        dayTextView.apply {
            text = nextMonthDay.toString()
            setTextColor(ContextCompat.getColor(context, R.color.grey_400))
        }
        setupDayClickListener(dayTextView, nextMonth, nextMonthDay)
    }

    private var onDateSelectedListener: ((Calendar) -> Unit)? = null

    fun setOnDateSelectedListener(listener: (Calendar) -> Unit) {
        onDateSelectedListener = listener
    }

    /**
     * 날짜 클릭 리스너 설정
     * 날짜 클릭 시 이전 선택을 해제하고 새로운 날짜를 선택 상태로 변경
     */
    private fun setupDayClickListener(textView: TextView, calendar: Calendar, day: Int) {
        textView.setOnClickListener {
            // 이전 선택 해제
            selectedDate?.let { updateCalendar() }

            // 새로운 날짜 선택
            selectedDate = (calendar.clone() as Calendar).apply {
                set(Calendar.DAY_OF_MONTH, day)
            }

            selectedDate?.let { onDateSelectedListener?.invoke(it) }

            // UI 업데이트: 배경 변경 및 텍스트 색상을 흰색으로 변경
            textView.apply {
                setBackgroundResource(R.drawable.bg_calendar_selected_filled)
                setTextColor(ContextCompat.getColor(context, R.color.white))
            }
        }
    }

    /**
     * 주어진 날짜가 현재 선택된 날짜인지 확인
     * @return 선택된 날짜와 같으면 true, 다르면 false
     */
    private fun isSelectedDate(calendar: Calendar, day: Int): Boolean {
        return selectedDate?.let { selected ->
            selected.get(Calendar.YEAR) == calendar.get(Calendar.YEAR) &&
                    selected.get(Calendar.MONTH) == calendar.get(Calendar.MONTH) &&
                    selected.get(Calendar.DAY_OF_MONTH) == day
        } ?: false
    }

    /**
     * 선택된 날짜를 외부에서 가져올 수 있는 메서드
     * @return 선택된 날짜가 있다면 해당 Calendar 객체, 없다면 null
     */
    fun getSelectedDate(): Calendar? = selectedDate
}