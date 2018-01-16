package com.self.pojo;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "ea_employee")
public class Employee implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id  
    @GeneratedValue(strategy = GenerationType.IDENTITY)  
    @GenericGenerator(name = "id", strategy = "id")  
    @Column(name = "ID", columnDefinition="INT UNSIGNED UNIQUE" )  
    private int id;//员工ID 
	
	@Column(name = "NAME", length = 20, unique = true, nullable = false)  
    private String name;//员工姓名   
	
	@Column(name = "PASSWORD", columnDefinition="CHAR(32) NOT NULL")  
    private String password;//员工密码
	
	@Column(name = "ROLEID", columnDefinition="INT UNSIGNED NOT NULL")  
    private int roleId;//角色
	
	@Column(name = "SEX", length = 2, nullable = false)  
    private String sex;//员工性别
	
	@Column(name = "PHONE", length = 13, unique = true, nullable = false)  
    private String phone;//员工电话
	
	@Column(name = "EMAIL", length = 50, unique = true, nullable = false)  
    private String email;//员工邮箱
	
	@Column(name = "ADDTIME", nullable = true)  
    private Date addTime;//添加时间

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int getRoleId() {
		return roleId;
	}

	public void setRoleId(int roleId) {
		this.roleId = roleId;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Date getAddTime() {
		return addTime;
	}

	public void setAddTime(Date addTime) {
		this.addTime = addTime;
	}

	@Override
	public String toString() {
		return "Employee [id=" + id + ", name=" + name
				+ ", password=" + password + ", roleId=" + roleId + ", sex="
				+ sex + ", phone=" + phone + ", email=" + email + ", addTime="
				+ addTime + "]";
	}
	
	
}

