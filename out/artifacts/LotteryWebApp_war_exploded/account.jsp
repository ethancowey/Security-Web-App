<%@ page import="java.util.ArrayList" %>
<%@ page import="java.lang.reflect.Array" %>
<%@ page import="java.util.List" %>
<%@ page import="jdk.jfr.internal.JVM" %>
<%@ page import="java.io.PrintWriter" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Account</title>
</head>
<%if(session.getAttribute("admin")!=null){
    response.sendRedirect("UserLogin"); //This redirects to the doGet method which logs you out preventing an invalid user getting here
}
%>
<body>
<h1>User Account</h1>

<p><%= session.getAttribute("message") %></p>
<p><%= request.getAttribute("filteredMessage") %></p>
<p><%= session.getAttribute("message2") %></p>


<p><%= "Below Are your draws remember to click get draws for them to appear" %></p>
<p><%= session.getAttribute("draws") %></p>

<script>
    function validateNumbers(){
    var flag = 0;
    var num1 = document.getElementById("number1");
    checknumber(num1)
    var num2 = document.getElementById("number2");
    checknumber(num2)
    var num3 = document.getElementById("number3");
    checknumber(num3)
    var num4 = document.getElementById("number4");
    checknumber(num4)
    var num5 = document.getElementById("number5");
    checknumber(num5)
    var num6 = document.getElementById("number6");
    checknumber(num6)
    function checknumber(num){
    if (num.value < 0 || num.value > 60 || num.value == null) {
        alert ("Number must be between 0 and 60");
    return flag = 1;
    }
    }
    if(flag === 1){
        return false;
    }
    else{
        return true;
    }
    }
    function numberGenerator() {
        numberGeneratorCall(document.getElementById("number1"));
        numberGeneratorCall(document.getElementById("number2"));
        numberGeneratorCall(document.getElementById("number3"));
        numberGeneratorCall(document.getElementById("number4"));
        numberGeneratorCall(document.getElementById("number5"));
        numberGeneratorCall(document.getElementById("number6"));
    }
        function numberGeneratorCall(x){
        var byteNumbers = new Uint8Array(1);
        window.crypto.getRandomValues(byteNumbers);//Helps make a secure random number
        var randomNum = '0.' + byteNumbers[0].toString();
        var rangeNum = 60 - 0 + 1;//Max - Min + 1
        x.value = Math.floor((randomNum * rangeNum));//sets x the form element box to the random number for the user
    }

</script>
<form action="AddUserNumbers" onsubmit="return validateNumbers()" method="post">
    <label for="number1">First Number:</label><br>
    <input type="number" id="number1" name="number1" required><br>
    <label for="number2">Second Number:</label><br>
    <input type="number" id="number2" name="number2" required><br>
    <label for="number3">Third Number:</label><br>
    <input type="number" id="number3" name="number3" required><br>
    <label for="number4">Fourth Number:</label><br>
    <input type="number" id="number4" name="number4" required><br>
    <label for="number5">Fifth Number:</label><br>
    <input type="number" id="number5" name="number5" required><br>
    <label for="number6">Sixth Number:</label><br>
    <input type="number" id="number6" name="number6" required><br>
    <input type="submit" value="Submit">
</form>
<button onclick="numberGenerator()" type="button">generate numbers</button>
<br>
<form action ="GetUserNumbers" method="post">
    <input type="submit" value="get your draws">
</form>
<br>
<form action="NumberChecker" method="post">
    <input type="submit" value="Check your numbers">
</form>
<p><%="Below will tell you if you've won please click Check your Numbers to see if you've won"%></p>
<p><%=session.getAttribute("winCheck")%></p>
</br>

<a href="UserLogin">Home Page and Log out</a>

</body>
</html>
