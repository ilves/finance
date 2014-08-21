<%@ include file="/WEB-INF/layout/taglibs.jsp"%>
<div class="menu bg-warning">
    <ul class="nav nav-pills nav-stacked">
        <li class="<c:if test="${requestScope['javax.servlet.forward.request_uri'] eq '/' or fn:contains(requestScope['javax.servlet.forward.request_uri'], '/ad')}">active</c:if>">
            <c:url value="/" var="home" />
            <a href="${home}">T&ouml;&ouml;kuulutused</a>
        </li>
        <li class="<c:if test="${requestScope['javax.servlet.forward.request_uri'] eq '/new'}">active</c:if>">
            <c:url value="/new" var="newAd" />
            <a href="${newAd}">Lisa uus kuulutus</a>
        </li>
        <li class="<c:if test="${requestScope['javax.servlet.forward.request_uri'] eq '/info'}">active</c:if>">
            <c:url value="/info" var="info" />
            <a href="${info}">Info</a>
        </li>
    </ul>
</div>


<iframe class="fb-plugin" src="//www.facebook.com/plugins/likebox.php?href=https%3A%2F%2Fwww.facebook.com%2Fprogrammeerija&amp;width&amp;height=400&amp;colorscheme=light&amp;show_faces=true&amp;header=true&amp;stream=false&amp;show_border=true&amp;appId=1461170917493753" scrolling="no" frameborder="0" style="border:none; overflow:hidden; height:400px;" allowTransparency="true"></iframe>