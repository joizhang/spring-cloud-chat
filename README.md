# spring-cloud-chat

## 系统说明

- 权限管理系统基于 [pig](https://github.com/pig-mesh/pig)
- 聊天应用基于 Vue 3 + TypeScript，仿[WhatsApp](https://web.whatsapp.com/)风格

## 核心功能

- [x] 离线消息保存
- [x] 一对一聊天
- [x] 基于Websocket的消息推送
- [x] 基于一致性哈希的分布式消息服务器
- [ ] 群组聊天(最多100人)
- [ ] 多设备支持，同一个账号可以同时登录多个设备。

## 快速开始

### 核心依赖

| 依赖                          | 版本         |
|-----------------------------|------------|
| Spring Boot                 | 2.7.10     |
| Spring Cloud                | 2021.0.6   |
| Spring Cloud Alibaba        | 2021.0.5.0 |
| Spring Authorization Server | 0.4.1      |
| Mybatis Plus                | 3.5.3.1    |
| hutool                      | 5.8.17     |

### 模块说明

```text
spring-cloud-chat
├── chat-auth -- 授权服务提供[3000]
└── chat-common -- 系统公共模块
     ├── chat-common-bom -- 全局依赖管理控制
     ├── chat-common-core -- 公共工具类核心包
     ├── chat-common-datasource -- 动态数据源包
     ├── chat-common-log -- 日志服务
     ├── chat-common-mybatis -- mybatis 扩展封装
     ├── chat-common-security -- 安全工具类
     ├── chat-common-swagger -- 接口文档
     ├── chat-common-feign -- feign 扩展封装
├── chat-gateway -- Spring Cloud Gateway网关[9999]
└── chat-upms -- 通用用户权限管理模块
     ├── chat-upms-api -- 通用用户权限管理系统公共api模块
     ├── chat-upms-biz -- 通用用户权限管理系统业务处理模块[4000]
     └── chat-codegen -- 图形化代码生成 [5002]
└── chat-visual
     └── chat-monitor -- 服务监控 [5001]
└── chat-web -- 客户聊天模块
     ├── chat-upms-api -- 客户聊天api模块
     └── chat-upms-biz -- 客户聊天业务处理模块[4001]
```

### 前端页面

chat-ui -- https://github.com/joizhang/chat-ui

## 参考

### 基于 `@EnableWebSocket`

- [Spring WebSocket](https://zetcode.com/spring/websocket/)
- [【WEB系列】springboot + websocket初体验](https://spring.hhui.top/spring-blog/2019/04/21/190421-SpringBoot%E9%AB%98%E7%BA%A7%E7%AF%87WEB%E4%B9%8Bwebsocket%E7%9A%84%E4%BD%BF%E7%94%A8%E8%AF%B4%E6%98%8E/)

### 基于 `@EnableWebSocketMessageBroker`

- [SpringBoot 实现 Websocket 通信详解](http://www.mydlq.club/article/86/)
- [Chat application using Spring WebSockets](https://github.com/salmar/spring-websocket-chat)

### 基于 `ServerEndpointExporter`

- [Spring Boot 精选课程](https://github.com/ityouknow/spring-boot-leaning/tree/master/2.x_42_courses)
- [springboot整合websocket](https://github.com/Tellsea/springboot-learn/tree/master/springboot-websocket)
- [websocket消息推送，单独发送信息，群发信息](https://github.com/niezhiliang/springbootwebsocket)
- [基于 Spring Boot 2.1.9 、 Jpa、 Spring Security、redis、Vue的前后端分离的后台管理系统](https://github.com/perye/dokit)
- [SpringBootLearning，SpringCloudStudy，学习SpringBoot2/SpringCloud2的项目](https://github.com/moshowgame/spring-cloud-study)

### 基于 Netty

- [基于Netty高可用分布式即时通讯系统](https://github.com/zhangyaoo/fastim)
- [互联网实时聊天系统 (Spring + Netty + Websocket)](https://github.com/Kanarienvogels/Chatroom)
- [开源的H5即时聊天系统 spring-boot + netty + protobuf + vue ~](https://github.com/lmxdawn/him-netty)

### 相关资料

- [Design A Chat System](https://bytebytego.com/courses/system-design-interview/design-a-chat-system)
- [【websocket】spring boot 集成 websocket 的四种方式](https://juejin.cn/post/6844903976727494669#heading-25)
- [IM即时通讯实现以及技术难点](https://www.bilibili.com/video/BV1KM411S7WT/)
