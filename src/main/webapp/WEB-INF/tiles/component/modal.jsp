<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
	 <style>
	 
	 .modal-open .modal {
	  display: block;
	}
	 
	 </style>
	 
	<!-- Modal Default Form -->
	 
	 
	 <div id = "defaultModal" class="modal fade" tabindex="-1" role="dialog" aria-hidden="false">
	    <div class="modal-dialog modal-register">
	        <div class="modal-content">
	            <div id="modalHeader" class="modal-header no-border-header text-center">
	                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
	                  <span aria-hidden="true">&times;</span>
	                </button> 
	            </div>
	            <div id="modalBody" class="modal-body"></div>	           
	        </div>
	    </div>
	</div>
	
	 <div id = "tripleModal" class="modal fade" tabindex="-1" role="dialog" aria-hidden="false">
	    <div class="modal-dialog modal-register">
	        <div class="modal-content">
	            <div id="modalHeader" class="modal-header no-border-header text-center">
	                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
	                  <span aria-hidden="true">&times;</span>
	                </button> 
	            </div>
	            <div id="modalBody" class="modal-body"></div>
	            <div id="modalFooter" class="modal-footer"></div>
	           
	        </div>
	    </div>
	</div>
	
	<script>
	
	//모달 생성 Object
	function Modal () {
	
		this.modal = $("#defaultModal").clone();
		this.header = this.modal.find("#modalHeader");
		this.body = this.modal.find("#modalBody");
		this.footer = this.modal.find("#modalFooter");
		
	}
	
	
	
	Modal.prototype.setModal = function(modal){
		
		this.modal = modal;
		this.header = this.modal.find("#modalHeader");
		this.body = this.modal.find("#modalBody");
		this.footer = this.modal.find("#modalFooter");
	
	}
	
	Modal.prototype.getModal = function(){
	
		return this.modal;
	}
	
	Modal.prototype.setHeader = function(header){
		this.header = header;
	}
	
	Modal.prototype.getHeader = function(){
		
		return this.header;
	}
			
	Modal.prototype.setBody = function(body){
		this.body = body;
	}
	
	Modal.prototype.getBody = function(){
		
		return this.body;
	}
	
	Modal.prototype.setFooter = function(footer){
		this.footer = footer;
	}
	
	Modal.prototype.getFooter = function(){
		
		return this.footer;
	}
	


	//모달 타입을 결정하는 팩토리 함수
	function ModalFactory(type, header, body, footer){
		
		this.modal = new Modal();
		
				switch(type){
				
					case 'simple' : {
					
						this.modal.getHeader().append(header);
						this.modal.getBody().append(body);
		
					}break;
					
					case 'triple' : {
						
						this.modal.setModal($("#tripleModal").clone());
						this.modal.getHeader().append(header);
						this.modal.getBody().append(body);
						this.modal.getFooter().append(footer);
						
						
		
					}break;
					
					default : break;

				}
			
				this.modal.getModal().modal();
				return this.modal.getModal();

		}
	
	
	function makePWModal(){
		
		let header = $("<h3>").attr("id", "findPwModal").addClass("modal-title text-center").html("비밀번호 찾기").append($("<p>").html("ê°ìì ìë ¥í ì´ë©ì¼ì ìë ¥íì¸ì.."));
		
		let successFunc = function(){
			
		}
		
		let failFunc = function(){
			
			
		}
		
		let body= $("<div>").addClass("form-group")
				.append($("<label>").html("Email"))
				.append($("<input>").attr("type", "email").attr("placeholder", "Email").addClass("form-control"))
				.append($("<br>"))
				.append(
						$("<button>").attr("id", "findPwInnerBtn").addClass("btn btn-block btn-round").html("ë¹ë°ë²í¸ ì°¾ê¸°").on('click', function(){ 
						
				 		let email = $(this).parent().find("input[type=email]").val();
						console.log("찾아온 이메일.." + email);
				 		
						//You request by AJAX, than decise by recived token.	
						let token = false;
						if(token){
						makeSimpleNotifyModal('비밀번호 변경', '임시 비밀번호가 이메일로 발송 되었습니다. \n 이메일을 확인 해 주세요', function(){});
						}
						else{
						makeSimpleNotifyModal('비밀번호 변경', '이메일 인증에 실패하였습니다. \n 관리자에게 문의 해 주세요.', function(){});
						}
						
						}))
				.append($("<br>"));		
					
		return ModalFactory("simple", header, body);
		
		
	
	}
	
	
	//이메일 회원가입 모달
	function makeJoinEmailModal(){
	
		 let header = $("<h3>").addClass("modal-title text-center")
		 			.append($("<label>").addClass("form-group").html("ì´ë©ì¼ë¡ íìê°ì"));
		 
		 let body = $("<div>").addClass("form-group")
		 			.append($("<label>").html("Email"))
		 			.append($("<input>").attr("type", "email").attr("placeholder", "Email").addClass("form-control"))
		 			.append($("<br>"))
		 			.append($("<label>").html("PW"))
		 			.append($("<input>").attr("type", "password").attr("placeholder", "Password").addClass("form-control"))
		 			.append($("<br>"))
		 			.append($("<label>").html("PW CHECK"))
		 			.append($("<input>").attr("type", "password").attr("placeholder", "Password Check").addClass("form-control"))
		 			.append($("<br>"))
		 			.append($("<button>").attr("id", "joinEmailBtn").addClass("btn btn-block btn-round").html("Join").on('click', function(e){

		 				makeSimpleNotifyModal('이메일로 회원가입', '인증 번호가 발송되었습니다..',  function(){});
		 				e.preventDefault();

					}))
					.append($("<br>"));

		 return ModalFactory("simple", header, body);


	}
	
	
	//복구용 계정 모달
	function makeRecoverModal(){
		
		 let header = $("<h3>").addClass("modal-title text-center").html("복구용 계정으로 로그인")
			 	.append($("<br>"))
				.append($("<p>").html("복구용 계정으로 로그인 하여 주십시오."));
			 
		 let body = $("<div>").addClass("form-group")
		 		.append($("<label>").html("ID..."))
		 		.append($("<input>").attr("type", "text").attr("placeHolder", "Email").addClass("form-control"))
		 		.append($("<br>"))
		 		.append($("<label>").html("PW.."))
		 		.append($("<input>").attr("type", "password").attr("placeHolder", "Password").addClass("form-control"))
		 		.append($("<button>").attr("id", "recoverSocialAuthBtn").addClass("btn btn-block btn-round").html("Login").css("margin-top", "40px").on('click', function(e){
		 			
		 			let id = $(this).parent().find("input[type=email]").val();
		 			let pwd = $(this).parent().find("input[type=pwd]").val();
		 			console.log("받아온 id " + id + " 받아온 pwd " + pwd);
		 			e.preventDefault();
		 			
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

	
	// header, body, footer로 이루어진 예, 아니오를 선택할 수 있는 모달
	function makeCheckableModal(title, subTitle, content, okFunc, refuseFunc){
		
		
		let successBtn = ($("<button>").attr("id", "btn_OK").attr("data-dismiss", "modal").addClass("btn btn-default btn-link").html("예"))
		.on("click", function(){
			okFunc();
		});
		
		let failBtn = ($("<button>").attr("id", "btn_Refuse").attr("data-dismiss", "modal").attr("type", "button").addClass("btn btn-danger btn-link close").html("아니오"))
		.on("click", function(){
			
			refuseFunc();
		});
		
		 let header = $("<h3>").addClass("modal-title text-center").html(title)
			.append($("<br>"))
			.append($("<p>").html(subTitle));
			
		let body = $("<div>")
			 .append("<h4>").html(content);
		
		 let footer = $("<div>").addClass("w-100").css("padding", "0")
		 				.append($("<div>").addClass("left-side").append(successBtn))
						.append($("<div>").addClass("divider"))
						.append($("<div>").addClass("right-side").append(failBtn));
						
		
		 
		 
		return  ModalFactory("triple",  header, body, footer);
			
		
	}


	// header + body로 이루어진 2단계 모달
	function makeSimpleNotifyModal (title, subTitle, content, closeParent){

		 let header = $("<h3>").addClass("modal-title text-center").html(title)
			.append($("<br>"))
			.append($("<p>").html(subTitle));

			
		 let body = $("<div>")
		 .append("<h4>").html(content);
		 
 		let modalFactory = ModalFactory("simple", header, body);
 		modalFactory.on('hide.bs.modal', function() {
				
				if(closeParent){
					closeParent.modal('hide');
					
				}
				
			});
 
	};
	






</script>                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                   