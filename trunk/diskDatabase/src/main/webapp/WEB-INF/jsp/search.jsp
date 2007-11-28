<%@ include file="/WEB-INF/jsp/include.jsp" %>

<html>
<head><title><fmt:message key="title"/></title></head>
<body>
<h1><fmt:message key="heading"/></h1>
</p>

Search

<form id="form" method="post" action="search.htm">
	
	<spring:bind path="form.formAction">
		<input type="hidden" size="10" name="${status.expression}" value="search"/>
	</spring:bind>

	<spring:bind path="form.nameCriteria">
		<input type="text" size="128" name="${status.expression}" value="${status.value}"/>
	</spring:bind>

	<c:forEach items="${form.results}" var="result">
		<c:out value="${result.name}" /> | 
		<c:out value="${result.size}" /> |
		<c:out value="${result.id}" /> <br>
	</c:forEach>

	<button type="submit" name="submit">Submit</button>
    
</form>
     
</body>
</html>

