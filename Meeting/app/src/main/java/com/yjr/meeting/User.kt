package com.yjr.meeting

class User {
    //생성자 생성
    constructor(){

    }

    constructor(email:String){
        this.email=email // 이메일을 받는 생성자

    }

    //변수 2개 생성
    var email:String?=null
    var profileUrl:String?=null


    //필드생성 공백을 기본으로하는
    var name:String=""
    var profiletext:String=""
    var profile_text2:String=""

}