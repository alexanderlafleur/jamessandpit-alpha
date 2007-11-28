<%@ include file="/WEB-INF/jsp/include.jsp" %>

<html>
<head><title><fmt:message key="dir.title"/></title>

</head>
<body>
<h1><c:out value="${dir.name}"/></h1>
</p>

<c:if test="${dir.parent != 0}">
	<a href="dir.htm?mode=view&dirId=<c:out value="${dir.parent}"/>">[...]</a>
</c:if>
<p>

<table>
	<c:forEach items="${dir.dirs}" var="dir">
	<tr>
		<td>+
			<a href="dir.htm?dirId=<c:out value="${dir.id}"/>">
				<c:out value="${dir.name}"/>
			</a>
		</td>
	</tr>
	</c:forEach>
</table>

<table>
	<c:forEach items="${dir.files}" var="file">
	<tr>
		<td>-
			<a href="file.htm?fileId=<c:out value="${file.id}"/>">
				<c:out value="${file.name}"/>
			</a>
		</td>
	</tr>
	</c:forEach>
</table>

<table>
	<c:forEach items="${dir.mp3s}" var="mp3">
	<tr>
		<td>-
			<a href="mp3.htm?mp3Id=<c:out value="${mp3.id}"/>">
				<c:out value="${mp3.name}"/>
			</a>
		</td>
	</tr>
	</c:forEach>
</table>

</body>
</html>