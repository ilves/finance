<%@ include file="/WEB-INF/layout/taglibs.jsp"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<tiles:insertDefinition name="baseLayout">
    <tiles:putAttribute name="content">
        <div class="col-sm-8">
            <c:forEach var="chart" items="${charts}">
                <c:set var="id" value="${id+1}" />
                <div class="panel panel-info">
                    <div class="panel-heading">
                        <h3 class="panel-title">
                                ${chart.getTitle().getText()}
                                ${chart.getTitle().setText(null)}
                        </h3>
                    </div>
                    <div class="panel-body">
                        <z:chart chart="${parser.parse(chart)}" id="${id}" />
                    </div>
                </div>
            </c:forEach>
        </div>
        <div class="col-sm-4">
            <%@ include file="/WEB-INF/parts/interval_filter.jsp" %>
        </div>
    </tiles:putAttribute>
</tiles:insertDefinition>