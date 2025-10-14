# 工厂模式完整演示项目

本项目完整演示了四种工厂模式的实现和使用：简单工厂、工厂方法、抽象工厂和单例工厂。

## 工厂模式解决的核心问题

在面向对象编程中，创建对象是一个常见的操作。但是，直接使用 `new` 关键字创建对象会带来以下问题：

### 传统模式的问题示例

```java
// 传统方式：客户端直接创建对象
public class KeyboardClient {
    public void useKeyboard(String brand) {
        Keyboard keyboard;

        // 问题1：紧耦合 - 客户端直接依赖具体实现类
        if ("HP".equals(brand)) {
            keyboard = new HPKeyboard();
        } else if ("Dell".equals(brand)) {
            keyboard = new DellKeyboard();
        } else if ("Lenovo".equals(brand)) {
            keyboard = new LenovoKeyboard();
        } else {
            throw new IllegalArgumentException("不支持的品牌: " + brand);
        }

        // 问题2：复杂的初始化逻辑散布在客户端
        keyboard.initialize();
        keyboard.loadDrivers();
        keyboard.calibrate();

        // 使用键盘
        keyboard.print();
    }
}

// 问题3：多个客户端都需要重复相同的创建逻辑
public class AnotherKeyboardClient {
    public void processInput(String brand) {
        Keyboard keyboard;

        // 相同的创建逻辑重复出现
        if ("HP".equals(brand)) {
            keyboard = new HPKeyboard();
        } else if ("Dell".equals(brand)) {
            keyboard = new DellKeyboard();
        } else if ("Lenovo".equals(brand)) {
            keyboard = new LenovoKeyboard();
        } else {
            throw new IllegalArgumentException("不支持的品牌: " + brand);
        }

        // 相同的初始化逻辑重复出现
        keyboard.initialize();
        keyboard.loadDrivers();
        keyboard.calibrate();

        keyboard.input(new Context("data", "operation"));
    }
}
```

### 传统模式的具体问题

1. **紧耦合**：客户端代码直接依赖具体的实现类（`HPKeyboard`、`DellKeyboard` 等）
   - 客户端必须知道所有具体产品类的存在
   - 增加了客户端与产品类之间的依赖关系

2. **难以扩展**：添加新的产品类型需要修改客户端代码
   ```java
   // 新增 Apple 键盘时，所有客户端都需要修改
   if ("HP".equals(brand)) {
       keyboard = new HPKeyboard();
   } else if ("Dell".equals(brand)) {
       keyboard = new DellKeyboard();
   } else if ("Lenovo".equals(brand)) {
       keyboard = new LenovoKeyboard();
   } else if ("Apple".equals(brand)) {  // 每个客户端都要加这一行
       keyboard = new AppleKeyboard();
   }
   ```

3. **复杂的初始化**：对象创建可能需要复杂的初始化逻辑
   - 初始化代码散布在各个客户端中
   - 容易遗漏某些初始化步骤
   - 初始化逻辑变更时需要修改所有客户端

4. **违反开闭原则**：对扩展开放，对修改关闭
   - 每次添加新产品都需要修改现有客户端代码
   - 无法在不修改现有代码的情况下扩展新功能

5. **代码重复**：相同的创建和初始化逻辑在多处重复
   - 维护成本高
   - 容易出现不一致的问题

6. **测试困难**：客户端与具体实现类紧耦合
   - 难以进行单元测试
   - 无法轻易替换为测试用的 Mock 对象

### 工厂模式的解决方案

工厂模式通过将对象创建逻辑封装在工厂中，解决了这些问题：

```java
// 使用工厂模式后的客户端代码
public class KeyboardClient {
    private KeyboardFactory factory = new KeyboardFactory();

    public void useKeyboard(String brand) {
        // 1. 解耦：客户端不再依赖具体实现类
        Keyboard keyboard = factory.createKeyboard(brand);

        // 2. 简化：复杂的初始化逻辑封装在工厂中
        // 3. 复用：所有客户端共享相同的创建逻辑
        // 4. 扩展：新增产品只需修改工厂，客户端无需改动

        keyboard.print();
    }
}
```

## 四种工厂模式详解

工厂模式的演进过程体现了设计模式的不断优化：每种模式都是为了解决前一种模式的缺陷而产生的。

### 1. 简单工厂模式 (Simple Factory)

#### 定义
简单工厂模式不是 GoF 23 种设计模式之一，但它是工厂模式的基础。它通过一个工厂类来创建不同类型的产品。

#### 结构图
```
┌─────────────┐    creates    ┌─────────────┐
│   Client    │──────────────→│   Factory   │
└─────────────┘               └─────────────┘
                                     │ creates
                                     ▼
                              ┌─────────────┐
                              │   Product   │
                              └─────────────┘
                                     △
                    ┌────────────────┼────────────────┐
                    │                │                │
            ┌───────────────┐ ┌──────────────┐ ┌──────────────┐
            │ ConcreteProductA│ │ConcreteProductB│ │ConcreteProductC│
            └───────────────┘ └──────────────┘ └──────────────┘
```

#### 核心代码
```java
// 产品接口
public interface Keyboard {
    void print();
    void input(Context context);
}

// 具体产品
public class HPKeyboard implements Keyboard {
    @Override
    public void print() {
        System.out.println("HP键盘正在执行打印操作...");
    }

    @Override
    public void input(Context context) {
        System.out.println("HP键盘正在处理输入: " + context);
    }
}

// 简单工厂
public class KeyboardFactory {
    public Keyboard getInstance(int brand) {
        if (BrandEnum.HP.getCode() == brand) {
            return new HPKeyboard();
        } else if (BrandEnum.LENOVO.getCode() == brand) {
            return new LenovoKeyboard();
        } else if (BrandEnum.DELL.getCode() == brand) {
            return new DellKeyboard();
        }
        throw new IllegalArgumentException("不支持的品牌代码: " + brand);
    }
}
```

#### 特点
- **优点**：实现简单，客户端不需要知道具体产品类
- **缺点**：违背开闭原则，添加新产品需要修改工厂类
- **适用场景**：产品种类少且不经常变化的场景

#### 简单工厂的缺陷
简单工厂的核心问题在于 **违背了开闭原则**：
1. **扩展性差**：每次添加新产品（如苹果键盘），都需要修改 `KeyboardFactory` 类，增加新的 `if-else` 分支
2. **可读性差**：随着产品增多，`getInstance()` 方法会变得越来越长，充满 `if-else` 判断
3. **维护困难**：工厂类承担了所有产品的创建责任，违反了单一职责原则

```java
// 简单工厂的问题示例
public Keyboard getInstance(int brand) {
    if (BrandEnum.HP.getCode() == brand) {
        return new HPKeyboard();
    } else if (BrandEnum.LENOVO.getCode() == brand) {
        return new LenovoKeyboard();
    } else if (BrandEnum.DELL.getCode() == brand) {
        return new DellKeyboard();
    } else if (BrandEnum.APPLE.getCode() == brand) {  // 新增产品需要修改这里
        return new AppleKeyboard();
    } else if (BrandEnum.MICROSOFT.getCode() == brand) {  // 继续增加...
        return new MicrosoftKeyboard();
    }
    // ... 更多 if-else，代码变得臃肿
    return null;
}
```

---

### 2. 工厂方法模式 (Factory Method)

#### 定义
工厂方法模式是 GoF 23 种设计模式之一。它定义一个创建对象的接口，让子类决定实例化哪一个类。

#### 解决的问题
工厂方法模式专门解决简单工厂的 **开闭原则违背问题**：
- **符合开闭原则**：添加新产品时，只需新增工厂子类，无需修改现有代码
- **单一职责**：每个工厂只负责创建一种产品
- **利用多态**：通过工厂接口统一调用，避免大量 `if-else` 判断

#### 结构图
```
┌─────────────┐                    ┌─────────────┐
│   Client    │                    │   Creator   │
└─────────────┘                    └─────────────┘
                                          △
                                          │
                    ┌─────────────────────┼─────────────────────┐
                    │                     │                     │
            ┌───────────────┐     ┌──────────────┐     ┌──────────────┐
            │ConcreteCreatorA│     │ConcreteCreatorB│     │ConcreteCreatorC│
            └───────────────┘     └──────────────┘     └──────────────┘
                    │                     │                     │
                    │ creates             │ creates             │ creates
                    ▼                     ▼                     ▼
            ┌───────────────┐     ┌──────────────┐     ┌──────────────┐
            │ ConcreteProductA│     │ConcreteProductB│     │ConcreteProductC│
            └───────────────┘     └──────────────┘     └──────────────┘
                    △                     △                     △
                    └─────────────────────┼─────────────────────┘
                                          │
                                   ┌─────────────┐
                                   │   Product   │
                                   └─────────────┘
```

#### 核心代码
```java
// 抽象工厂接口
public interface IKeyboardFactory {
    Keyboard getInstance();
}

// 具体工厂实现
public class HPKeyboardFactory implements IKeyboardFactory {
    @Override
    public Keyboard getInstance() {
        System.out.println("HP工厂正在生产HP键盘...");
        return new HPKeyboard();
    }
}

// 工厂提供者（解决动态选择问题）
public class KeyboardFactoryProvider {
    public static IKeyboardFactory getFactory(int brand) {
        if (BrandEnum.HP.getCode() == brand) {
            return new HPKeyboardFactory();
        } else if (BrandEnum.LENOVO.getCode() == brand) {
            return new LenovoKeyboardFactory();
        } else if (BrandEnum.DELL.getCode() == brand) {
            return new DellKeyboardFactory();
        }
        throw new IllegalArgumentException("不支持的品牌代码: " + brand);
    }

    public static void demon(int brandCode) {

        // 动态获取工厂
        IKeyboardFactory factory = getFactory(brandCode);

        // 使用工厂创建产品
        Keyboard keyboard = factory.getInstance();
        keyboard.print();
    }
}
```

#### 特点
- **优点**：符合开闭原则，利用多态避免 if-else 判断
- **缺点**：增加了类的数量，失去了根据参数动态选择的能力
- **解决方案**：通过工厂提供者重新获得动态选择能力
- **适用场景**：需要频繁扩展产品种类的场景

#### 工厂方法的缺陷
工厂方法模式虽然在一定程度上改善了开闭原则，但实际上并没有彻底解决问题：

1. **开闭原则未彻底解决**：虽然添加新产品不需要修改现有工厂类，但工厂提供者（Factory Provider）仍然需要修改
2. **类爆炸**：每一种品牌对应一个工厂子类，如果业务涉及的子类越来越多，系统中类的个数成倍增加
3. **单一产品限制**：每个工厂只能创建一种产品，无法处理产品族的概念
4. **缺乏产品一致性**：无法保证创建的相关产品来自同一个产品族

**关键问题分析**：
```java
// 工厂提供者仍然违背开闭原则
public static IKeyboardFactory getFactory(int brand) {
    if (BrandEnum.HP.getCode() == brand) {
        return new HPKeyboardFactory();
    } else if (BrandEnum.LENOVO.getCode() == brand) {
        return new LenovoKeyboardFactory();
    } else if (BrandEnum.DELL.getCode() == brand) {
        return new DellKeyboardFactory();
    } else if (BrandEnum.APPLE.getCode() == brand) {  // 新增品牌仍需修改这里
        return new AppleKeyboardFactory();
    }
    throw new IllegalArgumentException("不支持的品牌代码: " + brand);
}
```

**真正的解决方案**：使用反射、配置文件或注册机制来实现完全的开闭原则：

```java
// 方案1：使用注册机制
public class KeyboardFactoryRegistry {
    private static final Map<Integer, Class<? extends IKeyboardFactory>> factoryMap = new HashMap<>();

    // 静态注册（可以通过配置文件或注解扫描实现）
    static {
        register(BrandEnum.HP.getCode(), HPKeyboardFactory.class);
        register(BrandEnum.DELL.getCode(), DellKeyboardFactory.class);
        register(BrandEnum.LENOVO.getCode(), LenovoKeyboardFactory.class);
    }

    public static void register(int brand, Class<? extends IKeyboardFactory> factoryClass) {
        factoryMap.put(brand, factoryClass);
    }

    public static IKeyboardFactory getFactory(int brand) {
        Class<? extends IKeyboardFactory> factoryClass = factoryMap.get(brand);
        if (factoryClass == null) {
            throw new IllegalArgumentException("不支持的品牌代码: " + brand);
        }
        try {
            return factoryClass.newInstance();
        } catch (Exception e) {
            throw new RuntimeException("创建工厂失败", e);
        }
    }
}

// 方案2：使用配置文件
// factory.properties:
// 1=com.example.factory.method.HPKeyboardFactory
// 2=com.example.factory.method.DellKeyboardFactory
// 3=com.example.factory.method.LenovoKeyboardFactory

public class ConfigurableKeyboardFactory {
    private static final Map<Integer, IKeyboardFactory> factoryCache = new HashMap<>();

    static {
        loadFactoriesFromConfig();
    }

    private static void loadFactoriesFromConfig() {
        Properties props = new Properties();
        try (InputStream is = ConfigurableKeyboardFactory.class
                .getResourceAsStream("/factory.properties")) {
            props.load(is);
            for (String key : props.stringPropertyNames()) {
                int brand = Integer.parseInt(key);
                String className = props.getProperty(key);
                Class<?> clazz = Class.forName(className);
                IKeyboardFactory factory = (IKeyboardFactory) clazz.newInstance();
                factoryCache.put(brand, factory);
            }
        } catch (Exception e) {
            throw new RuntimeException("加载工厂配置失败", e);
        }
    }

    public static IKeyboardFactory getFactory(int brand) {
        IKeyboardFactory factory = factoryCache.get(brand);
        if (factory == null) {
            throw new IllegalArgumentException("不支持的品牌代码: " + brand);
        }
        return factory;
    }
}
```

这样，添加新品牌时只需要：
1. 创建新的工厂类（符合开闭原则）
2. 在配置文件中添加一行配置（无需修改代码）

```java
// 工厂方法的问题示例：类数量爆炸
// 如果有 5 个品牌，就需要 5 个工厂类
public class HPKeyboardFactory implements IKeyboardFactory { ... }
public class DellKeyboardFactory implements IKeyboardFactory { ... }
public class LenovoKeyboardFactory implements IKeyboardFactory { ... }
public class AppleKeyboardFactory implements IKeyboardFactory { ... }
public class MicrosoftKeyboardFactory implements IKeyboardFactory { ... }

// 如果还要支持鼠标，又需要 5 个鼠标工厂类
public class HPMouseFactory implements IMouseFactory { ... }
public class DellMouseFactory implements IMouseFactory { ... }
// ... 类数量继续爆炸
```

---

### 3. 抽象工厂模式 (Abstract Factory)

#### 定义
抽象工厂模式提供一个创建一系列相关或相互依赖对象的接口，而无需指定它们具体的类。

#### 解决的问题
抽象工厂模式专门解决工厂方法的 **类爆炸和产品族一致性问题**：
- **减少工厂类数量**：将产品进行分组，每组中的不同产品由同一个工厂类的不同方法来创建
- **确保产品族一致性**：同一个工厂创建的所有产品都属于同一个产品族，风格统一
- **支持产品族扩展**：添加新的产品族只需新增一个工厂类

#### 结构图
```
┌─────────────┐                    ┌─────────────┐
│   Client    │                    │AbstractFactory│
└─────────────┘                    └─────────────┘
                                          △
                    ┌─────────────────────┼─────────────────────┐
                    │                     │                     │
            ┌───────────────┐     ┌──────────────┐     ┌──────────────┐
            │ConcreteFactoryA│     │ConcreteFactoryB│     │ConcreteFactoryC│
            └───────────────┘     └──────────────┘     └──────────────┘
                    │                     │                     │
                    │ creates             │ creates             │ creates
                    ▼                     ▼                     ▼
    ┌─────────────────────────────┐ ┌─────────────────────────────┐ ┌─────────────────────────────┐
    │     Product Family A        │ │     Product Family B        │ │     Product Family C        │
    │ ┌─────────┐ ┌─────────┐    │ │ ┌─────────┐ ┌─────────┐    │ │ ┌─────────┐ ┌─────────┐    │
    │ │ProductA1│ │ProductA2│    │ │ │ProductB1│ │ProductB2│    │ │ │ProductC1│ │ProductC2│    │
    │ └─────────┘ └─────────┘    │ │ └─────────┘ └─────────┘    │ │ └─────────┘ └─────────┘    │
    └─────────────────────────────┘ └─────────────────────────────┘ └─────────────────────────────┘
```

#### 核心代码
```java
// 抽象工厂接口
public interface IFactory {
    MainFrame createMainFrame();
    Monitor createMonitor();
    Keyboard createKeyboard();
}

// 具体工厂实现（Dell产品族）
public class DellFactory implements IFactory {
    @Override
    public MainFrame createMainFrame() {
        System.out.println("Dell工厂正在制造Dell主机...");
        return new DellMainFrame();
    }

    @Override
    public Monitor createMonitor() {
        System.out.println("Dell工厂正在制造Dell显示器...");
        return new DellMonitor();
    }

    @Override
    public Keyboard createKeyboard() {
        System.out.println("Dell工厂正在制造Dell键盘...");
        return new DellKeyboard();
    }
}

// 客户端使用
public class Client {
    public void createComputerSet(IFactory factory) {
        MainFrame mainFrame = factory.createMainFrame();
        Monitor monitor = factory.createMonitor();
        Keyboard keyboard = factory.createKeyboard();
        // 确保所有产品都来自同一个产品族
    }
}
```

#### 特点
- **优点**：确保产品族的一致性，减少工厂子类数量
- **缺点**：扩展产品种类困难（需要修改所有工厂）
- **适用场景**：需要创建产品族，确保产品一致性的场景

---

### 4. 单例工厂模式 (Singleton Factory)

#### 定义
单例工厂模式结合了单例模式和工厂模式，确保工厂实例全局唯一，同时可以控制产品的创建策略。

#### 结构图
```
┌─────────────┐                    ┌─────────────────────┐
│   Client    │                    │  SingletonFactory   │
└─────────────┘                    │ ┌─────────────────┐ │
                                   │ │ -instance       │ │
                                   │ │ +getInstance()  │ │
                                   │ │ +createProduct()│ │
                                   │ │ +getSingleton() │ │
                                   │ └─────────────────┘ │
                                   └─────────────────────┘
                                             │
                                             │ creates/caches
                                             ▼
                                   ┌─────────────────────┐
                                   │      Products       │
                                   │ ┌─────────────────┐ │
                                   │ │ Cached Products │ │
                                   │ │ New Products    │ │
                                   │ └─────────────────┘ │
                                   └─────────────────────┘
```

#### 核心代码
```java
public class SingletonKeyboardFactory {
    // 工厂实例（单例）
    private static volatile SingletonKeyboardFactory instance;

    // 产品实例缓存
    private volatile HPKeyboard hpKeyboard;
    private volatile DellKeyboard dellKeyboard;
    private volatile LenovoKeyboard lenovoKeyboard;

    // 私有构造函数
    private SingletonKeyboardFactory() {
        System.out.println("SingletonKeyboardFactory 实例被创建");
    }

    // 双重检查锁定获取工厂实例
    public static SingletonKeyboardFactory getInstance() {
        if (instance == null) {
            synchronized (SingletonKeyboardFactory.class) {
                if (instance == null) {
                    instance = new SingletonKeyboardFactory();
                }
            }
        }
        return instance;
    }

    // 创建新实例（多例产品）
    public Keyboard createKeyboard(int brand) {
        if (BrandEnum.HP.getCode() == brand) {
            return new HPKeyboard();
        }
        // ... 其他品牌
    }

    // 获取单例产品（懒加载 + 双重检查）
    public Keyboard getSingletonKeyboard(int brand) {
        if (BrandEnum.HP.getCode() == brand) {
            if (hpKeyboard == null) {
                synchronized (this) {
                    if (hpKeyboard == null) {
                        hpKeyboard = new HPKeyboard();
                    }
                }
            }
            return hpKeyboard;
        }
        // ... 其他品牌
    }
}
```

#### 特点
- **优点**：工厂全局唯一，节省内存；可控制产品创建策略；线程安全
- **缺点**：实现复杂度较高
- **适用场景**：需要全局唯一工厂实例，且要控制产品实例数量的场景

## 四种模式对比总结

| 模式 | 工厂数量 | 产品数量 | 工厂实例 | 主要特点 | 解决的问题 |
|------|----------|----------|----------|----------|------------|
| 简单工厂 | 1个 | 1类 | 多例 | 简单但不灵活 | 封装对象创建逻辑 |
| 工厂方法 | 多个 | 1类 | 多例 | 灵活但类多 | 符合开闭原则 |
| 抽象工厂 | 多个 | 多类 | 多例 | 产品族一致 | 确保产品族一致性 |
| 单例工厂 | 1个 | 1类 | 单例 | 全局唯一工厂 | 控制工厂和产品实例 |

## 概括

### 简单工厂
- 方便集中管理各竞对

### 工厂方法
- 解决「简单工厂」新增产品时需修改代码，违反开闭原则
- 避免大量 if-else 判断

### 抽象工厂
- 解决「工厂方法」产品和工厂类需成对出现导致的文件数量膨胀

### 单例工厂
- 需要全局唯一的工厂实例
- 要控制产品实例的数量
- 工厂创建成本高，需要复用



