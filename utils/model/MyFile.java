package com.self.model;

import java.util.List;

/**
 * 定义一个MyFile model,用于FileUtils.getFileList使用
 * @author wzh
 * @2018-10-16
 */
public class MyFile {
	
	private String name;
	
	private List<String> list;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<String> getList() {
		return list;
	}

	public void setList(List<String> list) {
		this.list = list;
	}

	public MyFile() {
		super();
	}

	public MyFile(String name, List<String> list) {
		super();
		this.name = name;
		this.list = list;
	}

	@Override
	public String toString() {
		return "MyFile [name=" + name + ", list=" + list + "]";
	}
	
}
