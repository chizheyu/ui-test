
1.将项目目录下的 apache-ant-1.9.4-bin.rar 解压缩到

2.修改 allBuild.xml
打开 xml 找到
`<property name="browser" location="C:/Program Files (x86)/Google/Chrome/Application/chrome.exe"/>`

将 location 改成自己电脑的浏览器路径

3.这是存放错误截图的路径统一为 "/target/classes/screenFile"

4.点击 runAllTest.bat 

项目跑完后，会在浏览器上打开 junit 报告


配置文件（ src\main\resources\config.properties ）里面有个 option 选项，这是用来选择测试平台的，选"1"是 staging, "2" 是 coding.net, "3" 是 134 本地服务器.

还要将配置文件中的 localBaseUrl 和 localLoginUrl 改成 http://127.0.0.1/ 或 http://coding.com/ 根据自己本地的url来 不要忘了结尾的"/"
