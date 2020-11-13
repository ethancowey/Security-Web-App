<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
  <head>
    <title>Home</title>
  </head>
  <body onload="loginDisabled()">

  <h1>Home Page</h1>
  <script>
  function validateForm() {
      //document.getElementID("phone") will from the form get the phone number
      //True if format follows regex False if not
      var phoneValidCheck = RegExp(/^[0-9]{2}[-\s\.]{0,1}[0-9]{4}[-\s\.]{0,1}[0-9]{7}$/).test(document.getElementById("phone").value);
      if (!phoneValidCheck) {
          alert("Incorrect format for phone number \n Should be in the format XX-XXXX-XXXXXXX");
            return false;//Returning false means the form is not submitted
      }
      //document.getElementID("password") will from the form get the password
      //True if format follows regex False if not
      var passValidCheck = RegExp(/(?=^.{8,15}$)(?=.*\d)(?=.*[a-z])(?=.*[A-Z])[0-9a-zA-Z]*$/).test(document.getElementById("password").value);
      if(!passValidCheck){
          alert("Incorrect format for password \n It should be between 8 and 15 characters have at least 1 digit, 1 uppercase and 1 lowercase letter");
          return false;//Returning false means the form is not submitted
      }

      //document.getElementID("email") will from the form get the email
      //True if format follows regex False if not
      var emailValidCheck = RegExp(/^[\w\.=-]+@[\w\.-]+\.[A-Z a-z]{2,3}$$/).test(document.getElementById("email").value);
      if(!emailValidCheck){
          alert("Incorrect format for an email address");
          return false;//Returning false means the form is not submitted
      }
      return true;//All tests passed so the form is submitted
  }

  function validateLogin(){
      //document.getElementID("password") will from the form get the password
      //True if format follows regex False if not
      var passValidCheck = RegExp(/(?=^.{8,15}$)(?=.*\d)(?=.*[a-z])(?=.*[A-Z])[0-9a-zA-Z]*$/).test(document.getElementById("passwordLogin").value);//True if format follows regex False if not
      if(!passValidCheck){
          alert("incorrect format for password");
          return false;
      }
      return true;
  }
  function loginDisabled(){
  <%if(session.getAttribute("attempts")!=null&&(int)session.getAttribute("attempts") ==3){%>
  document.getElementById("usernameLogin").disabled=true;//When the fourth attempt is made it will disable the submit button and all the fields
  document.getElementById("passwordLogin").disabled=true;
  document.getElementById("buttonLogin").disabled=true;
  document.getElementById("LoginForm").disabled=true;
  alert("Login Failed no Login Attempts Available");
  <%session.setAttribute("attempts",0);//reset back to 0 so if you reload the page so you can try again%>
  <%}%>
  }
  </script>
  <form action="CreateAccount" onsubmit="return validateForm()" method="post">
      <label for="firstname">First name:</label><br>
      <input type="text" id="firstname" name="firstname" required><br>
      <label for="lastname">Last name:</label><br>
      <input type="text" id="lastname" name="lastname" required><br>
      <label for="username">Username:</label><br>
      <input type="text" id="username" name="username" required><br><br>
      <label for="phone">phone:</label><br>
      <input type="text" id="phone" name="phone" required><br><br>
      <label for="email">email:</label><br>
      <input type="text" id="email" name="email" required><br><br>
      <label for="password">password:</label><br>
      <input type="text" id="password" name="password" required><br><br>
      <label for="admin">admin:</label><br>
      <input type="checkbox" id="admin" name="admin"><br><br>
      <input type="submit" value="Submit">
  </form>
  <form id =LoginForm action="UserLogin" onsubmit="return validateLogin()" method="post">
  <label for="usernameLogin">Username:</label><br>
  <input type="text" id="usernameLogin" name="username"><br><br>
  <label for="passwordLogin">password:</label><br>
  <input type="text" id="passwordLogin" name="password"><br><br>
  <input type="submit" id="buttonLogin" value="Submit">
  </form>
  </body>
</html>
