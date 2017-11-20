import React from 'react';
import { Button } from 'react-bootstrap';
import Request from "superagent";

let av_fields = [];

export default class ViewLogicSets extends React.Component {
constructor(props)
{
super(props);
this.state={msgDetails:[],logicDetails:[],topicName:'',algorithmName:'',msgName:'',trainTopic:'',predictTopicInput:'',predictTopicOutput:'',regularisation:0.0,learningRate:0.0,
            modelSaveInterval:0,algoName:'',msgStructure:'{}',continousVariable:'',messageName:'',categoryVariable:'',predictionVariable:''};
}
coponentDidMount() {

}
componentWillMount() {
    const script = document.createElement("script");
    script.src = "../include/js/js_logicset.js";
    script.async =true;
    document.body.appendChild(script);
    let self = this;
      Request
      .get('http://127.0.0.1:5000/retrieveMsgSchemas')
      .end(function(err, res) {
      if(err) {
        console.log('error');
      }
      else {
        self.setState({msgDetails:JSON.parse(res.text)});
      }});
      Request
      .get('http://127.0.0.1:5000/retrieveLogicSets')
      .end(function(err, res) {
        if(err) {
          console.log('error');
        }
        else {
          self.setState({logicDetails:JSON.parse(res.text)});
          self.setState({topicName:self.state.logicDetails[self.props.logicIndex][2],algorithmName:self.state.logicDetails[self.props.logicIndex][3]})
          self.setState({trainTopic:self.state.logicDetails[self.props.logicIndex][4]})
          self.setState({predictTopicInput:self.state.logicDetails[self.props.logicIndex][5],predictTopicOutput:self.state.logicDetails[self.props.logicIndex][6],regularisation:self.state.logicDetails[self.props.logicIndex][7],learningRate:self.state.logicDetails[self.props.logicIndex][8],modelSaveInterval:self.state.logicDetails[self.props.logicIndex][9]})
          document.getElementById("message").value = self.state.logicDetails[self.props.logicIndex][1];
          document.getElementById("topicName").value=self.state.topicName;
          document.getElementById("algorithm").value=self.state.algorithmName;
          document.getElementById("trainTopic").value = self.state.trainTopic;
          document.getElementById("predictTopicInput").value = self.state.predictTopicInput;
          document.getElementById("predictTopicOutput").value = self.state.predictTopicOutput;
          document.getElementById("regularisation").value = self.state.regularisation;
          document.getElementById("learningRate").value = self.state.learningRate;
          document.getElementById("modelSaveInterval").value = self.state.modelSaveInterval;

          self.someOtherFunc();
          var values1=self.state.logicDetails[self.props.logicIndex][11];
          $.each(values1.split(","), function(i,e){
            $('#id_continousVariable option[value="' + e + '"]').attr("selected","selected");
           });
           $('#id_continousVariable').multiselect('rebuild');

           var values2=self.state.logicDetails[self.props.logicIndex][12];
           $.each(values2.split(","), function(i,e){
            $('#id_categoryVariable option[value="' + e + '"]').attr("selected","selected");
           });
           $('#id_categoryVariable').multiselect('rebuild');

           var values3=self.state.logicDetails[self.props.logicIndex][13];
           $.each(values3.split(","), function(i,e){
            $('#id_predictionVariable option[value="' + e + '"]').attr("selected","selected");
           });
           $('#id_predictionVariable').multiselect('rebuild');
        }});
  }
  someOtherFunc() {
      var xyz = this.state.msgDetails;
      for(var i in xyz) {
        var mName = xyz[i][1];
        if(mName === $('#message').val()) {
            $('#id_continousVariable').empty();
            $('#id_categoryVariable').empty();
            $('#id_predictionVariable').empty();
            var jsone = JSON.parse(xyz[i][2]);
            for(var k in jsone) {
                $('#id_continousVariable').append('<option value="'+k+'">' + jsone[k][1] + '</option>');
                $('#id_categoryVariable').append('<option value="'+k+'">' + jsone[k][1] + '</option>');
                $('#id_predictionVariable').append('<option value="'+k+'">' + jsone[k][1] + '</option>');
            }
        }
      }
      $('#id_continousVariable').multiselect('rebuild');
      $('#id_categoryVariable').multiselect('rebuild');
      $('#id_predictionVariable').multiselect('rebuild');
  }
  logicMessageDetails() {
    var indents = [];
    for(var i in this.state.msgDetails){
      indents.push(<option>{this.state.msgDetails[i][1]}</option>);
    }
    return indents;
  }
  saveLogicSet() {
    var mName=document.getElementById("message").value;
    var topicName=document.getElementById("topicName").value;
    var algorithmName=document.getElementById("algorithm").value;
    var trainTopic=document.getElementById("trainTopic").value;
    var topicInput=document.getElementById("predictTopicInput").value;
    var topicOutput=document.getElementById("predictTopicOutput").value;
    var regularisation=document.getElementById("regularisation").value;
    var learningRate=document.getElementById("learningRate").value;
    var modelSaveInterval=document.getElementById("modelSaveInterval").value;
    var continousVariable,categoryVariable,predictionVariable;
    [continousVariable,categoryVariable,predictionVariable] = this.fetchVariables();
    var logicSetName = this.props.logicName;
    Request
    .post('http://127.0.0.1:5000/updateLogicSet')
    .send({msgName:mName,topicName:topicName,algorithmName:algorithmName,trainTopic:trainTopic,
      predictTopicInput:topicInput,predictTopicOutput:topicOutput,regularisation:regularisation,
      learningRate:learningRate,modelSaveInterval:modelSaveInterval,algoName:logicSetName,
      continousVariable:continousVariable,categoryVariable:categoryVariable,predictionVariable:predictionVariable})
    .end(function(err, res) {
    if(err) {
      console.log('error while saveLogicSet..');
    }
    else {
      console.log(res.text);
    }});
  }
  fetchVariables() {
    var selected_option = $('#id_continousVariable option:selected');
    var continousVariable = '',categoryVariable = '',predictionVariable = '';
    for(var i=0;i<selected_option.length;i++) {
      if(selected_option[i].value.length > 0) {
        continousVariable = continousVariable.concat(selected_option[i].value+",");
      }
    }
    selected_option = $('#id_categoryVariable option:selected');
    for(var i=0;i<selected_option.length;i++) {
      if(selected_option[i].value.length > 0) {
        categoryVariable = categoryVariable.concat(selected_option[i].value+",");
      }
    }
    selected_option = $('#id_predictionVariable option:selected');
    for(var i=0;i<selected_option.length;i++) {
      if(selected_option[i].value.length > 0) {
        predictionVariable = predictionVariable.concat(selected_option[i].value+",");
      }
    }
    return [continousVariable,categoryVariable,predictionVariable];
  }
  value() {
    var mName = document.getElementById("message");
    if(mName != null) {
      var mName = mName.value;
      this.setState({messageName:mName});
      var xyz = this.state.msgDetails;
      for(var i in xyz) {
        if(xyz[i][1] === mName) {
          var jsone = JSON.parse(xyz[i][2]);
          for(var k in jsone) {
            av_fields.push(k);
          }
          this.setState({msgStructure:JSON.stringify(jsone)});
        }
      }
    }
  }
  continousVariable() {
   this.setState({continousVariable:document.getElementById("id_continousVariable").value});
   alert(document.getElementById("id_continousVariable").value);
  }
    render() {
    	return (
        <div id="custom-cls">
          <nav className="breadcrumb">
              <span className="breadcrumb-item">Logic Set----</span>
              <span className="breadcrumb-item active">{this.props.logicName}</span>
            </nav>
            <div className="row">
            <div className="col-sm-6 leftColumn">
              <form>
                  <div className="form-group">
                    <label>Message Name</label>
                    <select className="form-control" id="message" onChange={this.value.bind(this)}>
                      {this.logicMessageDetails()}
                    </select>
                   </div>
                   <div className="form-group">
                     <label >Topic Name</label>
                     <input type="text" className="form-control" placeholder="Topic Name" id="topicName"/>
                   </div>
                   <div className="form-group">
                     <label >Algorithm</label>
                     <select className="form-control" id="algorithm">
                        <option>Linear Regression</option>
                        <option>Logistic Regression</option>
                     </select>
                   </div>
                  <div className="form-group">
                    <label >Train Topic</label>
                    <input type="text" className="form-control" placeholder="Train Topic" id="trainTopic"/>
                  </div>
                  <div className="form-group">
                    <label>Predict Input Topic</label>
                    <input type="text" className="form-control" id="predictTopicInput" placeholder="Predict Topic Input"/>
                  </div>
                  <div className="form-group">
                    <label>Predict Topic Output</label>
                    <input type="text" className="form-control" placeholder="Predict Topic Output"
                    id="predictTopicOutput"/>
                  </div>
                  <div className="form-group">
                    <label>Num Iterations</label>
                    <input type="text" className="form-control" placeholder="Regularisation"
                    id="regularisation"/>
                  </div>
                  <div className="form-group">
                    <label>Learning Rate</label>
                    <input type="text" className="form-control" placeholder="Learning Rate"
                    id="learningRate"/>
                  </div>
                  <div className="form-group">
                    <label>Model Save Interval</label>
                    <input type="text" className="form-control" placeholder="Model Save Interval"
                    id="modelSaveInterval"/>
                  </div>
                   <button type="submit" className="btn btn-success buttonMargin" onClick={this.saveLogicSet.bind(this)}>Submit</button>
                </form>
            </div>
            <div className="col-sm-5 rightColumn">
              <form>
                <h3>Train On</h3>
                  <div className="form-group">
                    <label>Continous Variable</label>
                        <select id ="id_continousVariable" className="multiselect-ui form-control" multiple="multiple">
                        </select>
                  </div>
                  <div className="form-group">
                    <label>Category Variable</label>
                        <select id ="id_categoryVariable" className="multiselect-ui form-control" multiple="multiple">
                        </select>
                  </div>
                  <div className="form-group">
                    <label>Prediction Variable</label>
                        <select id ="id_predictionVariable" className="multiselect-ui form-control" multiple="multiple">
                        </select>
                  </div>
              </form>
            </div>
          </div>
    	</div>);
    }
}
