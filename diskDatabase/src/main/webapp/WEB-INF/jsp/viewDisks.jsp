<%@ include file="/WEB-INF/jsp/include.jsp" %>

<html>
<head><title><fmt:message key="title"/></title></head>
<body>
<h1><fmt:message key="heading"/></h1>
</p>

ViewDisks

<c:forEach items="${disk}" var="disk">
 <c:out value="${disk.label}" />  <c:out value="${disk.id}" /> 
 <br>
</c:forEach>

</body>
</html>

