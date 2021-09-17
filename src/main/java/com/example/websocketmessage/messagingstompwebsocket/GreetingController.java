package com.example.websocketmessage.messagingstompwebsocket;

import com.example.websocketmessage.domain.Message;
import com.example.websocketmessage.domain.ReceiveMessage;
import com.example.websocketmessage.domain.SendMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.simp.user.SimpUser;
import org.springframework.messaging.simp.user.SimpUserRegistry;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.util.HtmlUtils;

import javax.annotation.Resource;
import javax.websocket.server.PathParam;
import java.security.Principal;
import java.util.Map;

@Slf4j
@Controller
public class GreetingController {

	@Resource
	private SimpMessagingTemplate messagingTemplate;

	@Resource
	private SimpUserRegistry userRegistry;

	/**
	 * 点对点的通信
	 * @param principal 记录的当前会话的用户
	 * @param accessor
	 * @param message 可接收的对象参数形式
	 * @param receiveMessage 可接收的任一对象参数形式
	 * @param data 可接收的Map参数形式
	 * @throws InterruptedException
	 */
	@MessageMapping(value = "/individual")
	public void templateTest1(Principal principal,
							  StompHeaderAccessor accessor,
							  SendMessage message,
							  ReceiveMessage receiveMessage,
							  Map<String,String> data) throws InterruptedException {

		log.info("当前在线人数:" + userRegistry.getUserCount());
		int i = 1;
		for (SimpUser user : userRegistry.getUsers()) {
			log.info("用户" + i++ + "---" + user);
		}
		Thread.sleep(5000);
		// 发送消息给指定用户
		// 该调用方法中的user参数与需要发送给目的用户的用户名一致
		messagingTemplate.convertAndSendToUser(principal.getName(), "/queue/dragon", message);
	}

	/**
	 * @MessageMapping("/broadcast") 注解的方法将用来接收"/app/broadcast路径发送来的消息，
	 * 	 在注解方法中对消息进行处理后，再将消息转发到 @SendTo 定义的路径上，而@SendTo路径是个前缀为"/topic"的路径，
	 * 	 因此该消息将被交给消息代理 broker ，再由 broker 进行广播。
	 *
	 *  这里也可以用更灵活的方式，使用spring的SimpMessagingTemplate模板，
	 *  messagingTemplate.convertAndSend方法广播式通信
	 * @param message 该message为客户端发送到"/app/broadcast"的JSON数据
	 * @return 服务器上，该方法接受处理message后将处理后的消息发送到 @SendTo("/topic/getResponse")
	 * @throws Exception
	 */
	/*广播通信*/
	@MessageMapping("/broadcast")
	@SendTo("/topic/getResponse")
	public Message greeting(SendMessage message) throws Exception {
		System.out.println(message.toString());
		Thread.sleep(1000);
		log.info("接收消息1");
		Message message1 = new Message();
		message1.setContent("messagingTemplate");
		message1.setSendId(999L);
		message1.setId(666L);
		message1.setReceiverId(999L);
		message1.setType(111);
		// messagingTemplate.convertAndSend("/topic/getResponse", message1);
		message1.setContent(message.getContent());
		message1.setSendId(message.getSendId());
		message1.setId(message.getId());
		message1.setReceiverId(message.getReceiverId());
		message1.setType(message.getType());
		return message1;
	}

	@MessageMapping("/broadcast2")
	public ReceiveMessage greetingTwo(Message message) throws Exception {
		System.out.println(message.toString());
		Thread.sleep(1000);
		log.info("存数据库2");
		ReceiveMessage receiveMessage = new ReceiveMessage();
		receiveMessage.setContent("readyTest");
		receiveMessage.setName("test1");
		messagingTemplate.convertAndSend("/topic/getResponse", receiveMessage);
		return receiveMessage;
	}

}
