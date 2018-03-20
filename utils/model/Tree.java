package com.jie.model;

import java.util.List;

public class Tree {

    private String id;

    private String pid;

    private String name;
    
    private String url;
    
    private String code;

    private List<Tree> children;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public List<Tree> getChildren() {
		return children;
	}

	public void setChildren(List<Tree> children) {
		this.children = children;
	}

	
	public Tree(String id, String name, String pid) {
		this.id = id;
		this.name = name;
		this.pid = pid;
	}
	
	public Tree(String id, String name, String url, String code, String pid) {
		this.id = id;
		this.name = name;
		this.url = url;
		this.code = code;
		this.pid = pid;
	}
	
	public Tree(String id, String name, Tree parent) {  
        this.id = id;  
        this.pid = parent.getId();  
        this.name = name; 
    } 
	
	public Tree(String id, String name,  String url, String code, Tree parent) {  
        this.id = id;  
        this.pid = parent.getId();  
        this.name = name; 
        this.url = url;
		this.code = code;
    }
	
	public Tree() {
		super();
	}

	@Override
	public String toString() {
		return "Tree [id=" + id + ", pid=" + pid + ", name=" + name + ", url="
				+ url + ", code=" + code + ", children=" + children + "]";
	}

	
}
