<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>homePage</title>
<%-- <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/bootstrap.min.css"> --%>
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
<link rel="stylesheet" href="style.css">
</head>
<style>
	html {
		/* background-image:c:url("img/background.jpg");  */
	   /*  background-image:url("https://www.planwallpaper.com/static/images/518169-backgrounds.jpg"); */ 
		background-color : #ffffff;
		background-attachment: fixed;
		background-repeat: no-repeat;
   		background-position: right bottom;
		font-family:'Questrial', sans-serif;
		font-size:20px;
	}
	#searchBlock { 
	  	 position: absolute;
		 width: 600px;
		 height: 200px;
		 top: 40%;
		 left: 50%;
		 margin: -50px 0 0 -300px;	
	}
	.center-block {
		position: relative;
	    margin-left:0%;
	    margin-bottom:5%;
	    text-align:center; 
	}
	#EngineName {
		color :#0066cc;
		font-family: 'Open Sans', sans-serif;
	}
	
</style>
<body>
<div id = 'searchBlock'>
		<div class="center-block">
		   <%-- <img src="<c:url value='img/cute-whale.png'/>" /> --%>
		   <!-- <img src="img/cute-whale.png"> -->
	      <h1 id="EngineName" >Smart Search</h1><i>make your life easier</i>
    	</div>
		<form method = "POST" action = "query">
			<div class="input-group">
                  <input type="text" class="form-control" placeholder="Input query here" id="query" name="query" value="">
                   <div class="input-group-btn">
                   <button type="submit" class="btn btn-primary">Search</button>
                  </div>
              </div>			
		</form>		

<div style = " margin: 0 auto; padding : 15px; position:absolute; margin-left : 20%; margin-right:20%;text-align:center ">
	
		<button class="btn btn-info" style = "width:150px"><a href = "yelp">Yelp Service</a></button>
	    <button onclick="getWeatherfunction()" style = "width:150px" class="btn btn-info">
			<span class="glyphicon glyphicon-cloud"></span> Weather Service</button> 
		<div id = "weather"></div>
		<p><span id="temp"></span></p>
		<p><span id="wind"></span></p>
	
	<script>
	   function getWeatherfunction()
	    {
		   var weather = new XMLHttpRequest();
		   weather.open("GET","http://api.wunderground.com/api/4212fdedc86ec0b5/conditions/q/PA/philadelphia.json",false);
		   weather.send(null);
		   
		   var r = JSON.parse(weather.response);
		   var dis = "Current location: " + r.current_observation.display_location.full + "<br />";
		   var temp = r.current_observation.temperature_string + "<br>";
		   var wind = "Wind:"+r.current_observation.wind_string + "<br>";
		   document.getElementById("weather").innerHTML = dis;
		   document.getElementById("temp").innerHTML = temp;
		   document.getElementById("wind").innerHTML = wind;
	    }   
	</script>
</div>
</div>
</body>
</html>
