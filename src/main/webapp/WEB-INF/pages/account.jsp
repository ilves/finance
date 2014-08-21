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

        <div id="container" style="min-width: 310px; height: 400px; margin: 0 auto"></div>
        <script type="text/javascript">
            jQuery.noConflict();

            var example = 'column-negative',
                    theme = 'default';

            (function($){ // encapsulate jQuery
                $(function () {
                    $('#container').highcharts({
                        chart: {
                            type: 'column'
                        },
                        title: {
                            text: 'Column chart with negative values'
                        },
                        xAxis: {
                            categories: ${cartcategories}
                        },
                        credits: {
                            enabled: false
                        },
                        series: ${chartseries}
                    });
                });		})(jQuery);
        </script>
    </tiles:putAttribute>
</tiles:insertDefinition>