<%@ include file="/WEB-INF/layout/taglibs.jsp"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ attribute name="root" required="true" type="ee.golive.finants.model.Account"%>
<%@ attribute name="level" required="true" type="java.lang.Integer"%>

<c:if test="${level eq 1}">
    <table class="account-list table">
</c:if>
    <c:forEach var="acc" items="${root.childAccounts}">
    <tr class="level-${level}" data-id="${acc.guid}" data-parent="${acc.parent_guid}">
        <td><span class="toggle <c:if test="${!empty acc.childAccounts}">active</c:if>"><b>&DoubleDownArrow;</b></span><a href="<spring:url value="/account/${acc.guid}" />">${acc.name}</a></td>
        <td class="sum"><fmt:formatNumber currencySymbol="" type="currency" value="${acc.sum/100}" /> &euro;</td>
    </tr>
    <z:account_list root="${acc}" level="${level+1}" />
    </c:forEach>
<c:if test="${level eq 1}">
    </table>
</c:if>