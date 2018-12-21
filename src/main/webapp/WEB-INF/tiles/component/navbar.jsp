<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<style>

.loginService{}

</style>
    
<nav id="navbar" class="navbar navbar-expand-lg bg-white fixed-top nav-down navbar-transparent">
    <div class="container">
      <div class="navbar-translate">
        <a class="navbar-brand" href="/" rel="tooltip" title="Jenga" data-placement="bottom">
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
		        	<img id="nav_user_profile" src="" style="" alt="User Profile" class="img-circle img-responsive img-no-padding text-center" onerror="this.src='http://www.clker.com/cliparts/d/L/P/X/z/i/no-image-icon-hi.png'">
		        </div>
	       	 </div>
        </li>
        
                
          <li class="dropdown nav-item">
            <a href="#" class="dropdown-toggle nav-link" data-toggle="dropdown" aria-expanded="false">Block</a>
            <div class="dropdown-menu dropdown-menu-right dropdown-warning">
              <a class="dropdown-item"  href="/board/search"><i class="nc-icon nc-zoom-split"></i>블록 찾기</a>
              <a class="dropdown-item"  href="#" ><i class="nc-icon nc-bulb-63"></i>인기 블록</a>
              <a class="dropdown-item loginService" href="/board/stackBlock?status=stack"><i class="nc-icon nc-app"></i>블록 쌓기</a>
              <a class="dropdown-item loginService" href="#"><i class="nc-icon nc-diamond"></i>내가 찜한 블록</a>
              <a class="dropdown-item loginService" href="#"><i class="nc-icon nc-bag-16"></i>내 블록 관리</a>
            </div>
          </li>
          <li class="dropdown nav-item">
            <a href="#" class="dropdown-toggle nav-link" id="navbarDropdownMenuLink" data-toggle="dropdown">
             My Info
            </a>
            <div class="dropdown-menu dropdown-menu-right dropdown-warning" aria-labelledby="navbarDropdownMenuLink">
              <a class="dropdown-item loginService" data-scroll="true" data-id="#headers" href="/modMemInfo">
                <i class="nc-icon nc-paper loginService"></i> 내 정보 관리
              </a>
              <a class="dropdown-item" data-scroll="true" data-id="#features" href="#">
                <i class="nc-icon nc-alert-circle-i"></i> 공지사항
              </a>

            </div>
          </li>
         
          <li class="nav-item">
         
         <!-- 세션 존재여부에 따라 로그인, 로그아웃 여부가 다르게 보임 -->
         <c:choose>
            <c:when test="${sessionScope.Member ne null}">
            <a class="nav-link" name="loginBtn" value="1">LOGOUT</a>
            </c:when>
            
            <c:otherwise>
             <a class="nav-link" name="loginBtn" value="0">LOGIN</a>
            
            </c:otherwise>
          </c:choose>
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
   
   
  $(document).ready(function(){

      $(window).on('unload', function(){

          $.busyLoadFull("hide", {});

      });

      $(document).find("a").css('cursor', 'pointer');


      $("#nav_user_profile").attr("src", "${sessionScope.Member.mem_profile}");


      $(".loginService").on('click', function(e){
          e.preventDefault();

	      //세션체크
          let session = "${sessionScope.Member}";
          let dest = $(e.target).attr("href");

          if(!session){

		  swal({
              text : "로그인이 필요한 서비스 입니다. 로그인 페이지로 이동하시겠습니까?",
              type : "warning",
              showCancelButton : true,
              confirmButtonText: "이동"
          }).then(function(result){

              if(result.dismiss == 'cancel'){
                  return;
              }
              else{
                  window.location.href="/login";

              }
          });
		 

          }else{

              $.busyLoadFull("show", {

                  fontawesome: "fa fa-cog fa-spin fa-3x fa-fw",
                  text : "페이지를 불러오고 있습니다..."

              });

              window.location.href= dest;
              <%--window.location.href= "${map.dest}";--%>

          }
		  
	  });


	  $("a[name='loginBtn']").on('click', function(e){

	      let validLogin = $(e.target).attr("value");

          if(validLogin == 0){

              window.location.href = "/login";


          }else if(validLogin == 1){

              $.ajax({

                  url : "/logout",
                  type : "GET",
                  data : null,
                  success : function(response){
                      window.location.href = "/";

                  },
                  error : function(xhs, status, error){

                      swal({

                          text : "로그아웃에 실패하였습니다.",
                          type : "error"

                      });

                      console.log("로그아웃 실패.. " + status);

                      window.location.href = "/";

                  }

              });

          }



      });


	  
  });
 
 </script>