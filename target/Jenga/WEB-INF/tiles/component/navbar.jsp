<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<nav id="navbar" class="navbar navbar-expand-lg bg-white fixed-top nav-down navbar-transparent">
    <div class="container">
      <div class="navbar-translate">
        <a class="navbar-brand" href="/" rel="tooltip" title="Jenga" data-placement="bottom" target="_blank">
          Jenga
        </a>
        <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navigation" aria-controls="navigation-index" aria-expanded="false" aria-label="Toggle navigation">
          <span class="navbar-toggler-bar bar1"></span>
          <span class="navbar-toggler-bar bar2"></span>
          <span class="navbar-toggler-bar bar3"></span>
          
        </button>
      </div>
      <div class="collapse navbar-collapse" data-color="light" >
        <ul class="navbar-nav ml-auto">   
        <li class="nav-item" >
	        
	        <div class="nav-link">
		       	 <div class="profile-photo-small" style="width:40px; height:40px; "> 
		        	<img src="http://www.clker.com/cliparts/d/L/P/X/z/i/no-image-icon-hi.png" style="" alt="User Profile" class="img-circle img-responsive img-no-padding text-center">
		        </div>
	       	 </div>
        </li>
        
                
          <li class="dropdown nav-item">
            <a href="#" class="dropdown-toggle nav-link" data-toggle="dropdown" aria-expanded="false">Block</a>
            <div class="dropdown-menu dropdown-menu-right dropdown-warning">
              <a href="#" class="dropdown-item"><i class="nc-icon nc-zoom-split"></i>블록 찾기</a>
              <a href="#" class="dropdown-item"><i class="nc-icon nc-bulb-63"></i>인기 블록</a>
              <a href="#" class="dropdown-item"><i class="nc-icon nc-app"></i>블록 쌓기</a>
              <a href="#" class="dropdown-item"><i class="nc-icon nc-diamond"></i>내가 찜한 블록</a>
              <a href="#" class="dropdown-item"><i class="nc-icon nc-bag-16"></i>내 블록 관리</a>          
            </div>
          </li>
          <li class="dropdown nav-item">
            <a href="#" class="dropdown-toggle nav-link" id="navbarDropdownMenuLink" data-toggle="dropdown">
             My Info
            </a>
            <div class="dropdown-menu dropdown-menu-right dropdown-warning" aria-labelledby="navbarDropdownMenuLink">
              <a class="dropdown-item" data-scroll="true" data-id="#headers" href="#">
                <i class="nc-icon nc-paper"></i> 내 정보 관리
              </a>
              <a class="dropdown-item" data-scroll="true" data-id="#features" href="#">
                <i class="nc-icon nc-alert-circle-i"></i> 공지사항
              </a>

            </div>
          </li>
         
          <li class="nav-item">
            <a class="nav-link">LOGOUT</a>
          </li>

        </ul>
      </div>
    </div>
  </nav>
  <script>
  
   var navBar = $("#navbar");
   var setNavType = function (type) {
	  
	  navBar.removeClass();
	  
	  if(type === "transparent"){
	  
		  navBar.addClass("navbar navbar-expand-lg bg-white fixed-top nav-down navbar-transparent");
	 
	  }else if(type === "blue"){
		  
	 	  navBar.addClass("navbar navbar-expand-lg sticky-top nav-down bg-info");

	  }
	  
  }
 
 </script>