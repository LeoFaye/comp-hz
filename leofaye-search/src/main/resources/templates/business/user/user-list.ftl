<!DOCTYPE html>  
<html>
<head>
<link href="https://cdn.bootcss.com/bootstrap/3.3.7/css/bootstrap.min.css" rel="stylesheet"/>
<link href="https://cdn.bootcss.com/bootstrap/3.3.7/css/bootstrap-theme.min.css" rel="stylesheet" />
<script src="https://cdn.bootcss.com/jquery/1.11.3/jquery.min.js"></script>
<script src="https://cdn.bootcss.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
</head>  
<style>
	.input-add{margin:10px 0;}
	.list{margin-top:20px;}
</style>

<script>
$(function(){
	
});
</script>

<body>
  
<div class="container" style="margin-top:10px;">
	
	<form action="http://localhost:8080/user/add" method="post" modelAttribute="User">
	
		
		
		<#if result ?? && result.code.value == 0>
			<div class="alert alert-success alert-dismissible" role="alert">
			  <button type="button" class="close" data-dismiss="alert" aria-label="Close"><span aria-hidden="true">&times;</span></button>
			  <strong>${result.message}</strong>
			</div>
		</#if>
	
		<div class="row input-add">
			<div class="col-xs-3">
				<input name="name" class="form-control" placeholder="user name">
			</div>
			<div class="col-xs-3">
				<input name="age" class="form-control" placeholder="user age">
			</div>
			<div class="col-xs-5">
				<button class="btn btn-success" type="submit">Add User</button>
			</div>
		</div>
	
		<div class="list">
			<#if users ??>
				<table class="table table-bordered">
					<thead>
						<tr>
							<th>id</th>
							<th>userName</th>
							<th>createTime</th>
						</tr>
					</thead>
					<tbody>
						<#list users as user>
							<tr>
								<td>${user.id}</td>
								<td>${user.name}</td>
								<!--<td>${user.createTime?date} ${user.createTime?time}</td>-->
								<td>
									${user.createTime?string('yyyy-MM-dd hh:mm:ss')}
								</td>
							</tr>
						</#list>
					</tbody>
				</table>
			</#if>
		</div>
	</form>
			
</div>
</body>  
</html> 