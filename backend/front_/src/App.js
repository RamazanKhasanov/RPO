import React from 'react';
import './App.css';
import { Router, Switch, Route } from "react-router-dom";
import { createBrowserHistory } from 'history';
import NavigationBar from "./components/NavigationBar";
import Home from "./components/Home";

function App() {
  return (
      <div className = "App">
          <Router history = { createBrowserHistory() }>
              <NavigationBar/>
              <div className="container-fluid">
                  <Switch>
                      <Route path="/home/" exact component={Home} />
                  </Switch>
              </div>
        </Router>
      </div>
  );
}

export default App;