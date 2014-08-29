<%@ include file="/WEB-INF/layout/taglibs.jsp"%>
<html>
    <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

    <link rel='stylesheet' href='/webjars/bootstrap/3.2.0/css/bootstrap.min.css' />

    <link rel="stylesheet" type="text/css" href="<c:url value='/resources/style/main.css'/>"/>

    <script type="text/javascript" src="/webjars/jquery/2.1.1/jquery.min.js"></script>
    <script type="text/javascript" src="<c:url value='/resources/js/main.js'/>"></script>
    <script type="text/javascript" src="/webjars/highcharts/4.0.3/highcharts.js"></script>


    <title>TITLE</title>
    <style type="text/css">

    </style>
</head>
<body role="document">
    <script>
        (function(i,s,o,g,r,a,m){i['GoogleAnalyticsObject']=r;i[r]=i[r]||function(){
            (i[r].q=i[r].q||[]).push(arguments)},i[r].l=1*new Date();a=s.createElement(o),
                m=s.getElementsByTagName(o)[0];a.async=1;a.src=g;m.parentNode.insertBefore(a,m)
        })(window,document,'script','//www.google-analytics.com/analytics.js','ga');
        ga('create', 'UA-20618536-8', 'auto');
        ga('send', 'pageview');
    </script>
    <tiles:insertAttribute name="header" />
    <div class="container">
        <div class="row">
            <div class="col-sm-8">
                <tiles:insertAttribute name="content" />
            </div>
            <div class="col-sm-3 col-sm-offset-1">
                <tiles:insertAttribute name="menu" />
            </div>
        </div>
    </div>
    <tiles:insertAttribute name="footer" />
</body>
</html>