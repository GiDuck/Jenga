<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
    
    
<script src="${pageContext.request.contextPath}/resources/assets/js/regexManager.js"></script>  
<script src="${pageContext.request.contextPath}/resources/js/mem_js.js"></script>  

<%--	 <style>
	 .modal-open .modal {
	  display: block;
	}
	 
	 </style>--%>
	 
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
	
	//Modal Object
	//Modal is Mother Object of all Modals.
	//.. You can inherit and overwrite custom modals by this Object. 
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
	


	//Modal Factory Function
	//.. You can make a modal to match your purpose simplly.
	//... Parameter is type (simple, triple), simple is made by two part. header and body.
	//... triple is made by three part. header and body and footer.
	//... You can define DOM object and inject that.
	//... So Modal Factory will be assemble that.
	
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
	
	//PW Modal
	function makePWModal(){
		
		let header = $("<h3>").attr("id", "findPwModal").addClass("modal-title text-center").html("비밀번호 찾기").append($("<p>").html("가입시 입력한 이메일을 입력하세요.."));
		
		let successFunc = function(){
			
		}
		
		let failFunc = function(){
			
			
		}
		
		let body= $("<div>").addClass("form-group")
				.append($("<label>").html("Email"))
				.append($("<input>").attr("type", "email").attr("placeholder", "Email").addClass("form-control"))
				.append($("<br>"))
				.append(
						$("<button>").attr("id", "findPwInnerBtn").addClass("btn btn-block btn-round").html("Find").on('click', function(){ 
						
				 		let email = $(this).parent().find("input[type=email]").val();
						console.log("찾아온 이메일.." + email);
				 		
						//You request by AJAX, than decise by recived token.	
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

        let header = $("<h3>").addClass("modal-title text-center")
            .append($("<label>").addClass("form-group").html("이메일로 회원가입"));

        let body = $("<form>").addClass("form-group").attr("id","form_setMemInfo").attr("name","form_setMemInfo").attr("method","post").on("submit", function(e){
            e.preventDefault();
            return false;
        })
            .append($("<label>").html("Email"))
            .append($("<input>").attr("type", "email").attr("placeholder", "Email").attr("name","em_id").attr("id", "em_id").addClass("form-control"))
            .append($("<br>"))
            .append($("<label>").html("PW"))
            .append($("<input>").attr("id", "em_pwd").attr("type", "password").attr("name","em_pwd").attr("placeholder", "영문자와 특수문자가 1개 이상 포함된 8~16자리").addClass("form-control").on('keydown focus', function(e){


                let pwd = undefined;

                setTimeout(function(){
                    console.log($('#em_id').val());
                    $pwdCheck = $(e.target).parent().find("#pwdCheck");
                    $pwd = $(e.target);


                    pwd = $(e.target).val();

                    if(REGEX_PASSWORD.test(pwd)){
                        $pwd.css("background-color", "#BEEFFF");



                    }else{

                        $pwd.css("background-color", "#FFE6E6");


                    }

                    if($pwdCheck.val() === pwd && (pwd && $(e.target).val())){

                        $pwdCheck.css("background-color", "#BEEFFF");
                    }else{

                        $pwdCheck.css("background-color", "#FFE6E6");

                    }


                }, 500);

            }))
            .append($("<br>"))
            .append($("<label>").html("PW CHECK"))
            .append($("<input>").attr("id", "pwdCheck").attr("type", "password").attr("placeholder", "Password Check").addClass("form-control").on('keydown focus', function(e){
                let pwd = undefined;

                setTimeout(function(){

                    $pwd = $(e.target).parent().find("#pwd");

                    if($pwd.val() === $(e.target).val() && ($pwd.val() && $(e.target).val())){

                        $(e.target).css("background-color", "#BEEFFF");
                    }else{

                        $(e.target).css("background-color", "#FFE6E6");

                    }


                }, 500);


            }))
            .append($("<br>"))
            .append($("<button>").attr("id", "joinEmailBtn").addClass("btn btn-block btn-round").html("Join").on('click', function(e){


                setTimeout(function(){

                },2000);


                let id = $(this).parent().find("input[type=email]").val();
                console.log(id);
                let pwd = $(this).parent().find("#em_pwd").val();
                let checkPwd = $(this).parent().find("#pwdCheck").val();


                if(!validCheckAuth(id, pwd)){

                    return;

                }

                if(pwd !== checkPwd){

                    makeSimpleNotifyModal('이메일로 회원가입', '비밀번호가 동일하지 않습니다. 다시 확인해 주십시오.',  function(){});
                    return;

                }




                //Please request on here by AJAX to Server.
                //.. You can receive token that procedure was fine or bad.
                $.ajax({
                    url: "/authCheck",
                    type: "post",
                    data: {
                        "em_id": id,
                        "em_pwd": pwd
                    },

                }).done(function (responseData) {
                    console.log(responseData);
                    // let token = true;

                    // 이메일이 존재하지 않을때 / 이메일 O, 인증여부 Y
                    if (responseData.indexOf('sendAuthKey') != -1) {
                        makeAuthModal(id);
                        $("#em_id").prop('readonly', true);
                        $("#em_pwd").prop('readonly', true);
                        $("#em_pwd2").prop('readonly', true);

                        // 이미 가입한 이메일 일 때
                    } else if (responseData.indexOf('isExist') != -1) {
                        makeSimpleNotifyModal('이메일로 회원가입', '이메일 형식이 잘못되었거나 존재하는 이메일입니다. 다시 확인해 주세요.', function () { //수정함
                        });
                        $("#em_id").val("");
                        $("#em_pwd").val("");
                        $("#em_pwd2").val("");
                    }
                    e.preventDefault();
                })
            }))
            .append($("<br>"));



        return ModalFactory("simple", header, body);

    }


	//You can check auth string by this modal.
	function makeAuthModal(id, md){

		
		let header = $("<h3>").addClass("modal-title text-center")
			.append($("<label>").addClass("form-group").html("이메일 인증하기"));
		
		
		 let body = $("<div>").addClass("form-group")
			.append($("<label>").html("인증 문자 입력"))
			.append($("<p>").html("이메일로 전송된 인증문자를 입력 해 주세요."))
			.append($("<input>").attr("id","em_akey").attr("type", "text").attr("placeholder", "인증문자를 입력하세요.").addClass("form-control"))
 			.append($("<br>"))
 			.append($("<input>").attr("type", "button").addClass("btn btn-info w-100 text-center").val("인증하기").on('click', function(e){

 				
 				e.preventDefault();
                alert("클릭됐다");
                $.ajax({
                    url: "/join",
                    type: "post",
                    data: {
                        "em_id" : id,
                        "em_akey": $(this).parent().find("input[type=text]").val()
                    },
                    success : function(responseData){
                        alert("펑션실행");
                        alert(responseData);
                        if (responseData.indexOf('success') != -1) {
                            let simpleModal = makeSimpleNotifyModal('', '인증에 성공하였습니다.', '');

                            simpleModal.unbind('hide.bs.modal');
                            let ad = document.form_setMemInfo;
                            console.log(ad);
                            console.log(simpleModal.find("input[type=email]").val());
                            console.log(simpleModal.find("input[type=password]").val());


                            console.log($('#em_id').val());
                            console.log($('#em_pwd').val());

                            simpleModal.on('hide.bs.modal',function (e) {
							console.log("이메일머임?");
							ad.action = "/setMemInfo";

							ad.submit();
							e.stopImmediatePropagation();

                            });



                        } else if (responseData.indexOf('error') != -1) {
                            makeSimpleNotifyModal('', '인증 실패하였습니다.', function(){});
                        }
                    }
                })

 				//Please make to request using AJAX to server.
 				//..below source will be embbed in success function on AJAX.

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

 		return modalFactory;
 
	};

	// 북마크파일 업로드
    function makeUploadBookMarkFileModal(type){

        let $syncDateField;

        if(type == "chrome"){

            $syncDateField = $("p[name='chromeSyncDate']");


        }else if(type == "explore"){

            $syncDateField =  $("p[name='exploreSyncDate']");


        }else{

            swal({

                text : "지원되지 않는 북마크 입니다.",
                type : "error"

            });

            return;

        }


        let header = $("<h3>").addClass("modal-title text-center").html(type + " 북마크 동기화")
            .append($("<br>"))
            .append($("<p>").html(type + "북마크를 동기화 합니다. 북마크 내보내기 기능을 사용하여 HTML 파일로 지정해 주십시오."));


        let body = $("<div>").addClass("w-100")
            .append($("<div>").append($("<input>").attr("type", "file").attr("id", "BKfile").attr("accept", ".html")))
            .append($("<br>"))
            .append($("<div>").append($("<div>").addClass("btn w-100").html("북마크 동기화 ")).on('click', function(e){

                e.stopPropagation();
                e.preventDefault();

                let $upFile = document.getElementById("BKfile");
                let upFile = $upFile.files[0];
                let formData = new FormData();
                formData.append('file', upFile);
                formData.append('bp_browstype', type)
                formData.append('bp_booktype', 'html')
                console.log('fixed');


                if(upFile.size > 10485760 ){

                    swal('파일 업로드 실패', '파일의 크기는 10MB를 초과할 수 없습니다.', 'error');
                    return;

                }else if(upFile.size < 10){


                    swal('파일 업로드 실패', '파일이 없거나 크기가 너무 작습니다.', 'error');
                    return;

                }else if(upFile.type != "text/html"){

					swal('파일 업로드 실패', '가져오기는 html 파일만 가능합니다.', 'error');
					return;

				}

                $.ajax({

                    type : "POST",
                    url : "/board/fileUpload",
                    data : formData,
                    enctype: 'multipart/form-data',
                    processData: false,
                    contentType : false,
                    success : function(){
                        swal('동기화 성공', '북마크가 성공적으로 업로드 되었습니다.', 'success');
                        let syncDate = new Date();
                        let syncComDateStr = syncDate.getFullYear() + "/" + (syncDate.getMonth()+1)+"/" + syncDate.getDate()
                        + " " + syncDate.getHours()+":"+syncDate.getMinutes();
                        $syncDateField.html(syncComDateStr);

                    },
                    error : function(xhs, status, error){

                        swal('동기화 실패', '북마크 동기화에 실패하였습니다.', 'error');
                        console.log("북마크 동기화 실패..." + status);

                    }


                });


            }));



        return ModalFactory("simple", header, body);




    }

    //북마크 혹은 폴더의 내용을 변경하게 하는 모달
    function makeAddOrModifyElementModal($element, isAdd, successFunc)
    {
        let $title = $($element).find("input[name='title']");
        let $url = $($element).find("input[name='url']");
        let isFolder = $($element).find("input[name='isFolder']").val();
        let timeStamp = $($element).find("input[name='timeStamp']").val();
        let type = undefined;
        let token = isAdd ? "추가" : "수정";
        let bookmarks = popChild("right");

        if(isFolder == "true"){

            type = "폴더";

        }else if(isFolder == "false"){

            type = "북마크";
        }

        let header = $("<h3>").addClass("modal-title text-center").html(type + " "+token+"하기")
            .append($("<br>"));

        let body = $("<div>").addClass("form-group")
            .append($("<label>").html("TITLE"))
            .append($("<input>").attr("name", "title").addClass("form-control border-input").val($title.val()))
            .append($("<br>"));

        if(isFolder == "false"){
            body.append($("<label>").html("URL"))
                .append($("<input>").attr("name", "url").addClass("form-control border-input").val($url.val()));
        }

        body.append($("<button>").attr("name", "elementSubmitBtn").addClass("btn btn-block btn-round").html(token+"하기").css("margin-top", "40px").on('click', function(e){
            e.preventDefault();

            let title = $(e.target).parent().find("input[name='title']").val();
            let url = undefined;
            let selectedElement = undefined;



            //수정일 경우 Action
            if(!isAdd){


                for(let i = 0; i<bookmarks.length; ++i){

                    if(bookmarks[i].add_date == timeStamp){

                        selectedElement = bookmarks[i];
                        break;
                    }

                }

                selectedElement.title = title;


            }


            if(isFolder == "false"){

                        url = $(this).parent().find("input[name='url']").val();

                        if(!isAdd){
                        selectedElement.url = url;

                        }

                if(successFunc){
                    successFunc(bookmarks, title, url);
                }

            }else{

                if(successFunc){
                    successFunc(bookmarks, title);
                }
            }



            $(this).closest("#defaultModal").modal("hide");

        })).append($("<br>"));

        let modal = ModalFactory("simple", header, body);


        $(modal).on("keydown",function(e){



            if(e.keyCode == 13){


                $($(e.target).parent().find("button[name='elementSubmitBtn']")).trigger("click");

            }


        });



        return modal;

    }







    </script>                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                   
