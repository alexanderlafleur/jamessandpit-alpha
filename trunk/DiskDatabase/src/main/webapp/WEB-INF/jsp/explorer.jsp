<%@ include file="/WEB-INF/jsp/include.jsp" %>

<html>
<head><title><fmt:message key="explorer.title"/></title>
	<LINK href="css/main.css" rel="stylesheet" type="text/css"/>
	<SCRIPT LANGUAGE="JavaScript" SRC="js/main.js"> </SCRIPT>
</head>
<body>
<h1><fmt:message key="explorer.heading"/></h1>

<form id="form"	method="post" >

	<spring:bind path="form.action">
		<input id="action" type="hidden" size="10" name="${status.expression}" value="${status.value}"/>
	</spring:bind>
	
	<fmt:message key="explorer.wallet"/>
	
	<spring:bind path="form.wallet">
		<select name="wallet" onchange="submitForm('changeWallet')">
			<c:forEach items="${wallets}" var="wallet">
				<option value="<c:out value="${wallet.id}"/>">
					<c:out value="${wallet.id}"/> - <c:out value="${wallet.description}" />
				</option>
			</c:forEach>
		</select>
	</spring:bind>
		
	<fmt:message key="explorer.disks"/>

	<spring:bind path="form.disk">	
		<select name="disk" onchange="submitForm('changeDisk')">
			<c:forEach items="${form.disks}" var="disk">
				<option value="<c:out value="${disk.id}"/>">
					<c:out value="${disk.id}"/> - <c:out value="${disk.label}" />
				</option>
			</c:forEach>
		</select>
	</spring:bind>

	<input type="button" name="deleteDisk" value="deleteDisk" onClick="submitForm('deleteDisk')"/>
	<br>
		
		<table>
		<tr>
			<th><fmt:message key="explorer.dir"/></th>
			<th><fmt:message key="explorer.dirs"/></th>
			<th><fmt:message key="explorer.files"/></th>
		</tr>
		<tr>
			<td valign="top">
				<a onclick="submitForm('parentDir')">...</a>
				<br>
				<c:out value="${form.dir}" />
			</td>
			
			<td valign="top">
				<table>
					<spring:bind path="form.dir">	
						<select name="dir" size="10" onchange="submitForm('selectDir')">
							<c:forEach items="${form.dirs}" var="dir">
								<option value="<c:out value="${dir.id}"/>">
									<c:out value="${dir.id}"/> - <c:out value="${dir.name}" />
								</option>
							</c:forEach>
						</select>
					</spring:bind>
				</table>
			</td>

			<td valign="top">
				<table>
					<spring:bind path="form.file">	
						<select name="file" size="10" onchange="submitForm('selectFile')">
							<c:forEach items="${form.files}" var="file">
								<option value="<c:out value="${file.id}"/>">
									<c:out value="${file.id}"/> - <c:out value="${file.name}" />
								</option>
							</c:forEach>
						</select>
					</spring:bind>
				</table>
			</td>			
		</tr>
		</table>
	
</form>

</body>
</html>