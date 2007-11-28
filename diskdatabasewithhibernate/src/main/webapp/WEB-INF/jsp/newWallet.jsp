<%@ include file="/WEB-INF/jsp/include.jsp" %>

<html>
<head><title><fmt:message key="newWallet.title"/></title>

</head>
<body>
<h1><fmt:message key="newWallet.heading"/></h1>
</p>

<form>
	<jsp:include page="/WEB-INF/jsp/fields/walletDescription.jsp"/>

  	<input type="hidden" name="mode" value="save"/>
  	<input type="submit" value="New Wallet" />
</form>

</body>
</html>