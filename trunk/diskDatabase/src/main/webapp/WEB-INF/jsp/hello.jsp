<%@ include file="/WEB-INF/jsp/include.jsp" %>

<html>
<head><title><fmt:message key="main.title"/></title></head>
<body>
<h1><fmt:message key="main.heading"/></h1>
<p><fmt:message key="main.greeting"/> 
</p>

<c:url var="url" value="/wallet" >
  <c:param name="Mode" value="New" />
</c:url>
|
<c:url var="url" value="/wallet" >
  <c:param name="Mode" value="Edit" />
</c:url>
|
<c:url var="url" value="/wallet" >
  <c:param name="Mode" value="Delete" />
</c:url>
|
<c:url var="url" value="/wallet" >
  <c:param name="Mode" value="Disks" />
</c:url>


<c:url var="urla" value="/wallet" >
  <c:param name="Mode" value="New" />
</c:url>
<p><strong><a href="${urla}">

<c:url value="mypage.jsp"><c:param name="query" value="${param.query}"/>
</c:url>

|
<c:url var="url" value="/wallet" >
  <c:param name="Mode" value="Edit" />
</c:url>
<p><strong><a href="${url}">
|
<c:url var="url" value="/wallet" >
  <c:param name="Mode" value="Delete" />
</c:url>
<p><strong><a href="${url}">
|
<c:url var="url" value="/wallet" >
  <c:param name="Mode" value="Disks" />
</c:url>
<p><strong><a href="${url}">

</body>
</html>
