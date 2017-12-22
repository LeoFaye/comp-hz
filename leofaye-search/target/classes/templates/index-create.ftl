<!DOCTYPE html>  
<html>
<head>
<link href="https://cdn.bootcss.com/bootstrap/3.3.7/css/bootstrap.min.css" rel="stylesheet"/>
<link href="https://cdn.bootcss.com/bootstrap/3.3.7/css/bootstrap-theme.min.css" rel="stylesheet" />
<script src="https://cdn.bootcss.com/jquery/1.11.3/jquery.min.js"></script>
<script src="https://cdn.bootcss.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
</head>  
<style>
	.index-rectangle{width:55px;margin:1px;padding:10px;border:1px solid #ddd;display:inline-block;}
</style>

<script>
$(function(){
	$(".btn").on("click",function() {
		if ($(this).hasClass("btn-success")) {
			$("form").attr("action", "${url}/data/index").submit();
		} else if ($(this).hasClass("btn-danger")) {
			$("form").attr("action", "${url}/index/delete").submit();
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
	
	
		
	
		<div class="col-xs-offset-2 col-xs-8 content" style="margin-top:15px;">
			<form action="" method="post">
			
			
			
			<#if result ??>
				<div class="alert alert-success alert-dismissible" role="alert">
				  <button type="button" class="close" data-dismiss="alert" aria-label="Close"><span aria-hidden="true">&times;</span></button>
				  <strong>${result.message}</strong>
				</div>
			</#if>
			
			<div class="row" style="margin-top:50px;">
				<div class="col-xs-8">
					<#if indices ??>
						<#list indices as index>
							   <label style="margin:5px;width:120px;">
							     <input name="indices" type="checkbox" value="${index}"> ${index}
							   </label>
						</#list>
					</#if>
				</div>
				<div class="col-xs-2">
					<button class="btn btn-danger" type="button">Delete Index</button>
				</div>
			</div>
			
			<div class="row" style="margin-top:50px;">
				<div class="col-xs-4">
					<input name="index" class="form-control" placeholder="index">
				</div>
				<div class="col-xs-4">
					<input name="type" class="form-control" placeholder="type">
				</div>
				<div class="col-xs-2">
					<button class="btn btn-success" type="button">Index</button>
				</div>
			</div>
			
			
			</form>
			<div style="margin-top:20px;">
			<#if batchNum ??>
				<#list 1..batchNum as n>
					<#if successNum ?? && n <= successNum>
						<div  class="index-rectangle text-center" style="background:#8bc34a;">
							<font style="font-color:#999999;">${n}</font>
						</div>
					<#else>
						<div class="index-rectangle text-center">
							<font style="font-color:#999999;">${n}</font>
						</div>
					</#if>
				</#list>
			</#if>
			</div>
		</div>
	
</div>
</body>  
</html> 