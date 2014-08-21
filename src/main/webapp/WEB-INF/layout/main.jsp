<%@ include file="/WEB-INF/layout/taglibs.jsp"%>
<html>
    <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link rel="stylesheet" type="text/css" href="<c:url value='/resources/bootstrap/css/bootstrap.css'/>"/>
    <link rel="stylesheet" type="text/css" href="<c:url value='/resources/bootstrap/css/bootstrap-theme.css'/>"/>
    <link rel="stylesheet" type="text/css" href="<c:url value='/resources/style/main.css'/>"/>

    <title>Default tiles template</title>
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