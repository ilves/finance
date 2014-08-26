<%@ include file="/WEB-INF/layout/taglibs.jsp"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<tiles:insertDefinition name="baseLayout">
    <tiles:putAttribute name="content">
        <h1>${account.name}</h1>
        <table class="table">
            <!--
             <c:forEach var="split" items="${account.getSplits()}">
             <tr>
                <td>${split.transaction.description}</td>
                <td>${split.value_num}</td>
             </tr>
             </c:forEach>
            -->
            <c:forEach var="point" items="${stats}">
                <tr>
                    <td>${point.year}.${point.month}</td>
                    <td><fmt:formatNumber currencySymbol="" type="currency" value="${point.sum/100}" /> &euro;</td>
                    <td>
                        <c:set var="total" value="${total+point.sum}"/>
                        <fmt:formatNumber currencySymbol="" type="currency" value="${total/100}" /> &euro;
                    </td>
                </tr>
            </c:forEach>
        </table>


    </tiles:putAttribute>
</tiles:insertDefinition>