# 缓存管理工具

## 优点

- 采用AOP方法管理缓存，没有业务代码侵入
- 热插拔，管理或不管理缓存只需一个配置文件
- 缓存统一优化简单，完全抛开业务场景
- 切换缓存端方便，需要开发对应缓存客户端的适配器

## 功能

- 可配置需要缓存方法的拦截规则，具体参数配置请参考 `MethodMatchFilterConfigBean`
- 单线程中，源方法（查询方法）执行次数被控制只能执行一次，防止对数据库造成不必要压力
- 单线程中，对缓存的查询被控制只能执行一次（存在有效缓存），防止对缓存客户端连接数过多
- 并发情况下，只允许一个线程执行源方法查询数据库，其他线程阻塞等待执行结果并返回

## 示例

参考Spring集成测试用例：
- [applicationContext.xml](https://github.com/qianlixy/cache-manager/blob/master/src/test/resources/applicationContext.xml)
- [applicationCache.xml](https://github.com/qianlixy/cache-manager/blob/master/src/test/resources/applicationCache.xml)

## 框架图

![缓存管理中间件架构图](https://raw.githubusercontent.com/qianlixy/cache-manager/master/doc/images/framework.png)

## 反馈
欢迎指正，qianli_xy@163.com
