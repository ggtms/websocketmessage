var stompClient = null;

function setConnected(connected) {
    $("#connect").prop("disabled", connected);
    $("#disconnect").prop("disabled", !connected);
    if (connected) {
        $("#conversation").show();
    }
    else {
        $("#conversation").hide();
    }
    $("#greetings").html("");
}
// var socket = new SockJS(“http://172.16.10.148:80/spring-boot-websocket“),
// var stompClient = Stomp.over(socket); 创建客户端连接对象。connect()，建立连接，
// connect的成功回调函数里执行subscribe()订阅，订阅的地址/topic/greetings对应服务端的@SendTo地址。
function connect() {
    var socket = new SockJS('/spring-boot-websocket');
    stompClient = Stomp.over(socket);
    //Session.setItem("key",value)
    //Session.setItem("key",value)

    //这里可以改成token
    stompClient.connect({}, function (frame) {
        setConnected(true);
        console.log('Connected: ' + frame);
        // 订阅的点对面的消息
        stompClient.subscribe('/topic/getResponse', function (response) {
            alert("/topic/getResponse your received message:" + response);
            showGreeting(JSON.parse(response.body).content+
                JSON.parse(response.body).name+
                JSON.parse(response.body).sendId);
        });
        // 订阅的点对点的消息
        stompClient.subscribe('/user/queue/dragon', function (response) {
            console.log("/user/queue/dragon: your received message:" + response);
            showGreeting(JSON.parse(response.body).content+
                JSON.parse(response.body).name+
                JSON.parse(response.body).sendId);
        });
    });
}

function disconnect() {
    if (stompClient !== null) {
        stompClient.disconnect();
    }
    setConnected(false);
    console.log("Disconnected,close socket connect");
}

function sendName() {
    // 客户端先产生消息并发送到服务器  |path|header|content|
    stompClient.send("/app/individual", {}, JSON.stringify({'name': $("#name").val(),'content':"test",'receiverId':"0"}));
}

function sendAll() {
    // 产生消息并发送  点对面
    stompClient.send("/app/broadcast", {}, JSON.stringify({'name': $("#names").val(),'type':"1"
        ,'content':"test", 'sendId':"12345", 'receiverId':"0",'state':"0"}));
}
function send() {
    // 产生消息并发送  点对面
    stompClient.send("/app/broadcast2", {}, JSON.stringify({'name': $("#names").val(),'type':"2"
        ,'content':"2", 'sendId':"2", 'receiverId':"2",'state':"2"}));
}

function showGreeting(message) {
    $("#greetings").append("<tr><td>" + message + "</td> <td>" +  message + "2" +"</td></tr>");
}

$(function () {
    $("form").on('submit', function (e) {
        e.preventDefault();
    });
    $( "#connect" ).click(function() { connect(); });
    $( "#disconnect" ).click(function() { disconnect(); });
    $( "#send" ).click(function() { sendName(); });
    $( "#sendAll" ).click(function() { sendAll(); });
    $( "#sends" ).click(function() { send(); });
});

