import "./App.css";
import Header from "./Components/Header";
import { BrowserRouter as Router, Route, Routes } from "react-router-dom";
import Home from "./Components/Home";
import Certificates from "./Components/Certificates";

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
        </Routes>
      </div>
    </Router>
  );
}

export default App;
