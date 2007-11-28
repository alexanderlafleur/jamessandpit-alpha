<%@ include file="/WEB-INF/jsp/include.jsp" %>

<html>
<head><title><fmt:message key="viewWallet.title"/></title>

</head>
<body>
<h1><fmt:message key="viewWallet.heading"/></h1>
</p>

viewWallet.jsp
<p/>

<a href="manageWallets.htm">Wallets</a>
<p/>

	<fmt:message key="field.id"/>
	<c:out value="${wallet.id}" />
	<br>
	
	<fmt:message key="field.description"/>
	<c:out value="${wallet.description}" />
	<p>
	
	<a href="newDisk.htm?mode=new&walletId=<c:out value="${wallet.id}" />">New Disk</a> |
	<a href="deleteDisk">Delete Disk</a>
	<br>

	<fmt:message key="viewWallet.disks.heading"/>
	<br>
	
	<c:forEach items="${wallet.disks}" var="disk">
		<a href="disk.htm?mode=view&diskId=<c:out value="${disk.id}"/>">
			<c:out value="${disk.label}" />
		</a>
		-
		<a href="disk.htm?mode=delete&diskId=<c:out value="${disk.id}"/>">
			delete
		</a>
		
		<br>
	</c:forEach>

</body>
</html>