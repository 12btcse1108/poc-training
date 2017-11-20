import React from "react";
import ReactDOM from "react-dom";
import { Route, IndexRoute, Router, Link, hashHistory } from "react-router";
import Home from "../components/Home.jsx";
import Dashboard from "../components/Dashboard.jsx";
import Message from "../components/Message.jsx";
import LogicSets from "../components/LogicSets.jsx";
import CustomLogicSets from "../components/CustomLogicSets.jsx";
import Help from "../components/Help.jsx";
import Login from "../components/Login.jsx";

ReactDOM.render(
	<Router history={hashHistory}>
		<Route path="/" component={Login} />
		<Route path="/home" component={Home}>
			<Route path="/dashboard" component={Dashboard} />
			<Route path="/message" component={Message} />
			<Route path="/logicsets" component={LogicSets} />
			<Route path="/customlogicsets" component={CustomLogicSets} />
			<Route path="/help" component={Help} />
		</Route>
	</Router>,
	document.getElementById("app")
);
