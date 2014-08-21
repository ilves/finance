<%@ include file="/WEB-INF/layout/taglibs.jsp"%>
<tiles:insertDefinition name="baseLayout">
    <tiles:putAttribute name="content">
        (${accountService.getAccountTotal(root)})
        <z:account_list root="${root}" />
    </tiles:putAttribute>
</tiles:insertDefinition>