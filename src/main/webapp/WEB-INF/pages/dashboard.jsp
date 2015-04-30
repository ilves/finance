<%@ include file="/WEB-INF/layout/taglibs.jsp"%>
<tiles:insertDefinition name="baseLayout">
    <tiles:putAttribute name="content">
        <div class="row">
            <div class="col-md-3">
                <h3>Money</h3>
                <div class="bg-success text-right text-success padding no-margin">
                    <h1 class="no-margin">
                        <strong>
                            <fmt:formatNumber currencySymbol="" type="currency" value="${cashTotal/100}" /> &euro;
                        </strong>
                    </h1>
                </div>

                <hr />

                <h3>Investments</h3>
                <div class="bg-info text-right text-success padding no-margin">
                    <h1 class="no-margin">
                        <strong>
                            <fmt:formatNumber currencySymbol="" type="currency" value="${investmentsTotal/100}" /> &euro;
                        </strong>
                    </h1>
                </div>

                <hr />

                <h3>Net Worth</h3>
                <div class="bg-warning text-right text-warning padding no-margin">
                    <h1 class="no-margin text-success">
                        <strong>
                            <fmt:formatNumber currencySymbol="" type="currency" value="${netWorth/100}" /> &euro;
                        </strong>
                    </h1>
                    <h3 class="no-margin text-warning">
                        <fmt:formatNumber currencySymbol="" type="currency" value="${assetsTotal/100}" /> &euro;
                    </h3>
                    <h3 class="no-margin text-danger">
                        <fmt:formatNumber currencySymbol="" type="currency" value="${liabilitiesTotal/100}" /> &euro;
                    </h3>
                </div>
            </div>
            <div class="col-md-9"></div>
        </div>
    </tiles:putAttribute>
</tiles:insertDefinition>