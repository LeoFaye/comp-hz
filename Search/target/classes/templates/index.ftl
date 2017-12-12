<!DOCTYPE html>  
<html>
<head>
<link href="https://cdn.bootcss.com/bootstrap/3.3.7/css/bootstrap.min.css" rel="stylesheet"/>
<link href="https://cdn.bootcss.com/bootstrap/3.3.7/css/bootstrap-theme.min.css" rel="stylesheet" />
<script src="https://cdn.bootcss.com/jquery/1.11.3/jquery.min.js"></script>
<script src="https://cdn.bootcss.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
</head>  
<style>
	.create-index{background-color:blue; padding:10px;}
	.left div{margin-top:15px;}
</style>

<script>
$(function(){
	$(".btn").on("click", function(){
		var t = $(this);
		if (t.hasClass("index-delete")) {
			$("#form1").attr("action", "http://localhost:8080/index/delete").submit();
		} else if (t.hasClass("index-create")) {
			$("#form1").attr("action", "http://localhost:8080/index/create").submit();
		} else if (t.hasClass("index-add")) {
			$("#form1").attr("action", "http://localhost:8080/index/add").submit();
		} else {
			$("#form1").attr("action", "http://localhost:8080/index/search").submit();
		}
	});
	$("input").on("keydown", function(event) {
		if(event.keyCode == 13) {
			return false;
		}
	});
});
</script>

<body>
  
<div class="container" style="margin-top:10px;">
	
	
		<div class="col-xs-offset-2 col-xs-8">
			<form id="form1" action="" method="post">
				<div class="input-group">
					<input class="form-control" name="input">
					<div class="input-group-btn">
						<button class="btn btn-warning index-delete" type="button">Delete Index</button>
						<button class="btn btn-warning index-create" type="button">Create Index</button>
						<button class="btn btn-warning index-add" type="button">Add Index</button>
						<button class="btn btn-warning search" type="button">Search</button>
					</div>
				</div>
			</form>
			<form id="form2" action="" method="post" style="margin-top:10px;">
				<div class="row">
					<div class="col-xs-2">
						<input class="form-control" name="index" placeholder="index">
					</div>
					<div class="col-xs-2">
						<input class="form-control" name="type" placeholder="type">
					</div>
					<div class="col-xs-2">
						<button class="btn btn-warning set-mapping">Set Mapping</button>
					</div>
				</div>
			</form>
		</div>
	
		<div class="col-xs-offset-2 col-xs-8" style="margin-top:15px;">
			<#if manageUsers ??>
			<table class="table table-bordered">
				<thead>
					<tr>
						<th>id</th>
						<th>userName</th>
						<th>createTime</th>
					</tr>
				</thead>
				<tbody>
					<#list manageUsers as manageUser>
						<tr>
							<td>${manageUser.id}</td>
							<td>${manageUser.userName}</td>
							<td>${manageUser.createTime?date} ${manageUser.createTime?time}</td>
						</tr>
					</#list>
				</tbody>
			</table>
		</#if>
		</div>
	
</div>
</body>  
</html> 