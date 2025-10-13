# Spring AOP 切面功能验证指南

## 🎯 项目概述

这是一个完整的Spring AOP演示项目，展示了面向切面编程在实际项目中的各种应用场景。项目包含5个核心切面，涵盖了企业级开发中最常见的横切关注点。

## 🚀 快速启动

```bash
cd aop-demo
mvn spring-boot:run
```

应用启动后访问：`http://localhost:8081`

## 🧪 AOP切面功能验证完整指南

### 🔍 1. 日志记录切面 (LoggingAspect)

**功能说明**：
- 拦截Controller层和Service层的所有方法
- 记录HTTP请求详细信息（URL、方法、参数、响应时间）
- 使用多种通知类型：`@Around`、`@Before`、`@After`、`@AfterReturning`、`@AfterThrowing`

**验证命令**：
```bash
# 任意接口都会触发日志记录
curl http://localhost:8081/test/welcome
curl http://localhost:8081/users/1
curl http://localhost:8081/users
```

**预期控制台输出**：
```
🌐 ===== HTTP请求开始 =====
📍 请求URL: http://localhost:8081/users/1
🔧 请求方法: GET
🎯 执行方法: UserController.getUserById
📝 请求参数: [1]
🔧 [前置通知] 准备执行: UserService.getUserById
✅ [返回通知] 方法正常返回: UserService.getUserById, 返回值: User(id=1, name=张三, ...)
🏁 [后置通知] 执行完成: UserService.getUserById
✅ 请求成功，响应时间: 15ms
📤 响应结果: ApiResponse(code=200, message=获取用户信息成功, ...)
🌐 ===== HTTP请求结束 =====
```

---

### ⚡ 2. 性能监控切面 (PerformanceAspect)

**功能说明**：
- 监控Service层和Controller层方法执行时间
- 对超过1000ms的慢方法进行告警
- 使用`@Around`环绕通知

**验证命令**：
```bash
# 测试慢方法监控（会触发告警，执行时间约2秒）
curl http://localhost:8081/test/slow

# 对比正常方法（不会触发告警）
curl http://localhost:8081/users/1
```

**预期控制台输出**：
```
🐌 [性能告警] 慢方法检测: UserService.slowMethod 执行时间: 2003ms (超过阈值1000ms)
📧 [告警通知] 发送慢方法告警: UserService.slowMethod 执行时间: 2003ms
⚡ [性能监控] UserController.getUserById 执行时间: 15ms
```

---

### 🔐 3. 权限校验切面 (AuthAspect)

**功能说明**：
- 基于自定义注解`@RequireAuth`进行权限控制
- 支持USER和ADMIN两种角色
- 使用`@Before`前置通知

**验证命令**：
```bash
# 需要USER权限的操作
curl -X PUT http://localhost:8081/users/1 \
  -H "Content-Type: application/json" \
  -d '{"name":"更新后的名字","email":"updated@example.com"}'

# 需要ADMIN权限的操作
curl -X DELETE http://localhost:8081/users/1
curl http://localhost:8081/admin/stats
curl http://localhost:8081/admin/cache/clear
curl http://localhost:8081/admin/cache/stats
```

**预期控制台输出**：
```
🔐 [权限校验] 开始检查权限
👤 方法: updateUser
🎭 需要角色: USER
📝 权限描述: 更新用户信息需要USER权限
🔍 当前用户角色: USER (模拟)
✅ [权限校验] 权限检查通过

🔐 [权限校验] 开始检查权限
👤 方法: deleteUser
🎭 需要角色: ADMIN
📝 权限描述: 删除用户需要ADMIN权限
🔍 当前用户角色: ADMIN (模拟)
✅ [权限校验] 权限检查通过
```

---

### 💾 4. 缓存切面 (CacheAspect)

**功能说明**：
- 基于自定义注解`@Cacheable`实现方法级缓存
- 支持设置缓存过期时间
- 使用内存缓存（ConcurrentHashMap）

**验证命令**：
```bash
# 第一次请求 - 缓存未命中，执行方法
curl http://localhost:8081/users/1

# 第二次请求 - 缓存命中，直接返回
curl http://localhost:8081/users/1

# 查看缓存统计（需要ADMIN权限）
curl http://localhost:8081/admin/cache/stats

# 清空缓存（需要ADMIN权限）
curl http://localhost:8081/admin/cache/clear
```

**预期控制台输出**：
```
💾 [缓存] 缓存未命中: user:1
💾 [缓存] 执行方法获取数据
💾 [缓存] 缓存已存储: user:1, 过期时间: 60秒

💾 [缓存] 缓存命中: user:1
💾 [缓存] 直接返回缓存数据，跳过方法执行
```

---

### 🎭 5. 数据脱敏切面 (DataMaskingAspect)

**功能说明**：
- 对返回的用户敏感信息进行自动脱敏
- 脱敏规则：手机号、身份证、邮箱
- 使用`@AfterReturning`返回通知

**验证命令**：
```bash
# 获取单个用户信息（观察脱敏效果）
curl http://localhost:8081/users/1

# 获取所有用户列表（观察脱敏效果）
curl http://localhost:8081/users
```

**预期响应数据**：
```json
{
  "code": 200,
  "message": "获取用户信息成功",
  "data": {
    "id": 1,
    "name": "张三",
    "email": "zh***@example.com",      // 邮箱脱敏
    "phone": "138****5678",            // 手机号脱敏
    "idCard": "123456********5678",    // 身份证脱敏
    "age": 25,
    "role": "USER"
  }
}
```

**预期控制台输出**：
```
🎭 [数据脱敏] 开始处理返回数据
🎭 [数据脱敏] 脱敏手机号: 138****5678
🎭 [数据脱敏] 脱敏邮箱: zh***@example.com
🎭 [数据脱敏] 脱敏身份证: 123456********5678
🎭 [数据脱敏] 数据脱敏完成
```

---

### 🧪 6. 异常处理切面验证

**功能说明**：
- 通过日志切面的`@AfterThrowing`捕获异常
- 记录异常详细信息

**验证命令**：
```bash
# 测试异常处理
curl http://localhost:8081/test/exception
```

**预期响应**：
```json
{
  "code": 500,
  "message": "异常测试成功: 这是一个模拟异常，用于测试异常处理切面",
  "data": null,
  "timestamp": 1760324182039
}
```

**预期控制台输出**：
```
💥 [异常通知] 方法执行异常: UserService.simulateException, 异常: 这是一个模拟异常，用于测试异常处理切面
❌ 请求失败，响应时间: 5ms
💥 异常信息: 这是一个模拟异常，用于测试异常处理切面
```

---

## 📊 综合验证脚本

按顺序执行以下命令来完整验证所有切面：

```bash
echo "=== 1. 测试日志切面 ==="
curl http://localhost:8081/test/welcome

echo -e "
=== 2. 测试性能监控切面 ==="
curl http://localhost:8081/test/slow

echo -e "
=== 3. 测试缓存切面（第一次请求）==="
curl http://localhost:8081/users/1

echo -e "
=== 4. 测试缓存切面（第二次请求）==="
curl http://localhost:8081/users/1

echo -e "
=== 5. 测试数据脱敏切面 ==="
curl http://localhost:8081/users

echo -e "
=== 6. 测试权限校验切面（USER权限）==="
curl -X PUT http://localhost:8081/users/1 \
  -H "Content-Type: application/json" \
  -d '{"name":"更新后的名字"}'

echo -e "
=== 7. 测试权限校验切面（ADMIN权限）==="
curl -X DELETE http://localhost:8081/users/2

echo -e "
=== 8. 测试异常处理切面 ==="
curl http://localhost:8081/test/exception
```

## 🎯 观察要点

1. **控制台日志**：重点观察应用控制台的详细日志输出
2. **响应数据**：注意数据脱敏效果和响应格式
3. **执行时间**：观察性能监控的时间统计
4. **缓存效果**：第二次请求应该更快（缓存命中）
5. **异常处理**：异常被正确捕获并返回友好信息

## 📁 项目结构

```
aop-demo/
├── src/main/java/com/example/aop/
│   ├── AopDemoApplication.java          # 🚀 Spring Boot 启动类
│   ├── aspect/                          # 🎭 AOP切面包
│   │   ├── LoggingAspect.java          # 📝 日志记录切面
│   │   ├── PerformanceAspect.java      # ⚡ 性能监控切面
│   │   ├── AuthAspect.java             # 🔐 权限校验切面
│   │   ├── CacheAspect.java            # 💾 缓存切面
│   │   └── DataMaskingAspect.java      # 🎭 数据脱敏切面
│   ├── controller/                      # 🎮 控制器层
│   │   ├── HomeController.java         # 🏠 首页控制器
│   │   ├── UserController.java         # 👤 用户控制器
│   │   ├── AdminController.java        # 👑 管理员控制器
│   │   └── TestController.java         # 🧪 测试控制器
│   ├── service/                         # 🔧 服务层
│   │   └── UserService.java            # 👥 用户服务
│   ├── entity/                          # 📊 实体类
│   │   └── User.java                   # 👤 用户实体
│   └── dto/                            # 📦 数据传输对象
│       └── ApiResponse.java            # 📋 统一响应格式
├── src/main/resources/
│   └── application.yml                  # ⚙️ 应用配置
├── pom.xml                             # 📋 Maven依赖配置
└── README.md                           # 📖 项目说明文档
```

## 🔧 核心技术栈

| 技术 | 版本 | 用途 |
|------|------|------|
| Spring Boot | 2.7.0 | 基础框架 |
| Spring AOP | 内置 | 面向切面编程 |
| AspectJ | 内置 | AOP实现 |
| Lombok | 1.18.24 | 简化代码 |
| Maven | - | 依赖管理 |

## 💡 学习要点

1. **切入点表达式**：如何精确定位要拦截的方法
2. **通知类型**：不同通知的执行时机和用途
3. **自定义注解**：结合AOP实现声明式编程
4. **实际应用**：AOP在企业级开发中的常见场景

通过这个演示项目，你可以深入理解Spring AOP的工作原理和实际应用，为在实际项目中使用AOP打下坚实基础！🎉

