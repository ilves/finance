<%@ include file="/WEB-INF/layout/taglibs.jsp"%>
<tiles:insertDefinition name="baseLayout">
    <tiles:putAttribute name="content">
        <table class="table">
            <c:forEach var="stock" items="${list}">
                <tr>
                    <td>${stock.value.code}</td>
                    <td>${stock.value.price}</td>
                    <td>${stock.value.profit}</td>
                    <td>${stock.value.totalStock}</td>
                </tr>
            </c:forEach>
        </table>
    </tiles:putAttribute>
</tiles:insertDefinition>