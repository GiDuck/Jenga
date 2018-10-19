
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<!DOCTYPE html>
<html lang="ko">
<head>


<meta charset="UTF-8"/>
<meta content='width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0, shrink-to-fit=no' name='viewport' />
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />

<title>Basic Layout</title>

<tiles:insertAttribute name="cssFiles"/>
</head>
<body>
<tiles:insertAttribute name="navbar"/>
<tiles:insertAttribute name="header"/>
<tiles:insertAttribute name="content"/>
<tiles:insertAttribute name="footer"/>
</body>

<tiles:insertAttribute name="jsFiles"/>



</html>