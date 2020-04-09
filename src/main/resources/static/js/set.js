layui.use([ 'layer', 'form' ], function() {
	var form = layui.form;
	var layer = layui.layer;
	$ = layui.jquery;
	$.ajax({
		url : 'http://localhost:2019/set/getProperties',
		method : 'get',
		dataType : 'JSON',
		success : function(res) {
			var excelColumns = res.excelColumns;
			var fieldname = res.fieldname;
			var sheet = res.sheet;
			var exportPath = res.exportPath;
			var threaCount = res.threadCount;
			var baiduAk = res.baiduAk;
			$("#excelColumns").attr("value", excelColumns);
			$("#fieldname").attr("value", fieldname);
			$("#sheet").attr("value", sheet);
			$("#exportPath").each(function() {
				$(this).children("option").each(function() {
					if (this.value == exportPath) {
						$(this).attr("selected", "selected")
					}
				});
			});
			$("#threadCount").each(function() {
				$(this).children("option").each(function() {
					if (this.value == threaCount) {
						$(this).attr("selected", "selected")
					}
				});
			});
			$("#add").on('click', function() {
				layer.open({
					type : 1,
					title : "自定义设置",
					skin : "myclass",// 自定样式
					area : [ "400px", "360px" ],
					content : $("#set").html(),
					async : false,
					cache : false,
					success : function(layero, index) {
						form.render('select');
					},
					yes : function() {
					}
				});
			});
			$("#baiduAk").attr("value", baiduAk);
		},
		error : function(data) {
		}
	});

	form.verify({
		questionnaireName : function(value) {
			if (value.length < 5) {
				return '标题至少得5个字符啊';
			}
		}
	});
	form.on('submit(suu)', function(data) {
		$.ajax({
			url : 'http://localhost:2019/set/setProperties',
			method : 'post',
			cache : false,
			data : JSON.stringify(data.field),
			dataType : 'JSON',
			closeBtn : 1,
			contentType : "application/json",
			success : function(res) {
				layer.msg("修改成功。正在重新加载页面");
				setTimeout(function() { // 使用 setTimeout（）方法设定定时2000毫秒
					window.location.reload();// 页面刷新
				}, 1000);
			},
			error : function(res) {
				layer.msg("保存失败");
			}
		});
		return false;
	});

});