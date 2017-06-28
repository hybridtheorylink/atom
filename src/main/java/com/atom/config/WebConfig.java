
package com.atom.config;


import javax.sql.DataSource;

import com.atom.mvc.controller.CommonController;
import com.atom.mvc.controller.MsgController;
import com.atom.plugins.disruptor.DisruptorPlugin;
import com.atom.plugins.mongo.plugin.MongoJFinalPlugin;
import com.jfinal.config.Constants;
import com.jfinal.config.Handlers;
import com.jfinal.config.Interceptors;
import com.jfinal.config.JFinalConfig;
import com.jfinal.config.Plugins;
import com.jfinal.config.Routes;
import com.jfinal.core.ApplicationContextKit;
import com.jfinal.core.JFinal;
import com.jfinal.kit.PropKit;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.druid.DruidPlugin;
import com.jfinal.plugin.redis.RedisPlugin;
import com.jfinal.template.Engine;

import sun.net.www.content.text.plain;

/**
 * 本 demo 仅表达最为粗浅的 jfinal 用法，更为有价值的实用的企业级用法
 * 详见 JFinal 俱乐部: http://jfinal.com/club
 * 
 * API引导式配置
 */
public class WebConfig extends JFinalConfig {
	
	/**
	 * 运行此 main 方法可以启动项目，此main方法可以放置在任意的Class类定义中，不一定要放于此
	 * 
	 * 使用本方法启动过第一次以后，会在开发工具的 debug、run config 中自动生成
	 * 一条启动配置，可对该自动生成的配置再添加额外的配置项，例如 VM argument 可配置为：
	 * -XX:PermSize=64M -XX:MaxPermSize=256M
	 */
	public static void main(String[] args) {
		/**
		 * 特别注意：Eclipse 之下建议的启动方式
		 */
		JFinal.start("WebRoot", 8080, "/", 5);

		/**
		 * 特别注意：IDEA 之下建议的启动方式，仅比 eclipse 之下少了最后一个参数
		 */
		// JFinal.start("WebRoot", 80, "/");
	}
	
	/**
	 * 配置常量
	 */
	public void configConstant(Constants me) {
		// 加载少量必要配置，随后可用PropKit.get(...)获取值
		PropKit.use("init.properties");
		me.setDevMode(PropKit.getBoolean("devMode", false));
	}
	
	/**
	 * 配置路由
	 */
	public void configRoute(Routes me) {
//		me.add("/", IndexController.class, "/index");	// 第三个参数为该Controller的视图存放路径
//		me.add("/blog", BlogController.class);			// 第三个参数省略时默认与第一个参数值相同，在此即为 "/blog"
//		me.add("/api", CommonController.class);
		me.add("/api", CommonController.class);
		me.add("/msg", MsgController.class);
		
	}
	
	public void configEngine(Engine me) {
//		me.addSharedFunction("/common/_layout.html");
//		me.addSharedFunction("/common/_paginate.html");
	}
	
	public static DruidPlugin createDruidPlugin() {
		return new DruidPlugin(PropKit.get("mysql.jdbcUrl"), PropKit.get("mysql.userName"), PropKit.get("mysql.passWord").trim());
	}
	
	/**
	 * 配置插件
	 */
	public void configPlugin(Plugins me) {
		// 配置C3p0数据库连接池插件
		RedisPlugin redis = new RedisPlugin("weibus", "localhost");
		me.add(redis);
		DataSource dataSource = ApplicationContextKit.getBean("druidDataSource");
		ActiveRecordPlugin arp = new ActiveRecordPlugin(dataSource);
//		arp.setDevMode(true);
//		arp.setShowSql(true);
//		arp.setContainerFactory(new CaseInsensitiveContainerFactory(true));
//		arp.setDialect(new MysqlDialect());
//		DruidPlugin druidPlugin = createDruidPlugin();
//		me.add(druidPlugin);
		
		// 配置ActiveRecord插件
//		ActiveRecordPlugin arp = new ActiveRecordPlugin(druidPlugin);
		// 所有映射在 MappingKit 中自动化搞定
//		_MappingKit.mapping(arp);
		MongoJFinalPlugin mongo = new MongoJFinalPlugin();
		mongo.add("127.0.0.1",27017);
		mongo.setDatabase("atom");
		me.add(mongo);
		me.add(arp);
		
		DisruptorPlugin disruptor = new DisruptorPlugin();
	    me.add(disruptor);
	}
	
	/**
	 * 配置全局拦截器
	 */
	public void configInterceptor(Interceptors me) {
		
	}
	
	/**
	 * 配置处理器
	 */
	public void configHandler(Handlers me) {
		
	}
}
