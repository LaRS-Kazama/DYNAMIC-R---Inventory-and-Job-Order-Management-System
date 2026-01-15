# Dynamic R - Inventory & Job Order Management System

A comprehensive Java Swing-based management system for tracking inventory and job orders with role-based access control.

## 📋 Features

### User Roles
- **Admin**: Full system access including inventory management, job order management, and dashboard analytics
- **Production Foreman (Mechanical)**: View and update job orders assigned to mechanical department
- **Warehouse Manager (Electrical)**: View and update job orders assigned to electrical/warehouse department

### Inventory Management
- ✅ Add, update, and soft-delete inventory items
- ✅ Real-time stock tracking (Available, Low Stock, Out of Stock, Damaged, Scrapped)
- ✅ Location-based organization
- ✅ Batch number tracking
- ✅ Automatic date stamping
- ✅ Filter and search functionality
- ✅ Category-based organization

### Job Order Management
- ✅ Create and manage job orders with multiple stages
- ✅ Department-based assignment (Mechanical, Electrical, Painting, Inspection)
- ✅ Stage tracking (Receiving, Dismantling, Rebuilding, Painting, Inspection, Completed)
- ✅ Status monitoring (In Progress, Pending, On Hold, Completed)
- ✅ Material assignment from available inventory
- ✅ File attachment support
- ✅ Progress notes and customization fields
- ✅ Deadline tracking with date picker

### Dashboard & Analytics
- 📊 Real-time statistics and counts
- 📈 Job order stage distribution chart
- 📝 Recent activity log
- 🎯 Quick overview cards for key metrics

## 🛠️ Technologies Used

- **Language**: Java 8+
- **GUI Framework**: Java Swing
- **Database**: MySQL
- **JDBC**: MySQL Connector/J
- **Build Tool**: Maven/Gradle (or specify if none)

## 📦 Prerequisites

- Java Development Kit (JDK) 8 or higher
- MySQL Server 5.7 or higher
- MySQL JDBC Driver
- IDE (IntelliJ IDEA, Eclipse, or NetBeans recommended)

## 🚀 Installation & Setup

### 1. Clone the Repository
```bash
git clone https://github.com/yourusername/dynamic-r-management-system.git
cd dynamic-r-management-system
```

### 2. Database Setup
```sql
-- Create database
CREATE DATABASE dynamic_r_db;
USE dynamic_r_db;

-- Create inventory table
CREATE TABLE inventory_table (
    inventory_id VARCHAR(20) PRIMARY KEY,
    item_name VARCHAR(255) NOT NULL,
    category VARCHAR(100),
    quantity INT DEFAULT 0,
    batch_no VARCHAR(100),
    location VARCHAR(100),
    status VARCHAR(50),
    date_added DATE
);

-- Create job orders table
CREATE TABLE job_orders (
    job_id INT PRIMARY KEY AUTO_INCREMENT,
    client_name VARCHAR(255) NOT NULL,
    department VARCHAR(100),
    employee_assigned VARCHAR(255),
    deadline DATE,
    stage VARCHAR(100),
    status VARCHAR(50),
    materials TEXT,
    job_description TEXT,
    customization TEXT,
    notes TEXT,
    reference_no VARCHAR(100),
    attachment_path TEXT,
    date_created DATE
);

-- Create users table (for authentication)
CREATE TABLE users (
    user_id INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(50) NOT NULL,
    full_name VARCHAR(255)
);

-- Insert default admin user
INSERT INTO users (username, password, role, full_name) 
VALUES ('admin', 'admin123', 'Admin', 'System Administrator');
```

### 3. Configure Database Connection

Edit `CommonConstant/MysqlCredentials.java`:
```java
public class MysqlCredentials {
    public static final String DB_URL = "jdbc:mysql://localhost:3306/dynamic_r_db";
    public static final String DB_USERNAME = "your_mysql_username";
    public static final String DB_PASSWORD = "your_mysql_password";
    public static final String DB_INVENTORY_TABLE_NAME = "inventory_table";
    public static final String DB_JOBORDER_TABLE = "job_orders";
}
```

### 4. Add MySQL Connector
Download MySQL Connector/J from [MySQL official site](https://dev.mysql.com/downloads/connector/j/) and add to project libraries.

### 5. Run the Application

Run the main class: `LoginPage.LoginForm`

## 👤 Default Login Credentials

| Role | Username | Password |
|------|----------|----------|
| Admin | admin | admin123 |

⚠️ **Important**: Change default passwords after first login!

## 📸 Screenshots

<img width="1920" height="1080" alt="Screenshot (314)" src="https://github.com/user-attachments/assets/e329bae1-892e-4ae9-a01b-54484bc24bbe" />
<img width="1920" height="1080" alt="Screenshot (312)" src="https://github.com/user-attachments/assets/32ceaa83-5797-4bdd-897d-2ae260c09a5e" />
<img width="1920" height="1080" alt="Screenshot (311)" src="https://github.com/user-attachments/assets/a4ebddf5-ebcd-44fa-86cc-cb24c39f319f" />
<img width="1920" height="1080" alt="Screenshot (310)" src="https://github.com/user-attachments/assets/29741bb7-239f-4f81-9159-87de995e5810" />
<img width="1920" height="1080" alt="Screenshot (313)" src="https://github.com/user-attachments/assets/375eb5b7-f857-4500-b524-536fbad4b235" />
<img width="1920" height="1080" alt="Screenshot (309)" src="https://github.com/user-attachments/assets/3c407663-f5fb-4f33-99ad-6b128908dc31" />
<img width="1920" height="1080" alt="Screenshot (307)" src="https://github.com/user-attachments/assets/4d446229-db00-4636-be4d-6b22470e4175" />
<img width="1920" height="1080" alt="Screenshot (308)" src="https://github.com/user-attachments/assets/70f5e1ee-08f8-4d19-8ef6-00977c9231cb" />
<img width="1920" height="1080" alt="Screenshot (315)" src="https://github.com/user-attachments/assets/de14bafd-d858-4a7c-ab63-5b52b48d486d" />
<img width="1920" height="1080" alt="Screenshot (306)" src="https://github.com/user-attachments/assets/994862f0-7049-4834-8191-cb4e91d64f6f" />
<img width="1920" height="1080" alt="Screenshot (316)" src="https://github.com/user-attachments/assets/2f39c476-c1f6-4aa5-b093-d81c39d4eaa9" />

## 📂 Project Structure
```
dynamic-r-management-system/
├── src/
│   ├── AdminPage/
│   │   ├── AdminDashboard.java
│   │   ├── DashboardPanel.java
│   │   ├── InventoryPanel.java
│   │   ├── JobOrderPanel.java
│   │   ├── AddItemDialog.java
│   │   ├── UpdateItemDialog.java
│   │   ├── AddJobOrderDialog.java
│   │   ├── UpdateJobOrderDialog.java
│   │   └── DeleteJobOrderDialog.java
│   ├── ProductionForemanPage/
│   │   ├── ProductionJobOrderDashboard.java
│   │   ├── ProductionJobOrderPanel.java
│   │   └── UpdateProgressDialog.java
│   ├── WarehouseManagerPage/
│   │   └── WarehouseDashboard.java
│   ├── WarehousePage/
│   │   ├── WarehouseJobOrderPanel.java
│   │   └── WarehouseUpdateProgressDialog.java
│   ├── DaoClass/
│   │   ├── InventoryDao.java
│   │   └── JobOrderDao.java
│   ├── CommonConstant/
│   │   ├── AppColors.java
│   │   └── MysqlCredentials.java
│   └── LoginPage/
│       ├── LoginForm.java
│       └── BaseForm.java
├── screenshots/
├── database/
│   └── schema.sql
├── README.md
├── LICENSE
└── .gitignore
```

## 🔒 Security Features

- Role-based access control (RBAC)
- Soft delete for inventory items (data retention)
- Department-based job order isolation
- Logout confirmation dialogs

## 🎯 Key Functionalities

### Inventory
- **Soft Delete**: Items marked as "Scrapped" remain in database for historical records
- **Smart Filtering**: Only available items appear in job order material selection
- **Auto-ID Generation**: Automatic inventory ID creation (INV-001, INV-002, etc.)
- **Status Tracking**: Real-time status updates (Available, Low Stock, Damaged, etc.)

### Job Orders
- **Department Segregation**: Each department sees only their assigned orders
- **Progress Tracking**: Stage-by-stage workflow monitoring
- **Material Validation**: Only available inventory items can be assigned
- **File Attachments**: Support for reference documents and files

## 🐛 Known Issues

- Export to Excel feature pending implementation
- Email notifications not yet implemented

## 🚧 Future Enhancements

- [ ] Email notifications for approaching deadlines
- [ ] Excel export functionality
- [ ] Advanced reporting and analytics
- [ ] Mobile responsive interface
- [ ] API for third-party integration
- [ ] Backup and restore functionality
- [ ] Audit trail logging

## 🤝 Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

1. Fork the project
2. Create your feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## 📝 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.


## 🙏 Acknowledgments

- Thanks to all contributors
- Inspired by real-world inventory management needs
- Built with Java Swing best practices

## 📞 Support

For support, email your.email@example.com or open an issue in the repository.

---

⭐ Star this repo if you find it helpful!
