# spring-cloud-chat

## 系统说明

- 权限管理系统基于 [pig](https://github.com/pig-mesh/pig)
- 聊天应用基于 Vue 3 + TypeScript，仿[WhatsApp](https://web.whatsapp.com/)风格

## 核心功能

- [x] 离线消息保存
- [x] 一对一聊天
- [x] 基于Websocket的消息推送
- [x] 分布式消息服务器
- [ ] 一对一语音聊天
- [ ] 一对一视频聊天
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
