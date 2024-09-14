<h2>Password Manager</h2>

<b>A fully-functional password manager written in Java Spring Boot and Electron.js / React!</b>

<img src='https://i.imgur.com/eLMt5eD.gif'/>

User data is tracked in an H2 SQL Database which includes:
<ul>
  <li>Website</li>
  <li>Username</li>
  <li>Password</li>
  <li>Date Created</li>
</ul> </br>

Users can copy, add, delete, and update passwords. </br>

<img src='https://i.imgur.com/gwR0Opr.gif'/>

Passwords are encrypted using <b>AES-256</b> encryption. Default key and salt values are: </br>
"YOUR_SECRET_KEY" and "YOUR_SALT"

<h3>How to run with default encryption keys</h3>

Clone the repo </br>
Run the pwm-frontend.exe executable in \pwm-frontend\out\pwm-frontend-win32-x64

<h3>How to run with custom encryption key</h3>

Clone the repo </br>
Open the project in your Java IDE of choosing </br>
Set your key and salt in src/main/java/pwn/DBConnect.java (You might want to do this with environment variables!) </br>
Package the project into .jar with Maven and move the jar file to \pwm-frontend\out\pwm-frontend-win32-x64\resources\target </br>
Move both .db files from the root directory to \pwm-frontend\out\pwm-frontend-win32-x64\resources </br>
Run pwm-frontend.exe!

<h3>In progress features:</h3>
Favourite specific passwords </br>
Search by website or username </br>
Choose database file location







