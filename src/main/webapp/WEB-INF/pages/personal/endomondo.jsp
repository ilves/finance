<%@ include file="/WEB-INF/layout/taglibs.jsp"%>
<tiles:insertDefinition name="baseLayout">
    <tiles:putAttribute name="content">
        <div class="row">
            <div class="col-md-12">
                <z:chart chart="${parser.parse(chart)}" id="1" />
            </div>
        </div>
    </tiles:putAttribute>
</tiles:insertDefinition>