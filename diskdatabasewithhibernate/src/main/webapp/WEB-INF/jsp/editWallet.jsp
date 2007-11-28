<%@ include file="/WEB-INF/jsp/include.jsp" %>

<html>
<head><title><fmt:message key="editWallet.title"/></title>

</head>
<body>
<h1><fmt:message key="editWallet.heading"/></h1>
</p>
edit wallet

<form>
	<jsp:include page="/WEB-INF/jsp/fields/walletDescription.jsp"/>

  	<input type="hidden" name="mode" value="save"/>
  	<input type="submit" value="Save Wallet" />
</form>

</body>
</html>