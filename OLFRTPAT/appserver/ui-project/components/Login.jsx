import React from "react";
import { Button } from "react-bootstrap";
import { Link } from "react-router";
import Request from "superagent";

class Login extends React.Component {
  constructor(props) {
    super(props);
  }

  validateLogin() {
    let self = this;
    var user = document.getElementById("usr").value;
    var pwd = document.getElementById("pwd").value;
    console.log(user, pwd);
    Request.post("http://127.0.0.1:5000/login")
      .send({ userName: user, password: pwd })
      .set("Content-Type", "application/json;charset=utf-8")
      .end(function(err, res) {
        if (err) {
          console.log("error");
        } else {
          if (res.text == "Login Success") {
            self.props.router.push("/dashboard");
          } else {
            document.getElementById("message").innerHTML = "Login Failed!";
          }
        }
      });
  }

  render() {
    return (
      <div className="bg-image">
        <img src="https://ld-wp.template-help.com/woocommerce_58921/wp-content/uploads/2017/02/home_section_bg.jpg" alt="background-image" class="bg_image" />
        <h1 className="welcome">Welcome to online training!</h1>
        <div className="container custom-align login-background">
          {/* <h2 className="style-head">Sign In!</h2> */}
          <form>
            <div className="form-group">
              <label className="style-text">Username:</label>
              <input type="email" className="form-control" id="usr" />
            </div>
            <div className="form-group">
              <label className="style-text">Password:</label>
              <input type="password" className="form-control" id="pwd" />
            </div>
            <div className="wrapper-button">
            <button
              type="submit"
              className="btn btn-success btn-login"
              onClick={this.validateLogin.bind(this)}
            >
              Sign In
            </button>
          </div>
            <div id="message" />
          </form>
        </div>
    </div>
    );
  }
}
export default Login;
