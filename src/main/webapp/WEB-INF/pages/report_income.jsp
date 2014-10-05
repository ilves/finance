<%@ include file="/WEB-INF/layout/taglibs.jsp"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<tiles:insertDefinition name="baseLayout">
    <tiles:putAttribute name="content">
        <%@ include file="/WEB-INF/parts/interval_filter.jsp" %>
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