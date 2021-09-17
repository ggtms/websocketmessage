package com.example.websocketmessage.messagingstompwebsocket;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketTransportRegistration;

/**
 * @author Lenovo
 */
@Configuration
@EnableWebSocketMessageBroker   // 开启WebSocket代理
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

	/**
	 * 配置信息代理
	 */
	@Override
	public void configureMessageBroker(MessageBrokerRegistry config) {
		// 启用基于内存的消息代理，以将消息传送回以 "/topic" 为前缀的目标上的客户端  订阅Broker名称(支持多个)
		config.enableSimpleBroker("/queue", "/topic");
		// 此前缀将用于定义所有消息映射（全局使用的消息前缀）
		// 表示配置一个或多个前缀，通过这些前缀过滤出需要被注解方法处理的消息。
		// 例如，前缀为 /app 的 destination 可以通过@MessageMapping注解的方法处理，
		// 而其他 destination （例如 /topic /queue）将被直接交给 broker 处理
		config.setApplicationDestinationPrefixes("/app");
		// 点对点使用的订阅前缀（客户端订阅路径上会体现出来），不设置的话，默认也是/user/
		config.setUserDestinationPrefix("/user/");
	}

	/**
	 * 注册stomp的端点
	 */
	@Override
	public void registerStompEndpoints(StompEndpointRegistry registry) {
		// 注册一个websocket的连接服务
		registry.addEndpoint("/spring-boot-websocket")
				// 配置拦截websocket连接请求
				.addInterceptors(new LoadAuthHandshakeInterceptor())
				// 支持socketJS
				.withSockJS();
	}

	/**
	 * 配置客户端入站通道拦截器
	 */
	@Override
	public void configureClientInboundChannel(ChannelRegistration registration) {
		// registration.setInterceptors(createUserInterceptor());
		registration.interceptors(createUserInterceptor());
	}

	/**
	 * 将客户端渠道拦截器加入spring ioc容器
	 * @return
	 */
	@Bean
	public UserInterceptor createUserInterceptor() {
		return new UserInterceptor();
	}


	/**
	 * 配置与处理从 WebSocket 客户端接收和发送到 WebSocket 客户端的消息相关的选项
	 * @param registration
	 */
	@Override
	public void configureWebSocketTransport(WebSocketTransportRegistration registration) {
		// 配置入站子协议消息的最大大小  默认值为 64K（即 64 * 1024）
		registration.setMessageSizeLimit(500 * 1024 * 1024);
		// 配置在使用 SockJS 回退选项时向 WebSocket 会话发送消息或 HTTP 响应时要缓冲的最大数据量
		registration.setSendBufferSizeLimit(1024 * 1024 * 1024);
		// 为在使用 SockJS 回退选项时将消息发送到 WebSocket 会话或写入 HTTP 响应时允许的最长时间配置时间限制（以毫秒为单位）
		registration.setSendTimeLimit(200000);
	}

}
