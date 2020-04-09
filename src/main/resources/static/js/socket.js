var socket;
if (typeof (WebSocket) == "undefined") {
	console.log("您的浏览器不支持WebSocket");
} else {
	console.log("您的浏览器支持WebSocket");
	//实现化WebSocket对象，指定要连接的服务器地址与端口  建立连接  
	//socket = new WebSocket("${basePath}websocket/${cid}".replace("http","ws")); 
	socket = new WebSocket("ws://localhost:2019/websocket/20");
	//打开事件  
	socket.onopen = function() {
		console.log("Socket 已打开");
		//socket.send("这是来自客户端的消息" + location.href + new Date());  
	};
	//获得消息事件  
	var i = 0;
	var j = 0;
	socket.onmessage = function(msg) {
		if(msg.data.substring(0, 1) == "@"){
			i = 0;
			j = 100 / parseInt(msg.data.substring(1, msg.data.length));
		}
		console.log(msg.data);
		//发现消息进入    开始处理前端触发逻辑
		$("#logger").val(msg.data);
		i = i + j;
		if(i > 100){
			i = 100;
		}
		element.progress('demo', Math.ceil(i)+'%');
		$('.site-demo-active').on('click', function() {
			var othis = $(this), type = $(this).data('type');
			active[type] ? active[type].call(this, othis) : '';
		});
	};
	//关闭事件  
	socket.onclose = function() {
		console.log("Socket已关闭");
	};
	//发生了错误事件  
	socket.onerror = function() {
		alert("Socket发生了错误");
		//此时可以尝试刷新页面
	}
}