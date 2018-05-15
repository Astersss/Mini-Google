<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" import = "java.util.List" %>
<%@ page import="org.cis455.project.*" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Query Page cis455 project</title>
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
<link rel="stylesheet" href="style.css">
<link rel="stylesheet" href="//code.jquery.com/ui/1.12.1/themes/base/jquery-ui.css">
<link rel="stylesheet" href="/resources/demos/style.css">
<script src="https://code.jquery.com/jquery-1.12.4.js"></script>
<script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js"></script>
<script>
  $( function() {
    var availableTags = [
         "computer science",
         "donald trump",
		"time","year","people","way","day","man","thing","woman","life","child","world","school",
		"state","family","student","group","country","problem","hand","part","place","case","week",
		"company","system","program","question","work","government","number","night","point","home",
		"water","room","mother","area","money","story","fact","month","lot","right","study","book",
		"eye","job","word","business","issue","side","kind","head","house","service","friend",
		"father","power","hour","game","line","end","member","law","car","city","community","name",
		"president","team","minute","idea","kid","body","information","back","parent","face","others",
		"level","office","door","health","person","art","war","history","party","result","change","morning",
		"reason","research","girl","guy","moment","air","teacher","force","education",
    ];
    $( "#tags" ).autocomplete({
      source: availableTags
    });
  } );
</script>

</head>
<style>
#pages_container { 
	  	float: none;
    	margin: 0 auto;
    	width = 60%;
	}
.title{
font-size:20px;
font-weight: 700;

}
</style>
<body>
	
	<div id = "pages_container" class="container col-md-9">
			<br><br>
			<form method = "POST" action = "query">
				<div class="input-group" style="width:70%">
	                  <input type="text" id="tags" class="form-control ui-widget" placeholder="Input query here" id="query" name="query" value="">
	                   <div class="input-group-btn">
	                   <button type="submit" class="btn btn-primary">Search</button>
	                  </div>
	              </div>
			</form>
		<br>
		<div id = "weatherblock">
		    <button onclick="getWeatherfunction()" class="btn btn-info">
				<span class="glyphicon glyphicon-cloud"></span> Get current weather</button> 
			<div id = "weather"></div>
			<p><span id="temp"></span></p>
			<p><span id="wind"></span></p>
		</div>	
	
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
	<script>
	   function getWeatherfunction()
	    {
		   var weather = new XMLHttpRequest();
		   weather.open("GET","http://api.wunderground.com/api/4212fdedc86ec0b5/conditions/q/PA/philadelphia.json",false);
		   weather.send(null);
		   
		   var r = JSON.parse(weather.response);
		   var dis = "Current location: " + r.current_observation.display_location.full + "<br />";
		   var temp = r.current_observation.temperature_string + "<br />";
		   var wind = "Wind:"+r.current_observation.wind_string + "<br />";
		   document.getElementById("weather").innerHTML = dis;
		   document.getElementById("temp").innerHTML = temp;
		   document.getElementById("wind").innerHTML = wind;
	    }   
	</script>
<%
	/* int pageNum = 0;
	String timediff = "0.0";
	List<WebContent> pages = (List<WebContent>) session.getAttribute("pages");
	String timediffStr = (String) session.getAttribute("timediff");
	if(pages != null) pageNum = pages.size();
	if(timediffStr != null) timediff = timediffStr; */
	String query = request.getParameter("query");
	QueryService queryService = new QueryService();
	
	long timeStart = System.currentTimeMillis();
	List<WebContent> pages = queryService.getMatchedWeb(query);
	long timeStop = System.currentTimeMillis();
	String timediff = String.valueOf((timeStop - timeStart) / 1000.0);
	int pageNum = pages.size();
	
%>
	
	
	<div style="color :#808080">Total <%=pageNum%> results in <%=timediff%> seconds </div><br>
<% 	if(pages != null){
	   for(int i = 0; i < pages.size();i++){ 

		WebContent webpage = pages.get(i);
		String url = webpage.getUrl();
		String title = webpage.getTitle();
		String summary = webpage.getSummary();
%>
	<div>
		<div class="title"><a href="<%=url%>"><%=title%></a></h4></div>
		<div style="color :#808080" ><%=url%></div>
		<%=summary%>
		<br><br>
	</div>
	
<% 		}
   }else{//pages == null
		 out.print("No matched web pages");
   }%>
</div>
</body>
</html>