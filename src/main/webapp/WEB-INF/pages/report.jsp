<%@ include file="/WEB-INF/layout/taglibs.jsp"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<tiles:insertDefinition name="baseLayout">
    <tiles:putAttribute name="content">
        <ul class="nav nav-pills">
          <li class="<c:if test="${period eq 'full'}">active</c:if>"><a href="?period=full">Alates algusest</a></li>
          <li class="<c:if test="${period eq 'previous'}">active</c:if>"><a href="?period=previous">Eelmine aasta</a></li>
          <li class="<c:if test="${period eq 'current'}">active</c:if>"><a href="?period=current">See aasta</a></li>
        </ul>
        <z:chart chart="${chart}" />
        <script type="text/javascript">
            jQuery.noConflict();

            var example = 'column-negative',
                    theme = 'default';

            (function($){ // encapsulate jQuery
                $(function () {
                    $('#scontainer').highcharts({
                        chart: {

                        },
                        title: {
                            text: 'Column chart with negative values'
                        },
                        xAxis: {
                            categories:
                        },
                        stackLabels: {
                            enabled: true,
                            style: {
                                fontWeight: 'bold',
                                color: (Highcharts.theme && Highcharts.theme.textColor) || 'gray'
                            }
                        },
                        tooltip: {
                            formatter: function () {
                                return '<b>' + this.x + '</b><br/>' +
                                        this.series.name + ': ' + this.y + '<br/>' +
                                        'Total: ' + this.point.stackTotal;
                            }
                        },
                        plotOptions: {
                            column: {
                                stacking: 'normal'
                            }
                        },
                        credits: {
                            enabled: false
                        },
                    });
                });		})(jQuery);
        </script>
    </tiles:putAttribute>
</tiles:insertDefinition>