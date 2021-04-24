import React from 'react';
import { Navbar, Nav} from 'react-bootstrap';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faHome } from '@fortawesome/free-solid-svg-icons';
import {Link, withRouter} from "react-router-dom";
import {faUser} from "@fortawesome/free-solid-svg-icons/faUser";
import Utils from "../utils/Utils";
import BackendService from "../services/BackendService";

class NavigationBar extends React.Component {

    constructor(props) {
        super(props);
        this.goHome = this.goHome.bind(this)
        this.logout = this.logout.bind(this);
    }

    goHome()
    {
        this.props.history.push("/home")
    }

    logout(){
        BackendService.logout().finally(() => {
            Utils.removeUser();
            this.goHome();
        })
    }

    render() {
        let uname = Utils.getUserName();
        return (
          <Navbar bg="light" expand="lg">
              <Navbar.Brand><FontAwesomeIcon icon={faHome}/>{' '}myRPO</Navbar.Brand>
              <Navbar.Toggle aria-controls="basic-navbar-nav" />
              <Navbar.Collapse id="basic-navbar-nav">
                  <Nav className="mr-auto">
                      {/*<Nav.Link href="/home">Home</Nav.Link>*/}
                      <Nav.Link as={Link} to="/home">Home</Nav.Link>
                      <Nav.Link onClick={this.goHome}>Another home</Nav.Link>
                      <Nav.Link onClick={()=>{this.props.history.push("/home")}}>Yet another home</Nav.Link>
                  </Nav>
              </Navbar.Collapse>
              <Navbar.Text>{uname}</Navbar.Text>
              {uname &&
                <Nav.Link onClick={this.logout}><FontAwesomeIcon icon={faUser} fixedWidth/>{' '}Выход</Nav.Link>
              }
              {!uname &&
                 <Nav.Link as={Link} to="/login"><FontAwesomeIcon icon={faUser} fixedWidth/>{' '}Вход</Nav.Link>
              }
          </Navbar>
        );
    }
}

export default withRouter(NavigationBar)