<%@ include file="/WEB-INF/layout/taglibs.jsp"%>
<tiles:insertDefinition name="baseLayout">
    <tiles:putAttribute name="content">
        <div class="row">
            <div class="col-md-4">
                <h3>Add weight</h3>
                <form:form method="POST" action="/weight/add" modelAttribute="weight">
                    <table class="table">
                        <tr>
                            <td><form:label path="date_created">Date</form:label></td>
                            <td><form:input path="date_created" /></td>
                        </tr>
                        <tr>
                            <td><form:label path="weight">Weight</form:label></td>
                            <td><form:input path="weight" /></td>
                        </tr>
                        <tr>
                            <td colspan="2">
                                <input type="submit" value="Add"/>
                            </td>
                        </tr>
                    </table>
                </form:form>
            </div>
            <div class="col-md-8">
                <h3>Entries</h3>
                <div style="max-height: 220px; overflow: scroll;">
                    <table class="table">
                        <c:forEach var="weight" items="${weights}">
                            <tr>
                                <td><fmt:formatDate value="${weight.date_created}" type="both" pattern="MM-dd-yyyy" /></td>
                                <td>${weight.weight}</td>
                            </tr>
                        </c:forEach>
                    </table>
                </div>
            </div>
        </div>
        <div class="row">
            <div class="col-md-12">
                <z:chart chart="${parser.parse(chart)}" id="1" />
            </div>
        </div>
    </tiles:putAttribute>
</tiles:insertDefinition>