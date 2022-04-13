import "./App.css";
import Header from "./Components/Header";
import { BrowserRouter as Router, Route, Routes } from "react-router-dom";
import Home from "./Components/Home";
import Certificates from "./Components/Certificates";
import CSRs from "./Components/CSRs";
import NewCSR from "./Components/NewCSR";

function App() {
  return (
    <Router>
      <div className="App">
        <Header></Header>
        <Routes>
          <Route exact path="/" element={<Home></Home>}></Route>
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
