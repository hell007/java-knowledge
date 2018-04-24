package com.self.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.gexin.rp.sdk.base.IPushResult;
import com.gexin.rp.sdk.base.impl.ListMessage;
import com.gexin.rp.sdk.base.impl.SingleMessage;
import com.gexin.rp.sdk.base.impl.Target;
import com.gexin.rp.sdk.base.payload.APNPayload;
import com.gexin.rp.sdk.base.payload.MultiMedia;
import com.gexin.rp.sdk.http.IGtPush;
import com.gexin.rp.sdk.template.LinkTemplate;
import com.gexin.rp.sdk.template.NotificationTemplate;
import com.gexin.rp.sdk.template.NotyPopLoadTemplate;
import com.gexin.rp.sdk.template.TransmissionTemplate;
import com.gexin.rp.sdk.template.style.Style0;

/**
 *  个推工具包
 * @author wzh
 * @time 
 */
public class AppPushUtils {
	//定义常量, appId、appKey、masterSecret  在"个推控制台"中获得的应用配置
	// 由IGetui管理页面生成，是您的应用与SDK通信的标识之一，每个应用都对应一个唯一的AppID
	private static String appId = "";
	// 预先分配的第三方应用对应的Key，是您的应用与SDK通信的标识之一。
	private static String appKey = "";
	// 个推服务端API鉴权码，用于验证调用方合法性。在调用个推服务端API时需要提供。（请妥善保管，避免通道被盗用）
	private static String masterSecret = "";
	
	// 构造器
	public AppPushUtils(String appId, String appKey, String masterSecret) {
		// 初始化类
		this.appId = appId;
		this.appKey = appKey;
		this.masterSecret = masterSecret;
	}
	
	 
	/*
	 * 设置通知消息模板
	 * 1. appId
	 * 2. appKey
	 * 3. 要传送到客户端的 msg
	 * 3.1 标题栏：key = title, 
	 * 3.2 通知栏内容： key = titleText,
	 * 3.3 穿透内容：key = transText 
	 */
	private static NotificationTemplate notificationTemplateDemo(String appId, String appKey, Map<String, String> msg){
		// 在通知栏显示一条含图标、标题等的通知，用户点击后激活您的应用
		NotificationTemplate template = new NotificationTemplate();
		// 设置appid，appkey
		template.setAppId(appId);
		template.setAppkey(appKey);
		// 穿透消息设置为，1 强制启动应用 2为等待应用启动
		template.setTransmissionType(1);
		// 设置穿透内容
		System.out.println(msg.get("title") + "::" + msg.get("titleText") + "::" + msg.get("transText"));
		template.setTransmissionContent(msg.get("transText"));
		// 设置style
		Style0 style = new Style0();
		// 设置通知栏标题和内容
		style.setTitle(msg.get("title"));
		style.setText(msg.get("titleText"));
		// 设置通知，响铃、震动、可清除
		style.setRing(true);
		style.setVibrate(true);
		style.setClearable(true);
		// 设置
		template.setStyle(style);
		
		return template;
	}
	
	/**
	 * 点击通知打开网页模板
	 * @param appId
	 * @param appKey
	 * @param msg
	 * @return
	 */
	public static LinkTemplate linkTemplateDemo(String appId, String appKey, Map<String, String> msg) {
	    LinkTemplate template = new LinkTemplate();
	    // 设置APPID与APPKEY
	    template.setAppId(appId);
	    template.setAppkey(appKey);

	    Style0 style = new Style0();
	    // 设置通知栏标题与内容
	    style.setTitle(msg.get("title"));
		style.setText(msg.get("titleText"));
	    // 配置通知栏图标
	    //style.setLogo("icon.png");
	    // 配置通知栏网络图标
	    //style.setLogoUrl("");
	    // 设置通知是否响铃，震动，或者可清除
	    style.setRing(true);
	    style.setVibrate(true);
	    style.setClearable(true);
	    template.setStyle(style);

	    // 设置打开的网址地址
	    template.setUrl("http://www.getui.com");
	    // 设置定时展示时间
	    // template.setDuration("2015-01-16 11:40:00", "2015-01-16 12:24:00");
	    return template;
	}
	
	/**
	 * 点击通知弹窗下载模板
	 * @param appId
	 * @param appKey
	 * @param msg
	 * @return
	 */
	public static NotyPopLoadTemplate notyPopLoadTemplateDemo(String appId, String appKey, Map<String, String> msg) {
		NotyPopLoadTemplate template = new NotyPopLoadTemplate();
	    // 设置APPID与APPKEY
	    template.setAppId(appId);
	    template.setAppkey(appKey);

	    Style0 style = new Style0();
	    // 设置通知栏标题与内容
	    style.setTitle(msg.get("title"));
		style.setText(msg.get("titleText"));
	    // 配置通知栏图标
	    //style.setLogo("icon.png");
	    // 配置通知栏网络图标
	    //style.setLogoUrl("");
	    // 设置通知是否响铃，震动，或者可清除
	    style.setRing(true);
	    style.setVibrate(true);
	    style.setClearable(true);
	    template.setStyle(style);

	    // 设置弹框标题与内容
	    template.setPopTitle("弹框标题");
	    template.setPopContent("弹框内容");
	    // 设置弹框显示的图片
	    template.setPopImage("");
	    template.setPopButton1("下载");
	    template.setPopButton2("取消");
	    // 设置下载标题
	    template.setLoadTitle("下载标题");
	    template.setLoadIcon("file://icon.png");
	    //设置下载地址        
	    template.setLoadUrl("http://gdown.baidu.com/data/wisegame/80bab73f82cc29bf/shoujibaidu_16788496.apk");
	    // 设置定时展示时间
	    // template.setDuration("2015-01-16 11:40:00", "2015-01-16 12:24:00");
	    return template;
	}
	
	/**
	 * 透传消息模版设置
	 * @param appId
	 * @param appKey
	 * @param msg
	 * @return
	 */
	public static TransmissionTemplate transmissionTemplateDemo(String appId, String appKey, Map<String, String> msg) {
	    TransmissionTemplate template = new TransmissionTemplate();
	    template.setAppId(appId);
	    template.setAppkey(appKey);
	    // 透传消息设置，1为强制启动应用，客户端接收到消息后就会立即启动应用；2为等待应用启动
	    template.setTransmissionType(2);
	    // 设置穿透内容
	 	//System.out.println(msg.get("title") + "::" + msg.get("titleText") + "::" + msg.get("transText"));	
	    template.setTransmissionContent(msg.get("transText"));
	    // 设置定时展示时间
	    // template.setDuration("2015-01-16 11:40:00", "2015-01-16 12:24:00");
	    return template;
	}
	
	
	/**
	 * iOS推送说明
	 * @param appId
	 * @param appKey
	 * @param msg
	 * @return
	 */
	public static TransmissionTemplate getTemplate(String appId, String appKey, Map<String, String> msg) {
	    TransmissionTemplate template = new TransmissionTemplate();
	    template.setAppId(appId);
	    template.setAppkey(appKey);
	    template.setTransmissionContent(msg.get("transText"));
	    template.setTransmissionType(2);
	    APNPayload payload = new APNPayload();
	    //在已有数字基础上加1显示，设置为-1时，在已有数字上减1显示，设置为数字时，显示指定数字
	    payload.setAutoBadge("+1");
	    payload.setContentAvailable(1);
	    payload.setSound("default");
	    payload.setCategory("$由客户端定义");

	    //简单模式APNPayload.SimpleMsg 
	    payload.setAlertMsg(new APNPayload.SimpleAlertMsg("hello"));

	    //字典模式使用APNPayload.DictionaryAlertMsg
	    //payload.setAlertMsg(getDictionaryAlertMsg());

	    // 添加多媒体资源
	    payload.addMultiMedia(new MultiMedia().setResType(MultiMedia.MediaType.video)
	                .setResUrl("http://ol5mrj259.bkt.clouddn.com/test2.mp4")
	                .setOnlyWifi(true));

	    template.setAPNInfo(payload);
	    return template;
	}
	private static APNPayload.DictionaryAlertMsg getDictionaryAlertMsg(){
	    APNPayload.DictionaryAlertMsg alertMsg = new APNPayload.DictionaryAlertMsg();
	    alertMsg.setBody("body");
	    alertMsg.setActionLocKey("ActionLockey");
	    alertMsg.setLocKey("LocKey");
	    alertMsg.addLocArg("loc-args");
	    alertMsg.setLaunchImage("launch-image");
	    // iOS8.2以上版本支持
	    alertMsg.setTitle("Title");
	    alertMsg.setTitleLocKey("TitleLocKey");
	    alertMsg.addTitleLocArg("TitleLocArg");
	    return alertMsg;
	}

	
	//使用
	
	// 对单个用户推送消息
	/*
	 * 1. cid
	 * 2. 要传到客户端的 msg
	 * 2.1 标题栏：key = title, 
	 * 2.2 通知栏内容： key = titleText,
	 * 2.3 穿透内容：key = transText 
	 */
	public IPushResult pushMsgToSingle(String cid, Map<String, String> msg) {
		// 代表在个推注册的一个 app，调用该类实例的方法来执行对个推的请求
		IGtPush push = new IGtPush(appKey, masterSecret);
		// 创建信息模板
		NotificationTemplate template = notificationTemplateDemo(appId, appKey, msg);
		
		//TransmissionTemplate template = transmissionTemplateDemo(appId, appKey, msg);
		//定义消息推送方式为，单推
		SingleMessage message = new SingleMessage();
		// 设置推送消息的内容
		message.setData(template);
		// 设置推送目标
		Target target = new Target();
		target.setAppId(appId);
		// 设置cid
		target.setClientId(cid);
		// 获得推送结果
		IPushResult result = push.pushMessageToSingle(message, target);
		/*
		 * 1. 失败：{result=sign_error}
		 * 2. 成功：{result=ok, taskId=OSS-0212_1b7578259b74972b2bba556bb12a9f9a, status=successed_online}
		 * 3. 异常
		 */
		return result;
	}
	
	
	
	public IPushResult pushMsgToList(String[] cids, Map<String, String> msg) {
		// 代表在个推注册的一个 app，调用该类实例的方法来执行对个推的请求
		IGtPush push = new IGtPush(appKey, masterSecret);
		// 创建信息模板
		NotificationTemplate template = notificationTemplateDemo(appId, appKey, msg);
		
		//TransmissionTemplate template = transmissionTemplateDemo(appId, appKey, msg);
		        
		//定义消息推送方式为，多推
        ListMessage message = new ListMessage();
        message.setData(template);
        // 设置消息离线，并设置离线时间
        message.setOffline(true);
        // 离线有效时间，单位为毫秒，可选
        message.setOfflineExpireTime(24 * 1000 * 3600);
        // 配置推送目标
        List<Target> targets = new ArrayList<Target>();
        
        for(int i=0,len=cids.length; i<len; i++) {
        	System.out.println(cids[i]);
        	Target targetTemp = new Target();
        	targetTemp.setAppId(appId);
        	targetTemp.setClientId(cids[i]);
        	targets.add(targetTemp);
        	//targetTemp.setAlias(Alias1);
        }
        // taskId用于在推送时去查找对应的message
        String taskId = push.getContentId(message);
        IPushResult result = push.pushMessageToList(taskId, targets);
        
		return result;
	}
	
	
	
	@RequestMapping(value = "getui")
	public String getui(Model model) {
		String appId = "8xO68VhE097nrb7iLtOEL2";  
        String appKey = "Enn8AUi93c75DHdOrJk1V4";  
        String masterSecret = "LfvK0oXVbrA2h15UOumGb3";  
          
        Map<String,String> msg = new HashMap<String, String>();  
        msg.put("title", "个推测试");  
        msg.put("titleText", "个推中");  
        //msg.put("transText", "{title:'通知标题',content:'通知内容',payload:'通知去干嘛这里可以自定义'}");  
        msg.put("transText", "{payload:'通知去干嘛这里可以自定义'}"); 
          //c23b79ca51cdd30b9383a5ba37f8894e "bbf41a62e592df0cc2b468338abc0018","b69ebb023fab52414b15bf9991814d92","a5e7a3e5c741d28a20b1aa08ac782b38"
        String[] cids = {"590e8e1327276ce39f74e5fe898535b0"};  
          
        AppPushUtils pushUtils = new AppPushUtils(appId, appKey, masterSecret); 
        
        //sigle
//        for(String cid : cids) {  
//            System.out.println("正在发送消息...");  
//            IPushResult ret =  pushUtils.pushMsgToSingle(cid, msg);  
//            System.out.println(ret.getResponse().toString());  
//        } 
         
        //list
        @SuppressWarnings("unused")
		IPushResult ret =  pushUtils.pushMsgToList(cids, msg);
        
        return "test/getui";
	}

	        
	    
	
	
	
}
