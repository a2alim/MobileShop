/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.controller;

import com.entity.Admin;
import com.entity.Employee;
import com.entity.Login;
import com.entity.Products;
import com.entity.Purchase;
import com.entity.Sales;
import com.util.HibernateUtil;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

/**
 *
 * @author ABDUL ALIM
 */
@ManagedBean
@RequestScoped
public class ProductBean {

    Products products = new Products();
    Login login = new Login();
    Employee employee = new Employee();
    private Purchase purchase = new Purchase();
    private Sales sales = new Sales();
    private Admin admin = new Admin();
    Date dt = new Date();
    String date = new SimpleDateFormat("yyyy-MM-dd").format(dt);
    private int sqty;
    private String sDate;
    private String pDate;
    private int sumq = 0;
    private double sump = 0;
    private List<Products> productList;
    private List<Products> productList1;

    public Products getProducts() {
        return products;
    }

    public void setProducts(Products products) {
        this.products = products;
    }

    public Login getLogin() {
        return login;
    }

    public void setLogin(Login login) {
        this.login = login;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public int getSqty() {
        return sqty;
    }

    public void setSqty(int sqty) {
        this.sqty = sqty;
    }

    public Purchase getPurchase() {
        return purchase;
    }

    public void setPurchase(Purchase purchase) {
        this.purchase = purchase;
    }

    public Sales getSales() {
        return sales;
    }

    public void setSales(Sales sales) {
        this.sales = sales;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    

    public int getSumq() {
        return sumq;
    }

    public void setSumq(int sumq) {
        this.sumq = sumq;
    }

    public double getSump() {
        return sump;
    }

    public void setSump(double sump) {
        this.sump = sump;
    }

    public Admin getAdmin() {
        return admin;
    }

    public void setAdmin(Admin admin) {
        this.admin = admin;
    }

    public String addEmployee() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            employee.setJoinDate(date);
            session.save(employee);
            tx.commit();
            session.flush();
        } catch (Exception e) {
            tx.rollback();
        }

        try {
            tx = session.beginTransaction();
            login.setEmail(employee.getEmail());
            login.setPassword(employee.getPassword());
            login.setUsername(employee.getUsername());
            session.save(login);
            tx.commit();
            session.flush();
        } catch (Exception e) {
            tx.rollback();
        }

        return "employeeInfo";

    }

    public String addProduct() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            products.setpDate(date);
            session.save(products);
            tx.commit();
            session.flush();
        } catch (Exception e) {
        }
        return "purchase";

    }

    public String adminLogin() {

        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        Session session = sessionFactory.openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            Query query = session.createQuery("from Admin  where username= :username and password= :password");
            query.setString("username", admin.getUsername());
            query.setString("password", admin.getPassword());
            List list = query.list();
            session.flush();
            if (list.size() == 1) {
                return "adminHome";
            } else {
                return "welcomePrimefaces";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String employeeLogin() {

        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        Session session = sessionFactory.openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            Query query = session.createQuery("from Login  where username= :username and password= :password");
            query.setString("username", login.getUsername());
            query.setString("password", login.getPassword());
            List list = query.list();
            session.flush();
            if (list.size() == 1) {
                return "home";
            } else {
                return "welcomePrimefaces";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Products> showProductsList() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;

        try {
            tx = session.beginTransaction();
            Query q = session.createQuery("from Products");
            List<Products> list = q.list();
            tx.commit();
            session.flush();
            sump = 0;
            sumq = 0;
            for (Products l : list) {
                sump += l.getPrice();
                sumq += l.getQty();
            }
            return list;
        } catch (Exception e) {
//            tx.rollback();
        }
        return null;
    }

    public String showProduct(Products p) {
        products.setId(p.getId());
        products.setName(p.getName());
        products.setModel(p.getModel());
        products.setColor(p.getColor());
        products.setPrice(p.getPrice());
        products.setQty(p.getQty());

        return null;
    }

    public String salesProduct() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        int aqty = products.getQty();
        products.setQty(aqty - sqty);
        products.setsDate(date);
        try {
            tx = session.beginTransaction();
            session.update(products);
            tx.commit();
            session.flush();
        } catch (Exception e) {
//            tx.rollback();
        }

        try {
            tx = session.beginTransaction();
            sales.setProductid(products.getId());
            sales.setName(products.getName());
            sales.setModel(products.getModel());
            sales.setColor(products.getColor());
            sales.setPrice(products.getPrice());
            sales.setQty(sqty);
            sales.setsDate(date);
            session.save(sales);
            tx.commit();
            session.flush();
        } catch (Exception e) {
//            tx.rollback();
        }
        return "sales";
    }

    public String addPurchase() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        int aqty = products.getQty();
        products.setQty(aqty + sqty);
        products.setpDate(date);
        try {
            tx = session.beginTransaction();
            session.update(products);
            tx.commit();
            session.flush();
        } catch (Exception e) {
//            tx.rollback();
        }

        try {
            tx = session.beginTransaction();
            purchase.setProductid(products.getId());
            purchase.setName(products.getName());
            purchase.setModel(products.getModel());
            purchase.setColor(products.getColor());
            purchase.setPrice(products.getPrice());
            purchase.setQty(sqty);
            purchase.setpDate(date);
            session.save(purchase);
            tx.commit();
            session.flush();
        } catch (Exception e) {
//            tx.rollback();
        }
        return "addPurchase";
    }

    public void salesReport() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            System.out.println(sDate);
            Query q = session.createQuery("from Sales where sDate=:sDate");
            q.setString("sDate", sDate);
            productList = q.list();
            session.flush();
        } catch (Exception e) {
//            tx.rollback();
        }
        
    }

    public List<Products> addNewProductReport() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;

        try {
            tx = session.beginTransaction();
            Query q = session.createQuery("from Products where pDate= :pDate");
            q.setString("pDate", pDate);
            List<Products> list = q.list();
            tx.commit();
            session.flush();
            sump = 0;
            sumq = 0;
            for (Products l : list) {
                sump += l.getPrice();
                sumq += l.getQty();
            }
            return list;
        } catch (Exception e) {
//            tx.rollback();
        }
        return null;
    }

    public void parchaseReport() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            Query q = session.createQuery("from Purchase where pDate=:pDate");
            q.setString("pDate", pDate);
            productList1 = q.list();
            session.flush();
        } catch (Exception e) {
//            tx.rollback();
        }

    }

    public List<Products> showEmployeeList() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;

        try {
            tx = session.beginTransaction();
            Query q = session.createQuery("from Employee");
            List<Products> list = q.list();
            tx.commit();
            session.flush();
            return list;
        } catch (Exception e) {
            tx.rollback();
        }
        return null;
    }

    public String updateEmp(Employee emp) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.update(employee);
            tx.commit();
            session.flush();
        } catch (Exception e) {
//            tx.rollback();
        }

        try {
            tx = session.beginTransaction();
            login.setEmail(emp.getEmail());
            login.setPassword(emp.getPassword());
            login.setUsername(emp.getUsername());
            session.update(login);
            tx.commit();
            session.flush();
        } catch (Exception e) {
            tx.rollback();
        }
        return "employeeInfo";

    }

    public String editEmp(Employee emp) {
        employee.setId(emp.getId());
        employee.setName(emp.getName());
        employee.setDesignation(emp.getDesignation());
        employee.setEmail(emp.getEmail());
        employee.setPhone(emp.getPhone());
        employee.setAddress(emp.getAddress());
        employee.setJoinDate(emp.getJoinDate());
        employee.setUsername(emp.getUsername());
        employee.setPassword(emp.getPassword());

        return "updateEmp";
    }

    public void deleteEmp(Employee emp) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.delete(emp);
            tx.commit();
            session.flush();
        } catch (Exception e) {
            session.getTransaction().rollback();
        }

    }

    public String getsDate() {
        return sDate;
    }

    public void setsDate(String sDate) {
        this.sDate = sDate;
    }

    public String getpDate() {
        return pDate;
    }

    public void setpDate(String pDate) {
        this.pDate = pDate;
    }

    public List<Products> getProductList() {
        return productList;
    }

    public void setProductList(List<Products> productList) {
        this.productList = productList;
    }

    public List<Products> getProductList1() {
        return productList1;
    }

    public void setProductList1(List<Products> productList1) {
        this.productList1 = productList1;
    }
    
    

}
