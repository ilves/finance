<%@ include file="/WEB-INF/layout/taglibs.jsp"%>
<%@ attribute name="chart" required="true" type="java.lang.String"%>

<div id="container" style="min-width:310px;height:400px;margin: 0 auto;"></div>
<script type="text/javascript">
    jQuery.noConflict();
    var example = 'column-negative', theme = 'default';
    (function($){
        $(function () {
            $('#container').highcharts(${chart});
        });
    })(jQuery);
</script>