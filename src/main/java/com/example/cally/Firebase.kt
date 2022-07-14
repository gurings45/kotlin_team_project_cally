package com.example.cally

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.multidex.MultiDexApplication
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.util.*

class Firebase: MultiDexApplication() {
    companion object{
        lateinit var firebaseAuth: FirebaseAuth
        var email: String? = null

        //파이어베이스 객체 참조변수
        @SuppressLint("StaticFieldLeak")
        lateinit var db: FirebaseFirestore

        fun checkAuth(): Boolean {
            var authFlag = false

            var user = firebaseAuth.currentUser
            if(user != null){
                authFlag = user.isEmailVerified
            }

            return authFlag
        }
    }

    override fun onCreate() {
        super.onCreate()
        firebaseAuth = Firebase.auth

        //파이어베이스 데이터베이스 객체 생성
        db = FirebaseFirestore.getInstance()
    }

    //UserData 추가
    fun addUser(){
        //사용자 정보 받기
        val currentUser = firebaseAuth.currentUser

        db.collection("User")
            .whereEqualTo("userId", currentUser?.uid)
            .get()
            .addOnSuccessListener { result ->
                var isRegistered = false
                //var userId: String? = null

                if(result.documents.isNotEmpty()){
                    //userId = result.documents[0].get("userId").toString()
                    isRegistered = true
                    //Log.d("Hwang", "$userId 가져오기 성공")
                    //Log.d("Hwang", "$isRegistered")
                }

                //사용자정보 미생성시 생성
                if(!isRegistered){
                    //User 클래스 생성 및 데이터베이스에 유저정보 저장
                    val userData = UserData(currentUser?.uid!!, currentUser.email!!)
                    val user = mapOf(
                        "userId" to userData.userId,
                        "userEmail" to userData.userEmail
                    )

                    db.collection("User")
                        .add(user)
                        .addOnSuccessListener {
                            Log.d("Hwang", "UserData 생성 성공")
                        }
                        .addOnFailureListener {
                            Log.d("Hwang", "UserData 생성 실패")
                        }
                }
            }
            .addOnFailureListener {
                Log.d("Hwang", "userId 가져오기 실패")
            }
    }

    //TodoData 추가
    @SuppressLint("NotifyDataSetChanged")
    fun addTodo(context: Context, todoData: TodoData) {
        //db 필드명과 todoData 맵핑
        val todo = mapOf(
            "userId" to todoData.userId,
            "todoTitle" to todoData.todoTitle,
            "todoDate" to todoData.todoDate,
            "todoDone" to todoData.todoDone
        )

        //파이어스토어에 할일 항목 추가
        db.collection("Todo")
            .add(todo)
            .addOnSuccessListener {
                //할일 추가 완료 안내
                Toast.makeText(context, "할 일이 추가되었습니다.", Toast.LENGTH_SHORT).show()
                //TodoData 클래스에 문서 ID 저장
                todoData.docId = it.id
                it.update("docId", todoData.docId)
            }
            .addOnFailureListener {
                //할일 추가 실패 안내
                Toast.makeText(context, "할 일 저장에 실패하였습니다.", Toast.LENGTH_SHORT).show()
            }

    }

    //할일 해당 일자 할일 목록 받기
    @SuppressLint("NotifyDataSetChanged")
    fun selectTodoList(todoRecyclerAdapter: TodoRecyclerAdapter, selectedDate: Date): MutableList<TodoData> {
        todoRecyclerAdapter.todoDataList.clear()

        db.collection("Todo")
            .whereIn("userId", mutableListOf(firebaseAuth.currentUser?.uid))
            .whereEqualTo("todoDate", SimpleDateFormat("yyyy-MM-dd-E", Locale.KOREA).format(selectedDate.time))
            .get()
            .addOnSuccessListener { result ->
                for(document in result){
                    val todo = document.toObject(TodoData::class.java)
                    todoRecyclerAdapter.todoDataList.add(todo)
                }
                todoRecyclerAdapter.notifyDataSetChanged()
                Log.d("Hwang", "할일 목록 가져오기 성공")
            }
            .addOnFailureListener {
                Log.d("Hwang", "할일 목록 가져오기 실패")
            }

        return todoRecyclerAdapter.todoDataList
    }

    //todoData 수정
    fun updateTodo(todoData: TodoData) {
        db.collection("Todo").document(todoData.docId!!)
            .set(todoData)
            .addOnSuccessListener {
                Log.d("Hwang", "todo 수정 성공")
            }
            .addOnFailureListener {
                Log.d("Hwang", "todo 수정 실패")
            }
    }

    //todoData 삭제
    fun deleteTodo(todoData: TodoData){
        db.collection("Todo").document(todoData.docId!!)
            .delete()
            .addOnSuccessListener {
                Log.d("Hwang", "todo 삭제 성공")
            }
            .addOnFailureListener {
                Log.d("Hwang", "todo 삭제 실패")
            }
    }
}