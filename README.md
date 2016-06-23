用 selenium 做 http get 和 post 请求


#### 思路
先用 phantom + selenium 模拟登陆，绕过小二工作台的证书。
然后用 selenium 执行 js 脚本，通过 ajax（XMLHttpRequest），发送 get he post 请求，获取 Jason 返回后，进行校验。




#### quick start：
1.clone 代码
2.将配置文件 resources/config.properties 里的 defaultUserName= defaultUserPassword= 换成自己的预账号和密码
3.运行 org.ChiTest.rc.Login.LoginTest#test01_do_post_and_get