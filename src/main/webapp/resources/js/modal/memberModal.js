function requireJs(url){

    var script = document.createElement('script');
    script.src = url;
    document.getElementsByTagName('head')[0].appendChild(script);

}

(function(){
    requireJs("/resources/js/common.js");
    requireJs("/resources/js/member.js");
})();


//PW Modal
function makePWModal(){

    let header = $("<h3>").attr("id", "findPwModal").addClass("modal-title text-center").html("비밀번호 찾기").append($("<p>").html("가입시 입력한 이메일을 입력하세요.."));
    let body= $("<div>").addClass("form-group")
        .append($("<label>").html("Email"))
        .append($("<input>").attr("type", "email").attr("placeholder", "Email").addClass("form-control"))
        .append($("<br>"))
        .append(
            $("<button>").attr("id", "findPwInnerBtn").addClass("btn btn-block btn-round").html("Find").on('click', function(){

                let email = $(this).parent().find("input[type=email]").val();
                $.ajax({
                    url : "/findPwd",
                    type : "post",
                    data : {"find_pwd" : email},
                }).done(function (responseData) {

                    let token = false;
                    if(responseData.indexOf('success') != -1){
                        makeSimpleNotifyModal('비밀번호 변경', '임시 비밀번호가 이메일로 발송 되었습니다. \n 이메일을 확인 해 주세요', function(){});
                    }
                    else if(responseData.indexOf('error') != -1)
                        makeSimpleNotifyModal('비밀번호 변경', '이메일 인증에 실패하였습니다. \n 관리자에게 문의 해 주세요.', function(){});
                })
            }))
        .append($("<br>"));

    return ModalFactory("simple", header, body);



}

//Email Join Modal
function makeJoinEmailModal(){
    let reqTime = -1;
    let reqSemaphore = false;

    let header = $("<h3>").addClass("modal-title text-center")
        .append($("<label>").addClass("form-group").html("이메일로 회원가입"));

    let body = $("<form>").addClass("form-group").attr("id","form_setMemInfo").attr("name","form_setMemInfo").attr("method","post").on("submit", function(e){
        e.preventDefault();
        return false;
    })
        .append($("<label>").html("Email"))
        .append($("<input>").attr("type", "email").attr("placeholder", "Email").attr("name","em_id").attr("id", "em_id").addClass("form-control").on('keyup', function(e){

            e.stopPropagation();
            let $email = $(e.target).closest("form").find("#em_id");
            setTimeout(function(){
                checkEmailFieldValid($email);

            }, 50);

        }))
        .append($("<br>"))
        .append($("<label>").html("PW"))
        .append($("<input>").attr("id", "em_pwd").attr("type", "password").attr("name","em_pwd").attr("placeholder", "영문자와 특수문자가 1개 이상 포함된 8~16자리").addClass("form-control").on('keyup', function(e){

            setTimeout(function(){
                $pwdCheck = $(e.target).closest("form").find("#em_pwdCheck");
                $pwd = $(e.target).closest("form").find("#em_pwd");

                checkPasswordFieldValid($pwd);
                confirmPasswordField($pwd, $pwdCheck);


            }, 50);

        }))
        .append($("<br>"))
        .append($("<label>").html("PW CHECK"))
        .append($("<input>").attr("id", "em_pwdCheck").attr("type", "password").attr("placeholder", "Password Check").addClass("form-control").on('keyup', function(e){

            setTimeout(function(){

                $pwd = $(e.target).closest("form").find("#em_pwd");
                $pwdCheck = $(e.target).closest("form").find("#em_pwdCheck");

                confirmPasswordField($pwd, $pwdCheck);


            }, 50);


        }))
        .append($("<br>"))
        .append($("<button>").attr("id", "joinEmailBtn").addClass("btn btn-block btn-round").html("Join").on('click', function(e){

            if(reqTime === -1){
                reqTime = new Date().getTime();
            }else{
                let nowTime = new Date().getTime();
                let timeInterval = parseInt(nowTime - reqTime);
                console.log(reqTime);
                console.log(timeInterval);
                if(timeInterval < 5000 || reqSemaphore){
                    swal("요청 중..", "요청이 진행중입니다. 잠시 후 다시 시도해 주세요.", "info"); return;
                }
            }

            let email = $(this).closest("form").find("#em_id").val();
            let pwd = $(this).closest("form").find("#em_pwd").val();
            let checkPwd = $(this).closest("form").find("#em_pwdCheck").val();

            if(!validCheckAuth(email, pwd)){
                reqTime = -1;
                swal("가입 실패", "입력 정보가 올바르지 않습니다. 다시 확인 해 주세요.", "error");
                return;
            }

            if(pwd !== checkPwd){
                reqTime = -1;
                swal("가입 실패", "비밀번호가 같지 않습니다. 다시 확인 해 주세요.", "error");
                return;
            }
            reqSemaphore = true;

            $.ajax({
                url: "/authCheck",
                type: "post",
                data: {"email": email, "password" : pwd},
                success: function (responseData) {
                    let statusCode = parseInt(responseData);
                    let elements = ["#em_id", "#em_pwd", "#em_pwd2"];

                    if (statusCode === authStatusCode.AUTH_SUCCESS || statusCode === authStatusCode.AUTH_NOT_VALID) {
                        makeAuthModal(email, pwd);
                        for (let i = 0; i < elements.length; ++i) {
                            $(elements[i]).prop('readonly', true);
                        }
                    } else if (statusCode === authStatusCode.AUTH_EXIST) {

                        swal("인증 실패", "이미 가입되어 있는 이메일입니다.", "error");
                        for (let i = 0; i < elements.length; ++i) {
                            $(elements[i]).val("");
                        }
                        reqTime = -1;
                        return;
                    }else{
                        swal("인증 실패", "인증에 실패하였습니다.", "error");
                        busyLoadHide();
                    }
                    e.preventDefault();
                    reqSemaphore = false;
                }, error: function (xhs, status, error) {
                    swal("가입 실패", "인증 중 문제가 발생하였습니다.", "error");
                    console.log(error);
                    $(document).find(".modal").modal("hide");
                }
            })
        }))
        .append($("<br>"));



    return ModalFactory("simple", header, body);

}

function makeAuthModal(id, pwd){

    busyLoadHide();
    let header = $("<h3>").addClass("modal-title text-center")
        .append($("<label>").addClass("form-group").html("이메일 인증하기"));

    let body = $("<div>").addClass("form-group")
        .append($("<label>").html("인증 문자 입력"))
        .append($("<p>").html("이메일로 전송된 인증문자를 입력 해 주세요."))
        .append($("<input>").attr("id","em_akey").attr("type", "text").attr("placeholder", "인증문자를 입력하세요.").addClass("form-control"))
        .append($("<br>"))
        .append($("<input>").attr("type", "button").addClass("btn btn-info w-100 text-center").val("인증하기").on('click', function(e){
            e.preventDefault();
            let authKey = $(this).closest(".form-group").find("#em_akey").val();
            if(!authKey || (authKey.replace(REGEX_TRIM_VOID, "")).length  < 1){
                swal("인증 문자 오류", "인증 문자를 정확하게 입력 해 주십시오.", "error");
                return;
            }
            $.ajax({
                url: "/join",
                type: "post",
                data: {
                    email : id,
                    authKey : authKey
                },
                success : function(responseData){
                    let statusCode = parseInt(responseData);
                    if (statusCode === authStatusCode.AUTH_SUCCESS) {
                        swal({
                            title : "인증 성공",
                            text : "인증이 성공적으로 끝났습니다.",
                            type : "success",
                            showConfirmButton: false,
                            timer : 2000

                        }).then(function(){

                            $(document).find(".modal").modal("hide");
                            window.location.replace("/login");
                        });

                    }else{
                        swal("가입 실패", "가입에 실패하였습니다.", "error");
                        busyLoadHide();
                        $(document).find(".modal").modal("hide");

                    }


                }
            })

        }));



    return ModalFactory("simple", header, body);


}



//복구용 계정 모달
function makeRecoverModal(){

    let header = $("<h3>").addClass("modal-title text-center").html("복구용 계정으로 로그인")
        .append($("<br>"))
        .append($("<p>").html("복구용 계정으로 로그인 하여 주십시오."));

    let body = $("<div>").addClass("form-group")
        .append($("<label>").html("ID..."))
        .append($("<input>").attr("type", "email").attr("placeHolder", "Email").addClass("form-control"))
        .append($("<br>"))
        .append($("<label>").html("PW.."))
        .append($("<input>").attr("type", "password").attr("placeHolder", "Password").addClass("form-control"))
        .append($("<button>").attr("id", "recoverSocialAuthBtn").addClass("btn btn-block btn-round").html("Login").css("margin-top", "40px").on('click', function(e){

            let id = $(this).parent().find("input[type=email]").val();
            let pwd = $(this).parent().find("input[type=password]").val();
            e.preventDefault();

        }))
        .append($("<br>"));

    return ModalFactory("simple", header, body);


}



function makeModifyMyProfileModal(){


    let $img = $("<img>").attr("src", "").attr("onerror", "this.src='${pageContext.request.contextPath}/resources/assets/img/image_placeholder.jpg'").addClass("w-100");
    let $inputTitle = $("<input>").attr("name", "title").attr("placeholder", "제목을 입력하세요..").addClass("form-control border-input");
    let $inputGroup = $("<input>").attr("name", "group").attr("placeholder", "지역 혹은 소속된 그룹을 입력하세요..").addClass("form-control border-input");
    let $inputLink = $("<input>").attr("name", "link").attr("placeholder", "연결할 링크를 입력하세요.").addClass("form-control border-input");
    let $inputFile = $("<input>").attr("type", "file").attr("name", "imageFIle").addClass("form-control border-input");

    $inputFile.on("change", function(e){
        checkImageFile(e);
        $img.css("display","none");
        let files =  $(e.target).prop("files");
        let file = files[0];
        let imagePath =URL.createObjectURL(file);
        $img.attr("src", imagePath);
        $img.fadeIn("slow");


    });

    let header = $("<h3>").addClass("modal-title text-center").html("내 프로필 페이지 수정하기")
        .append($("<br>"));

    let body = $("<div>").addClass("form-group")
        .append($("<label>").html("Title"))
        .append($inputTitle)
        .append($("<br>"))
        .append($("<label>").html("Group"))
        .append($inputGroup)
        .append($("<br>"))
        .append($("<label>").html("Web Link"))
        .append($inputLink)
        .append($("<br>"))
        .append($("<label>").html("Background Image"))
        .append($img)
        .append($inputFile)
        .append($("<br>"));

    let footer = $("<div>").addClass("w-100 ml-auto mr-auto")
        .append($("<div>").append($("<button>").addClass("btn btn-success w-75").html("수정하기").on("click", function(e){

            e.preventDefault();
            alert("수정하기 클릭!");

        })));


    return ModalFactory("triple",  header, body, footer);


}


function makeModifyPWModal(authCodeArr){



    let header = $("<h3>").attr("id", "modPwModal").addClass("modal-title text-center").html("비밀번호 수정");
    let body= $("<div>").addClass("form-group")
        .append($("<label>").html("새로운 비밀번호 입력"))
        .append($("<input>").attr("id", "newPwInput").attr("type", "password").attr("placeholder", "영문자, 특수문자 중 1개 포함 8 ~ 16자").addClass("form-control").on("keyup", function(e){
            let $pwd = $(this).parent().find("#newPwInput");
            let $pwdCheck = $(this).parent().find("#newPwCheckInput");


            setTimeout(function(){
                checkPasswordFieldValid($pwd);
                confirmPasswordField($pwd, $pwdCheck);

            }, 50);

        }))
        .append($("<br>"))
        .append($("<label>").html("새로운 비밀번호 확인"))
        .append($("<input>").attr("id", "newPwCheckInput").attr("type", "password").attr("placeholder", "비밀번호 확인").addClass("form-control").on("keyup", function(){
            let $pwd = $(this).parent().find("#newPwInput");
            let $pwdCheck = $(this).parent().find("#newPwCheckInput");

           setTimeout(function(){
               confirmPasswordField($pwd, $pwdCheck);
           }, 50);


        }))
        .append($("<br>")).append($("<br>"))
        .append(
            $("<button>").attr("id", "modPwInnerBtn").addClass("btn btn-block btn-round").html("수정하기").on('click', function(){

                let $pwd = $(this).parent().find("#newPwInput");
                let $pwdCheck = $(this).parent().find("#newPwCheckInput");

                if(!validPassword($pwd.val())){
                    swal("비밀번호 수정 실패", "비밀번호가 올바른 형식이 아닙니다.", "error");
                    return;
                }else if($pwd.val() !== $pwdCheck.val()){

                    swal("비밀번호 수정 실패", "확인한 비밀번호가 같지 않습니다.", "error");
                    return;

                }

                  $.ajax({
                    url : "/changePwd",
                    type : "post",
                    data : {"password" : $pwd.val()},
                      success : function(statusCode){


                          if(statusCode == authStatusCode.MOD_SUCCESS){
                            swal("수정 성공", "비밀번호 수정이 성공하였습니다.", "success");
                            $(document).find(".modal").modal("hide");
                        }else{
                            swal("수정 실패", "비밀번호 수정이 실패 하였습니다.", "error");

                        }
                      }, error : function(xhs, status, error){
                          swal("수정 실패", "비밀번호 수정 중 오류가 발생하였습니다. 문제가 지속되면 관리자에게 문의하십시오.", "error");
                          console.error(status);
                      }

                });
            }))
        .append($("<br>"));

    return ModalFactory("simple", header, body);



}



//소셜 로그인과 동기화 모달
/* function makeRecoverModalSync(){

    modal.getModal().attr("id", "recoverSocialAuthSync");

     let header = $("<h3>").addClass("modal-title text-center").html("소셜 계정과 동기화 하기")
                 .append($("<br>"))
                 .append($("<p>").html(""));

     let body = $("<div>").addClass("form-group")
                 .append($("<label>")).html("Email")
                 .append($("<input>").attr("type", "email")).attr("placeholder", "Email").addClass("form-control")
                 .append($("<br>"))
                 .append($("<br>"))
                 .append($("<button>").attr("id", "recoverSocialAuthSyncBtn").addClass("btn btn-block btn-round").html("Login"))
                 .append($("<br>"));

     return ModalFactory("simple", header, body);


} */
