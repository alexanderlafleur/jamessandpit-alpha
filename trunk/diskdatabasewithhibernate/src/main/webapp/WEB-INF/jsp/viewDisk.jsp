<%@ include file="/WEB-INF/jsp/include.jsp" %>

<html>
<head><title><fmt:message key="viewDisk.title"/></title>

</head>
<body>
<h1><fmt:message key="viewDisk.heading"/></h1>
</p>

viewdisk

	<fmt:message key="field.id"/>
	<c:out value="${disk.id}" />
	<br>
	
	<fmt:message key="field.description"/>
	<c:out value="${disk.label}" />
	<p>
	
	<fmt:message key="viewDisk.dirs.heading"/>
	<br>

	<c:forEach items="${disk.root.dirs}" var="dir">
		<a href="dir.htm?mode=view&dirId=<c:out value="${dir.id}"/>">
			<c:out value="${dir.name}" />
		</a>
		<br>
	</c:forEach>

	<fmt:message key="viewDisk.mp3s.heading"/>
	<br>

	<c:forEach items="${disk.root.mp3s}" var="mp3">
		<a href="mp3.htm?mode=view&mp3Id=<c:out value="${mp3.id}"/>">
			<c:out value="${mp3.title}" />
		</a>
		<br>
	</c:forEach>

	<fmt:message key="viewDisk.files.heading"/>
	<br>
	
	<c:forEach items="${disk.root.files}" var="file">
		<a href="file.htm?mode=view&fileId=<c:out value="${file.id}"/>">
			<c:out value="${file.name}" />
		</a>
		<br>
	</c:forEach>

</body>
</html>