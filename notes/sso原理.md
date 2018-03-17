## 单点登录原理

/**
 user <=>  sso  全局会话
 system <=> user 局部会话
 system <=> sso 局部会话
 
user -> system1(protected->unlogin?地址) -> sso(validate->unlogin) -> loginPage &&
signin-> [sso(validate->token) ->system1(token) -> sso(validate->ok)] -> pass

user -> system2(protected->unlogin?地址) -> sso(validate->logined) -> 
[system2(token) -> sso(validate->ok)] -> pass


String callbackURL = request.getRequestURL().toString();
StringBuilder url = new StringBuilder();
url.append(SSO_SERVER_URL).append("?callbackURL=").append(callbackURL);
response.sendRedirect(url.toString());
**/

/**
简单描述：
用户访问系统1的受保护资源，系统1发现用户未登录，跳转至sso认证中心，并将自己的地址作为参数
sso认证中心发现用户未登录，将用户引导至登录页面

 用户输入用户名密码提交登录申请
sso认证中心校验用户信息，创建用户与sso认证中心之间的会话，称为全局会话，同时创建授权令牌
sso认证中心带着令牌跳转会最初的请求地址（系统1）
系统1拿到令牌，去sso认证中心校验令牌是否有效
sso认证中心校验令牌，返回有效，注册系统1
系统1使用该令牌创建与用户的会话，称为局部会话，返回受保护资源

用户访问系统2的受保护资源
系统2发现用户未登录，跳转至sso认证中心，并将自己的地址作为参数
sso认证中心发现用户已登录，跳转回系统2的地址，并附上令牌
系统2拿到令牌，去sso认证中心校验令牌是否有效
sso认证中心校验令牌，返回有效，注册系统2
系统2使用该令牌创建与用户的局部会话，返回受保护资源
 */


http://blog.csdn.net/xqhys/article/details/79008531
