messages = [];
$(function() {
  $.get("http://127.0.0.1:5000/retrieveMsgSchemas", function(data) {
    messages = data;
  });
  $("#message").change(function() {
    console.log("second");
    var xyz = messages;
    for (var i in xyz) {
      var mName = xyz[i][1];
      if (mName === $("#message").val()) {
        $("#id_continousVariable").empty();
        $("#id_categoryVariable").empty();
        $("#id_predictionVariable").empty();
        var jsone = JSON.parse(xyz[i][2]);
        for (var k in jsone) {
          $("#id_continousVariable").append("<option>" + k + "</option>");
          $("#id_categoryVariable").append("<option>" + k + "</option>");
          $("#id_predictionVariable").append("<option>" + k + "</option>");
        }
      }
    }
    $("#id_continousVariable").multiselect("rebuild");
    $("#id_categoryVariable").multiselect("rebuild");
    $("#id_predictionVariable").multiselect("rebuild");
  });
  $("#id_continousVariable").multiselect({
    includeSelectAllOption: true
  });
  $("#id_categoryVariable").multiselect({
    includeSelectAllOption: true
  });
  $("#id_predictionVariable").multiselect({
    includeSelectAllOption: true
  });
});
