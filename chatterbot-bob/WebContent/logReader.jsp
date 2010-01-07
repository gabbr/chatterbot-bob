<%@ page language="java" contentType="text/html; charset=US-ASCII"
	pageEncoding="US-ASCII"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=US-ASCII">
<meta http-equiv="refresh" content="3">
<title>Insert title here</title>
</head>
<body onload="logReader.scrollTop = logReader.scrollHeight;">
<jsp:useBean id="logReader" scope="session" class="it.unibz.lib.bob.chatterbot.LogReader">
</jsp:useBean>
Debug-Ausgabe:
<div id="logReader"
	style="height: 500px; width: 1050px; overflow: auto; background-color: #FFFFFF; border: 1px solid #555555;">
<jsp:getProperty name="logReader" property="tail" /></div>
<script type="text/javascript">var logReader = document.getElementById("logReader"); logReader.scrollTop = logReader.scrollHeight;</script>
</body>
</html>
