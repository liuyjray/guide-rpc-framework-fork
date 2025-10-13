package com.example.ioc;

import java.util.HashMap;
import java.util.Map;

/**
 * 简单的 IoC 容器演示
 *
 * IoC (Inversion of Control) 控制反转：
 * - 传统方式：对象自己创建和管理依赖
 * - IoC方式：由容器来创建和管理对象及其依赖关系
 */
public class SimpleIocDemo {

    public static void main(String[] args) {
        System.out.println("=== IoC 容器演示 ===\n");

        // 1. 传统方式演示
        System.out.println("1. 传统方式（没有IoC）：");
        traditionalWay();

        System.out.println("\n" + "===========" + "\n");

        // 2. IoC 方式演示
        System.out.println("2. IoC 方式（使用容器）：");
        iocWay();
    }

    /**
     * 传统方式：对象自己创建依赖
     */
    private static void traditionalWay() {
        // 用户服务自己创建数据库连接
        UserService userService = new UserService();
        userService.saveUser("张三");
    }

    /**
     * IoC 方式：由容器管理依赖
     */
    private static void iocWay() {
        // 创建 IoC 容器
        SimpleIocContainer container = new SimpleIocContainer();

        // 注册组件到容器
        container.register("database", new Database());
        // 注意：userService 由容器创建，不在这里直接创建

        // 从容器获取服务（容器会自动注入依赖）
        UserServiceWithIoc userService = container.getBean("userService");
        userService.saveUser("李四");
    }
}

// ==================== 传统方式的类 ====================

/**
 * 数据库类
 */
class Database {
    public void save(String data) {
        System.out.println("💾 保存数据到数据库: " + data);
    }

    public void connect() {
        System.out.println("🔗 连接到数据库");
    }
}

/**
 * 传统方式的用户服务
 * 问题：紧耦合，自己创建依赖
 */
class UserService {
    private Database database;

    public UserService() {
        // 自己创建数据库连接 - 这就是紧耦合
        this.database = new Database();
        this.database.connect();
        System.out.println("👤 UserService: 我自己创建了数据库连接");
    }

    public void saveUser(String username) {
        System.out.println("👤 UserService: 准备保存用户 " + username);
        database.save("用户: " + username);
    }
}

// ==================== IoC 方式的类 ====================

/**
 * IoC 方式的用户服务
 * 优点：松耦合，依赖由外部注入
 */
class UserServiceWithIoc {
    private Database database;

    // 通过构造函数注入依赖
    public UserServiceWithIoc(Database database) {
        this.database = database;
        this.database.connect();
        System.out.println("👤 UserServiceWithIoc: 数据库依赖已被注入");
    }

    public void saveUser(String username) {
        System.out.println("👤 UserServiceWithIoc: 准备保存用户 " + username);
        database.save("用户: " + username);
    }
}

/**
 * 简单的 IoC 容器
 * 负责管理对象的创建和依赖注入
 */
class SimpleIocContainer {
    // 存储已注册的对象
    private Map<String, Object> beans = new HashMap<>();

    /**
     * 注册对象到容器
     */
    public void register(String name, Object bean) {
        beans.put(name, bean);
        System.out.println("📦 IoC容器: 注册了 " + name);
    }

    /**
     * 从容器获取对象
     * 如果对象需要依赖，容器会自动注入
     *
     * 🔍 依赖注入的关键步骤：
     * 1. 当请求 "userService" 时
     * 2. 容器先找到 UserServiceWithIoc 需要的依赖（Database）
     * 3. 从容器中获取 Database 实例
     * 4. 创建 UserServiceWithIoc 时，把 Database 传给它的构造函数
     * 5. 这样 UserServiceWithIoc 就得到了它需要的依赖！
     */
    @SuppressWarnings("unchecked")
    public <T> T getBean(String name) {
        if ("userService".equals(name)) {
            System.out.println("🔍 IoC容器: 开始创建 UserService...");

            // 步骤1: 分析 UserServiceWithIoc 需要什么依赖
            System.out.println("   ↳ 分析发现：UserServiceWithIoc 需要一个 Database 对象");

            // 步骤2: 从容器中获取 Database 依赖
            Database database = (Database) beans.get("database");
            System.out.println("   ↳ 从容器中找到了 Database: " + database);

            // 步骤3: 创建 UserServiceWithIoc 并注入依赖
            System.out.println("   ↳ 创建 UserServiceWithIoc，并把 Database 注入给它");
            UserServiceWithIoc userService = new UserServiceWithIoc(database);

            System.out.println("📦 IoC容器: ✅ 成功创建 UserService 并完成依赖注入！");
            return (T) userService;
        }

        return (T) beans.get(name);
    }
}



