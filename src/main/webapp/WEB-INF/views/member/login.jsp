<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<jsp:include page="./mem_components.jsp"/>


<style>

  video
  {
    width: 100%;
    height: 100%;
    max-height: 100%;

  }

  .opt_fixed
  {
    position: fixed;
    padding : 8px;


  }

  .findSomeText
  {

    color : white;
    font-weight : bold;
    margin : 5px;
  }



</style>


<div style="width:auto; height:auto;" class="subscribe-line-transparent" >

  <video autoplay muted loop id="backgroundVideo" style="object-fit: fill;">

    <source src="${pageContext.request.contextPath}/resources/assets/video/fireworks.mp4" type="video/mp4">

  </video>

  <div class="container opt_fixed">


    <div class="row">

      <div class="col-12" style="height : 100px; width : auto;"></div>

      <div class="col-sm-8 mr-auto ml-auto">
        <div id="regForm" class="card card-register mr-auto ml-auto" style="background-color: rgba( 255, 255, 255, 0.3 );">
          <h3 class="card-title" style="color : #ffffff; display : inline; font-weight : bold;">Play with Us!</h3>
          <div class="register-form">
            <label>Email</label>
            <input type="email" class="form-control no-border" placeholder="Email" style="color : black;">
            <label>Password</label>
            <input type="password" class="form-control no-border" placeholder="Password" style="color : black;">
            <button id="btnLogin" class="btn btn-danger btn-block btn-round">Login</button>
          </div>

          <div id="join_socialBtn" class="row text-center" style="padding : 10px">

            <div class="col-12 text-left w-100">
              <label>아이디 저장</label>

              <label>
                <input type="checkbox" data-toggle="switch" data-on-color="default" data-off-color="default" style="margin:0">
                <span class="toggle"></span>
              </label>
              <span class="toggle"></span>
            </div>
            <div id="findPWBtn" class="col-12" style="padding : 10px"><span class="findSomeText">비밀번호를 잃어버렸나요?</span></div>
            <div id="recoverAuthBtn" class="col-12" style="padding : 10px"><span class="findSomeText">복구할 계정이 있나요?</span></div>

          </div>


        </div>
      </div>
    </div>
  </div>

</div>


<script>

    //비밀번호 찾기 버튼 클릭시 Action
    $("#findPWBtn").on('click', function(e){

        //모달 초기화 및 이벤트 캡쳐링 방지
        makePWModal();
        e.preventDefault();

    });

    //복구용 계정으로 로그인 클릭시 Action
    $("#recoverAuthBtn").on('click', function(e){

        makeRecoverModal();
        e.preventDefault();


    });




    $(document).ready(function() {


        //소셜 로그인 버튼창 초기화
        let $btn_comp = $("#btn_components").clone();
        $btn_comp.css({ 'display' : 'block'});
        $btn_comp.addClass("col-12");
        $("#join_socialBtn").append($btn_comp);



        $("#btnLogin").on('click', function(){
            let inputEmail = $(this).parent().find("input[type=email]").val();
            let inputPw = $(this).parent().find("input[type=password]").val();
            if(validCheckAuth(inputEmail, inputPw)){
                $.ajax({
                    url: "/logincheck",
                    type: "post",
                    data: {
                        "em_id": inputEmail,
                        "em_pwd": inputPw
                    },
                    success: function (responseData){
                        if (responseData.indexOf('iderror') != -1) {
                            alert("존재하지 않는 아이디 입니다. 다시 확인해 주세요!");
                            $('#login_em_id').val("");
                            $('#login_em_id').focus();
                            return false;

                        } else if (responseData.indexOf('pwderror') != -1) {
                            alert("잘못된 비밀번호입니다. 다시 확인해 주세요!");
                            $('#login_em_pwd').val("");
                            $('#login_em_pwd').focus();
                        } else {
                            location.replace("/");

                        }
                    }
                })
            }
        });

        //이메일 버튼 클릭시 Action
        $btn_comp.find("#emailBtn").on('click', function(e){

            makeJoinEmailModal();
            e.preventDefault();
        });


        //페이스북 버튼 클릭시 Action
        $btn_comp.find("#fbBtn").on('click', function(){

            location.href = "${f}";

        });

        //구글 버튼 클릭시 Action
        $btn_comp.find("#googleBtn").on('click', function(){
            location.href = "${g}";


        });

        //카카오 버튼 클릭시 Action
        $btn_comp.find("#kakaoBtn").on('click', function(){
            location.href = "${k}";


        });

        //네이버 버튼 클릭시 Action
        $btn_comp.find("#naverBtn").on('click', function(){
            location.href = "${n}";


        });

        //로그인 버튼 클릭시 Action
        $btn_comp.find("#login-check").on('click', function(){
            let inputEmail = $btn_comp.find("input[type=email]").html();
            let inputPw = $btn_comp.find("input[type=password]").html();
            /*e.preventDefault();
            console.log(e);
            if ($('#login_em_id').val() == "") {
                alert('이메일을 입력해주세요');
                $('#lgoin_em_id').focus();
                return false;
            } else if ($('#login_em_pwd').val() == "") {
                alert('비밀번호를 입력해주세요');
                $('#login_em_pwd').focus();
                return false;
            } else {
                $.ajax({
                    url: "/logincheck",
                    type: "post",
                    data: {
                        "em_id": $('#login_em_id').val(),
                        "em_pwd": $('#login_em_pwd').val()
                    },
                    success: function (responseData) {

                        if (responseData.indexOf('iderror') != -1) {
                            alert("존재하지 않는 아이디 입니다. 다시 확인해 주세요!");
                            $('#em_id').val("");
                            $('#em_id').focus();
                            return false;

                        } else if (responseData.indexOf('pwderror') != -1) {
                            alert("잘못된 비밀번호입니다. 다시 확인해 주세요!");
                            $('#em_pwd').val("");
                            $('#em_pwd').focus();
                        } else {
                            location.replace("/");

                        }
                    }
                });
            }

        });*/

        });
    });


    $(document).ready(function(){
        let em_id = getCookie("saveid");
        $("input[type=email]").val(em_id);
        if($("input[type=email]").val() != ""){
            $("input[type=checkbox]").attr("checked", true);
        }
        $("input[type=checkbox]").on("switchChange.bootstrapSwitch",function () {
            alert("체크박스 변경됨");
            $("input[type=checkbox]");
            if($("input[type=checkbox]").is(":checked")){
                let saveid = $("input[type=email]").val();
                setCookie("saveid",saveid,7);
            }else{
                deleteCookie("saveid");
            }
        });
        $("input[type=email]").keyup(function () {
            if($("input[type=checkbox]").is(":checked")){
                let saveid = $("input[type=email]").val();
                setCookie("saveid",saveid,7);
            }
        })
    });
    /*쿠키 세팅*/
    function setCookie(cookie_name, value, days){
        let date = new Date();
        date.setDate(date.getDate()+days);
        let cookie_value = escape(value) + ((date==null) ? "" : "; expires=" + date.toGMTString());
        document.cookie = cookie_name + "=" + cookie_value;
    }
    function deleteCookie(cookie_Name){
        let expireDate = new Date();
        expireDate.setDate(expireDate.getDate() - 1);
        document.cookie = cookie_Name + "= " + "; expires=" + expireDate.toGMTString();
    }
    function getCookie(cookie_Name) {
        cookie_Name = cookie_Name + '=';
        let cookieData = document.cookie;
        let start = cookieData.indexOf(cookie_Name);
        let cookieValue = '';
        if(start != -1){
            start += cookie_Name.length;
            let end = cookieData.indexOf(';', start);
            if(end == -1)end = cookieData.length;
            cookieValue = cookieData.substring(start, end);
        }
        return unescape(cookieValue);
    }

</script>