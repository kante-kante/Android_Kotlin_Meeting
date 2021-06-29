package com.yjr.meeting

import java.util.*

class Post {
    constructor(){}


    constructor(imageUrl:String,text:String,text1:String,text2:String,userId:String){
    //업로드된 시간을
        this.imageUrl=imageUrl
        this.text=text
        this.projectname=text1
        this.projectnum=text2
        this.userId=userId
        uploadDate=Date() //생성자를 이용함.



    }

    //맴버변수 선언
    var imageUrl:String="" // 업로드한 이미지의 경로
    var text:String="" //입력한 문구 저장 변수
    var projectname:String=""
    var projectnum:String=""
    var userId:String="" //포스트를 업로드한 사용자의 아이디
    var uploadDate:Date= Date() //업로드한 시간. > sns 에서 중요하고 정렬이 되게 하기 위함




}