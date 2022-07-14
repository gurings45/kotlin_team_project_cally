package com.example.cally

import java.util.*

//사용자 데이터 클래스
data class UserData(var userId: String = "",
                    var userEmail: String = "")

//일정 데이터 클래스
data class ScheduleData(var userId: String = "",
                        var scheduleTitle: String = "",
                        var startDate: String = Calendar.getInstance().time.toString(),
                        var endDate: String = Calendar.getInstance().time.toString(),
                        var isRepeated: Boolean = false,
                        var repeatCycle: Int = 0,
                        var scheduleDetail: String = "",
                        var docId: String? = null)

//할일 목록 데이터 클래스
data class TodoData(var userId: String = "",
                    var todoTitle: String = "",
                    var todoDate: String = Calendar.getInstance().time.toString(),
                    var todoDone: Boolean = false,
                    var docId: String? = null)

//그룹 데이터 클래스
data class GroupData(var userId: String = "",
                     var userIds: Array<String> = arrayOf())