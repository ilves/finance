<%@ include file="/WEB-INF/layout/taglibs.jsp"%>
<tiles:importAttribute name="menuItems" />
<ul class="nav navbar-nav pull-right">
    <c:forEach var="item" items="${menuItems.items}">
        <li class="<c:if test="${item.active == true}">active</c:if>">
            <a href="${item.url}">${item.label}</a>
        </li>
    </c:forEach>
</ul>
