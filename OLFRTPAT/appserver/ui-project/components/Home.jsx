import React from "react";
import { Link } from "react-router";
import { Button } from "react-bootstrap";

class Home extends React.Component {
  constructor(props) {
    super(props);
  }

  logout() {
    console.log("logout done");
    this.props.router.push("/");
  }

  render() {
    return (
      <div>
        <nav className="navbar navbar-fixed-top" id="myNavbar">
          <div className="container-fluid">
            <div className="navbar-header left-header">
              <img src="../resources/images/wdlogo.png" />
            </div>
            <div className="navbar-header right-header">
              Welcome Admin!
              <button
                type="button"
                className="fa fa-sign-out custom-button"
                onClick={this.logout.bind(this)}
              />
            </div>
          </div>
        </nav>
        <div className="container-fluid custom-main-container">
          <div className="custom-side-nav">
            <div>
              <Link
                to="/dashboard"
                style={{ textDecoration: "none", color: "white" }}
              >
                <img src="../resources/images/ic_dashboard_white_48px.svg" />
                <span>Dashboard</span>
              </Link>
            </div>
            <div>
              <Link
                to="/message"
                style={{ textDecoration: "none", color: "white" }}
              >
                <img src="../resources/images/ic_message_white_48px.svg" />
                <span>Message</span>
              </Link>
            </div>
            <div>
              <Link
                to="/logicsets"
                style={{ textDecoration: "none", color: "white" }}
              >
                <img src="../resources/images/ic_device_hub_white_48px.svg" />
                <span>Logic Sets</span>
              </Link>
            </div>
            <div>
              <Link
                to="/customlogicsets"
                style={{ textDecoration: "none", color: "white" }}
              >
                <img src="../resources/images/ic_gamepad_white_48px.svg" />
                <span>Custom Logic Sets</span>
              </Link>
            </div>
            <div>
              <Link
                to="/help"
                style={{ textDecoration: "none", color: "white" }}
              >
                <img src="../resources/images/ic_help_white_48px.svg" />
                <span>Help/FAQs</span>
              </Link>
            </div>
          </div>
          <div className="custom-article">
            <div className="custom-content">
              {this.props.children}
            </div>
            <nav className="navbar navbar-default navbar-fixed-bottom custom-footer">
              <div className="container">
              Copyright@2017 www.wipro.com
            </div>
            </nav>
          </div>

        </div>
      </div>
    );
  }
}
export default Home;
