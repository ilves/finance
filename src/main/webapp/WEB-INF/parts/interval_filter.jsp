<div class="panel panel-default">
    <div class="panel-heading">
        <h3 class="panel-title">Period</h3>
    </div>
    <div class="panel-body">
        <ul class="nav nav-pills">
            <li class="<c:if test="${period eq 'full'}">active</c:if>"><a href="?period=full&step=${step}">Full</a></li>
            <li class="<c:if test="${period eq 'previous'}">active</c:if>"><a href="?period=previous&step=${step}">${end.get(1)-1}</a></li>
            <li class="<c:if test="${period eq 'current'}">active</c:if>"><a href="?period=current&step=${step}">${end.get(1)}</a></li>
        </ul>
    </div>
</div>


<div class="panel panel-default">
    <div class="panel-heading">
        <h3 class="panel-title">Interval</h3>
    </div>
    <div class="panel-body">
        <ul class="nav nav-pills">
            <li class="<c:if test="${step eq 'month'}">active</c:if>"><a href="?step=month&period=${period}">Kuu</a></li>
            <li class="<c:if test="${step eq 'year'}">active</c:if>"><a href="?step=year&period=${period}">Aasta</a></li>
        </ul>
    </div>
</div>