<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<jsp:include page="./c_modal.jsp"></jsp:include>


<style>

    video {
        width: 100%;
        height: 100%;
        max-height: 100%;

    }

    .opt_fixed {
        position: fixed;
        padding: 8px;

    }

    .findSomeText {

        color: white;
        font-weight: bold;
        margin: 5px;
    }


</style>


<div style="width:auto; height:auto;" class="subscribe-line-transparent">

    <video autoplay muted loop id="backgroundVideo" style="object-fit: fill;">

        <source src="${pageContext.request.contextPath}/resources/assets/video/fireworks.mp4" type="video/mp4">

    </video>

    <div class="container opt_fixed">


        <div class="row">

            <div class="col-12" style="height : 100px; width : auto;"></div>

            <div class="col-sm-8 mr-auto ml-auto">
                <div id="regForm" class="card card-register mr-auto ml-auto"
                     style="background-color: rgba( 255, 255, 255, 0.3 );">
                    <h3 class="card-title" style="color : #ffffff; display : inline; font-weight : bold;">Play with
                        Us!</h3>
                    <form class="register-form" method ="post">
                        <label>Email</label>

                        <input type="email" class="form-control no-border" placeholder="Email" id="login_em_id"
                               style="color : black;">
                        <label>Password</label>
                        <input type="password" class="form-control no-border" placeholder="Password" id="login_em_pwd"
                               style="color : black;">
                        <button class="btn btn-danger btn-block btn-round" id="login-check" >Login</button>
                        <input type="checkbox" id="saveid" name="saveid">아이디 저장
                    </form>
                    <div id="join_socialBtn" class="col-12 text-center"></div>
                    <div class="row text-center" style="margin-top:15px; padding : 12px;">
                        <div id="findPWBtn" class="col-12" style="margin-bottom : 10px;" data-toggle="modal"
                             data-target="#findPwModal"><a href="#"><span class="findSomeText">비밀번호를 잃어버렸나요?</span></a>
                        </div>
                        <div id="recoverAuthBtn" class="col-12" data-toggle="modal" data-target="#recoverAuthModal"><a
                                href="#"><span class="findSomeText">복구할 계정이 있나요?</span></a></div>
                    </div>


                </div>
            </div>
        </div>
    </div>
</div>


<%-- if ($('#em_id').val() == "") {
alert('이메일을 입력해주세요');
$('#em_id').focus();
return false; --%>
<script>
    $(function () {
        $('#login-check').on('click', function (e) {
            e.preventDefault();
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

        });

    });

    $(document).ready(_ => {

        let $btn_comp = $("#btn_components").clone();

        $("#join_socialBtn").html($btn_comp.css({'display': 'inline'}));


        $btn_comp.find("#emailBtn").on('click', _ => {
            $(this).remove();
            $("#joinEmailModal").modal('show');

        });


        $btn_comp.find("#fbBtn").on('click', _ => {
            location.href = "${f}";

        });

        $btn_comp.find("#googleBtn").on('click', _ => {
            location.href = "${g}";

        });

        $btn_comp.find("#kakaoBtn").on('click', _ => {
            location.href = "${k}";

        });

        $btn_comp.find("#naverBtn").on('click', _ => {
            location.href = "${n}";


        });


    });


    $(document).ready(function(){
       let em_id = getCookie("saveid");
       $("#login_em_id").val(em_id);
       if($("#login_em_id").val() != ""){
           $("#saveid").attr("checked", true);
       }

       $("#saveid").change(function () {
           if($("#saveid").is(":checked")){
               let saveid = $("#login_em_id").val();
               setCookie("saveid",saveid,7);
           }else{
               deleteCookie("saveid");
           }
       });

       $("#login_em_id").keyup(function () {
           if($("#saveid").is(":checked")){
               let saveid = $("#login_em_id").val();
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