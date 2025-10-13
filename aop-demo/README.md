# Spring AOP 切面

## 📚 AOP基础知识

### 🤔 什么是AOP？

**AOP（Aspect-Oriented Programming，面向切面编程）** 是一种编程范式，它通过预编译方式和运行期动态代理实现程序功能的统一维护。AOP是对OOP（面向对象编程）的补充，用于处理系统中分布于各个模块的横切关注点。

### 🏠 生活中的AOP例子

想象一下你经营一家餐厅，有很多不同的服务员负责不同的工作：

**传统方式（没有AOP）**：
```
服务员A：迎接客人 → 记录日志 → 检查权限 → 带客人入座 → 记录日志
服务员B：接受点餐 → 记录日志 → 检查权限 → 处理订单 → 记录日志
服务员C：上菜服务 → 记录日志 → 检查权限 → 端菜上桌 → 记录日志
服务员D：结账服务 → 记录日志 → 检查权限 → 处理付款 → 记录日志
```

每个服务员都要重复做"记录日志"和"检查权限"这些工作，代码重复且容易出错。

**AOP方式（引入切面）**：
```
📝 日志记录员：专门负责记录所有服务员的工作日志
🔐 安保人员：专门负责检查所有服务员的权限
⏱️ 效率监督员：专门负责监控所有服务员的工作效率

服务员A：专心做迎接客人 → 带客人入座
服务员B：专心做接受点餐 → 处理订单
服务员C：专心做上菜服务 → 端菜上桌
服务员D：专心做结账服务 → 处理付款
```

这样，每个服务员只需要专注于自己的核心业务，而"记录日志"、"检查权限"、"效率监控"这些横切关注点由专门的人员统一处理。

**AOP的好处**：
- 🎯 **职责分离**：服务员专注核心业务，辅助工作由专人负责
- 🔄 **统一管理**：所有日志记录规则统一，修改时只需改一处
- 📈 **提高效率**：避免重复工作，减少出错概率
- 🛠️ **易于维护**：需要调整日志格式时，只需要培训日志记录员

在编程中，AOP就是这样的思想：
- **业务方法** = 服务员的核心工作
- **切面（Aspect）** = 专门的辅助人员（日志记录员、安保人员等）
- **横切关注点** = 需要在多个地方重复的工作（日志、权限、监控等）

### 🎯 核心概念

| 概念 | 英文 | 说明 | 示例 |
|------|------|------|------|
| **切面** | Aspect | 横切关注点的模块化，包含通知和切入点 | 日志记录切面、事务管理切面 |
| **连接点** | Join Point | 程序执行过程中能够应用通知的所有点 | 方法调用、异常抛出 |
| **切入点** | Pointcut | 匹配连接点的断言，定义通知应用的位置 | `execution(* com.example.service.*.*(..))` |
| **通知** | Advice | 在特定连接点执行的代码 | @Before、@After、@Around |
| **目标对象** | Target Object | 被一个或多个切面通知的对象 | UserService、OrderService |
| **代理** | Proxy | AOP框架创建的对象，用于实现切面契约 | JDK动态代理、CGLIB代理 |
| **织入** | Weaving | 将切面与目标对象连接创建代理对象的过程 | 编译时、类加载时、运行时 |

### 🔄 通知类型详解

```java
@Aspect
@Component
public class ExampleAspect {

    // 前置通知：在目标方法执行前执行
    @Before("execution(* com.example.service.*.*(..))")
    public void beforeAdvice(JoinPoint joinPoint) {
        System.out.println("方法执行前: " + joinPoint.getSignature().getName());
    }

    // 后置通知：在目标方法执行后执行（无论是否异常）
    @After("execution(* com.example.service.*.*(..))")
    public void afterAdvice(JoinPoint joinPoint) {
        System.out.println("方法执行后: " + joinPoint.getSignature().getName());
    }

    // 返回通知：在目标方法正常返回后执行
    @AfterReturning(pointcut = "execution(* com.example.service.*.*(..))", returning = "result")
    public void afterReturningAdvice(JoinPoint joinPoint, Object result) {
        System.out.println("方法正常返回: " + result);
    }

    // 异常通知：在目标方法抛出异常后执行
    @AfterThrowing(pointcut = "execution(* com.example.service.*.*(..))", throwing = "exception")
    public void afterThrowingAdvice(JoinPoint joinPoint, Exception exception) {
        System.out.println("方法抛出异常: " + exception.getMessage());
    }

    // 环绕通知：包围目标方法执行，最强大的通知类型
    @Around("execution(* com.example.service.*.*(..))")
    public Object aroundAdvice(ProceedingJoinPoint joinPoint) throws Throwable {
        System.out.println("环绕通知 - 方法执行前");
        try {
            Object result = joinPoint.proceed(); // 执行目标方法
            System.out.println("环绕通知 - 方法执行后");
            return result;
        } catch (Exception e) {
            System.out.println("环绕通知 - 方法执行异常");
            throw e;
        }
    }
}
```

### 📐 切入点表达式语法

#### 1. execution表达式
```java
// 语法：execution(修饰符 返回类型 包.类.方法(参数) 异常)
@Pointcut("execution(public * com.example.service.*.*(..))")  // public方法
@Pointcut("execution(* com.example.service.UserService.*(..))")  // UserService的所有方法
@Pointcut("execution(* com.example.service.*.get*(..))")  // 所有get开头的方法
@Pointcut("execution(* com.example.service.*.*(String, ..))")  // 第一个参数为String的方法
```

#### 2. within表达式
```java
@Pointcut("within(com.example.service.*)")  // service包下的所有类
@Pointcut("within(com.example.service..*)")  // service包及子包下的所有类
@Pointcut("within(com.example.service.UserService)")  // UserService类
```

#### 3. annotation表达式
```java
@Pointcut("@annotation(org.springframework.transaction.annotation.Transactional)")
@Pointcut("@within(org.springframework.stereotype.Service)")
@Pointcut("@target(org.springframework.stereotype.Repository)")
```

#### 4. 组合表达式
```java
@Pointcut("execution(* com.example.service.*.*(..)) && @annotation(Cacheable)")
@Pointcut("within(com.example.service.*) || within(com.example.controller.*)")
@Pointcut("execution(* com.example.service.*.*(..)) && !execution(* com.example.service.*.get*(..))")
```

### 🏗️ AOP实现原理

#### 1. 代理模式
Spring AOP基于代理模式实现，有两种代理方式：

**JDK动态代理**：
- 基于接口的代理
- 目标对象必须实现接口
- 运行时动态生成代理类

**CGLIB代理**：
- 基于继承的代理
- 可以代理没有接口的类
- 通过字节码技术生成子类

```java
// Spring自动选择代理方式的逻辑
if (目标对象实现了接口) {
    使用JDK动态代理;
} else {
    使用CGLIB代理;
}
```

#### 2. 织入时机
- **编译时织入**：AspectJ编译器
- **类加载时织入**：AspectJ LTW（Load-Time Weaving）
- **运行时织入**：Spring AOP（动态代理）

### 🌟 AOP的优势

1. **关注点分离**：将横切关注点从业务逻辑中分离
2. **代码复用**：一次编写，多处使用
3. **易于维护**：集中管理横切逻辑
4. **降低耦合**：业务代码与系统服务解耦
5. **声明式编程**：通过注解简化开发

### 🎯 常见应用场景

| 场景 | 说明 | 实现方式 |
|------|------|----------|
| **日志记录** | 记录方法调用、参数、返回值 | @Around + 日志框架 |
| **性能监控** | 监控方法执行时间 | @Around + 时间统计 |
| **事务管理** | 自动管理数据库事务 | @Transactional |
| **安全控制** | 权限校验、认证 | @Before + 自定义注解 |
| **缓存管理** | 方法级缓存 | @Around + 缓存框架 |
| **异常处理** | 统一异常处理 | @AfterThrowing |
| **参数校验** | 自动校验方法参数 | @Before + 校验框架 |
| **审计日志** | 记录操作日志 | @AfterReturning |

### 🔍 AOP vs OOP

| 对比维度 | OOP（面向对象编程） | AOP（面向切面编程） |
|---------|-------------------|-------------------|
| **关注点** | 业务逻辑的纵向分解 | 横切关注点的横向分解 |
| **模块化单位** | 类和对象 | 切面（Aspect） |
| **主要目标** | 代码重用、封装、继承 | 关注点分离、减少代码重复 |
| **解决问题** | 业务逻辑复杂性 | 横切关注点散布问题 |
| **典型应用** | 业务模型、数据处理 | 日志、事务、安全、缓存 |

### 🎭 横切关注点详解

**横切关注点（Cross-cutting Concerns）** 是指那些影响多个模块的功能，如果用传统的OOP方式实现，会导致代码重复和模块间的紧耦合。

#### 常见的横切关注点：
1. **系统级关注点**：日志记录、性能监控、异常处理
2. **业务级关注点**：事务管理、安全控制、缓存管理
3. **技术级关注点**：数据校验、审计跟踪、资源管理

#### 传统方式 vs AOP方式：

**传统方式的问题**：
```java
public class UserService {
    public User createUser(User user) {
        // 日志记录 - 重复代码
        logger.info("开始创建用户: " + user.getName());

        // 参数校验 - 重复代码
        if (user == null || user.getName() == null) {
            throw new IllegalArgumentException("用户信息不能为空");
        }

        // 权限检查 - 重复代码
        if (!hasPermission("CREATE_USER")) {
            throw new SecurityException("权限不足");
        }

        try {
            // 核心业务逻辑
            User savedUser = userRepository.save(user);

            // 日志记录 - 重复代码
            logger.info("用户创建成功: " + savedUser.getId());
            return savedUser;
        } catch (Exception e) {
            // 异常处理 - 重复代码
            logger.error("用户创建失败", e);
            throw e;
        }
    }
}
```

**AOP方式的优势**：
```java
@Service
public class UserService {

    @LogExecution  // 日志切面处理
    @ValidateParams  // 参数校验切面处理
    @RequirePermission("CREATE_USER")  // 权限切面处理
    @HandleException  // 异常处理切面处理
    public User createUser(User user) {
        // 只关注核心业务逻辑
        return userRepository.save(user);
    }
}
```

### 🔄 AOP执行流程

```
1. 客户端调用目标方法
        ↓
2. Spring容器拦截调用
        ↓
3. 创建代理对象（如果尚未创建）
        ↓
4. 执行前置通知（@Before）
        ↓
5. 执行环绕通知的前半部分（@Around - before proceed）
        ↓
6. 执行目标方法
        ↓
7. 执行环绕通知的后半部分（@Around - after proceed）
        ↓
8. 执行返回通知（@AfterReturning）或异常通知（@AfterThrowing）
        ↓
9. 执行后置通知（@After）
        ↓
10. 返回结果给客户端
```

### 🏭 Spring AOP vs AspectJ

| 特性 | Spring AOP | AspectJ |
|------|------------|---------|
| **实现方式** | 基于代理的运行时织入 | 编译时/类加载时织入 |
| **性能** | 运行时开销较大 | 编译时织入，运行时开销小 |
| **功能完整性** | 功能相对简单 | 功能强大完整 |
| **学习成本** | 相对简单 | 学习曲线陡峭 |
| **集成度** | 与Spring无缝集成 | 需要特殊编译器 |
| **适用场景** | Spring应用的常见需求 | 复杂的AOP需求 |

### ⚠️ 注意事项与最佳实践

#### 1. 性能考虑
- **代理开销**：每次方法调用都会经过代理，带来性能开销
- **内存占用**：代理对象会增加内存使用
- **优化建议**：避免在高频调用的方法上使用复杂的切面逻辑

#### 2. 调试困难
- **堆栈信息**：代理对象可能影响异常堆栈的可读性
- **断点调试**：IDE可能无法正确定位到切面代码
- **解决方案**：使用日志记录切面执行过程

#### 3. 内部调用限制
```java
@Service
public class UserService {

    @Transactional
    public void methodA() {
        // 这个调用不会触发事务切面！
        methodB();
    }

    @Transactional
    public void methodB() {
        // 业务逻辑
    }
}
```

**解决方案**：
```java
@Service
public class UserService {

    @Autowired
    private UserService self;  // 注入自己的代理对象

    @Transactional
    public void methodA() {
        // 通过代理对象调用，可以触发切面
        self.methodB();
    }
}
```

#### 4. 代理限制
- **final类/方法**：CGLIB无法代理final类或final方法
- **private方法**：无法被代理
- **static方法**：无法被代理
- **构造方法**：无法被代理

#### 5. 最佳实践
1. **切面职责单一**：每个切面只处理一种横切关注点
2. **避免过度使用**：不要为了使用AOP而使用AOP
3. **性能测试**：在性能敏感的应用中要进行充分测试
4. **文档记录**：详细记录切面的作用和配置
5. **异常处理**：切面中的异常要妥善处理，避免影响业务逻辑

### 🔧 Spring AOP配置

#### 1. 启用AOP
```java
@Configuration
@EnableAspectJAutoProxy  // 启用AspectJ自动代理
public class AopConfig {
}
```

#### 2. 配置选项
```java
@EnableAspectJAutoProxy(
    proxyTargetClass = true,  // 强制使用CGLIB代理
    exposeProxy = true        // 暴露当前代理对象到AopContext
)
```

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

**核心实现代码**：
```java
@Aspect
@Component
@Slf4j
public class LoggingAspect {

    // 定义切入点：拦截Controller层的所有方法
    @Pointcut("execution(* com.example.aop.controller.*.*(..))")
    public void controllerLayer() {}

    // 定义切入点：拦截Service层的所有方法
    @Pointcut("execution(* com.example.aop.service.*.*(..))")
    public void serviceLayer() {}

    // 环绕通知 - Controller层：记录请求信息、响应时间等
    @Around("controllerLayer()")
    public Object logController(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();

        // 获取HTTP请求信息
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes != null) {
            HttpServletRequest request = attributes.getRequest();
            log.info("🌐 ===== HTTP请求开始 =====");
            log.info("📍 请求URL: {}", request.getRequestURL());
            log.info("🔧 请求方法: {}", request.getMethod());
            log.info("🎯 执行方法: {}.{}",
                    joinPoint.getTarget().getClass().getSimpleName(),
                    joinPoint.getSignature().getName());
            log.info("📝 请求参数: {}", Arrays.toString(joinPoint.getArgs()));
        }

        try {
            // 执行原方法
            Object result = joinPoint.proceed();
            long endTime = System.currentTimeMillis();
            log.info("✅ 请求成功，响应时间: {}ms", endTime - startTime);
            log.info("📤 响应结果: {}", result);
            log.info("🌐 ===== HTTP请求结束 =====\n");
            return result;
        } catch (Exception e) {
            long endTime = System.currentTimeMillis();
            log.error("❌ 请求失败，响应时间: {}ms", endTime - startTime);
            log.error("💥 异常信息: {}", e.getMessage());
            throw e;
        }
    }

    // 前置通知 - Service层
    @Before("serviceLayer()")
    public void logServiceBefore(JoinPoint joinPoint) {
        log.info("🔧 [前置通知] 准备执行: {}.{}",
                joinPoint.getTarget().getClass().getSimpleName(),
                joinPoint.getSignature().getName());
    }

    // 返回通知 - Service层
    @AfterReturning(pointcut = "serviceLayer()", returning = "result")
    public void logServiceReturn(JoinPoint joinPoint, Object result) {
        log.info("✅ [返回通知] 方法正常返回: {}.{}, 返回值: {}",
                joinPoint.getTarget().getClass().getSimpleName(),
                joinPoint.getSignature().getName(), result);
    }

    // 异常通知 - Service层
    @AfterThrowing(pointcut = "serviceLayer()", throwing = "exception")
    public void logServiceException(JoinPoint joinPoint, Exception exception) {
        log.error("💥 [异常通知] 方法执行异常: {}.{}, 异常: {}",
                joinPoint.getTarget().getClass().getSimpleName(),
                joinPoint.getSignature().getName(), exception.getMessage());
    }
}
```

**关键技术点解析**：
1. **切入点表达式**：`execution(* com.example.aop.controller.*.*(..))`匹配controller包下所有类的所有方法
2. **环绕通知**：`@Around`可以控制目标方法的执行，获取执行前后的状态
3. **连接点信息**：通过`JoinPoint`获取方法名、参数等信息
4. **HTTP请求信息**：通过`RequestContextHolder`获取当前HTTP请求的详细信息

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

**核心实现代码**：
```java
@Aspect
@Component
@Slf4j
public class PerformanceAspect {

    // 性能监控阈值（毫秒）
    private static final long SLOW_METHOD_THRESHOLD = 1000;

    // 定义切入点：监控Service层和Controller层的所有方法
    @Pointcut("execution(* com.example.aop.service.*.*(..)) || " +
              "execution(* com.example.aop.controller.*.*(..))")
    public void monitoredMethods() {}

    // 环绕通知：监控方法执行时间
    @Around("monitoredMethods()")
    public Object monitorPerformance(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        String className = joinPoint.getTarget().getClass().getSimpleName();
        String methodName = joinPoint.getSignature().getName();

        try {
            // 执行原方法
            Object result = joinPoint.proceed();

            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;

            // 记录执行时间
            if (duration > SLOW_METHOD_THRESHOLD) {
                log.warn("🐌 [性能告警] 慢方法检测: {}.{} 执行时间: {}ms (超过阈值{}ms)",
                        className, methodName, duration, SLOW_METHOD_THRESHOLD);

                // 发送告警通知
                sendSlowMethodAlert(className, methodName, duration);
            } else {
                log.info("⚡ [性能监控] {}.{} 执行时间: {}ms",
                        className, methodName, duration);
            }

            return result;
        } catch (Exception e) {
            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;

            log.error("💥 [性能监控] {}.{} 执行异常，耗时: {}ms, 异常: {}",
                    className, methodName, duration, e.getMessage());
            throw e;
        }
    }

    // 发送慢方法告警
    private void sendSlowMethodAlert(String className, String methodName, long duration) {
        // 模拟发送告警
        log.warn("📧 [告警通知] 发送慢方法告警: {}.{} 执行时间: {}ms",
                className, methodName, duration);

        // 实际项目中可以集成：
        // 1. 邮件通知
        // 2. 短信通知
        // 3. 钉钉/企业微信通知
        // 4. 监控系统（如Prometheus + Grafana）
        // 5. APM系统（如SkyWalking、Pinpoint）
    }
}
```

**关键技术点解析**：
1. **复合切入点**：使用`||`操作符组合多个切入点表达式
2. **性能阈值**：通过常量定义性能监控的阈值，便于配置管理
3. **告警机制**：慢方法检测后触发告警通知，可扩展多种通知方式
4. **异常处理**：即使方法执行异常，也要记录执行时间

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

**核心实现代码**：
```java
@Aspect
@Component
@Slf4j
public class AuthAspect {

    // 自定义权限校验注解
    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface RequireAuth {
        String role() default "USER";    // 需要的角色
        String description() default ""; // 权限描述
    }

    // 前置通知：权限校验
    @Before("@annotation(requireAuth)")
    public void checkAuth(JoinPoint joinPoint, RequireAuth requireAuth) {
        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getTarget().getClass().getSimpleName();
        String requiredRole = requireAuth.role();
        String description = requireAuth.description();

        log.info("🔐 [权限校验] 检查方法: {}.{}", className, methodName);
        log.info("🎭 [权限校验] 需要角色: {}", requiredRole);
        if (!description.isEmpty()) {
            log.info("📝 [权限校验] 权限描述: {}", description);
        }

        // 模拟获取当前用户角色（实际项目中从SecurityContext或Session获取）
        String currentUserRole = getCurrentUserRole();
        log.info("👤 [权限校验] 当前用户角色: {}", currentUserRole);

        // 权限校验逻辑
        if (!hasPermission(currentUserRole, requiredRole)) {
            log.error("❌ [权限校验] 权限不足！当前角色: {}, 需要角色: {}",
                     currentUserRole, requiredRole);
            throw new SecurityException(String.format("权限不足，需要角色: %s，当前角色: %s",
                                                     requiredRole, currentUserRole));
        }

        log.info("✅ [权限校验] 权限校验通过");
    }

    // 模拟获取当前用户角色
    // 实际项目中应该从以下地方获取：
    // 1. Spring Security的SecurityContext
    // 2. JWT Token解析
    // 3. Session
    // 4. ThreadLocal
    private String getCurrentUserRole() {
        // 这里简单模拟，实际项目中需要根据具体的认证方案来实现
        String[] roles = {"USER", "ADMIN", "GUEST"};
        int index = (int) (System.currentTimeMillis() % 3);
        return roles[index];
    }

    // 权限校验逻辑
    // 实际项目中可能需要更复杂的权限模型，比如：
    // 1. 基于角色的访问控制（RBAC）
    // 2. 基于属性的访问控制（ABAC）
    // 3. 层次化角色权限
    private boolean hasPermission(String currentRole, String requiredRole) {
        if (currentRole == null) {
            return false;
        }

        // 简单的权限层次：ADMIN > USER > GUEST
        switch (requiredRole) {
            case "GUEST":
                return true; // 所有人都可以访问GUEST级别的资源
            case "USER":
                return "USER".equals(currentRole) || "ADMIN".equals(currentRole);
            case "ADMIN":
                return "ADMIN".equals(currentRole);
            default:
                return false;
        }
    }
}
```

**关键技术点解析**：
1. **自定义注解**：`@RequireAuth`注解用于标记需要权限校验的方法
2. **注解驱动切入点**：`@annotation(requireAuth)`匹配带有指定注解的方法
3. **权限层次设计**：ADMIN > USER > GUEST的简单权限模型
4. **异常处理**：权限不足时抛出`SecurityException`异常

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
🔐 [权限校验] 检查方法: UserController.updateUser
🎭 [权限校验] 需要角色: USER
📝 [权限校验] 权限描述: 更新用户信息需要USER权限
👤 [权限校验] 当前用户角色: USER
✅ [权限校验] 权限校验通过

🔐 [权限校验] 检查方法: UserController.deleteUser
🎭 [权限校验] 需要角色: ADMIN
📝 [权限校验] 权限描述: 删除用户需要ADMIN权限
👤 [权限校验] 当前用户角色: ADMIN
✅ [权限校验] 权限校验通过
```

---

### 💾 4. 缓存切面 (CacheAspect)

**功能说明**：
- 基于自定义注解`@Cacheable`实现方法级缓存
- 支持设置缓存过期时间
- 使用内存缓存（ConcurrentHashMap）

**核心实现代码**：
```java
@Aspect
@Component
@Slf4j
public class CacheAspect {

    // 简单的内存缓存
    // 实际项目中建议使用Redis、Caffeine等专业缓存组件
    private final ConcurrentHashMap<String, CacheItem> cache = new ConcurrentHashMap<>();

    // 自定义缓存注解
    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface Cacheable {
        String value() default "";       // 缓存key前缀
        int expireTime() default 300;    // 缓存过期时间（秒）
    }

    // 缓存数据项
    private static class CacheItem {
        private final Object data;
        private final long expireTime;

        public CacheItem(Object data, long expireTime) {
            this.data = data;
            this.expireTime = expireTime;
        }

        public boolean isExpired() {
            return System.currentTimeMillis() > expireTime;
        }

        public Object getData() {
            return data;
        }
    }

    // 环绕通知：缓存处理
    @Around("@annotation(cacheable)")
    public Object handleCache(ProceedingJoinPoint joinPoint, Cacheable cacheable) throws Throwable {
        // 生成缓存key
        String cacheKey = generateCacheKey(joinPoint, cacheable);

        // 尝试从缓存获取
        CacheItem cacheItem = cache.get(cacheKey);
        if (cacheItem != null && !cacheItem.isExpired()) {
            log.info("🎯 [缓存命中] key: {}", cacheKey);
            return cacheItem.getData();
        }

        // 缓存未命中或已过期，执行原方法
        log.info("❌ [缓存未命中] key: {}, 执行原方法", cacheKey);
        Object result = joinPoint.proceed();

        // 将结果存入缓存
        long expireTime = System.currentTimeMillis() + cacheable.expireTime() * 1000L;
        cache.put(cacheKey, new CacheItem(result, expireTime));
        log.info("💾 [缓存存储] key: {}, 过期时间: {}秒", cacheKey, cacheable.expireTime());

        return result;
    }

    // 生成缓存key
    private String generateCacheKey(ProceedingJoinPoint joinPoint, Cacheable cacheable) {
        StringBuilder keyBuilder = new StringBuilder();

        // 添加前缀
        if (!cacheable.value().isEmpty()) {
            keyBuilder.append(cacheable.value()).append(":");
        }

        // 添加类名和方法名
        keyBuilder.append(joinPoint.getTarget().getClass().getSimpleName())
                  .append(".")
                  .append(joinPoint.getSignature().getName());

        // 添加参数
        Object[] args = joinPoint.getArgs();
        if (args.length > 0) {
            keyBuilder.append(":")
                      .append(Arrays.toString(args).hashCode());
        }

        return keyBuilder.toString();
    }

    // 清空缓存（可以通过管理接口调用）
    public void clearCache() {
        cache.clear();
        log.info("🗑️ [缓存清理] 所有缓存已清空");
    }

    // 获取缓存统计信息
    public String getCacheStats() {
        long totalItems = cache.size();
        long expiredItems = cache.values().stream()
                .mapToLong(item -> item.isExpired() ? 1 : 0)
                .sum();

        return String.format("缓存统计 - 总数: %d, 过期: %d, 有效: %d",
                totalItems, expiredItems, totalItems - expiredItems);
    }
}
```

**关键技术点解析**：
1. **自定义注解**：`@Cacheable`注解支持配置缓存前缀和过期时间
2. **缓存key生成**：基于类名、方法名和参数生成唯一的缓存key
3. **过期机制**：通过时间戳实现缓存过期功能
4. **线程安全**：使用`ConcurrentHashMap`保证并发安全

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
❌ [缓存未命中] key: user:UserController.getUserById:1, 执行原方法
💾 [缓存存储] key: user:UserController.getUserById:1, 过期时间: 60秒

🎯 [缓存命中] key: user:UserController.getUserById:1
```

---

### 🎭 5. 数据脱敏切面 (DataMaskingAspect)

**功能说明**：
- 对返回的用户敏感信息进行自动脱敏
- 脱敏规则：手机号、身份证、邮箱
- 使用`@AfterReturning`返回通知

**核心实现代码**：
```java
@Aspect
@Component
@Slf4j
public class DataMaskingAspect {

    // 定义切入点：拦截Controller层返回用户信息的方法
    @Pointcut("execution(* com.example.aop.controller.*.*(..)) && " +
              "(execution(* *..getUserById(..)) || execution(* *..getAllUsers(..))")
    public void userDataMethods() {}

    // 返回通知：对用户敏感数据进行脱敏
    @AfterReturning(pointcut = "userDataMethods()", returning = "result")
    public void maskSensitiveData(Object result) {
        if (result == null) {
            return;
        }

        log.info("🎭 [数据脱敏] 开始处理返回数据");

        // 处理单个用户对象
        if (result instanceof User) {
            maskUserData((User) result);
        }
        // 处理用户列表
        else if (result instanceof List) {
            List<?> list = (List<?>) result;
            for (Object item : list) {
                if (item instanceof User) {
                    maskUserData((User) item);
                }
            }
        }
        // 处理包装在ApiResponse中的数据
        else if (result.getClass().getSimpleName().equals("ApiResponse")) {
            try {
                // 使用反射获取data字段
                java.lang.reflect.Field dataField = result.getClass().getDeclaredField("data");
                dataField.setAccessible(true);
                Object data = dataField.get(result);

                if (data instanceof User) {
                    maskUserData((User) data);
                } else if (data instanceof List) {
                    List<?> list = (List<?>) data;
                    for (Object item : list) {
                        if (item instanceof User) {
                            maskUserData((User) item);
                        }
                    }
                }
            } catch (Exception e) {
                log.warn("⚠️ [数据脱敏] 处理ApiResponse数据时出错: {}", e.getMessage());
            }
        }

        log.info("✅ [数据脱敏] 数据脱敏处理完成");
    }

    // 对用户数据进行脱敏处理
    private void maskUserData(User user) {
        if (user == null) {
            return;
        }

        // 脱敏手机号：138****1234
        if (user.getPhone() != null) {
            String originalPhone = user.getPhone();
            user.setPhone(maskPhone(originalPhone));
            log.debug("📱 [数据脱敏] 手机号: {} -> {}", originalPhone, user.getPhone());
        }

        // 脱敏身份证：123456********1234
        if (user.getIdCard() != null) {
            String originalIdCard = user.getIdCard();
            user.setIdCard(maskIdCard(originalIdCard));
            log.debug("🆔 [数据脱敏] 身份证: {} -> {}", originalIdCard, user.getIdCard());
        }

        // 脱敏邮箱：zh***@example.com
        if (user.getEmail() != null) {
            String originalEmail = user.getEmail();
            user.setEmail(maskEmail(originalEmail));
            log.debug("📧 [数据脱敏] 邮箱: {} -> {}", originalEmail, user.getEmail());
        }
    }

    // 脱敏手机号
    // 规则：保留前3位和后4位，中间用****替换
    private String maskPhone(String phone) {
        if (phone == null || phone.length() < 7) {
            return phone;
        }

        if (phone.length() == 11) {
            return phone.substring(0, 3) + "****" + phone.substring(7);
        } else {
            // 其他长度的手机号，保留前后各2位
            return phone.substring(0, 2) + "****" + phone.substring(phone.length() - 2);
        }
    }

    // 脱敏身份证号
    // 规则：保留前6位和后4位，中间用********替换
    private String maskIdCard(String idCard) {
        if (idCard == null || idCard.length() < 10) {
            return idCard;
        }

        if (idCard.length() == 18) {
            return idCard.substring(0, 6) + "********" + idCard.substring(14);
        } else if (idCard.length() == 15) {
            return idCard.substring(0, 6) + "*****" + idCard.substring(11);
        } else {
            // 其他长度，保留前后各3位
            return idCard.substring(0, 3) + "****" + idCard.substring(idCard.length() - 3);
        }
    }

    // 脱敏邮箱
    // 规则：保留前2位和@后的域名，用户名中间用***替换
    private String maskEmail(String email) {
        if (email == null || !email.contains("@")) {
            return email;
        }

        String[] parts = email.split("@");
        if (parts.length != 2) {
            return email;
        }

        String username = parts[0];
        String domain = parts[1];

        if (username.length() <= 2) {
            return email; // 用户名太短，不脱敏
        } else if (username.length() <= 4) {
            return username.charAt(0) + "***@" + domain;
        } else {
            return username.substring(0, 2) + "***@" + domain;
        }
    }
}
```

**关键技术点解析**：
1. **复合切入点表达式**：同时匹配包路径和方法名的复合条件
2. **返回通知处理**：`@AfterReturning`在方法正常返回后执行，可以修改返回值
3. **反射处理包装对象**：通过反射处理ApiResponse等包装类中的数据
4. **多种脱敏算法**：针对不同类型的敏感数据使用不同的脱敏规则

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
    "email": "zh***@example.com",
    "phone": "138****5678",
    "idCard": "123456********5678",
    "age": 25,
    "role": "USER"
  }
}
```

**预期控制台输出**：
```
🎭 [数据脱敏] 开始处理返回数据
📱 [数据脱敏] 手机号: 13812345678 -> 138****5678
📧 [数据脱敏] 邮箱: zhangsan@example.com -> zh***@example.com
🆔 [数据脱敏] 身份证: 123456789012345678 -> 123456********5678
✅ [数据脱敏] 数据脱敏处理完成
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

