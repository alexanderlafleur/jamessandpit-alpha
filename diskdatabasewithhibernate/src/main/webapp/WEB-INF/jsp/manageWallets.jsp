<%@ include file="/WEB-INF/jsp/include.jsp" %>

<html>
<head><title><fmt:message key="manageWallets.title"/></title>

</head>
<body>
<h1><fmt:message key="manageWallets.heading"/></h1>
</p>

<a href="newWallet.htm?mode=new">New wallet</a> |
<a href="deleteWallet">Delete Wallet</a>

<p>


manageWallets.html

<form>
	<input name="mode" type="hidden" value="view"/>
	
	<c:forEach items="${wallets}" var="wallet">
		<a href="manageWallet.htm?mode=view&walletId=<c:out value="${wallet.id}"/>">
			<c:out value="${wallet.description}" />
		</a>
		-
		<a href="manageWallet.htm?mode=delete&walletId=<c:out value="${wallet.id}"/>">
			delete
		</a>
		<br>
	</c:forEach>
	
</form>

</body>
</html>