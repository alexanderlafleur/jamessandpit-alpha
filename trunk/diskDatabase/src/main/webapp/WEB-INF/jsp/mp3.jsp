<%@ include file="/WEB-INF/jsp/include.jsp" %>

<html>
<head><title><fmt:message key="mp3.title"/></title>

</head>
<body>
<h1> </h1>
</p>

<p>

<c:forEach items="${form.results}" var="result">
<ul>
 <li> name <c:out value="${result.name}"/> </li>
</ul>
</c:forEach>

</body>
</html>