sin1 = [];
sin2 = [];
sin3 = [];
time = new Date().getTime();
gridster = null;
chart1 = nv.models
  .lineChart()
  .margin({ left: 90, right: 90, top: 90, bottom: 90 })
  .useInteractiveGuideline(true)
  .duration(0)
  .showLegend(true)
  .showYAxis(true)
  .showXAxis(true);
chart2 = nv.models
  .scatterChart()
  .showDistX(true) //showDist, when true, will display those little distribution lines on the axis.
  .showDistY(true)
  .color(d3.scale.category10().range());
chart2.tooltipContent(function(key) {
  return "<h3>" + key + "</h3>";
});
chart2.xAxis.tickFormat(d3.format(".02f"));
chart2.yAxis.tickFormat(d3.format(".02f"));

var myData1 = [
  {
    values: sin1,
    key: "Predicted price",
    color: "red",
    area: false
  },
  {
    values: sin2,
    key: "Wipro stock price",
    color: "‎#FF7F50",
    // fill:"‎#FF7F50",
    area: true
  }
];
var myData2 = [
  {
    values: sin3,
    key: "X-Y relation",
    color: "red",
    area: true
  }
];
j = 0;
windowSize = 100;

function stockPrice1() {
  $.ajax({
    url: "http://finance.google.com/finance/info?client=ig&q=BOM:WIPRO",
    dataType: "jsonp",
    jsonp: "callback",
    jsonpCallback: "quote"
  });
  quote = function(data) {
    $.each(data, function(key, val) {
      while (sin2.length > windowSize) {
        sin2.shift();
      }
      sin2.push({
        x: Math.round(((new Date().getTime() - time) / 1000 - 0) * 100 / 60) /
          100,
        y: data[0].l_fix
      });
      if (sin1.length == 0 || sin1[sin1.length - 1].y == 0) {
        sin1.push({ x: ((new Date().getTime() - time) / 1000 + 0) / 60, y: 0 });
      }
    });
  };
  d3.select("#chart svg").datum(myData1).call(chart1);
}
function stockPrice2() {
  d3.select("#chart2 svg")
  .datum(myData2)
  .call(chart2);
}
function haveGraph1() {
  nv.addGraph(function() {
    chart1.xAxis.axisLabel("Time (min)").tickFormat(d3.format(",r"));
    chart1.yAxis.axisLabel("Price (Rs.)").tickFormat(d3.format(".02f"));
    nv.utils.windowResize(function() {
      chart1.update();
    });
    return chart1;
  });
  var stockPriceTicker = setInterval(stockPrice1,5000);
}
function haveGraph2() {
  gridster.add_widget(
    '<li class="custom_grid" data-row="2" data-col="1" data-sizex="1" data-sizey="2">' +
      '<div id="chart2"><svg></svg></div></li>',
    1,
    1
  );
  nv.addGraph(function() {
    chart2.xAxis.axisLabel("X-Axis").tickFormat(d3.format(",r"));
    chart2.yAxis.axisLabel("Y-Axis").tickFormat(d3.format(".02f"));
    nv.utils.windowResize(function() {
      chart2.update();
    });
    return chart2;
  });
  var stockPriceTicker = setInterval(stockPrice2, 5000);
}
//-----------------------------------------
function wsConnect1(logicName) {
  if ("WebSocket" in window) {
    ws = new WebSocket("ws://" + document.domain + ":5005/websocket");
    ws.onopen = function(msg) {
      console.log("connection opend.");
      ws.send(JSON.stringify({ output: logicName }));
    };
    $("#nav-message").click(function() {
      console.log("stockPriceTicker interval cleared.");
      ws.onclose = function() {};
      ws.close();
      clearInterval(stockPriceTicker);
    });
    ws.onmessage = function(msg) {
      var message = JSON.parse(msg.data);
      console.log("Pushing ", message.output.split(":")[1]);
      sin1.push({
        x: Math.round(((new Date().getTime() - time) / 1000 + 0) * 100 / 60) /
          100,
        y: message.output.split(":")[1]
      });
      sin2.push({
        x: Math.round(((new Date().getTime() - time) / 1000 + 0) * 100 / 60) /
          100,
        y: message.output.split(":")[0].split(",").pop(-1)
      });
      while (sin1.length > windowSize) {
        sin1.shift();
      }
      while (sin2.length > windowSize) {
        sin2.shift();
      }
      d3.select("#chart svg").datum(myData1).attr("fill","steelblue").call(chart1);
    };
    window.onbeforeunload = function() {
      ws.onclose = function() {};
      ws.close();
    };
    ws.error = function(err) {
      console.log(err);
    };
  }
}
function wsConnect2() {
  if ("WebSocket" in window) {
    ws = new WebSocket("ws://" + document.domain + ":5006/websocket");
    ws.onopen = function(msg) {
      console.log("connection opend.");
      ws.send(JSON.stringify({ output: "ready" }));
    };
    $("#nav-message").click(function() {
      console.log("stockPriceTicker interval cleared.");
      ws.onclose = function() {};
      ws.close();
      clearInterval(stockPriceTicker);
    });
    ws.onmessage = function(msg) {
      var message = JSON.parse(msg.data);
      console.log("Pushing ", message);
      sin3.push({ x: Math.random(10) * 100, y: Math.random(10) * 100 });
    };
    window.onbeforeunload = function() {
      ws.onclose = function() {};
      ws.close();
    };
    ws.error = function(err) {
      console.log(err);
    };
  }
}

function init_grid() {
  gridster = $(".gridster ul")
    .gridster({
      widget_margins: [10, 10],
      widget_base_dimensions: [900, 200],
      resize: {
        enabled: true,
        max_size: [3, 3],
        min_size: [1, 1]
      }
    })
    .data("gridster");
}

$(document).ready(function() {
  $(".custom-logic-list button").click(function() {
    var logicName = $(this).html();
    $(".welcome-div").hide();
    $(".dashboard-graph-div").show("slide", function() {
      haveGraph1();
      $("#custom-add-graph").click(function() {
        haveGraph2();
        wsConnect2();
      });
      //wsConnect1(logicName);
    });
  });
  init_grid();
});
