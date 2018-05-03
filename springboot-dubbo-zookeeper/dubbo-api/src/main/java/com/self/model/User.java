package com.self.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Table(name = "self_user")
public class User implements Serializable{
    /**
     * 用户ID
     */
    @Id
    @SequenceGenerator(name="",sequenceName="SELECT LAST_INSERT_ID()")
    private String id;

    /**
     * 用户名
     */
    private String name;

    /**
     * 密码
     */
    private String passwd;

    /**
     * 性别
     */
    private String gender;

    private String phone;

    /**
     * 用户头像
     */
    private String header;

    /**
     * 登陆IP
     */
    private String ip;

    /**
     * 状态
     */
    private Boolean status;

    /**
     * 禁用原因
     */
    private String reason;

    private String salt;

    private Date created;

    @Column(name = "lastLogin")
    private Date lastlogin;

    public User(String id, String name, String passwd, String gender, String phone, String header, String ip, Boolean status, String reason, String salt, Date created, Date lastlogin) {
        this.id = id;
        this.name = name;
        this.passwd = passwd;
        this.gender = gender;
        this.phone = phone;
        this.header = header;
        this.ip = ip;
        this.status = status;
        this.reason = reason;
        this.salt = salt;
        this.created = created;
        this.lastlogin = lastlogin;
    }

    public User() {
        super();
    }

    /**
     * 获取用户ID
     *
     * @return id - 用户ID
     */
    public String getId() {
        return id;
    }

    /**
     * 设置用户ID
     *
     * @param id 用户ID
     */
    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    /**
     * 获取用户名
     *
     * @return name - 用户名
     */
    public String getName() {
        return name;
    }

    /**
     * 设置用户名
     *
     * @param name 用户名
     */
    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    /**
     * 获取密码
     *
     * @return passwd - 密码
     */
    public String getPasswd() {
        return passwd;
    }

    /**
     * 设置密码
     *
     * @param passwd 密码
     */
    public void setPasswd(String passwd) {
        this.passwd = passwd == null ? null : passwd.trim();
    }

    /**
     * 获取性别
     *
     * @return gender - 性别
     */
    public String getGender() {
        return gender;
    }

    /**
     * 设置性别
     *
     * @param gender 性别
     */
    public void setGender(String gender) {
        this.gender = gender == null ? null : gender.trim();
    }

    /**
     * @return phone
     */
    public String getPhone() {
        return phone;
    }

    /**
     * @param phone
     */
    public void setPhone(String phone) {
        this.phone = phone == null ? null : phone.trim();
    }

    /**
     * 获取用户头像
     *
     * @return header - 用户头像
     */
    public String getHeader() {
        return header;
    }

    /**
     * 设置用户头像
     *
     * @param header 用户头像
     */
    public void setHeader(String header) {
        this.header = header == null ? null : header.trim();
    }

    /**
     * 获取登陆IP
     *
     * @return ip - 登陆IP
     */
    public String getIp() {
        return ip;
    }

    /**
     * 设置登陆IP
     *
     * @param ip 登陆IP
     */
    public void setIp(String ip) {
        this.ip = ip == null ? null : ip.trim();
    }

    /**
     * 获取状态
     *
     * @return status - 状态
     */
    public Boolean getStatus() {
        return status;
    }

    /**
     * 设置状态
     *
     * @param status 状态
     */
    public void setStatus(Boolean status) {
        this.status = status;
    }

    /**
     * 获取禁用原因
     *
     * @return reason - 禁用原因
     */
    public String getReason() {
        return reason;
    }

    /**
     * 设置禁用原因
     *
     * @param reason 禁用原因
     */
    public void setReason(String reason) {
        this.reason = reason == null ? null : reason.trim();
    }

    /**
     * @return salt
     */
    public String getSalt() {
        return salt;
    }

    /**
     * @param salt
     */
    public void setSalt(String salt) {
        this.salt = salt == null ? null : salt.trim();
    }

    /**
     * @return created
     */
    public Date getCreated() {
        return created;
    }

    /**
     * @param created
     */
    public void setCreated(Date created) {
        this.created = created;
    }

    /**
     * @return lastLogin
     */
    public Date getLastlogin() {
        return lastlogin;
    }

    /**
     * @param lastlogin
     */
    public void setLastlogin(Date lastlogin) {
        this.lastlogin = lastlogin;
    }
}
