<%@ include file="/WEB-INF/jsp/include.jsp" %>

<html>
<head><title><fmt:message key="manageDisks.title"/></title>

</head>
<body>
<h1><fmt:message key="manageDisks.heading"/></h1>
</p>

<a href="newDisk.htm?mode=new">New Disk</a> |
<a href="manageDisks.htm?mode=load">Load Disk</a> |
<a href="deleteDisk">Delete Disk</a>

<p>

manageDisks.jsp

<a href="manageWallet.htm?mode=view&walletId=${wallet.id}">Up to Wallet</a>

<form>
	<input name="mode" type="hidden" value="view"/>
	
	<c:forEach items="${disks}" var="disk">
		<a href="manageDisks.htm?mode=view&diskId=<c:out value="${disk.id}"/>">
			<c:out value="${disk.label}" />
		</a>
		<br>
	</c:forEach>
</form>

</body>
</html>