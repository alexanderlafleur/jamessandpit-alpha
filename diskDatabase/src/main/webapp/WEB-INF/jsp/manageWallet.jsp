<%@ include file="/WEB-INF/jsp/include.jsp" %>

<html>
<head><title><fmt:message key="manageWallet.title"/></title>

</head>
<body>
<h1><fmt:message key="manageWallet.heading"/></h1>
</p>

<a href="manageWallets.htm">Wallets</a>
managewallet

<form>
	<fmt:message key="field.id"/>
	<c:out value="${wallet.id}" />
	<br>
	
	<fmt:message key="field.description"/>
	<c:out value="${wallet.description}" />
	<p>
	
  	<input type="hidden" name="mode" value="save"/>
  	<input type="submit" value="Save Wallet" />
</form>

</body>
</html>