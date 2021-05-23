# JWG
计算机网络管理课程作业+课程设计
## 课程作业
利用Apache Netty实现一个客户端和服务器：
1.	客户端（Netty client实现）
（1）从不同网站进行爬虫（动态或静态爬虫、英文网站）
（2）向Apache Kafka队列中传送文本和URL信息
2.	服务器（Netty client实现）
（1）从Apache Kafka队列中接收文本和URL信息
	（2）将接收到的信息保存于Redis中
## 课程设计
课程设计在课程实验的基础上，利用Apache Storm进行。
1. 采用Apache Storm对课程实验网络爬虫获取的HTML文本信息进行字频统计、词频统计、句频统计；
2. 在Apache Storm中分别使用Redis和Apache Kafka作为数据源（Spout），并比较吞吐和时延的指标；
3. 注意Apache Storm的并行度划分；
4. 最终结果存入Redis中。
