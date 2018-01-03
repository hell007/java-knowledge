import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Scanner;

import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;

/**
 * 双向串行通信类
 * @author 都颜汗
 *
 */
public class MyTwoWaySerialComm {

	/**
	 * 端口连接
	 * @param portName
	 * @throws Exception 
	 */
	void connect(String portName) throws Exception {
		
		// 通过端口名获取端口标志对象
		CommPortIdentifier portIdentifier = CommPortIdentifier.getPortIdentifier(portName);
		
		// 检测当前端口是否已有所属(即检测是否被占用)
		boolean isOwned = portIdentifier.isCurrentlyOwned();
		
		// 若已经被占用
		if(isOwned)
		{
			System.out.println("当前端口已经被占用");
		}
		// 若当前端口可用
		else {
			// 打开通信端口 （注意：这里使用应用名，和打开超时时间，单位ms），打开后获取通信端口对象
			CommPort commPort = portIdentifier.open(this.getClass().getName(), 2000);
			
			// 判断当前这个端口对象commPort是否是串行端口类的对象,注意：这里如果没有获取到commPort对象    一样会返回false
			if(commPort instanceof SerialPort)
			{
				// 如果是串行端口（电脑上除了串行端口，还有并行端口），则转换成串行端口对象
				SerialPort serialPort = (SerialPort)commPort;
				// 设置端口参数(波特率，数据位，停止位，奇偶性[校验])
				serialPort.setSerialPortParams(9600, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
				
				// 获取当前串口(串行端口)的输入输出流
				InputStream is = serialPort.getInputStream();
				OutputStream os = serialPort.getOutputStream();
				
				// 利用上面的输入输出流创建两个单独的线程进行报文的传送与接收
				(new Thread(new SerialReadThreadBody(is))).start();
				(new Thread(new SerialWriteThreadBody(os))).start();
				
			}
			// 否则当前端口不是串行端口，或者串口打开失败
			else {
				System.out.println("当前端口不是串行端口，或发生未知错误，导致串口打开失败！");
			}
			
		}
	}
	
	/**
	 * 创建一个串读线程体内部类
	 * 作用：从设备读报文到上位机
	 * @author 都颜汗
	 *
	 */
	public class SerialReadThreadBody implements Runnable {

		private InputStream is;
		
		// 创建一个构造，用于获取输入流对象
		public SerialReadThreadBody(InputStream is) {
			this.is = is;
		}
		
		@Override
		public void run() {
			// 操作：从输入流中读取报文，传给上位机，显示出来
			byte[] buf = new byte[1024];
			int len = -1;
			try {
				String temperatureHexStr = "";
				while((len = this.is.read(buf)) > -1)
				{
					if(len == 1)
						System.out.println("当前默认地址：" + SerialCommUtils.byte2HexStr(buf[0]));
					else
						for(int i = 0; i < len; i++)
						{
							System.out.print(SerialCommUtils.byte2HexStr(buf[i]) + " ");
							if(i == 2 || i == 3)
							{
								temperatureHexStr += SerialCommUtils.byte2HexStr(buf[i]);
							}
							if(i == len-1)
							{
								// 打印温度
								System.out.println("\n当前获取到的温度是：" + SerialCommUtils.hexStr2Integer(temperatureHexStr)*0.1 + "°C");
							}
						}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
		
	}
	
	/**
	 * 创建一个串写线程体内部类
	 * 作用：从上位机写报文到设备
	 * @author 都颜汗
	 *
	 */
	public class SerialWriteThreadBody implements Runnable {

		OutputStream os;
		
		public SerialWriteThreadBody(OutputStream os) {
			this.os = os;
		}
		
		// 0 3 0 0 0 2 197 218
		
		@Override
		public void run() {
			// 操作：从输出流中将报文传给设备，然后设备做出响应，单独通过串读线程将结果返回给上位机
			try {
				Scanner sc = new Scanner(System.in);
				String input = "";
				while(!(input = sc.next().trim()).equals(""))
				{
					this.os.write(SerialCommUtils.hexStr2Integer(input));
				}
				
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
		
	}
	
	public static void main (String[] args)
	{
		try {
			new MyTwoWaySerialComm().connect("COM4");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
