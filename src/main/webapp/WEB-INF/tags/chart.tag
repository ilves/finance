<%@ include file="/WEB-INF/layout/taglibs.jsp"%>
<%@ attribute name="chart" required="true" type="java.lang.String"%>
<%@ attribute name="id" required="false" type="java.lang.Integer"%>

<div id="container-${id}" style="min-width:310px;height:400px;margin: 0 auto;margin-top:20px;"></div>
<script type="text/javascript">
    jQuery.noConflict();
    var example = 'column-negative', theme = 'default';
    (function($){
        $(function () {
            $('#container-${id}').highcharts(${chart});
        });
    })(jQuery);
</script>