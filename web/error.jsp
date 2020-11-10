<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Error</title>
</head>
<body>
   <h1>Error Page</h1>

   <p><%= request.getAttribute("message") %></p>

   <a href="index.jsp">Home Page</a>
</body>
</html>
