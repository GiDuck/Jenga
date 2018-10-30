<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

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
background-color : red;
}

.myBookMarkItem{

}


.myBookMarkItem:hover{
background-color : pink;
}

.bookMarkLabel{

padding : 25px;

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
    		
    		
    			<div class="col-12 myBookMarkItem"><div>item1<hr></div></div>
    			<div class="col-12 myBookMarkItem"><div>item2<hr></div></div>
    			<div class="col-12 myBookMarkItem"><div>item3<hr></div></div>
    			<div class="col-12 myBookMarkItem"><div>item4<hr></div></div>
    			<div class="col-12 myBookMarkItem"><div>item5<hr></div></div>
    			<div class="col-12 myBookMarkItem"><div>item6<hr></div></div>
    			<div class="col-12 myBookMarkItem"><div>item7<hr></div></div>
    			<div class="col-12 myBookMarkItem"><div>item8<hr></div></div>
    			<div class="col-12 myBookMarkItem"><div>item9<hr></div></div>
    			<div class="col-12 myBookMarkItem"><div>item10<hr></div></div>
    			<div class="col-12 myBookMarkItem"><div>item11<hr></div></div>
    			<div class="col-12 myBookMarkItem"><div>item12<hr></div></div>

    		
    		 	
    		</div>
    		
    		<div class="row text-center" style="padding : 10px">
				<div class = "col-sm-3"><div class="btn w-100 text-center"> < </div></div>
				<div class = "col-sm-3"><div class="btn w-100 text-center"> > </div></div>
				<div class = "col-sm-5"><div class="btn w-100 text-center"> 요소 추가 </div></div>
			
			</div>
    		
    		
    		</div>
    		
    		
    		<div class="col-md-7 col-sm-9 mr-auto ml-auto">
    	
    	    <div class="w-100 text-center bookMarkLabel"><h4><b>북마크 편집</b></h4></div>
    		
    		<div id="editBookMark" class= "editBookMarkField bookMarkField" class="row">
    		    		    <div class="col-12 bookMarkItem"><div>parent1<hr></div></div>
    		    		    <div class="col-12 bookMarkItem"><div>parent2<hr></div></div>
    		    		    <div class="col-12 bookMarkItem"><div>parent3<hr></div></div>
    		    		    <div class="col-12 bookMarkItem"><div>parent4<hr></div></div>
    		    		    <div class="col-12 bookMarkItem"><div>parent5<hr></div></div>
    		    		    <div class="col-12 bookMarkItem"><div>parent6<hr></div></div>
    		    		    <div class="col-12 bookMarkItem"><div>parent7<hr></div></div>
    		    		    <div class="col-12 bookMarkItem"><div>parent8<hr></div></div>
    		    		    <div class="col-12 bookMarkItem"><div>parent9<hr></div></div>
    		    		    <div class="col-12 bookMarkItem"><div>parent10<hr></div></div>
    		    		    <div class="col-12 bookMarkItem"><div>parent11<hr></div></div>
    		    		    <div class="col-12 bookMarkItem"><div>parent12<hr></div></div>
				</div>
				
			<div class="row text-center" style="padding : 10px">
				<div class = "col-md-1 col-sm-6"><div class="btn w-100 text-center"> < </div></div>
				<div class = "col-md-1 col-sm-6"><div class="btn w-100 text-center"> > </div></div>
				<div class = "col-md-4 col-sm-4"><div class="btn w-100 text-center"> 북마크 추가 </div></div>
				<div class = "col-md-3 col-sm-4"><div class="btn w-100 text-center"> 폴더 추가 </div></div>
				<div class = "col-md-3 col-sm-4"><div class="btn w-100 text-center"> 선택 삭제 </div></div>
			
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
                <input class="tagsinput" data-color="success" type="text" value="Minimal, Light, New, Friends" data-role="tagsinput" />
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
              <button class="btn btn-outline-primary btn-block btn-round">Save</button>
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
			  	cursor : "clone",
		        opacity : 0.8,
		        revert : true,
		        helper : "clone",
		        zIndex : 200
		        });
	  
	  
	  }
	  
  }
  
  var setOnClickListener = ($element, token) => {
	  
	  let leftClicked = false;
	  let rightClicked = false;
	  
	  if(token === "left"){
		  
		  $element.on('click', (e) => {
			  
			  e.preventDefault();
			  $element.css("background-color", "green");
			  let children = $("#editBookMark").children();
			  children.each(function(index, item){

				  
			  });
			  
		  });
		  
		  
	  }
	  
	  
  }
  
  
  function setDroppable($element, token){
	  
	  if(token === "right"){

		  $element.droppable({
  	
	        tolerance : "intersect",
	        activeClass: "ui-state-default",
	        hoverClass: "ui-state-hover",
	        drop : function(e, ui){

  	        let dragNode = $(ui.draggable);

	        		if(dragNode.hasClass("bookMarkItem"))
	        		{
	        	      
	        	       dragNode.insertBefore($(this));

	        		
	        		}else{
	        			dragNode = dragNode.clone();
	        			dragNode.css("padding-left", "20px");
	        			dragNode.css("background-color", "yellow");

	        			dragNode.insertBefore($(this));
	        			dragNode.removeClass("myBookMarkItem");
	        			dragNode.addClass($(this).attr('class'));
	        			setDraggable(dragNode, "right");
	        			setDroppable(dragNode, "right");


	        			
	        		}

	        }

		  
		  });
	  
	  }
	  
  }
  
  
  function setContextMenu($element){
	  
	  
	  
	  
	  
  }
  
  $(document).ready( _ => {
	  
	  setNavType("blue");
	  $("#summernote").summernote({
		  
	        placeholder: '내용을 작성하세요..',
	        tabsize: 2,
	        height: 300
		  
	  });
	  setOnClickListener($(".myBookMarkItem"), "left");
	  setDraggable($(".myBookMarkItem"), "left");
	  
	  setDraggable($(".bookMarkItem"), "right");
	  setDroppable($(".bookMarkItem"), "right");
	  

	  
	 
	  
  });
  
  </script>