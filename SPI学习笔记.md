# SPI（Service Provider Interface）学习笔记

## 📚 目录
- [1. SPI是什么？](#1-spi是什么)
- [2. SPI的核心思想](#2-spi的核心思想)
- [3. SPI工作原理](#3-spi工作原理)
- [4. RPC框架中的SPI实现](#4-rpc框架中的spi实现)
- [5. ExtensionLoader源码分析](#5-extensionloader源码分析)
- [6. 实际应用场景](#6-实际应用场景)
- [7. SPI vs 其他扩展机制](#7-spi-vs-其他扩展机制)
- [8. 总结](#8-总结)

## 1. SPI是什么？

**SPI（Service Provider Interface）** = **服务提供者接口**

### 🎯 简单理解
SPI是一种**插件化扩展机制**，让你可以在不修改原代码的情况下，动态地替换或扩展功能实现。

### 🔌 生活类比
SPI就像"万能插座"：
```
🔌 插座接口（SPI接口）
├── 🇨🇳 中式插头（实现A）
├── 🇺🇸 美式插头（实现B）
├── 🇪🇺 欧式插头（实现C）
└── 🇬🇧 英式插头（实现D）
```

## 2. SPI的核心思想

**"面向接口编程 + 配置文件驱动"**

```
┌─────────────┐    使用    ┌─────────────┐
│   应用代码   │ ────────→ │   接口定义   │
└─────────────┘           └─────────────┘
                                 ↑
                                 │ 实现
                          ┌─────────────┐
                          │  具体实现类  │ ← 通过配置文件指定
                          └─────────────┘
```

### 🔄 SPI的四个步骤
1. **📋 定义标准接口**（用@SPI注解标记）
2. **🔧 提供多种实现**（实现接口的具体类）
3. **📝 配置文件映射**（META-INF/extensions/目录下）
4. **🚀 运行时动态加载**（通过ExtensionLoader）

## 3. SPI工作原理

### 🎮 完整流程图
```
用户调用
    ↓
ExtensionLoader.getExtensionLoader(接口.class)
    ↓
loader.getExtension("实现名称")
    ↓
检查缓存 → 没有缓存 → 创建实例
    ↓
读取配置文件 → 解析映射关系 → 加载类 → 创建对象
    ↓
返回实例给用户
```

### 🏪 工作过程类比
1. **开店**：`getExtensionLoader()` - 为每种接口开一家专门店
2. **顾客购买**：`getExtension()` - 顾客要买特定商品
3. **查库存**：检查缓存中是否有现货
4. **生产商品**：`createExtension()` - 没有就去工厂生产
5. **查说明书**：读取配置文件找到对应的工厂
6. **交付商品**：返回创建好的实例

## 4. RPC框架中的SPI实现

### 4.1 序列化器示例

**第一步：定义接口**
```java
@SPI  // 标记这是一个SPI接口
public interface Serializer {
    /**
     * 序列化
     */
    byte[] serialize(Object obj);

    /**
     * 反序列化
     */
    <T> T deserialize(byte[] bytes, Class<T> clazz);
}
```

**第二步：提供多种实现**
```java
// Kryo实现
public class KryoSerializer implements Serializer {
    @Override
    public byte[] serialize(Object obj) {
        // Kryo序列化逻辑
        return kryoBytes;
    }
}

// Hessian实现
public class HessianSerializer implements Serializer {
    @Override
    public byte[] serialize(Object obj) {
        // Hessian序列化逻辑
        return hessianBytes;
    }
}

// Protostuff实现
public class ProtostuffSerializer implements Serializer {
    @Override
    public byte[] serialize(Object obj) {
        // Protostuff序列化逻辑
        return protostuffBytes;
    }
}
```

**第三步：配置文件映射**
```properties
# 文件位置：META-INF/extensions/github.javaguide.serialize.Serializer
kyro=github.javaguide.serialize.kyro.KryoSerializer
protostuff=github.javaguide.serialize.protostuff.ProtostuffSerializer
hessian=github.javaguide.serialize.hessian.HessianSerializer
```

**第四步：动态加载使用**
```java
// 使用时，通过名称动态获取实现
ExtensionLoader<Serializer> loader = ExtensionLoader.getExtensionLoader(Serializer.class);

// 可以随时切换不同的序列化实现
Serializer kryoSerializer = loader.getExtension("kyro");
Serializer hessianSerializer = loader.getExtension("hessian");

// 序列化对象
byte[] data = kryoSerializer.serialize(myObject);
```

### 4.2 负载均衡器示例

**接口定义**：
```java
@SPI
public interface LoadBalance {
    /**
     * 从服务列表中选择一个服务
     */
    String selectServiceAddress(List<String> serviceAddresses, RpcRequest rpcRequest);
}
```

**配置文件**：
```properties
# META-INF/extensions/github.javaguide.loadbalance.LoadBalance
loadBalance=github.javaguide.loadbalance.loadbalancer.ConsistentHashLoadBalance
loadBalanceNew=github.javaguide.loadbalance.loadbalancer.ConsistentHashLoadBalanceNew
```

## 5. ExtensionLoader源码分析

### 5.1 核心数据结构
```java
public final class ExtensionLoader<T> {
    // 配置文件目录
    private static final String SERVICE_DIRECTORY = "META-INF/extensions/";

    // 每个接口对应一个ExtensionLoader（全局缓存）
    private static final Map<Class<?>, ExtensionLoader<?>> EXTENSION_LOADERS = new ConcurrentHashMap<>();

    // 当前接口类型
    private final Class<?> type;

    // 实例缓存（按名称缓存）
    private final Map<String, Holder<Object>> cachedInstances = new ConcurrentHashMap<>();

    // 类缓存（从配置文件读取的映射关系）
    private final Holder<Map<String, Class<?>>> cachedClasses = new Holder<>();
}
```

### 5.2 获取ExtensionLoader
```java
public static <S> ExtensionLoader<S> getExtensionLoader(Class<S> type) {
    // 1. 参数校验
    if (type == null) {
        throw new IllegalArgumentException("Extension type should not be null.");
    }
    if (!type.isInterface()) {
        throw new IllegalArgumentException("Extension type must be an interface.");
    }
    if (type.getAnnotation(SPI.class) == null) {
        throw new IllegalArgumentException("Extension type must be annotated by @SPI");
    }

    // 2. 从缓存获取或创建新的ExtensionLoader
    ExtensionLoader<S> extensionLoader = (ExtensionLoader<S>) EXTENSION_LOADERS.get(type);
    if (extensionLoader == null) {
        EXTENSION_LOADERS.putIfAbsent(type, new ExtensionLoader<S>(type));
        extensionLoader = (ExtensionLoader<S>) EXTENSION_LOADERS.get(type);
    }
    return extensionLoader;
}
```

### 5.3 获取扩展实例
```java
public T getExtension(String name) {
    if (StringUtil.isBlank(name)) {
        throw new IllegalArgumentException("Extension name should not be null or empty.");
    }

    // 1. 获取或创建Holder
    Holder<Object> holder = cachedInstances.get(name);
    if (holder == null) {
        cachedInstances.putIfAbsent(name, new Holder<>());
        holder = cachedInstances.get(name);
    }

    // 2. 双重检查锁创建实例
    Object instance = holder.get();
    if (instance == null) {
        synchronized (holder) {
            instance = holder.get();
            if (instance == null) {
                instance = createExtension(name);  // 创建扩展实例
                holder.set(instance);
            }
        }
    }
    return (T) instance;
}
```

### 5.4 创建扩展实例
```java
private T createExtension(String name) {
    // 1. 获取扩展类
    Class<?> clazz = getExtensionClasses().get(name);
    if (clazz == null) {
        throw new RuntimeException("扩展类不存在: " + name);
    }

    // 2. 使用单例工厂创建实例
    return (T) SingletonFactory.getInstance(clazz);
}
```

### 5.5 加载扩展类
```java
private Map<String, Class<?>> getExtensionClasses() {
    // 1. 从缓存获取
    Map<String, Class<?>> classes = cachedClasses.get();

    // 2. 双重检查锁加载
    if (classes == null) {
        synchronized (cachedClasses) {
            classes = cachedClasses.get();
            if (classes == null) {
                classes = new HashMap<>();
                loadDirectory(classes);  // 从配置文件加载
                cachedClasses.set(classes);
            }
        }
    }
    return classes;
}
```

### 5.6 读取配置文件
```java
private void loadDirectory(Map<String, Class<?>> extensionClasses) {
    // 1. 构建配置文件路径
    String fileName = SERVICE_DIRECTORY + type.getName();

    try {
        // 2. 获取所有配置文件URL
        ClassLoader classLoader = ExtensionLoader.class.getClassLoader();
        Enumeration<URL> urls = classLoader.getResources(fileName);

        // 3. 逐个解析配置文件
        if (urls != null) {
            while (urls.hasMoreElements()) {
                URL resourceUrl = urls.nextElement();
                loadResource(extensionClasses, classLoader, resourceUrl);
            }
        }
    } catch (IOException e) {
        log.error(e.getMessage());
    }
}

private void loadResource(Map<String, Class<?>> extensionClasses, ClassLoader classLoader, URL resourceUrl) {
    try (BufferedReader reader = new BufferedReader(new InputStreamReader(resourceUrl.openStream(), UTF_8))) {
        String line;
        while ((line = reader.readLine()) != null) {
            // 1. 过滤注释
            final int ci = line.indexOf('#');
            if (ci >= 0) {
                line = line.substring(0, ci);
            }

            // 2. 去除空格
            line = line.trim();
            if (line.length() > 0) {
                try {
                    // 3. 解析key=value格式
                    final int ei = line.indexOf('=');
                    String name = line.substring(0, ei).trim();
                    String clazzName = line.substring(ei + 1).trim();

                    if (name.length() > 0 && clazzName.length() > 0) {
                        // 4. 加载类并缓存
                        Class<?> clazz = classLoader.loadClass(clazzName);
                        extensionClasses.put(name, clazz);
                    }
                } catch (ClassNotFoundException e) {
                    log.error(e.getMessage());
                }
            }
        }
    } catch (IOException e) {
        log.error(e.getMessage());
    }
}
```

## 6. 实际应用场景

### 6.1 数据库驱动切换
```java
// 不用SPI的方式（硬编码）
if (dbType.equals("mysql")) {
    driver = new MySQLDriver();
} else if (dbType.equals("postgresql")) {
    driver = new PostgreSQLDriver();
}

// 使用SPI的方式（配置驱动）
Driver driver = ExtensionLoader.getExtensionLoader(Driver.class)
                              .getExtension(dbType);
```

### 6.2 支付方式切换
```java
// SPI接口
@SPI
public interface PaymentProcessor {
    PaymentResult process(PaymentRequest request);
}

// 配置文件
alipay=com.example.AlipayProcessor
wechat=com.example.WechatPayProcessor
unionpay=com.example.UnionPayProcessor

// 使用
PaymentProcessor processor = ExtensionLoader.getExtensionLoader(PaymentProcessor.class)
                                           .getExtension("alipay");
processor.process(paymentRequest);
```

### 6.3 消息队列切换
```java
@SPI
public interface MessageQueue {
    void send(String topic, String message);
    String receive(String topic);
}

// 配置
kafka=com.example.KafkaMessageQueue
rabbitmq=com.example.RabbitMQMessageQueue
rocketmq=com.example.RocketMQMessageQueue
```

## 7. SPI vs 其他扩展机制

| 机制 | 特点 | 使用场景 | 优缺点 |
|------|------|----------|--------|
| **SPI** | 配置文件驱动，运行时加载 | 插件化系统，多实现切换 | ✅灵活扩展 ❌配置复杂 |
| **工厂模式** | 代码中硬编码创建逻辑 | 简单的对象创建 | ✅简单直接 ❌扩展困难 |
| **依赖注入** | 容器管理对象生命周期 | Spring等框架中 | ✅自动管理 ❌框架依赖 |
| **反射** | 通过类名动态创建对象 | 框架底层实现 | ✅动态性强 ❌性能较差 |

## 8. 总结

### 🎯 SPI核心要点
- **SPI = Service Provider Interface = 服务提供者接口**
- **核心思想**：面向接口编程 + 配置文件驱动
- **工作流程**：定义接口 → 提供实现 → 配置映射 → 动态加载

### ✅ SPI的优势
1. **🔧 可扩展性**：新增实现无需修改原代码
2. **🔄 可替换性**：通过配置文件轻松切换实现
3. **🧩 模块化**：各个实现相互独立
4. **🎯 解耦合**：应用代码与具体实现解耦

### 🎮 记忆口诀
**SPI就像"可换电池的遥控器"**
- 🎮 遥控器（接口）不变
- 🔋 电池（实现）随便换
- 📋 说明书（配置文件）告诉你哪个电池配哪个遥控器
- 🤖 智能管家（ExtensionLoader）帮你自动换电池

### 🚀 在RPC框架中的应用
通过SPI机制可以轻松切换：
- 🔄 序列化方式（Kryo、Hessian、Protostuff）
- ⚖️ 负载均衡算法（一致性哈希、随机）
- 🌐 传输方式（Netty、Socket）
- 📝 注册中心（Zookeeper、Nacos等）

这样设计让框架具有极强的扩展性和灵活性！🎉

