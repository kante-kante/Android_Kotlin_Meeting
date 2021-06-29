package com.yjr.meeting

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.Intent.ACTION_PICK
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.util.*

class AddFragment:Fragment() {

    lateinit var loadingPb:ProgressBar
    lateinit var imageIv:ImageView
    lateinit var textEt:EditText
    lateinit var submitBtn:Button

    lateinit var auth:FirebaseAuth
    lateinit var storage:FirebaseStorage
    lateinit var firestore:FirebaseFirestore

    lateinit var projectname:EditText
    lateinit var projectnum:EditText

    val REQ_IMAGE = 1111
    var selectedImage: Uri?=null //이미지의 경로를 지정하는 변수 > null로 초기화 해준다.

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        //layout을 가져와서 view 로 리턴
        var view=inflater.inflate(R.layout.fragment_add,container,false)
        //가져온 뷰에 위젯들이 있기 떄문에 찾아서 넣어준다.
        loadingPb=view.findViewById(R.id.loading_pb)
        imageIv=view.findViewById(R.id.image_iv)
        textEt=view.findViewById(R.id.text_et)
        submitBtn=view.findViewById(R.id.submit_btn)

            //추가
        projectname=view.findViewById(R.id.project_name)
        projectnum=view.findViewById(R.id.project_num)



        return view

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth= FirebaseAuth.getInstance()
        storage= FirebaseStorage.getInstance()
        firestore= FirebaseFirestore.getInstance()



        imageIv.setOnClickListener {
            //이미지가 선택되었을때
            var intent=Intent(ACTION_PICK)
            intent.type="image/*"
            startActivityForResult(intent,REQ_IMAGE)
        }

        submitBtn.setOnClickListener {
            //버튼 클릭
            var text=textEt.text.toString()
            //추가
            var projectName=projectname.text.toString()
            var projectNum=projectnum.text.toString()

            if(text.length==0){
                Toast.makeText(activity,"문구를 입력해주세요",Toast.LENGTH_SHORT).show() //아무것도 안입력시
                return@setOnClickListener
            }

            if(projectName.length==0){
                Toast.makeText(activity,"문구를 입력해주세요",Toast.LENGTH_SHORT).show() //아무것도 안입력시
                return@setOnClickListener
            }
            if(projectNum.length==0){
                Toast.makeText(activity,"문구를 입력해주세요",Toast.LENGTH_SHORT).show() //아무것도 안입력시
                return@setOnClickListener
            }



            if(selectedImage==null){
                Toast.makeText(activity,"이미지를 선택해주세요",Toast.LENGTH_SHORT).show() //아무것도 등록시
                return@setOnClickListener
            }
            //업로드 진행
            startLoading()
            var fileName=UUID.randomUUID().toString() //파일을 uuid를 통해 새로 생성
            storage.getReference().child("post").child(fileName)  //이미지 폴더를 새로 만든다
                    .putFile(selectedImage!!)  //이미지는 공백일리 없다.
                    .addOnSuccessListener {
                        it.metadata?.reference?.downloadUrl?.addOnSuccessListener {
                            //it 이라는return 된 파라미터에 포스트 데이터를 생성
                            var imageUrl=it.toString()
                            var post=Post(imageUrl,text,projectName,projectNum,auth.currentUser?.email!!)
                            firestore.collection("Post")
                                    .document().set(post) //콜렉션에 집어넣는다.
                                    .addOnSuccessListener {
                                        endLoading()
                                        //이미지 창 초기
                                        clear()
                                        //0번쨰 탭 이동
                                        var mainActivity=activity as MainActivity // 형변환
                                        mainActivity.moveTab(0) //홈탭으로 이동
                                    }

                        }
                    }
        }



    }



    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        //결과값 받아오기
        if(requestCode==REQ_IMAGE&&resultCode==RESULT_OK){
            //이미지를 사용자가 선택해 왔을 때
            selectedImage=data?.data //이미지의 url를 변수에 임시 저장
            imageIv.setImageURI(selectedImage) //선택한 사진이 나타나도록
            imageIv.scaleType=ImageView.ScaleType.CENTER_CROP //이미지 스케일
        }
    }

    fun clear(){
        textEt.text.clear()
        imageIv.setImageDrawable(activity?.resources?.getDrawable(R.drawable.baseline_add_black_48))
        imageIv.scaleType=ImageView.ScaleType.CENTER_INSIDE
    }

    fun startLoading(){
        loadingPb.visibility=VISIBLE

    }

    fun endLoading(){
        loadingPb.visibility= GONE
    }




}