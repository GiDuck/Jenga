<%--
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>


<!-- 비밀번호 찾기 모달 -->

<div class="modal fade" id="findPwModal" tabindex="-1" role="dialog" aria-hidden="false">
    <div class="modal-dialog modal-register">
        <div class="modal-content">
            <div class="modal-header no-border-header text-center">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                  <span aria-hidden="true">&times;</span>
                </button>
                <h3 class="modal-title text-center">비밀번호 찾기</h3>
                <p>가입시 입력한 이메일을 입력하세요..</p>
            </div>
            <div class="modal-body">
                <div class="form-group">
                    <label>Email</label>
                <input type="email" value="" placeholder="Email" class="form-control" id="find_pwd"/>
              </div>
              <br>
                <button id="findPwInnerBtn" class="btn btn-block btn-round"> 비밀번호 찾기</button>
              <br>
            </div>
           
        </div>
    </div>
</div>


<!--  이메일 회원가입  모달 -->

<div class="modal fade" id="joinEmailModal" tabindex="-1" role="dialog" aria-hidden="false">
    <div class="modal-dialog modal-register">
        <div class="modal-content">
            <div class="modal-header no-border-header text-center">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                  <span aria-hidden="true">&times;</span>
                </button>
                <h3 class="modal-title text-center">이메일로 회원가입</h3>
                <br>
            </div>
            &lt;%&ndash; 유효성 검사 후 JOIN 버튼 누르면 컨트롤러에서 이메일, 비밀번호 EMEMBER에 넣고 uuid 생성해서 추가정보로 uuid 넘기면됨&ndash;%&gt;
            <form method="POST" id="form_setMemInfo" name="form_setMemInfo">
                <div class="modal-body">
                    <div class="form-group">
                        <label>Email</label>
                    <input type="email" value="" placeholder="Email" class="form-control" id="em_id" name="em_id"/><br>
                        <label>PW</label>
                    <input type="password" value="" placeholder="Password" class="form-control" id="em_pwd" name="em_pwd" />
                        <br>
                        <label>PW CHECK</label>
                    <input type="password" value="" placeholder="Password Check" id="em_pwd2" class="form-control" />
                        <br>
                        <label>Auth Key</label>
                    <input type="text" value="" placeholder="Auth Key" id="em_akey" name="em_akey" />

                  </div>
                  <br>
                    <button id="sendKeyBtn" class="btn btn-block btn-round"> 인증번호 전송</button>
                  <br>
                  <br>
                    <button id="joinEmailBtn" class="btn btn-block btn-round"> Join</button>
                  <br>
                </div>
            </form>
        </div>
    </div>
</div>


<!--  복구용 계정  모달 -->

<div class="modal fade" id="recoverAuthModal" tabindex="-1" role="dialog" aria-hidden="false">
    <div class="modal-dialog modal-register">
        <div class="modal-content">
            <div class="modal-header no-border-header text-center">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                  <span aria-hidden="true">&times;</span>
                </button>
                <h3 class="modal-title text-center">복구용 계정으로 로그인</h3>
                <br>
                <p>가입 혹은 활동 시 설정한 복구용 ID와 PW를 입력하세요.</p>
            </div>
            <div class="modal-body">
                <div class="form-group">
                    <label>ID..</label>
                <input type="text" value="" placeholder="Email" class="form-control" /><br>
                    <label>PW..</label>
                <input type="password" value="" placeholder="Password" class="form-control" />
              </div>
              <br>
                <button id="recoverAuthLogin" class="btn btn-block btn-round"> Login</button>
              <br>
            </div>

        </div>
    </div>
</div>


<!--  복구용 계정  모달 -->

<div class="modal fade" id="recoverSocialAuthSync" tabindex="-1" role="dialog" aria-hidden="false">
    <div class="modal-dialog modal-register">
        <div class="modal-content">
            <div class="modal-header no-border-header text-center">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                  <span aria-hidden="true">&times;</span>
                </button>
                <h3 class="modal-title text-center">동기화 할 계정을 선택하십시오.</h3>
                <br>
                <p>이메일 혹은 소셜 계정으로 동기화 시킬 수 있습니다.</p>
            </div>
            <div class="modal-body">
                <div class="form-group">
                    <label>Email</label>
                <input type="email" value="" placeholder="Email" class="form-control" /><br>
                    <label>PW</label>
                <input type="password" value="" placeholder="Password" class="form-control" />
              </div>
              <br>
                <button id="recoverSocialAuthSyncBtn" class="btn btn-block btn-round"> Login</button>
              <br>
            </div>
            
            

        </div>
    </div>
</div>



<!-- 기본 확인 모달 -->

<div class="modal fade" id="simpleNotifyModal" tabindex="-1" role="dialog" aria-labelledby="simpleNotifyModalLabel" aria-hidden="true">
  <div class="modal-dialog" role="document">
    <div class="modal-content">
      <div class="modal-header">
        <h5 class="modal-title" id="simpleNotifyModalLabel"></h5>
        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
          <span aria-hidden="true">&times;</span>
        </button>
      </div>
      <div class="modal-body"></div>
      <div class="modal-footer">
          <div class="w-100">
              <button id="closeBtn" type="button" class="btn btn-default btn-link" data-dismiss="modal"></button>
          </div>
      </div>
    </div>
  </div>
</div>

<script>

( _ => {

	
	

    var makeSimpleNotifyModal = function(mTitle, mContent, mCloseName, closeParent){

        var simpleNotifyModal= $("#simpleNotifyModal");
        simpleNotifyModal.find(".modal-title").html(mTitle);
        simpleNotifyModal.find(".modal-body").html(mContent);
        simpleNotifyModal.find("#closeBtn").html(mCloseName);
        simpleNotifyModal.find("#closeBtn").on('click', _ => {

        });

        simpleNotifyModal.modal({show : true});
        /*simpleNotifyModal.on('hide.bs.modal', function(e){

            location.href = "/setMemInfo";

        });*/

    };
	// 비밀번호 찾기
    // 빈칸 유효성검사
	$("#findPwInnerBtn").on('click', _ => {

	    $.ajax({
            url : "/findPwd",
            type : "post",
            data : {"find_pwd" : $("#find_pwd").val()},
        }).done(function (responseData) {
            var data = responseData;
            if(data.indexOf('success') != -1){
                $("#find_pwd").val("");
                makeSimpleNotifyModal('이메일 전송 완료', '임시 비밀번호가 이메일로 발송되었습니다. \n 확인 해 주세요.', '닫기', $("#findPwInnerBtn").parents("#findPwModal"));
                return false;
            }else if(data.indexOf('error') != -1){
                $("#find_pwd").val("");
                makeSimpleNotifyModal('이메일 전송 실패', '존재하지 않는 이메일입니다. \n 다시 확인 해 주세요.', '닫기', $("#findPwInnerBtn").parents("#findPwModal"));
                return false;
            }
        })

	});

	// 인증키 메일에 보내기 -> 여기서 유효성검사(빈칸, 비번체크 등)

    // /authCheck에서 이메일 중복검사
	$("#sendKeyBtn").on('click', _ => {
        event.preventDefault();
        alert($("#em_id").val());
        alert($("#em_pwd").val());
        console.log("뭐고"+$("#em_id").val());
        $.ajax({
            url: "/authCheck",
            type: "post",
            data: {
                "em_id": $("#em_id").val(),
                "em_pwd": $("#em_pwd").val()
            },

        }).done(function (responseData) {
               console.log(responseData);

               // 이메일이 존재하지 않을때 / 이메일 O, 인증여부 Y
                if(responseData.indexOf('sendAuthKey') != -1){
                    makeSimpleNotifyModal('아직 한 단계가 더 남았습니다.', '이메일로 인증번호가 전송되었습니다. \n 확인 해 주세요.', '닫기', $("#sendKeyBtn").parents("#joinEmailModal"));
                    $("#em_id").prop('readonly', true);
                    $("#em_pwd").prop('readonly', true);
                    $("#em_pwd2").prop('readonly', true);
                // 이미 가입한 이메일 일 때
                }else if(responseData.indexOf('isExist') != -1){
                    makeSimpleNotifyModal('아직 한 단계가 더 남았습니다.', '이미 존재하는 이메일입니다. \n 다시 확인 해 주세요.', '닫기', $("#sendKeyBtn").parents("#joinEmailModal"));
                    $("#em_id").val("");
                    $("#em_pwd").val("");
                    $("#em_pwd2").val("");
                }
            })
        });

	// 인증번호 전송버튼에서 체크하고 이메일로 보낸 Auth Key를 체크해서 추가입력 페이지로 넘어감
	$("#joinEmailBtn").on('click', _ => {
        event.preventDefault();
	    alert("클릭됐다");
	    $.ajax({
            url: "/join",
            type: "post",
            data: {
                "em_id" : $("#em_id").val(),
                "em_akey": $("#em_akey").val()
            },
            success : function(responseData){
                alert("펑션실행");
                alert(responseData);
                if (responseData.indexOf('success') != -1) {
                    makeSimpleNotifyModal('이메일 인증 완료', '인증이 완료되었습니다.', '닫기', $("#joinEmailBtn").parents("#joinEmailModal"));
                    /*$("#em_id").val("");
                    $("#em_pwd").val("");
                    $("#em_pwd2").val("");
                    $("#em_akey").val("");*/
                    $("#simpleNotifyModal").on('hide.bs.modal',function (e) {
                        let d = document.form_setMemInfo;
                        d.method = "post"
                        d.action = "/setMemInfo"
                        d.submit();
                        e.stopImmediatePropagation();
                    });

                } else if (responseData.indexOf('error') != -1) {
                    alert("에러에러에러")
                    makeSimpleNotifyModal('아직 한 단계가 더 남았습니다.', '인증번호가 올바르지 않습니다. \n 다시 확인 해 주세요.', '닫기', $("#joinEmailBtn").parents("#joinEmailModal"));
                    $("#em_akey").val("");
                }
            }
        })/*.done(function (responseData) {
            alert("펑션실행");
            alert(responseData);
            if (responseData.indexOf('success') != -1) {
                makeSimpleNotifyModal('이메일 인증 완료', '인증이 완료되었습니다.', '닫기', $("#joinEmailBtn").parents("#joinEmailModal"));
                /!*$("#em_id").val("");
                $("#em_pwd").val("");
                $("#em_pwd2").val("");
                $("#em_akey").val("");*!/
                $("#joinEmailBtn").on(function () {
                    alert("버튼 실행")
                    var d = document.form_setMemInfo;
                    d.method = "post"
                    d.action = "/setMemInfo"
                    d.submit();
                })
            } else if (responseData.indexOf('error') != -1) {
                alert("에러에러에러")
                makeSimpleNotifyModal('아직 한 단계가 더 남았습니다.', '인증번호가 올바르지 않습니다. \n 다시 확인 해 주세요.', '닫기', $("#joinEmailBtn").parents("#joinEmailModal"));
                $("#em_akey").val("");
            }
        })*/

    })
		// makeSimpleNotifyModal('아직 한 단계가 더 남았습니다.', '이메일로 인증번호가 전송되었습니다. \n 확인 해 주세요.', '닫기', $("#joinEmailBtn").parents("#joinEmailModal"));

	})();

</script>--%>
