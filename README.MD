<h1 align="center">
  <br>
  <br>
  TConfig
  <h4 align="center">
  源自 MinecraftForge 的配置文件管理系统
  </h4>
  <h5 align="center">
<a href="#maven">Maven 导入</a>&nbsp;&nbsp;
<a href="#gradle">Gradle 导入</a>&nbsp;&nbsp;
<a href="#dev">开发</a>&nbsp;&nbsp;
<a href="#faq">常见问题</a>&nbsp;&nbsp;
<a href="#license">开源条款</a>
</h5>
  <br>
  <br>
  <br>
</h1>

## 导入

<a name="maven"></a>

#### Maven 导入
没有添加 JitPack 的 Repo 的话首先添加 Repo, 在 pom 里面把这些粘贴进去：

```xml
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>
```

然后添加这个库：

```xml
<dependency>
    <groupId>com.github.nitu2003</groupId>
    <artifactId>TConfig</artifactId>
    <version>1.0</version>
</dependency>
```

然后 Reimport 就完事了！

<a name="gradle"></a>

#### Gradle 导入
没有添加 JitPack 的 Repo 的话首先添加 Repo, 在 pom 里面把这些粘贴进去:

```gradle
allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}
```

然后添加这个库:

```gradle
dependencies {
    implementation 'com.github.nitu2003:TConfig:1.0'
}
```

<!-- HyDevelop：每次更新都要手动改这些版本号好烦的_(:з」∠)_... -->
<!-- Taskeren： 那就少更新啊！ -->

#### [其他导入(SBT / Leiningen)](https://jitpack.io/#nitu2003/TConfig)

<a name="dev"></a>

## 开发

#### 创建一个设定档
```java
// 传入一个任意文件
Configuration c = new Configuration(new File("config.cfg"));
```

#### 从设定档中读取数据
```java
// 获取一个字符串（键值，类别，默认值，注解）
String s = c.getString(name, category, defaultValue, comment);
// 获取一个整形（键值，类别，默认值，最小值，最大值，注解）
int i = c.getInt(name, category, defaultValue, minValue, maxValue, comment);
// 获取一个布尔型（键值，类别，默认值，注解）
boolean b = c.getBoolean(name, category, defaultValue, comment);
// 获取一个浮点型（键值，类别，默认值，最小值，最大值，注解）
float f = c.getFloat(name, category, defaultValue, minValue, maxValue, comment);
// 获取一个字符串数组（键值，类型，默认值，注解）
String[] sa = c.getStringList(name, category, defaultValues, comment);
```

其他的就自己去试试看吧，我也没用过（跑）

#### 保存设定档
```java
// 空文件读取完毕后，需要保存默认值，请务必执行此方法！
c.save();
```

<a name="faq"></a>

## 常见问题

#### 为什么新设定档没有保存？
读取完成后执行 `c.save()` 啦！

#### 有没有其他数组的读取？
没有，你可以利用 `getStringList` 来做自己的。  
~~*当然我也欢迎你提交其他类型的数组方法，大改就是转到字符串再转回来这样子！*~~

<a name="license"></a>

## 开源条款
程序源代码部分按照 LGPL 2.1 开源  
Codes are licensed under LGPL 2.1  
Readme 借鉴自 [PicqBotX](https://github.com/HyDevelop/PicqBotX)，获得授权使用。  
Readme is copied from [PicqBotX](https://github.com/HyDevelop/PicqBotX) with license.
