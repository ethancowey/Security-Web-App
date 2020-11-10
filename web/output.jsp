<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Output</title>
</head>
<body>

<h1>Database Contents</h1>

<div>
    <%= request.getAttribute("data") %>
</div>

<div>
    <a href="index.jsp">Home Page</a>
</div>

</body>
</html>
