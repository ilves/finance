<%@ include file="/WEB-INF/layout/taglibs.jsp"%>
<div class="menu bg-warning">
    <ul class="nav nav-pills nav-stacked">
        <li>
            <c:url value="/bondora" var="bondora" />
            <a href="${bondora}">Bondora</a>
        </li>
        <li>
            <c:url value="/portfolio" var="portfolio" />
            <a href="${portfolio}">Portfolio</a>
        </li>
        <li>
            <c:url value="/investing" var="investing" />
            <a href="${investing}">Investing</a>
        </li>
        <li>
            <c:url value="/income" var="income" />
            <a href="${income}">Income</a>
        </li>
    </ul>
</div>