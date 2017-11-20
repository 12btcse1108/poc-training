import React from "react";
import { Button } from "react-bootstrap";
import Request from "superagent";

class Dashboard extends React.Component {
	constructor(props) {
		super(props);
		this.state = {
			msgDetails: [],
			logicSets: [],
			addGraph: false,
			gridsterList: [],
			algoName: "",
			currentLogicDetails: []
		};
	}
	componentWillMount() {
		let self = this;
		const script = document.createElement("script");
		script.src = "../include/js/script.js";
		script.async = true;
		document.body.appendChild(script);
		Request.get("http://127.0.0.1:5000/retrieveMsgSchemas").end(function(
			err,
			res
		) {
			if (err) {
				console.log("error");
			} else {
				self.setState({ msgDetails: JSON.parse(res.text) });
			}
		});
		Request.get("http://127.0.0.1:5000/retrieveLogicSets").end(function(
			err,
			res
		) {
			if (err) {
				console.log("error", err);
			} else {
				console.log(res.text);
				self.setState({ logicSets: JSON.parse(res.text) });
			}
		});
	}
	populateLogicSets() {
		var indents = [];
		var xyz = this.state.logicSets;
		for (var i in xyz) {
			indents.push(
				<li>
					<div>
						<button
							type="button"
							ref="currentLogic"
							className="btn btn-success custom-width"
							onClick={this.handleAddButton.bind(
								this,
								xyz[i][10]
							)}
						>
							{xyz[i][10]}
						</button>
					</div>
				</li>
			);
		}
		return indents;
	}
	handleAddButton(logic) {
		this.setState({ addGraph: true });
		console.log(logic);
		this.setState({ algoName: logic });
		this.populateLogic(logic);
	}
	populateLogic(logicName) {
		let self = this;
		Request.post("http://127.0.0.1:5000/retrieveLogic")
			.send({ algoName: logicName })
			.end(function(err, res) {
				if (err) {
					console.log("error", err);
				} else {
					self.setState({
						currentLogicDetails: JSON.parse(res.text)
					});
				}
			});
		console.log(self.state.currentLogicDetails);
	}
	populateField() {
		var mName = this.state.currentLogicDetails;
		console.log();
	}
	render() {
		return (
			<div id="custom-dashboard">
				<div className="col-sm-2">
					<h2> Logic Sets </h2>
					<ul className="list-group custom-logic-list">
						{this.populateLogicSets()}
					</ul>
				</div>
				<div className="col-sm-10">
					{this.state.addGraph == true
						? <div>
								<button
									className="btn btn-success custom-right-align"
									data-toggle="modal"
									data-target="#myModal"
								>
									Add Graph
								</button>
								<div
									id="myModal"
									className="modal fade"
									role="dialog"
								>
									<div className="modal-dialog">
										<div className="modal-content">
											<div className="modal-header">
												<button
													type="button"
													className="close"
													data-dismiss="modal"
												>
													x
												</button>
												<h4 className="modal-title">
													Modal Header
												</h4>
											</div>
											<div className="modal-body">
												<form>
													<div className="form-group">
														<label>
															Message Name
														</label>
														<input
															type="text"
															value="asd"
															className="form-control"
															id="messageName"
															ref="val"
														/>
													</div>
													<div className="form-group">
														<label>X-Axis</label>
														<input
															type="text"
															className="form-control"
															id="xName"
															ref="val"
														/>
													</div>
													<div className="form-group">
														<label>Y-Axis</label>
														<input
															type="text"
															className="form-control"
															id="yName"
															ref="val"
														/>
													</div>
													<button
														type="button"
														id="custom-add-graph"
														className="btn btn-success custom-add-graph"
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
							</div>
						: null}
					<div className="welcome-div">
						<span>
							Click on your logic set to see Real-time prediction graph.
						</span>
						<hr className="hr-primary" />
					</div>
					<div className="dashboard-graph-div">
						<h4>Real-time prediction graph</h4>
						<hr className="hr-primary" />
						<div className="gridster">
							<ul>
								<li
									className="custom_grid"
									data-row="1"
									data-col="1"
									data-sizex="1"
									data-sizey="1"
								>
									<div id="chart"><svg /></div>
								</li>
							</ul>
						</div>
					</div>
				</div>
			</div>
		);
	}
}
export default Dashboard;
