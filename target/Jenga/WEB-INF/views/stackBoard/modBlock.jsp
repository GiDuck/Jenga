<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<style>



.bookMarkField{

overflow: scroll;
border-style : groove;
min-height:100px;
max-height:400px;
height : auto;

}


.bookMarkItem{
}


.bookMarkItem:hover{

background-color : green;

}

.myBookMarkItem{

}


.myBookMarkItem:hover{

background-color : pink;

}

.bookMarkLabel{

padding : 25px;

}

.selectedElementLeft{

background-color : red;

}


.selectedElementRight{
background-color : blue;


}

</style>

<div class="main">
    <div class="section">
    
    <div class="container">
    
         <h2><b>Stack Block</b></h2>
    
    
    	<div class="row">
    	
    		<div class="col-md-4 col-sm-9 mr-auto ml-auto">
    		 <div class="w-100 text-center bookMarkLabel"><h4><b>내 북마크</b></h4></div>
    		
    		<div id="getMyBookMark" class="bookMarkField" class="row">
    		    		
    		
    		 	
    		</div>
    		
    		<div class="row text-center" style="padding : 10px">
				<div class = "col-sm-6"><div id="moveToUpperLeft" class="btn w-100 text-center"> < </div></div>
				<div class = "col-sm-6"><div id="moveToLowerLeft" class="btn w-100 text-center"> > </div></div>
				
			
			</div>
    		
    		
    		</div>
    		
    		
    		<div class="col-md-7 col-sm-9 mr-auto ml-auto">
    	
    	    <div class="w-100 text-center bookMarkLabel"><h4><b>북마크 편집</b></h4></div>
    		
    		<div id="editBookMark" class= "editBookMarkField bookMarkField" class="row">

				</div>
				
			<div class="row text-center" style="padding : 10px">
				<div class = "col-md-1 col-sm-6"><div id="moveToUpperRight" class="btn w-100 text-center"> < </div></div>
				<div class = "col-md-1 col-sm-6"><div id="moveToLowerRight" class="btn w-100 text-center"> > </div></div>
				<div class = "col-md-4 col-sm-4"><div id="addElementRight" class="btn w-100 text-center"> 북마크 추가 </div></div>
				<div class = "col-md-3 col-sm-4"><div id="addFolderRight" class="btn w-100 text-center"> 폴더 추가 </div></div>
				<div class = "col-md-3 col-sm-4"><div id="removeElementRight" class="btn w-100 text-center"> 선택 삭제 </div></div>
			
			</div>
				
				
    		</div>
    	
    	</div>
    
    
    </div>
    
    <br><br><br><br>
    
    
      <div class="container">
        <form>
          <div class="row">
            <div class="col-md-5 col-sm-5">
              <h6>Main Image</h6>
              <div class="fileinput fileinput-new text-center" data-provides="fileinput">
                <div class="fileinput-new thumbnail img-no-padding" style="max-width: 370px; max-height: 250px;">
                  <img src="${pageContext.request.contextPath}/resources/assets/img/image_placeholder.jpg" alt="...">
                </div>
                <div class="fileinput-preview fileinput-exists thumbnail img-no-padding" style="max-width: 370px; max-height: 250px;"></div>
                <div>
                  <span class="btn btn-outline-default btn-round btn-file">
                    <span class="fileinput-new">Select image</span>
                  <span class="fileinput-exists">Change</span>
                  <input type="file" name="...">
                  </span>
                  <a href="#paper-kit" class="btn btn-link btn-danger fileinput-exists" data-dismiss="fileinput"><i class="fa fa-times"></i> Remove</a>
                </div>
              </div>
              <h6>Tags</h6>
              <div id="tags">
                <c:forEach items="map">
                <input class="tagsinput" data-color="success" type="text" value="${map.get("tag"+i)}" data-role="tagsinput" />
                </c:forEach>
                <!-- <div class="tagsinput-add"></div> -->
                <!-- You can change "tag-primary" with with "tag-info", "tag-success", "tag-warning","tag-danger" -->
              </div>
              <h6>Categories</h6>
              <div id="tags-2">
                <input class="tagsinput" data-color="success" type="text" value="Food, Drink" data-role="tagsinput" />
                <!-- <div class="tagsinput-add"></div> -->
                <!-- You can change "tag-primary" with with "tag-info", "tag-success", "tag-warning","tag-danger" -->
              </div>
              <h6>Format
                <span class="icon-danger">*</span>
              </h6>
              <div class="form-check-radio">
                <label class="form-check-label">
                  <input class="form-check-input" type="radio" name="exampleRadios" id="exampleRadios1" value="option1"> Digital
                  <span class="form-check-sign"></span>
                </label>
              </div>
              <div class="form-check-radio">
                <label class="form-check-label">
                  <input class="form-check-input" type="radio" name="exampleRadios" id="exampleRadios2" value="option2" checked> Print
                  <span class="form-check-sign"></span>
                </label>
              </div>
            </div>
            <div class="col-md-7 col-sm-7">
              <div class="form-group">
                <h6>Title
                  <span class="icon-danger">*</span>
                </h6>
                <input type="text" class="form-control border-input" placeholder="enter the product name here...">
              </div>
              <div class="form-group">
                <h6>Tagline
                  <span class="icon-danger">*</span>
                </h6>
                <input type="text" class="form-control border-input" placeholder="enter the product tagline here...">
              </div>
              <div class="row price-row">
                <div class="col-md-6">
                  <h6>Date
                    <span class="icon-danger">*</span>
                  </h6>
                  <div class="input-group border-input">
                    <input type="text" value="" placeholder="enter price" class="form-control border-input">
                    <div class="input-group-append">
                      <span class="input-group-text"><i class="fa fa-euro"></i></span>
                    </div>
                  </div>
                </div>
                <div class="col-md-6">
                  <h6>Discount</h6>
                  <div class="input-group border-input">
                    <input type="text" value="" placeholder="enter discount" class="form-control border-input">
                    <div class="input-group-append">
                      <span class="input-group-text">%</span>
                    </div>
                  </div>
                </div>
              </div>
              <div class="form-group">
                <h6>Description</h6>
                <section id="editor">
                <div id="summernote">
                </div>
                </section>
              </div>
              <div class="form-check">
                <label class="form-check-label">
                  <input class="form-check-input" type="checkbox" value=""> Display on landing page
                  <span class="form-check-sign"></span>
                </label>
              </div>
            </div>
          </div>
          <div class="row buttons-row">
            <div class="col-md-4 col-sm-4">
              <button class="btn btn-outline-danger btn-block btn-round">Cancel</button>
            </div>
            <div class="col-md-4 col-sm-4">
              <button type="submit" formmethod="post" class="btn btn-outline-primary btn-block btn-round">Save</button>
<%--
              <button class="btn btn-outline-primary btn-block btn-round">Save</button>
--%>
            </div>
            <div class="col-md-4 col-sm-4">
              <button class="btn btn-primary btn-block btn-round">Save & Publish </button>
            </div>
          </div>
        </form>
      </div>
    </div>
  </div>
  

  <script>

	  /* left : 왼쪽 필드에서 드래그 가능한 이벤트 부착
	  	 right : 오른쪽 필드에서 드래그 가능한 이벤트 부착
	  */
	  
	  var bookmarks = ${resultJSON};
	  var bookmarkElements = bookmarks.roots.bookmark_bar.children;
	  
	  
	  function removeOtherClass(type){
		  
		  if(type === "right"){
			  
			  $("#editBookMark").find(".selectedElementLeft").removeClass("selectedElementLeft");
			  
		  }else if(type === "left"){
			  
			  
			  $("#getMyBookMark").find(".selectedElementRight").removeClass("selectedElementRight");

			  
		  }
		  
		  
		  
	  }
	  
	  
	  function initBookmarks(){
	  
	  
	  	 }
	  
	  function setDraggable($element, token) {
		  
		  if(token === 'left' ){
		  
			  $element.draggable({
				 
			    cursor : "move",
		        opacity : 0.8,
		        revert : "invaild",
		        helper : "clone"
			  
		  
			  });
			  
		  }else if(token === "right" ){
			  
			  $element.draggable({
			        classes : {"ui-draggable" : "highlight"},
				  	cursor : "move",
			        opacity : 0.8,
			        revert : true,
			        helper : "clone",
			        zIndex : 300
			        });
		  
		  
		  }
		  
	  }

	  //clickListener 
	  var setOnClickListener = function($element, token) {
			 
		  	let $selected = undefined;
			
		  	$element.on('click', function(e) {
			e.preventDefault();
			e.stopPropagation();
			
			console.log($element, token);
			
			 if(token === "right"){
				 
			 removeOtherClass("right");
			 $selected = $("#editBookMark").find(".selectedElementRight");			
			 $selected.removeClass("selectedElementRight");
			 $selected.removeAttr("background-color", "");
			 console.log("right");
			 
	 			
			$element.addClass("selectedElementRight");
	
			 
			 }else if(token === "left"){
			
			removeOtherClass("left");
			 $selected = $("#getMyBookMark").find(".selectedElementLeft");
			 $selected.removeClass("selectedElementLeft");
			 $selected.removeAttr("background-color", "");
			
			

			$element.addClass("selectedElementLeft");

						

				 
			}

	  
		   });
  
	  }
  
  
	  function setDroppable($element, token){
		  
		  if(token === "right"){
	
			  $element.droppable({
	  	
		        tolerance : "intersect",
		        activeClass: "ui-state-default",
		        hoverClass: "ui-state-hover",
		        drop : function(e, ui){
	
	  	        let dragNode = $(ui.draggable);

	  	        		//즉, 오른쪽 노드가 오른쪽 필드에 드래그 앤 드랍 되었을 시.
		        		if(dragNode.hasClass("bookMarkItem"))
		        		{

		        	       dragNode.insertAfter($(this));
	
		        		//왼쪽 필드의 노드가 오른쪽 필드로 드래그 앤 드랍 되었을 시.
		        		}else if(dragNode.hasClass("myBookMarkItem")){
		        			
		        			dragNode = dragNode.clone();
		        			dragNode.css("padding-left", "20px");
		        			$(dragNode).removeClass("myBookMarkItem");
		        			$(dragNode).addClass($(this).attr('class'));
		        			$(dragNode).unbind('click');
		        			setOnClickListener($(dragNode), "right");		        			
		        			setDraggable($(dragNode), "right");
		        			setDroppable($(dragNode), "right");
		        			$(dragNode).insertBefore($(this));
		        			removeOtherClass("right");
		
		        			
		        		}
	
		        }
	
			  
			  });
		  
		  }
		  
	  }
	  
  
	  //요소 추가 함수
	  //요소 혹은 폴더를 구분하여 추가한다.
	  //추가 연산은 오른쪽 필드에서만 가능하므로 editBookMark 필드에서만 진행한다.
	  function add(panelType, type){

		let $parent = $("#editBookMark");
		let $newElement;
			  
		//현재 선택된 노드 가져오기
		let $selElement = $($parent).find(".selectedElementRight");
			
		
		if(type === "folder"){
		  		  
		  $newElement = $("<div>").addClass("col-12 w-100 bookMarkItem").css("padding-left", "20px").attr("name", "elementParent")
			  			 .append($("<div>").addClass("w-100").append($("<i>").addClass("nc-icon nc-bag-16").css("padding-left", "5px"))
			  			 .append($("<input>").addClass("w-100").attr("type", "text").attr("placeHolder", "폴더 이름 입력..").css("border", 0)))
			  			 .append($("<br>"));
		  
		  
		}else if(type === "element"){
			
		  $newElement = ($("<div>").addClass("col-12 w-100 bookMarkItem").attr("name", "elementParent")
				  .append($("<hr>"))	  
				  .append($("<div>").addClass("w-100").append($("<i>").addClass("nc-icon nc-book-bookmark text-left").html("Bookmark")))
				  .append($("<div>").addClass("w-100").attr("name", "section")
				  .append($("<input>").attr("type", "text").attr("placeHolder", "제목 입력..").css("border", 0).css("padding-bottom", "10px").addClass("w-100"))
				  .append($("<input>").attr("type", "text").attr("placeHolder", "url 입력..").css("border", 0).addClass("w-100 bookMarkItem").val("https://"))
				  .append($("<hr>")))).append($("<br>")).clone();
		
		  
		}
		
		  setDraggable($newElement, "right");
		  setDroppable($newElement, "right");	
		  setOnClickListener($newElement, "right");		  
		  
		  if(!$selElement){
				
			  $parent.append($newElement);

		  }else{			  
			  
			  $newElement.insertAfter($selElement);
		  
		  }

	  }

  
	  
	  //상위 이동 함수
	  function moveUpper($element, $parent){
		    
	  }
	  
	  //하위 이동 함수
	  function moveLower($element, $parent){
		  
		  
	  }  
  
  
	  //삭제 함수
	  function removeElement(panelType){
		  
		  
		let $selElement = findCheckElement(panelType);
		  
		  if(!$selElement){
			  
			  swal('','현재 선택된 요소가 없습니다. 삭제를 원하는 노드를 선택하십시오.', '');
			  return;
		  }
		  
		  $selElement.parent().find("div").remove();
	  
	  }  
  
	  
	  //선태된 요소를 찾는 함수
	  function findCheckElement(panelType){
		  
		  let $parent = undefined;
		  let $item = undefined;
		  
		  if(panelType === "left"){
			  
			  $parent = $("#getMyBookMark");
			  $item = $parent.find(".selectedElementLeft");

			  
		  }else if(panelType === "right"){
	
			  $parent = $("#editBookMark");
			  $item = $parent.find(".selectedElementRight");

			  
		  }else{
			  
			  swal('', '에러가 발생하였습니다. 관리자에게 문의하십시오.', '');
			  return false;
			  
		  }
		 
		  
		  if(!$item){
			  
			  return $parent;
			  
		  }else{
			  
			  return $item;
			  
		  }
	  
	
	  }
	  
  


  //버튼 클릭리스너 부착 함수
  function attchBtnEvent(){
	  
	//...왼쪽 패널
	
	//왼쪽패널, 상위폴더로 이동
	$("#moveToUpperLeft").on('click', function(e){
		
		swal('', '왼쪽 패널, 상위 요소로 이동', '');
		
	});  

	//왼쪽패널, 하위폴더로 이동
	$("#moveToLowerLeft").on('click', function(e){
		swal('', '왼쪽 패널, 하위 요소로 이동', '');

		
	});
	
	//왼쪽패널, 요소 추가 
	$("#addElementLeft").on('click', function(e){
		
		
		add("left", "element");
		
		
	});
	
	//왼쪽패널, 폴더 추가 
	$("#addFolderLeft").on('click', function(e){
		
		add("left", "folder");
		
	});
	
	
	//...오른쪽 패널
	
	//오른쪽패널, 상위폴더로 이동
	$("#moveToUpperRight").on('click', function(e){
		
		swal('', '오른쪽 패널, 상위 요소로 이동', '');

		
		
	});
	
	//오른쪽패널, 하위폴더로 이동
	$("#moveToLowerRight").on('click', function(e){
		
		swal('', '왼쪽 패널, 하위 요소로 이동', '');

		
	});
	
	//오른쪽패널, 요소 추가
	$("#addElementRight").on('click', function(e){
		
		add("right", "element");

		
	});
	
	//오른쪽패널, 폴더 추가
	$("#addFolderRight").on('click', function(e){
		
		
		add("right", "folder");

	});
	
	//오른쪽패널, 요소 제거
	$("#removeElementRight").on('click', function(e){
		
		removeElement("right");
		
	});
	
	  
	  
  }

  
  $(document).ready(function() {
	  
	  setNavType("blue");
	  $("#summernote").summernote({
		  
	        placeholder: '내용을 작성하세요..',
	        tabsize: 2,
	        height: 300
		  
	  });
	  
	  
	  attchBtnEvent();
	  let $itemParent = $("#getMyBookMark");
	  let $rParent = $("#editBookMark");


	  
	  var $bookmark;
	  
	  for(let i=0; i<bookmarkElements.length; ++i){

		  $bookmark = bookmarkElements[i];

		  let $item = $("<div>").addClass("row myBookMarkItem").attr("name", "elementParent").css("padding", "10px");
		  let $innerItem = $("<div>").addClass("col-10 w-100")
		  .append($("<input>").attr("name", $bookmark.id).val($bookmark.name).attr("disabled", true).css("border", 0).css("background-color", "#ffffff").css("user-select", "none").addClass("w-100"))
		  .append($("<input>").attr("name", $bookmark.id).val($bookmark.url).attr("disabled", true).css("border", 0).css("background-color", "#ffffff").css("user-select", "none").addClass("w-100"));

		   
		  //내 북마크 item
		  
		  if($bookmark.type === "folder"){
			  
			  $item.append($innerItem);
			  $item.append($("<div>").addClass("col-2").append($("<i>").addClass("nc-icon nc-minimal-right text-right w-100")));
			  $innerItem.append($("<hr>"));

		  }else{
			
			  $item.append($innerItem);
			  $innerItem.append($("<hr>"));			 
			  
		  }
  	  
		  $itemParent.append($item);
		  setOnClickListener($item , "left"); 



		  
	  }
	  
	  let $rightItem = $("<div>").addClass("col-12 bookMarkItem w-100").attr("name", "elementParent").html("parent").append($("<hr>"));
	  let $parentNode = $rightItem;
	  $rParent.append($parentNode);
	  
	  setOnClickListener($parentNode , "right"); 
	  setDraggable($(".myBookMarkItem"), "left");	
 	  setDraggable($(".bookMarkItem"), "right");
	  setDroppable($(".bookMarkItem"), "right");
	  

	  
	 
	  
  });
  
  </script>