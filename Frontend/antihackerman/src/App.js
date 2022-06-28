import "./App.css";
import Header from "./Components/Header";
import {
  BrowserRouter as Router,
  Route,
  Routes,
  Navigate,
} from "react-router-dom";
import Home from "./Components/Home";
import Certificates from "./Components/Certificates";
import CSRs from "./Components/CSRs";
import NewCSR from "./Components/NewCSR";
import Group from "./Components/Group";
import Groups from "./Components/Groups";
import Login from "./Components/Login";
import AuthService from "./Services/AuthService";
import Object from "./Components/Object";
import NewAnalogDevice from "./Components/newAnalogDevice";
import NewDigitalDevice from "./Components/newDigitalDevice";
import Logs from "./Components/Logs";
import { NotificationContainer } from "react-notifications";
import "react-notifications/lib/notifications.css";
import Device from "./Components/Device";
import NewAlarm from "./Components/newAlarm";

function App() {
  const ProtectedRoute = ({ children }) => {
    if (!AuthService.getUser()) {
      return <Navigate to="/" replace />;
    }

    return children;
  };

  return (
    <Router>
      <NotificationContainer />
      <div className="App">
        <Routes>
          <Route
            exact
            path="/home"
            element={
              <ProtectedRoute>
                <Home />
              </ProtectedRoute>
            }
          ></Route>
          <Route exact path="/" element={<Login></Login>}></Route>
          <Route
            exact
            path="/groups"
            element={
              <ProtectedRoute>
                <Groups />
              </ProtectedRoute>
            }
          ></Route>
          <Route
            exact
            path="/group/:id"
            element={
              <ProtectedRoute>
                <Group />
              </ProtectedRoute>
            }
          ></Route>
          <Route
            exact
            path="/object/:id"
            element={
              <ProtectedRoute>
                <Object />
              </ProtectedRoute>
            }
          ></Route>
          <Route
            exact
            path="/device/:id"
            element={
              <ProtectedRoute>
                <Device />
              </ProtectedRoute>
            }
          ></Route>
          <Route
            exact
            path="/device/newAlarm/:id/:type"
            element={
              <ProtectedRoute>
                <NewAlarm />
              </ProtectedRoute>
            }
          ></Route>
          <Route
            exact
            path="object/:id/newAnalogDevice/"
            element={
              <ProtectedRoute>
                <NewAnalogDevice />
              </ProtectedRoute>
            }
          ></Route>
          <Route
            exact
            path="object/:id/newDigitalDevice/"
            element={
              <ProtectedRoute>
                <NewDigitalDevice />
              </ProtectedRoute>
            }
          ></Route>
          <Route
            exact
            path="/certificates"
            element={
              <ProtectedRoute>
                <Certificates />
              </ProtectedRoute>
            }
          ></Route>
          <Route
            exact
            path="/CSRs"
            element={
              <ProtectedRoute>
                <CSRs />
              </ProtectedRoute>
            }
          ></Route>
          <Route
            exact
            path="/logs"
            element={
              <ProtectedRoute>
                <Logs />
              </ProtectedRoute>
            }
          ></Route>
          <Route exact path="/newCSR" element={<NewCSR></NewCSR>}></Route>
        </Routes>
      </div>
    </Router>
  );
}

export default App;
