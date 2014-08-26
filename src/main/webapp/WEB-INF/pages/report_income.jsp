<%@ include file="/WEB-INF/layout/taglibs.jsp"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<tiles:insertDefinition name="baseLayout">
    <tiles:putAttribute name="content">
        <ul class="nav nav-pills">
          <li class="<c:if test="${period eq 'full'}">active</c:if>"><a href="?period=full">Alates algusest</a></li>
          <li class="<c:if test="${period eq 'previous'}">active</c:if>"><a href="?period=previous">Eelmine aasta</a></li>
          <li class="<c:if test="${period eq 'current'}">active</c:if>"><a href="?period=current">See aasta</a></li>
        </ul>
        <table style="width:auto;" class="table pull-right">
            <tr>
                <th>Palk kokku:</th>
                <td><fmt:formatNumber currencySymbol="" type="currency" value="${salary_sum/100}" />&euro;
                (<fmt:formatNumber currencySymbol="" type="currency" value="${salary_sum/100/months}" />&euro;)</td>
            </tr>
            <tr>
                <th>Firma k&auml;ive kokku:</th>
                <td><fmt:formatNumber currencySymbol="" type="currency" value="${company_sum/100}" />&euro;
                (<fmt:formatNumber currencySymbol="" type="currency" value="${company_sum/100/months}" />&euro;)</td>
                </td>
            </tr>
            <tr>
                <th>Kokku:</th>
                <td><fmt:formatNumber currencySymbol="" type="currency" value="${(salary_sum+company_sum)/100}" />&euro;
                (<fmt:formatNumber currencySymbol="" type="currency" value="${(salary_sum+company_sum)/100/months}" />&euro;)
                </td>
            </tr>
        </table>
        <div class="clearfix"></div>
        <z:chart chart="${chart}" />

    </tiles:putAttribute>
</tiles:insertDefinition>