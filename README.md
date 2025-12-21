<h1 align="center">🍽️ MultiCanteen Management System</h1>

<p><strong>📅 Project Date:</strong> December 2025</p>

<h2>🔍 Overview</h2>
<p>
The <strong>MultiCanteen Management System</strong> is a web-based application developed using 
<strong>Spring Boot</strong> that enables efficient management of multiple canteens under a single platform. 
The system supports role-based access for <strong>Admin</strong>, <strong>Canteen</strong>, and 
<strong>Student</strong> users, allowing students to place orders, canteens to manage menus and orders, 
and admins to control the entire system.
</p>

<h2>⚡ Features</h2>
<ul>
  <li>👨‍💼 <strong>Admin Module:</strong> Manage canteens, users, and view overall orders.</li>
  <li>🏪 <strong>Canteen Module:</strong> Add/update food items, manage menus, and process orders.</li>
  <li>🎓 <strong>Student Module:</strong> Register, login, browse menus, place orders, and view order history.</li>
  <li>🔐 <strong>Authentication & Authorization:</strong> Secure login with role-based access.</li>
  <li>🗄️ <strong>Database Integration:</strong> Persistent data storage using MySQL.</li>
  <li>🧩 <strong>MVC Architecture:</strong> Clean separation of Controller, Service, Repository, and Model layers.</li>
</ul>

<h2>📁 Project Structure</h2>
<pre>
src/main/java
 └── com/canteen/multi_canteen
      ├── controller
      ├── service
      ├── repository
      ├── model
      └── dto

src/main/resources
 ├── templates
 ├── static
 └── application.properties
</pre>

<h2>🛠️ Technologies Used</h2>
<ul>
  <li>Java ☕</li>
  <li>Spring Boot 🌱</li>
  <li>Spring MVC & Spring Data JPA 🔧</li>
  <li>Spring Security + BCrypt 🔐</li>
  <li>MySQL 🗄️</li>
  <li>Thymeleaf 🌐</li>
  <li>Maven 🧰</li>
</ul>

<h2>🚀 How to Run</h2>
<ol>
  <li>Clone the repository:<br>
      <code>git clone https://github.com/USERNAME/multicanteen.git</code>
  </li>
  <li>Open the project in IntelliJ IDEA / Eclipse</li>
  <li>Configure MySQL database in <code>application.properties</code></li>
  <li>Run the Spring Boot application</li>
  <li>Open browser and access:<br>
      <code>http://localhost:8080/login</code>
  </li>
</ol>

<h2>🔐 User Roles</h2>
<ul>
  <li><strong>ADMIN</strong> – Full system control</li>
  <li><strong>CANTEEN</strong> – Manage menu and orders</li>
  <li><strong>STUDENT</strong> – Place and track food orders</li>
</ul>

<h2>📌 Future Enhancements</h2>
<ul>
  <li>Online payment integration</li>
  <li>Order notifications</li>
  <li>Mobile-responsive UI</li>
  <li>Reports and analytics dashboard</li>
</ul>

<h2>📧 Contact</h2>
<p>
<strong>Praveen Naik</strong><br>
Email: <strong>naikp3256@gmail.com</strong>
</p>
