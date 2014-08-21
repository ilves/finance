<%@ include file="/WEB-INF/layout/taglibs.jsp"%>
<tiles:insertDefinition name="baseLayout">
    <tiles:putAttribute name="content">
        <z:account_list root="${root}" level="1" />
    </tiles:putAttribute>
</tiles:insertDefinition>