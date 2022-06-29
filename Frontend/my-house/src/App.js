import "./App.css";
import Header from "./Components/Header";
import {
  BrowserRouter as Router,
  Route,
  Routes,
  Navigate,
} from "react-router-dom";
import Home from "./Components/Home";
import Group from "./Components/Group";
import Object from "./Components/Object";
import Groups from "./Components/Groups";
import Login from "./Components/Login";
import AuthService from "./Services/AuthService";
import Device from "./Components/Device";
import {NotificationContainer} from 'react-notifications';
import 'react-notifications/lib/notifications.css';
import Report from "./Components/Report";

function App() {
  const ProtectedRoute = ({ children }) => {
    if (!AuthService.getUser()) {
      return <Navigate to="/" replace />;
    }

    return children;
  };

  return (
    <Router>
      <NotificationContainer/>
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
            path="/report"
            element={
              <ProtectedRoute>
                <Report />
              </ProtectedRoute>
            }
          ></Route>
        </Routes>
      </div>
    </Router>
  );
}

export default App;
