package com.example.cally

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.cally.databinding.FragmentFriendsBinding

//친구 목록 불러오기
class FriendsFragment : Fragment() {
    lateinit var binding: FragmentFriendsBinding
    var groupDataList: MutableList<GroupData> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFriendsBinding.inflate(inflater, container, false)

        //친구 목록이 비어있다면 텍스트만 보이기
        if(groupDataList.size<=0){
            binding.tvEmptyFriend.visibility = View.VISIBLE
            binding.friendsRecyclerView.visibility = View.INVISIBLE
            binding.searchView.visibility = View.INVISIBLE
        }else{
            binding.tvEmptyFriend.visibility = View.INVISIBLE
            binding.friendsRecyclerView.visibility = View.VISIBLE
            binding.searchView.visibility = View.VISIBLE
        }

        //친구 추가 이벤트 설정
        binding.fabFriends.setOnClickListener {

        }

        return binding.root
    }
}