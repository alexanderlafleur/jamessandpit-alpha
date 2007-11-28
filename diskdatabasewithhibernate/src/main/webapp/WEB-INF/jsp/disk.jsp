<%@ include file="/WEB-INF/jsp/include.jsp" %>

<html>
<head><title><fmt:message key="disk.title"/></title>

</head>
<body>
<h1><fmt:message key="disk.heading"/></h1>
</p>

<h3>disks</h3>

<a href="newdisk">New disk</a> |
<a href="editdisk">Edit disk</a> |
<a href="deletedisk">Delete disk</a> |
<a href="viewDisks">View Disks</a> <br>
<p>

<form>
	<select name="disk" onchange="form.submit()">
		<option></option>
		
		<c:forEach items="${disks}" var="disk">
		<option value="<c:out value="${disk.id}"/>">
			<c:out value="${disk.label}" />
		</option>
		</c:forEach>
	</select>
</form>

<fmt:message key="disk.dirs.heading"/>
<table>
	<c:forEach items="${disk.root.dirs}" var="dir">
	<tr>
		<td>
			<a href="dir.htm?dir=<c:out value="${dir.id}"/>">
				<c:out value="${dir.name}"/>
			</a>
		</td>
	</tr>
	</c:forEach>
</table>

<fmt:message key="disk.files.heading"/>
<table>
	<c:forEach items="${disk.root.files}" var="file">
	<tr>
		<td>
			<a href="file.htm?dir=<c:out value="${file.id}"/>">
				<c:out value="${file.name}"/>
			</a>
		</td>
	</tr>
	</c:forEach>
</table>

</body>
</html>