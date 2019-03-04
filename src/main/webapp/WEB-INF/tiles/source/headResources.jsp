
  <!-- CSS Files -->
  <link href="https://fonts.googleapis.com/css?family=Montserrat:400,700,200" rel="stylesheet" />
  <link href="https://maxcdn.bootstrapcdn.com/font-awesome/latest/css/font-awesome.min.css" rel="stylesheet">

  <script src="${pageContext.request.contextPath}/resources/assets/js/core/jquery.min.js" type="text/javascript"></script>
  <script src="https://code.jquery.com/ui/1.12.1/jquery-ui.min.js" integrity="sha256-VazP97ZCwtekAsvgPBSUwPFKdrwD3unUfSGVYrahUqU=" crossorigin="anonymous"></script>


  <link href="${pageContext.request.contextPath}/resources/assets/css/bootstrap.min.css" rel="stylesheet" />
  <link href="${pageContext.request.contextPath}/resources/assets/demo/demo.css" rel="stylesheet">

  <script src="${pageContext.request.contextPath}/resources/assets/js/core/popper.min.js" type="text/javascript"></script>
  <script src="${pageContext.request.contextPath}/resources/assets/js/core/bootstrap.min.js" type="text/javascript"></script>

  <link href="${pageContext.request.contextPath}/resources/assets/css/paper-kit.css?v=2.2.1" rel="stylesheet" />
  <link href="${pageContext.request.contextPath}/resources/Loading-Mask-Plugin-jQuery-Busy-Load/dist/app.css" rel="stylesheet">
  <script src="${pageContext.request.contextPath}/resources/Loading-Mask-Plugin-jQuery-Busy-Load/dist/app.min.js"></script>
  <script src="${pageContext.request.contextPath}/resources/js/common.js"></script>
  <script src="https://cdn.jsdelivr.net/npm/sweetalert2@7.28.11/dist/sweetalert2.all.min.js"></script>

  <link href="${pageContext.request.contextPath}/resources/twitterHeart/style.css" rel="stylesheet">
  <script src="${pageContext.request.contextPath}/resources/twitterHeart/heart.js"></script>
  <script src="${pageContext.request.contextPath}/resources/js/common.js"></script>
  <script src="${pageContext.request.contextPath}/resources/js/regexManager.js"></script>
  <script src="${pageContext.request.contextPath}/resources/js/htmlParser.js"></script>

  <script>
      exceptionHandler(function(){
          window.authStatusCode = parseStatusArrToObj(JSON.parse('${authStatusCode}'));
      });
      exceptionHandler(function(){
          window.fileStatusCode = parseStatusArrToObj(JSON.parse('${fileStatusCode}'));
      });
      exceptionHandler(function(){
          window.blockStatusCode = parseStatusArrToObj(JSON.parse('${blockStatusCode}'));
      });
  </script>