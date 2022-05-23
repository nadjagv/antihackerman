import "./App.css";
import Header from "./Components/Header";
import { BrowserRouter as Router, Route, Routes } from "react-router-dom";
import Home from "./Components/Home";
import Certificates from "./Components/Certificates";
import CSRs from "./Components/CSRs";
import NewCSR from "./Components/NewCSR";
import Group from "./Components/Group";
import Groups from "./Components/Groups";
import Login from "./Components/Login";

function App() {
  return (
    <Router>
      <div className="App">
        <Header></Header>
        <Routes>
          <Route exact path="/home" element={<Home></Home>}></Route>
          <Route exact path="/" element={<Login></Login>}></Route>
          <Route exact path="/groups" element={<Groups></Groups>}></Route>
          <Route exact path="/group/:id" element={<Group></Group>}></Route>
          <Route
            exact
            path="/certificates"
            element={<Certificates></Certificates>}
          ></Route>
          <Route exact path="/CSRs" element={<CSRs></CSRs>}></Route>
          <Route exact path="/newCSR" element={<NewCSR></NewCSR>}></Route>
        </Routes>
      </div>
    </Router>
  );
}

export default App;
