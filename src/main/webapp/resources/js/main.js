jQuery.noConflict();
(function($){
    $(document).ready(function() {
        $(".account-list tr").toggle();
        $(".account-list .level-1").toggle();
        zebra();
        $(".account-list .toggle").click(function () {
            var tr = $(this).parents("tr");
            var id = $(tr).attr("data-id");
            $('[data-parent="' + id + '"]').toggle();
            zebra();
            return false;
        });
    });
    function zebra() {
        $(".account-list tr:visible").each(function(index, row){
            $(row).removeClass('odd');
            if (index%2==1){
                $(row).addClass('odd');
            }
        });
    }
})(jQuery);

function StackedTotal(obj) {
    return '<b>' +   Highcharts.dateFormat('%Y-%m',
        new Date(obj.x))+ '</b><br/>' +
        obj.series.name + ': ' + obj.y + '€<br/>' +
        'Total: ' + obj.point.stackTotal + '€';
}

function PercentFormatter(obj) {
    return '<b>' +   Highcharts.dateFormat('%Y-%m',
        new Date(obj.x))+ '</b><br/>' +
        obj.series.name + ': ' + (Math.round(obj.y*100)/100) + '%<br/>';
}