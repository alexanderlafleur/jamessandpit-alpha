<%@ include file="/WEB-INF/jsp/include.jsp" %>

<fmt:message key="field.description"/>

<input name="walletDescription" type="text">
	<c:out value="${wallet.description}" />
</input>
		
