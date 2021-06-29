package com.yjr.meeting

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class HomeFragment: Fragment() {

    lateinit var listRv:RecyclerView
    lateinit var homeAdapter:HomeAdapter
    lateinit var postList:ArrayList<Post>
    //파이어스토어를 이용해서 데이터를 가져온다
    lateinit var firestore:FirebaseFirestore



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view=inflater.inflate(R.layout.fragment_home,container,false)
        listRv=view.findViewById(R.id.list_rv) //뷰가 생성되기 전
        return view;
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        firestore= FirebaseFirestore.getInstance()
        postList=ArrayList<Post>()
        homeAdapter=HomeAdapter(activity!!,postList)
        listRv.adapter=homeAdapter
        listRv.layoutManager=LinearLayoutManager(activity)
        //파이어스토어에서 포스트 콜렉션



        firestore.collection("Post") //홈 게시글 올릴 때 파이어스토어의 Post 에서 가져온다.
                .orderBy("uploadDate",Query.Direction.ASCENDING)
                .addSnapshotListener { value, error ->
                    if(value!=null){
                        for(dc in value.documentChanges) {
                            //생성된 것에만 넣어준다
                            if (dc.type == DocumentChange.Type.ADDED) {
                                var post = dc.document.toObject(Post::class.java)
                                postList.add(0,post) //0번째에 넣겠다.
                            }
                        }
                        homeAdapter.notifyDataSetChanged()
                    }

                }


    }


}