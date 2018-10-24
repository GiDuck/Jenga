<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
 <jsp:include page="./modal.jsp"></jsp:include>    
    
    
    <style>
    
	video 
	{
	    width: 100%;
	    height: 100%;
	    max-height: 100%;
	    
	}
	
	.opt_fixed
	{
	position: fixed;
	padding : 8px;
	
	
	}
	
	.findSomeText
	{
	
	color : white;
	font-weight : bold;
	margin : 5px;
	}
    

    
    </style>
    

    <div style="width:auto; height:auto;" class="subscribe-line-transparent" >
    
    <video autoplay muted loop id="backgroundVideo" style="object-fit: fill;">
    
    	<source src="${pageContext.request.contextPath}/resources/assets/video/fireworks.mp4" type="video/mp4">
    	
    </video>
    
      <div class="container opt_fixed">
 
    
        <div class="row">
        
        <div class="col-12" style="height : 100px; width : auto;"></div>
        
          <div class="col-sm-8 mr-auto ml-auto">
            <div id="regForm" class="card card-register mr-auto ml-auto" style="background-color: rgba( 255, 255, 255, 0.3 );">
              <h3 class="card-title" style="color : #ffffff; display : inline; font-weight : bold;">Play with Us!</h3>
              <form class="register-form">
                <label>Email</label>
                <input type="email" class="form-control no-border" placeholder="Email" style="color : black;">
                <label>Password</label>
                <input type="password" class="form-control no-border" placeholder="Password" style="color : black;">
                <button class="btn btn-danger btn-block btn-round">Register</button>
              </form>
              
			<div class="col-12 text-center">
			<div id="fbBtn" class="btn btn-just-icon btn-facebook"><i class="fa fa-facebook" aria-hidden="true"></i></div>
			<div id="googleBtn" class="btn btn-just-icon btn-google"><i class="fa fa-google" aria-hidden="true"></i></div>
			<div id="kakaoBtn" class="btn btn-just-icon btn-twitter"><i class="fa fa-twitter" aria-hidden="true"></i></div>
			<div id="naverBtn" class="btn btn-just-icon btn-linkedin"><i class="fa fa-linkedin" aria-hidden="true"></i></div>
            </div>
            
              <div class="row text-center" style="margin-top:15px; padding : 12px;">
                <div id="findPWBtn" class="col-12" style="margin-bottom : 10px;" data-toggle="modal" data-target="#findPwModal"><a href="#"><span class="findSomeText">비밀번호를 잃어버렸나요?</span></a></div>
                <div id="recoverAuthBtn" class="col-12" data-toggle="modal" data-target="#recoverAuthModal"><a href="#"><span class="findSomeText">복구할 계정이 있나요?</span></a></div>             
              </div>

          </div>
          </div>
        </div>
        </div>
    </div>
    
    
<script>

$(document).ready( _ => {
	
	$("#fbBtn").on('click', _ => {
		location.href="${f}";
	});
	
	$("#googleBtn").on('click', _ => {

        location.href="${g}";
	});
	
	$("#kakaoBtn").on('click', _ => {

        location.href="${k}";
	});
	
	$("#naverBtn").on('click', _ => {

        location.href="${n}";
	});
});
</script>