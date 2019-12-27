#### **一、简单题**

##### 1、Mybatis动态sql是做什么的？都有哪些动态sql？简述一下动态sql的执行原理？

mybaits动态sql可以让我们通过标签的编写sql，根据逻辑判断不同的条件来动态的拼接sql。

常见的动态元素有if、choose(when,otherwise)、trim(where,set)、foreach、bind，除此之外在mybatis3.2开始支持了可插拔的脚本方式lang属性来实现动态sql。

其执行原理为，使用 OGNL 的表达式，从 SQL 参数对象中计算表达式的值,根据表达式的值动态拼接 SQL ，以此来完成动态 SQL 的功能。 

#####2、Mybatis是否支持延迟加载？如果支持，它的实现原理是什么？

Mybatis仅支持association关联对象和collection关联集合对象的延迟加载，association指的就是一对一，collection指的就是一对多查询。在Mybatis配置文件中，可以配置是否启用延迟加载lazyLoadingEnabled=true|false。

它的原理是，使用CGLIB创建目标对象的代理对象，当调用目标方法时，进入拦截器方法，比如调用a.getB().getName()，拦截器invoke()方法发现a.getB()是null值，那么就会单独发送事先保存好的查询关联B对象的sql，把B查询上来，然后调用a.setB(b)，于是a的对象b属性就有值了，接着完成a.getB().getName()方法的调用。这就是延迟加载的基本原理。

当然了，不光是Mybatis，几乎所有的包括Hibernate，支持延迟加载的原理都是一样的。

#####3、Mybatis都有哪些Executor执行器？它们之间的区别是什么？

Mybatis有三种基本的Executor执行器，SimpleExecutor、ReuseExecutor、BatchExecutor：

 SimpleExecutor：每执行一次update或select，就开启一个Statement对象，用完立刻关闭Statement对象 

ReuseExecutor：执行update或select，以sql作为key查找Statement对象，存在就使用，不存在就创建，用完后，不关闭Statement对象，而是放置于Map<String, Statement>内，供下一次使用。简言之，就是重复使用Statement对象 。

BatchExecutor：执行update（没有select，JDBC批处理不支持select），将所有sql都添加到批处理中（addBatch()），等待统一执行（executeBatch()），它缓存了多个Statement对象，每个Statement对象都是addBatch()完毕后，等待逐一执行executeBatch()批处理。与JDBC批处理相同。

#####4、简述下Mybatis的一级、二级缓存（分别从存储结构、范围、失效场景。三个方面来作答）？

一级缓存是SqlSession级别的缓存，在操作数据库时需要构造sqlSession对象，在对象中有一个数据结构（HashMap）用于存储缓存数据。一级缓存的作用域默认是一个SqlSession，不同的sqlSession之间的缓存数据区域（HashMap）是互相不影响的。有多个SqlSession或者分布式的环境下，数据库写操作会引起脏数据。

当整合spring时一级缓存会存在失效的问题，因为mybatis和spring的集成包当中扩展了一个类SqlSessionTemplate，这个类在spring容器启动时被注入给了mapper这个类替代了原来的DefaultSqlSession，SqlSessionTemplate当中的方法不是直接查询，而是经过了动态代理的对象，代理对象增强了原方法，关闭掉了session，由于一级缓存作用范围为SqlSession，会导致原有一级缓存失效。

二级缓存是mapper级别的缓存，多个SqlSession去操作同一个Mapper的sql语句，多个SqlSession可以共用二级缓存，二级缓存是跨SqlSession的，二级缓存本质上也是一个HashMap，与一级缓存不同，一级缓存存储的为查询结果对象，而二级缓存存储的为查询后对象的数据。

如果在分布式环境下，会造成二级缓存失效情况，可以通过扩展cache接口来自定义的实现二级缓存，或者借助mybatis提供的mybatis-redis等分布式缓存来解决该问题。

#####5、简述Mybatis的插件运行原理，以及如何编写一个插件？

mybatis在四大组件（Executor、StatementHandler、ParameterHandler、ResultSetHandler）处提供了简单易用的插件扩展机制，在四大对象创建的时候，

1.每个创建出来的对象不是直接返回的，而实interceptorChain.pluginAll(parameterHandler)；

2.获取到所有的Interceptor(拦截器)（插件需要实现的接口）；调用interceptor.plugin(target);返回target包装后的对象

3.插件机制，我们可以使用插件为目标对象创建一个代理对象；aop（面向切面）我们的插件可以以为四大对象创建出代理对象，代理对象就可以拦截到四大对象的每一个执行；

编写自定义插件的时候我们需要通过实现mybatis插件接口Interceptor，添加注解@Intercepts，可以在该注解中定义多个@Signature对多个地方进行拦截，我们需要在@Signature当中指定拦截的接口、方法名、拦截方法的入参（由于存在方法重载情况，需要来确定方法的唯一性），同时实现其中包含的三个方法：intercept方法，为插件核心方法，每次执行操作的时候，都会进行这个拦截器的方法内，我们可以在当中编写插件的具体实现逻辑；plugin方法，是用来生成target的代理对象，主要用来把拦截器生成一个代理放到拦截器链中；setProperties方法会在初始化时调用，将插件配置的属性从这里设置进来，以供我们获取使用。之后在对sqlMapConfig.xml进行配置添加该自定义的插件即可。

