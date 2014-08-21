<%@ include file="/WEB-INF/layout/taglibs.jsp"%>
<tiles:insertDefinition name="baseLayout">
    <tiles:putAttribute name="content">
        <h1>${account.name}</h1>
        <table class="table">
             <c:forEach var="split" items="${account.getSplits()}">
             <tr>
                <td>${split.transaction.description}</td>
                <td>${split.value_num}</td>
             </tr>
             </c:forEach>
        </table>
    </tiles:putAttribute>
</tiles:insertDefinition>