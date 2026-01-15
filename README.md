# DYNAMIC-R---Inventory-and-Job-Order-Management-System


A comprehensive Java Swing-based management system for tracking inventory and job orders with role-based access control.

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

- ### Job Order Management
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
INSERT INTO user_table (username, user_password, role, full_name) VALUES
('admin', '240be518fabd2724ddb6f04eeb1da5967448d7e831c08c8fa822809f74c720a9', 'Admin', 'System Administrator'),
('warehouse', '0e842cbe0341154ee33e0ed3bc18282cd69e016a8d56fda05ec92e7ff20a0f31', 'Warehouse Manager', 'Warehouse Manager'),
('foreman', '58e84b33d6714011787d934f859b0d12a74cc4f6f7c8939b8499d0481ff86b37', 'Production Foreman', 'Production Foreman');
```

### 4. Add MySQL Connector

Download MySQL Connector/J from [MySQL official site](https://dev.mysql.com/downloads/connector/j/) and add to project libraries.

### 5. Run the Application

Run the main class: `LoginPage.LoginForm`

## 👤 Default Login Credentials

| Role | Username | Password |
|------|----------|----------|
| Admin | admin | admin123 |

## 📸 Screenshots
<img width="1920" height="1080" alt="Screenshot (306)" src="https://github.com/user-attachments/assets/ba49764b-af24-4e44-a9ca-49751cbb1cc1" />
<img width="1920" height="1080" alt="Screenshot (307)" src="https://github.com/user-attachments/assets/72d37be3-128e-4ad2-b525-3a32179f395e" />
<img width="1920" height="1080" alt="Screenshot (308)" src="https://github.com/user-attachments/assets/e58f81dd-8a8d-40bf-badf-e08acce4c8d3" />
<img width="1920" height="1080" alt="Screenshot (309)" src="https://github.com/user-attachments/assets/6cf75dfe-6a13-413e-ac9e-ff35920726db" />
<img width="1920" height="1080" alt="Screenshot (310)" src="https://github.com/user-attachments/assets/0eed8227-660a-4e5b-90ee-505bbd7e11bb" />
<img width="1920" height="1080" alt="Screenshot (311)" src="https://github.com/user-attachments/assets/2f0b2406-a224-499a-b19d-f9fb16584152" />
<img width="1920" height="1080" alt="Screenshot (312)" src="https://github.com/user-attachments/assets/e130c25e-39b4-4597-af83-a17732b30d01" />
<img width="1920" height="1080" alt="Screenshot (313)" src="https://github.com/user-attachments/assets/d502bd8c-3031-45f2-84ab-e7fa1287af0a" />
<img width="1920" height="1080" alt="Screenshot (314)" src="https://github.com/user-attachments/assets/cca5fe2a-883d-447a-96df-3868eff24240" />
<img width="1920" height="1080" alt="Screenshot (315)" src="https://github.com/user-attachments/assets/1e67ef13-4b19-4f20-a3ca-9bc59f218a8e" />

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

## 👨‍💻 Author

**Your Name**
- GitHub: [@yourusername](https://github.com/yourusername)
- LinkedIn: [Your Name](https://linkedin.com/in/yourprofile)
- Email: your.email@example.com

## 🙏 Acknowledgments

- Thanks to all contributors
- Inspired by real-world inventory management needs
- Built with Java Swing best practices

## 📞 Support

For support, email your.email@example.com or open an issue in the repository.

---

