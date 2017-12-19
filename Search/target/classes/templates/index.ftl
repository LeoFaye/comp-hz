<!DOCTYPE html>  
<html>
<head>
<link href="https://cdn.bootcss.com/bootstrap/3.3.7/css/bootstrap.min.css" rel="stylesheet"/>
<link href="https://cdn.bootcss.com/bootstrap/3.3.7/css/bootstrap-theme.min.css" rel="stylesheet" />
<script src="https://cdn.bootcss.com/jquery/1.11.3/jquery.min.js"></script>
<script src="https://cdn.bootcss.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
</head>  
<style>
	
</style>

<script>
$(function(){
	$(".btn").on("click", function(){
		var t = $(this);
		if (t.hasClass("index-delete")) {
			$("#form1").attr("action", "http://localhost:8080/index/delete").submit();
		} else if (t.hasClass("index-create")) {
			$("#form1").attr("action", "http://localhost:8080/index/create").submit();
		} else if (t.hasClass("index-search")) {
			$("#form1").attr("action", "http://localhost:8080/index/search").submit();
		} else if (t.hasClass("mapping-create")) {
			$("#form2").attr("action", "http://localhost:8080/mapping/create").submit();
		} else if (t.hasClass("data-index")) {
			$("#form2").attr("action", "http://localhost:8080/data/index").submit();
		} else if () {
			location.href = http://localhost:8080/view/mapping/{index}
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
			<div class="btn-group" role="group" aria-label="..." style="margin-bottom:10px;">
				<button class="btn btn-success view-mapping" type="button">View Mapping</button>
				<button class="btn btn-success view-indices" type="button">View Indices</button>
			</div>
		
			<form id="form1" action="" method="post">
				<div class="input-group">
					<input class="form-control" name="index">
					<div class="input-group-btn">
						<button class="btn btn-danger index-delete" type="button">Delete Index</button>
						<button class="btn btn-primary index-create" type="button">Create Index</button>
						<button class="btn btn-default index-search" type="button">Search</button>
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
						<button class="btn btn-warning mapping-create">Set Mapping</button>
					</div>
					<div class="col-xs-2">
						<button class="btn btn-success data-index" type="button">Index</button>
					</div>
				</div>
			</form>
		</div>
	
		<div class="col-xs-offset-2 col-xs-8 content" style="margin-top:15px;">
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
		<#if res ??>
			${res}
		</#if>
		<#if index ??>
			<span style="font-weight:bold;">[ ${index} ]</span>
		</#if>
		</div>
	
</div>
</body>  
</html> 