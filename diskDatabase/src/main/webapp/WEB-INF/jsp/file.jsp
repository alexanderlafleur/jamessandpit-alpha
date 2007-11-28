<%@ include file="/WEB-INF/jsp/include.jsp" %>

<html>
<head><title><fmt:message key="file.title"/></title>

</head>
<body>
<h1><c:out value="${file.name}"/> </h1>
</p>

<c:if test="${file.parent != 0}">
	<a href="dir.htm?mode=view&dirId=<c:out value="${file.parent}"/>">[...]</a>
</c:if>

<c:out value="${file.name}"/> - <c:out value="${file.size}"/> bytes
</ul>

</body>
</html>