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
	
	let innerBtn = $("#findPwInnerBtn"); 
	
	innerBtn.on('click', _ => {
	
		makeSimpleNotifyModal('이메일 전송 완료', '임시 비밀번호가 이메일로 발송되었습니다. \n 확인 해 주세요.', '닫기', innerBtn.parents("#findPwModal"));
	
	});

})();

var makeSimpleNotifyModal = function(mTitle, mContent, mCloseName, closeParent){

	
	var simpleNotifyModal= $("#simpleNotifyModal");
		simpleNotifyModal.find(".modal-title").html(mTitle);
		simpleNotifyModal.find(".modal-body").html(mContent);
		simpleNotifyModal.find("#closeBtn").html(mCloseName);
		simpleNotifyModal.find("#closeBtn").on('click', _ => {

			
		});
		
		simpleNotifyModal.on('hide.bs.modal', _ => {
			
			
			if(closeParent){
				console.log(closeParent);
				closeParent.modal('hide');
				
			}
			
		});
		
		simpleNotifyModal.modal({show : true});
		
	

	
	
	
}


</script>