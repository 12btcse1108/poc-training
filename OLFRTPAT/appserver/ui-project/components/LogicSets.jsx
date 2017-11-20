import React from "react";
import { Button } from "react-bootstrap";
import Request from "superagent";
import ViewLogicSets from "./ViewLogicSets.jsx";
let logicDetails = [];

class LogicSets extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      msgName: "",
      topicName: "",
      algorithmName: "",
      trainTopic: "",
      predictTopicInput: "",
      predictTopicOutput: "",
      regularisation: 0.0,
      learningRate: 0.0,
      modelSaveInterval: 0,
      algoName: "",
      continousVariable: "",
      categoryVariable: "",
      predictionVariable: "",
      logicDetails: [],
      modalStatus: false,
      deleteLogicName: "",
      msgDetails: [],
      logicIndex: 0,
      showLogicData: false,
      analyzeState: "false"
    };
  }

  componentDidMount() {
    let self = this;
    Request.get("http://127.0.0.1:5000/retrieveMsgSchemas").end(function(
      err,
      res
    ) {
      if (err) {
        console.error(err);
      } else {
        self.setState({ msgDetails: JSON.parse(res.text) });
      }
    });
  }

  componentWillMount() {
    this.retrieveLogic();
  }

  retrieveLogic() {
    let self = this;
    Request.get("http://127.0.0.1:5000/retrieveLogicSets").end(function(
      err,
      res
    ) {
      if (err) {
        console.log("error");
      } else {
        self.setState({ logicDetails: JSON.parse(res.text) });
      }
    });
  }
  logicSetDetails() {
    var indents = [];
    for (var i in this.state.logicDetails) {
      indents.push(
        <tr>
          <td>{parseInt(i) + 1}</td>

          <td ref="currentAlgoName">
            {this.state.logicDetails[i][__.logicSetName]}
          </td>

          <td ref="currentMsgName">{this.state.logicDetails[i][__.msgName]}</td>

          <td>
            <button
              type="button"
              className="btn btn-success"
              onClick={this.showLogic.bind(
                this,
                i,
                this.state.logicDetails[i][__.logicSetName]
              )}
            >
              View
            </button>
          </td>

          <td>
            <button
              type="button"
              id={parseInt(i)}
              className={
                this.state.logicDetails[i][__.analyseState] == "true"
                  ? "btn btn-danger"
                  : "btn btn-success"
              }
              onClick={this.analyseLogic.bind(
                this,
                i,
                this.state.logicDetails[i][__.logicSetName]
              )}
            >
              {this.state.logicDetails[i][__.analyseState] == "true"
                ? "Stop Analysis"
                : "Analyse"}
            </button>
          </td>

          <td>
            <button
              type="button"
              className="btn btn-success"
              onClick={this.trainLogic.bind(
                this,
                i,
                this.state.logicDetails[i][__.logicSetName],
                this.state.logicDetails[i][__.algorithmName]
              )}
            >
              Train
            </button>
          </td>

          <td>
            <button
              type="button"
              className="btn btn-success"
              onClick={this.processLogic.bind(
                this,
                i,
                this.state.logicDetails[i][__.logicSetName]
              )}
            >
              Predict
            </button>
          </td>

          <td>
            <button
              type="button"
              className="btn btn-warning"
              data-toggle="modal"
              data-target="#myDeleteModal"
              onClick={this.openDeleteModal.bind(
                this,
                this.state.logicDetails[i][__.logicSetName]
              )}
            >
              Delete
            </button>
          </td>
        </tr>
      );
    }
    return indents;
  }

  showLogic(index, logicName) {
    this.setState({
      showLogicData: true,
      logicIndex: index,
      algoName: logicName
    });
  }
  analyseLogic(index, logicName) {
    this.setState({ algoName: logicName });
    console.log("inside button click", this.state.logicDetails);
    console.log(
      "color",
      document.getElementById(index).childNodes[0].ownerDocument.activeElement
        .className
    );
    console.log(
      "analyse",
      document.getElementById(index).childNodes[0].nodeValue
    );
    if (this.state.analyzeState === "true") {
      document.getElementById(index).childNodes[0].nodeValue = "Stop Analysis";
      document.getElementById(
        index
      ).childNodes[0].ownerDocument.activeElement.className =
        "btn btn-danger";
      this.setState({ analyzeState: "false" });
      Request.post("http://127.0.0.1:5000/analyze")
        .send({ algoName: logicName, index: index })
        .end(function(err, res) {
          if (err) {
            console.log("error");
          } else {
            console.log(res.text);
          }
        });
      console.log("analyse is done");
      this.updateAnalyse(logicName);
    } else {
      document.getElementById(index).childNodes[0].nodeValue = "Analyse";
      document.getElementById(
        index
      ).childNodes[0].ownerDocument.activeElement.className =
        "btn btn-success";
      this.setState({ analyzeState: "true" });
      Request.post("http://127.0.0.1:5000/stopAnalyze")
        .send({ algoName: logicName, index: index })
        .end(function(err, res) {
          if (err) {
            console.log("error");
          } else {
            console.log(res.text);
          }
        });
      console.log("stop analyse is done");
      this.updateAnalyse(logicName);
    }
  }

  updateAnalyse(logicName) {
    var logic = logicName;
    let self = this;
    var status = self.state.analyzeState;
    console.log("inside method", status, logic);
    Request.post("http://127.0.0.1:5000/updateAnalyse")
      .send({ algoName: logic, analyzeState: status })
      .end(function(err, res) {
        if (err) {
          console.log("error");
        } else {
          console.log("update response", res);
        }
      });
  }

  trainLogic(index, logicName, algorithmNameLogic) {
    this.setState({ algoName: logicName });
    Request.post("http://127.0.0.1:5000/train")
      .send({
        algoName: logicName,
        index: index,
        nameOfAlgorithm: algorithmNameLogic
      })
      .end(function(err, res) {
        if (err) {
          console.log("error");
        } else {
          console.log(res.text);
          window.location.reload();
        }
      });
  }
  processLogic(index, logicName) {
    this.setState({ algoName: logicName });
    Request.post("http://127.0.0.1:5000/process")
      .send({ algoName: logicName, index: index })
      .end(function(err, res) {
        if (err) {
          console.log("error");
        } else {
          console.log(res.text);
          window.location.reload();
        }
      });
  }
  openDeleteModal(logicName) {
    this.setState({ modalStatus: true, deleteLogicName: logicName });
  }

  deleteLogic() {
    console.log(this.state.modalStatus);
    Request.post("http://127.0.0.1:5000/deleteLogicSet")
      .send({ algoName: this.state.deleteLogicName })
      .end(function(err, res) {
        if (err) {
          console.log("error");
        } else {
          console.log(res.text);
          window.location.reload();
        }
      });
  }

  logicMessageDetails() {
    var optionValue = [];
    for (var i in this.state.msgDetails) {
      optionValue.push(<option>{this.state.msgDetails[i][1]}</option>);
    }
    return optionValue;
  }

  createLogicSetTable() {
    var aName = this.refs.lname.value;
    var mName = document.getElementById("logic").value;
    this.setState({ algoName: aName, msgName: mName });
    Request.post("http://127.0.0.1:5000/saveLogicSet")
      .send({
        msgName: mName,
        topicName: this.state.topicName,
        algorithmName: this.state.algorithmName,
        trainTopic: this.state.trainTopic,
        predictTopicInput: this.state.predictTopicInput,
        predictTopicOutput: this.state.predictTopicOutput,
        regularisation: this.state.regularisation,
        learningRate: this.state.learningRate,
        modelSaveInterval: this.state.modelSaveInterval,
        algoName: aName,
        continousVariable: this.state.continousVariable,
        categoryVariable: this.state.categoryVariable,
        predictionVariable: this.state.predictionVariable,
        analyzeState: this.state.analyzeState
      })
      .end(function(err, res) {
        if (err) {
          console.log("error");
        } else {
          console.log("LogicSet Details uploaded Succesfully");
          window.location.reload();
        }
      });
  }

  render() {
    return (
      <div id="custom-ls">
        {this.state.showLogicData
          ? <ViewLogicSets
              logicIndex={this.state.logicIndex}
              logicName={this.state.algoName}
            />
          : <div>
              <span>
                <button
                  type="button"
                  className="btn btn-info btn-md custom-right-align"
                  data-toggle="modal"
                  data-target="#myModal"
                >
                  Create LogicSet
                </button>
              </span>
              <div id="myModal" className="modal fade" role="dialog">
                <div className="modal-dialog">
                  <div className="modal-content">
                    <div className="modal-header">
                      <button
                        type="button"
                        className="close"
                        data-dismiss="modal"
                      >
                        ×
                      </button>
                      <h4 className="modal-title">Create Logicset</h4>
                    </div>
                    <div className="modal-body">
                      <form>
                        <div className="form-group">
                          <label>Logic Name</label>
                          <input
                            type="text"
                            className="form-control"
                            id="lname"
                            ref="lname"
                          />
                        </div>
                        <div className="form-group">
                          <label>Message Name</label>
                          <select className="form-control" id="logic">
                            {this.logicMessageDetails()}
                          </select>
                        </div>
                        <div className="form-group">
                          <label>Algorithm</label>
                          <select className="form-control" id="algorithm">
                            <option>Linear Regression</option>
                            <option>Logistic Regression</option>
                          </select>
                        </div>
                        <button
                          type="submit"
                          className="btn btn-success"
                          onClick={this.createLogicSetTable.bind(this)}
                          data-dismiss="modal"
                        >
                          Create
                        </button>
                        <button
                          type="button"
                          className="btn btn-warning custom-right-align"
                          data-dismiss="modal"
                        >
                          Close
                        </button>

                      </form>
                    </div>
                  </div>
                </div>
              </div>
              <table className="table table-responsive">
                <thead>
                  <tr>
                    <th>S.No</th>
                    <th>Logic Set Name</th>
                    <th>Message</th>
                    <th>View</th>
                    <th>Analyse</th>
                    <th>Train</th>
                    <th>Predict</th>
                    <th>Delete</th>
                  </tr>
                </thead>
                <tbody>
                  {this.logicSetDetails()}
                </tbody>
              </table>
              {this.state.modalStatus
                ? <div className="modal fade" id="myDeleteModal" role="dialog">
                    <div className="modal-dialog">
                      <div className="modal-content">
                        <div className="modal-body">
                          <h5>Are you sure you want to DELETE?</h5>
                        </div>
                        <div className="modal-footer">
                          <button
                            type="button"
                            className="btn btn-success"
                            id="btnDelteYes"
                            onClick={this.deleteLogic.bind(this)}
                          >
                            Yes
                          </button>
                          <button
                            type="button"
                            className="btn btn-danger"
                            data-dismiss="modal"
                          >
                            No
                          </button>
                        </div>
                      </div>
                    </div>
                  </div>
                : null}
            </div>}
      </div>
    );
  }
}

const __ = {
  msgName: 1,
  algorithmName: 13,
  analyseState: 15,
  logicSetName: 10
};

export default LogicSets;
