package com.example.ioc;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

/**
 * 更真实的 IoC 容器实现
 * 使用反射来自动分析和注入依赖
 */
public class RealisticIocDemo {

    public static void main(String[] args) {
        System.out.println("=== 更真实的 IoC 容器演示 ===\n");

        // 创建真实的 IoC 容器
        RealisticIocContainer container = new RealisticIocContainer();

        // 注册类型（而不是实例）
        container.registerType("database", RealDatabase.class);
        container.registerType("userService", RealUserService.class);

        System.out.println("\n--- 开始获取 userService ---");

        // 容器会自动分析依赖并创建对象
        RealUserService userService = container.getBean("userService");
        userService.saveUser("小明");
    }
}

/**
 * 更真实的 IoC 容器
 * 使用反射自动分析依赖关系
 */
class RealisticIocContainer {
    // 存储类型注册信息
    private Map<String, Class<?>> typeRegistry = new HashMap<>();
    // 存储已创建的单例对象
    private Map<String, Object> singletonCache = new HashMap<>();

    /**
     * 注册类型
     */
    public void registerType(String name, Class<?> clazz) {
        typeRegistry.put(name, clazz);
        System.out.println("📦 注册类型: " + name + " -> " + clazz.getSimpleName());
    }

    /**
     * 获取 Bean，自动分析和注入依赖
     */
    @SuppressWarnings("unchecked")
    public <T> T getBean(String name) {
        // 检查是否已经创建过（单例模式）
        if (singletonCache.containsKey(name)) {
            System.out.println("🔄 从缓存中获取: " + name);
            return (T) singletonCache.get(name);
        }

        // 获取要创建的类型
        Class<?> clazz = typeRegistry.get(name);
        if (clazz == null) {
            throw new RuntimeException("未找到类型: " + name);
        }

        System.out.println("🔍 开始创建: " + clazz.getSimpleName());

        try {
            // 🔑 关键：使用反射分析构造函数
            Constructor<?>[] constructors = clazz.getConstructors();
            Constructor<?> constructor = constructors[0]; // 简化：取第一个构造函数

            // 分析构造函数参数（依赖）
            Class<?>[] paramTypes = constructor.getParameterTypes();
            System.out.println("   ↳ 分析构造函数，发现需要 " + paramTypes.length + " 个依赖:");

            // 准备构造函数参数
            Object[] args = new Object[paramTypes.length];
            for (int i = 0; i < paramTypes.length; i++) {
                Class<?> paramType = paramTypes[i];
                System.out.println("     - 参数 " + (i+1) + ": " + paramType.getSimpleName());

                // 🔄 递归获取依赖（这里简化：根据类型名查找）
                String dependencyName = findBeanNameByType(paramType);
                if (dependencyName != null) {
                    System.out.println("       ↳ 找到对应的 bean: " + dependencyName);
                    args[i] = getBean(dependencyName); // 递归调用
                } else {
                    throw new RuntimeException("找不到类型 " + paramType + " 的 bean");
                }
            }

            // 🎯 使用反射创建对象
            System.out.println("   ↳ 使用反射创建对象...");
            Object instance = constructor.newInstance(args);

            // 缓存单例
            singletonCache.put(name, instance);

            System.out.println("✅ 成功创建: " + clazz.getSimpleName());
            return (T) instance;

        } catch (Exception e) {
            throw new RuntimeException("创建对象失败: " + clazz, e);
        }
    }

    /**
     * 根据类型查找对应的 bean 名称
     * 实际的容器会有更复杂的匹配逻辑
     */
    private String findBeanNameByType(Class<?> type) {
        for (Map.Entry<String, Class<?>> entry : typeRegistry.entrySet()) {
            if (type.isAssignableFrom(entry.getValue())) {
                return entry.getKey();
            }
        }
        return null;
    }
}

/**
 * 真实的数据库类
 */
class RealDatabase {
    public RealDatabase() {
        System.out.println("🔗 RealDatabase 构造函数被调用");
    }

    public void save(String data) {
        System.out.println("💾 RealDatabase 保存: " + data);
    }
}

/**
 * 真实的用户服务类
 * 构造函数需要 Database 依赖
 */
class RealUserService {
    private RealDatabase database;

    // 🔑 容器会通过反射分析这个构造函数
    public RealUserService(RealDatabase database) {
        System.out.println("👤 RealUserService 构造函数被调用，收到依赖: " + database.getClass().getSimpleName());
        this.database = database;
    }

    public void saveUser(String username) {
        System.out.println("👤 RealUserService 保存用户: " + username);
        database.save("用户: " + username);
    }
}

/*
🎓 真实 IoC 容器的核心技术：

1. 🔍 反射分析：
   - Class.getConstructors() 获取构造函数
   - Constructor.getParameterTypes() 获取参数类型
   - Constructor.newInstance(args) 创建对象

2. 🔄 递归依赖解析：
   - 发现依赖时递归调用 getBean()
   - 自动解决依赖链

3. 📝 注解支持：
   - @Component, @Service, @Repository
   - @Autowired, @Inject
   - @Qualifier 指定具体实现

4. 🏭 生命周期管理：
   - 单例模式（Singleton）
   - 原型模式（Prototype）
   - 初始化和销毁回调

5. 🎯 高级特性：
   - AOP（面向切面编程）
   - 条件注入（@Conditional）
   - 配置属性绑定（@ConfigurationProperties）
*/

