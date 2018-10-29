
<div class="container" style="width:auto; height:900px;">


<div id="moveBtn" class="btn btn-danger w-100">move to join page..</div>
<div id="moveBtn2" class="btn btn-info w-100">move to write page..</div>


</div>


<script>

$(document).ready( _ => {

	setNavType("blue");

	
	
	$("#moveBtn").on('click', _ => {
		
		location.replace("/join#regForm");
		
	});
	
	$("#moveBtn2").on('click', _ => {
		
		location.replace("/board/stackBlock");

		
	});
	
	
	
});



</script>