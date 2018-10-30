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
		  if(temp !== 1){
			  
			  //html값을 가져와서 배열에 넣어준다.
			  selCards.push($(item).find("h3").html());
			  
		  }
	  });
	  
	  //배열을 리턴
	  return selCards;
	  	  
	  
  }