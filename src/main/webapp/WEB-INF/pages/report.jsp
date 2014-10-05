<%@ include file="/WEB-INF/layout/taglibs.jsp"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<tiles:insertDefinition name="baseLayout">
    <tiles:putAttribute name="content">
        <%@ include file="/WEB-INF/parts/interval_filter.jsp" %>
        <z:chart chart="${chart}" />
    </tiles:putAttribute>
</tiles:insertDefinition>