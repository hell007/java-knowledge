/**
 * 串行通信工具类
 * @author 都颜汗
 *
 */
public class SerialCommUtils {

	
	/**
	 * 将一个十六进制字符串转换成一个十进制数值
	 * @param hexStr 十六进制字符串，每个字符串包含两个十六进制字符，每个字符串表示一个字节
	 * @return 十进制整数
	 */
	public static int hexStr2Integer(String hexStr) {
		// 先获取字符串长度
		int len = hexStr.length();
		int sum = 0;
		// 从高位到低位挨个儿计算
		for(int i = 0; i < len; i++)
		{
			// 获取当前位的十六进制数对应的十进制值
			int curNum = SerialCommUtils.hexChar2Integer(hexStr.charAt(i));
			sum += curNum * Math.pow(16, len-i-1);
		}
		return sum;
	}
	
	/**
	 * 将一个十六进制字符转换成一个十进制数值
	 * @param c  一个十六进制字符
	 * @return c这个十六进制字符对应的十进制整数
	 */
	public static int hexChar2Integer(char c){
		
		if(c >= '0' && c <= '9')
		{
			return c-48;
		}
		else if((c >= 'a' && c <= 'f') || (c >= 'A' && c <= 'F'))
		{
			if(c >= 'a')
				return c-87;
			else
				return c-55;
		}
		
		return -1;
	}
	
	/**
	 * 将一个十进制数值转成一个十六进制字符
	 * @param n 是一个十进制数值，合理范围为0~15
	 * @return	返回一个十六进制字符
	 */
	public static char int2HexChar(int n)
	{
		if(n <=9 && n >= 0)
		{
			return (char)(n+48);
		}
		else if(n > 9 && n <= 15)
		{
			switch (n) {
				case 10: return 'A'; 
				case 11: return 'B'; 
				case 12: return 'C'; 
				case 13: return 'D'; 
				case 14: return 'E'; 
				case 15: return 'F'; 
			default:
				break;
			}
		}
		
		return '■';
	}
	
	/**
	 * 将一个字节转换成一个字符串
	 * @param n  是待转换的字节
	 * @return	返回一个包含两个十六进制字符的字符串
	 */
	public static String byte2HexStr(byte b)
	{
		// 首先，从设备返回的字节可能是个负值，先将其转换成一个不带符号的正值，放到int中保存
		int tmp = b & 0xFF; // 这里利用按位与操作
		int before = tmp/16;
		int after = tmp%16;
		String ret = "" + SerialCommUtils.int2HexChar(before) + SerialCommUtils.int2HexChar(after);
		return ret;
	}
}
