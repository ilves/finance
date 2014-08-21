<%@ include file="/WEB-INF/layout/taglibs.jsp"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<tiles:insertDefinition name="baseLayout">
    <tiles:putAttribute name="content">
        <div id="container" style="min-width: 310px; height: 400px; margin: 0 auto"></div>
        <script type="text/javascript">
            jQuery.noConflict();

            var example = 'column-negative',
                    theme = 'default';

            (function($){ // encapsulate jQuery
                $(function () {
                    $('#container').highcharts({
                        chart: {
                            type: '${chart.type}'
                        },
                        title: {
                            text: 'Column chart with negative values'
                        },
                        xAxis: {
                            categories: ${chart.categoriesJson}
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
                        series: ${chart.seriesJson}
                    });
                });		})(jQuery);
        </script>
    </tiles:putAttribute>
</tiles:insertDefinition>