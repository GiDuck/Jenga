/**
 * 회원 정보에 관한 자바스크립트 함수들을 모아놓는 파일
 */




  //선택된 카드를 가져오는 함수
  function getSelectedCard() {


	  //카드 목록을 가져와서 
	  let $cards = $("#selectFavorField").find(".card");
	  let selCards = new Array();

	  $cards.each(function(index, item) {

		  let temp = $(item).css('opacity');

		  //만약 투명도가 1이 아니면 (카드 선택 시 투명도가 1 미만으로 설정되어있음)
		  if(temp < 1){
			  
			  //html값을 가져와서 배열에 넣어준다.
			  selCards.push($(item).find("h3").html());
			  
		  }
	  });
	  
	  //배열을 리턴
	  return selCards;
	  	  
	  
  }
  

  
  function validCheckAuth(id, pwd){
	  
	  
	  	let passport = true;
	  	let idIsVoid = (!id || id.trim() == null) ? true : false;
		let pwdIsVoid = (!pwd || pwd.trim() == null) ? true : false;
		let vaildPwd = REGEX_PASSWORD.test(pwd);
		let vaildId = REGEX_EMAIL.test(id);
		
	  
		if(!vaildPwd ||  !vaildId || idIsVoid || pwdIsVoid){
				
				let miss = undefined;
				
				console.log(idIsVoid + "..." + pwdIsVoid + "..." + vaildPwd + "..." + vaildId);
				
				if(idIsVoid || pwdIsVoid){
								 						
				miss = "이메일 혹은 비밀번호가 공백입니다.";
				passport = false;
					
				}
				else if(!vaildPwd){
					
					miss = "비밀번호는 영문자와 특수문자를 1개 이상 포함하여야 합니다.";
					passport = false;	
					
				}else if (!vaildId){
					
					miss = "이메일이 정확하지 않습니다. 확인 해 주세요.";
					passport = false;
					
				}
				
				if(!passport){
				
					makeSimpleNotifyModal('입력 정보가 정확하지 않습니다', miss ,  function(){});
				
				}
				
			}
			console.log(passport);
	  
	  return passport;
	  
	  
  }
  
  
 