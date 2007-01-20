<spring:hasBindErrors name="${empty param.name ? 'form' : param.name}">
  <div class="errorMessage">
    The following errors were detected:
    <c:forEach items="${errors.allErrors}" var="error">
      <div class="errorItem">
        <span class="error-icon">!</span>
        ${error.arguments} - ${error.defaultMessage}
      </div>
<!--
ERROR: ${error}
-->
    </c:forEach>
  </div>
</spring:hasBindErrors>
