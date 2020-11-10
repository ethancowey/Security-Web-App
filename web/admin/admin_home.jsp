<%--
  Created by IntelliJ IDEA.
  User: ethan
  Date: 05/11/2020
  Time: 10:29
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Admin Home page</title>
    <h1>Admin Home Page</h1>
</head>
<%System.out.println(session.getAttribute("admin"));
    if(session.getAttribute("admin")==null){
    response.sendRedirect("UserLogin");//This redirects to the doGet method which logs you out preventing an invalid user getting here
}
%>
<body>
<form action="DisplayAllData" method="post">
    <input type="submit" value="Get All User Account Data">
</form>

    <%if(request.getAttribute("data") != null){%>
    <div><%=request.getAttribute("data")%></div>
    <%}%>


<a href="UserLogin">Home Page and Log out</a>
</body>
</html>
