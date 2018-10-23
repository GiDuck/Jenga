<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<script>

	var callAjax = function(_type, _url, params, onSuccess, onFail){
		
		
		if(!onSuccess){
			
			onSuccess = (response) => {
			
			console.log('response.. ' + response);	
				
			}
			
			
		}else if(!onFail){
			
			
			onFail =  function(request, status, error) {
				
				console.log("error code.. " + request.status + " message : " + request.responseText + "error : " + error );
				
			}
			
			
		}
		
		
		
		$.ajax({
			
			type : _type,
			url : _url,
			data : params,
			success : onSuccess,
			error : onFail
			
			
		});
		
		
	}



</script>