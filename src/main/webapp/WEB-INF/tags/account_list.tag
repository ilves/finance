<%@ include file="/WEB-INF/layout/taglibs.jsp"%>
<%@ attribute name="root" required="true" type="ee.golive.finants.model.Account"%>

<ul>
    <c:forEach var="acc" items="${root.childAccounts}">
        <li>
            <a href="<spring:url value="/account/${acc.guid}" />">${acc.name}</a>
            (${accountService.getAccountTotal(acc)})
            <z:account_list root="${acc}" />
        </li>
    </c:forEach>
</ul>