import React from 'react';
import { Navbar, Nav, NavDropdown} from 'react-bootstrap';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faHome } from '@fortawesome/free-solid-svg-icons';
import { withRouter } from "react-router-dom";

class NavigationBar extends React.Component {

    constructor(props) {
        super(props);
        this.goHome = this.goHome.bind(this)
    }

    goHome()
    {
        this.props.history.push("/home")
    }

    render() {
        return (
          <Navbar bg="light" expand="lg">
              <Navbar.Brand><FontAwesomeIcon icon={faHome}/>{' '}myRPO</Navbar.Brand>
              <Navbar.Toggle aria-controls="basic-navbar-nav" />
              <Navbar.Collapse id="basic-navbar-nav">
                  <Nav className="mr-auto">
                      <Nav.Link href="/home/">Home</Nav.Link>
                      <Nav.Link onClick={this.goHome}>Another home</Nav.Link>
                      <Nav.Link onClick={()=>{this.props.history.push("/home")}}>Yet another home</Nav.Link>
                  </Nav>
              </Navbar.Collapse>
          </Navbar>
        );
    }
}

export default withRouter(NavigationBar);