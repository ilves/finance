<%@ include file="/WEB-INF/layout/taglibs.jsp"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<tiles:insertDefinition name="baseLayout">
    <tiles:putAttribute name="content">
        <%@ include file="/WEB-INF/parts/interval_filter.jsp" %>
        <table style="width:auto;" class="table pull-right">
            <tr>
                <th>P2P Loan kokku:</th>
                <td><fmt:formatNumber currencySymbol="" type="currency" value="${p2p_sum/100}" />&euro;
                (<fmt:formatNumber currencySymbol="" type="currency" value="${p2p_sum/100/months}" /> &euro;)
                </td>
            </tr>
            <tr>
                <th>Kinnisvara kokku:</th>
                <td><fmt:formatNumber currencySymbol="" type="currency" value="${real_sum/100}" />&euro;
                (<fmt:formatNumber currencySymbol="" type="currency" value="${real_sum/100/months}" /> &euro;)
                </td>
            </tr>
            <tr>
                <th>Aktsiad kokku:</th>
                <td><fmt:formatNumber currencySymbol="" type="currency" value="${shares_sum/100}" />&euro;
                (<fmt:formatNumber currencySymbol="" type="currency" value="${shares_sum/100/months}" /> &euro;)
                </td>
            </tr>
            <tr>
                <th>Kokku:</th>
                <td><fmt:formatNumber currencySymbol="" type="currency" value="${(real_sum+p2p_sum+shares_sum)/100}" />&euro;
                (<fmt:formatNumber currencySymbol="" type="currency" value="${(real_sum+p2p_sum+shares_sum)/100/months}" /> &euro;)
                </td>
            </tr>
        </table>
        <div class="clearfix"></div>
        <z:chart chart="${chart}" />

    </tiles:putAttribute>
</tiles:insertDefinition>