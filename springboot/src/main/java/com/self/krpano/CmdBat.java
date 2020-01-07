package com.self.krpano;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class CmdBat {
	
	/**
	 * main
	 * @param args
	 */
	public static void main(String[] args) {
		Room r = new Room();
		String dpath = "D:\\Eclipse\\mine\\myself\\src\\main\\resources\\static\\kr";
		String file = "demo";
		String[] fn1 = { "111","222","333" };
		String[] fn2 = { "客厅", "卧室","大客厅" };
		String title = "krpano-demo展示";
		String music = "static/kr/music/default.mp3";
		try {
			setKrpano(r,dpath, file, fn1, fn2, title, music);
		} catch (InterruptedException e) {
			e.printStackTrace();
			System.out.println("上传失败");
		}
	}
	
	/**
	 * 使用cmd命名生成krpano demo文件到指定 web server目录
	 * @param r
	 * @param dpath
	 * @param file
	 * @param fn1
	 * @param fn2
	 * @param title
	 * @param music
	 * @throws InterruptedException
	 */
	public static void setKrpano(final Room r,
			final String dpath, 
			final String file,
			final String[] fn1, 
			final String[] fn2, 
			final String title,
			final String music)
			throws InterruptedException {
		//素材文件目录
		final String temppath = "D:\\Dev\\temp-room\\";
		String path = temppath+file;
		String ex = "krpanotools64.exe makepano -config=templates\\vtour-multires.config "
				+ path + "\\*.jpg";
		Runtime runtime = Runtime.getRuntime();
		boolean b = true;
		Process p = null;
		try {
			System.out.println("ex=="+ex);
			p = runtime.exec("cmd /c start d:\\Dev\\krpano-1.19-pr16\\" + ex);
		} catch (Exception e) {
			b = false;
		}
		//无异常，继续操作
		if (b) {
			//System.out.println("b==="+b);
			final InputStream is1 = p.getInputStream();
			final InputStream is2 = p.getErrorStream();
			
			//线程操作
			new Thread() {
				public void run() {
					BufferedReader br1 = new BufferedReader(new InputStreamReader(is1));
					try {
						String line1 = null;
						while ((line1 = br1.readLine()) != null) {
							if (line1 != null) {
								System.out.println("br1==========line1======"+ line1);
							}
						}
					} catch (IOException e) {
						e.printStackTrace();
					} finally {
						try {
							is1.close();
							// 执行文件复制
							File f = new File(dpath + "\\" + file);
							f.mkdirs();// 创建目录
							// 复制文件
							boolean b3 = copyFile(temppath
									+ file + "\\vtour\\tour.xml", dpath
									+ "\\" + file + "\\tour.xml");
							if (b3) {
								// 复制文件夹
								boolean b4 = copyFolder(
										temppath + file
												+ "\\vtour\\panos",
										dpath + "\\" + file + "\\panos");
								if (b4) {
									// 删除临时生成文件
									//delFolder(temppath + file);
									// 修改krpano文件内容
									String xmlPath = dpath + "\\"
											+ file + "\\tour.xml";
									File xmlFile = new File(xmlPath);
									DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
									DocumentBuilder dBuilder;
									try {
										dBuilder = dbFactory.newDocumentBuilder();
										Document doc = dBuilder.parse(xmlFile);
										doc.getDocumentElement().normalize();
										for (int i = 0; i < fn1.length; i++) {
											updateAttributeValue(doc, fn1[i], fn2[i]);
										}

										// update Element value
										updateElementValue(doc, title);

										// delete element
										deleteElement(doc);

										// add new element
										addElement(doc);

										//updateAttributeColorValue(doc, "0x000000");
										//addMusicElement(doc,music);
										// write the updated document to file or console
										doc.getDocumentElement().normalize();
										TransformerFactory transformerFactory = TransformerFactory.newInstance();
										Transformer transformer = transformerFactory.newTransformer();
										DOMSource source = new DOMSource(doc);
										StreamResult result = new StreamResult(new File(xmlPath));
										transformer.setOutputProperty(OutputKeys.INDENT, "yes");
										transformer.transform(source, result);
										//生成成功
										r.setMark("1");
										//AdminService as = ContextUtil.getBean(AdminService.class, "adminService");
										//as.updateRoom(r);
										/*System.out.println("XML file updated successfully");*/

									} catch (
											SAXException
											| ParserConfigurationException
											| IOException
											| TransformerException e1) {
										e1.printStackTrace();
										//生成失败
										r.setMark("2");
										//AdminService as = ContextUtil.getBean(AdminService.class, "adminService");
										//as.updateRoom(r);
									}

								}
							}

						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
			}.start();
			
			//线程操作
			new Thread() {
				public void run() {
					BufferedReader br2 = new BufferedReader(
							new InputStreamReader(is2));
					try {
						String line2 = null;
						while ((line2 = br2.readLine()) != null) {
							if (line2 != null) {
								System.out.println("br2==========line2======"+ line2);
							}
						}
					} catch (IOException e) {
						e.printStackTrace();
					} finally {
						try {
							is2.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
			}.start();
			
			p.waitFor();
			p.destroy();
		} else {
			System.out.println("setKrpano==自动化生成失败！");
		}
	}
	
	/**
	 * 复制单个文件
	 * @param oldPath 原文件路径 如：c:/fqf.txt
	 * @param newPath 复制后路径 如：f:/fqf.txt
	 * @return
	 */
	public static boolean copyFile(String oldPath, String newPath) {
		try {
			int bytesum = 0;
			int byteread = 0;
			File oldfile = new File(oldPath);
			if (oldfile.exists()) { // 文件存在时
				InputStream inStream = new FileInputStream(oldPath); // 读入原文件
				FileOutputStream fs = new FileOutputStream(newPath);
				byte[] buffer = new byte[1444];
				int length;
				while ((byteread = inStream.read(buffer)) != -1) {
					bytesum += byteread; // 字节数 文件大小
					// System.out.println(bytesum);
					fs.write(buffer, 0, byteread);
				}
				inStream.close();
			}
		} catch (Exception e) {
			// System.out.println("复制单个文件操作出错");
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * 复制整个文件夹内容
	 * @param oldPath 原文件路径 如：c:/fqf
	 * @param newPath 复制后路径 如：f:/fqf/ff
	 * @return
	 */
	public static boolean copyFolder(String oldPath, String newPath) {
		try {
			(new File(newPath)).mkdirs(); // 如果文件夹不存在 则建立新文件夹
			File a = new File(oldPath);
			String[] file = a.list();
			File temp = null;
			for (int i = 0; i < file.length; i++) {
				if (oldPath.endsWith(File.separator)) {
					temp = new File(oldPath + file[i]);
				} else {
					temp = new File(oldPath + File.separator + file[i]);
				}

				if (temp.isFile()) {
					FileInputStream input = new FileInputStream(temp);
					FileOutputStream output = new FileOutputStream(newPath
							+ "/" + (temp.getName()).toString());
					byte[] b = new byte[1024 * 5];
					int len;
					while ((len = input.read(b)) != -1) {
						output.write(b, 0, len);
					}
					output.flush();
					output.close();
					input.close();
				}
				if (temp.isDirectory()) {// 如果是子文件夹
					copyFolder(oldPath + "/" + file[i], newPath + "/" + file[i]);
				}
			}
		} catch (Exception e) {
			// System.out.println("复制整个文件夹内容操作出错");
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * 删除文件夹
	 * @param folderPath
	 */
	public static void delFolder(String folderPath) {
		try {
			delAllFile(folderPath); // 删除完里面所有内容
			String filePath = folderPath;
			filePath = filePath.toString();
			java.io.File myFilePath = new java.io.File(filePath);
			myFilePath.delete(); // 删除空文件夹
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static boolean delAllFile(String path) {
		boolean flag = false;
		File file = new File(path);
		if (!file.exists()) {
			return flag;
		}
		if (!file.isDirectory()) {
			return flag;
		}
		String[] tempList = file.list();
		File temp = null;
		for (int i = 0; i < tempList.length; i++) {
			if (path.endsWith(File.separator)) {
				temp = new File(path + tempList[i]);
			} else {
				temp = new File(path + File.separator + tempList[i]);
			}
			if (temp.isFile()) {
				temp.delete();
			}
			if (temp.isDirectory()) {
				delAllFile(path + "/" + tempList[i]);// 先删除文件夹里面的文件
				delFolder(path + "/" + tempList[i]);// 再删除空文件夹
				flag = true;
			}
		}
		return flag;
	}

	private static void addElement(Document doc) {
		NodeList employees = doc.getElementsByTagName("krpano");
		Element emp = null;
		
		// loop for each employee
		for (int i = 0; i < employees.getLength(); i++) {
			emp = (Element) employees.item(i);
			Element contextmenu = doc.createElement("include");
			contextmenu.setAttribute("url", "./contextmenu.xml");
			emp.appendChild(contextmenu);
			Element music = doc.createElement("include");
			music.setAttribute("url", "./music.xml");
			emp.appendChild(music);
			Element video = doc.createElement("include");
			video.setAttribute("url", "./video.xml");
			emp.appendChild(video);
			Element vtourskin = doc.createElement("include");
			vtourskin.setAttribute("url", "./vtourskin.xml");
			emp.appendChild(vtourskin);	
		}
	}
	
	private static void addMusicElement(Document doc,String music) {
		NodeList employees = doc.getElementsByTagName("krpano");
		Element emp = null;

		// loop for each employee
		for (int i = 0; i < employees.getLength(); i++) {
			emp = (Element) employees.item(i);
			Element musicEl = doc.createElement("action");
			musicEl.setAttribute("name", "bgsnd_action");
			musicEl.setAttribute("autorun", "onstart");
			musicEl.appendChild(doc.createTextNode("playsound(bgsnd, '"+music+"', 0);"));
			emp.appendChild(musicEl);
		}
	}
	
	private static void deleteElement(Document doc) {
		NodeList employees = doc.getElementsByTagName("krpano");
		Element emp = null;
		// loop for each employee
		for (int i = 0; i < employees.getLength(); i++) {
			emp = (Element) employees.item(i);
			Node genderNode = emp.getElementsByTagName("include").item(0);
			emp.removeChild(genderNode);
		}
	}

	private static void updateElementValue(Document doc, String title) {
		NodeList employees = doc.getElementsByTagName("krpano");
		Element emp = null;
		// loop for each employee
		for (int i = 0; i < employees.getLength(); i++) {
			emp = (Element) employees.item(i);
			emp.setAttribute("title", title);
		}
	}

	private static void updateAttributeValue(Document doc, String oldname, String newname) {
		NodeList employees = doc.getElementsByTagName("scene");
		Element emp = null;
		// loop for each employee
		for (int i = 0; i < employees.getLength(); i++) {
			emp = (Element) employees.item(i);
			if (emp.getAttribute("title").equals(oldname)) {
				emp.setAttribute("title", newname);
				break;
			}
		}
	}

	private static void updateAttributeColorValue(Document doc, String newname) {
		NodeList employees = doc.getElementsByTagName("skin_settings");
		Element emp = null;
		// loop for each employee
		for (int i = 0; i < employees.getLength(); i++) {
			emp = (Element) employees.item(i);
			emp.setAttribute("design_bgcolor", newname);
			emp.setAttribute("design_bgalpha", "0.8");
		}
	}
	
	
}
