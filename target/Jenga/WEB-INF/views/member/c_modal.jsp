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
                <input type="email" value="" placeholder="Email" class="form-control" />
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
            <%-- 유효성 검사 후 JOIN 버튼 누르면 컨트롤러에서 이메일, 비밀번호 EMEMBER에 넣고 uuid 생성해서 추가정보로 uuid 넘기면됨--%>
            <form method="POST" action="/setMemInfo">
                <div class="modal-body">
                    <div class="form-group">
                        <label>Email</label>
                    <input type="email" value="" placeholder="Email" class="form-control" name="em_id"/><br>
                        <label>PW</label>
                    <input type="password" value="" placeholder="Password" class="form-control" name="em_pwd" />
                        <br>
                        <label>PW CHECK</label>
                    <input type="password" value="" placeholder="Password Check" class="form-control" />

                  </div>
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
			
			
			simpleNotifyModal.on('hide.bs.modal', _ => {
				
				if(closeParent){
					closeParent.modal('hide');
					
				}
				
			});
			
			simpleNotifyModal.modal({show : true});

	};
	
	$("#findPwInnerBtn").on('click', _ => {
		
		makeSimpleNotifyModal('이메일 전송 완료', '임시 비밀번호가 이메일로 발송되었습니다. \n 확인 해 주세요.', '닫기', $("#findPwInnerBtn").parents("#findPwModal"));
		
	});
	
	// $("#joinEmailBtn").on('click', _ => {
	//
	// 	makeSimpleNotifyModal('아직 한 단계가 더 남았습니다.', '이메일로 인증번호가 전송되었습니다. \n 확인 해 주세요.', '닫기', $("#joinEmailBtn").parents("#joinEmailModal"));
	//
	// });
	
	
	
	
})();





</script>