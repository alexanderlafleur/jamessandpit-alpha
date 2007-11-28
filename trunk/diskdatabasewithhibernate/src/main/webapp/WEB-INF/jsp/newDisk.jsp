<%@ include file="/WEB-INF/jsp/include.jsp" %>

<html>
<head><title><fmt:message key="newDisk.title"/></title>

</head>
<body>
<h1><fmt:message key="newDisk.heading"/></h1>
</p>

<form>
	<jsp:include page="/WEB-INF/jsp/fields/diskDescription.jsp"/>

  	<input type="hidden" name="walletId" value="<c:out value="${walletId}" />"/>
  	<input type="hidden" name="mode" value="save"/>
  	<input type="submit" value="New Disk" />
</form>

</body>
</html>