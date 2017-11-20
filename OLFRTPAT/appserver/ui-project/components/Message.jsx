import React from 'react';
import { Button } from 'react-bootstrap';
import Request from "superagent";

let msgDetails=[];
let rows=[];

class Message extends React.Component {
	constructor(props){
		super(props);
    	this.state={msgName:'',msgStructure:'{}',msgOrField:'msg',msgDetails:[],fieldDescription:''};
	}
	componentDidMount() {
      let self = this;
      Request
      .get('http://127.0.0.1:5000/retrieveMsgSchemas')
      .end(function(err, res){
        if(err){
          console.log('error');
        }
        else
        {
          self.setState({msgDetails:JSON.parse(res.text)});
          console.log(self.state.msgDetails);
        }})
    }
	createMsgTable() {
		var input = this.refs.val.value;
		this.setState({msgName:input});
	    Request
		  .post('http://127.0.0.1:5000/saveMsgSchema')
		  .send({msgName:input,msgStructure:this.state.msgStructure})
		  .end(function(err, res){
		    if(err){
	      		console.log('error');
		    }
		    else
		    {
		      	console.log('Message Details uploaded Succesfully');
		    }})
	}
	updateMsgTable(){
		var name = this.refs.fieldName.value;
		var description = this.refs.description.value;
		var type = document.getElementById("fieldType").value;
		var msgJSON = JSON.parse(this.state.msgStructure);
		msgJSON[name] = new Array();
		msgJSON[name].push(type);
		msgJSON[name].push(description);
		var abc = JSON.stringify(msgJSON);
		console.log(abc);
		this.setState({msgStructure:abc});
	    Request
		  .post('http://127.0.0.1:5000/updateMsgSchema')
		  .send({msgName:this.state.msgName,msgStructure:abc,fieldDescription:description})
		  .end(function(err, res){
		    if(err){
		      console.log('error');
		    }
		    else
		    {
		      console.log('Message Details updated Succesfully');
		    }});
	}
	deleteMsgTable(message){
		Request
		.post('http://127.0.0.1:5000/deleteMsgSchema')
		.send({msgName:message})
		.end(function(err, res){
		if(err){
		  console.log('error');
		}
		else
		{
		  console.log(res.text);
		  window.location.reload();
		}})
 	}
 	showMessages() {
 		this.setState({msgOrField:'msg'});
 	}
 	messageDetails() {
      var indents = [];
            for(var i in this.state.msgDetails){
                indents.push(<tr>
                  <td className="hidden" ref="currentMsgStruct">{this.state.msgDetails[i][2]}</td>
                  <td>{parseInt(i)+1}</td>
                  <td ref="currentMsgName">{this.state.msgDetails[i][1]}</td>
                  <td><button type="button" className="btn btn-success" onClick={this.fieldDetails.bind(this,this.state.msgDetails[i][1],this.state.msgDetails[i][2])}>View</button></td>
                  <td><button type="button" className="btn btn-warning" onClick={this.deleteMsgTable.bind(this,this.state.msgDetails[i][1])}>Delete</button></td>
                  </tr>
                );
              }
      return indents;
    }
	viewFieldDetails() {
	var indents = [];
	var i=0;
	var structure=JSON.parse(this.state.msgStructure);
	console.log(structure);
				for(var key in structure){
				console.log(key);
						indents.push(<tr>
							<td>{++i}</td>
							<td ref="currentfieldName">{key}</td>
							<td ref="currentfieldType">{structure[key][0]}</td>
							<td ref="currentfieldType">{structure[key][1]}</td>
							<td><button type="button" className="btn btn-success">View</button></td>
							<td><button type="button" className="btn btn-warning" onClick={this.deleteField.bind(this,key)}>Delete</button></td>
							</tr>
						);
					}
	return indents;
	}
    deleteField(key) {
	var structure=JSON.parse(this.state.msgStructure);
	delete structure[key];
	var abc = JSON.stringify(structure);
	this.setState({msgStructure:abc});
	console.log(structure);
	this.retrieveFields(abc);
	}
    retrieveFields(abc) {
	Request
	.post('http://127.0.0.1:5000/updateMsgSchema')
	.send({msgName:this.state.msgName,msgStructure:abc})
	.end(function(err, res){
		if(err){
			console.log('error')
		}
		else
		{
			console.log(res);
		}})
	}
    fieldDetails(_1,_2) {
      this.setState({msgOrField:'field',
      	msgName:_1,
      	msgStructure:_2});
      console.log("inside fieldDetails", this.state.msgStructure);
    }
    render() {
    	return (
        <div id="custom-message">
			{this.state.msgOrField=='msg'?
			<div>
			<span><button type="button" className="btn btn-info btn-md custom-right-align" data-toggle="modal" data-target="#myModal">Create Message</button></span>
				<div id="myModal" className="modal fade" role="dialog">
				  <div className="modal-dialog">
				    <div className="modal-content">
				      <div className="modal-header">
				        <button type="button" className="close" data-dismiss="modal">&times;</button>
				        <h4 className="modal-title">Create Message</h4>
				      </div>
				      <div className="modal-body">
				        	<form>
							  <div className="form-group">
							    <label>Message Name</label>
							    <input type="text" className="form-control" id="name" ref="val"/>
							  </div>
							  <button type="submit" className="btn btn-success" onClick={this.createMsgTable.bind(this)}>Create</button>
								<button type="button" className="btn btn-warning custom-right-align" data-dismiss="modal">Close</button>

							</form>
				      </div>
				    </div>
				  </div>
				</div>
	        	<table className="table table-responsive">
	              <thead>
	                <tr>
	                  <th>S.No</th>
	                  <th>Name</th>
	                  <th>View</th>
	                  <th>Delete</th>
	                </tr>
	              </thead>
	              <tbody>
	          		{this.messageDetails()}
	          	</tbody>
	          </table>
	          </div>:null}
	          {this.state.msgOrField=='field'?<div>
				<span><button type="button" className="btn btn-info btn-md custom-left-align" onClick={this.showMessages.bind(this)}>Back</button></span>
				<span><button type="button" className="btn btn-info btn-md custom-right-align" data-toggle="modal" data-target="#myModal">Add Field</button></span>
		        <div id="myModal" className="modal fade" role="dialog">
				  <div className="modal-dialog">
				    <div className="modal-content">
				      <div className="modal-header">
				        <button type="button" className="close" data-dismiss="modal">&times;</button>
				        <h4 className="modal-title">Add Field</h4>
				      </div>
				      <div className="modal-body">
				        	<form>
							  <div className="form-group">
							    <label>Field Name</label>
							    <input type="text" className="form-control" id="name" ref="fieldName"/>
							  </div>
								<div className="form-group">
							    <label>Description</label>
							    <input type="text" className="form-control" id="name" ref="description"/>
							  </div>
							  <div className="form-group">
							    <label>Field Type</label>
                        <select className="form-control" id="fieldType">
                        <option>int</option>
                        <option>string</option>
												<option>double</option>
                        </select>
							  </div>
							  <button type="submit" className="btn btn-success" data-dismiss="modal" onClick={this.updateMsgTable.bind(this)}>Create</button>
								<button type="button" className="btn btn-warning custom-right-align" data-dismiss="modal" >Close</button>
							</form>
						</div>
				    </div>
				  </div>
				</div>
		        <table className="table table-responsive">
	            <thead>
	              <tr>
	                <th>S.No</th>
	                <th>Field Name</th>
	                <th>Type</th>
									<th>Description</th>									
	                <th>View</th>
	                <th>Delete</th>
	              </tr>
	            </thead>
	            <tbody>
	              {this.viewFieldDetails()}
	            </tbody>
	          </table></div>:null}
    	</div>);
    }
}
export default Message;
